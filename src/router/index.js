import Vue from "vue";
import VueRouter from "vue-router";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import "@/assets/css/global.css";
// 在文件顶部添加echarts导入
import echarts from 'echarts';  // 或按需引入
import Login from "@/views/Login.vue";
import Booking from "@/views/superadmin/Booking.vue";
import Superadmin from "@/views/superadmin/Superadmin.vue";
import path from "path-browserify";
import BookingRecords from "@/views/superadmin/BookingRecords.vue";
import AuditBooking from "@/views/superadmin/AuditBooking.vue";
import Calendar from "@/views/superadmin/Calendar.vue";
import ImportCurriculum from "@/views/superadmin/ImportCurriculum.vue";
import UserManagement from "@/views/superadmin/UserManagement.vue";
import ManageBuildings from "@/views/superadmin/ManageBuildings.vue";
import UsersBooking from "@/views/users/UsersBooking.vue";
import UsersBookingRecords from "@/views/users/UsersBookingRecords.vue";
Vue.prototype.$echarts = echarts;
Vue.use(ElementUI);
Vue.use(VueRouter);

const routes = [
{
    path: "/",
    redirect: "/login"
},
{
    path: '/login',
    component:Login
},
{
    path: "*",
    redirect: "/login"
},
{   
    path:"/booking",
    component:Booking
},
{   
    path:"/records",
    component: BookingRecords
},
{   
    path:"/superadmin",
    component:Superadmin
},
{
    path:"/audit",
    component:AuditBooking
},
{
    path:"/calendar",
    component:Calendar
},
{
    path:"/import",
    component:ImportCurriculum
},
{
    path:"/users",
    component:UserManagement
},
{
    path:"/classrooms",
    component:ManageBuildings
},
{
    path:"/usersbooking",
    component:UsersBooking
},
{
    path:"/usersrecords",
    component:UsersBookingRecords
}

]


const router = new VueRouter({
    routes
});
  //没登录的情况下，访问任何一个页面都会返回登录页面
router.beforeEach((to, from, next) => {
    if (to.meta.requireAuth) {
    const token = getToken();
    if (token !== null) {
        //直接放行
        next();
    } else {
        next("/login");
    }
    }
    else{
    next();
    }
    });
export default router;