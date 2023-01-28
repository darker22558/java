import request from "@/utils/request";

export function getPatentList(queryInfo) {
  return request({
    url: "/achievement/patent/getPatentList",
    method: "GET",
    params: { ...queryInfo },
  });
}

export function deletePatentById(id) {
  return request({
    url: "/achievement/patent/deletePatentById",
    method: "DELETE",
    params: { id },
  });
}

export function deletePatentBatchByIds(ids) {
  return request({
    url: "/achievement/patent/deletePatentBatchByIds",
    method: "DELETE",
    data: ids,
  });
}

export function saveOrUpdate(form) {
  return request({
    url: "/achievement/patent/saveOrUpdate",
    method: "POST",
    data: form,
  });
}
