<template>
  <div>
    <h2>DashBoard</h2>
    <el-button @click="testCorsRegion()"> {{ testResult }} </el-button>
  </div>
</template>

<script>
import { testCors } from "@/api/test";

export default {
  name: "Dashboard",
  data() {
    return {
      testResult: "点击测试",
    };
  },
  methods: {
    testCorsRegion() {
      testCors().then((res) => {
        this.testResult = res.data;
      });
    },
  },
};
</script>

<style scoped></style>
