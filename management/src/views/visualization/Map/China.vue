<script>
import * as echarts from "echarts";
import { getCoalfieldInfoOfAllProvince } from "@/api/visualization/map";

const chinaJson = require("../../../utils/map/china.json");
export default {
  name: "China",
  render: function (createElement) {
    return createElement("div", {
      attrs: {
        id: "main",
      },
      style: {
        height: "100%",
        width: "100%",
        margin: "auto",
      },
    });
  },
  data() {
    return {
      coalfieldCountOfProvince: {},
      coalfieldList: [],
      dataList: [
        { ename: "nanhai", name: "南海诸岛" },
        { ename: "beijing", name: "北京" },
        { ename: "tianjin", name: "天津" },
        { ename: "shanghai", name: "上海" },
        { ename: "chongqing", name: "重庆" },
        { ename: "hebei", name: "河北" },
        { ename: "henan", name: "河南" },
        { ename: "yunnan", name: "云南" },
        { ename: "liaoning", name: "辽宁" },
        { ename: "heilongjiang", name: "黑龙江" },
        { ename: "hunan", name: "湖南" },
        { ename: "anhui", name: "安徽" },
        { ename: "shandong", name: "山东" },
        { ename: "xinjiang", name: "新疆" },
        { ename: "jiangsu", name: "江苏" },
        { ename: "zhejiang", name: "浙江" },
        { ename: "jiangxi", name: "江西" },
        { ename: "hubei", name: "湖北" },
        { ename: "guangxi", name: "广西" },
        { ename: "gansu", name: "甘肃" },
        { ename: "shanxi", name: "山西" },
        { ename: "neimenggu", name: "内蒙古" },
        { ename: "shanxi1", name: "陕西" },
        { ename: "jilin", name: "吉林" },
        { ename: "fujian", name: "福建" },
        { ename: "guizhou", name: "贵州" },
        { ename: "guangdong", name: "广东" },
        { ename: "qinghai", name: "青海" },
        { ename: "xizang", name: "西藏" },
        { ename: "sichuan", name: "四川" },
        { ename: "ningxia", name: "宁夏" },
        { ename: "hainan", name: "海南" },
        { ename: "taiwan", name: "台湾" },
        { ename: "xianggang", name: "香港" },
        { ename: "aomen", name: "澳门" },
      ],
    };
  },
  mounted() {
    this.getCoalfieldInfoOfAllProvince();
  },
  methods: {
    getCoalfieldInfoOfAllProvince() {
      getCoalfieldInfoOfAllProvince().then((res) => {
        this.coalfieldCountOfProvince = res.data.coalfieldCountOfProvince;
        this.coalfieldList = res.data.coalfieldList;
        this.initEchart();
      });
    },
    initEchart() {
      let dataList = this.dataList;
      for (let i = 0; i < dataList.length; i++) {
        dataList[i].value = 0;
        if (dataList[i].name in this.coalfieldCountOfProvince) {
          dataList[i].value = this.coalfieldCountOfProvince[dataList[i].name];
        }
      }
      const _this = this;
      var myChart = echarts.init(document.getElementById("main"));
      echarts.registerMap("china", chinaJson);
      var option = {
        tooltip: {
          //数据格式化
          formatter: function (params) {
            return (
              params.seriesName + "<br />" + params.name + "：" + params.value
            );
          },
        },
        visualMap: {
          min: 0,
          max: 20,
          left: "left",
          top: "bottom",
          //取值范围的文字
          text: ["高", "低"],
          inRange: {
            //取值范围的颜色
            color: ["#e0ffff", "blue"],
          },
          //图注
          show: true,
        },
        geo: {
          //引入地图数据
          map: "china",
          //不开启缩放和平移
          roam: false,
          //视角缩放比例
          zoom: 1.2,
          label: {
            normal: {
              show: true,
              fontSize: "12",
              color: "rgba(0,0,0,0.7)",
            },
          },
          itemStyle: {
            normal: {
              borderColor: "rgba(0, 0, 0, 0.2)",
            },
            emphasis: {
              //高亮的显示设置
              //鼠标选择区域颜色
              areaColor: "skyblue",
              shadowOffsetX: 0,
              shadowOffsetY: 0,
              shadowBlur: 20,
              borderWidth: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)",
            },
          },
        },
        // 鼠标悬浮提示框
        series: [
          {
            name: "省份",
            type: "map",
            geoIndex: 0,
            data: this.dataList,
          },
        ],
      };
      myChart.setOption(option);
      myChart.on("click", function (params) {
        if (!params.data.ename) {
          alert("暂无" + params.name + "地图数据");
          return;
        }
        _this.$router.push({
          path: "/visualization/province",
          query: { provinceName: params.data.ename, province: params.name },
        });
      });
    },
  },
};
</script>
