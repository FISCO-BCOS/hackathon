<template>
  <div class="register-container">
    <div class="register-box">
      <!-- 注册组件 -->
      <ID_register @id-info="handleIDInfo" @face-photo="handleFacePhoto" />
      <login v-if="showLogin" @close="handleCloseLogin" :id-number="idNumber" :id-photo="idPhoto"
        :face-photo="facePhoto" @face-info="handleFaceInfo" />

      <!-- 返回按钮 -->
      <el-button @click="goBack" class="close-button">
        <h3 style="color: brown; font-size: 30px;">X</h3>
      </el-button>
    </div>
  </div>
</template>

<script>
import ID_register from '@/components/ID_register.vue';
import login from '@/components/login.vue';
import { ElMessage, ElButton, ElIcon } from 'element-plus';
import { Pedersen } from '@/utils/Pedersen';
import CryptoJS from 'crypto-js';


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
    };
  },
  watch: {
    faceInfo(newVal) {
      this.checkAndRegister();
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1); // 返回上一页
    },
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

      const g = 56544564n; // 示例大素数
      const h = 237684576n; // 另一个示例大素数
      const pedersen = new Pedersen(g, h);

      // 将Float32Array转换为普通数组并调整数据范围
      const adjustedFeatures = Array.from(faceinfo).map(value => {
        // 将范围从[-1, 1]调整到[0, 2]
        const adjustedValue = (value + 1) * 1000;
        // 取整并转换为BigInt
        return BigInt(Math.round(adjustedValue));
      });

      // 为每个特征生成随机数
      const randomNumbers = adjustedFeatures.map(() => BigInt(Math.floor(Math.random() * 1000000)));

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

      return hash;
    },
    
    async register() {
      console.log('身份证号码:', this.idNumber);
      console.log('证件人脸信息:', this.facePhoto);
      console.log('扫描人脸信息:', this.faceInfo);
      try {
        const distance = this.calculateEuclideanDistance(this.facePhoto, this.faceInfo);
        const similarity = (1 / (1 + distance)) * 100;
        console.log('人脸相似度:', similarity);
        if (similarity < 70) {
          ElMessage.error('请正视摄像头，请重新注册。');
          return;
        } else {
          ElMessage.success('注册成功！');
          //将faceInfo进行承诺
          const { commitments, adjustedFeatures, randomNumbers } = this.generateCommitmentsFromFaceInfo(this.faceInfo);
          console.log('承诺:', commitments);
          console.log('调整后的特征:', adjustedFeatures);
          console.log('随机数:', randomNumbers);
          //将身份证号进行哈希
          this.hashIdNumber = this.hash_Id_Number();
          const registerData = {
            id: this.hashIdNumber,
            m: adjustedFeatures,
            r: randomNumbers,
            cmt: commitments
          };
        }
      } catch (error) {
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
.register-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #ffffff;
  position: relative;
}

.register-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 30px;
  background-color: #ffffff;
  border: none;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
  box-sizing: border-box;
  position: relative;
}

.register-box h2 {
  margin-bottom: 25px;
  font-size: 26px;
  color: #333333;
  text-align: center;
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
