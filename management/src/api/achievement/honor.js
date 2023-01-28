import request from "@/utils/request";

export function getHonorList(queryInfo) {
  return request({
    url: "/achievement/honor/getHonorList",
    method: "GET",
    params: { ...queryInfo },
  });
}

export function deleteHonorById(id) {
  return request({
    url: "/achievement/honor/deleteHonorById",
    method: "DELETE",
    params: { id },
  });
}

export function deleteHonorBatchByIds(ids) {
  return request({
    url: "/achievement/honor/deleteHonorBatchByIds",
    method: "DELETE",
    data: ids,
  });
}

export function saveOrUpdate(form) {
  return request({
    url: "/achievement/honor/saveOrUpdate",
    method: "POST",
    data: form,
  });
}
