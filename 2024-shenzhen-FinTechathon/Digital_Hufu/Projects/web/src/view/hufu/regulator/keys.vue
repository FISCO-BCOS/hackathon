<template>
    <div class="wallet-keys-container">
      <el-card class="apply-form">
        <template #header>
          <div class="card-header">
            <span>申请密钥</span>
          </div>
        </template>
  
        <el-form 
          :model="formData"
          :rules="rules"
          ref="formRef"
          label-width="120px"
        >
          <el-form-item label="钱包ID" prop="wallet_id">
            <el-input 
              v-model="formData.wallet_id" 
              placeholder="请输入钱包ID"
            />
          </el-form-item>
          
          <el-form-item label="证明材料" prop="evidence">
            <el-upload
              class="evidence-uploader"
              action="#"
              :auto-upload="false"
              :on-change="handleFileChange"
              :limit="1"
              accept=".txt"
            >
              <el-button type="primary">选择文件</el-button>
            </el-upload>
          </el-form-item>
  
          <el-form-item>
            <el-button 
              type="primary" 
              @click="handleSubmit" 
              :loading="loading"
            >
              {{ loading ? '提交中...' : '提交申请' }}
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
  
      <el-card class="progress-table">
        <template #header>
          <div class="card-header">
            <span>申请记录</span>
            <el-button type="primary" link @click="fetchApplications">刷新</el-button>
          </div>
        </template>
  
        <el-table :data="applications" style="width: 100%">
          <el-table-column prop="wallet_id" label="钱包ID" width="180" />
          <el-table-column prop="evidence" label="证明材料" show-overflow-tooltip>
            <template #default="{ row }">
              <el-link 
                type="primary" 
                :underline="false"
                v-if="row.evidence"
                @click="downloadEvidence(row.evidence)"
              >
                <el-icon class="el-icon--left"><Document /></el-icon>
                查看内容
              </el-link>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="120">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="created_at" label="申请时间" width="180" />
        </el-table>
  
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            @current-change="handlePageChange"
            layout="total, prev, pager, next"
          />
        </div>
      </el-card>
  
      <el-dialog
        v-model="dialogVisible"
        title="申请成功"
        width="60%"
        class="key-dialog"
      >
        <div class="key-info">
          <div class="key-item">
            <div class="key-label">私钥：</div>
            <div class="key-content">
              <el-input
                type="textarea"
                v-model="keyData.data"
                readonly
                :rows="2"
                resize="none"
              />
            </div>
          </div>
          
          <div class="key-item">
            <div class="key-label">密钥分片：</div>
            <div class="shares-list">
              <el-collapse>
                <el-collapse-item 
                  v-for="(share, index) in keyData.shares" 
                  :key="index"
                  :title="`分片 ${index + 1}`"
                >
                  <div class="share-content">
                    <el-input
                      type="textarea"
                      v-model="keyData.shares[index]"
                      readonly
                      :rows="2"
                      resize="none"
                    />
                  </div>
                </el-collapse-item>
              </el-collapse>
            </div>
          </div>
        </div>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import axios from 'axios'
  import { Document } from '@element-plus/icons-vue'
  
  const formRef = ref(null)
  const formData = reactive({
    wallet_id: '',
    evidence: null
  })
  
  const rules = {
    wallet_id: [
      { required: true, message: '请输入钱包ID', trigger: 'blur' }
    ],
    evidence: [
      { required: true, message: '请上传证明材料', trigger: 'change' }
    ]
  }
  
  const applications = ref([])
  const currentPage = ref(1)
  const pageSize = ref(10)
  const total = ref(0)
  
  const dialogVisible = ref(false)
  const keyData = reactive({
    data: '',
    shares: []
  })
  
  const loading = ref(false)
  
  const getStatusType = (status) => {
    const statusMap = {
      pending: 'warning',
      approved: 'success',
      rejected: 'danger'
    }
    return statusMap[status] || 'info'
  }
  
  const getStatusText = (status) => {
    const statusMap = {
      pending: '审核中',
      approved: '已通过',
      rejected: '已拒绝'
    }
    return statusMap[status] || '未知状态'
  }
  
  const fetchApplications = async () => {
    try {
      const response = await axios.post('http://45.8.113.140:3338/api/v1/regulator/application')
      if (response.data.code === 0) {
        applications.value = response.data.data.map(item => ({
          wallet_id: item.wallet_id,
          evidence: item.content,
          status: getApplicationStatus(item.status),
          created_at: formatTimestamp(item.timestamp)
        }))
      } else {
        throw new Error(response.data.msg)
      }
    } catch (error) {
      console.error('Error fetching applications:', error)
      ElMessage.error('获取申请记录失败：' + (error.response?.data?.msg || error.message))
    }
  }
  
  const getApplicationStatus = (status) => {
    if (!status) return 'pending'
    return 'approved'
  }
  
  const formatTimestamp = (timestamp) => {
    if (!timestamp) return '-'
    try {
      const year = timestamp.substring(0, 4)
      const month = timestamp.substring(4, 6)
      const day = timestamp.substring(6, 8)
      const hour = timestamp.substring(8, 10)
      const minute = timestamp.substring(10, 12)
      const second = timestamp.substring(12, 14)
      
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    } catch (error) {
      console.error('Error parsing timestamp:', error)
      return timestamp
    }
  }
  
  const handlePageChange = (page) => {
    currentPage.value = page
    fetchApplications()
  }
  
  const handleFileChange = (file) => {
    if (file.raw.type !== 'text/plain') {
      ElMessage.error('请上传txt格式的文件')
      return false
    }
    formData.evidence = file.raw
  }
  
  const handleSubmit = async () => {
    if (!formRef.value) return
    
    loading.value = true
    
    try {
      await formRef.value.validate()
      
      const formDataToSend = new FormData()
      formDataToSend.append('wallet_id', formData.wallet_id.toString())
      if (formData.evidence && formData.evidence instanceof File) {
        formDataToSend.append('evidence', formData.evidence)
      } else {
        throw new Error('请选择有���证明文件')
      }

      const response = await axios.post('http://45.8.113.140:3338/api/v1/regulator/private-key', formDataToSend, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
  
      if (response.data.message === 'success') {
        keyData.data = response.data.data
        keyData.shares = response.data.shares
        dialogVisible.value = true
        ElMessage.success('申请成功')
        formRef.value.resetFields()
        fetchApplications()
      } else {
        ElMessage.error('申请失败：' + response.data.message)
      }
    } catch (error) {
      console.error('Error:', error)
      ElMessage.error('申请失败：' + (error.response?.data?.error || error.message))
    } finally {
      loading.value = false
    }
  }
  
  const downloadEvidence = (content) => {
    // 下载文件
    const blob = new Blob([content], { type: 'text/plain' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '证明材料.txt'
    link.click()
    window.URL.revokeObjectURL(url)
  }
  
  onMounted(() => {
    fetchApplications()
  })
  </script>
  
  <style scoped>
  .wallet-keys-container {
    padding: 20px;
    background-color: #f5f7fa;
    height: 100%;
  }
  
  .apply-form, .progress-table {
    margin-bottom: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 20px;
    font-weight: bold;
    color: #333;
  }
  
  .el-button {
    transition: background-color 0.3s;
  }
  
  .el-button:hover {
    background-color: #409eff;
  }
  
  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
  
  .el-form-item {
    margin-bottom: 20px;
  }
  
  .el-input {
    border-radius: 4px;
    transition: border-color 0.3s;
  }
  
  .el-input:hover {
    border-color: #409eff;
  }
  
  .key-dialog :deep(.el-dialog__body) {
    padding: 20px;
  }
  
  .key-info {
    padding: 10px;
  }
  
  .key-item {
    margin-bottom: 24px;
  }
  
  .key-label {
    font-weight: bold;
    margin-bottom: 12px;
    font-size: 16px;
    color: #333;
  }
  
  .key-content,
  .share-content {
    display: flex;
    gap: 12px;
    align-items: flex-start;
  }
  
  .key-content .el-input,
  .share-content .el-input {
    flex: 1;
  }
  
  .shares-list {
    margin-top: 12px;
  }
  
  .shares-list :deep(.el-collapse) {
    border: none;
  }
  
  .shares-list :deep(.el-collapse-item__header) {
    font-size: 14px;
    color: #606266;
    background-color: #f5f7fa;
    padding: 0 12px;
    border-radius: 4px;
  }
  
  .shares-list :deep(.el-collapse-item__content) {
    padding: 12px;
  }
  
  .el-button {
    white-space: nowrap;
  }
  
  :deep(.el-textarea__inner) {
    font-family: monospace;
    font-size: 14px;
    line-height: 1.5;
    background-color: #f8f9fa;
  }
  
  .evidence-uploader {
    width: 100%;
  }
  
  .el-upload__tip {
    color: #909399;
    font-size: 12px;
    margin-top: 8px;
  }
  
  :deep(.evidence-dialog) {
    width: 80% !important;
    
    .el-message-box__content {
      white-space: pre-wrap;
      font-family: monospace;
      max-height: 600px;
      overflow-y: auto;
      padding: 20px;
      font-size: 14px;
      line-height: 1.6;
      background-color: #f8f9fa;
      border-radius: 4px;
      margin: 10px 0;
    }

    .el-message-box__container {
      padding: 20px;
    }

    .el-message-box__header {
      padding: 20px;
    }
  }
  </style>
  