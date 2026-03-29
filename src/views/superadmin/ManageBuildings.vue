<template>
  <div class="container">
    <!-- 导航栏 -->
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

    <!-- 搜索区域 -->
    <div class="search-container">
      <el-input
        v-model="searchKey"
        placeholder="请输入教室名"
        @keyup.enter.native="searchBuildings"
        style="width: 200px; margin-left: 10px;"
      />
      <el-date-picker
        v-model="selectedDate"
        type="date"
        placeholder="选择日期"
        value-format="yyyy-MM-dd"
        :default-value="new Date()"
        @change="handleDateChange"
        style="width: auto; margin-left: 20px;"
      />
      <el-select
        v-model="selectedSection"
        placeholder="选择节次"
        :disabled="disableSectionSelect"
        style="width: auto; margin-left: 20px;"
        @change="searchBuildings"
      >
        <el-option label="全部" value=""></el-option>
        <el-option label="1-2" value="1-2"></el-option>
        <el-option label="3-4" value="3-4"></el-option>
        <el-option label="5-6" value="5-6"></el-option>
        <el-option label="7-8" value="7-8"></el-option>
        <el-option label="9-10" value="9-10"></el-option>
        <el-option label="11-12" value="11-12"></el-option>
      </el-select>
      <el-button type="primary" @click="searchBuildings" style="margin-left: 30px;">搜索</el-button>
      <el-button type="danger" @click="batchDeleteBuildings" style="margin-left: 20px;">批量删除</el-button>
    </div>

    <!-- 表格区域 -->
    <div class="table-container">
      <el-table
        :data="buildings"
        style="width: 100%"
        class="data-table"
        @selection-change="handleSelectionChange"
        ref="buildingTable"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="build_name" label="教室姓名" :width="columnWidth(30)"></el-table-column>
        <el-table-column prop="build_capacity" label="容量" :width="columnWidth(15)"></el-table-column>
        <el-table-column prop="build_state" label="状态" :width="columnWidth(25)">
          <template slot-scope="scope">
            <span>{{ getStateLabel(scope.row.build_state) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="build_section" label="节次" :width="columnWidth(15)"></el-table-column>
        <el-table-column label="操作" :width="columnWidth(30)">
          <template slot-scope="scope">
            <div class="action-buttons">
              <el-button class="edit-button" @click="showEditBuildingDialog(scope.row)">修改状态</el-button>
              <el-button class="delete-button" @click="deleteBuilding(scope.row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        background
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
import request from "@/utils/request.js";
import { jwtDecode } from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import dayjs from 'dayjs';
import isSameOrBefore from 'dayjs/plugin/isSameOrBefore';

dayjs.extend(isSameOrBefore);

const NAV_TABS = [
  { label: '首页', name: 'superadmin' },
  { label: '预约', name: 'booking' },
  { label: '预约记录', name: 'records' },
  { label: '审核教室', name: 'audit' },
  { label: '设置校历', name: 'calendar' },
  { label: '导入课表', name: 'import' },
  { label: '用户管理', name: 'users' },
  { label: '教室管理', name: 'classrooms' }
];

export default {
  data() {
    return {
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      searchKey: '',
      buildings: [],
      activeTab: 'classrooms',
      tabs: NAV_TABS,
      selectedDate: '',
      selectedSection: '',
      disableSectionSelect: true,
      multipleSelection: []
    };
  },
  created() {
    this.parseToken();
    this.loadBuildings();
  },
  methods: {
    columnWidth(percent) {
      const tableWidth = document.querySelector('.data-table')?.offsetWidth || 1200;
      return `${Math.floor(tableWidth * percent / 100)}px`;
    },
    handlePageChange(page) {
      this.currentPage = page;
      this.loadBuildings();
    },
    handleDateChange() {
      this.disableSectionSelect = false;
      this.selectedSection = '';
      this.searchBuildings();
    },
    searchBuildings() {
      this.currentPage = 1;
      this.loadBuildings();
    },
    async loadBuildings() {
      try {
        const response = await request.get('/buildings', {
          params: {
            page: this.currentPage,
            pageSize: this.pageSize,
            buildName: this.searchKey,
            buildTime: this.selectedDate,
            buildSection: this.selectedSection
          }
        });
        const { code, data } = response.data;
        if (code === 1 && data) {
          this.buildings = data.rows || [];
          this.total = data.total || 0;
        } else {
          this.buildings = [];
          this.total = 0;
          this.$message.error(response.data.msg || "加载教室失败");
        }
      } catch (error) {
        this.$message.error("请求异常：" + error.message);
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    async batchDeleteBuildings() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请先勾选要删除的教室');
        return;
      }
      try {
        const deleteRequests = this.multipleSelection.map(row => {
          return request.delete(`/buildings/delete/${row.build_name}`, {
            params: {
              buildTime: this.selectedDate || row.build_time,
              buildSection: this.selectedSection || row.build_section
            }
          });
        });
        await Promise.all(deleteRequests);
        this.$message.success('批量删除成功');
        this.loadBuildings();
      } catch (error) {
        this.$message.error('批量删除失败：' + error.message);
      }
    },
    showEditBuildingDialog(building) {
      this.editingBuilding = { ...building };
      this.editBuildingDialogVisible = true;
    },
    async updateBuilding() {
      this.editingBuilding.buildTime = this.selectedDate;
      this.editingBuilding.buildSection = this.selectedSection;
      const response = await request.post('/buildings/update', this.editingBuilding);
      if (response.data.code === 1) {
        this.$message.success('教室更新成功');
        this.editBuildingDialogVisible = false;
        this.loadBuildings();
      } else {
        this.$message.error('更新失败');
      }
    },
    async deleteBuilding(building) {
      try {
        const response = await request.delete(`/buildings/delete/${building.build_name}`, {
          params: {
            buildTime: this.selectedDate || building.build_time,
            buildSection: this.selectedSection || building.build_section
          }
        });
        if (response.data.code === 1) {
          this.$message.success('删除成功');
          this.loadBuildings();
        } else {
          this.$message.error('删除失败');
        }
      } catch (error) {
        this.$message.error("请求异常：" + error.message);
      }
    },
    handleTabChange(tab) {
      const targetRoute = `/${tab.name}`;
      if (this.$route.path !== targetRoute) {
        this.$router.push(targetRoute);
      }
    },
    confirmLogout() {
      this.$confirm('是否确定退出登录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.logout();
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消退出' });
      });
    },
    logout() {
      localStorage.removeItem('token');
      this.$router.push('/login');
    },
    parseToken() {
      const token = getToken();
      if (!token) {
        this.$message.error('请先登录');
        this.$router.push('/superadmin');
        return;
      }
      try {
        const decoded = jwtDecode(token);
        this.bk_pson_id = decoded.id;
        this.bk_pson_name = decoded.name;
      } catch (error) {
        this.$message.error('登录异常');
        this.$router.push('/superadmin');
      }
    },
    getStateLabel(state) {
      switch (state) {
        case 0: return '空闲';
        case 1: return '未审核';
        case 2: return '已审核';
        case 3: return '不可用';
        default: return '未知';
      }
    }
  }
};
</script>


<style scoped>
.container {
  width: 100vw;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
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
  margin-left: auto;
}

.operation-bar {
  width: 100%;
  display: flex;
  align-items: center;
  padding: 20px 20px 0 20px;
  box-sizing: border-box;
}

.search-container {
  width: 100%;
  padding: 20px;
  display: flex;
  align-items: center;
  background-color: #fafafa;
  box-sizing: border-box;
}

.table-container {
  width: 100%;
  padding: 0 20px;
  flex: 1;
  margin-bottom: 24px;
  box-sizing: border-box;
}

.data-table {
  width: 100%;
  height: 100%;
}

.pagination {
  width: 100%;
  text-align: center;
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  align-items: center;
}

.action-buttons .el-button {
  margin-right: 16px;
}

.edit-button {
  background-color: #409EFF;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
}

.delete-button {
  background-color: #F56C6C;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
}
</style>
