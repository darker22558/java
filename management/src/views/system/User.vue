<template>
  <div>
    <div style="margin-left: 2px; display: flex; justify-content: space-between">
      <el-input placeholder="请输入用户名" v-model="queryInfo.username" clearable style="width: 180px" suffix-icon="el-icon-document-remove"></el-input>
      <el-input placeholder="请输入昵称" v-model="queryInfo.nickname" clearable style="width: 180px; margin-left: 10px" suffix-icon="el-icon-document-remove"></el-input>
      <el-input placeholder="请输入邮箱" v-model="queryInfo.email" clearable style="width: 180px; margin-left: 10px" suffix-icon="el-icon-document-remove"></el-input>
      <el-select v-model="queryInfo.role" clearable placeholder="请选择角色" class="select">
        <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.flag">
          {{ item.name }}
        </el-option>
      </el-select>
      <el-button @click.native.prevent="loadUserList" style="margin-left: 10px" type="primary">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
      <el-button type="primary" @click="addUser">新增</el-button>
      <el-button type="danger" @click="deleteUserBatch">批量删除</el-button>
    </div>
    <div style="margin-left: 2px; margin-top: 3px">
      <el-table :data="userList" border stripe v-loading="loading" :height="420" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <!--        <el-table-column label="序号" prop="id" width="50"></el-table-column>-->
        <el-table-column label="用户名" prop="username" width="160"></el-table-column>
        <el-table-column label="昵称" prop="nickname" width="160"></el-table-column>
        <el-table-column label="头像地址" prop="avatar" width="200" show-overflow-tooltip></el-table-column>
        <el-table-column label="邮箱" prop="email" width="200"></el-table-column>
        <el-table-column label="创建时间" width="160">
          <template v-slot="scope">
            <el-date-picker style="width: 100%" disabled v-model="scope.row.createTime" size="small"></el-date-picker>
          </template>
        </el-table-column>
        <el-table-column label="更新时间" width="150">
          <template v-slot="scope">
            <el-date-picker style="width: 100%" disabled v-model="scope.row.updateTime" size="small"></el-date-picker>
          </template>
        </el-table-column>
        <el-table-column label="角色权限" prop="role" width="80" :formatter="formType"></el-table-column>
        <el-table-column label="操作" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="success" icon="el-icon-edit" @click="editUser(scope.row)"></el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteUser(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="40%">
      <el-form label-width="80px" size="small">
        <el-form-item label="头像">
          <el-input v-model="userForm.avatar"></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload class="avatar-uploader" action :http-request="uploadAvatar" :on-exceed="handleExceed" :before-upload="beforeAvatarUpload" :show-file-list="false">
            <img v-if="userForm.avatar" :src="userForm.avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" style="line-height: 100px;"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="userForm.username"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="userForm.nickname"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userForm.email"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="userForm.password"></el-input>
        </el-form-item>
        <el-form-item label="角色权限">
          <el-select v-model="userForm.role" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.flag">
              {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateUser">确 定</el-button>
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
  getUserList,
  deleteUserById,
  deleteUserBatchByIds,
  uploadAvatar,
  saveUser,
  updateUser
} from "@/api/system/user";
import {
  getRoleList,
} from "@/api/system/role";
export default {
  name: "User",
  data() {
    return {
      queryInfo: {
        username: "",
        nickname: "",
        email: "",
        role: "",
        pageNum: 1,
        pageSize: 10,
      },
      userForm: {},
      total: 0,
      userList: [],
      roleList: [],
      loading: false,
      // 复选框选中的值列表
      selected: [],
      // 允许上传的用户文件类型
      ExcelFileType: ["xlsx", "xls"],
      // 运行上传文件大小，单位 M
      ExcelFileSize: 1,
      dialogFormVisible: false,
    };
  },
  created() {
    this.loadRoleList();
    this.loadUserList();
  },
  methods: {
    handleSizeChange(val) {
      // 每页显示的条数
      this.queryInfo.pageSize = val;
      this.loadUserList();
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      // 显示第几页
      this.queryInfo.pageNum = val;
      this.loadUserList();
      console.log(`当前页: ${val}`);
    },
    // 获取选中的值
    handleSelectionChange(selected) {
      this.selected = selected;
      // console.log('选中的值', selected.map((item) => item.id))
    },
    // 查询角色列表
    loadRoleList() {
      const queryInfo = {
        name: "",
        description: "",
        flag: "",
      }
      getRoleList(queryInfo).then((res) => {
        this.roleList = res.data.roleList;
      });
    },
    // 查询用户列表
    loadUserList() {
      this.loading = true;
      getUserList(this.queryInfo).then((res) => {
        this.userList = res.data.userList;
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 清空查询条件查询所有用户
    reset() {
      this.queryInfo.username = "";
      this.queryInfo.nickname = "";
      this.queryInfo.email = "";
      this.queryInfo.pageNum = 1;
      this.queryInfo.pageSize = 10;
      this.loadUserList();
    },
    formType(cellValue){
      for(let i = 0; i< this.roleList.length; i++){
        if (cellValue.role === this.roleList[i].flag) {
          return this.roleList[i].name
        }
      }
    },
    addUser() {
      this.userForm = {};
      this.dialogFormVisible = true;
    },
    editUser(row) {
      this.userForm = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true;
    },
    saveOrUpdateUser() {
      if (this.userForm.id) {
        updateUser(this.userForm)
            .then((res) => {
              this.$message.success(res.message);
              this.dialogFormVisible = false;
              this.loadUserList();
            })
      } else {
        saveUser(this.userForm)
            .then((res) => {
              this.$message.success(res.message);
              this.dialogFormVisible = false;
              this.loadUserList();
            })
      }
    },
    // 根据id删除用户
    deleteUser(id) {
      this.$confirm("此操作将永久删除该用户,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deleteUserById(id).then((res) => {
            this.$message.success(res.message);
            this.loadUserList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除操作",
          });
        });
    },
    // 根据id批量删除用户
    deleteUserBatch() {
      const ids = this.selected.map((item) => item.id).join();
      /* 根据后台想要的参数格式选择
      console.log(ids.join(",")); // string:1,2,3,4
      console.log(ids); // object:[1,2,3,4]
      */
      this.$confirm("此操作将永久删除所选的用户,是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      })
        .then(() => {
          deleteUserBatchByIds(ids).then((res) => {
            this.$message.success(res.message + ",ID为: " + ids);
            this.loadUserList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消批量删除操作",
          });
        });
    },
    // 超出文件个数的回调
    handleExceed(files) {
      this.$message.warning(`超出上传数量限制！最多上传 1 个表格文件，选择了 ${files.length} 个文件`)
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    // 上传头像
    uploadAvatar(item) {
      this.$message("头像上传中······");
      // 上传文件的需要formdata类型
      const FormDatas = new FormData();
      FormDatas.append("file", item.file);
      uploadAvatar(FormDatas).then((res) => {
        this.$message.success(res.message);
        this.userForm.avatar = res.data;
        // 成功过后刷新列表，清空上传文件列表
        this.handleSuccess();
      });
    },
    // 上传成功后的回调
    handleSuccess() {
      this.loadUserList()
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
