import defaultSettings from "@/settings";

const title = defaultSettings.title || "科研数据";

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`;
  }
  return `${title}`;
}
