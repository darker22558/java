import Cookies from "js-cookie";

const TokenKey = "geo_integrated_token";
const UserInfoKey = "geo_integrated_user_info";

export function getToken() {
  // return Cookies.get(TokenKey);
  return localStorage.getItem(TokenKey)
}

export function setToken(token) {
  // return Cookies.set(TokenKey, token);
  return localStorage.setItem(TokenKey, token)
}

export function removeToken() {
  // return Cookies.remove(TokenKey);
  return localStorage.removeItem(TokenKey)
}

export function getUserInfo() {
  return Cookies.get(UserInfoKey);
}

export function setUserInfo(userInfo) {
  return Cookies.set(UserInfoKey, JSON.stringify(userInfo));
}

export function removeUserInfo() {
  return Cookies.remove(UserInfoKey);
}
