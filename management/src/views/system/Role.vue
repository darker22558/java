<template>
  <div>
    <div style="margin-left: 2px; display: flex; justify-content: space-between">
      <el-input placeholder="请输入名称" v-model="queryInfo.name" clearable style="width: 180px" suffix-icon="el-icon-document-remove"></el-input>
      <el-input placeholder="请输入描述" v-model="queryInfo.description" clearable style="width: 180px; margin-left: 10px" suffix-icon="el-icon-document-remove"></el-input>
      <el-input placeholder="请输入唯一标识" v-model="queryInfo.flag" clearable style="width: 180px; margin-left: 10px" suffix-icon="el-icon-document-remove"></el-input>
      <el-button @click.native.prevent="loadRoleList" style="margin-left: 10px" type="primary">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="addRole">新增</el-button>
    </div>
    <div style="margin-left: 2px; margin-top: 3px">
      <el-table :data="roleList" border stripe v-loading="loading" :height="400">
        <!--        <el-table-column type="selection" width="40"></el-table-column>-->
        <!--        <el-table-column label="序号" prop="id" width="50"></el-table-column>-->
        <el-table-column label="名称" prop="name" width="200"></el-table-column>
        <el-table-column label="描述" prop="description" width="300"></el-table-column>
        <el-table-column label="唯一标识" prop="flag" width="300"></el-table-column>
        <el-table-column label="操作" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="success" icon="el-icon-edit" @click="editRole(scope.row)"></el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteRole(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="40%">
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="roleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="roleForm.description"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="roleForm.flag"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateRole">确 定</el-button>
      </div>
    </el-dialog>
    <div style="padding: 10px 0">
      <el-pagination
        background
        layout="total, prev, pager, next"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {
  getRoleList,
  deleteRoleById,
  saveOrUpdate,
} from "@/api/system/role";

export default {
  name: "Role",
  data() {
    return {
      queryInfo: {
        name: "",
        description: "",
        flag: ""
      },
      roleForm: {},
      total: 0,
      roleList: [],
      loading: false,
      dialogFormVisible: false,
    };
  },
  created() {
    this.loadRoleList();
  },
  methods: {
    // 查询角色列表
    loadRoleList() {
      this.loading = true;
      getRoleList(this.queryInfo).then((res) => {
        this.roleList = res.data.roleList;
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 清空查询条件查询所有角色
    reset() {
      this.queryInfo.name = "";
      this.queryInfo.description = "";
      this.queryInfo.flag = "";
      this.loadRoleList();
    },
    addRole() {
      this.roleForm = {};
      this.dialogFormVisible = true;
    },
    editRole(row) {
      this.roleForm = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true;
    },
    saveOrUpdateRole() {
      saveOrUpdate(this.roleForm).then((res) => {
              this.$message.success(res.message);
              this.dialogFormVisible = false;
              this.loadRoleList();
            })
    },
    // 根据id删除角色
    deleteRole(id) {
      this.$confirm("此操作将永久删除该角色,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deleteRoleById(id).then((res) => {
            this.$message.success(res.message);
            this.loadRoleList();
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

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  /*line-height: 100px;*/
  text-align: center;
}
.avatar {
  width: 100px;
  height: 100px;
  display: block;
}
</style>
