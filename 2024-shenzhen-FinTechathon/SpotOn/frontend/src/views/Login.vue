<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title"> <span class="artistic-text">Welcome to YINGYING</span></h2>
      <h3 class="login-subtitle">A Web3.0 Passport Constructed Using Biometric Authentication</h3>
      <div class="button-group">
        <el-button 
          type="primary" 
          class="login-button" 
          @click="login">Login
        </el-button>
      </div>
      <h4 class="login-subtitle">—— Login via video ——</h4>
      <div class="register-link">
          Don't have an account? <router-link to="face_register">Register now</router-link>
      </div>
      <div class="footer">
        <img src="@/assets/logo2.png" alt="team logo" class="team-logo">
        <p>YINGYING Team Copyrighted © 2024</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  methods: {
    login() {
      this.$router.push({ name: 'face_login' });  // 跳转到人脸登录页面
    }
  }
}
</script>

<style scoped>
/* 背景样式 */
.login-container {
  position: fixed; /* 固定位置，覆盖整个视口 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  
  display: flex;
  justify-content: center;
  align-items: center;

  background-image: url('@/assets/background.jpg'); /* 使用绝对路径 */
  background-size: cover; /* 图片覆盖整个背景 */
  background-position: center center; /* 居中对齐 */
  background-repeat: no-repeat; /* 防止图片重复 */
  background-attachment: fixed; /* 固定背景图片 */

}

/* 登录框样式 */
.login-box {
  background-color: rgba(0, 0, 0, 0.5);
  padding: 50px 80px;  /* 调整内边距，左右各80px */
  border-radius: 20px;  /* 圆角效果 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 柔和阴影 */
  text-align: center;
  width: 400px; /* 调整为更宽的登录框 */
  /* height: 700px; */ /* 可以根据内容调整高度，建议移除固定高度 */
  border: 1px solid #9079e6; /* 添加边框 */
}

/* 标题样式 */
.login-title {
  margin-bottom: 10px;
  font-size: 40px; /* 调整字体大小以适应容器 */
  font-weight: bold;
  color: #333;
  white-space: nowrap; /* 防止换行 */
  overflow: hidden; /* 隐藏溢出部分 */
  text-overflow: ellipsis; /* 添加省略号表示溢出 */
}

.login-subtitle {
  margin-bottom: 20px; /* 修正错误的单位 */
  font-size: 16px; /* 增大字体大小 */
  color: #ffffff;
}
/* 按钮组样式 */
.button-group {
  display: flex;
  justify-content: center;
  margin-bottom: 20px; /* 添加下边距 */
}

/* 按钮样式 */
.login-button {
  width: 100%;  /* 按钮宽度填满容器 */
  max-width: 200px; /* 设置最大宽度 */
  font-size: 20px;
  border-radius: 25px;  /* 圆角按钮 */
  padding: 10px 20px;  /* 增加内边距 */
  background-image: linear-gradient(135deg, #44056d, #9326ec); /* 渐变色背景 */
  border: none;  /* 移除默认边框 */
  color: rgb(255, 255, 255);
}

.login-button:hover {
  background-image: linear-gradient(135deg, #5c1a83, #a719e9); /* 渐变色背景的反转 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);  /* 悬停时的阴影效果 */
}
.artistic-text {
/* 使用自定义艺术字体 */
font-family: '仿宋', 'STXihei', cursive;
/* 设置字体大小和颜色 */
font-size: 40px;
color: #ffffff;

/* 添加文字阴影以增强艺术感 */
text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);

white-space: nowrap; /* 防止换行 */
}
.register-link {
  color: #ffffff;
  font-size: 14px;
}

.register-link a {
  color: #66b3ff;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}

/* 添加团队Logo的样式 */
.team-logo {
  width: 20px; /* 根据需要调整大小 */
  height: auto;
  margin-right: 15px;
  vertical-align: middle;
}

.footer {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 14px;
  margin-top: 20px;
}

/* 响应式设计调整 */
@media (max-width: 480px) {
  .login-box {
    padding: 30px 20px;
    border-radius: 8px;
  }

  .login-title {
    font-size: 28px; /* 在小屏幕上适当减小字体大小 */
  }

  .artistic-text {
    font-size: 28px; /* 在小屏幕上适当减小字体大小 */
  }
}
</style>
