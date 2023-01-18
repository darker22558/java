import defaultSettings from "@/settings";

const title = defaultSettings.title || "地学综合平台";

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`;
  }
  return `${title}`;
}
