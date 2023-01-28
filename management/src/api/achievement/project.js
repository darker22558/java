import request from "@/utils/request";

export function getProjectList(queryInfo) {
  return request({
    url: "/achievement/project/getProjectList",
    method: "GET",
    params: { ...queryInfo },
  });
}

export function deleteProjectById(id) {
  return request({
    url: "/achievement/project/deleteProjectById",
    method: "DELETE",
    params: { id },
  });
}

export function deleteProjectBatchByIds(ids) {
  return request({
    url: "/achievement/project/deleteProjectBatchByIds",
    method: "DELETE",
    data: ids,
  });
}

export function saveOrUpdate(form) {
  return request({
    url: "/achievement/project/saveOrUpdate",
    method: "POST",
    data: form,
  });
}
