<template>
  <div>
    <div style="margin-left: 2px; display: flex; justify-content: space-between">
      <el-input placeholder="请输入标题(模糊)" v-model="queryInfo.title" clearable style="width: 160px"></el-input>
      <el-input placeholder="请输入issn" v-model="queryInfo.issn" clearable style="width: 120px; margin-left: 5px"></el-input>
      <el-button @click.native.prevent="loadPaperList" style="margin-left: 5px" type="primary">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="addPaper">新增</el-button>
      <el-button type="danger" @click="deletePaperBatch">批量删除</el-button>
      <el-button type="primary" @click="downloadTemplate">下载模板</el-button>
      <el-button type="primary" @click="exportBatch">批量导出</el-button>
      <el-upload action :http-request="importBatch" :on-exceed="handleExceed" :before-upload="beforeExcelUpload" :show-file-list="false">
        <el-button type="primary">批量导入</el-button>
      </el-upload>
    </div>
    <div style="margin-left: 2px; margin-top: 3px">
      <el-table id="papers" :data="paperList" border stripe v-loading="loading" :height="400" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"> </el-table-column>
<!--        <el-table-column label="序号" prop="id" width="50"> </el-table-column>-->
        <el-table-column label="eid" prop="eid" width="160"> </el-table-column>
        <el-table-column label="标题" prop="title" width="200" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="发表时间" width="260">
          <template v-slot="scope">
            <el-date-picker disabled v-model="scope.row.publicDate" size="small"></el-date-picker>
          </template>
        </el-table-column>
        <el-table-column label="issn" prop="issn" width="100"></el-table-column>
        <el-table-column label="备注" prop="remark" width="50" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="操作" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="success" icon="el-icon-edit" @click="editPaper(scope.row)"></el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deletePaper(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="文献信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small">
        <el-form-item label="eid">
          <el-input v-model="paperForm.eid"></el-input>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="paperForm.title" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="发表时间">
          <el-date-picker v-model="paperForm.publicDate"></el-date-picker>
        </el-form-item>
        <el-form-item label="标准国际刊号">
          <el-input v-model="paperForm.issn"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="paperForm.remark"></el-input>
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
        :total="total"
      >
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
  importDataBatch,
} from "@/api/data/paper";
import {downloadMethod} from "@/utils/request";

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
      ExcelFileSize: 5,
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
          this.dialogFormVisible = false;
          this.loadPaperList();
        })
        .catch(() => {
          this.$message.error("保存失败");
        });
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
            this.$message.success(response.message);
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
            this.$message.success(response.message + ",ID为: " + ids);
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
    // 下载模板
    downloadTemplate() {
      const url = `http://localhost:9090/management/data/paper/exportDataTemplate`;
      const filename = "文献数据模板.xlsx";
      downloadMethod(url, filename)
    },
    // 超出文件个数的回调
    handleExceed(files) {
      this.$message.warning(`超出上传数量限制！最多上传 1 个表格文件，选择了 ${files.length} 个文件`)
    },
    // 上传Excel文件之前
    beforeExcelUpload(file) {
      if (file.type !== "" || file.type != null || file.type !== undefined) {
        // 计算文件的大小
        const fileSize = file.size / 1024 / 1024
        // 这里做文件大小限制
        if (fileSize > this.ExcelFileSize) {
          this.$message("上传文件大小不能超过 5MB!");
          return false;
        }
        // 截取文件的后缀，判断文件类型
        const suffix = file.name.replace(/.+\./, "").toLowerCase();
        // 如果文件类型不在允许上传的范围内
        if (this.ExcelFileType.includes(suffix)) {
          return true;
        } else {
          this.$message.error("批量上传需要使用excel文件!");
          return false;
        }
      }
    },
    // 上传文件
    importBatch(item) {
      this.$message("数据上传中······");
      // 上传文件的需要formdata类型
      const FormDatas = new FormData();
      FormDatas.append("file", item.file);
      importDataBatch(FormDatas).then((res) => {
        this.$message.success(res.message);
        // 成功过后刷新列表，清空上传文件列表
        this.handleSuccess();
      });
    },
    // 上传成功后的回调
    handleSuccess() {
      this.loadPaperList()
    },
    // 批量导出数据
    exportBatch() {
      const url = `http://localhost:9090/management/data/paper/exportDataBatch`;
      const filename = "文献数据信息.xlsx";
      const query = {
        title: this.queryInfo.title,
        issn: this.queryInfo.issn,
      };
      downloadMethod(url, filename, query)
    },
  },
};
</script>

<style scoped></style>
