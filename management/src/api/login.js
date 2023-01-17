import request from "@/utils/request";

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
