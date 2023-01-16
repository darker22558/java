import request from "@/utils/request";

export function login(loginForm) {
  return request({
    url: "/management/user/login",
    method: "post",
    data: loginForm,
  });
}
