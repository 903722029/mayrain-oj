<script setup lang="ts">
import { reactive } from "vue";
import MDEditor from "@/components/MDEditor.vue";

const form = reactive({
  answer: "",
  content: "",
  tags: [],
  title: "",
  judgeConfig: {
    memoryLimit: 0,
    stackLimit: 0,
    timeLimit: 0,
  },
  judgeCase: [
    {
      inputCase: "",
      outputCase: "",
    },
  ],
});
</script>

<template>
  <div id="addQuestionView"></div>
  <a-form :model="form">
    <a-form-item field="answer" label="题目答案">
      <MDEditor />
    </a-form-item>
    <a-form-item field="content" label="题目内容">
      <MDEditor />
    </a-form-item>
    <a-form-item field="title" label="标题">
      <a-input v-model="form.title" placeholder="请输入标题" />
    </a-form-item>
    <a-form-item field="tags" label="标签">
      <a-input-tag
        :style="{ width: '320px' }"
        placeholder="请输入标签"
        allow-clear
      />
    </a-form-item>
    <a-form-item label="判题配置" :merge-props="false">
      <a-space direction="vertical" fill style="min-width: 480px">
        <a-form-item field="judgeConfig.memoryLimit" label="内存限制">
          <a-input
            v-model="form.judgeConfig.memoryLimit"
            placeholder="请输入内存限制"
          />
        </a-form-item>
        <a-form-item field="judgeConfig.timeLimit" label="时间限制">
          <a-input
            v-model="form.judgeConfig.timeLimit"
            placeholder="请输入时间限制"
          />
        </a-form-item>
        <a-form-item field="judgeConfig.stackLimit" label="堆栈限制">
          <a-input
            v-model="form.judgeConfig.stackLimit"
            placeholder="请输入堆栈限制"
          />
        </a-form-item>
      </a-space>
    </a-form-item>
    <a-form :model="form" :style="{width:'600px'}">
      <a-form-item v-for="(judgeCase,index) of form.judgeCase" :field="`posts[${index}].value`" :label="`Post-${index}`" :key="index">
        <a-input v-model="judgeCase.inputCase" placeholder="请输入输入用例" />
        <a-input v-model="judgeCase.outputCase" placeholder="请输入输出用例" />
        <a-button @click="handleDelete(index)" :style="{marginLeft:'10px'}">Delete</a-button>
      </a-form-item>
    </a-form>
    <a-form-item>
      <a-button>Submit</a-button>
    </a-form-item>
  </a-form>
</template>

<style scoped></style>
