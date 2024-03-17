package com.mayrain.ojcodesandbox;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.mayrain.ojcodesandbox.model.ExecuteCmdMessage;
import com.mayrain.ojcodesandbox.model.ExecuteCodeRequest;
import com.mayrain.ojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Si Yutong
 * @Date: 2024-03-02
 * @Description:
 **/
@Component
public class JavaDockerCodeSandBox extends JavaCodeSandBoxTemplate{
    public static final long TIME_OUT = 5000L;
    public static final Boolean FIRST_INIT = true;

    public static void main(String[] args) {
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "3 4"));
        executeCodeRequest.setLanguage("java");
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        JavaDockerCodeSandBox javaNativeCodeSandBox = new JavaDockerCodeSandBox();
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandBox.excuteCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    /**
     * docker 实现运行代码文件
     * @param userCodeFile
     * @param inputList 输入
     * @return
     */
    @Override
    public List<ExecuteCmdMessage> runFile(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        DockerClient dockerClient  = DockerClientBuilder.getInstance().build();
        String image = "openjdk:8-alpine";
        if (FIRST_INIT) {
            PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
            PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                @Override
                public void onNext(PullResponseItem item) {
                    System.out.println("下载镜像：" + item.getStatus());
                    super.onNext(item);
                }
            };
            try {
                pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
            } catch (InterruptedException e) {
                System.out.println("下载镜像失败");
                throw new RuntimeException(e);
            }
        }
        // 创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(100 * 1000 * 1000L);
        hostConfig.withMemorySwap(1L);
        hostConfig.withCpuCount(1L);
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/app")));
        CreateContainerResponse containerResponse = containerCmd.withHostConfig(hostConfig)
                .withNetworkDisabled(true).withReadonlyRootfs(true).withAttachStderr(true)
                .withAttachStdin(true).withAttachStdout(true).withTty(true).exec();
        String containerId = containerResponse.getId();
        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();

        // 执行
        List<ExecuteCmdMessage> executeCmdMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            String[] inpuptArgsArray = inputArgs.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"}, inpuptArgsArray);
            ExecuteCmdMessage executeCmdMessage = new ExecuteCmdMessage();
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId).withCmd(cmdArray).withAttachStderr(true).withAttachStdin(true)
                    .withAttachStdout(true).exec();
            System.out.println("创建执行命令为：" + execCreateCmdResponse);
            String execId = execCreateCmdResponse.getId();
            final String[] message = new String[1];
            final String[] errorMessage = new String[1];
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)) {
                        message[0] = "输出错误错误：" + new String(frame.getPayload());
                    } else {
                        errorMessage[0] = "输出结果：" + new String(frame.getPayload());
                    }
                    super.onNext(frame);
                }
            };
            // 获取内存
            final long[] maxMemory = {0L};
            final boolean[] timeout = {true};
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> resultCallback = new ResultCallback<Statistics>() {
                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onNext(Statistics statistics) {
                    System.out.println("内存占用: " + statistics.getMemoryStats().getUsage());
                    maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {
                    timeout[0] = false;
                }

                @Override
                public void close() throws IOException {

                }
            };
            statsCmd.exec(resultCallback);
            statsCmd.close();
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            try {
                dockerClient.execStartCmd(execId).exec(execStartResultCallback).awaitCompletion(TIME_OUT, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.out.println("执行命令失败");
                throw new RuntimeException(e);
            }
            stopWatch.stop();
            long time = stopWatch.getLastTaskTimeMillis();
            executeCmdMessage.setExecuteMessage(message[0]);
            executeCmdMessage.setErrorMessage(errorMessage[0]);
            executeCmdMessage.setTime(time);
            executeCmdMessage.setMemory(maxMemory[0]);
            executeCmdMessageList.add(executeCmdMessage);
        }
        return executeCmdMessageList;
    }
}
