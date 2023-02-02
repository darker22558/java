import request from "@/utils/request";

export function testCors() {
  return request({
    url: "/test/hello",
    method: "get",
  });
}

export function getAuthCode(email) {
  return request({
    url: "/test/getAuthCode",
    method: "GET",
    params: { email }
  });
}

export function verifyAuthCode(verifyForm) {
  return request({
    url: "/test/verifyAuthCode",
    method: "GET",
    params: { ...verifyForm }
  });
}
