import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import Container from "@/components/Container.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    component: Container,
    meta: { title: "首页", icon: "el-icon-house" },
    redirect: "/home",
    children: [
      {
        path: "/home",
        name: "首页",
        component: Home,
      },
    ],
  },
  {
    path: "/data",
    name: "数据",
    component: Container,
    meta: { title: "数据中心", icon: "el-icon-notebook-2" },
    hidden: false,
    children: [
      {
        path: "/mine",
        name: "煤矿",
        meta: { title: "煤矿数据", icon: "el-icon-notebook-2" },
      },
      {
        path: "/remoteSensing",
        name: "遥感",
        meta: { title: "遥感数据", icon: "el-icon-notebook-2" },
      },
      {
        path: "/earthquake",
        name: "地震",
        meta: { title: "地震数据", icon: "el-icon-notebook-2" },
      },
    ],
  },
  {
    path: "/data",
    name: "项目",
    component: Container,
    meta: { title: "项目综合", icon: "el-icon-collection" },
    hidden: false,
    children: [
      {
        path: "/country",
        name: "国自然",
        meta: { title: "国自然项目", icon: "el-icon-collection" },
      },
      {
        path: "/independent",
        name: "自主",
        meta: { title: "自主课题", icon: "el-icon-collection" },
      },
      {
        path: "/else",
        name: "其他",
        meta: { title: "其他项目", icon: "el-icon-collection\n" },
      },
    ],
  },
  {
    path: "/news",
    name: "新闻",
    component: Container,
    meta: { title: "专项新闻", icon: "el-icon-news" },
    hidden: false,
    children: [
      {
        path: "/moment",
        name: "动态",
        meta: { title: "最新动态", icon: "el-icon-news" },
      },
      {
        path: "/notice",
        name: "公告",
        meta: { title: "信息公告", icon: "el-icon-news" },
      },
    ],
  },
  {
    path: "/about",
    name: "关于",
    component: Container,
    meta: { title: "关于我们", icon: "el-icon-collection-tag\n" },
    hidden: false,
    children: [
      {
        path: "/overview",
        name: "概况",
        meta: { title: "平台概况", icon: "el-icon-collection-tag\n" },
      },
      {
        path: "/about",
        name: "About",
        component: () =>
          import(/* webpackChunkName: "about" */ "../views/about/About.vue"),
      },
    ],
  },
  {
    path: "/visualization",
    name: "可视化",
    component: Container,
    meta: { title: "可视化", icon: "el-icon-data-analysis" },
    hidden: false,
    children: [
      {
        path: "/dataService",
        name: "数据服务",
        meta: { title: "数据服务中心", icon: "el-icon-data-analysis" },
      },
    ],
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
