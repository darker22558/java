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
```xml
    <dependencies>
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
    </dependencies>
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
#### 2.1.1.引入ElementUI
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

#### 2.1.2.引入Axios并配置请求文件
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

#### 2.1.3.完成前端跨域配置
+ 见[request.js](./management/src/utils/request.js)文件中的创建axios实例，baseURL填写后端地址和端口

### 2.2 地学综合平台管理系统

#### 2.2.1.布局调整
+ 添加界面统一布局[Container](management/src/components/Container.vue)并完善侧边栏导航配置
```vue
<template>
  <div>
    <el-container style="height: 100%; border: 1px solid #eee" class="container">
      <el-aside style="width: 20%; background-color: #545c64; height: 100vh">
        <div style="height: 54px; line-height: 60px; text-align: left; margin-left: 23px; margin-top: 6px">
          <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px"/>
          <b style="color: black; margin-left: 5px">地学综合平台</b>
        </div>
        <el-menu
            router
            class="el-menu-demo"
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
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 12px; background-color: #545c64">
          <el-avatar :size="25" style="margin-top: 5px; margin-right: 15px" src="http://hexo.li98.cn/img/snail2.png"></el-avatar>
          <el-dropdown>
            <span style="color: black; cursor: pointer">
              <b>
                {{ userInfo.username || "请登录" }}
              </b>
            </span>
            <el-dropdown-menu slot="dropdown">
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
      </el-container>
    </el-container>
  </div>
</template>
```

+ 该布局使用的是ElementUI的container组件，Header+Main+Aside的形式

#### 2.2.2.自定义配置，见[vue.config.js](management/vue.config.js)和[settings.js](management/src/settings.js)

### 2.3 地学综合平台

#### 2.3.1.布局调整
+ 添加界面统一布局[Container](management/src/components/Container.vue)

+ 该布局使用的是ElementUI的container组件，Header+Main+Footer的形式

#### 2.3.2.自定义配置，见[vue.config.js](./front/vue.config.js)和[settings.js](front/src/settings.js)

## 3.management的登录、退出功能
+ 添加登录界面[Login.vue](management/src/views/Login.vue)

+ 在[Container](management/src/components/Container.vue)的Header中添加`退出`按钮

+ 在[api/login.js](management/src/api/login.js)中添加登录、退出api
```javascript
export function login(loginForm) {
  return request({
    url: "/management/user/login",
    method: "POST",
    data: loginForm,
  });
}
export function logout() {
    return request({
        url: "/management/user/logout",
        method: "POST",
    });
}
```

+ 在[SysUserController.java](backend/src/main/java/com/geo/integrated/controller/management/SysUserController.java)中完成登录、退出功能
```java
@RestController
@RequestMapping("/management/user")
public class SysUserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDTO loginDTO) {
        User user = userService.login(loginDTO);
        if (user == null) {
            return Result.fail("用户不存在或密码不正确");
        }
        String jwt = TokenUtils.generateToken(user.getId(), user.getPassword());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("user", user);
        data.put("jwt", jwt);
        return Result.success("登录成功", data);
    }
    @PostMapping("/logout")
    public Result logout() {
        return Result.success("退出登录");
    }
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
      localStorage.removeItem("token");
      sessionStorage.removeItem("userInfo");
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

## 4.设计数据库表并添加对应的后端实体类
+ 数据库导入问题，navicate15导入csv报错以下，把编码选为`936 (ANSI/OEM - Simplified Chinese GBK)`即可
```sql
[ERR] 1366 - Incorrect string value: '\xD6\xD0\xB9\xFA\xBB\xB4...' for colum
```

## 5.初步完善management中各界面的功能
### 5.1.数据相关（data）
#### 5.1.1.文献数据
+ 实体类[DataPaper.java](backend/src/main/java/com/geo/integrated/model/DataPaper.java)
+ 控制层[DataPaperController.java](backend/src/main/java/com/geo/integrated/controller/management/DataPaperController.java)
+ 业务层[DataPaperService.java](backend/src/main/java/com/geo/integrated/service/DataPaperService.java)
+ 业务实现层[DataPaperServiceImpl.java](backend/src/main/java/com/geo/integrated/service/impl/DataPaperServiceImpl.java)
+ 持久层[DataPaperMapper.java](backend/src/main/java/com/geo/integrated/dao/DataPaperMapper.java)
+ xml[DataPaperMapper.xml](backend/src/main/resources/mapper/DataPaperMapper.xml)
+ 界面组件[PaperData.vue](management/src/views/data/PaperData.vue)
+ api文件[paper.js](management/src/api/data/paper.js)
+ 添加界面路由[index.js](management/src/router/index.js)
    ```javascript
    const routes = [
      {
        path: "/data",
        name: "数据",
        component: Container,
        meta: { title: "数据管理", icon: "el-icon-notebook-2" },
        hidden: false,
        children: [
          {
            path: "/paper",
            name: "文献",
            meta: { title: "文献数据", icon: "el-icon-notebook-2" },
            component: PaperData
          },
        ],
      },
    ];
    ```
