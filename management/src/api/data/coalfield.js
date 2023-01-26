import request from "@/utils/request";

export function getCoalfieldList(queryInfo) {
  return request({
    url: "/coalfield/getCoalfieldList",
    method: "GET",
    params: { ...queryInfo },
  });
}

export function deleteCoalfieldById(id) {
  return request({
    url: "/coalfield/deleteCoalfieldById",
    method: "DELETE",
    params: { id },
  });
}

export function deleteCoalfieldBatchByIds(ids) {
  return request({
    url: "/coalfield/deleteCoalfieldBatchByIds",
    method: "DELETE",
    data: ids,
  });
}

export function saveOrUpdate(form) {
  return request({
    url: "/coalfield/saveOrUpdate",
    method: "POST",
    data: form,
  });
}
