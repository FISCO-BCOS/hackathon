<template>
  <div class="login-overlay">
    <div class="login-modal">
      <div class="video-container">
        <div class="video-wrapper">
          <video ref="video" autoplay></video>
          <canvas ref="canvas"></canvas>
        </div>
      </div>
      <div class="attention">
        <h1><span class="attention-text">Please ensure that the face is clear and visible</span></h1>
        <h1><span class="attention-text">Don't move your head</span></h1>
      </div>
      <div class="button-container">
        <BaseButton 
          type="primary" 
          @click="goBack" 
          aria-label="返回上一页"
          marginTop="20px"
        >
          Back
        </BaseButton>
      </div>
      <div class="footer">
        <p>YINGYING Team Copyrighted © 2024</p>
      </div>
    </div>
  </div>
</template>
  
<script>
import * as faceapi from 'face-api.js';
import { ElLoading, ElMessage } from 'element-plus';
import BaseButton from '@/components/BaseButton.vue';

export default {
  name: 'Login',
  components: {
    BaseButton,
  },
  data() {
    return {
      stream: null,
      detectedFace: null,
      loadingInstance: null,
    };
  },
  mounted() {
    this.showLoading();
    this.initFaceRecognition();
    window.addEventListener('resize', this.updateCanvasSize);
  },
  methods: {
    showLoading() {
      this.loadingInstance = ElLoading.service({
        lock: true,
        text: 'Loading Models...',
        background: 'rgba(0, 0, 0, 0.7)',
      });
    },
    hideLoading() {
      if (this.loadingInstance) {
        this.loadingInstance.close();
      }
    },
    async loadModels() {
      const modelUrl = '/models'; // 模型文件放置路径
      await faceapi.nets.ssdMobilenetv1.loadFromUri(modelUrl);
      await faceapi.nets.faceLandmark68Net.loadFromUri(modelUrl);
      await faceapi.nets.faceRecognitionNet.loadFromUri(modelUrl);
      console.log('Face API 模型加载完成');
    },
    async startAutoCapture() {
      const scanDuration = 2000; // 总时长2秒
      const scanInterval = 200;  // 扫描频率200ms
      const iterations = scanDuration / scanInterval;
      let bestDescriptorObj = null; // 用于存储置信度最高的描述符及其检测结果

      try {
        const video = this.$refs.video;
        const canvas = this.$refs.canvas;
        const context = canvas.getContext('2d');
        this.updateCanvasSize(); // 调整画布大小

        for (let i = 0; i < iterations; i++) {
          context.drawImage(video, 0, 0, canvas.width, canvas.height);

          const detections = await faceapi.detectAllFaces(canvas)
            .withFaceLandmarks() // 确保获取到面部特征点
            .withFaceDescriptors(); // 获取面部描述符

          detections.forEach(detection => {
            const score = detection.detection.score;
            if (!bestDescriptorObj || score > bestDescriptorObj.score) {
              bestDescriptorObj = {
                descriptor: detection.descriptor,
                score: score,
                detection: detection.detection,
                landmarks: detection.landmarks,
              };
            }
          });

          await new Promise(resolve => setTimeout(resolve, scanInterval));
        }

        if (!bestDescriptorObj) {
          ElMessage.warning('2秒内未检测到人脸');
          this.handleClose();
          return;
        }

        this.detectedFace = bestDescriptorObj.descriptor;
        // console.log('置信度最高的人脸描述符:', this.detectedFace);
        this.$emit('faceInfo', this.detectedFace);
        //console.log('Emitted face-info event with faceInfo:', this.detectedFace);
      } catch (error) {
        console.error('人脸识别过程中发生错误:', error);
        ElMessage.error('人脸识别过程中发生错误，请重试。');
        this.handleClose();
      }
    },
    goBack() {
      this.stopRecognition();
      this.$router.push({ name: 'Login' }); // 直接导航到 Login 页面
    },
    stopRecognition() {
      if (this.stream) {
        this.stream.getTracks().forEach(track => track.stop());
        this.stream = null;
      }
      const video = this.$refs.video;
      video.srcObject = null;
      const canvas = this.$refs.canvas;
      const context = canvas.getContext('2d');
      context.clearRect(0, 0, canvas.width, canvas.height);
    },
    updateCanvasSize() {
      const canvas = this.$refs.canvas;
      const video = this.$refs.video;
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
    },
    async initVideo() {
      try {
        this.stream = await navigator.mediaDevices.getUserMedia({ video: true });
        this.$refs.video.srcObject = this.stream;
        return new Promise((resolve) => {
          this.$refs.video.onloadedmetadata = () => {
            this.$refs.video.play();
            resolve();
          };
        });
      } catch (error) {
        console.error('获取摄像头权限失败:', error);
        if (error.name === 'NotAllowedError' || error.name === 'PermissionDeniedError') {
          ElMessage.error('无法访问摄像头权限。请检查浏览器设置并确保已授予摄像头访问权限。');
        } else if (error.name === 'NotFoundError') {
          ElMessage.error('未检测到摄像头设备。请连接摄像头并重试。');
        } else {
          ElMessage.error('无法访问摄像头，请重试。');
        }
        throw error;
      }
    },
    async initFaceRecognition() {
      try {
        this.showLoading(); // 显示加载指示器
        await this.loadModels();
        await this.initVideo();
        await this.hideLoading();
        await this.startAutoCapture();
      } catch (error) {
        console.error('初始化人脸识别时发生错误:', error);
        ElMessage.error('初始化人脸识别时发生错误，请检查摄像头权限。');
      } 
    },
  },
  beforeUnmount() {
    this.stopRecognition();
    if (this.loadingInstance) {
      this.loadingInstance.close();
    }
    window.removeEventListener('resize', this.updateCanvasSize);
  },
};
</script>

<style scoped>
.login-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgb(0, 0, 0); 
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000; 
}

.login-modal {
  background-color: rgb(0, 0, 0); 
  padding: 100% 100%;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 80%;
  max-width: 500px;
  position: relative;
}

.video-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background: rgb(0, 0, 0);
}

.video-wrapper {
  position: relative;
  width: 480px;
  height: 480px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  border: 5px solid #840ae7;
  background: rgb(0, 0, 0);
  margin-top: -50px;

  /* 添加适当的响应式调整 */
  @media (max-width: 600px) {
    width: 300px;
    height: 300px;
    border-width: 3px;
    margin-top: -30px;
  }
}

video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

canvas {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.button-container {
  display: flex;
  flex-direction: column;
  align-items: center; /* 水平居中 */
  margin-top: 20px;
}

.attention {
  text-align: center;
  color: rgb(255, 255, 255);
}

.attention-text {
  font-size: 18px;
  font-family: '仿宋', 'STXihei', cursive;
  margin-bottom: 5px;
}
</style>