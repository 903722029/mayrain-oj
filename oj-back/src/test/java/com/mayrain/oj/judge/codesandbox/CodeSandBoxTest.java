package com.mayrain.oj.judge.codesandbox;

import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.mayrain.oj.judge.codesandbox.model.ExecuteCodeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Si Yutong
 * @Date: 2024-02-25
 * @Description:
 **/
@SpringBootTest
class CodeSandBoxTest {
    @Value("${codeSandBox.type:example}")
    private String type;
    @Test
    void testCodeSandBoxByValue() {
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        String language = "java";
        String code = "int main(){}";
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        codeSandBox.excuteCode(executeCodeRequest);
    }

    @Test
    void testCodeSandBoxByProxy() {
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);
        String language = "java";
        String code = "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = Integer.parseInt(args[0]);\n" +
                "        int b = Integer.parseInt(args[1]);\n" +
                "        System.out.println(\"结果是：\" + (a + b));\n" +
                "    }\n" +
                "}";
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.excuteCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
//            String type = scanner.next();
//            CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
//            String language = "java";
//            String code = "int main(){}";
//            List<String> inputList = Arrays.asList("1 2", "3 4");
//            ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
//                    .code(code)
//                    .language(language)
//                    .inputList(inputList)
//                    .build();
//            codeSandBox.excuteCode(executeCodeRequest);
//        }
//    }
}
