import { createApp } from "vue";
import ArcoVue from "@arco-design/web-vue";
import "@arco-design/web-vue/dist/arco.css";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "@/authority/index";

createApp(App).use(ArcoVue).use(store).use(router).mount("#app");
