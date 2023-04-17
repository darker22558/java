<template>
  <div>
    <div>
      <el-button @click="goBack()"> 返回上一层 </el-button>
    </div>
    <div :id="id" class="provinceCharts"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
export default {
  name: "Province",
  data() {
    return {
      id: "echarts_" + new Date().getTime() + Math.floor(Math.random() * 1000),
      echartObj: null,
      option: {
        title: {
          text: "",
          top: "8%",
          left: "8%",
          textStyle: {
            fontSize: 14,
            fontWeight: 300,
            color: "#000",
          },
        },
        tooltip: {
          padding: 0,
          // 数据格式化
          formatter: function (params) {
            return params.name + "：" + (params.value || 0);
          },
        },
        legend: {
          orient: "vertical",
          top: "9%",
          left: "5%",
          icon: "circle",
          data: [],
          selectedMode: "single",
          selected: {},
          itemWidth: 12,
          itemHeight: 12,
          itemGap: 30,
          inactiveColor: "#b6d7ff",
          textStyle: {
            color: "#ec808d",
            fontSize: 12,
            fontWeight: 300,
            padding: [0, 0, 0, 15],
          },
        },
        visualMap: {
          min: 0,
          max: 500,
          left: "left",
          top: "bottom",
          // 取值范围的文字
          text: ["高", "低"],
          inRange: {
            // 取值范围的颜色
            color: ["#e0ffff", "blue"],
          },
          // 图注
          show: true,
        },
        geo: {
          map: "",
          // 不开启缩放和平移
          roam: false,
          // 视角缩放比例
          zoom: 0.8,
          label: {
            normal: {
              show: true,
              fontSize: 10,
              color: "#000",
            },
            emphasis: {
              show: true,
              color: "blue",
            },
          },
          itemStyle: {
            normal: {
              borderColor: "rgba(0, 0, 0, 0.2)",
            },
            emphasis: {
              areaColor: "skyblue", // 鼠标选择区域颜色
              shadowOffsetX: 0,
              shadowOffsetY: 0,
              shadowBlur: 20,
              borderWidth: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)",
            },
          },
          left: "5%",
          right: "5%",
          top: "5%",
          bottom: "5%",
        },
        series: [
          {
            name: "省份数据",
            type: "map",
            geoIndex: 0, // 不可缺少，否则无tooltip 指示效果
            data: [],
          },
        ],
        provinceJSON: {},
        provinceName: "",
      },
    };
  },
  mounted() {
    this.initDate();
    this.resizeListener();
  },
  methods: {
    initDate() {
      const provinceName = this.$route.query.provinceName;
      const province = this.$route.query.province;
      this.provinceName = provinceName;
      this.provinceJSON = require("../../../utils/map/省份数据/json(省份)/" +
        provinceName);
      this.option.title.text = province;
      this.option.geo.map = province;
      this.echartObj = echarts.init(document.getElementById(this.id));
      echarts.registerMap(province, this.provinceJSON);
      this.echartObj.setOption(this.option);
      this.echartObj.on("click", (params) => {
        this.$router.push({
          path: "/visualization/city",
          query: { city: params.name },
        });
      });
    },
    resizeListener() {
      window.addEventListener("resize", () => {
        if (this.echartObj && this.echartObj.resize) {
          this.echartObj.resize();
        }
      });
    },
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>
<style>
.provinceCharts {
  height: 600px;
  width: 900px;
  margin: auto;
}
</style>
