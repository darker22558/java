import axios from "axios";
import { Message } from "element-ui";

// create an axios instance
const service = axios.create({
  // url = base url + request url
  baseURL: "http://localhost:9090",
  // send cookies when cross-domain requests
  // withCredentials: true,
  // request timeout
  timeout: 30000,
});

// request interceptor
service.interceptors.request.use(
  (config) => {
    // do something before request is sent
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
