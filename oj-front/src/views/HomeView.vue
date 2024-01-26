<template>
  <div class="home">
    <MDEditor :value="value" :handle-change="handleChange" />
    <img alt="Vue logo" src="../assets/logo.png" />
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App" />
  </div>
</template>

<script setup lang="ts">
import { defineComponent, ref } from "vue";
import HelloWorld from "@/components/HelloWorld.vue";
import MDEditor from "@/components/MDEditor.vue"; // @ is an alias to /src
const value = ref("");
const handleChange = (v: string) => {
  value.value = v;
};
</script>
