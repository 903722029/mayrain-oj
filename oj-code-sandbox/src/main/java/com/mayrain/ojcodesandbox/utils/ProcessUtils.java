package com.mayrain.ojcodesandbox.utils;

import cn.hutool.core.date.StopWatch;
import com.mayrain.ojcodesandbox.model.ExecuteCmdMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: Si Yutong
 * @Date: 2024-03-03
 * @Description:
 **/
public class ProcessUtils {
    public static ExecuteCmdMessage executeCmd(Process process, String opName) throws InterruptedException, IOException {
        ExecuteCmdMessage executeCmdMessage = new ExecuteCmdMessage();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        int exitValue = process.waitFor();
        executeCmdMessage.setExitValue(exitValue);
        if (exitValue == 0) {
            System.out.println(opName + "成功");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String outputLine;
            StringBuilder outputStringBuilder = new StringBuilder();
            while (((outputLine = bufferedReader.readLine()) != null)) {
                outputStringBuilder.append(outputLine);
            }
            executeCmdMessage.setExecuteMessage(outputStringBuilder.toString());
        } else {
            System.out.println(opName + "失败，错误码：" + exitValue);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorOutputLine;
            StringBuilder errorStringBuilder = new StringBuilder();
            while (((errorOutputLine = bufferedReader.readLine()) != null)) {
                errorStringBuilder.append(errorOutputLine);
            }
            executeCmdMessage.setErrorMessage(errorStringBuilder.toString());
        }
        stopWatch.stop();
        long time = stopWatch.getLastTaskTimeMillis();
        executeCmdMessage.setTime(time);
        return executeCmdMessage;
    }

}
