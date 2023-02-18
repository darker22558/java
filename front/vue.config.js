const path = require("path");
function resolve(dir) {
  return path.join(__dirname, dir);
}

const defaultSettings = require("./src/settings.js");
// 网站标题
const name = defaultSettings.title || "科研数据";
// 本地端口
const port = process.env.port || process.env.npm_config_port || 9092;

// 所有配置项说明参见https://cli.vuejs.org/config/
module.exports = {
  /**
   * 如果计划将站点部署在子路径下，则需要设置publicPath，比如GitHub Pages。
   * 如果计划将站点部署到https://foo.github.io/bar/，那么publicPath应该设置为“/bar/”。
   * 在大多数情况下请使用“/”!!!
   * 详见: https://cli.vuejs.org/config/#publicpath
   */
  publicPath: "/",
  outputDir: "dist",
  assetsDir: "static",
  productionSourceMap: false,
  devServer: {
    port: port,
    // host: "localhost",
    // 自动打开浏览器
    open: false,
    overlay: {
      warnings: false,
      errors: true,
    },
  },
  configureWebpack: {
    // 在webpack的name字段中提供应用的标题，这样就可以在index.html中访问它以注入正确的标题。
    name: name,
    resolve: {
      alias: {
        "@": resolve("src"),
      },
    },
  },
};
