<template>
  <div class="id-recognize-container">
    <h2><span class="artistic-text">Welcome to YINGYING</span></h2>
    <h3 class="register-subtitle">A Web3.0 Passport Constructed Using Biometric Authentication</h3>
    <!-- 隐藏的文件输入框 -->
    <input type="file" ref="fileInput" @change="onFileChange" accept="image/*" class="hidden-file-input" />
    <!-- 自定义上传按钮 -->
    <BaseButton type="success" @click="triggerFileSelect" :disabled="image" aria-label="Upload ID Photo" class="upload-button">
      <!-- 改进后的 SVG 图标 -->
      <svg xmlns="http://www.w3.org/2000/svg" class="upload-icon" viewBox="0 0 24 24" fill="currentColor">
        <path fill-rule="evenodd" d="M12 16a.75.75 0 01-.75-.75V7.56l-3.72 3.72a.75.75 0 11-1.06-1.06l4.5-4.5a.75.75 0 011.06 0l4.5 4.5a.75.75 0 11-1.06 1.06L12.75 7.56v7.69A.75.75 0 0112 16z" clip-rule="evenodd" />
        <path d="M5.25 19.5a.75.75 0 01.75-.75h12a.75.75 0 010 1.5h-12a.75.75 0 01-.75-.75z" />
      </svg>
      <span>{{ image ? 'Uploaded' : 'Upload ID Photo' }}</span>
    </BaseButton>

    <!-- 识别按钮 -->
    <BaseButton type="primary" @click="recognize" :disabled="!image" aria-label="Confirm" class="confirm-button">
      Confirm
    </BaseButton>
    <BaseButton type="primary" @click="goBack" aria-label="Back" class="back-button">
      Back
    </BaseButton>
    <div>
      <h4 class="register-subtitle">—— Register via video ——</h4>
    </div>
    <div class="footer">
      <img src="@/assets/logo2.png" alt="team logo" class="team-logo">
      <p>YINGYING Team Copyrighted © 2024</p>
    </div>

  </div>
</template>

<script>
import BaseButton from '@/components/BaseButton.vue';
import { createWorker } from 'tesseract.js';
import * as faceapi from 'face-api.js';

export default {
  name: 'ID_register',
  components: {
    BaseButton,
  },
  data() {
    return {
      image: null,        // 上传的图片
      result: null,       // OCR识别结果
      idPhoto: null,      // 提取的身份证照片
      loading: false,     // 加载状态
      idPhotoError: false,// 是否提取到身份证照片
      worker: null,       // tesseract worker 实例
      idNumber: null,     // 身份证号码
    };
  },
  async mounted() {
    await this.loadFaceModels();
  },
  methods: {
    goBack() {
      this.$router.push('/');
    },
    // 触发文件选择
    triggerFileSelect() {
      if (this.fileSelecting) {
        return;
      }
      this.fileSelecting = true;
      this.$refs.fileInput.click();
    },

    // 处理文件上传
    async onFileChange(event) {
      const file = event.target.files[0];
      if (file && file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = async (e) => {
          this.image = e.target.result; // 确保读取完成后设置图片数据
          await this.$nextTick(() => {
            console.log('图片已读取');
          });
        };
        reader.readAsDataURL(file); // 使用 FileReader 读取图片
      } else {
        alert('请上传有效的图片文件');
      }
    },

    // 识别图片内容
    async recognize() {
      this.loading = true;
      this.idPhotoError = false;
      this.result = null;
      this.idNumber = null; // 重置身份证号码

      if (!this.image) {
        alert('请先选择一张图片');
        this.loading = false;
        return;
      }

      try {
        if (!this.worker) {
          this.worker = await createWorker();
        }

        const { data: { text } } = await this.worker.recognize(this.image, 'chi_sim');
        this.result = text;

        // 提取身份证号码
        const idNumberMatch = text.match(/\b\d{17}[\dXx]\b/);
        if (idNumberMatch) {
          this.idNumber = idNumberMatch[0];
          console.log('提取的身份证号码:', this.idNumber);
        } else {
          this.idNumber = null;
          console.log('未检测到身份证号码');
        }

        // 触发自定义事件，将身份证信息传递给父组件
        if (this.idNumber) {
          this.$emit('id-info', {
            idNumber: this.idNumber,
          });
        }

      } catch (error) {
        console.error('识别过程中发生错误:', error);
        alert(`识别过程中发生错误: ${error.message || '请重试'}`);
      } finally {
        this.loading = false;
        if (this.worker) {
          await this.worker.terminate();
          this.worker = null;
        }
        this.extractIdPhoto(this.image);
      }
    },

    // 将 dataUrl 转换为 Blob
    dataUrlToBlob(dataUrl) {
      const byteString = atob(dataUrl.split(',')[1]); // 解码 base64
      const mimeString = dataUrl.split(',')[0].split(':')[1].split(';')[0]; // 获取 MIME 类型
      const ab = new ArrayBuffer(byteString.length);
      const ia = new Uint8Array(ab);

      for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
      }

      return new Blob([ab], { type: mimeString });
    },

    // 提取身份证照片中的人脸信息
    async extractIdPhoto(image) {
      const blob = this.dataUrlToBlob(image);
      const img = await faceapi.bufferToImage(blob);
      const detections = await faceapi.detectSingleFace(img).withFaceLandmarks().withFaceDescriptor();
      if (detections) {
        // 返回面部描述符数组
        this.$emit('facePhoto', detections.descriptor);
        console.log('提取的身份证照片中的人脸信息:', detections.descriptor);
      } else {
        alert('请上传清晰的身份证照片');
        return null;
      }
    },

    // 加载人脸识别模型
    async loadFaceModels() {
      const modelUrl = '/models'; // 模型文件路径
      try {
        await faceapi.nets.ssdMobilenetv1.loadFromUri(modelUrl);
        await faceapi.nets.faceLandmark68Net.loadFromUri(modelUrl);
        await faceapi.nets.faceRecognitionNet.loadFromUri(modelUrl);
        console.log('Face API 模型加载完成');
      } catch (error) {
        console.error('加载人脸识别模型失败:', error);
        alert('加载人脸识别模型失败，请检查模型路径和网络连接。');
      }
    },
  },

  // 在组件卸载时清理工作
  beforeUnmount() {
    if (this.worker) {
      this.worker.terminate();
      this.worker = null;
    }
  },
};
</script>

