import router from "./router";
// import store from "./store";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import getPageTitle from "@/utils/get-page-title";
NProgress.configure({ showSpinner: false }); // NProgress Configuration

const whiteList = ["/login"]; // no redirect whitelist

// 全局前置守卫,当有路由进行跳转时就会进入这个守卫
// to: Route: 即将要进入的目标 路由对象
// from: Route: 当前导航正要离开的路由
// next: Function: 一定要调用该方法来 resolve 这个钩子。执行效果依赖 next 方法的调用参数。
router.beforeEach((to, from, next) => {
  // 开始加载进度条
  NProgress.start();
  // 设置页面标题
  document.title = getPageTitle(to.meta.title);
  // 判断用户是否登录，有token值就意味着已经登录了
  const hasToken = localStorage.getItem("token");
  // const hasToken = store.state.token;
  // console.log("判断用户是否登录: " + hasToken);
  if (hasToken) {
    const hasUserInfo = sessionStorage.getItem("userInfo");
    if (!hasUserInfo) {
      next({path: "/login"})
      localStorage.removeItem("token")
      // 进度条结束
      NProgress.done();
    }
    if (to.path === "/login") {
      // 如果路由要跳转到登录页，重定向到主页
      next({ path: "/" });
      // 进度条结束
      NProgress.done();
    } else {
      // 如果路由要跳转到其他界面，比如首页
      // 去vuex仓库获取用户信息
      const hasGetUserInfo = sessionStorage.getItem("userInfo");
      // console.log("用户信息：");
      // console.log(hasGetUserInfo);
      if (hasGetUserInfo) {
        // 如果取到了用户的名字信息就直接让它跳转到目标路由
        next();
      } else {
        // 如果取不到则重定向到登录界面
        next(`/login?redirect=${to.path}`);
        NProgress.done();
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在白名单中的路由可以直接访问
      next();
    } else {
      // 不在白名单中的路由重定向到登录界面
      next(`/login?redirect=${to.path}`);
      NProgress.done();
    }
  }
});

//全局后置钩子
router.afterEach(() => {
  // finish progress bar
  NProgress.done();
});
