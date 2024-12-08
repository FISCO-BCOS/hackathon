<template>
  <div class="page-wrapper">
    <div class="header">
      <Header />
    </div>

    <div class="main-content">
      <div class="transfer-section">
        <div class="transfer-container">
          <h3 class="transfer-title">版权流转</h3>
          <div class="input-group">
            <input
              v-model="collectionId"
              type="text"
              class="form-control custom-input"
              placeholder="输入Collection ID" 
            >
            <input
              v-model="ownerId1"
              type="text"
              class="form-control custom-input"
              placeholder="输入Owner ID 1"
            >
            <input
              v-model="ownerId2"
              type="text"
              class="form-control custom-input"
              placeholder="输入Owner ID 2"
            >
            <input
              v-model="goods"
              type="number"
              class="form-control custom-input"
              placeholder="输入转账金额"
            >
            <div class="input-group-append">
              <button
                class="btn btn-transfer"
                @click="transferToChains"
              >确认提交</button>
            </div>
          </div>
          <div
            v-if="isLoading"
            class="mt-3 status-message"
          >加载中...</div>
          <div
            v-else-if="error"
            class="mt-3 status-message error"
          >{{ error }}</div>
          <div
            v-else-if="transferSuccess"
            class="mt-3 status-message success"
          >
            转账成功
          </div>
          <div
            v-if="transferResult"
            class="transfer-result"
          >
            <h4>转账结果</h4>
            <div class="info-content">
              <pre>{{ transferResult }}</pre>
            </div>
          </div>

        </div>
        <div class="background-section">
          <Background :cols="5" />
        </div>
      </div>
      <div class="navigation-section">
        <button
          class="btn-navigate"
          @click="navigateToTxt2img"
        >
          <span class="btn-text">返回主页</span>
          <span class="btn-icon">←</span>
        </button>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import Header from '@/view/header/index.vue'
import { useRouter } from 'vue-router'
import Background from '@/view/background/index.vue'
const router = useRouter()
const collectionId = ref('')
const ownerId1 = ref('')
const ownerId2 = ref('')
const goods = ref(0)
const isLoading = ref(false)
const error = ref(null)
const transferSuccess = ref(false)
const transferResult = ref(null)
const navigateToTxt2img = () => {
  router.push({ name: 'dashboard' })
}
const transferToChains = async() => {
  if (!collectionId.value || !ownerId1.value || !ownerId2.value || goods.value <= 0) {
    error.value = '请填写完整的转账信息'
    return
  }

  isLoading.value = true
  error.value = null
  transferSuccess.value = false
  transferResult.value = null

  try {
    const response = await fetch(`http://127.0.0.1:8887/transfertochains`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        owner_id1: ownerId1.value,
        owner_id2: ownerId2.value,
        collectionid: collectionId.value,
        goods: goods.value,
      }),
    })

    if (!response.ok) {
      throw new Error(`请求失败: ${response.status}`)
    }

    const data = await response.json()
    transferResult.value = data
    transferSuccess.value = true
  } catch (e) {
    error.value = e.message || '转账发生错误'
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
/* 根据你提供的样式定制 */
.page-wrapper {
  min-height: 100vh;
  background: white;
  background-size: cover;
  position: relative;
}

.main-content {
  display: flex;
  padding: 20px;
  height: calc(100vh - 60px); /* 假设header高度为60px */
}

.transfer-section {
  flex: 1;
  padding: 20px;
}

.transfer-container {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  width: 400px; /* 设置一个固定的宽度 */

}

.transfer-title {
  color: blue;
  margin-bottom: 20px;
  font-weight: bold;
}

.custom-input {
  border: 2px solid blue;
  border-radius: 8px;
  padding: 12px;
  font-size: 16px;
  margin-bottom: 15px;
  width: 100%; /* 使输入框宽度占满其父容器的宽度 */
  box-sizing: border-box; /* 确保内边距和边框包含在宽度内 */
}

.btn-transfer {

background-color: blue;
color: white;
border: none;
padding: 12px 25px;
border-radius: 8px;
font-weight: bold;
transition: background-color 0.3s;
width: 100%; /* 使输入框宽度占满其父容器的宽度 */
box-sizing: border-box; /* 确保内边距和边框包含在宽度内 */
}

.btn-transfer:hover {
  background-color: blue;
}

.status-message {
  text-align: center;
  padding: 10px;
  border-radius: 5px;
}

.error {
  color: #dc3545;
  background-color: rgba(220, 53, 69, 0.1);
}

.success {
  color: #28a745;
  background-color: rgba(40, 167, 69, 0.1);
}

.transfer-result {
  margin-top: 20px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 10px;
  padding: 20px;
}

.info-content {
  margin-top: 15px;
}

.info-content pre {
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 4px;
  padding: 10px;
}
.navigation-section {
  flex: 0 0 500px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.btn-navigate {
    position: absolute; /* 使用绝对定位 */
    top: 20px; /* 距离页面顶部20像素 */
    left: 20px; /* 距离页面左侧20像素 */
    /* 其他样式保持不变 */
    background-color: #ffffff;
    border: none;
    border-radius: 12px;
    padding: 20px 40px;
    font-size: 18px;
    font-weight: bold;
    color: blue;
    cursor: pointer;
    transition: all 0.3s;
    display: flex;
    align-items: center;
    gap: 10px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.btn-navigate:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.btn-icon {
  font-size: 24px;
  transition: transform 0.3s;
}

.btn-navigate:hover .btn-icon {
  transform: translateX(5px);
}
.background-section {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  width: 70%; /* 调整背景占据屏幕宽度的比例，这里设置为 50% */
  z-index:-1; /* 将背景层级设为 -1，使其位于其他内容之后 */
  background-color: white;
}
.page-wrapper{
  z-index: 1;
}
</style>