<style scoped>
.id-recognize-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 50px 40px;
  border: none;
  /* 移除边框 */
  border-radius: 12px;
  background-color: rgba(0, 0, 0, 0);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  
  /* 添加 Flex 布局并居中对齐 */
  display: flex;
  flex-direction: column;
  align-items: center;
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 0px;
  font-size: 24px;
}

.hidden-file-input {
  display: none;
}

.upload-button, .confirm-button {
  display: flex;
  align-items: center;
  justify-content: center;
  /* 设置按钮宽度 */
  width: 220px;
  height: 50px;
  padding: 0;
  margin: 15px 0; /* 添加上下间距 */
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.back-button {
  background-color: #4c185c; 
  color: white;
  border: none;
  margin-top: 10px;
  width: 220px;
  height: 50px;
  font-size: 16px;
}

.upload-button {
  background-color: #4c185c; /* 未上传图片按钮颜色 */
  color: white;
  border: none;
}

.upload-button:hover {
  background-color: #100113;
}

.confirm-button {
  background-color: #610580; /* 确定按钮颜色 */
  color: white;
  border: none;
}

.confirm-button:disabled {
  background-color:#7e7e7e;
  cursor: not-allowed;
}

.confirm-button:not(:disabled):hover {
  background-color: #810fdf;
}

.upload-icon {
  width: 20px;
  height: 20px;
  margin-right: 8px;
  fill: white;
}

.result-section {
  margin-top: 20px;
}

.id-number-container,
.photo-container {
  margin-top: 15px;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
}

.id-number-container h3,
.photo-container h3 {
  margin-bottom: 10px;
  color: #555;
}

.id-photo {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
}

.artistic-text {
  font-family: '仿宋', 'STXihei', cursive;
  /* 设置字体大小和颜色 */
  font-size: 40px;
  color: #ffffff;
  /* 添加文字阴影以增强艺术感 */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  margin-bottom: 20px;
}

.register-subtitle {
  margin-bottom: 20px; /* 修正错误的单位 */
  font-size: 16px; /* 增大字体大小 */
  color: #ffffff;
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

/* 响应式设计 */
@media (max-width: 480px) {
  .id-recognize-container {
    padding: 30px 20px;
    border-radius: 8px;
  }

  .artistic-text {
    font-size: 28px; /* 在小屏幕上适当减小字体大小 */
  }

  .upload-button, .confirm-button {
    width: 180px; /* 减小按钮宽度以适应小屏幕 */
    height: 45px;
    font-size: 14px;
  }
  .register-subtitle {
    margin-bottom: 3px; /*间距*/
    font-size: 14px; /* 增大字体大小 */
    color: hsl(0, 0%, 100%);
  }
}
</style>