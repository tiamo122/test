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

    <div class="search-container">
      <el-date-picker
        v-model="searchParams.bkTime"
        type="date"
        placeholder="选择日期"
        value-format="yyyy-MM-dd"
        :default-value="new Date()"
        @change="handleSearch"
      />
    </div>
    <el-table
  :data="tableData"
  border
  class="data-table"
  style="width: 100%"> <!-- 确保表格宽度100% -->
  <!-- 百分比列宽示例 -->
  <el-table-column prop="bk_time" label="日期" :width="columnWidth(20)"  />
  <el-table-column prop="bk_bd_name" label="地点" :width="columnWidth(20)" />
  <el-table-column prop="bk_bd_section" label="节次" :width="columnWidth(20)" />
  <el-table-column prop="lesName" label="课程名称" :width="columnWidth(25)" />
  <el-table-column prop="bk_pson_name" label="借用人" :width="columnWidth(15)"  />
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
</template>

<script>
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
  data() {
    return {
      loading: false,
      currentPage: 1,
      pageSize: 13,
      total: 0,
      tableData: [],
      searchParams: {
        bkTime: dayjs().format('YYYY-MM-DD'),
        bkBdState:'已审核'
      },
      activeTab: 'superadmin',
      tabs: NAV_TABS
    }
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
            ...this.searchParams
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
    columnWidth(percent) {
      const tableWidth = document.querySelector('.data-table')?.offsetWidth || 1200
      return `${Math.floor(tableWidth * percent / 100)}px`
    },

    handlePageChange(newPage) {
      this.currentPage = newPage
      this.loadData()
    },

    handleSearch() {
      this.currentPage = 1
      this.loadData()
    },

    handleTabChange(tab) {
      const targetRoute = `/${tab.name}`
      if (this.$route.path !== targetRoute) {
        this.$router.push(targetRoute)
      }
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

    // 修改后的退出方法
logout() {
  localStorage.removeItem('token')
  
  // 强制清除浏览器历史记录
  if (window.history && window.history.pushState) {
    window.history.replaceState(null, '', '/login')
    window.history.pushState(null, '', '/login')
    window.addEventListener('popstate', () => {
      this.$router.replace('/login')
    })
  }
  
  // 使用replace跳转
  this.$router.replace('/login').catch(() => {})
},
  }
}
</script>

<style scoped>
.container {
  position: relative;
  min-height: 100vh;
  padding-bottom: 80px; 
}

.search-container {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-start;
}

.data-table {
  margin-bottom: 24px;
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

.el-table__body-wrapper {
  min-height: 300px;
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