<template>
    <div>
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
      
    
        <!-- 状态选项弹出框 -->
        <div v-if="showStatusOptions" class="status-options">
            <button @click="toggleDateRed" :class="{ 'active': customRedDates[selectedCell] }">
                {{ customRedDates[selectedCell] ? '取消标红' : '标记为红色' }}
            </button>
            <button @click="toggleHighlight" :class="{ 'active': customHighlights[selectedCell] }">
                {{ customHighlights[selectedCell] ? '取消高亮' : '高亮显示' }}
            </button>
            <div class="annotation-input">
            <input 
                placeholder="输入注释(5字内)" 
                maxltype="text" 
                ength="5"
                @keyup.enter="addAnnotation($event.target.value)"
                ref="annotationInput"
            >
            <button @click="addAnnotation($refs.annotationInput.value)">添加</button>
      </div>
    </div>
  
      <!-- 日历容器 -->
      <div class="calendar-wrapper">
        <div class="calendar-container">
          <h2 class="calendar-title">学期视图日历</h2>
          <div class="year-month-selector">
            <label for="year-select">选择年份</label>
            <select id="year-select" v-model="selectedYear">
                <option v-for="year in years" :key="year" :value="year">{{ year }}年</option>
            </select>
            
            <label for="month-select">选择月份</label>
            <select id="month-select" v-model="selectedMonth">
                <option v-for="month in months" :key="month" :value="month">{{ month }}月</option>
            </select>
            </div>
            <button class="status-button" @click="toggleStatusOptions">设置状态</button>
          <table class="calendar-table" id="dynamic-calendar">
            <thead>
              <tr>
                <th>周次/星期</th>
                <th>一</th>
                <th>二</th>
                <th>三</th>
                <th>四</th>
                <th>五</th>
                <th>六</th>
                <th>日</th>
              </tr>
            </thead>
            <tbody>
                <tr v-for="(week, index) in calendar" :key="index">
                    <td v-for="(day, dayIndex) in week" 
                        :key="dayIndex" 
                        :class="getCellClasses(day)"
                        @click="setSelectedCell(day)"
                        @mouseenter="setHoveredCell(day)"
                        @mouseleave="setHoveredCell(null)">
                    <div class="cell-content">
                        {{ day.date }}
                        <span v-if="cellAnnotations[day.dateString]" class="annotation">
                        {{ cellAnnotations[day.dateString] }}
                        </span>
                    </div>
                    </td>
                </tr>
            </tbody>
          </table>
        </div>
      </div>
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
          //导航栏
          activeTab: 'calendar', // 设置默认激活的标签页
          tabs: NAV_TABS,

          selectedSemester: '2025-1',
          selectedYear: new Date().getFullYear(),
          selectedMonth: new Date().getMonth() + 1,
          calendar: [],
          
          // 扩展节假日，包含完整的假期日期范围
          holidays: {
            '元旦': ['2025-01-01'],
            '春节': ['2025-01-28', '2025-01-29', '2025-01-30', '2025-01-31', '2025-02-01', '2025-02-02', '2025-02-03'],
            '清明节': ['2025-04-04', '2025-04-05', '2025-04-06'],
            '劳动节': ['2025-05-01', '2025-05-02', '2025-05-03'],
            '端午节': ['2025-06-12', '2025-06-13', '2025-06-14'],
            '中秋节': ['2025-09-19', '2025-09-20', '2025-09-21'],
            '国庆节': ['2025-10-01', '2025-10-02', '2025-10-03', '2025-10-04', '2025-10-05', '2025-10-06', '2025-10-07']
          },
          years: Array.from({length: 10}, (_, i) => 2023 + i),
          months: Array.from({length: 12}, (_, i) => i + 1),
          
          showStatusOptions: false,
            selectedCell: null,
            cellAnnotations: {},
            customHighlights: {},
            customRedDates: {},
            clickedCells: new Set() // 用于存储被点击过的单元格
          
        };
      },
      methods: {
        handleTabChange(tab) {
          const targetRoute = `/${tab.name}`;
          if (this.$route.path !== targetRoute) {
          this.$router.push(targetRoute);
          }
        },

        generateCalendar() {
          const year = this.selectedYear;
          const month = this.selectedMonth;
          
          // 获取当月第一天和最后一天
          const firstDay = new Date(year, month - 1, 1);
          const lastDay = new Date(year, month, 0);
          const daysInMonth = lastDay.getDate();
          
          // 获取上个月和下个月的信息
          const prevMonthLastDay = new Date(year, month - 1, 0).getDate();
          const nextMonthFirstDay = new Date(year, month, 1);
          
          // 计算日历开始和结束需要补充的天数
          const startDayOfWeek = firstDay.getDay() || 7; // 0(周日)转换为7
          const endDayOfWeek = lastDay.getDay() || 7;
          const prevMonthDays = startDayOfWeek - 1;
          const nextMonthDays = 7 - endDayOfWeek;
          
          // 生成日历数据
          this.calendar = [];
          let date = 1;
          let nextDate = 1;
          
          // 获取所有节假日日期数组
          const allHolidays = Object.values(this.holidays).flat();
          
          for (let week = 0; week < 6; week++) { // 最多6周
            const weekData = [];
            
            // 添加周次
            weekData.push({
              date: week + 1,
              class: 'week-cell',
              isWeekNumber: true
            });
            
            // 处理每一天
            for (let day = 1; day <= 7; day++) {
              // 上个月的日期
              if (week === 0 && day < startDayOfWeek) {
                const prevDate = prevMonthLastDay - (prevMonthDays - day);
                weekData.push({
                  date: prevDate,
                  class: 'other-month',
                  isPrevMonth: true
                });
              } 
              // 下个月的日期
              else if (date > daysInMonth) {
                weekData.push({
                  date: nextDate++,
                  class: 'other-month',
                  isNextMonth: true
                });
              } 
              // 当月的日期
              else {
                const dateString = `${year}-${String(month).padStart(2, '0')}-${String(date).padStart(2, '0')}`;
                let classList = [];
                
                // 节假日判断
                if (allHolidays.includes(dateString)) {
                  classList.push('holiday');
                }
                
                // 每月第一天
                if (date === 1) {
                  classList.push('first-day');
                  if (month === 1) {
                    classList.push('first-year-day');
                  }
                }
                
                weekData.push({
                  date: date,
                  class: classList.join(' '),
                  rawDate: date,
                  isCurrentMonth: true,
                  dateString: dateString
                });
                
                date++;
              }
            }
            
            // 如果已经处理完所有日期且不需要补充下个月的日期，则提前结束
            if (date > daysInMonth && nextDate > nextMonthDays) {
              this.calendar.push(weekData);
              break;
            }
            
            this.calendar.push(weekData);
          }

        },
      
      setupDragToScroll() {
        const table = document.getElementById('dynamic-calendar');
        let isDown = false;
        let startX;
        let scrollLeft;
        
        table.addEventListener('mousedown', (e) => {
          isDown = true;
          startX = e.pageX - table.offsetLeft;
          scrollLeft = table.scrollLeft;
        });
        
        table.addEventListener('mouseleave', () => {
          isDown = false;
        });
        
        table.addEventListener('mouseup', () => {
          isDown = false;
        });
        
        table.addEventListener('mousemove', (e) => {
          if (!isDown) return;
          e.preventDefault();
          const x = e.pageX - table.offsetLeft;
          const walk = (x - startX) * 2; // Scroll speed
          table.scrollLeft = scrollLeft - walk;
        });
      },
      
      changeYear() {
        this.generateCalendar();
      },
      
      changeMonth() {
        this.generateCalendar();
      },

      toggleStatusOptions() {
        this.showStatusOptions = !this.showStatusOptions;
      },
      
      setSelectedCell(cell) {
        if (cell.isCurrentMonth) {
          this.selectedCell = cell.dateString;
        } else {
          this.selectedCell = null;
        }
      },
      
      markDateRed() {
        if (this.selectedCell) {
          this.$set(this.customRedDates, this.selectedCell, true);
          this.selectedCell = null;
          this.showStatusOptions = false;
        }
      },
      
      highlightDate() {
        if (this.selectedCell) {
          this.$set(this.customHighlights, this.selectedCell, true);
          this.selectedCell = null;
          this.showStatusOptions = false;
        }
      },
      
      addAnnotation(annotation) {
        if (this.selectedCell && annotation && annotation.length <= 5) {
          this.$set(this.cellAnnotations, this.selectedCell, annotation);
          this.selectedCell = null;
          this.showStatusOptions = false;
        }
      },
      handleCellClick(cell) {
        if (cell.isCurrentMonth) {
          this.selectedCell = cell.dateString;
          
          // 切换点击状态
          if (this.clickedCells.has(cell.dateString)) {
            this.clickedCells.delete(cell.dateString);
          } else {
            this.clickedCells.add(cell.dateString);
          }
        }
    },
    setHoveredCell(cell) {
        this.hoveredCell = cell?.dateString || null;
      },
      
      toggleDateRed() {
        if (this.selectedCell) {
          if (this.customRedDates[this.selectedCell]) {
            this.$delete(this.customRedDates, this.selectedCell);
          } else {
            this.$set(this.customRedDates, this.selectedCell, true);
          }
          this.showStatusOptions = false;
        }
      },
      
      toggleHighlight() {
        if (this.selectedCell) {
          if (this.customHighlights[this.selectedCell]) {
            this.$delete(this.customHighlights, this.selectedCell);
          } else {
            this.$set(this.customHighlights, this.selectedCell, true);
          }
          this.showStatusOptions = false;
        }
      },
      
    getCellClasses(cell) {
        const classes = [cell.class];
        
        if (this.customRedDates[cell.dateString]) {
          classes.push('custom-red');
        }
        
        if (this.customHighlights[cell.dateString]) {
          classes.push('custom-highlight');
        }
        
        if (this.clickedCells.has(cell.dateString)) {
          classes.push('clicked-cell');
        }
        
        return classes.join(' ');
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
      

    
    },
    watch: {
      selectedYear() {
        this.generateCalendar();
      },
      selectedMonth() {
        this.generateCalendar();
      }
    },

    
    
};
  </script>
  
  <style scoped>
      /* 导航栏整体样式 */
  .navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: white;
    
    border-bottom: 1px solid #ddd;
    width: 100%;
  }
  
  /* 左侧区域样式 */
  .left-section {
    display: flex;
    align-items: center;
  }
  
  /* Logo 样式 */
  .left-section img {
    height: 40px; /* 根据实际图片大小调整 */
    margin-right: 10px;
  }
  
  /* 导航链接列表样式 */
  .nav-links {
    list-style: none;
    display: flex;
    gap: 20px; /* 导航链接之间的间距 */
    margin: 0;
    padding: 0;
  }
  
  /* 导航链接样式 */
  .nav-links li a {
    text-decoration: none;
    color: #333;
    font-size: 16px;
    font-weight: bold;
  }
  
  /* 导航链接悬停样式 */
  .nav-links li a:hover {
    color: #1e90ff;
  }
  
  /* 退出按钮样式 */
  .logout-button {
    background-color: #dc143c;
    color: white;
    border: none;
    padding: 10px 20px;
    font-size: 16px;
    cursor: pointer;
    border-radius: 5px;
  }
  
  /* 日历包装容器 */
  .calendar-wrapper {
    width: 100%;
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }
  
  /* 日历容器调整为70%宽度 */
  .calendar-container {
    width: 70%;
    position: relative;
  }
  
  /* 表格样式调整 */
  .calendar-table {
    width: 100%;
    table-layout: fixed;
    border-collapse: collapse;
  }
  
  .calendar-table th,
  .calendar-table td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: center;
    height: 60px;
    box-sizing: border-box;
  }
  
  /* 周次列宽度 */
  .calendar-table th:first-child,
  .calendar-table td:first-child {
    width: 10%;
  }
  
  /* 日期列等宽 */
  .calendar-table th:not(:first-child),
  .calendar-table td:not(:first-child) {
    width: calc(90% / 7);
  }
  
  .week-cell {
    font-weight: bold;
    background-color: #f5f5f5;
  }
  
  /* 其他原有样式保持不变... */
  .calendar-title {
    font-size: 24px;
    color: #1e90ff;
    margin-bottom: 20px;
  }
  
  .semester-selector {
    margin-bottom: 10px;
  }
  
  .status-button {
    position: absolute;
    right: 0;
    top: 40px;
    background-color: #dc143c;
    color: white;
    border: none;
    padding: 10px 20px;
    font-size: 16px;
    cursor: pointer;
    border-radius: 10px;
  }
  
  .highlight {
    background-color: #ffa500;
  }
  
  .holiday {
    background-color: #f08080;
  }
  
  .important {
    font-weight: bold;
  }
  .year-month-selector {
  margin-bottom: 10px;
}

