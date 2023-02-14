import request from "@/utils/request";

export function getRoleList(queryInfo) {
  return request({
    url: "/system/role/getRoleList",
    method: "GET",
    params: { ...queryInfo },
  });
}

export function deleteRoleById(id) {
  return request({
    url: "/system/role/deleteRoleById",
    method: "DELETE",
    params: { id },
  });
}

export function saveOrUpdate(form) {
  return request({
    url: "/system/role/saveOrUpdate",
    method: "POST",
    data: form,
  });
}