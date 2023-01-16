<template>
  <div>
    <el-container
      style="height: 100%; border: 1px solid #eee"
      class="container"
    >
      <el-aside
        style="width: 20%; background-color: rgb(238, 241, 246); height: 100vh"
      >
        <div style="height: 60px; line-height: 60px; text-align: center">
          <img
            src="../assets/logo.png"
            alt=""
            style="width: 20px; position: relative; top: 5px"
          />
          <b style="color: black; margin-left: 5px">地学综合平台</b>
        </div>
        <el-menu :router="true">
          <el-submenu
            v-for="(item, index) in this.$router.options.routes"
            :key="index"
            :index="index + ''"
          >
            <template slot="title">
              <i class="el-icon-box"></i>
              <span>{{ item.name }}</span>
            </template>
            <el-menu-item
              :key="indexOfChild"
              :index="children.path"
              v-for="(children, indexOfChild) in item.children"
            >
              {{ children.name }}
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 12px">
          <el-dropdown>
            <span>{{ username }}</span>
            <el-avatar
              :size="30"
              style="margin-top: 8px"
              src="http://hexo.li98.cn/img/snail2.png"
            ></el-avatar>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>查看</el-dropdown-item>
              <el-dropdown-item>新增</el-dropdown-item>
              <el-dropdown-item>删除</el-dropdown-item>
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
export default {
  name: "Container",
  data() {
    return {
      username: this.$store.state.userInfo.username,
    };
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
