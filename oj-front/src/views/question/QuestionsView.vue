<script setup lang="ts">
import { reactive, ref } from "vue";
import { onMounted, watchEffect } from "@vue/runtime-dom";
import { Question, QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";

onMounted(() => {
  loadData();
});
const dataList = ref([]);
const total = ref(0);
const searchParams = ref({
  title: "",
  tags: [],
  pageSize: 10,
  current: 1,
});
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionVoByPageUsingPost(
    searchParams.value
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
    console.log("dataList", dataList.value);
  } else {
    message.error("查询失败");
  }
};
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
  loadData();
};
const router = useRouter();
const toQuestionPage = (question: Question) => {
  router.push({
    path: `/view/question/${question.id}`,
  });
};

const doSearch = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
  loadData();
};
const columns = [
  {
    title: "题号",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "标签",
    slotName: "tags",
  },
  {
    title: "通过率",
    slotName: "acceptedRate",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    slotName: "optional",
  },
];
</script>

<template>
  <div id="questionsView">
    <a-form :model="searchParams" :layout="'inline'">
      <a-form-item field="title" label="标题">
        <a-input
          v-model="searchParams.title"
          placeholder="请输入标题"
          style="min-width: 240px"
        />
      </a-form-item>
      <a-form-item field="tags" label="标签">
        <a-input-tag
          v-model="searchParams.tags"
          placeholder="请输入标签"
          style="min-width: 240px"
        />
      </a-form-item>
      <a-form-item>
        <a-button @click="doSearch" type="primary">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider :size="2" style="border-bottom-style: dotted" />
    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total: total,
        showTotal: true,
      }"
      @page-change="onPageChange"
    >
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag
            v-for="(tag, index) of record.tags"
            :key="index"
            color="green"
            closable
            >{{ tag }}
          </a-tag>
        </a-space>
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD") }}
      </template>
      <template #acceptedRate="{ record }">
        {{
          `${
            record.submissionNum ? record.passNum / record.submissionNum : 0
          }% (${record.passNum}/${record.submissionNum})`
        }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="toQuestionPage(record)"
            >DO
          </a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<style scoped>
#questionsView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
