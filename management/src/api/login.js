import request from "@/utils/request";

export function login(loginForm) {
  return request({
    url: "/system/user/login",
    method: "POST",
    data: loginForm,
  });
}

export function logout() {
  return request({
    url: "/system/user/logout",
    method: "POST",
  });
}

export function generateAuthCode(username) {
  return request({
    url: "/system/user/generateAuthCode",
    method: "GET",
    params: { username }
  });
}

export function verifyAuthCode(verifyForm) {
  return request({
    url: "/system/user/verifyAuthCode",
    method: "GET",
    params: { ...verifyForm }
  });
}
