<template>
    <div class="center-container">
        <el-upload
    class="upload-demo"
    :action="`${path}/fileUploadAndDownload/upload`"
    :headers="{ 'x-token': userStore.token }"
    :on-success="uploadSuccess"
    :before-upload="checkFile"
    :on-error="uploadError"
    drag
    :multiple="false"
  >
  <div class="center-content">
    <el-icon class="el-icon--upload"><upload-filled /></el-icon>
    <div class="el-upload__text">
      拖动文件到此处 或 <em>点击此处</em>
    </div>
  </div>
    <template #tip>
      <div class="el-upload__tip" style="text-align: center">
        jpg/png files with a size less than 500kb
      </div>
    </template>
  </el-upload>
    </div>
  </template>
  



  <script setup>
  /*
  image压缩
  */

  import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/pinia/modules/user'
import { isVideoMime, isImageMime } from '@/utils/image'

defineOptions({
  name: 'UploadCommon',
})

const emit = defineEmits(['on-success'])
const path = ref(import.meta.env.VITE_BASE_API)

const userStore = useUserStore()
const fullscreenLoading = ref(false)

const checkFile = (file) => {
  fullscreenLoading.value = true
  const isLt500K = file.size / 1024 / 1024 < 0.5 // 500K, @todo 应支持在项目中设置
  const isLt5M = file.size / 1024 / 1024 < 5 // 5MB, @todo 应支持项目中设置
  const isVideo = isVideoMime(file.type)
  const isImage = isImageMime(file.type)
  let pass = true
  if (!isVideo && !isImage) {
    ElMessage.error('上传图片只能是 jpg,png,svg,webp 格式, 上传视频只能是 mp4,webm 格式!')
    fullscreenLoading.value = false
    pass = false
  }
  if (!isLt5M && isVideo) {
    ElMessage.error('上传视频大小不能超过 5MB')
    fullscreenLoading.value = false
    pass = false
  }
  if (!isLt500K && isImage) {
    ElMessage.error('未压缩的上传图片大小不能超过 500KB，请使用压缩上传')
    fullscreenLoading.value = false
    pass = false
  }

  console.log('upload file check result: ', pass)

  return pass
}

const uploadSuccess = (res) => {
  const { data } = res
  if (data.file) {
    emit('on-success', data.file.url)
  }
}

const uploadError = () => {
  ElMessage({
    type: 'error',
    message: '上传失败'
  })
  fullscreenLoading.value = false
}
  </script>
  
  <style scoped>
  .name {
    cursor: pointer;
  }

  </style>
  
  
<style scoped>
.el-icon-upload:before {
    content: '\e7c3'   
}
.el-icon-upload {
    font-size: 50px;      
    margin: 4px 0 2px;   
}
:deep .el-upload .el-upload-dragger {
    height: 500px;
    width: 1000px;
}
</style>

<style scoped>
.center-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* Adjust the height as needed */
}
.center-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}
</style>