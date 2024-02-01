<template>
  <div class="home">
    <CodeEditor code-value="codeValue" :code-handle-change="codeHandleChange" />
    <MDEditor :md-value="mdValue" :md-handle-change="mdHandleChange" />
  </div>
</template>

<script setup lang="ts">
import { defineComponent, ref } from "vue";
import MDEditor from "@/components/MDEditor.vue";
import CodeEditor from "@/components/CodeEditor.vue"; // @ is an alias to /src
const codeValue = ref("");
const mdValue = ref("");
const codeHandleChange = (v: string) => {
  codeValue.value = v;
  console.log(v);
};
const mdHandleChange = (v: string) => {
  mdValue.value = v;
  console.log(v);
};
</script>
