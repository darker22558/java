<template>
  <div>
    <h2>仪表盘</h2>
    <el-button @click="testCorsRegion()"> {{ testResult }} </el-button>
    <div>
      <el-button @click="generateAuthCode()">生成验证码 </el-button>
      <el-input v-model="authCode" disabled> </el-input>
      <el-button @click="testAuthCode()"> <span>{{ authResult }}</span> </el-button>
    </div>
  </div>
</template>

<script>
import { getAuthCode, verifyAuthCode, testCors } from "@/api/test";

export default {
  name: "Dashboard",
  data() {
    return {
      testResult: "",
      email: "whtli@geo.com",
      authCode: "",
      authResult: "待验证",
    };
  },
  methods: {
    testCorsRegion() {
      testCors().then((res) => {
        this.testResult = res.data;
      });
    },
    generateAuthCode() {
      getAuthCode(this.email).then((res) => {
        this.authCode = res.data;
      });
    },
    testAuthCode() {
      const verifyForm = {
        email: this.email,
        authCode: this.authCode,
      }
      verifyAuthCode(verifyForm).then((res) => {
        this.authResult = res.data;
      });
    },
  },
};
</script>

<style scoped></style>
