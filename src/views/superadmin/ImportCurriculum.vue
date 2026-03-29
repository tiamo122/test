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
  
      <!-- 上传区域 -->
      <div class="drag-upload-module" ref="uploadArea" @dragover.prevent @drop="handleDrop" @click="openFileSelector">
        <!-- 如果scheduleFile存在，显示文件名，否则显示提示文字 -->
        <span v-if="scheduleFile">{{ scheduleFile.name }}</span>
        <span v-else>点击或拖拽文件到这里上传</span>
        <input type="file" ref="fileInput" @change="handleFileChange" accept=".xlsx,.xls,.csv" hidden />
      </div>
  
      <div class="button-container">
        <button class="import-button" @click="importTimetable">导入课表</button>
      </div>

    <!-- 查看课表弹窗 -->
    <div v-if="showScheduleDialog" class="dialog-overlay">

      <div class="dialog-content">
        <el-date-picker
          v-model="selectedDate"
          type="date"
          placeholder="选择日期"
          @change="fetchScheduleForDate">
        </el-date-picker>
        <!--日期选择器-->
        <button class="close-button" @click="showScheduleDialog = false">关闭</button>

        <h2>课表信息</h2>
        <div v-if="scheduleData.length > 0">
          <table>
            <thead>
              <tr>
                <th>教室</th>
                <th>节次</th>
                <th>课程</th>
                <th>教师</th>
                <th>班级</th>
                <!-- 其他表头 -->
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in scheduleData" :key="index">
                <td>{{ item.location }}</td>
                <td>{{ item.section }}</td>
                <td>{{ item.courseName }}</td>
                <td>{{ item.teacher }}</td>
                <td>{{ item.className }}</td>
                <!-- 其他数据列 -->
              </tr>
            </tbody>
          </table>
        </div>
        <div v-else>
          <p>无数据</p>
        </div>
      </div>
    </div>

    <!--修改课表弹窗-->
    <div v-if="editScheduleDialog" class="dialog-overlay">

      <div class="dialog-content">
        <el-date-picker
          v-model="selectedDate"
          type="date"
          placeholder="选择日期"
          @change="fetchScheduleForDate">
        </el-date-picker>
        <!--日期选择器-->
        <button class="close-button" @click="editScheduleDialog = false">关闭</button>

        <h2>课表信息</h2>
        <div v-if="scheduleData.length > 0">
          <table>
            <thead>
              <tr>
                <th>教室</th>
                <th>节次</th>
                <th>课程</th>
                <th>教师</th>
                <th>班级</th>
                <th>操作</th>
                <!-- 其他表头 -->
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in scheduleData" :key="index">
                <td><input v-if="item.editing" v-model="item.location" /></td>
                <td><input v-if="item.editing" v-model="item.section" /></td>
                <td><input v-if="item.editing" v-model="item.courseName" /></td>
                <td><input v-if="item.editing" v-model="item.teacher" /></td>
                <td><input v-if="item.editing" v-model="item.className" /></td>
                <td>
                  <button v-if="!item.editing" @click="editRow(index)">修改</button>
                  <button v-else @click="saveRow(index)">完成</button>
                </td>
                <!-- 其他数据列 -->
              </tr>
            </tbody>
          </table>
        </div>
        <div v-else>
          <p>无数据</p>
        </div>
      </div>
    </div>

  </div>
</template>
  
  <script>
import request from '@/utils/request';

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
        scheduleFile:null,
        showScheduleDialog: false,
        editScheduleDialog: false,
        scheduleData: [],
        selectedDate: new Date(), // 默认为当前日期
        activeTab:'import',
        tabs: NAV_TABS,
        activeName:'import',
        activePath:'',
        };
    },
    methods: {
      handleTabChange(tab) {//导航栏跳转逻辑
        const targetRoute = `/${tab.name}`
        if (this.$route.path !== targetRoute) {
        this.$router.push(targetRoute)
        }
      },
    
      handleDrop(event) {
        const droppedFiles = event.dataTransfer.files;
        if (droppedFiles.length > 0) {
        // 只取第一个文件
          this.scheduleFile = droppedFiles[0];
        }
      },
      openFileSelector() {
        // 触发隐藏的文件输入元素的点击事件
        this.$refs.fileInput.click();
      },
      handleFileChange(event) {
        // 获取选中的文件并存储到 scheduleFile 变量中
        this.scheduleFile = event.target.files[0];
      },
      importTimetable() {
        if (!this.scheduleFile) {
          alert('请先上传文件！');
          return;
        }

        const formData = new FormData();
        formData.append('file', this.scheduleFile);

        request.post('/importcurriculum', formData).then(res => {
                console.log("导入数据",res)
                if (res.status ===200) {
                    this.$message.success('导入成功')
                    } else {
                        this.$message.error('导入失败,请检查文件格式是否正确')
                        }
                    })
      },

      saveRow(index) {
        const updatedItem = this.scheduleData[index];
        // 发送更新后的数据到后端
         this.updateScheduleData(updatedItem).then(() => {
        // 后端更新成功后，刷新数据
          this.fetchScheduleForDate(this.selectedDate);
        });
        this.scheduleData[index].editing = false;
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
  .container {
    position: relative;
    min-height: 100vh;
    padding-bottom: 80px; 
  }
  
  .drag-upload-module {
  width: 500px; /* 可根据实际需要调整宽度 */
  height: 360px; /* 可根据实际需要调整高度 */
  border: 2px dashed #CCCCCC; /* 虚线边框 */
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  position: absolute;
  top: 50%; /* 垂直居中 */
  left: 50%; /* 水平居中 */
  transform: translate(-50%, -65%); /* 校正位置 */
}
  
.button-container {
  text-align: center;
  position: absolute;
  top: 50%; /* 以拖拽上传模块为参考点 */
  left: 50%; /* 以拖拽上传模块为参考点 */
  transform: translate(-50%, calc(-50% + 250px)); /* 向下偏移一定距离 */
  width: 500px; /* 与拖拽上传模块宽度一致 */
  display: flex;
  justify-content: center;
  flex-direction: row;
  align-items: center;
}

.button-container button {
  padding: 10px 20px; /* 按钮内边距 */
  margin: 25px; /* 按钮之间的间距 */
  cursor: pointer;
  border-radius: 5px; /* 圆角 */
}

/* 导入课表按钮样式 */
.import-button {
  background-color: rgb(0, 128, 255); /* 蓝色背景 */
  border: none; /* 无边框 */
  color: black; /* 黑色字体 */
}

/* 查看课表按钮样式 */
.view-button {
  background-color: white; /* 白色背景 */
  border: 2px solid rgb(64, 64, 64); /* 灰色边框 */
  color: black; /* 黑色字体 */
}

/* 修改课表按钮样式 */
.edit-button {
  background-color: red; /* 红色背景 */
  border: none; /* 无边框 */
  color: white; /* 白色字体 */
}

/*
.close-button{
  background-color: white; 
  border: 1px solid rgb(64, 64, 64); 
  color: black; 
}


.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.dialog-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
}
*/

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* 确保弹窗在最上层 */
}

.dialog-content {
  background-color: white;
  padding: 20px;
  width: 50%; /* 弹窗宽度为页面的一半 */
  max-width: 600px; /* 可选，设置最大宽度 */
  position: relative;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
  border-radius: 4px; /* 可选，添加圆角 */
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 5px 10px;
  font-size: 14px;
  cursor: pointer;
  background: none;
  border: none;
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
  