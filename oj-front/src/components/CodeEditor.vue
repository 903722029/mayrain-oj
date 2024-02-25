<script setup lang="ts">
import * as monaco from "monaco-editor";
import { Editor } from "@bytemd/vue-next";
import { ref } from "vue";
import {
  onMounted,
  toRaw,
  withDefaults,
  defineProps,
  watch,
} from "@vue/runtime-dom";

interface Props {
  codeValue: string; // 使用codeValue替代value
  language: string;
  codeHandleChange: (v: string) => void;
}

// 使用codeValue，确保与interface一致
const props = withDefaults(defineProps<Props>(), {
  codeValue: "", // 使用codeValue
  language: "java",
  codeHandleChange: (v: string) => {
    console.log();
  },
});

const codeEditorRef = ref();
const codeEditor = ref();

// 监听语言变化
watch(
  () => props.language,
  (newLanguage, oldLanguage) => {
    if (codeEditor.value && newLanguage !== oldLanguage) {
      // 当语言发生变化时，更新 Monaco 编辑器的模型语言
      monaco.editor.setModelLanguage(codeEditor.value.getModel(), newLanguage);
    }
  }
);
onMounted(() => {
  console.log(props.language);
  debugger;
  if (!codeEditorRef.value) return;
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
