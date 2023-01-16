<template>
  <div>
    <h2>仪表盘</h2>
    <el-button @click="testCorsRegion()"> {{ testResult }} </el-button>
  </div>
</template>

<script>
import { testCors } from "@/api/test";

export default {
  name: "Dashboard",
  data() {
    return {
      testResult: "",
    };
  },
  methods: {
    testCorsRegion() {
      testCors().then(res => {
        this.testResult = res.data
      });
    },
  },
};
</script>

<style scoped></style>
