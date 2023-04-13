<template>
  <div style="margin: 2px">
    <el-row :gutter="20" style="margin: 12px">
      <el-col :span="12">
        <el-card style="color: #409eff">
          <div class="el-icon-s-management">总PV</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ totalPageView }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="color: #f56c6c">
          <div class="el-icon-s-marketing">日PV</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ todayPageView }}
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin: 12px">
      <el-col :span="12">
        <el-card style="color: #67c23a">
          <div class="el-icon-user-solid">总UV</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ totalUniqueVisitor }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="color: #e6a23c">
          <div class="el-icon-s-opportunity">日UV</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            {{ todayUniqueVisitor }}
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
// import * as echarts from "echarts";
import { getStatisticalData } from "@/api/visualization/statistic";
export default {
  name: "Statistic",
  data() {
    return {
      totalPageView: 0,
      todayPageView: 0,
      totalUniqueVisitor: 0,
      todayUniqueVisitor: 0,
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
        this.totalPageView = res.data.totalPageView;
        // 今日页面访问量
        this.todayPageView = res.data.todayPageView;
        // 总独立访客数
        this.totalUniqueVisitor = res.data.totalUniqueVisitor;
        // 今日独立访客数
        this.todayUniqueVisitor = res.data.todayUniqueVisitor;
      });
    },
  },
};
</script>

<style scoped></style>
