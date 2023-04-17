import request from "@/utils/request";

export function getCoalfieldInfoOfAllProvince() {
    return request({
        url: "/data/coalfield/getCoalfieldInfoOfAllProvince",
        method: "GET",
    });
}
