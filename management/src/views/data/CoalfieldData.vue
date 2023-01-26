<template>
  <div>
    <div style="padding: 10px">
      <el-input placeholder="请输入煤田名称" v-model="queryInfo.coalfieldName" clearable style="width: 220px" suffix-icon="el-icon-document-remove"></el-input>
      <el-select v-model="queryInfo.coalCoveringArea" clearable placeholder="请选择聚煤区">
        <el-option v-for="item in allCoalCoveringArea" :key="item.areaId" :label="item.areaName" :value="item.areaName">
          {{ item.areaName }}
        </el-option>
      </el-select>
      <el-button @click.native.prevent="loadCoalfieldList" style="margin-left: 10px" type="primary">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin-left: 10px; width: 50%; display: flex; justify-content: space-between">
      <el-button type="primary" @click="addCoalfield" class="el-icon-circle-plus-outline"> 新增</el-button>
      <el-button type="danger" @click="deleteCoalfieldBatch" class="el-icon-remove-outline"> 批量删除</el-button>
    </div>
    <div style="margin: 10px; width: 99%">
      <el-table :data="coalfieldList" border stripe v-loading="loading" :height="660" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"> </el-table-column>
<!--        <el-table-column label="序号" prop="id" width="50"> </el-table-column>-->
        <el-table-column label="煤田名称" prop="coalfieldName" width="100"> </el-table-column>
        <el-table-column label="所属省份" prop="province" width="80"> </el-table-column>
        <el-table-column label="所属聚煤区" prop="coalCoveringArea" width="140" > </el-table-column>
        <el-table-column label="成煤期" prop="coalFormingPeriods" width="80"> </el-table-column>
        <el-table-column label="样本数量" prop="sampleNumber" width="80"> </el-table-column>
        <el-table-column label="灰分产量" prop="ashYield" width="100">
          <template slot="header"> <span>灰分产量</span>
            <el-tooltip class="item" effect="dark" content="（ %; Dry Basis）" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="相关论文" prop="relevantPaper" width="240" :show-overflow-tooltip="true"> </el-table-column>
        <el-table-column label="备注" prop="remark" width="60" :show-overflow-tooltip="true"> </el-table-column>
        <el-table-column label="操作" fixed="right">
          <template v-slot="scope">
            <el-button type="success" icon="el-icon-edit" @click="editCoalfield(scope.row)"> 编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteCoalfield(scope.row.id)"> 删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="煤田信息" :visible.sync="dialogFormVisible" width="40%">
      <el-form label-width="180px" size="small">
        <el-form-item label="煤田名称">
          <el-input v-model="coalfieldForm.coalfieldName"></el-input>
        </el-form-item>
        <el-form-item label="所属省份">
          <el-input v-model="coalfieldForm.province"></el-input>
        </el-form-item>
        <el-form-item label="所属聚煤区">
          <el-select v-model="coalfieldForm.coalCoveringArea" clearable placeholder="请选择聚煤区">
            <el-option v-for="item in allCoalCoveringArea" :key="item.areaId" :label="item.areaName" :value="item.areaName">
              {{ item.areaName }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="成煤期">
          <el-input v-model="coalfieldForm.coalFormingPeriods"></el-input>
        </el-form-item>
        <el-form-item label="样本数量">
          <el-input v-model="coalfieldForm.sampleNumber"></el-input>
        </el-form-item>
        <el-form-item label="灰分产量（ %; Dry Basis）">
          <el-input v-model="coalfieldForm.ashYield"></el-input>
        </el-form-item>
        <el-form-item label="相关论文">
          <el-input v-model="coalfieldForm.relevantPaper" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="coalfieldForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateCoalfield">确 定</el-button>
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
import {getCoalfieldList, saveOrUpdate, deleteCoalfieldById, deleteCoalfieldBatchByIds } from "@/api/data/coalfield";

export default {
  name: "CoalfieldData",
  data() {
    return {
      queryInfo: {
        coalfieldName: "",
        coalCoveringArea: "",
        pageNum: 1,
        pageSize: 10,
      },
      allCoalCoveringArea: [
        {
          areaId: "1",
          areaName: "北部区域",
        },
        {
          areaId: "2",
          areaName: "西北区域",
        },
        {
          areaId: "3",
          areaName: "西藏-云南西部区域",
        },
        {
          areaId: "4",
          areaName: "东北区域",
        },
        {
          areaId: "5",
          areaName: "南部区域",
        }
      ],
      coalfieldForm: {},
      total: 0,
      coalfieldList: [],
      loading: false,
      // 复选框选中的值列表
      selected: [],
      // 允许上传的煤田文件类型
      ExcelFileType: ["xlsx", "xls"],
      // 运行上传文件大小，单位 M
      ExcelFileSize: 1,
      dialogFormVisible: false,
    };
  },
  created() {
    this.loadCoalfieldList();
  },
  methods: {
    handleSizeChange(val) {
      // 每页显示的条数
      this.queryInfo.pageSize = val;
      this.loadCoalfieldList();
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      // 显示第几页
      this.queryInfo.pageNum = val;
      this.loadCoalfieldList();
      console.log(`当前页: ${val}`);
    },
    // 获取选中的值
    handleSelectionChange(selected) {
      this.selected = selected;
      console.log(
        "选中的值",
        this.selected.map((item) => item.id)
      );
    },
    // 查询煤田列表
    loadCoalfieldList() {
      this.loading = true;
      getCoalfieldList(this.queryInfo).then((res) => {
        this.coalfieldList = res.data.coalfieldList;
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 清空查询条件查询所有煤田
    reset() {
      this.queryInfo.title = "";
      this.queryInfo.issn = "";
      this.loadCoalfieldList();
    },
    addCoalfield() {
      this.dialogFormVisible = true;
      this.coalfieldForm = {};
    },
    editCoalfield(row) {
      this.coalfieldForm = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true;
    },
    saveOrUpdateCoalfield() {
      saveOrUpdate(this.coalfieldForm)
        .then((res) => {
          this.$message.success(res.message);
          this.dialogFormVisible = false
          this.loadCoalfieldList();
        })
        .catch(() => {
          this.$message.error("保存失败");
        });
    },
    // 根据id删除煤田
    deleteCoalfield(id) {
      this.$confirm("此操作将永久删除该煤田信息,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deleteCoalfieldById(id).then((res) => {
            this.$message.success(res.message);
            this.loadCoalfieldList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除操作",
          });
        });
    },
    // 根据id批量删除煤田
    deleteCoalfieldBatch() {
      const ids = this.selected.map((item) => item.id);
      /* 根据后台想要的参数格式选择
      console.log(ids.join(",")); // string:1,2,3,4
      console.log(ids); // object:[1,2,3,4]
      */
      this.$confirm("此操作将永久删除所选的煤田信息,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deleteCoalfieldBatchByIds(ids).then((res) => {
            this.$message.success(res.message + ",ID为: " + ids);
            this.loadCoalfieldList();
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
