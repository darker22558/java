<template>
  <div>
    <!--搜索-->
    <div style="margin-left: 2px;">
      <el-input placeholder="请输入操作描述" v-model="queryInfo.description" clearable style="width: 220px" suffix-icon="el-icon-document-remove"></el-input>
      <el-input placeholder="请输入异常信息" v-model="queryInfo.error" clearable style="margin-left: 10px; width: 220px" suffix-icon="el-icon-document-remove"></el-input>
      <el-button type="primary" style="margin-left: 10px" @click="loadExceptionLog">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin-left: 2px; margin-top: 3px">
      <el-table :data="logList" border stripe :height="420">
        <el-table-column label="详情" type="expand" width="50">
          <template v-slot="props">
            <el-form label-position="left" inline class="table-expand">
              <el-form-item label="请求接口">
                <span>{{ props.row.uri }}</span>
              </el-form-item>
              <el-form-item label="请求参数">
                <span>{{ props.row.param }}</span>
              </el-form-item>
              <el-form-item label="异常信息">
                <el-input v-model="props.row.error" type="textarea" :autosize="{ minRows: 2, maxRows: 4}"></el-input>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="请求方式" prop="method" width="80" />
        <el-table-column label="操作描述" prop="description" width="80" />
        <el-table-column label="ip" prop="ip" width="120"/>
        <el-table-column label="ip来源" prop="ipSource" width="100" show-overflow-tooltip/>
        <el-table-column label="操作系统" prop="os" width="100" show-overflow-tooltip/>
        <el-table-column label="浏览器" prop="browser" show-overflow-tooltip />
        <el-table-column label="用户代理" prop="userAgent" width="80" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作时间" width="100">
          <template v-slot="scope">{{ scope.row.createTime.substring(0, 10) }}</template>
        </el-table-column>
        <el-table-column label="操作" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteLogById(scope.row.id)"></el-button>
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
  deleteExceptionLogById,
  getExceptionLogList,
} from "@/api/log/exception";

export default {
  name: "ExceptionLog",
  data() {
    return {
      queryInfo: {
        description: "",
        error: "",
        pageNum: 1,
        pageSize: 10,
      },
      logList: [],
      total: 0,
    };
  },
  created() {
    this.loadExceptionLog();
  },
  methods: {
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize;
      this.loadExceptionLog();
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage;
      this.loadExceptionLog();
    },
    loadExceptionLog() {
      getExceptionLogList(this.queryInfo).then((res) => {
        this.logList = res.data.exceptionLogList;
        this.total = res.data.total;
      });
    },
    // 清空查询条件查询所有异常日志
    reset() {
      this.queryInfo.description = "";
      this.queryInfo.error = "";
      this.queryInfo.pageNum = 1;
      this.queryInfo.pageSize = 10;
      this.loadExceptionLog();
    },
    deleteLogById(id) {
      this.$confirm("此操作将永久删除该日志,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deleteExceptionLogById(id).then((res) => {
            this.$message.success(res.message);
            this.loadExceptionLog();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除操作",
          });
        });
    },
  },
};
</script>

<style scoped></style>

<style>
.table-expand {
  font-size: 0;
  margin-left: 50px;
}
.table-expand label {
  width: 90px;
  color: #99a9bf;
}
</style>
