<template>
  <div style="margin: 20px">
<!--    <el-row :gutter="20" style="margin: 20px">
      <el-col :span="6">
        <el-card style="color: #409eff">
          <div class="el-icon-s-management">总PV</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ totalPageView }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #f56c6c">
          <div class="el-icon-s-marketing">日PV</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ todayPageView }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67c23a">
          <div class="el-icon-user-solid">总UV</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ totalUniqueVisitor }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #e6a23c">
          <div class="el-icon-s-opportunity">日UV</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ todayUniqueVisitor }}
          </div>
        </el-card>
      </el-col>
    </el-row>-->
    <el-row :gutter="20" style="margin: 20px">
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
    <el-row :gutter="20" style="margin: 20px">
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
import { getStatisticalData } from "@/api/visualization/statistic";
export default {
  name: "Statistic",
  data() {
    return {
      totalPageView: 0,
      todayPageView: 0,
      totalUniqueVisitor: 0,
      todayUniqueVisitor: 0,
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
      getStatisticalData().then((res) => {
        // 总页面访问量
        // this.totalPageView = res.data.totalPageView;
        // 今日页面访问量
        // this.todayPageView = res.data.todayPageView;
        // 总独立访客数
        // this.totalUniqueVisitor = res.data.totalUniqueVisitor;
        // 今日独立访客数
        // this.todayUniqueVisitor = res.data.todayUniqueVisitor;
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
