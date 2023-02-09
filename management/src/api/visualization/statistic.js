import request from "@/utils/request";

export function getStatisticalData() {
  return request({
    url: "/visualization/statistic/getStatisticalData",
    method: "GET",
  });
}
