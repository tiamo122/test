<template>
    <div class="login-container">
        <!-- 校徽 -->
        <img src="@/assets/xiaohui.png" alt="校徽" class="logo">

        <div class="main-content">
            <!-- 左侧轮播图 -->
            <div class="carousel-wrapper">
                <el-carousel height="500px" :autoplay="true" indicator-position="outside">
                    <el-carousel-item v-for="item in 3" :key="item">
                        <h3 class="small">{{ item }}</h3>
                    </el-carousel-item>
                </el-carousel>
            </div>

            <!-- 右侧登录表单 -->
            <div class="auth-wrapper">
                <h1 class="welcome-title">欢迎使用HUST教室预约系统</h1>
                
                
                    <div class="auth-card">
                    <img 
                    src="@/assets/hustpass.png" 
                    alt="统一身份认证"
                    class="auth-image"
                    >


                    <el-form ref="loginForm" :model="loginForm">
                        <el-input 
                            v-model="loginForm.id" 
                            placeholder="请输入账号"
                            class="auth-input"
                        ></el-input>
                        <el-input 
                            v-model="loginForm.password" 
                            placeholder="请输入密码"
                            type="password"
                            class="auth-input"
                        ></el-input>
                        <el-button 
                            type="primary" 
                            class="login-button"
                            @click="submitloginForm"
                        >登录</el-button>
                    </el-form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import request from "@/utils/request.js";
import { setToken } from "@/utils/storage.js";

export default {
    name: "Login",
    data() {
        return {
            loginForm: {
                id: '',
                password: ''
            },
            loginRules: {
                id: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        handleKeydown(event) {
            if (event.key === 'Enter') {
                this.submitloginForm();
            }
        },


    submitloginForm() {
    this.$refs.loginForm.validate((valid) => {
        if (valid) {
        request.post("/superadmin/login", this.loginForm)
        .then(res => {
            if (res.data.code === 1) {
            //console.log("登录成功",res)   
            setToken(res.data.data.token);  
            this.$message.success('登录成功');
            if (res.data.data.role==='0') {
                this.$router.push("/superadmin");
            }else  {
                this.$router.push("/usersbooking");
            }
            
            console.log('token',res.data.data.token);   
        } else {
            this.$message.error(res.msg || '登录失败,请检查账号或者密码！');
        }
        })
        .catch(error => {
        this.$message.error('请求异常：' + error.message);
        });
    }
});
}
    },
    mounted() {
        window.addEventListener('keydown', this.handleKeydown);
    },
    beforeDestroy() {
        window.removeEventListener('keydown', this.handleKeydown);
    }
}
</script>

<style scoped>
.login-container {
    min-height: 100vh;
    background: #f5f7fa;
    position: relative;
}

.logo {
    position: absolute;
    left: 30px;
    top: 20px;
    width: 120px;
    height: auto;
    z-index: 1;
}

.main-content {
    display: flex;
    
    float:relative;
    
}

.carousel-wrapper {
    flex: 1;
    min-width: 40%;
    padding-right: 160px;
    padding-left: 100px;
    padding-top: 180px;
    
}

.auth-wrapper {
    flex: 1;
    padding-right: 200px;
    display: flex;  
    flex-direction: column;
    justify-content: center;
    padding: 0 60px;
    margin-top: 50px;
    border: 0.5px dashed #ddd;
    border-radius: 12px; /* 更圆润的边框 */
}

.welcome-title {
    text-align: center;
    color: #303133;
    font-size: 24px;
    margin-bottom: 40px;
}

.auth-container {
    background: #fff;
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.1);
    height: 400px;
    
}

.carousel-item {
    width: 100%;
    height: 100%;
    background-size: cover;
    background-position: center;
}

.auth-header {
    text-align: center;
    margin-bottom: 30px;
}

.auth-title {
    font-size: 18px;
    color: #303133;
    margin-bottom: 8px;
}

.auth-subtitle {
    font-size: 14px;
    color: #909399;
}

.auth-input {
    margin-bottom: 20px;
}

.login-button {
    width: 100%;
    margin-top: 10px;
    background: #409EFF;
    font-size: 16px;
    padding: 12px;
}
.auth-image:hover {
    transform: scale(1.05);
}

.social-login {
    margin-top: 30px;
    display: flex;
    justify-content: center;
    gap: 20px;
}

.qq-login, .wechat-login {
    padding: 10px 20px;
    border-radius: 4px;
}

.qq-login {
    background: #3498db;
    color: white;
}

.wechat-login {
    background: #67C23A;
    color: white;
}

.iconfont {
    font-family: "iconfont" !important;
    font-size: 18px;
    margin-right: 5px;
}

.el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
}
.auth-image {
    width: 100%;
    height: auto;
    margin-bottom: 20px;
    transition: transform 0.3s;
}

.el-carousel__item:nth-child(3n) {
    background: url("@/assets/nomal-01.png");
}
.el-carousel__item:nth-child(3n+1) {
    background: url("@/assets/nomal-02.png");
}
.el-carousel__item:nth-child(3n+2) {
    background: url("@/assets/xiaoshiguan.jpg");
    background-size: cover;        /* 关键属性1：填充容器 */
    background-position: center;   /* 关键属性2：居中显示 */
    background-repeat: no-repeat;  /* 防止平铺 */
}
@media (max-width: 768px) {
    .el-carousel__item:nth-child(3n+2) {
        background-size: contain;  /* 小屏幕完整显示图片 */
    }
}
</style>