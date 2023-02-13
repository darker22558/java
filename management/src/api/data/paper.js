import request from "@/utils/request";

export function getPaperList(queryInfo) {
  return request({
    url: "/data/paper/getPaperList",
    method: "GET",
    params: { ...queryInfo }
  });
}

export function deletePaperById(id) {
  return request({
    url: "/data/paper/deletePaperById",
    method: "DELETE",
    params: { id },
  });
}

export function deletePaperBatchByIds(ids) {
  return request({
    url: "/data/paper/deletePaperBatchByIds",
    method: "DELETE",
    params: { ids },
  });
}

export function saveOrUpdate(form) {
  return request({
    url: "/data/paper/saveOrUpdate",
    method: "POST",
    data: form,
  });
}

export function importDataBatch(file) {
  return request({
    url: "/data/paper/importDataBatch",
    method: "POST",
    header: { "Content-Type": "multipart/form-data" },
    data: file,
  });
}
