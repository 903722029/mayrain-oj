<script setup lang="ts"></script>

<template>
  <div id="userLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <a-space>
          <img src="../assets/oj-Logo.png" class="logo" />
          <div>五月雨-oj</div>
        </a-space>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer">
        <a href="https://www.baidu.com"> created by syt </a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<style scoped>
#userLayout {
  text-align: center;
}

#userLayout .header {
  margin-bottom: 16px;
  margin-top: 16px;
}

#userLayout .content {
  padding: 20px;
}

#userLayout .footer {
  padding: 16px;
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}

#userLayout .logo {
  height: 48px;
}
</style>