.year-month-selector select {
  margin-right: 15px;
  padding: 5px;
  border-radius: 4px;
  border: 1px solid #ddd;
}
/* 其他月份日期样式 */
.other-month {
  color: #ccc !important;
  background-color: #f9f9f9;
}

/* 每月第一天样式 */
.first-day {
  color: red !important;
  font-weight: bold;
}

/* 每年第一天样式 */
.first-year-day {
  font-size: 1.1em;
  text-decoration: underline;
}
/* 状态选项弹出框样式 */
.status-options {
  position: absolute;
  right: 0;
  top: 80px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  z-index: 100;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.status-options button {
  padding: 5px 10px;
  background: #f0f0f0;
  border: 1px solid #ccc;
  border-radius: 3px;
  cursor: pointer;
}

.status-options button:hover {
  background: #e0e0e0;
}

.annotation-input {
  display: flex;
  gap: 5px;
}

.annotation-input input {
  flex: 1;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 3px;
}

/* 自定义样式 */
.custom-red {
  color: red !important;
  font-weight: bold;
}

.custom-highlight {
  background-color: #f08080 !important;
}

.annotation {
  display: block;
  font-size: 12px;
  color: #c20dde; 
  margin-top: 2px;
}
.clicked-cell .cell-content {
  text-decoration: underline;
  text-decoration-color: #000000;
  text-underline-offset: 3px;
}
/* 激活状态的按钮 */
.status-options button.active {
  background-color: #e0e0e0;
  font-weight: bold;
}

/* 单元格内容容器 */
.cell-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}
</style>