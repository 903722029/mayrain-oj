<template>
  <a-row
    id="globalHeader"
    style="margin-bottom: 16px"
    align="center"
    :wrap="false"
  >
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :selected-keys="selectedKey"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div class="title-bar">
            <img class="logo" src="../assets/oj-Logo.png" />
            <div class="title">五月雨-OJ</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div>
        {{ store.state.user?.loginUser?.userName }}
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "../router/routes";
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useStore } from "vuex";
import checkAuthority from "@/authority/checkAuthority";
import authorityEnum from "@/authority/authorityEnum";

const selectedKey = ref(["/"]);
const router = useRouter();
router.afterEach((to, from, failure) => {
  selectedKey.value = [to.path];
});

const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

const store = useStore();
const visibleRoutes = computed(() => {
  return routes.filter((item) => {
    if (item.meta?.hiding) {
      return false;
    }
    // 判断权限
    if (
      checkAuthority(store.state.user?.loginUser, item.meta?.access as string)
    ) {
      return true;
    }
  });
});
setTimeout(() => {
  store.dispatch("user/getLoginUser", {
    userName: "管理员",
    userRole: authorityEnum.ADMIN,
  });
}, 3000);
</script>

<style scoped>
.title-bar {
}

.logo {
  height: 48px;
}

.title {
  color: #444;
  margin-left: 15px;
}
</style>
