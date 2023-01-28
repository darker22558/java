import Vue from "vue";
import VueRouter from "vue-router";
import Dashboard from "@/views/Dashboard.vue";
import Container from "@/components/Container.vue";
import Login from "@/views/Login.vue";
import CoalfieldData from "@/views/data/CoalfieldData.vue";
import PaperData from "@/views/data/PaperData.vue";
import OperationLog from "@/views/log/OperationLog.vue";
import Patent from "@/views/achievement/Patent.vue";
import Project from "@/views/achievement/Project.vue";
import Honor from "@/views/achievement/Honor.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/login",
    name: "Login",
    component: Login,
    hidden: true,
  },
  {
    path: "/",
    name: "首页",
    meta: { title: "首页", icon: "el-icon-set-up" },
    component: Container,
    redirect: "/dashboard",
    children: [
      {
        path: "/dashboard",
        name: "导航",
        meta: { title: "导航", icon: "el-icon-set-up" },
        component: Dashboard,
      },
    ],
  },
  {
    path: "/data",
    name: "数据",
    component: Container,
    meta: { title: "数据管理", icon: "el-icon-notebook-2" },
    hidden: false,
    children: [
      {
        path: "/coalfield",
        name: "煤田",
        meta: { title: "煤田数据", icon: "el-icon-notebook-2" },
        component: CoalfieldData,
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
      {
        path: "/paper",
        name: "文献",
        meta: { title: "文献数据", icon: "el-icon-notebook-2" },
        component: PaperData,
      },
    ],
  },
  {
    path: "/achievement",
    name: "科研成果",
    component: Container,
    meta: { title: "科研成果管理", icon: "el-icon-collection" },
    hidden: false,
    children: [
      {
        path: "/honor",
        name: "所获荣誉",
        meta: { title: "所获荣誉", icon: "el-icon-collection" },
        component: Honor,
      },
      {
        path: "/project",
        name: "科研项目",
        meta: { title: "科研项目", icon: "el-icon-collection" },
        component: Project,
      },
      {
        path: "/patent",
        name: "发明专利",
        meta: { title: "发明专利", icon: "el-icon-collection" },
        component: Patent,
      },
      {
        path: "/independent",
        name: "自主",
        meta: { title: "自主课题", icon: "el-icon-collection" },
      },
      {
        path: "/else",
        name: "其他",
        meta: { title: "其他项目", icon: "el-icon-collection" },
      },
    ],
  },
  {
    path: "/news",
    name: "新闻",
    component: Container,
    meta: { title: "新闻管理", icon: "el-icon-news" },
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
    meta: { title: "概况管理", icon: "el-icon-collection-tag" },
    hidden: false,
    children: [
      {
        path: "/overview",
        name: "概况",
        meta: { title: "平台概况", icon: "el-icon-collection-tag" },
      },
    ],
  },
  {
    path: "/visualization",
    name: "可视化",
    component: Container,
    meta: { title: "可视化管理", icon: "el-icon-data-analysis" },
    hidden: false,
    children: [
      {
        path: "/dataService",
        name: "数据服务",
        meta: { title: "数据服务中心", icon: "el-icon-data-analysis" },
      },
    ],
  },
  {
    path: "/log",
    name: "日志",
    component: Container,
    meta: { title: "日志管理", icon: "el-icon-date" },
    hidden: false,
    children: [
      {
        path: "/exception",
        name: "异常",
        meta: { title: "异常日志", icon: "el-icon-date" },
      },
      {
        path: "/operation",
        name: "操作",
        meta: { title: "操作日志", icon: "el-icon-date" },
        component: OperationLog,
      },
      {
        path: "/visit",
        name: "访问",
        meta: { title: "访问日志", icon: "el-icon-date" },
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
