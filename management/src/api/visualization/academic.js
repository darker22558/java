import request from "@/utils/request";

export function getAcademicData() {
  return request({
    url: "/visualization/academic/getAcademicData",
    method: "GET",
  });
}
