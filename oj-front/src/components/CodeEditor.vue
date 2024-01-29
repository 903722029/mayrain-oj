<script setup lang="ts">
import * as monaco from "monaco-editor";
import { Editor } from "@bytemd/vue-next";
import { ref } from "vue";
import { onMounted, toRaw, withDefaults, defineProps } from "@vue/runtime-dom";

interface Props {
  codeValue: string;
  codeHandleChange: (v: string) => void;
}

const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  codeHandleChange: (v: string) => {
    console.log(v);
  },
});
const codeEditorRef = ref();
const codeEditor = ref();
onMounted(() => {
  if (!codeEditorRef.value) {
    return;
  }
  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    value: props.value,
    language: "java",
    automaticLayout: true,
    theme: "vs-dark",
    colorDecorators: true,
    minimap: {
      enabled: true,
    },
  });
  codeEditor.value.onDidChangeModelContent(() => {
    props.codeHandleChange(toRaw(codeEditor.value).getValue());
  });
});
</script>

<template>
  <div id="code-editor" ref="codeEditorRef" style="min-height: 400px" />
</template>

<style scoped></style>
