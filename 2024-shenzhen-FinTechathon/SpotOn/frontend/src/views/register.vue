<template>
  <div class="register-container">
    <div class="register-box">
      <!-- 注册组件 -->
      <ID_register @id-info="handleIDInfo" @face-photo="handleFacePhoto" />
      <div v-loading="loading" element-loading-text="Loading...">
        <login v-if="showLogin" @close="handleCloseLogin" :id-number="idNumber" :id-photo="idPhoto"
          :face-photo="facePhoto" @face-info="handleFaceInfo" />
      </div>
    </div>
  </div>
</template>

<script>
import ID_register from '@/components/ID_register.vue';
import login from '@/components/login.vue';
import { ElMessage, ElButton, ElIcon } from 'element-plus';
import { Pedersen } from '@/utils/Pedersen';
import CryptoJS from 'crypto-js';
import axios from 'axios';

export default {
  name: 'Register',
  components: {
    ID_register,
    login,
    ElButton,
    ElIcon
  },
  data() {
    return {
      idNumber: null,
      idPhoto: null,
      faceInfo: null,
      facePhoto: null,
      showLogin: false, // 控制 login.vue 的显示
      commitments: null,
      hashIdNumber: null,
      additionalString: 'face_register',
      loading: false, // 添加加载状态
    };
  },
  watch: {
    faceInfo(newVal) {
      this.checkAndRegister();
    }
  },
  methods: {
    handleIDInfo(idInfo) {
      this.idNumber = idInfo.idNumber;
      this.showLogin = true;
    },
    handleFacePhoto(facePhoto) {
      this.facePhoto = facePhoto;
    },
    handleFaceInfo(faceInfo) {
      this.faceInfo = faceInfo;
    },
    handleCloseLogin() {
      this.showLogin = false;
      this.faceInfo = null;
    },
    checkAndRegister() {
      if (this.facePhoto && this.faceInfo) {
        this.register();
      } else {
        ElMessage.error('人脸数据无效，请重新注册。');
      }
    },
    calculateEuclideanDistance(descriptor1, descriptor2) {
      let sum = 0;
      for (let i = 0; i < descriptor1.length; i++) {
        sum += Math.pow(descriptor1[i] - descriptor2[i], 2);
      }
      return Math.sqrt(sum);
    },
    generateCommitmentsFromFaceInfo(faceinfo) {
      if (!faceinfo || !(faceinfo instanceof Float32Array) || faceinfo.length !== 128) {
        throw new Error("Invalid faceinfo: must be a non-null Float32Array with 128 elements.");
      }

      const g = 56544564n; // 大素数
      const h = 237684576n; // 大素数
      const pedersen = new Pedersen(g, h);

      // 将Float32Array转换为普通数组并调整数据范围
      const adjustedFeatures = Array.from(faceinfo).map(value => {
        // 将范围从[-1, 1]调整到[0, 2]
        const adjustedValue = (value + 1) * 1000;
        // 取整并转换为BigInt
        return BigInt(Math.round(adjustedValue));
      });

      // 为每个特征生成随机数，
      const randomNumbers = adjustedFeatures.map(() => BigInt(Math.floor(Math.random() * 1000)));

      // 计算每个特征的承诺
      const commitments = adjustedFeatures.map((feature, index) => {
        return pedersen.Commitment(feature, randomNumbers[index]);
      });

      // 返回承诺、调整后的特征（bigint）和随机数
      return {
        commitments: commitments,
        adjustedFeatures: adjustedFeatures,
        randomNumbers: randomNumbers
      };
    },
    hash_Id_Number() {
      // 将身份证号与附加字符串连接
      const combinedString = this.idNumber + this.additionalString;

      // 使用 SHA256 进行哈希加密
      const hash = CryptoJS.SHA256(combinedString).toString();
      console.log('身份证号码哈希值:', hash);

      return hash;
    },
    
    async register() {
      console.log('身份证号码:', this.idNumber);
      console.log('证件人脸信息:', this.facePhoto);
      console.log('扫描人脸信息:', this.faceInfo);
      this.loading = true; // 开始加载
      try {
        const distance = this.calculateEuclideanDistance(this.facePhoto, this.faceInfo);
        const similarity = (1 / (1 + distance)) * 100;
        console.log('人脸相似度:', similarity);
        if (similarity < 70) {
          ElMessage.error('请正视摄像头，请重新注册。');
          return;
        } else {
          const { commitments, adjustedFeatures, randomNumbers } = this.generateCommitmentsFromFaceInfo(this.faceInfo);
          console.log('承诺:', commitments);
          console.log('调整后的特征:', adjustedFeatures);
          console.log('随机数:', randomNumbers);
          this.hashIdNumber = this.hash_Id_Number();
          const originalFeatures = Array.from(this.faceInfo).map(value => value.toString());//原始人脸信息
          const registerData = {
            id: this.hashIdNumber,
            oldfaceinfo: originalFeatures,
            randomnumbers: randomNumbers,
            commitment: commitments
          };
          console.log('注册数据:', registerData);
          const serializedData = JSON.parse(JSON.stringify(registerData, (key, value) =>
            typeof value === 'bigint' ? value.toString() : value
          ));
          console.log('序列化数据:', serializedData);
          axios.post('http://127.0.0.1:8088/register', serializedData).then(response => {
            this.loading = false; // 请求完成，停止加载
            if (response.data.success) {
              ElMessage.success('注册成功');
              this.$router.push({ name: 'rigister_app_choose' });
            } else {
              ElMessage.error(response.data.message || '注册失败，请重试');
            }
          });
        }
      } catch (error) {
        this.loading = false; // 请求出错，停止加载
        console.error('注册过程中发生错误，请重试:', error);
      } finally {
        this.idNumber = null;
        this.facePhoto = null;
        this.showLogin = false;
        this.commitments = null;
        this.hashIdNumber = null;
      }
    },
  },
};
</script>

<style scoped>
/* 背景样式 */
.register-container {
  position: fixed; /* 固定位置，覆盖整个视口 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  
  display: flex;
  justify-content: center;
  align-items: center;

  background-image: url('@/assets/background.jpg'); /* 使用与 Login.vue 相同的背景图片 */
  background-size: cover; /* 图片覆盖整个背景 */
  background-position: center center; /* 居中对齐 */
  background-repeat: no-repeat; /* 防止图片重复 */
  background-attachment: fixed; /* 固定背景图片 */
}

/* 注册框样式 */
.register-box {
  background-color: rgba(0, 0, 0, 0.5); /* 半透明背景 */
  padding: 50px 20px;  /* 内边距 */
  border-radius: 20px;  /* 圆角效果 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* 柔和阴影 */
  text-align: center;
  width: 400px; /* 固定宽度，可根据需要调整 */
  border: 1px solid #9079e6; /* 边框 */
}

/* 返回按钮的样式 */
.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 0;
  background: none;
  border: transparent;
  z-index: 50;
  font-size: 24px;
}

.close-button i {
  font-size: 40px;
  color: red;
  margin-top: 10px;
  margin-right: 10px;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .register-box {
    padding: 30px 20px;
    border-radius: 8px;
  }

  .register-box h2 {
    font-size: 22px;
  }
}
</style>
