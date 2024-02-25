package com.mayrain.oj.judge.codesandbox;

import com.mayrain.oj.judge.codesandbox.impl.ExampleCodeSandBox;
import com.mayrain.oj.judge.codesandbox.impl.RemoteCodeSandBox;
import com.mayrain.oj.judge.codesandbox.impl.ThirdPartyCodeSandBox;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description:
 **/
public class CodeSandBoxFactory {
    public static CodeSandBox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandBox();
            case "remote":
                return new RemoteCodeSandBox();
            case "thirdParty":
                return new ThirdPartyCodeSandBox();
            default:
                return new ExampleCodeSandBox();
        }
    }
}
