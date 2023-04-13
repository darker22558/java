<template>
  <div style="margin: 2px">
    <el-row :gutter="20" style="margin: 12px">
      <el-col :span="6">
        <el-card style="color: #409eff">
          <div class="el-icon-s-management">荣誉总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ totalHonor }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #f56c6c">
          <div class="el-icon-s-marketing">科研项目总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ totalProject }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67c23a">
          <div class="el-icon-s-order">发表论文总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ totalPaperPublished }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #e6a23c">
          <div class="el-icon-s-opportunity">发明专利总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ totalPatent }}
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin: 12px">
      <el-col :span="12">
        <el-card>
          <div ref="projectYear" style="height: 400px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div ref="projectType" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { getAcademicData } from "@/api/visualization/academic";
export default {
  name: "Academic",
  data() {
    return {
      totalHonor: 0,
      totalProject: 0,
      totalPaperPublished: 0,
      totalPatent: 0,
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

    };
  },
  // 页面元素渲染之后再触发
  mounted() {
    // 进入界面后自动刷新统计数据
    this.refresh();
  },
  methods: {
    refresh() {
      getAcademicData().then((res) => {
        // 所获荣誉总数
        this.totalHonor = res.data.totalHonor;
        // 科研项目总数
        this.totalProject = res.data.totalProject;
        // 发表论文总数
        // this.totalPaperPublished = res.data.totalPaperPublished;
        // 发明专利总数
        this.totalPatent = res.data.totalPatent;

        // 各年份项目数量柱状图
        this.projectYearOption.xAxis.data = Object.keys(res.data.projectYearCount);
        this.projectYearOption.series[0].data = Object.values(res.data.projectYearCount);
        this.projectYearOption.series[1].data = Object.values(res.data.projectYearCount);
        this.projectYear = echarts.init(this.$refs.projectYear);
        this.projectYear.setOption(this.projectYearOption);

        // 不同分类下项目数量，饼图
        this.projectTypeOption.series[0].data = res.data.projectTypeList;
        this.projectType = echarts.init(this.$refs.projectType);
        this.projectType.setOption(this.projectTypeOption);
      });
    },
  },
};
</script>

<style scoped></style>
