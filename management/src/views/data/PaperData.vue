<template>
  <div>
    <div style="padding: 10px">
      <el-input placeholder="请输入标题" v-model="queryInfo.title" clearable style="width: 220px" suffix-icon="el-icon-document-remove"></el-input>
      <el-input placeholder="请输入标准国际刊号（issn）" v-model="queryInfo.issn" clearable style="width: 220px; margin-left: 10px" suffix-icon="el-icon-document-remove"></el-input>
      <el-button @click.native.prevent="loadPaperList" style="margin-left: 10px" type="primary">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="addPaper" class="el-icon-circle-plus-outline"> 新增</el-button>
      <el-button type="danger" @click="deletePaperBatch" class="el-icon-remove-outline"> 批量删除</el-button>
    </div>
    <div style="margin: 10px; width: 99%">
      <el-table :data="paperList" border stripe v-loading="loading" :height="400" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"> </el-table-column>
        <el-table-column label="序号" prop="id" width="50"> </el-table-column>
        <el-table-column label="eid" prop="eid" width="160"> </el-table-column>
        <el-table-column label="标题" prop="title" width="200" :show-overflow-tooltip="true"> </el-table-column>
        <el-table-column label="发表时间" prop="publicDate" width="100" :show-overflow-tooltip="true">
          <template v-slot="scope">{{ scope.row.publicDate.substring(0, 10) }}</template>
        </el-table-column>
        <el-table-column label="标准国际刊号" prop="issn" width="110"> </el-table-column>
        <el-table-column label="备注" prop="remark" width="50" :show-overflow-tooltip="true"> </el-table-column>
        <el-table-column label="操作" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="success" icon="el-icon-edit" @click="editPaper(scope.row)"> 编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deletePaper(scope.row.id)"> 删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="文献信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small">
        <el-form-item label="eid">
          <el-input v-model="paperForm.eid" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="paperForm.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="发表时间">
          <el-date-picker v-if="paperForm.id != null" v-model="paperForm.publicDate" disabled autocomplete="off" format="yyyy 年 M 月 dd 日"></el-date-picker>
          <el-date-picker v-else v-model="paperForm.publicDate" autocomplete="off" format="yyyy 年 M 月 dd 日"></el-date-picker>
        </el-form-item>
        <el-form-item label="标准国际刊号">
          <el-input v-model="paperForm.issn" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="paperForm.remark" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdatePaper">确 定</el-button>
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
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {
  getPaperList,
  deletePaperById,
  deletePaperBatchByIds,
  saveOrUpdate,
} from "@/api/data/paper";

export default {
  name: "PaperData",
  data() {
    return {
      queryInfo: {
        title: "",
        issn: "",
        pageNum: 1,
        pageSize: 10,
      },
      paperForm: {},
      total: 0,
      paperList: [],
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
    this.loadPaperList();
  },
  methods: {
    handleSizeChange(val) {
      // 每页显示的条数
      this.queryInfo.pageSize = val;
      this.loadPaperList();
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      // 显示第几页
      this.queryInfo.pageNum = val;
      this.loadPaperList();
      console.log(`当前页: ${val}`);
    },
    // 获取选中的值
    handleSelectionChange(selected) {
      this.selected = selected;
      // console.log('选中的值', selected.map((item) => item.id))
    },
    // 查询文献列表
    loadPaperList() {
      this.loading = true;
      getPaperList(this.queryInfo).then((res) => {
        this.paperList = res.data.paperList;
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 清空查询条件查询所有文献
    reset() {
      this.queryInfo.title = "";
      this.queryInfo.issn = "";
      this.queryInfo.pageNum = 1;
      this.queryInfo.pageSize = 10;
      this.loadPaperList();
    },
    addPaper() {
      this.dialogFormVisible = true;
      this.paperForm = {};
    },
    editPaper(row) {
      this.paperForm = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true;
    },
    saveOrUpdatePaper() {
      saveOrUpdate(this.paperForm)
        .then((res) => {
          this.$message.success(res.message);
          this.dialogFormVisible = false
          this.loadPaperList();
        })
        .catch(() => {
          this.$message.error("保存失败");
      })
    },
    // 根据id删除文献
    deletePaper(id) {
      this.$confirm("此操作将永久删除该文献,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deletePaperById(id).then((response) => {
            this.$message.success(response.data.message);
            this.loadPaperList();
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
    deletePaperBatch() {
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
          deletePaperBatchByIds(ids).then((response) => {
            this.$message.success(response.data.message + ",ID为: " + ids);
            this.loadPaperList();
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
