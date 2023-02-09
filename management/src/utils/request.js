import axios from "axios";
import { Message } from "element-ui";
import store from "@/store";
import router from "@/router/index";
import {tokenHead} from "@/settings";

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
    // console.log("token ========== {}", token)
    if (token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers["Authorization"] = tokenHead + token;
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
