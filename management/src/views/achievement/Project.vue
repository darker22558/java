<template>
  <div>
    <div style="padding: 10px">
      <el-input placeholder="请输入项目名称" v-model="queryInfo.title" clearable style="width: 220px" suffix-icon="el-icon-document-remove"></el-input>
      <el-input placeholder="请输入负责人" v-model="queryInfo.chargePerson" clearable style="width: 220px; margin-left: 10px" suffix-icon="el-icon-document-remove"></el-input>
      <el-button @click.native.prevent="loadProjectList" style="margin-left: 10px" type="primary">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="addProject" class="el-icon-circle-plus-outline"> 新增</el-button>
      <el-button type="danger" @click="deleteProjectBatch" class="el-icon-remove-outline"> 批量删除</el-button>
    </div>
    <div style="margin: 10px; width: 99%">
      <el-table :data="projectList" border stripe v-loading="loading" :height="400" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
<!--        <el-table-column label="序号" prop="id" width="50"></el-table-column>-->
        <el-table-column label="编号" prop="number" width="140"></el-table-column>
        <el-table-column label="项目名称" prop="title" width="200" :show-overflow-tooltip="true"> </el-table-column>
        <el-table-column label="负责人" prop="chargePerson" width="70"></el-table-column>
        <el-table-column label="类别" prop="type" width="140" :show-overflow-tooltip="true"> </el-table-column>
        <el-table-column label="开始日期" prop="startDate" width="80"></el-table-column>
        <el-table-column label="终止日期" prop="endDate" width="80"></el-table-column>
        <el-table-column label="经费(万元)" prop="funds" width="90"> </el-table-column>
        <el-table-column label="经费来源" prop="fundsSource" width="80" :show-overflow-tooltip="true"> </el-table-column>
        <el-table-column label="操作" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="success" icon="el-icon-edit" @click="editProject(scope.row)"></el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteProject(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="项目信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small">
        <el-form-item label="编号">
          <el-input v-model="projectForm.number"></el-input>
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input v-model="projectForm.title"></el-input>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="projectForm.chargePerson"></el-input>
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="projectForm.startDate" format="yyyy 年 M 月 dd 日"></el-date-picker>
        </el-form-item>
        <el-form-item label="终止日期">
          <el-date-picker v-model="projectForm.endDate" format="yyyy 年 M 月 dd 日"></el-date-picker>
        </el-form-item>
        <el-form-item label="类别">
          <el-input v-model="projectForm.type"></el-input>
        </el-form-item>
        <el-form-item label="经费">
          <el-input v-model="projectForm.funds"></el-input>
        </el-form-item>
        <el-form-item label="经费来源">
          <el-input v-model="projectForm.fundsSource"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateProject">确 定</el-button>
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
  deleteProjectBatchByIds,
  deleteProjectById,
  getProjectList,
  saveOrUpdate,
} from "@/api/achievement/project";

export default {
  name: "Project",
  data() {
    return {
      queryInfo: {
        title: "",
        chargePerson: "",
        pageNum: 1,
        pageSize: 10,
      },
      projectForm: {},
      total: 0,
      projectList: [],
      loading: false,
      // 复选框选中的值列表
      selected: [],
      // 允许上传的文献文件类型
      ExcelFileType: ["xlsx", "xls"],
      // 运行上传文件大小，单位 M
      ExcelFileSize: 1,
      dialogFormVisible: false,
    };
  },
  created() {
    this.loadProjectList();
  },
  methods: {
    handleSizeChange(val) {
      // 每页显示的条数
      this.queryInfo.pageSize = val;
      this.loadProjectList();
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      // 显示第几页
      this.queryInfo.pageNum = val;
      this.loadProjectList();
      console.log(`当前页: ${val}`);
    },
    // 获取选中的值
    handleSelectionChange(selected) {
      this.selected = selected;
      // console.log('选中的值', selected.map((item) => item.id))
    },
    // 查询文献列表
    loadProjectList() {
      this.loading = true;
      getProjectList(this.queryInfo).then((res) => {
        this.projectList = res.data.projectList;
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 清空查询条件查询所有文献
    reset() {
      this.queryInfo.title = "";
      this.queryInfo.chargePerson = "";
      this.queryInfo.pageNum = 1;
      this.queryInfo.pageSize = 10;
      this.loadProjectList();
    },
    addProject() {
      this.dialogFormVisible = true;
      this.projectForm = {};
    },
    editProject(row) {
      this.projectForm = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true;
    },
    saveOrUpdateProject() {
      saveOrUpdate(this.projectForm)
        .then((res) => {
          this.$message.success(res.message);
          this.dialogFormVisible = false;
          this.loadProjectList();
        })
        .catch(() => {
          this.$message.error("保存失败");
        });
    },
    // 根据id删除文献
    deleteProject(id) {
      this.$confirm("此操作将永久删除该文献,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deleteProjectById(id).then((res) => {
            this.$message.success(res.message);
            this.loadProjectList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除操作",
          });
        });
    },
    // 根据id批量删除文献
    deleteProjectBatch() {
      const ids = this.selected.map((item) => item.id).join();
      /* 根据后台想要的参数格式选择
      console.log(ids.join(",")); // string:1,2,3,4
      console.log(ids); // object:[1,2,3,4]
      */
      this.$confirm("此操作将永久删除所选的文献,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deleteProjectBatchByIds(ids).then((res) => {
            this.$message.success(res.message + ",ID为: " + ids);
            this.loadProjectList();
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
