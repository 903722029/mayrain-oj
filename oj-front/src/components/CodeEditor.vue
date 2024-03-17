<script setup lang="ts">
import * as monaco from "monaco-editor";
import { Editor } from "@bytemd/vue-next";
import { ref, watch } from "vue";
import { onMounted, toRaw, withDefaults, defineProps } from "@vue/runtime-dom";

interface Props {
  value: string;
  language?: string;
  codeHandleChange: (v: string) => void;
}

const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  language: () => "java",
  codeHandleChange: (v: string) => {
    console.log(v);
  },
});

const codeEditorRef = ref();
const codeEditor = ref();

// 监听语言变化
watch(
  () => props.language,
  () => {
    console.log(props.language);
    if (codeEditor.value) {
      // 当语言发生变化时，更新 Monaco 编辑器的模型语言
      monaco.editor.setModelLanguage(
        toRaw(codeEditor.value).getModel(),
        props.language
      );
    }
  }
);
onMounted(() => {
  console.log(props.language);
  if (!codeEditorRef.value) {
    return;
  }
  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    value: props.codeValue, // 确保这里使用codeValue
    language: props.language,
    automaticLayout: true,
    theme: "vs-dark",
    minimap: { enabled: true },
  });
  codeEditor.value.onDidChangeModelContent(() => {
    props.codeHandleChange(toRaw(codeEditor.value).getValue());
  });
});
</script>

<template>
  <div
    id="code-editor"
    ref="codeEditorRef"
    style="min-height: 400px; height: 60vh"
  />
</template>

<style scoped></style>
