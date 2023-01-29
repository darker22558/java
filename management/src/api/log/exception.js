import request from "@/utils/request";

export function getExceptionLogList(queryInfo) {
  return request({
    url: "/log/exception/getExceptionLogList",
    method: "GET",
    params: {
      ...queryInfo,
    },
  });
}

export function deleteExceptionLogById(id) {
  return request({
    url: "/log/exception/deleteExceptionLogById",
    method: "DELETE",
    params: { id },
  });
}
