<template>
    <div>
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
  
      <!-- 数据表格 -->
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="bk_pson_id" label="申请人学号"></el-table-column>
        <el-table-column prop="bk_pson_name" label="申请人姓名"></el-table-column>
        <el-table-column prop="bk_reason" label="用途"></el-table-column>
        <el-table-column prop="bk_time" label="时间"></el-table-column>
        <el-table-column prop="bk_bd_section" label="节次"></el-table-column>
        <el-table-column prop="bk_bd_name" label="教室"></el-table-column>
        <el-table-column prop="bk_bd_state" label="预约当天教室情况"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="primary" @click="approve(scope.row)">通过</el-button>
            <el-button type="danger" @click="reject(scope.row)">不通过</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 分页 -->
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
  </template>
  
<script>
import axios from 'axios';
import request from '@/utils/request'
import dayjs from 'dayjs'

// 导航配置
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
    name:"AuditBookig",
    data() {
      return {
        tableData: [],
        currentPage: 1,
        pageSize: 10,
        total: 0,
        activeTab:'audit',
        tabs: NAV_TABS,
        activeName:'audit',
        activePath:'',
        bkBdState:'',
        bk_refused_reason:''

      };
    },
    created() {
      this.loadData()
    },
    methods: {
      async loadData() {
      this.loading = true
      try {
        const { data } = await request.get('/booking', {
          params: {
            pageNow: this.currentPage,
            pageSize: this.pageSize,
            bkTime:null,
            bkBdState:'未审核'
          }
        })
        this.tableData = data.list
        this.total = data.total
      } catch (error) {
        this.$message.error('数据加载失败')
        console.error('数据加载失败:', error)
      } finally {
        this.loading = false
      }
    },
      handleSizeChange(newSize) {
        this.pageSize = newSize;
        this.fetchBookingData();
      },
      handleCurrentChange(newPage) {
        this.currentPage = newPage;
        this.fetchBookingData();
      },
      approve(row) {
        const params = {
            bk_time: row.bk_time,
            bk_reason: row.bk_reason,
            bk_bd_name: row.bk_bd_name,
            bk_pson_name: row.bk_pson_name,
            bk_pson_id: row.bk_pson_id,
            bk_bd_section: row.bk_bd_section,
            bk_bd_state:'已审核'
                    }
            request.post('/superadmin/bookingconfirm', params).then(res => {
                console.log("审核数据",res)
                if (res.status ===200) {
                    this.$message.success('审核成功')
                    // 刷新表格数据
                    this.loadData()
                    } else {
                        this.$message.error('审核失败')
                        this.loadData()

                        }
                    })

        // 实现批准操作的逻辑

      },
      reject(row) {
        // 实现拒绝操作的逻辑
        this.$prompt('请输入驳回原因', '驳回原因', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({ value }) => {
          this.$message({
            type: 'success',
            message: '驳回原因是: ' + value
          });
          this.bk_refused_reason=value;
          const params = {
            bk_time: row.bk_time,
            bk_reason: row.bk_reason,
            bk_bd_name: row.bk_bd_name,
            bk_pson_name: row.bk_pson_name,
            bk_pson_id: row.bk_pson_id,
            bk_bd_section: row.bk_bd_section,
            bk_bd_state:'已驳回',
            bk_refused_reason: this.bk_refused_reason
                    }
        request.post('/superadmin/bookingconfirm', params).then(res => {
                console.log("驳回数据",res)
                if (res.status ===200) {
                    this.$message.success('驳回成功')
                    this.loadData()
                    } else {
                        this.$message.error('驳回失败')
                        this.loadData()
                        }
                    })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消输入'
          });       
        });

      },

      handleTabChange(tab) {//导航栏跳转逻辑
        const targetRoute = `/${tab.name}`
        if (this.$route.path !== targetRoute) {
        this.$router.push(targetRoute)
        }
      },

      handlePageChange(newPage) {//分页跳转逻辑
      this.currentPage = newPage
      this.loadData()
      },
      confirmLogout() {
      this.$confirm('是否确定退出登录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 点击确定后的操作
        this.logout();
      }).catch(() => {
        // 点击取消后的操作
        this.$message({
          type: 'info',
          message: '已取消退出'
        });
      });
    },

    logout() {
  localStorage.removeItem('token');
  this.$router.replace('/login'); 
  window.history.replaceState(null, document.title, '/login');
  window.history.pushState(null, document.title, '/login');
},

    }
  };
</script>
  
<style scoped>
/* 样式定义 */
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
    height: 40px; /* 根据实际图片大小调整 */
    margin-right: 10px; /* 图片和标签页之间的间距 */
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
</style>  