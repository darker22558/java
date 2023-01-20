import request from "@/utils/request";

export function login(loginForm) {
  return request({
    url: "/user/login",
    method: "POST",
    data: loginForm,
  });
}

export function logout() {
  return request({
    url: "/user/logout",
    method: "POST",
  });
}
