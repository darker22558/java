<template>
  <div>
    <div style="margin-left: 2px; display: flex; justify-content: space-between">
      <el-input placeholder="请输入所获荣誉名称" v-model="queryInfo.honorName" clearable style="width: 180px" suffix-icon="el-icon-document-remove"></el-input>
      <el-input placeholder="请输入获奖成果名称" v-model="queryInfo.achievementName" clearable style="width: 180px; margin-left: 10px" suffix-icon="el-icon-document-remove"></el-input>
      <el-input placeholder="请输入参与人姓名" v-model="queryInfo.participantsRank" clearable style="width: 180px; margin-left: 10px" suffix-icon="el-icon-document-remove"></el-input>
      <el-button @click.native.prevent="loadHonorList" style="margin-left: 10px" type="primary">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="addHonor">新增</el-button>
      <el-button type="danger" @click="deleteHonorBatch">批量删除</el-button>
    </div>
    <div style="margin-left: 2px; margin-top: 3px">
      <el-table :data="honorList" border stripe v-loading="loading" :height="420" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
<!--        <el-table-column label="序号" prop="id" width="50"></el-table-column>-->
        <el-table-column label="荣誉名称" prop="honorName" width="240"></el-table-column>
        <el-table-column label="成果名称" prop="achievementName" width="500" show-overflow-tooltip> </el-table-column>
        <el-table-column label="实验室人员排名" prop="participantsRank" width="300"></el-table-column>
        <el-table-column label="操作" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="success" icon="el-icon-edit" @click="editHonor(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteHonor(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="荣誉信息" :visible.sync="dialogFormVisible" width="40%">
      <el-form label-width="120px" size="small">
        <el-form-item label="所获荣誉名称">
          <el-input v-model="honorForm.honorName"></el-input>
        </el-form-item>
        <el-form-item label="获奖成果名称">
          <el-input v-model="honorForm.achievementName"></el-input>
        </el-form-item>
        <el-form-item label="实验室人员排名">
          <el-input v-model="honorForm.participantsRank"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateHonor">确 定</el-button>
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
  deleteHonorBatchByIds,
  deleteHonorById,
  getHonorList,
  saveOrUpdate,
} from "@/api/achievement/honor";

export default {
  name: "Honor",
  data() {
    return {
      queryInfo: {
        honorName: "",
        achievementName: "",
        participantsRank: "",
        pageNum: 1,
        pageSize: 10,
      },
      honorForm: {},
      total: 0,
      honorList: [],
      loading: false,
      // 复选框选中的值列表
      selected: [],
      // 允许上传的荣誉文件类型
      ExcelFileType: ["xlsx", "xls"],
      // 运行上传文件大小，单位 M
      ExcelFileSize: 1,
      dialogFormVisible: false,
    };
  },
  created() {
    this.loadHonorList();
  },
  methods: {
    handleSizeChange(val) {
      // 每页显示的条数
      this.queryInfo.pageSize = val;
      this.loadHonorList();
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      // 显示第几页
      this.queryInfo.pageNum = val;
      this.loadHonorList();
      console.log(`当前页: ${val}`);
    },
    // 获取选中的值
    handleSelectionChange(selected) {
      this.selected = selected;
      // console.log('选中的值', selected.map((item) => item.id))
    },
    // 查询荣誉列表
    loadHonorList() {
      this.loading = true;
      getHonorList(this.queryInfo).then((res) => {
        this.honorList = res.data.honorList;
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 清空查询条件查询所有荣誉
    reset() {
      this.queryInfo.honorName = "";
      this.queryInfo.achievementName = "";
      this.queryInfo.participantsRank = "";
      this.queryInfo.pageNum = 1;
      this.queryInfo.pageSize = 10;
      this.loadHonorList();
    },
    addHonor() {
      this.dialogFormVisible = true;
      this.honorForm = {};
    },
    editHonor(row) {
      this.honorForm = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true;
    },
    saveOrUpdateHonor() {
      saveOrUpdate(this.honorForm)
        .then((res) => {
          this.$message.success(res.message);
          this.dialogFormVisible = false;
          this.loadHonorList();
        })
        .catch(() => {
          this.$message.error("保存失败");
        });
    },
    // 根据id删除荣誉
    deleteHonor(id) {
      this.$confirm("此操作将永久删除该荣誉,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deleteHonorById(id).then((res) => {
            this.$message.success(res.message);
            this.loadHonorList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除操作",
          });
        });
    },
    // 根据id批量删除荣誉
    deleteHonorBatch() {
      const ids = this.selected.map((item) => item.id).join();
      /* 根据后台想要的参数格式选择
      console.log(ids.join(",")); // string:1,2,3,4
      console.log(ids); // object:[1,2,3,4]
      */
      this.$confirm("此操作将永久删除所选的荣誉,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deleteHonorBatchByIds(ids).then((res) => {
            this.$message.success(res.message + ",ID为: " + ids);
            this.loadHonorList();
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
