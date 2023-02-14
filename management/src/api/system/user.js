import request from "@/utils/request";

export function getUserList(queryInfo) {
  return request({
    url: "/system/user/getUserList",
    method: "GET",
    params: { ...queryInfo },
  });
}

export function deleteUserById(id) {
  return request({
    url: "/system/user/deleteUserById",
    method: "DELETE",
    params: { id },
  });
}

export function deleteUserBatchByIds(ids) {
  return request({
    url: "/system/user/deleteUserBatchByIds",
    method: "DELETE",
    data: ids,
  });
}

export function saveUser(form) {
  return request({
    url: "/system/user/saveUser",
    method: "POST",
    data: form,
  });
}

export function updateUser(form) {
  return request({
    url: "/system/user/updateUser",
    method: "POST",
    data: form,
  });
}

export function uploadAvatar(file) {
  return request({
    url: "/system/user/uploadAvatar",
    method: "POST",
    header: { "Content-Type": "multipart/form-data" },
    data: file,
  });
}