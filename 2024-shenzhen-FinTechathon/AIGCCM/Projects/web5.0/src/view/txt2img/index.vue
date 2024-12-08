<template>
  <div class="page-wrapper">
    <div class="header">
      <Header />
    </div>

    <div class="main-content">
      <div class="transfer-section">
        <div class="transfer-container">
          <h3 class="transfer-title">Text to Image Generator</h3>
          <div class="input-group">
            <input
              v-model="inputText"
              type="text"
              class="form-control custom-input"
              placeholder="请输入生成关键词"
            >
            <input
              v-model="inputNegativeText"
              type="text"
              class="form-control custom-input"
              placeholder="请输入反向生成关键词"
            >
            <input
              v-model="collection_name"
              type="text"
              class="form-control custom-input"
              placeholder="请输入作品名称"
            >
            <!-- <input
              v-model="collection_id"
              type="text"
              class="form-control custom-input"
              placeholder="请输入收藏品id"
            > -->
            <input
                v-model="owner_id"
                type="text"
                class="form-control custom-input"
                placeholder="制作者id"
            >
            <!-- <input
              v-model="certificate_time"
              type="text"
              class="form-control custom-input"
              placeholder="认证时间"
            > -->
            <div
    class="controls"
>
<label for="cfgScale">CFG Scale: {{ cfgScale }}</label>
<input
    v-model="cfgScale"
    type="range"
    min="1"
    max="20"
>
  </div>
  <div
      class="controls"
  >
<label for="samplingSteps">Sampling Steps: {{ samplingSteps }}</label>
<input
    v-model="samplingSteps"
    type="range"
    min="1"
    max="100"
>
  </div>
            <div class="input-group-append">
              <button
                class="btn btn-transfer"
                @click="generateImage"
              >点击生成图片</button>
            </div>
          </div>
          <div
    v-show="showImagePreview"
    class="preview"
>
  <div class="image-preview">
    <p>Generated Image:</p >
    <img
        :src="imageData"
        alt="Generated"
    >
        <div
          v-show="showHashPreview"
          style="position: absolute; bottom: 0; width: 540px; left: 50%; transform: translateX(-50%);
        background-color: #3498db; padding: 10px; border: 1px solid #2980b9; border-radius: 10px;
         font-family: Arial, sans-serif; font-size: 14px; color: #fff;"
        >
          <p style="font-weight: bold;">Verdict OK, Accepted, Picture Successfully Added</p>
          <p style="font-weight: bold;">collection_id:</p>
<!--          <p>"0xce28a18b54ee72450c403968f705253a59c87a22801a88cc642ae800bb8b4848"</p>-->
          <p>{{ collection_id }}</p>
          <p style="font-weight: bold;">tx:</p>
          <p>{{ tx }}</p>
        </div>
              <div>
                <button @click="confirmData">确认</button>
                <button @click="rejectData">不确认</button>
              </div>
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

<script>
import { useRouter } from 'vue-router'
import Background from '@/view/background/index.vue'
import Header from '@/view/header/index.vue'

export default {
  components: {
    Background,
    Header },

  data() {
    return {
      inputText: 'british short hair cat, high quality',
      inputNegativeText: 'out of frame, lowers, text, error',
      width: 512,
      height: 512,
      cfgScale: 7,
      samplingSteps: 10,
      imageData: null,
      showImagePreview: false,
      inputName: 'name',
      data1: 1,
      data2: 2,
      collection_name: 'name',
      collection_id: 'id',
      collection_make: 'maker',
      certificate_time: 'time',
      owner_id:'owner_111',
      showHashPreview: false,
      tx: 'thistx'

    }
  },
  setup() {
    const router = useRouter() // 使用useRouter钩子获取router实例

    const navigateToTxt2img = () => {
      router.push({ name: 'dashboard' }) // 定义navigateToTxt2img函数
    }

    return {
      navigateToTxt2img // 将navigateToTxt2img函数返回，使其在模板中可用
    }
  },

  methods: {
    async generateImage() {
      // this.showImagePreview = true // 点击按钮后立即显示白色框
      this.showImagePreview = false// 初始不显示图片预览
      this.imageData = null // 重置图片数据

      // 在第一次返回所有数据，第二次只返回1和2
      const payload = {
        text: this.inputText,
        negativetext: this.inputNegativeText,
        width: this.width,
        height: this.height,
        cfgScale: parseInt(this.cfgScale),
        name: this.inputName,
        samplingSteps: parseInt(this.samplingSteps),
        collectionname: this.collection_name,
        collectionid: this.collection_id,
        owner_id: this.owner_id,
        certificatetime: this.certificate_time
      }
      const baseUrl = 'http://127.0.0.1:8887/runsd'
      try {
        const response = await fetch(baseUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(payload)
        })

        if (!response.ok) {
          throw new Error('Network response was not ok')
        }
        const blob = await response.blob()
        console.log(blob)
        const imgURL = URL.createObjectURL(blob)
        console.log(imgURL)
        this.imageData = imgURL
        this.showImagePreview = true // 显示图片预览
        // const data = await response.json()
        // this.imageData = data.imageUrl
      } catch (error) {
        console.error('Error:', error)
      }
    },
    async confirmData() {
      //await this.generateImage()
      await this.sendDataToBackend('http://127.0.0.1:8887/sendtoblock', this.data1)
    },
    async rejectData() {
    //   await this.generateImage()
      await this.sendDataToBackend('http://127.0.0.1:8887/sentchain', this.data2)
    },
    async sendDataToBackend(url, data1) {
      try {
        const datajson = {
            "data":data1
        }
        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(datajson)
        })

        if (!response.ok) {
          throw new Error('Network response was not ok')
        }

        // 处理成功响应
        const data = await response.json()
        this.collection_id = data.collection_id
        this.tx = data.tx
        this.showHashPreview = true
      } catch (error) {
        console.error('Error:', error)
      }
    }

  }
}

</script>

<style scoped>
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
  width: 300px; /* 设置一个固定的宽度 */
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

.info-content img {
  max-width: 100%;
  height: auto;
}

.generated-image {
  max-width: 100%;
  height: auto;
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
    padding: 10px 20px;
    font-size: 18px;
    font-weight: bold;
    color: blue;
    cursor: pointer;
    transition: all 0.3s;
    display: flex;
    align-items: center;
    gap: 10px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    width: fit-content; /* 设置宽度为内容自适应 */
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
  width: 70%;
  z-index: -1;
  background-color: white;
}

.page-wrapper{
  z-index: 1;
}
.controls input[type="range"] {
  width: 100%;
  /* 修改滑块的背景色为红色 */
  -webkit-appearance: none;
  appearance: none;
  height: 7px;
  background: darkgray; /* 默认背景色 */
  border-radius: 5px;
}

.controls input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 15px;
  height: 15px;
  background: gray; /* 修改滑块颜色为红色 */
  border-radius: 50%;
  cursor: pointer;
}
.preview {
  position: absolute;
  top: 100px;
  right: 0;
  left: 700px;
  width: 500px;
  height: 500px;
  padding: 30px;
  background-color: white;
  z-index: 11;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.image-preview img {
  max-width: 70%;
  height: auto;
}
button {
  display: block;
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
