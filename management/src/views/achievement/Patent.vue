<template>
  <div>
    <div style="margin-left: 2px; display: flex; justify-content: space-between">
      <el-input placeholder="请输入专利编号" v-model="queryInfo.number" clearable style="width: 180px" suffix-icon="el-icon-document-remove"></el-input>
      <el-input placeholder="请输入专利名称" v-model="queryInfo.title" clearable style="width: 180px; margin-left: 10px" suffix-icon="el-icon-document-remove"></el-input>
      <el-input placeholder="请输入完成人" v-model="queryInfo.finisher" clearable style="width: 180px; margin-left: 10px" suffix-icon="el-icon-document-remove"></el-input>
      <el-button @click.native.prevent="loadPatentList" style="margin-left: 10px" type="primary">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="addPatent">新增</el-button>
      <el-button type="danger" @click="deletePatentBatch">批量删除</el-button>
    </div>
    <div style="margin-left: 2px; margin-top: 3px">
      <el-table :data="patentList" border stripe v-loading="loading" :height="400" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column label="序号" prop="id" width="50"></el-table-column>
        <el-table-column label="编号" prop="number" width="180"></el-table-column>
        <el-table-column label="专利名称" prop="title" width="260" :show-overflow-tooltip="true"> </el-table-column>
        <el-table-column label="完成人(固定人员)" prop="finisher" width="140"></el-table-column>
        <el-table-column label="操作" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="success" icon="el-icon-edit" @click="editPatent(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deletePatent(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="专利信息" :visible.sync="dialogFormVisible" width="40%">
      <el-form label-width="100px" size="small">
        <el-form-item label="编号">
          <el-input v-model="patentForm.number"></el-input>
        </el-form-item>
        <el-form-item label="专利名称">
          <el-input v-model="patentForm.title"></el-input>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="patentForm.finisher"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdatePatent">确 定</el-button>
      </div>
    </el-dialog>
    <div style="padding: 10px 0">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pageNum"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="queryInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {
  deletePatentBatchByIds,
  deletePatentById,
  getPatentList,
  saveOrUpdate,
} from "@/api/achievement/patent";

export default {
  name: "Patent",
  data() {
    return {
      queryInfo: {
        number: "",
        title: "",
        finisher: "",
        pageNum: 1,
        pageSize: 10,
      },
      patentForm: {},
      total: 0,
      patentList: [],
      loading: false,
      // 复选框选中的值列表
      selected: [],
      // 允许上传的专利文件类型
      ExcelFileType: ["xlsx", "xls"],
      // 运行上传文件大小，单位 M
      ExcelFileSize: 1,
      dialogFormVisible: false,
    };
  },
  created() {
    this.loadPatentList();
  },
  methods: {
    handleSizeChange(val) {
      // 每页显示的条数
      this.queryInfo.pageSize = val;
      this.loadPatentList();
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      // 显示第几页
      this.queryInfo.pageNum = val;
      this.loadPatentList();
      console.log(`当前页: ${val}`);
    },
    // 获取选中的值
    handleSelectionChange(selected) {
      this.selected = selected;
      // console.log('选中的值', selected.map((item) => item.id))
    },
    // 查询专利列表
    loadPatentList() {
      this.loading = true;
      getPatentList(this.queryInfo).then((res) => {
        this.patentList = res.data.patentList;
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 清空查询条件查询所有专利
    reset() {
      this.queryInfo.number = "";
      this.queryInfo.title = "";
      this.queryInfo.finisher = "";
      this.queryInfo.pageNum = 1;
      this.queryInfo.pageSize = 10;
      this.loadPatentList();
    },
    addPatent() {
      this.dialogFormVisible = true;
      this.patentForm = {};
    },
    editPatent(row) {
      this.patentForm = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true;
    },
    saveOrUpdatePatent() {
      saveOrUpdate(this.patentForm)
        .then((res) => {
          this.$message.success(res.message);
          this.dialogFormVisible = false;
          this.loadPatentList();
        })
        .catch(() => {
          this.$message.error("保存失败");
        });
    },
    // 根据id删除专利
    deletePatent(id) {
      this.$confirm("此操作将永久删除该专利,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deletePatentById(id).then((res) => {
            this.$message.success(res.message);
            this.loadPatentList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除操作",
          });
        });
    },
    // 根据id批量删除专利
    deletePatentBatch() {
      const ids = this.selected.map((item) => item.id).join();
      /* 根据后台想要的参数格式选择
      console.log(ids.join(",")); // string:1,2,3,4
      console.log(ids); // object:[1,2,3,4]
      */
      this.$confirm("此操作将永久删除所选的专利,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deletePatentBatchByIds(ids).then((res) => {
            this.$message.success(res.message + ",ID为: " + ids);
            this.loadPatentList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消批量删除操作",
          });
        });
    },
  },
};
</script>

<style scoped></style>
