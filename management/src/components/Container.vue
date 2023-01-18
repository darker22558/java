<template>
  <div>
    <el-container style="height: 100%; border: 1px solid #eee" class="container">
      <el-aside style="width: 20%; background-color: rgb(238, 241, 246); height: 100vh">
        <div style="height: 60px; line-height: 60px; text-align: left; margin-left: 20px">
          <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px"/>
          <b style="color: black; margin-left: 5px">地学综合平台</b>
        </div>
        <el-menu router>
          <template v-for="(item, index) in this.$router.options.routes">
            <el-submenu :key="index" :index="index + ''" v-if="!item.hidden">
              <template slot="title" v-if="item.meta">
                <i :class="item.meta.icon"></i>
                <span>{{ item.meta.title }}</span>
              </template>
              <template slot="title" v-else>
                <i class="el-icon-s-home"></i>
                <span>{{ item.name }}</span>
              </template>
              <template v-for="(children, indexOfChild) in item.children">
                <el-menu-item :key="indexOfChild" :index="children.path">
                  <template slot="title" v-if="children.meta">
                    <i :class="children.meta.icon"></i>
                    <span >{{ children.meta.title }}</span>
                  </template>
                  <template slot="title" v-else>
                    <i class="el-icon-s-home"></i>
                    <span >{{ children.name }}</span>
                  </template>
                </el-menu-item>
              </template>
            </el-submenu>
          </template>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 12px">
          <el-avatar :size="20" style="margin-top: 10px" src="http://hexo.li98.cn/img/snail2.png"></el-avatar>
          <el-dropdown>
            <span>{{ userInfo.username || "请登录" }}</span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>查看</el-dropdown-item>
              <a target="_blank" :href="github">
                <el-dropdown-item>Github</el-dropdown-item>
              </a>
              <a target="_blank" :href="docs">
                <el-dropdown-item>Readme</el-dropdown-item>
              </a>
              <el-dropdown-item divided @click.native="handleLogout()">
                <span style="display: block">退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-header>

        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { logout } from "@/api/login";
export default {
  name: "Container",
  data() {
    return {
      userInfo: sessionStorage.getItem("userInfo")
        ? JSON.parse(sessionStorage.getItem("userInfo"))
        : [],
      github: "https://github.com/whtli/geo-integrated",
      docs: "https://github.com/whtli/geo-integrated/blob/master/README.md",
    };
  },
  methods: {
    async handleLogout() {
      logout().then((res) => {
        console.log(res);
        // 把token和用户信息共享出去
        this.$store.commit("REMOVE_INFO");
        this.$router.push(`/login?redirect=${this.$route.fullPath}`);
      });
    },
  },
};
</script>

<style scoped>
.container {
  /*实现全面布局*/
  height: 100vh;
}

.el-header {
  background-color: #b3c0d1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}

.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: left;
  line-height: 200px;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: left;
  line-height: 20px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>
