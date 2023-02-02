import request from "@/utils/request";

export function testCors() {
  return request({
    url: "/test/hello",
    method: "get",
  });
}
