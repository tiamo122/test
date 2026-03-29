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
        v-model="buildTime"
        type="date"
        placeholder="选择日期"
        value-format="yyyy-MM-dd"
        :default-value="new Date()"
        @change="handleSearch"
        style="width: auto;"/>
        <div class="user-info">
            <span>账号：{{ bk_pson_id }}</span>
            <span style="margin-left: 20px;">姓名：{{ bk_pson_name }}</span>
        </div>
    </div>
    <el-table  :data="tableData" border class="data-table" style="width: 100%"> 
    <el-table-column  prop="build_time" label="日期" :width="columnWidth(20)"></el-table-column>
    <el-table-column prop="build_name" label="地点" :width="columnWidth(20)"></el-table-column>
    <el-table-column prop="build_section" label="节次" :width="columnWidth(10)"></el-table-column>
    <el-table-column prop="build_capacity" label="容量" :width="columnWidth(10)"></el-table-column>
    <el-table-column  label="用途" :width="columnWidth(30)" align="center">
        <template slot-scope="scope">
            <el-input v-model="scope.row.bk_reason" placeholder="请输入用途">
            </el-input>
        </template>
    </el-table-column>
    <el-table-column  label="操作" :width="columnWidth(10)" align="center" >
        <template slot-scope="scope">
            <el-button type="primary" size="small" @click="handleSubmit(scope.row)">提交</el-button>
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
    @current-change="handlePageChange">
</el-pagination>
    </div>
    </template>

<script>
import request from "@/utils/request.js";

import {jwtDecode} from "jwt-decode";
import {getToken
} from "@/utils/storage.js";
import dayjs from 'dayjs'
// 导航配置
const NAV_TABS = [
{ label: '预约', name: 'usersbooking' },
{ label: '预约记录', name: 'usersrecords' },
]

export default {
    name:"usersbooking",
    data() {
    return {
    bk_reason: '',
    bk_pson_id: '',
    bk_pson_name:'',
    loading: false,
    currentPage: 1,
    pageSize: 13,
    total:0,
    tableData: [],
    buildTime: dayjs().format('YYYY-MM-DD'),
    activeTab: 'usersbooking',
    tabs: NAV_TABS,
        activeName:'usersbooking',
        activePath:'',
        };
    },
    methods: {
    async loadData() {
    this.loading = true
    try {
        const { data } = await request.get('/building', {
        params: {
            pageNow: this.currentPage,
            pageSize: this.pageSize,
            buildTime:this.buildTime
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
    handleSubmit(row) {
        const params = {
            bk_time: row.build_time,
            bk_reason: row.bk_reason,
            bk_bd_name: row.build_name,
            bk_pson_name: this.bk_pson_name,
            bk_pson_id: this.bk_pson_id,
            bk_bd_section: row.build_section,
            bk_apply_time:dayjs().format('YYYY-MM-DD HH:mm:ss'),
                    }
            request.post('/superadmin/booking', params).then(res => {
                console.log("预约数据",res)
                if (res.status ===200) {
                    this.$message.success('预约成功')
                    this.loadData()
                    } else {
                        this.$message.error('预约失败')
                        this.loadData()
                        }
                    })
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

    tokenDecode(token) {
        if (token !== null) return jwtDecode(token);
    },
        //导航栏点击事
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

    logout() {
  localStorage.removeItem('token');
  this.$router.replace('/login'); 
  window.history.replaceState(null, document.title, '/login');
  window.history.pushState(null, document.title, '/login');
},
    parseToken() {
        const token = getToken()
        if (!token) {
            this.$message.error('请先登录')
            this.$router.push('/login')
            return
        }
        
        try {
            const decoded = jwtDecode(token)
            
            this.bk_pson_id = decoded.id
            this.bk_pson_name = decoded.name
        } catch (error) {
            
            console.error('Token解析失败:', error)
            this.$message.error('登录信息异常')
            this.$router.push('/login')
        }
    },
    },
    created() {
        this.parseToken()
        this.loadData()
}
    };
</script>
<style scoped>
.block { padding: 20px; }
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
.user-info {
    margin-left: 20px;
    padding: 8px 15px;
    
    border-radius: 4px;
    color: #606266;
}
</style>