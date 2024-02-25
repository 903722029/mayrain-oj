<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { Editor, Viewer } from "@bytemd/vue-next";
import { withDefaults, defineProps } from "vue";

interface Props {
  mdValue: string;
  mdHandleChange: (v: string) => void;
  mode: string;
}

const plugins = [
  gfm(),
  highlight(),
  // Add more plugins here
];

const props = withDefaults(defineProps<Props>(), {
  mdValue: () => "",
  mdHandleChange: (v: string) => {
    console.log(v);
  },
  mode: () => "split",
});
</script>

<template>
  <Viewer :value="mdValue" :plugins="plugins" @change="mdHandleChange" />
</template>

<style>
.bytemd-toolbar-icon.bytemd-tippy.bytemd-tippy-right:last-child {
  display: none;
}
</style>
