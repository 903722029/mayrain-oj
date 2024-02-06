<script setup lang="ts">
import { reactive, ref } from "vue";
import MDEditor from "@/components/MDEditor.vue";
import { QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRoute } from "vue-router";
import { onMounted } from "@vue/runtime-dom";

const route = useRoute();
const updatePage = route.path.includes("update");

const form = ref({
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
/**
 * 根据题目 id 获取旧数据
 */
const loadData = async () => {
  const id = route.query.id;
  if (!id) {
    return;
  }
  const res = await QuestionControllerService.getQuestionByIdUsingGet(
    id as any
  );
  if (res.code === 0) {
    form.value = res.data as any;
    if (form.value.judgeCase) {
      form.value.judgeCase = JSON.parse(form.value.judgeCase as any);
    }
    if (form.value.tags) {
      form.value.tags = JSON.parse(form.value.tags as any);
    }
    if (form.value.judgeConfig) {
      form.value.judgeConfig = JSON.parse(form.value.judgeConfig as any);
    }
  } else {
    message.error("查询错误");
  }
};

onMounted(() => {
  loadData();
});
const handleAdd = () => {
  form.value.judgeCase.push({
    inputCase: "",
    outputCase: "",
  });
};
const handleDelete = (index: number) => {
  form.value.judgeCase.splice(index, 1);
};

const doSubmit = async () => {
  console.log(form.value);
  // 判断更新页面or新增页面
  if (updatePage) {
    const res = await QuestionControllerService.updateQuestionUsingPost(
      form.value
    );
    if (res.code === 0) {
      message.success("更新成功");
    } else {
      message.success("更新失败");
    }
  } else {
    const res = await QuestionControllerService.addQuestionUsingPost(
      form.value
    );
    if (res.code === 0) {
      message.success("创建成功");
    } else {
      message.error("创建失败");
    }
  }
};

const onContentChange = (content: string) => {
  form.value.content = content;
};

const onAnswerChange = (answer: string) => {
  form.value.answer = answer;
};
</script>

<template>
  <div id="addQuestionView">
    <h2>创建题目</h2>
    <a-form :model="form">
      <a-form-item field="title" label="标题">
        <a-input v-model="form.title" placeholder="请输入标题" />
      </a-form-item>
      <a-form-item field="tags" label="标签">
        <a-input-tag
          :style="{ width: '320px' }"
          v-model="form.tags"
          placeholder="请输入标签"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="content" label="题目内容">
        <MDEditor
          :md-value="form.content"
          :md-handle-change="onContentChange"
        />
      </a-form-item>
      <a-form-item field="answer" label="题目答案">
        <MDEditor :md-value="form.answer" :md-handle-change="onAnswerChange" />
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
      <a-space direction="vertical" fill style="min-width: 480px">
        <a-form-item label="判题配置" :merge-props="false">
          <a-form :model="form" :style="{ width: '600px' }">
            <a-form-item
              v-for="(judgeCase, index) of form.judgeCase"
              :field="`judgeCases[${index}].value`"
              :label="`判题用例-${index}`"
              :key="index"
            >
              <a-input
                v-model="judgeCase.inputCase"
                placeholder="请输入输入用例"
              />
              <a-input
                v-model="judgeCase.outputCase"
                placeholder="请输入输出用例"
              />
              <a-button
                @click="handleDelete(index)"
                :style="{ marginLeft: '10px' }"
                status="danger"
                >删除用例
              </a-button>
            </a-form-item>
          </a-form>
        </a-form-item>
      </a-space>
      <a-form-item>
        <a-button @click="handleAdd" type="outline" status="success"
          >添加用例
        </a-button>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSubmit">提交</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<style scoped></style>
