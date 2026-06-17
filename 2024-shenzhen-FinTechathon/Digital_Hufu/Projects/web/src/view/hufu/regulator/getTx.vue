<template>
  <div class="transaction-page">
    <el-row>
      <el-col :span="24">
        <el-card class="transaction-form">
          <template #header>
            <div class="card-header">
              <span>获取加密交易记录</span>
              <el-icon class="header-icon"><Search /></el-icon>
            </div>
          </template>

          <el-form :model="formData" ref="formRef" label-width="120px">
            <el-form-item label="钱包ID" prop="wallet_id">
              <el-input 
                v-model="formData.wallet_id" 
                placeholder="请输入钱包ID"
                :prefix-icon="Wallet"
              />
            </el-form-item>
            
            <el-form-item label="私钥" prop="private_key">
              <el-input 
                v-model="formData.private_key"
                placeholder="请输入私钥"
                :prefix-icon="Lock"
                type="password"
                show-password
              />
            </el-form-item>

            <el-form-item>
              <el-button 
                type="primary" 
                @click="fetchTransactions"
                :icon="Search"
                class="submit-button"
              >
                获取交易
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <el-card v-if="transactions.length" class="transaction-list">
          <template #header>
            <div class="card-header">
              <span>加密交易记录</span>
              <div>
                <el-button 
                  type="primary"
                  @click="decryptAllData"
                  :loading="decrypting"
                >
                  解密数据
                </el-button>
                <el-tag type="info" class="record-count" style="margin-left: 10px">
                  共 {{ transactions.length }} 条记录
                </el-tag>
              </div>
            </div>
          </template>

          <!-- 加密数据表格 -->
          <el-table 
            v-if="!showDecrypted"
            :data="transactions" 
            style="width: 100%"
            :stripe="true"
            :border="true"
          >
            <el-table-column prop="transaction_id" label="交易ID" width="100" />
            <el-table-column prop="encrypted_from_wallet_id" label="加密发送方钱包ID" min-width="200">
              <template #default="scope">
                {{ scope.row.encrypted_from_wallet_id?.substring(0, 100) + '...' }}
              </template>
            </el-table-column>
            <el-table-column prop="encrypted_to_wallet_id" label="加密接收方钱包ID" min-width="200">
              <template #default="scope">
                {{ scope.row.encrypted_to_wallet_id?.substring(0, 100) + '...' }}
              </template>
            </el-table-column>
            <el-table-column prop="encrypted_amount" label="加密金额" min-width="200">
              <template #default="scope">
                {{ scope.row.encrypted_amount?.substring(0, 100) + '...' }}
              </template>
            </el-table-column>
            <el-table-column prop="CreatedAt" label="创建时间" width="180">
              <template #default="scope">
                {{ formatTime(scope.row.CreatedAt) }}
              </template>
            </el-table-column>
          </el-table>

          <!-- 解密后的数据表格 -->
          <el-table 
            v-else
            :data="decryptedTransactions" 
            style="width: 100%"
            :stripe="true"
            :border="true"
          >
            <el-table-column prop="transaction_id" label="交易ID" width="100" />
            <el-table-column prop="from_wallet_id" label="发送方钱包ID" width="150" />
            <el-table-column prop="to_wallet_id" label="接收方钱包ID" width="150" />
            <el-table-column prop="amount" label="金额" width="150">
              <template #default="scope">
                {{ formatAmount(scope.row.amount) }}
              </template>
            </el-table-column>
            <el-table-column prop="CreatedAt" label="创建时间" width="180">
              <template #default="scope">
                {{ formatTime(scope.row.CreatedAt) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
  
  <script setup>
  import { ref, reactive } from 'vue'
  import axios from 'axios'
  import { ElMessage } from 'element-plus'
  import dayjs from 'dayjs'
  import { Search, Wallet, Lock } from '@element-plus/icons-vue'
  
  const formRef = ref(null)
  const formData = reactive({
    wallet_id: '',
    private_key: ''
  })
  
  const transactions = ref([])
  const decrypting = ref(false)
  const showDecrypted = ref(false)
  const decryptedTransactions = ref([])
  
  const fetchTransactions = async () => {
    if (!formData.wallet_id || !formData.private_key) {
      ElMessage.warning('请填写完整信息')
      return
    }
  
    try {
      const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/tee/encrypted-history', {
        id: formData.wallet_id,
        private_key: formData.private_key
      })

      // 检查是否有错误返回
      if (response.data.error) {
        ElMessage.error(response.data.error)
        return
      }

      console.log('response.data')
      console.log(response.data)

      transactions.value = response.data || []

      if (transactions.value.length === 0) {
        ElMessage.info('暂无交易记录')
      } else {
        ElMessage.success(`成功获取 ${transactions.value.length} 条交易记录`)
      }


    } catch (error) {
      console.error(error)
      if (error.response) {
        switch (error.response.status) {
          case 400:
            ElMessage.error('请求格式错误：' + error.response.data.error)
            break
          case 401:
            ElMessage.error('私钥验证失败')
            break
          case 404:
            ElMessage.error(error.response.data.error)
            break
          default:
            ElMessage.error('获取交易失败：' + error.response.data.error)
        }
      } else {
        ElMessage.error('获取交易失败，请检查网络连接')
      }
    }
  }
  
  const formatAmount = (amount) => {
    if (!amount) return '0.00'
    return `${Number(amount).toFixed(2)}`
  }
  
  const formatTime = (timestamp) => {
    if (!timestamp) return '-'
    return dayjs(timestamp).format('YYYY-MM-DD HH:mm:ss')
  }
  
  const decryptAllData = async () => {
    if (decrypting.value) return
    decrypting.value = true
    
    try {
      const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/tee/decrypt-history', {
        id: formData.wallet_id,
        private_key: formData.private_key
      })

      if (response.data.error) {
        ElMessage.error(response.data.error)
        return
      }

      // 将解密后的数据与原始加密数据合并
      decryptedTransactions.value = transactions.value.map((tx, index) => ({
        ...tx,
        from_wallet_id: response.data[index]?.from,
        to_wallet_id: response.data[index]?.to,
        amount: response.data[index]?.amount,
        decrypted_create_at: response.data[index]?.create_at
      }))

      showDecrypted.value = true
      ElMessage.success(`成功解密 ${response.data.length} 条记录`)

    } catch (error) {
      console.error(error)
      ElMessage.error('解密失败：' + (error.response?.data?.error || '请检查网络连接'))
    } finally {
      decrypting.value = false
    }
  }
  </script>
  
  <style scoped>
  .transaction-page {
    padding: 24px;
    min-height: 100vh;
    background-color: #f5f7fa;
  }
  
  .transaction-form, .transaction-list {
    height: 100%;
    border-radius: 12px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 0;
  }
  
  .card-header span {
    font-size: 18px;
    font-weight: 600;
    color: #2c3e50;
  }
  
  .header-icon {
    font-size: 20px;
    color: #409EFF;
  }
  
  .submit-button {
    width: 100%;
    padding: 12px;
    font-size: 16px;
    font-weight: 500;
    letter-spacing: 1px;
    border-radius: 8px;
  }
  
  .record-count {
    font-size: 14px;
    padding: 6px 12px;
    border-radius: 16px;
  }
  
  :deep(.el-input__wrapper) {
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  }
  
  :deep(.el-input__wrapper:hover) {
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }
  
  :deep(.el-table) {
    border-radius: 8px;
    overflow: hidden;
  }
  
  :deep(.el-table th) {
    background-color: #f5f7fa;
    font-weight: 600;
    color: #2c3e50;
  }
  
  :deep(.el-table--striped .el-table__row--striped td) {
    background-color: #fafafa;
  }
  
  :deep(.el-tag) {
    padding: 4px 12px;
    height: 28px;
    line-height: 20px;
    border-radius: 14px;
    font-weight: 500;
  }
  
  :deep(.el-tag--success) {
    background-color: #f0f9eb;
    border-color: #e1f3d8;
    color: #67c23a;
  }
  
  :deep(.el-tag--danger) {
    background-color: #fef0f0;
    border-color: #fde2e2;
    color: #f56c6c;
  }
  
  :deep(.el-tag--info) {
    background-color: #f4f4f5;
    border-color: #e9e9eb;
    color: #909399;
  }
  
  .decrypted-data {
    margin-top: 8px;
    padding: 4px 8px;
    background-color: #f0f9eb;
    border-radius: 4px;
    color: #67c23a;
    font-size: 13px;
  }
  
  @media (max-width: 768px) {
    .el-row {
      margin: 0 !important;
    }
    
    .el-col {
      padding: 0 !important;
    }
    
    .transaction-form, .transaction-list {
      margin-bottom: 20px;
    }
  }
  </style>
  