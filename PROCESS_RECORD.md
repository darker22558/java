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

### 1.6.配置MySQL数据库和Mybatis Plus[application.yml](./backend/src/main/resources/application.yml)
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

### 1.8.全局异常处理
+ 因为不能避免服务器报错的情况，如果不配置异常处理机制，就会默认返回tomcat或者nginx的5XX页面。
+ 对用户来说此类返回信息不友好，用户无法理解是什么错误。这时候需要返回一个友好简单的格式给前端便于用户理解。
+ 自定义服务异常[ServiceException.java](backend/src/main/java/com/geo/integrated/exception/ServiceException.java)，继承自运行时异常RuntimeException
+ 添加全局异常处理[GlobalExceptionHandler.java](backend/src/main/java/com/geo/integrated/exception/GlobalExceptionHandler.java)

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
  baseURL: "http://localhost:9090/management",
  // send cookies when cross-domain requests
  // withCredentials: true,
  // request timeout
  timeout: 30000,
});

// request interceptor
service.interceptors.request.use(
        (config) => {
          // do something before request is sent
          const token = localStorage.getItem("token");
          if (token) {
            // let each request carry token
            // ['X-Token'] is a custom headers key
            // please modify it according to the actual situation
            config.headers["Authorization"] = token;
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
              message: res.message,
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
        <el-aside style="width: 18%; background-color: #545c64; height: 100vh">
          <div style="height: 54px; line-height: 60px; text-align: left; margin-left: 23px; margin-top: 6px">
            <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px"/>
            <b style="color: black; margin-left: 5px; font-size: 18px">地学综合平台</b>
          </div>
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
          <el-header style="text-align: right; font-size: 12px; background-color: #545c64">
            <el-avatar :size="40" style="margin-top: 5px; margin-right: 15px" :src="logo"></el-avatar>
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

#### 2.2.2.自定义配置

+ 见[vue.config.js](management/vue.config.js)和[settings.js](management/src/settings.js)

### 2.3 地学综合平台

#### 2.3.1.布局调整
+ 添加界面统一布局[Container](management/src/components/Container.vue)

+ 该布局使用的是ElementUI的container组件，Header+Main+Footer的形式

#### 2.3.2.自定义配置，见[vue.config.js](./front/vue.config.js)和[settings.js](front/src/settings.js)

## 3.management的登录、退出功能
> 通过整合JWT实现后台用户的登录和授权功能
> JWT实现认证和授权的原理:
> 用户调用登录接口，登录成功后获取到JWT工具类生成的token； 
> 之后用户每次调用接口都在http的header中添加一个叫Authorization的头，值为token； 
> 后台程序通过对Authorization头中信息的解码及数字签名校验来获取其中的用户信息，从而实现认证和授权。

### 3.1.后端整合JWT
+ 在[pom.xml](./backend/pom.xml)中添加依赖
  ```xml
      <dependencies>
          <!-- JWT -->
          <dependency>
              <groupId>com.auth0</groupId>
              <artifactId>java-jwt</artifactId>
              <version>3.10.3</version>
          </dependency>
      </dependencies>
  ```

+ 添加JWT拦截器[JwtInterceptor.java](backend/src/main/java/com/geo/integrated/config/interceptor/JwtInterceptor.java)

+ 完成拦截器配置[InterceptorConfig.java](backend/src/main/java/com/geo/integrated/config/InterceptorConfig.java)

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

### 3.2.前端组件、接口及配置

+ 添加登录界面[Login.vue](management/src/views/Login.vue)

+ 在[Container.vue](management/src/components/Container.vue)的Header中添加`退出`按钮

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

## 5.初步完善management相关的界面和功能
### 5.1.数据相关（data）
#### 5.1.1.文献数据
+ 实体类[DataPaper.java](backend/src/main/java/com/geo/integrated/model/DataPaper.java)
+ 控制层[DataPaperController.java](backend/src/main/java/com/geo/integrated/controller/management/DataPaperController.java)
+ 服务层[DataPaperService.java](backend/src/main/java/com/geo/integrated/service/DataPaperService.java)
+ 服务层实现[DataPaperServiceImpl.java](backend/src/main/java/com/geo/integrated/service/impl/DataPaperServiceImpl.java)
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
            path: "paper",
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
+ 服务层[DataCoalfieldService.java](backend/src/main/java/com/geo/integrated/service/DataCoalfieldService.java)
+ 服务层实现[DataCoalfieldServiceImpl.java](backend/src/main/java/com/geo/integrated/service/impl/DataCoalfieldServiceImpl.java)
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
            path: "coalfield",
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
+ 服务层[LogOperationService.java](backend/src/main/java/com/geo/integrated/service/LogOperationService.java)
+ 服务层实现[LogOperationServiceImpl.java](backend/src/main/java/com/geo/integrated/service/impl/LogOperationServiceImpl.java)
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
          path: "operation",
          name: "操作",
          meta: { title: "操作日志", icon: "el-icon-date" },
          component: OperationLog,
        },
      ],
    },
  ];
  ```
+ 关于自定义AOP时，Spring AOP @Before @Around @After的区别，[参考链接](https://blog.csdn.net/jsbylibo/article/details/106548691)

#### 5.2.2.异常日志
+ 与`5.2.1.操作日志`中配置步骤类似，不需要单独配置Logger，从操作日志或访问日志中获取引发异常的描述即可

### 5.3.科研成果相关（achievement）
+ 与`5.1.数据相关（data）`中配置步骤类似

## 6.后端整合
### 6.1.整合SwaggerUI

+ 添加依赖[pom.xml](./backend/pom.xml)
  ```xml
      <dependencies>
          <!--Swagger-UI API文档生产工具-->
          <dependency>
              <groupId>io.springfox</groupId>
              <artifactId>springfox-swagger2</artifactId>
              <version>2.7.0</version>
          </dependency>
          <dependency>
              <groupId>io.springfox</groupId>
              <artifactId>springfox-swagger-ui</artifactId>
              <version>2.7.0</version>
          </dependency>
      </dependencies>
  ```
+ 添加Swagger-UI配置[Swagger2Config.java](backend/src/main/java/com/geo/integrated/config/Swagger2Config.java)

+ 给Swagger2Config.java添加注解`@Profile({"dev"})`，仅在本地开发环境中支持使用Swagger、生产环境禁用

+ 在配置文件[application.yml](./backend/src/main/resources/application.yml)中添加配置项，解决整合后的报错问题
  ````bash
  Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
  2023-02-01 17:53:40.368 ERROR 10884 --- [           main] o.s.boot.SpringApplication               : Application run failed
  
  org.springframework.context.ApplicationContextException: Failed to start bean 'documentationPluginsBootstrapper'; nested exception is java.lang.NullPointerException
      at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:181)
      at org.springframework.context.support.DefaultLifecycleProcessor.access$200(DefaultLifecycleProcessor.java:54)
      at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:356)
      at java.lang.Iterable.forEach(Iterable.java:75)
      at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:155)
      at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:123)
      at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:935)
      at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:586)
      at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:147)
      at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:731)
      at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:408)
      at org.springframework.boot.SpringApplication.run(SpringApplication.java:307)
      at org.springframework.boot.SpringApplication.run(SpringApplication.java:1303)
      at org.springframework.boot.SpringApplication.run(SpringApplication.java:1292)
      at com.geo.integrated.BackendApplication.main(BackendApplication.java:15)
  Caused by: java.lang.NullPointerException: null
      at springfox.documentation.spi.service.contexts.Orderings$8.compare(Orderings.java:113)
      at springfox.documentation.spi.service.contexts.Orderings$8.compare(Orderings.java:110)
      at com.google.common.collect.ComparatorOrdering.compare(ComparatorOrdering.java:38)
      at java.util.TimSort.countRunAndMakeAscending(TimSort.java:355)
      at java.util.TimSort.sort(TimSort.java:234)
      at java.util.Arrays.sort(Arrays.java:1438)
      at com.google.common.collect.Ordering.sortedCopy(Ordering.java:817)
      at springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider.requestHandlers(WebMvcRequestHandlerProvider.java:52)
      at springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper$2.apply(DocumentationPluginsBootstrapper.java:129)
      at springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper$2.apply(DocumentationPluginsBootstrapper.java:126)
      at com.google.common.collect.Iterators$8.transform(Iterators.java:799)
      at com.google.common.collect.TransformedIterator.next(TransformedIterator.java:48)
      at com.google.common.collect.TransformedIterator.next(TransformedIterator.java:48)
      at com.google.common.collect.Iterators$5.hasNext(Iterators.java:548)
      at com.google.common.collect.ImmutableList.copyOf(ImmutableList.java:268)
      at com.google.common.collect.ImmutableList.copyOf(ImmutableList.java:226)
      at com.google.common.collect.FluentIterable.toList(FluentIterable.java:373)
      at springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper.defaultContextBuilder(DocumentationPluginsBootstrapper.java:100)
      at springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper.buildContext(DocumentationPluginsBootstrapper.java:91)
      at springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper.start(DocumentationPluginsBootstrapper.java:154)
      at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:178)
      ... 14 common frames omitted
  
  ````

+ 给各个Controller添加Swagger注解

+ 给实体类的属性添加@ApiModelProperty注解

+ 接口地址：http://localhost:9090/swagger-ui.html

### 6.2.整合SpringTask实现定时任务（功能测试）
> SpringTask是Spring自主研发的轻量级定时任务工具，相比于Quartz更加简单方便，且不需要引入其他依赖即可使用。
> 由于SpringTask已经存在于Spring框架中，所以无需添加依赖。
> [参考资料](https://www.macrozheng.com/mall/architect/mall_arch_06.html)

+ 添加[SpringTask](backend/src/main/java/com/geo/integrated/config/SpringTaskConfig.java)的配置，只需要在配置类中添加`@EnableScheduling`注解即可开启SpringTask的定时任务能力。

+ 添加[TestCountTask.java](backend/src/main/java/com/geo/integrated/component/TestCountTask.java)来测试定时任务的执行。


### 6.3.整合Redis实现缓存功能

> 以登录时获取验证码并做登录校验为例
> [参考资料](https://www.macrozheng.com/mall/architect/mall_arch_03.html)

+ 添加项目依赖[pom.xml](./backend/pom.xml)
  ```xml
      <dependencies>
          <!--Redis依赖配置-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-data-redis</artifactId>
          </dependency>
      </dependencies>
  ```

+ 修改配置文件[application-dev.yml](backend/src/main/resources/application-dev.yml)
  ```yaml
  spring:
    redis:
      host: localhost # Redis服务器地址
      database: 0 # Redis数据库索引（默认为0）
      port: 6379 # Redis服务器连接端口
      password: # Redis服务器连接密码（默认为空）
      jedis:
        pool:
          max-active: 8 # 连接池最大连接数（使用负值表示没有限制）
          max-wait: -1ms # 连接池最大阻塞等待时间（使用负值表示没有限制）
          max-idle: 8 # 连接池中的最大空闲连接
          min-idle: 0 # 连接池中的最小空闲连接
      timeout: 3000ms # 连接超时时间（毫秒）
  
  # 自定义redis key
  redis:
    key:
      prefix:
        authCode: "portal:authCode:"
      expire:
        authCode: 120 # 验证码超期时间
  ```

+ 添加[RedisService](backend/src/main/java/com/geo/integrated/service/RedisService.java)接口用于定义一些常用Redis操作

+ 在[RedisServiceImpl.java](backend/src/main/java/com/geo/integrated/service/impl/RedisServiceImpl.java)中注入StringRedisTemplate，实现RedisService接口

+ 在[loginDTO](backend/src/main/java/com/geo/integrated/model/dto/LoginDTO.java)中添加验证码字段

+ 在[SysUserController.java](backend/src/main/java/com/geo/integrated/controller/management/SysUserController.java)中添加根据登录用户名生成验证码的接口以及登录时的验证码校验流程

+ 在[SysUserService.java](backend/src/main/java/com/geo/integrated/service/SysUserService.java)中添加`生成验证码`和`校验验证码`接口

+ 添加[SysUserServiceImpl.java](backend/src/main/java/com/geo/integrated/service/impl/SysUserServiceImpl.java)实现`生成验证码`和`校验验证码`功能

  - 生成验证码时，将自定义的Redis键值加上邮箱生成一个Redis的key，以验证码为value存入到Redis中
  - 设置过期时间为SpringBoot配置文件中自定义的时间（如120s）
  - 校验验证码时根据邮箱来获取Redis里面存储的验证码，并与传入的验证码进行比对
  ```java
  @Service
  public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
      @Autowired
      private RedisService redisService;
  
      @Value("${redis.key.prefix.authCode}")
      private String redisKeyPrefixAuthCode;
  
      @Value("${redis.key.expire.authCode}")
      private Long authCodeExpireSeconds;
  
      /**
       * 生成验证码
       *
       * @param username 用户名
       * @return 验证码
       */
      @Override
      public Result generateAuthCode(String username) {
          StringBuilder sb = new StringBuilder();
          Random random = new Random();
          int len = 6;
          for (int i = 0; i < len; i++) {
              sb.append(random.nextInt(10));
          }
          // 验证码绑定用户名并存储到redis
          redisService.set(redisKeyPrefixAuthCode + username, sb.toString());
          redisService.expire(redisKeyPrefixAuthCode + username, authCodeExpireSeconds);
          return Result.success("获取验证码成功", sb.toString());
      }
  
      /**
       * 对输入的验证码进行校验
       *
       * @param username    用户名
       * @param authCode 验证码
       * @return 验证码的校验结果
       */
      @Override
      public Result verifyAuthCode(String username, String authCode) {
          if (StrUtil.isEmpty(authCode)) {
              return Result.fail("请输入验证码");
          }
          String realAuthCode = redisService.get(redisKeyPrefixAuthCode + username);
          boolean result = authCode.equals(realAuthCode);
          if (result) {
              return Result.success("验证码校验成功");
          } else {
              return Result.fail("验证码校验失败");
          }
      }
  }
  ```

+ 在[拦截器配置文件](backend/src/main/java/com/geo/integrated/config/InterceptorConfig.java)中放行`生成验证码`的接口，使后端在用户未登录时可以生成验证码