<template>
  <div class="header">
    <Header />
  </div>
  <div
      class="container"
      style="background-color: white; padding: 20px;"
  >
    <div class="background">
      <Background :cols="5" />
    </div>
    <!-- <div class="page">
      <Header></Header>
      </div>-->
    <div
        class="controls"
    >
      <h1>Text to Image Generator</h1>
      <div class="input-group">
        <input
            v-model="inputText"
            type="text"
            placeholder="请输入生成关键词"
            class="input-field"
        >
        <input
            v-model="inputNegativeText"
            type="text"
            placeholder="请输入反向生成关键词"
            class="input-field"
        >
        <input
            v-model="collection_name"
            type="text"
            placeholder="请输入作品名称"
            class="input-field"
        >
        <input
            v-model="collection_id"
            type="text"
            placeholder="请输入收藏品id"
            class="input-field"
        >
        <input
            v-model="collection_make"
            type="text"
            placeholder="制作者署名"
            class="input-field"
        >
        <input
            v-model="certificate_time"
            type="text"
            placeholder="认证时间"
            class="input-field"
        >

      </div>

      <label for="cfgScale">CFG Scale: {{ cfgScale }}</label>
      <input
          v-model="cfgScale"
          type="range"
          min="1"
          max="20"
      >

      <label for="samplingSteps">Sampling Steps: {{ samplingSteps }}</label>
      <input
          v-model="samplingSteps"
          type="range"
          min="1"
          max="100"
      >

      <button @click="generateImage">点击生成图片</button>
    </div>
    <div
        v-show="showImagePreview"
        class="preview"
    >
      <div class="image-preview">
        <p>Generated Image:</p>
        <img
            :src="imageData"
            alt="Generated"
        >
        <div>
          <button @click="confirmData">确认</button>
          <button @click="rejectData">不确认</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import Background2 from '@/view/backgroundtest/index.vue'
import Background from '@/view/background/index.vue'
import Header from '@/view/header/index.vue'
// import Header from "@/pages/pc/Home/header/Index.vue";
export default {
  components: {
    Background2,
    Background,
    Header
    // Header
  },
  data() {
    return {
      inputText: '',
      inputNegativeText: '',
      width: 512,
      height: 512,
      cfgScale: 7,
      samplingSteps: 50,
      imageData: null,
      showImagePreview: false,
      inputName: '',
      data1: 1,
      data2: 2,
      collection_name: '',
      collection_id: '',
      collection_make: '',
      certificate_time: ''

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
        cfgScale: parseint(this.cfgScale),
        name: this.inputName,
        samplingSteps: parseint(this.samplingSteps),
        collectionname: this.collection_name,
        collectionid: this.collection_id,
        collectionmake: this.collection_make,
        certificatetime: this.certificate_time
      }
      const baseUrl = 'http://127.0.0.1:8888/runsd';
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
        const imgURL = URL.create0bjectURL(blob)
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
      await this.generateImage()
      await this.sendDataToBackend('http://127.0.0.1:8888/sentchain',this.data1)
    },
    async rejectData() {
      await this.generateImage()
      await this.sendDataToBackend('http://127.0.0.1:8888/sentchain',this.data2)
    },
    async sendDataToBackend(url, data) {
      try {
        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        })

        if (!response.ok) {
          throw new Error('Network response was not ok')
        }

        // 处理成功响应
      } catch (error) {
        console.error('Error:', error)
      }
    }
  }
}
</script>

<style>

.container {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}
.controls input[type="range"] {
  width: 85%; /* 调整拖拉条的宽度，这里设置为 80% */
}

.background2 {
  position: absolute;

  width: 100%;
  height: 100%;
  z-index: 1;
  overflow: hidden;
  pointer-events: none;
}

.controls input[type="range"] {
  width: 85%;
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
.controls {
  position: absolute;
  top: 1px;
  left: 50px;
  width: 500px;
  height: 500px;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.8);
  z-index: 20;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.background {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  width: 70%; /* 调整背景占据屏幕宽度的比例，这里设置为 50% */
  z-index: 10; /* 将背景层级设为 -1，使其位于其他内容之后 */
  background-color: white;
}

.input-field {
  width: 100%;
  padding: 7px;
  margin-top: 7px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
  transition: border-color 0.3s;
}
.header{
  z-index: 1000; /* 将背景层级设为 -1，使其位于其他内容之后 */
  background-color: white;
}
.input-field:focus {
  border-color: #007bff;
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

label {
  margin-top: 10px;
  display: block;
}

.preview {
  position: absolute;
  top: 0;
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

label {
  margin-bottom: 5px;
}

select {
  padding: 2px;
  margin-bottom: 5px;
  width: 70%;
  border: 3px solid #ccc;
  border-radius: 2px;

}
</style>