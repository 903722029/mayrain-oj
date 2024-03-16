package com.mayrain.ojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.mayrain.ojcodesandbox.model.ExecuteCmdMessage;
import com.mayrain.ojcodesandbox.model.ExecuteCodeRequest;
import com.mayrain.ojcodesandbox.model.ExecuteCodeResponse;
import com.mayrain.ojcodesandbox.model.JudgeInfo;
import com.mayrain.ojcodesandbox.utils.ProcessUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @Author: Si Yutong
 * @Date: 2024-03-02
 * @Description:
 **/
public class JavaNativeCodeSandBoxOld implements CodeSandBox{
    public static final String GLOBAL_CODE_DIR_NAME = "tmpCode";
    public static final String USER_CODE_NAME = "Main.java";
    public static final long TIME_OUT = 5000L;
    public static final List<String> blacklist = Arrays.asList("Files", "exec");

    public static final WordTree WORD_TREE;
    static {
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(blacklist);
    }
    public static void main(String[] args) {
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "3 4"));
        executeCodeRequest.setLanguage("java");
        String code = ResourceUtil.readStr("testCode/danger/ReadFile.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        JavaNativeCodeSandBoxOld javaNativeCodeSandBoxOld = new JavaNativeCodeSandBoxOld();
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandBoxOld.excuteCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    @Override
    public ExecuteCodeResponse excuteCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String language = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();

        // 检验代码
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if (foundWord != null) {
            System.out.println(foundWord.getFoundWord());
            return null;
        }
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
        // 2. 执行编译指令
        String compileCommand = String.format("javac -encoding UTF-8 %s", userCodeFile.getAbsolutePath());
        ExecuteCmdMessage executeCmdMessage = null;
        try {
            Process process = Runtime.getRuntime().exec(compileCommand);
            executeCmdMessage = ProcessUtils.executeCmd(process, "编译");
        } catch (InterruptedException | IOException e) {
            return getErrorResponse(e);
        }
        System.out.println(executeCmdMessage);
        // 3. 运行代码
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
                executeCmdMessage = ProcessUtils.executeCmd(process, "运行");
            } catch (InterruptedException | IOException e) {
                return getErrorResponse(e);
            }
            executeCmdMessageList.add(executeCmdMessage);
        }
        // 4. 获取结果
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
        // 5. 文件清理
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
        }
        return executeCodeResponse;
    }

    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        executeCodeResponse.setMessage(e.getMessage());
        executeCodeResponse.setStatus(2);
        return executeCodeResponse;
    }
}
