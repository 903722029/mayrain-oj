<script setup lang="ts">
import { reactive, ref } from "vue";
import { onMounted } from "@vue/runtime-dom";
import { QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";

onMounted(() => {
  loadData();
});

const searchParams = ref({
  pageSize: 10,
  current: 1,
});
const dataList = ref([]);
const total = ref(0);
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionByPageUsingPost(
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

const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "内容",
    dataIndex: "content",
  },
  {
    title: "标签",
    dataIndex: "tags",
  },
  {
    title: "判题配置",
    dataIndex: "judgeConfig",
  },
  {
    title: "判题用例",
    dataIndex: "judgeCase",
  },
  {
    title: "用户id",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "Optional",
    slotName: "optional",
  },
];
</script>

<template>
  <div id="manageQuestionView">
    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total: total,
        showTotal: true,
      }"
    >
      <template #optional="{ record }">
        <a-button @click="$modal.info({ title: 'Name', content: record.name })"
          >view
        </a-button>
      </template>
    </a-table>
  </div>
</template>

<style scoped></style>
