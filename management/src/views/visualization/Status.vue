<template>
  <div style="margin: 2px">
    <el-row :gutter="20" style="margin: 12px">
      <el-col :span="8">
        <el-card style="color: #f56c6c">
          <div class="el-icon-s-marketing">CPU型号</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ cpuName }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card style="color: #67c23a">
          <div class="el-icon-s-order">物理CPU数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ cpuNumber }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card style="color: #e6a23c">
          <div class="el-icon-s-opportunity">物理核心数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ coreNumber }}
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin: 12px">
      <el-col :span="12">
        <el-card style="color: #409eff">
          <div class="el-icon-s-management">内存相关信息</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ memoryInfo }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="color: #f56c6c">
          <div class="el-icon-s-marketing">操作系统信息</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ operationSystemInfo }}
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin: 12px">
      <el-col :span="12">
        <el-card>
          <div ref="gauge" style="height: 400px"></div>
        </el-card>
      </el-col>
      <!--      <el-col :span="12">-->
      <!--        <el-card>-->
      <!--          <div ref="projectType" style="height: 400px;"></div>-->
      <!--        </el-card>-->
      <!--      </el-col>-->
    </el-row>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { getSystemState } from "@/api/visualization/status";
export default {
  name: "Status",
  data() {
    return {
      systemInfo: "",
      operationSystemInfo: "",
      hardwareInfo: "",

      memoryInfo: "",
      totalMemory: "",
      availableMemory: "",

      cpuInfo: "",
      cpuName: "",
      cpuNumber: "",
      coreNumber: "",

      projectYear: null,
      projectYearOption: {
        title: {
          text: "各年份项目数量",
          // subtext: "各年份项目数量",
          left: "center",
        },
        tooltip: {
          trigger: "axis",
        },
        xAxis: {
          name: "年",
          type: "category",
          data: [],
        },
        yAxis: {
          name: "数量",
          type: "value",
        },
        series: [
          {
            data: [],
            type: "line",
            smooth: true,
          },
          {
            data: [],
            type: "bar",
            smooth: true,
            label: {
              show: true,
              position: "top",
              valueAnimation: true,
            },
          },
        ],
      },
      projectType: null,
      projectTypeOption: {
        title: {
          text: "不同分类中的项目数量",
          // subtext: "不同分类下项目数量",
          left: "center",
        },
        tooltip: {
          trigger: "item",
        },
        // legend: {
        //   left: "center",
        //   top: "bottom",
        // },
        series: [
          {
            name: "数量",
            type: "pie",
            radius: "40%",
            data: [],
            // 饼图图形上的文本标签
            label: {
              show: true,
              //   position: "inner", // 标签的位置
              //   fontWeight: 300,
              fontSize: 12, // 文字的字体大小
              //   color: "#000",
              //   formatter: "{d}%",
            },
          },
        ],
      },
      gauge: null,
      gaugeDataOption: {
        series: [
          {
            type: "gauge",
            startAngle: 90,
            endAngle: -270,
            pointer: {
              show: false,
            },
            progress: {
              show: true,
              overlap: false,
              roundCap: true,
              clip: false,
              itemStyle: {
                borderWidth: 1,
                borderColor: "#464646",
              },
            },
            axisLine: {
              lineStyle: {
                width: 40,
              },
            },
            splitLine: {
              show: false,
              distance: 0,
              length: 10,
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              show: false,
              distance: 50,
            },
            data: [
              {
                value: 0,
                name: "内存使用率",
                title: {
                  offsetCenter: ["0%", "-15%"],
                },
                detail: {
                  valueAnimation: true,
                  offsetCenter: ["0%", "15%"],
                },
              },
            ],
            title: {
              fontSize: 18,
            },
            detail: {
              width: 60,
              height: 20,
              fontSize: 12,
              color: "inherit",
              borderColor: "inherit",
              borderRadius: 20,
              borderWidth: 1,
              formatter: "{value}%",
            },
          },
        ],
      },
    };
  },
  // 页面元素渲染之后再触发
  mounted() {
    // 进入界面后自动刷新统计数据
    this.refresh();
  },
  methods: {
    refresh() {
      getSystemState().then((res) => {
        // 系统信息
        this.systemInfo = res.data.systemInfo;
        // 操作系统信息
        this.operationSystemInfo = res.data.operationSystemInfo;
        // 硬件信息
        this.hardwareInfo = res.data.hardwareInfo;

        // 内存相关信息
        this.memoryInfo = res.data.memoryInfo;
        // 内存总容量
        this.totalMemory = res.data.totalMemory;
        // 可用内存的容量
        this.availableMemory = res.data.availableMemory;

        // CPU相关信息
        // this.cpuInfo = res.data.processor;
        // CPU名字
        this.cpuName = res.data.processorName;
        // 物理CPU数
        this.cpuNumber = res.data.physicalPackageCount;
        // 物理核心数
        this.coreNumber = res.data.physicalProcessorCount;
        console.log(res.data.availableMemory)
        console.log(res.data.totalMemory)
        this.gaugeDataOption.series[0].data[0].value = ((res.data.availableMemory / res.data.totalMemory) * 100).toFixed(2);
        this.gauge = echarts.init(this.$refs.gauge);
        this.gauge.setOption(this.gaugeDataOption);

        // 各年份项目数量柱状图
        // this.projectYearOption.xAxis.data = Object.keys(res.data.projectYearCount);
        // this.projectYearOption.series[0].data = Object.values(res.data.projectYearCount);
        // this.projectYearOption.series[1].data = Object.values(res.data.projectYearCount);
        // this.projectYear = echarts.init(this.$refs.projectYear);
        // this.projectYear.setOption(this.projectYearOption);

        // 不同分类下项目数量，饼图
        // this.projectTypeOption.series[0].data = res.data.projectTypeList;
        // this.projectType = echarts.init(this.$refs.projectType);
        // this.projectType.setOption(this.projectTypeOption);
      });
    },
  },
};
</script>

<style scoped></style>
