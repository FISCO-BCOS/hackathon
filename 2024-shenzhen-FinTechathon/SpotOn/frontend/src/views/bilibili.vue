<template>
  <div>
    <!-- 这个页面内容会在跳转前短暂显示 -->
    <p class="transition-text">正在跳转到哔哩哔哩...</p>
  </div>
</template>

<script>
export default {
  mounted() {
    // 页面加载后立即跳转到哔哩哔哩
    window.location.href = 'https://www.bilibili.com';
  },

  
}
</script>

<style scoped>
.transition-text {
  font-size: 20px;
  color: #ff69b4;
  text-align: center;
  animation: fadeInOut 2s ease-in-out, bounce 0.5s infinite alternate;
}


@keyframes fadeInOut {
  0%, 100% { opacity: 0; }
  50% { opacity: 1; }
}

@keyframes bounce {
  0% { transform: translateY(0); }
  100% { transform: translateY(-10px); }
}
</style>