<template>
  <div>
    <el-container
      style="height: 100%; border: 1px solid #eee"
      class="container"
    >
      <el-header>
        <el-menu
          router
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
        >
          <template v-for="(item, index) in this.$router.options.routes">
            <template v-if="!item.hidden && item.children != null && item.children.length > 1">
              <el-submenu :key="index" :index="index + ''" >
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
            <template v-if="!item.hidden && (item.children == null || item.children.length === 1)">
              <el-menu-item :key="index" :index="item.children[0].path">
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
          </template>
        </el-menu>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
      <el-footer>Footer</el-footer>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Container",
  data() {
    return {};
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>

<style scoped>
.container {
  /*实现全面布局*/
  height: 100vh;
}
</style>
