import router from "./router";
import getPageTitle from "@/utils/get-page-title";

// 全局前置守卫,当有路由进行跳转时就会进入这个守卫
// to: Route: 即将要进入的目标 路由对象
// from: Route: 当前导航正要离开的路由
// next: Function: 一定要调用该方法来 resolve 这个钩子。执行效果依赖 next 方法的调用参数。
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = getPageTitle(to.meta.title);
  next()
});
