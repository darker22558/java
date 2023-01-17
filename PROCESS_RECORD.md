# 项目完善过程记录

## 1.后端基本配置
### 1.1.常量接口(先定义好基本常量)
```java
public interface Constant {
    /**
     * 成功
     */
    Integer CODE_SUCCESSFUL = 200;

    /**
     * 参数错误
     */
    Integer CODE_PARAM_ERROR = 400;

    /**
     * 权限不足
     */
    Integer CODE_ACCESS_DENIED = 401;

    /**
     * 系统错误
     */
    Integer CODE_SYSTEM_ERROR = 500;

    /**
     * 其他业务异常
     */
    Integer CODE_ELSE_ERROR = 600;
}
```
### 1.2.接口统一返回包装类
```java
@Data
@ToString
public class Result implements Serializable {
    private Integer code;
    private String message;
    private Object data;

    public static Result success(Object data) {
        return success(Constant.CODE_SUCCESSFUL, "操作成功", data);
    }

    public static Result success(String message, Object data) {
        return success(Constant.CODE_SUCCESSFUL, message, data);
    }

    public static Result success(Integer code, String message, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static Result fail(String message) {
        return fail(Constant.CODE_PARAM_ERROR, message, null);
    }

    public static Result fail(String message, Object data) {
        return fail(Constant.CODE_PARAM_ERROR, message, data);
    }

    public static Result fail(Integer code, String message, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }
}
```
### 1.3.跨域配置
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
}
```
### 1.4.在[pom.xml](./backend/pom.xml)中引入hutool等常用的依赖
```java
        <!--mybatis plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.2</version>
        </dependency>
        <!-- hutool工具类-->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.3.3</version>
        </dependency>
        <!-- 实体类字段校验-->
        <dependency>
            <groupId>jakarta.validation</groupId>
            <artifactId>jakarta.validation-api</artifactId>
            <version>2.0.2</version>
        </dependency>
        <!-- JWT -->
        <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.10.3</version>
        </dependency>
```
### 1.5.Mybatis Plus设置
```java
@Configuration
@EnableTransactionManagement
@MapperScan("com.geo.integrated.dao")
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return mybatisPlusInterceptor;
    }
}
```
### 1.6.[application.yml](./backend/src/main/resources/application.yml)中配置数据库和Mybatis Plus
```yaml
server:
  address: localhost
  port: 8080

mybatis-plus:
  mapper-locations: classpath*:mapper/*.xml
  type-aliases-package: com.geo.integrated.dao
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/geo_integrated?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```
### 1.7.后台与前台控制层的目录结构区分
+ 后台管理系统相关的控制层统一放置在controller/management路径下
+ 前台相关的控制层统一放置在controller/front路径下

## 2.前端基本配置
### 2.1.通用配置(以后台管理系统为例)
### 2.1.1.引入ElementUI
+ 下载
```bash
npm install element-ui -S
```
+ 在[main.js](./management/src/main.js)中引入
```javascript
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
Vue.use(ElementUI);
```

### 2.1.2.引入Axios并配置请求文件
+ 下载
```bash
npm install axios -S
```
+ 配置[request.js](./management/src/utils/request.js)
```javascript
import axios from "axios";
import { Message } from "element-ui";
import store from "@/store";
import router from "@/router/index";

// create an axios instance
const service = axios.create({
    // url = base url + request url
    baseURL: "http://localhost:8080",
    // withCredentials: true, // send cookies when cross-domain requests
    timeout: 30000, // request timeout
});

// request interceptor
service.interceptors.request.use(
    (config) => {
        // do something before request is sent
        if (store.state.token) {
            // let each request carry token
            // ['X-Token'] is a custom headers key
            // please modify it according to the actual situation
            config.headers["Authorization"] = store.state.token;
        }
        return config;
    },
    (error) => {
        // do something with request error
        console.log(error); // for debug
        return Promise.reject(error);
    }
);

// response interceptor
service.interceptors.response.use(
    (response) => {
        const res = response.data;
        // 如果不是200，则判定为一个错误
        if (res.code !== 200) {
            Message({
                showClose: true,
                message: "错了哦，这是一条错误消息",
                type: "error",
                duration: 3 * 1000,
            });
            // 如果是401说明没有token，需要重新登陆，直接跳转到重新登录界面
            if (res.code === 401) {
                store.commit("RESET_STATE");
                router.push("/login");
            }
            return Promise.reject(new Error(res.message || "Error"));
        } else {
            return res;
        }
    },
    (error) => {
        console.log("err: " + error); // for debug
        Message({
            message: error.message,
            type: "error",
            duration: 5 * 1000,
        });
        return Promise.reject(error);
    }
);

export default service;
```

### 2.1.3.完成前端跨域配置
+ 见[request.js](./management/src/utils/request.js)文件中的创建axios实例，baseURL填写后端地址和端口

### 2.2 地学综合平台后台管理系统

#### 2.2.1.布局调整
+ 添加界面统一布局[Container](management/src/components/Container.vue)并完善侧边栏导航配置
+ 该布局使用的是ElementUI的container组件，Header+Main+Aside的形式

#### 2.2.2.自定义配置，见[vue.config.js](management/vue.config.js)和[settings.js](management/src/settings.js)

### 2.3 地学综合平台前台

#### 2.3.1.布局调整
+ 添加界面统一布局[Container](management/src/components/Container.vue)
+ 该布局使用的是ElementUI的container组件，Header+Main+Footer的形式

## 3.management的登录功能
+ 添加登录界面[Login.vue](management/src/views/Login.vue)

+ 在[api/login.js](management/src/api/login.js)中添加登录api
```javascript
export function login(loginForm) {
  return request({
    url: "/management/user/login",
    method: "POST",
    data: loginForm,
  });
}
```

+ 在[store/index.js](management/src/store/index.js)中添加关于登录和退出时设置token和userInfo的方法
```javascript
export default new Vuex.Store({
  state: {
    token: "",
    userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
  },
  mutations: {
    // 设置token
    SET_TOKEN: (state, token) => {
      state.token = token;
      localStorage.setItem("token", token);
    },
    // 设置用户信息
    SET_USERINFO: (state, userInfo) => {
      state.userInfo = userInfo;
      sessionStorage.setItem("userInfo", JSON.stringify(userInfo));
    },
    // 移除token和用户信息
    REMOVE_INFO: (state) => {
      state.token = "";
      state.userInfo = "";
      localStorage.setItem("token", "");
      sessionStorage.setItem("userInfo", "");
    },
  },
  getters: {
    // 获取用户信息
    getUserInfo: (state) => {
      return state.userInfo;
    },
  },
  actions: {},
  modules: {},
});
```

+ 配置路由权限拦截，见[permission.js](management/src/permission.js)
```javascript
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
```

+ 在[main.js](management/src/main.js)中引入permission.js
```javascript
// permission control
import "@/permission";
```
