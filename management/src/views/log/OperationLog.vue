<template>
  <div>
    <!--搜索-->
    <div style="padding: 10px">
      <!--<el-form-item label="操作时间">
        <DateTimeRangePicker :date="queryInfo.date" :set-date="setDate" />
      </el-form-item>-->
      <el-input placeholder="请输入操作描述" v-model="queryInfo.description" clearable style="width: 220px" suffix-icon="el-icon-document-remove"></el-input>
      <el-button type="primary" style="margin-left: 10px" @click="loadOperationLog">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px; width: 99%">
      <el-table :data="logList" border stripe :height="400">
        <el-table-column label="详情" type="expand" width="50">
          <template v-slot="props">
            <el-form label-position="left" inline class="table-expand">
              <el-form-item label="请求接口">
                <span>{{ props.row.uri }}</span>
              </el-form-item>
              <el-form-item label="请求参数">
                <span>{{ props.row.param }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="序号" type="index" width="50" />
        <el-table-column label="操作者" prop="username" width="80"/>
        <el-table-column label="请求方式" prop="method" width="80" />
        <el-table-column label="描述" prop="description" width="140" />
        <el-table-column label="ip" prop="ip" width="120"/>
        <el-table-column label="ip来源" prop="ipSource" show-overflow-tooltip />
        <el-table-column label="操作系统" prop="os" width="100"/>
        <el-table-column label="浏览器" prop="browser" show-overflow-tooltip />
        <el-table-column label="操作耗时" width="80">
          <template v-slot="scope">
            <el-tag size="small">{{ scope.row.times }}ms</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作时间" width="100">
          <template v-slot="scope">{{ scope.row.createTime.substring(0, 10) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template v-slot="scope">
            <el-popconfirm title="确定删除吗？" icon="el-icon-delete" icon-color="red" @onConfirm="deleteLogById(scope.row.id)">
              <el-button slot="reference" size="mini" type="danger" icon="el-icon-delete">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!--分页-->
    <div style="padding: 10px 0">
      <el-pagination
        background
        :current-page="queryInfo.pageNum"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="queryInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import {
  getOperationLogList,
  deleteOperationLogById,
} from "@/api/log/operation";

export default {
  name: "OperationLog",
  data() {
    return {
      queryInfo: {
        description: "",
        pageNum: 1,
        pageSize: 10,
      },
      logList: [],
      total: 0,
    };
  },
  created() {
    this.loadOperationLog();
  },
  methods: {
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize;
      this.loadOperationLog();
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage;
      this.loadOperationLog();
    },
    loadOperationLog() {
      /*const query = { ...this.queryInfo };
      if (query.date && query.date.length === 2) {
        query.date = query.date[0] + "," + query.date[1];
      }*/
      getOperationLogList(this.queryInfo).then((res) => {
        this.logList = res.data.operationLogList;
        this.total = res.data.total;
      });
    },
    // 清空查询条件查询所有操作日志
    reset() {
      this.queryInfo.description = "";
      this.queryInfo.pageNum = 1;
      this.queryInfo.pageSize = 10;
      this.loadOperationLog();
    },
    deleteLogById(id) {
      deleteOperationLogById(id).then((res) => {
        this.$message.success(res.data.message);
        this.loadOperationLog();
      });
    },
    setDate(value) {
      this.queryInfo.date = value;
    },
  },
};
</script>

<style scoped>

</style>

<style>
.table-expand {
  font-size: 0;
  margin-left: 50px;
}
.table-expand label {
  width: 90px;
  color: #99a9bf;
}
.table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>
