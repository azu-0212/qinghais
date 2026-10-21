<template>
  <div id="popupContainer" @contextmenu.prevent="removeDefault"></div>
</template>

<script>
import $ from "jquery";

export default {
  name: "Popup",
  methods: {
    // 去除右击默认事件
    removeDefault() {
    },
  }
};
</script>

<style lang="scss">
@import "@/assets/styles/popupStyle.scss";
</style>
