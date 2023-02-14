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
import Paper from "@/views/achievement/Paper.vue";
import ExceptionLog from "@/views/log/ExceptionLog.vue";
import Statistic from "@/views/visualization/Statistic.vue";
import User from "@/views/system/User.vue";
import Role from "@/views/system/Role.vue";

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
        path: "dashboard",
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
        path: "coalfieldData",
        name: "煤田",
        meta: { title: "煤田数据", icon: "el-icon-notebook-2" },
        component: CoalfieldData,
      },
      /*      {
        path: "remoteSensingData",
        name: "遥感",
        meta: { title: "遥感数据", icon: "el-icon-notebook-2" },
      },
      {
        path: "earthquakeData",
        name: "地震",
        meta: { title: "地震数据", icon: "el-icon-notebook-2" },
      },*/
      {
        path: "paperData",
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
        path: "honor",
        name: "所获荣誉",
        meta: { title: "所获荣誉", icon: "el-icon-collection" },
        component: Honor,
      },
      {
        path: "project",
        name: "科研项目",
        meta: { title: "科研项目", icon: "el-icon-collection" },
        component: Project,
      },
      {
        path: "paper",
        name: "论文发表",
        meta: { title: "论文发表", icon: "el-icon-collection" },
        component: Paper,
      },
      {
        path: "patent",
        name: "发明专利",
        meta: { title: "发明专利", icon: "el-icon-collection" },
        component: Patent,
      },
      /*{
        path: "independent",
        name: "自主",
        meta: { title: "自主课题", icon: "el-icon-collection" },
      },
      {
        path: "else",
        name: "其他",
        meta: { title: "其他项目", icon: "el-icon-collection" },
      },*/
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
        path: "moment",
        name: "动态",
        meta: { title: "最新动态", icon: "el-icon-news" },
      },
      {
        path: "notice",
        name: "公告",
        meta: { title: "信息公告", icon: "el-icon-news" },
      },
    ],
  },
  {
    path: "/about",
    name: "相关信息",
    component: Container,
    meta: { title: "相关信息", icon: "el-icon-collection-tag" },
    hidden: false,
    children: [
      {
        path: "overview",
        name: "概况",
        meta: { title: "平台概况", icon: "el-icon-collection-tag" },
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
        path: "statistic",
        name: "网站数据",
        meta: { title: "网站数据统计", icon: "el-icon-data-analysis" },
        component: Statistic,
      },
      {
        path: "dataService",
        name: "数据服务",
        meta: { title: "数据服务中心", icon: "el-icon-data-analysis" },
      },
    ],
  },
  {
    path: "/system",
    name: "系统",
    component: Container,
    meta: { title: "系统管理", icon: "el-icon-setting" },
    hidden: false,
    children: [
      {
        path: "role",
        name: "角色",
        meta: { title: "系统角色", icon: "el-icon-setting" },
        component: Role,
      },
      {
        path: "user",
        name: "用户",
        meta: { title: "系统用户", icon: "el-icon-setting" },
        component: User,
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
        path: "operation",
        name: "操作",
        meta: { title: "操作日志", icon: "el-icon-date" },
        component: OperationLog,
      },
      {
        path: "exception",
        name: "异常",
        meta: { title: "异常日志", icon: "el-icon-date" },
        component: ExceptionLog,
      },
      /*{
        path: "visit",
        name: "访问",
        meta: { title: "访问日志", icon: "el-icon-date" },
      },*/
    ],
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
