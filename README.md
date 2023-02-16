# 科研数据管理平系统

<p align="center">
    <a href="https://github.com/spring-projects/spring-boot" target="_blank">	
      <img src="https://img.shields.io/badge/SpringBoot-2.7.7-orange">
    </a>
    <a href="https://github.com/vuejs/vue" target="_blank">	
      <img src="https://img.shields.io/badge/Vue-2.6.11-brightgreen">
    </a>
    <a href="https://docs.oracle.com/javase/8/docs/api/?xd_co_f=47c934d9-e663-4eba-819c-b726fc2d0847" target="_blank">	
      <img src="https://img.shields.io/badge/JDK-1.8-orange">
    </a>
    <a href="https://github.com/baomidou/mybatis-plus" target="_blank">	
      <img src="https://img.shields.io/badge/Mybatis--Plus-3.5.2-orange">
    </a>
    <a href="https://github.com/ElemeFE/element" target="_blank">
        <img src="https://img.shields.io/badge/Element-%3E2.15.12-brightgreen" alt="element">
    </a>
    <a href="https://github.com/dromara/hutool" target="_blank">
        <img src="https://img.shields.io/badge/Hutool-5.8.11-blue" alt="element">
    </a>
    <a href="https://github.com/alibaba/easyexcel" target="_blank">
        <img src="https://img.shields.io/badge/EasyExcel-3.1.3-blue" alt="element">
    </a>
    <a href="https://github.com/alibaba/druid" target="_blank">
        <img src="https://img.shields.io/badge/Druid-1.2.6-blue" alt="element">
    </a>
</p>

## 简介
+ 项目地址：https://github.com/whtli/geo-integrated
+ 登录账号：`admin`；密码：`111111`


## 技术栈

### 后端

+ 核心框架：[Spring Boot](https://github.com/spring-projects/spring-boot)、MySQL
+ 认证与授权：SpringSecurity、[jjwt](https://github.com/jwtk/jjwt)
+ ORM 框架：[MyBatis-Plus](https://github.com/baomidou/mybatis-plus)
+ NoSQL 缓存：[Redis](https://github.com/redis/redis)
+ 工具类库：[Hutool](https://github.com/dromara/hutool)
+ 数据库连接池：[Druid](https://github.com/alibaba/druid)
+ Excel工具：[EasyExcel](https://github.com/alibaba/easyexcel)
+ 离线 IP 地址库：[ip2region](https://github.com/lionsoul2014/ip2region)
+ UserAgent 解析：[yauaa](https://github.com/nielsbasjes/yauaa)
+ 图像云存储：七牛云

### 前端

+ 核心框架：[Vue2](https://github.com/vuejs/vue)、[Vue Router](https://github.com/vuejs/vue-router)、[Vuex](https://github.com/vuejs/vuex)
+ UI 框架：[Element UI](https://github.com/ElemeFE/element)
+ 网络请求库：[axios](https://github.com/axios/axios)
+ 可视化图表库：[echarts](https://github.com/apache/echarts)


## 实现功能

### 项目过程记录见[PROCESS_RECORD.md](PROCESS_RECORD.md)

+ 数据信息的增删改查
+ 数据表格的导入导出（EasyExcel）
+ 自定义AOP记录操作日志、异常日志
+ 整合OSS实现图像上传到七牛云
+ 整合SpringSecurity和JWT实现认证和授权
+ 整合 ECharts 的数据统计
+ 整合了Redis实现数据缓存
+ 整合Swagger-UI实现在线API文档
+ 整合SpringTask实现定时任务
+ 自定义全局异常处理


## 注意事项

+ 创建 MySQL 数据库，字符集为utf8mb4，并执行[geo_integrated.sql](geo_integrated.sql)初始化表
+ 确保 Maven 和 npm 能够成功导入现版本依赖
+ 项目运行之前需要修改[backend/src/main/resources/application.yml](backend/src/main/resources/application.yml)等配置文件中的配置信息，如本地数据库名、七牛云的accessKey和accessSecretKey
+ 安装 Redis 并启动，然后启动后端服务
+ 系统的默认用户名密码为admin，123456
+ 分别在`management`目录下执行`npm install`安装依赖，然后可执行`npm run serve`启动项目
+ 部署前需要根据实际情况修改前端项目的baseURL和后端项目的server.address和server.port


## 感谢上面提到的每个开源项目

