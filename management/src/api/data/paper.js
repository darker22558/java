import request from "@/utils/request";

export function getPaperList(queryInfo) {
  return request({
    url: "/paper/getPaperList",
    method: "GET",
    params: { ...queryInfo }
  });
}

export function deletePaperById(id) {
  return request({
    url: "/paper/deletePaperById",
    method: "DELETE",
    params: { id },
  });
}

export function deletePaperBatchByIds(ids) {
  return request({
    url: "/paper/deletePaperBatchByIds",
    method: "DELETE",
    params: { ids },
  });
}

export function saveOrUpdate(form) {
  return request({
    url: "/paper/saveOrUpdate",
    method: "POST",
    data: form,
  });
}
