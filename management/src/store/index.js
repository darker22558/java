import Vue from "vue";
import Vuex from "vuex";
import { removeToken, setToken } from "@/utils/auth";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    token: "",
    userInfo: "",
  },
  mutations: {
    // 设置token
    SET_TOKEN: (state, token) => {
      state.token = token;
      // localStorage.setItem("token", token);
      setToken(token);
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
      // localStorage.removeItem("token");
      // sessionStorage.removeItem("userInfo");
      sessionStorage.clear();
      removeToken();
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
