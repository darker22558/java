import request from "@/utils/request";

export function login(loginForm) {
  return request({
    url: "/account/login",
    method: "POST",
    data: loginForm,
  });
}

export function logout() {
  return request({
    url: "/account/logout",
    method: "POST",
  });
}

export function generateAuthCode() {
  return request({
    url: "/account/generateAuthCode",
    method: "GET",
  });
}

