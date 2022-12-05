<!-- 审批文件提交页面 -->
<template>
  <div class="app-container scroll-y">
      <el-upload ref="uploadRef"
          action="" :auto-upload="false" :on-change="(file)=>changeHandler(file)" :limit="1">
          <template #trigger>
              <el-button type="primary" size="large">选取文件</el-button>
          </template>
          <!-- 上传到服务器 -->
          <el-button class="ml-3" type="success" size="large" @click="submitUpload">
              提交审批
          </el-button>
          <template #tip>
              <!-- front-size 设置字体大小 margin-top设置顶部占位 -->
              <div style="margin-top:10px; font-size: large;">请上传资质文件</div>
              <div class="el-upload__tip">文件不能超过5MB</div>
          </template>
      </el-upload>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { UploadInstance, valueEquals } from 'element-plus'
import { ElMessage } from 'element-plus'
import { useEntAccessStore } from '@/store/entAccess'


const uploadRef = ref<UploadInstance>()

let fileParam

const submitUpload = () => {
  let formData = new FormData()
  formData.append("file", fileParam.raw)
  const entAccessStore = useEntAccessStore()
  entAccessStore.apply(formData)
  .then(() => {
      console.log("apply success")
      ElMessage({ message: '提交成功，等待审查机构进行审查。', type: 'success' })
    })
    .catch((res) => {
      console.log("apply error")
    })
}

const changeHandler = (file) => {
    fileParam = file
}
</script>
