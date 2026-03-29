<template>
  <div class="container">
    <div class="navbar">
      <div class="left-section">
        <img src="@/assets/xiaohui.png" alt="华中科技大学">
        <el-tabs v-model="activeTab" @tab-click="handleTabChange">
          <el-tab-pane 
            v-for="tab in tabs" 
            :key="tab.name"
            :label="tab.label" 
            :name="tab.name"
          ></el-tab-pane>
        </el-tabs>
      </div>
      <button class="logout-button" @click="confirmLogout">退出</button>
    </div>

    <div class="filter-search-container">
      <el-select v-model="selectedRole" placeholder="请选择用户类型" style="width: 200px;">
        <el-option label="管理员" value="0"></el-option>
        <el-option label="教师" value="1"></el-option>
        <el-option label="学生" value="2"></el-option>
      </el-select>
      <el-input v-model="searchKeyword" placeholder="根据用户关键词进行搜索" style="width: 300px; margin-left: 10px;"></el-input>
      <el-button type="primary" @click="filterData">搜索</el-button>
    </div>

    <div class="user-management-content">
      <el-table
        :data="filteredUserData"
        border
        style="width: 100%; margin-top: 20px;"
      >
        <el-table-column prop="id" label="用户账号" width="300"></el-table-column>
        <el-table-column prop="name" label="用户姓名" width="300"></el-table-column>
        <el-table-column v-if="this.class===true" prop="classname" label="班级" width="300"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="300"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <div style="display: flex; justify-content: center; gap: 10px;">
            <el-button type="primary" @click="handleEdit(scope.row)">修改信息</el-button>
            <el-button type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
      background
      class="pagination"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
      @current-change="handlePageChange"
    />
    </div>

    <el-dialog title="修改用户信息" :visible.sync="dialogVisible" width="500px">
      <el-form :model="formData" :rules="rules" ref="editForm">
        <el-form-item label="用户账号" prop="id" label-width="100px">
          <el-input v-model="formData.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户姓名" prop="name" label-width="100px">
          <el-input v-model="formData.name"></el-input>
        </el-form-item>
        <el-form-item 
          v-if="formData.role === '2'" 
          label="班级" 
          prop="classname"
          label-width="100px">
          <el-input v-model="formData.classname"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email" label-width="100px">
          <el-input v-model="formData.email"></el-input>
        </el-form-item>
        <el-form-item label="用户类型" prop="role" label-width="100px">
          <el-select v-model="formData.role" @change="handleRoleChange">
            <el-option label="管理员" value="0"></el-option>
            <el-option label="教师" value="1"></el-option>
            <el-option label="学生" value="2"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">确定</el-button>
      </div>
    </el-dialog>  
  </div>
</template>

<script>
import request from '@/utils/request'

const NAV_TABS = [
  { label: '首页', name: 'superadmin' },
  { label: '预约', name: 'booking' },
  { label: '预约记录', name: 'records' },
  { label: '审核教室', name: 'audit' },
  { label: '设置校历', name: 'calendar' },
  { label: '导入课表', name: 'import' },
  { label: '用户管理', name: 'users' },
  { label: '教室管理', name: 'classrooms' }
]

export default {
  data() {
    return {
      activeTab: 'users',
      tabs: NAV_TABS,
      userData: [],
      filteredUserData: [],
      selectedRole: '',
      searchKeyword: '',
      class: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      loading: false,
      formData: {
        id: '',
        name: '',
        classname: '',
        email: '',
        role: ''
      },
      rules: {
        name: [{ required: true, message: '请输入用户姓名', trigger: 'blur' }],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        role: [{ required: true, message: '请选择用户类型', trigger: 'change' }],
        classname: [{ required: true, message: '请输入班级', trigger: 'blur' }]
      }
    }
  },
  methods: {
    handleTabChange(tab) {
      const targetRoute = `/${tab.name}`
      if (this.$route.path !== targetRoute) {
        this.$router.push(targetRoute)
      }
    },
    handleEdit(row) {
      this.formData = {
        id: row.id,
        name: row.name,
        classname: row.classname || '',
        email: row.email,
        role: String(row.role)
      }
      this.dialogVisible = true
    },
    handleDelete(row) {
      const params = { id: row.id }
      request.post('/usermanagement/deleteusers', params).then(res => { 
        if (res.status === 200) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error('删除失败')
        }
      })
    },
    handleRoleChange(value) {
      if (value !== '2') this.formData.classname = ''
    },

    submitEdit() {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          const params = {
            ...this.formData,
            role: Number(this.formData.role),
            classname: this.formData.role === '2' ? this.formData.classname : ''
          }
          request.post('/usermanagement/editusers', params).then(res => {
            if (res.status === 200) {
              this.$message.success('修改成功')
              console.log(res)
              this.dialogVisible = false
              this.loadData()
            }else{
              this.message.error('修改失败')
            }
          })
        }
      })
    },
    async filterData() {
    this.loading = true
    const params = {
        pageNow: this.currentPage,
        pageSize: this.pageSize,
      }
    const requestBody = {
        id: this.searchKeyword,
        name: this.searchKeyword,
        role: this.selectedRole,
        email: this.searchKeyword,
        classname: this.searchKeyword
      }
    try {
        const { data } = await request.post(`/usermanagement/findusers?pageNow=${params.pageNow}&pageSize=${params.pageSize}`
        , requestBody)
        this.filteredUserData = data.list
        this.total = data.total
        this.class = this.selectedRole === '2'
    } catch (error) {
        this.$message.error('数据加载失败')
        console.error('数据加载失败:', error)
    } finally {
        this.loading = false
    }
    },
    confirmLogout() {
      this.$confirm('是否确定退出登录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('token')
        this.$router.replace('/login')
        window.history.replaceState(null, document.title, '/login')
      })
    },
    handlePageChange(newPage) {
      this.currentPage = newPage
      this.loadData()
    },
    loadData() {
      this.filterData()
    }
  },
  mounted() {
    this.loadData()
  }
}
</script> 
<style scoped>
.container {
  position: relative;
  min-height: 100vh;
  padding-bottom: 80px; 
}
.pagination {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
  padding: 12px 20px;
  border-radius: 4px;
}
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  border-bottom: 1px solid #ddd;
  width: 100%;
}
.left-section {
  display: flex;
  align-items: center;
}
.left-section img {
  height: 40px;
  margin-right: 10px;
}
.logout-button {
  background-color: #dc143c;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  border-radius: 5px;
}
.user-management-content {
  margin-top: 20px;
}
.el-table .cell {
  text-align: center;
}
.el-button--mini {
  margin-right: 10px;
}
.filter-search-container {
  margin: 20px 0;
  padding: 0 20px;
}
</style>