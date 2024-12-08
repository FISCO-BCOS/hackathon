<template>
  <div class="app-choose-background">
    <div class="app-choose-container">
      <div class="app-choose-title">
        <h2>Firstly register,please select the app you want,then go to login!</h2>
      </div>
      <div class="apps-wrapper">
        <div class="app-section left">
          <h2>My App</h2>
          <div class="app-grid">
            <div v-for="app in userApps" :key="app.id" class="app-card">
              <img :src="app.imageUrl" alt="app.name" class="app-image" />
              <span class="app-link">
                {{ app.name }}
              </span>
            </div>
          </div>
        </div>
        <div class="app-section right">
          <h2>All Apps</h2>
          <div class="app-grid">
            <div v-for="app in allApps" :key="app.id" class="app-card" @click="selectApp(app)">
              <img :src="app.imageUrl" alt="app.name" class="app-image" />
              <span class="app-link">
                {{ app.name }}
              </span>
            </div>
          </div>
        </div>
      </div>
      <div class="login-button">
        <BaseButton class="beautiful-button" @click="goToLogin">Go to Login</BaseButton>
      </div>
      <div class="footer">
        <img src="@/assets/logo2.png" alt="team logo" class="team-logo">
        <p>YINGYING Team Copyrighted © 2024</p>
      </div>
    </div>
  </div>
</template>
  
  <script>
  import axios from 'axios';
  import BaseButton from '@/components/BaseButton.vue';
  // 定义应用名称到图片路径的映射
  const appImageMap = {
    '抖音': require('@/assets/抖音.jpg'),
    '微信': require('@/assets/微信.jpg'),
    '哔哩哔哩动画': require('@/assets/哔哩哔哩.jpg')
    // 添加更多应用名称和图片路径
  };
  
  export default {
    data() {
      return {
        userApps: [], // 用户的应用列表
        allApps: [
          { id: 1, name: '抖音', imageUrl: require('@/assets/抖音.jpg')  },
          { id: 2, name: '微信', imageUrl: require('@/assets/微信.jpg') },
          { id: 3, name: '哔哩哔哩动画', imageUrl: require('@/assets/哔哩哔哩.jpg') } // 目标应用
        ], // 所有应用列表
        selectedApp: null
      };
    },
    created() {
      this.fetchUserApps();
    },
    methods: {
      async fetchUserApps() {
        try {
          const response = await axios.post('http://127.0.0.1:8088/getappnames');
          console.log('API 响应:', response.data); // 调试信息
          if (response.data.success) {
            // 假设 response.data.apps 是一个对象
            this.userApps = Object.entries(response.data.apps).map(([key, name], index) => ({
              id: index, // 使用索引作为 id
              name: name,
              imageUrl: appImageMap[name] || require('@/assets/logo.png') // 使用映射表获取图片URL
            }));
            console.log('转换后的用户应用列表:', this.userApps); // 调试信息
          } else {
            this.$message.error('无法获取用户应用列表');
          }
        } catch (error) {
          console.error('请求过程中发生错误:', error);
          this.$message.error('请求过程中发生错误，请重试');
        }
      },
      async selectApp(app) {
        this.selectedApp = app;
        try {
          // 发送应用名到后端
          const response = await axios.post('http://127.0.0.1:8088/appname', { appname: app.name });
  
          if (response.data.success) {
            this.$message.success('应用已添加到您的列表');
            this.fetchUserApps(); // 更新用户应用列表 
          } else {
            this.$message.error('无法添加应用，请重试');
          }
        } catch (error) {
          console.error('请求过程中发生错误:', error);
          this.$message.error('请求过程中发生错误，请重试');
        }
      },
      goToLogin() {
        this.$router.push({ name: 'face_login' });
      }
    },
    beforeRouteLeave(to, from, next) {
      // 允许导航到特定页面
      if (to.name !== 'Login') {
        next();
      } else {
        next({ name: 'Login' });
      }
    }
  };
  </script>
  
  <style scoped>
  .app-choose-background {
    position: fixed; 
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-image: url('@/assets/background.jpg');
    background-size: cover;
    background-position: center center;
    background-repeat: no-repeat;
    background-attachment: fixed;
  }
  
  .app-choose-container {
    max-width: 1000px;
    margin: 0 auto;
    padding: 20px;
    background-color: rgba(0, 0, 0, 0.1);
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  .apps-wrapper {
    display: flex;
    justify-content: space-between;
    width: 100%;
    gap: 40px; /* 调整两边应用之间的间距 */
  }
  
  .app-section {
    flex: 1;
    padding: 20px;
    height: 500px;
    width: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 8px;
  }
  
  h2 {
    text-align: center;
    color: hsl(0, 100%, 100%);
    font-family: 'fangsong';
    margin-bottom: 5px;
    font-size: 40px;
    font-weight: bold;
  }
  
  .app-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
  }
  
  .app-card {
    width: 150px;
    padding: 10px;
    background-color: rgba(0, 0, 0, 0);
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    text-align: center;
    transition: transform 0.3s;
    cursor: pointer;
  }
  
  .app-card:hover {
    transform: scale(1.05);
  }
  
  .app-image {
    width: 50px;
    height: 50px;
    object-fit: cover;
    border-radius: 4px;
    margin-bottom: 10px;
  }
  
  .app-link {
    display: block;
    color: #ffffff;
    text-decoration: none;
    cursor: pointer;
    transition: color 0.3s;
  }
  
  .app-link:hover {
    color: #5948f7;
    text-decoration: none;
  }
  
  .return-button {
    background-color: #888888;
    color: white;
    border: none;
    margin-bottom: 20px;
  }
  
  .return-button:hover {
    background-color: #555555;
  }
  
  /* 响应式设计 */
  @media (max-width: 768px) {
    .apps-wrapper {
      flex-direction: column;
      gap: 20px;
    }
  }
  
  @media (max-width: 480px) {
    .app-choose-container {
      padding: 30px 20px;
      border-radius: 8px;
    }
  
    .app-card {
      width: 120px;
    }
  
    .app-image {
      width: 80px;
      height: 80px;
    }
  
    .return-button {
      width: 100%;
      max-width: none;
    }
  }
  
  .footer {
    display: flex;
    align-items: center;
    justify-content: center;
    color: #ffffff;
    font-size: 14px;
    margin-top: 20px;
  }
  
  /* 添加团队Logo的样式 */
  .team-logo {
    width: 20px; /* 根据需要调整大小 */
    height: auto;
    margin-right: 15px;
    vertical-align: middle;
  }
  
  .app-choose-title {
    margin-bottom: 20px;
    font-size: 20px;
    color: hsl(0, 100%, 100%);
    font-family: 'fangsong';
  }
  
  .my-app-title {
    text-align: center;
    color: #ffffff;
    margin-bottom: 15px;
    font-family: 'fangsong';
  }
  
  .my-app-title h3 {
    font-size: 18px;
    font-weight: bold;
  }
  .login-button {
    margin-top: 20px;
    color: #ffffff;
    font-size: 20px;
    font-family: 'fangsong';

  }
  .beautiful-button {
    background: linear-gradient(45deg, #5948f7, #f748b8);
    color: #ffffff;
    padding: 12px 24px;
    border: none;
    border-radius: 25px;
    font-size: 18px;
    font-family: 'fangsong', sans-serif;
    cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .beautiful-button:hover {
    transform: scale(1.05);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
  }
  
  .beautiful-button:active {
    transform: scale(0.98);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  </style>