<template>
  <div>
    <el-container style="height: 100vh;" class="container">
      <el-aside style="width: 18%; background-color: #545c64; ">
<!--        <div style="height: 54px; line-height: 60px; text-align: left; margin-left: 23px; margin-top: 6px">
          <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px"/>
          <b style="color: black; margin-left: 5px; font-size: 18px">地学综合平台</b>
        </div>-->
        <el-menu
          router
          class="el-menu-demo"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
        >
          <template v-for="(item, index) in this.$router.options.routes">
            <template v-if="!item.hidden && item.path === '/'">
              <el-menu-item :key="index" :index="item.path">
                <template slot="title" v-if="item.children[0].meta">
                  <i :class="item.children[0].meta.icon"></i>
                  <span>{{ item.children[0].meta.title }}</span>
                </template>
                <template slot="title" v-else>
                  <i class="el-icon-s-home"></i>
                  <span>{{ item.children[0].name }}</span>
                </template>
              </el-menu-item>
            </template>
            <template v-if="!item.hidden && item.path !== '/'">
              <el-submenu :key="index" :index="item.path">
                <template slot="title" v-if="item.meta">
                  <i :class="item.meta.icon"></i>
                  <span>{{ item.meta.title }}</span>
                </template>
                <template slot="title" v-else>
                  <i class="el-icon-s-home"></i>
                  <span>{{ item.name }}</span>
                </template>
                <template v-for="(children, indexOfChild) in item.children">
                  <el-menu-item :key="indexOfChild" :index="item.path + '/' + children.path">
                    <template slot="title" v-if="children.meta">
                      <i :class="children.meta.icon"></i>
                      <span>{{ children.meta.title }}</span>
                    </template>
                    <template slot="title" v-else>
                      <i class="el-icon-s-home"></i>
                      <span>{{ children.name }}</span>
                    </template>
                  </el-menu-item>
                </template>
              </el-submenu>
            </template>
          </template>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style="vertical-align: center; text-align: right; font-size: 12px; background-color: #b3c0d1">
          <el-avatar :size="40" style="margin-top: 10px; margin-right: 40px" :src="logo"></el-avatar>
          <el-dropdown style="position: absolute; right: 10px">
            <span style="color: black; cursor: pointer">
              <b>
                {{ userInfo.username || "请登录" }}
              </b>
            </span>
            <el-dropdown-menu slot="dropdown" >
              <el-dropdown-item>个人主页</el-dropdown-item>
              <a target="_blank" :href="github">
                <el-dropdown-item divided>Github</el-dropdown-item>
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

        <el-footer>

        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { logout } from "@/api/login";
import {getUserInfo} from "@/utils/auth";
export default {
  name: "Container",
  data() {
    return {
      /*userInfo: sessionStorage.getItem("userInfo")
        ? JSON.parse(sessionStorage.getItem("userInfo"))
        : [],*/
      userInfo: getUserInfo() ? JSON.parse(getUserInfo()) : [],
      github: "https://github.com/whtli/geo-integrated",
      docs: "https://github.com/whtli/geo-integrated/blob/master/README.md",
      logo: "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif?imageView2/1/w/80/h/80",
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
  /*!*实现全面布局*!*/
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
