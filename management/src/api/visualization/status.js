import request from "@/utils/request";

export function getSystemState() {
  return request({
    url: "/visualization/status/getSystemState",
    method: "GET",
  });
}
