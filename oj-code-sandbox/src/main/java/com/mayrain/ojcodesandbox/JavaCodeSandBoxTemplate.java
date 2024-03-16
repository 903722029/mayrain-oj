package com.mayrain.ojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.mayrain.ojcodesandbox.model.ExecuteCmdMessage;
import com.mayrain.ojcodesandbox.model.ExecuteCodeRequest;
import com.mayrain.ojcodesandbox.model.ExecuteCodeResponse;
import com.mayrain.ojcodesandbox.model.JudgeInfo;
import com.mayrain.ojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: Si Yutong
 * @Date: 2024-03-16
 * @Description:
 **/
@Slf4j
public abstract class JavaCodeSandBoxTemplate implements CodeSandBox {
    public static final String GLOBAL_CODE_DIR_NAME = "tmpCode";
    public static final String USER_CODE_NAME = "Main.java";
    public static final long TIME_OUT = 5000L;
    /**
     * 代码保存为文件
     * @param code 代码
     * @return 文件
     */
    public File saveCodeToFile(String code) {
        // 全局用户代码存放位置
        String userDir = System.getProperty("user.dir");
        String globalCodeDir = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        if (!FileUtil.exist(globalCodeDir)) {
            FileUtil.mkdir(globalCodeDir);
        }
        // 每个用户代码分别存放
        String userCodeParentPath = globalCodeDir + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + USER_CODE_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

    /**
     * 执行编译指令
     * @param userCodeFile 代码文件
     * @return 执行结果
     */
    public ExecuteCmdMessage compileCode(File userCodeFile) {
        String compileCommand = String.format("javac -encoding UTF-8 %s", userCodeFile.getAbsolutePath());
        ExecuteCmdMessage executeCmdMessage = null;
        try {
            Process process = Runtime.getRuntime().exec(compileCommand);
            executeCmdMessage = ProcessUtils.executeCmd(process, "编译");
            if (executeCmdMessage.getExitValue() != 0) {
                throw new RuntimeException("编译错误");
            }
            return executeCmdMessage;
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行代码
     * @param inputList 输入
     * @return
     */
    public List<ExecuteCmdMessage> runFile(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();

        List<ExecuteCmdMessage> executeCmdMessageList = new ArrayList<>();
        for (String input : inputList) {
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, input);
            try {
                Process process = Runtime.getRuntime().exec(runCmd);
                // 创建一个线程，指定超时时间，如果超时时间结束发现执行代码依然没有结束，则说明程序超时
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        System.out.println("超时了");
                        process.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteCmdMessage executeCmdMessage = ProcessUtils.executeCmd(process, "运行");
                executeCmdMessageList.add(executeCmdMessage);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException("程序执行异常");
            }
        }
        return executeCmdMessageList;
    }

    /**
     * 获取输出结果
     * @param executeCmdMessageList
     * @return
     */
    public ExecuteCodeResponse getOutput(List<ExecuteCmdMessage> executeCmdMessageList) {
        List<String> outputList = new ArrayList<>();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        long maxTime = 0L;
        for (ExecuteCmdMessage cmdMessage : executeCmdMessageList) {
            if (StrUtil.isNotEmpty(cmdMessage.getErrorMessage())) {
                executeCodeResponse.setStatus(3);
                executeCodeResponse.setMessage(cmdMessage.getErrorMessage());
                break;
            }
            outputList.add(cmdMessage.getExecuteMessage());
            if (cmdMessage.getTime() != null) {
                maxTime = Math.max(cmdMessage.getTime(), maxTime);
            }
        }
        if (outputList.size() == executeCmdMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 清理文件
     * @param userCodeFile
     * @return
     */
    public boolean clearFile(File userCodeFile) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
            return del;
        }
        return true;
    }

    @Override
    public ExecuteCodeResponse excuteCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String language = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();

        File userCodeFile = saveCodeToFile(code);
        ExecuteCmdMessage executeCmdMessage = compileCode(userCodeFile);
        System.out.println(executeCmdMessage);
        // 3. 运行代码
        List<ExecuteCmdMessage> executeCmdMessageList = runFile(userCodeFile, inputList);
        // 4. 获取结果
        ExecuteCodeResponse executeCodeResponse = getOutput(executeCmdMessageList);
        // 5. 文件清理
        boolean clear = clearFile(userCodeFile);
        if (!clear) {
            log.error("delete file error, userCodeFilePath = {}", userCodeFile.getAbsolutePath());
        }
        return executeCodeResponse;
    }
}