+ 后端使用Date类型的属性，如果前端使用字符串形式展示，则日期会少一天
  - 错误显示情况举例1：后端传的是8月1日，前端显示7月31日
  - 错误显示情况举例2：前端选择2月2日，回显是2月1日
  - 参考[element-ui的DatePicker日期选择器说明](https://element.faas.ele.me/#/zh-CN/component/date-picker#ri-qi-ge-shi)，使用`el-date-picker`展示日期字段，可以使其正常显示

#### 5.1.2.煤田数据
+ 实体类[DataCoalfield.java](backend/src/main/java/com/geo/integrated/model/DataCoalfield.java)
+ 控制层[DataCoalfieldController.java](backend/src/main/java/com/geo/integrated/controller/management/DataCoalfieldController.java)
+ 业务层[DataCoalfieldService.java](backend/src/main/java/com/geo/integrated/service/DataCoalfieldService.java)
+ 业务实现层[DataCoalfieldServiceImpl.java](backend/src/main/java/com/geo/integrated/service/impl/DataCoalfieldServiceImpl.java)
+ 持久层[DataCoalfieldMapper.java](backend/src/main/java/com/geo/integrated/dao/DataCoalfieldMapper.java)
+ xml[DataCoalfieldMapper.xml](backend/src/main/resources/mapper/DataCoalfieldMapper.xml)
+ 界面组件[CoalfieldData.vue](management/src/views/data/CoalfieldData.vue)
+ api文件[coalfield.js](management/src/api/data/coalfield.js)
+ 添加界面路由[index.js](management/src/router/index.js)
    ```javascript
    const routes = [
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
            component: PaperData
          },
        ],
      },
    ];
    ```

### 5.2.日志相关（log）
#### 5.2.1.操作日志
+ 实体类[LogOperation.java](backend/src/main/java/com/geo/integrated/model/LogOperation.java)
+ 控制层[LogOperationController.java](backend/src/main/java/com/geo/integrated/controller/management/LogOperationController.java)
+ 业务层[LogOperationService.java](backend/src/main/java/com/geo/integrated/service/LogOperationService.java)
+ 业务实现层[LogOperationServiceImpl.java](backend/src/main/java/com/geo/integrated/service/impl/LogOperationServiceImpl.java)
+ 持久层[LogOperationMapper.java](backend/src/main/java/com/geo/integrated/dao/LogOperationMapper.java)
+ xml[LogOperationMapper.xml](backend/src/main/resources/mapper/LogOperationMapper.xml)\
+ 记录操作日志的annotation[OperationLogger.java](backend/src/main/java/com/geo/integrated/annotation/OperationLogger.java)
+ 记录操作日志的切面[OperationLogAspect.java](backend/src/main/java/com/geo/integrated/aspect/OperationLogAspect.java)
+ AOP工具类[AopUtils.java](backend/src/main/java/com/geo/integrated/utils/AopUtils.java)
+ IP工具类[IpAddressUtils.java](backend/src/main/java/com/geo/integrated/utils/IpAddressUtils.java)
+ Jackson工具类[JacksonUtils.java](backend/src/main/java/com/geo/integrated/utils/JacksonUtils.java)
+ 用户代理解析工具类[UserAgentUtils.java](backend/src/main/java/com/geo/integrated/utils/UserAgentUtils.java)
+ ip转换的数据库[ip2region.db](backend/src/main/resources/ipdb/ip2region.db)
+ 新增IP相关的常量[Constant.java](backend/src/main/java/com/geo/integrated/common/Constant.java)
  ```java
  public interface Constant {
  
      /**
       * 未知IP
       */
      String IP_UNKNOWN = "unknown";
  
      /**
       * ipv4本机地址
       */
      String IP_V4_LOCALHOST = "127.0.0.1";
  
      /**
       * ipv6本机地址
       */
      String IP_V6_LOCALHOST = "0:0:0:0:0:0:0:1";
  }
  ```
+ 新增依赖[pom.xml](backend/pom.xml)
  ```xml
      <dependencies>
          <!-- aspect -->
          <dependency>
              <groupId>org.aspectj</groupId>
              <artifactId>aspectjweaver</artifactId>
              <version>1.9.6</version>
          </dependency>
          <!-- ip2region -->
          <dependency>
              <groupId>org.lionsoul</groupId>
              <artifactId>ip2region</artifactId>
              <version>1.7.2</version>
          </dependency>
          <!-- 解析客户端操作系统、浏览器 -->
          <dependency>
              <groupId>nl.basjes.parse.useragent</groupId>
              <artifactId>yauaa</artifactId>
              <version>5.20</version>
          </dependency>
      </dependencies>
  ```
+ 界面组件[OperationLog.vue](management/src/views/log/OperationLog.vue)
+ api文件[operation.js](management/src/api/log/operation.js)
+ 添加界面路由[index.js](management/src/router/index.js)
  ```javascript
  const routes = [
    {
      path: "/log",
      name: "日志",
      component: Container,
      meta: { title: "日志管理", icon: "el-icon-date" },
      hidden: false,
      children: [
        {
          path: "/operation",
          name: "操作",
          meta: { title: "操作日志", icon: "el-icon-date" },
          component: OperationLog,
        },
      ],
    },
  ];
  ```
+ 关于自定义AOP时，Spring AOP @Before @Around @After的区别，[参考链接](https://blog.csdn.net/jsbylibo/article/details/106548691)

### 5.3.科研成果相关（achievement）
+ 与`5.1.数据相关（data）`中配置步骤类似