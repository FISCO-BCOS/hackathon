<template>
    <div class="encrypted-transfer-container">
        <el-card class="transfer-card">
            <template #header>
                <div class="card-header">
                    <span>加密代理转账</span>
                    <el-tag type="success">安全加密</el-tag>
                </div>
            </template>

            <el-form :model="transferForm" :rules="transferRules" ref="transferFormRef" label-width="120px">
                <!-- 转账信息部分 -->
                <div class="transfer-section">
                    <h3 class="section-title">
                        <el-icon><Money /></el-icon>
                        转账信息
                    </h3>
                    <el-form-item label="付款方钱包ID">
                        <el-input v-model="walletInfo.ID" disabled />
                    </el-form-item>
                    
                    <!-- 新增：交易代理选择 -->
                    <el-form-item label="交易代理" prop="proxyWalletId">
                        <el-select v-model="transferForm.proxyWalletId" placeholder="选择交易代理" 
                            :disabled="isEncrypted" style="width: 100%;">
                            <el-option v-for="proxy in proxyWallets" 
                                :key="proxy.id" 
                                :label="`代理钱包 ${proxy.id}`" 
                                :value="proxy.id">
                                <div class="proxy-option">
                                    <el-icon><Connection /></el-icon>
                                    <span>代理钱包 {{ proxy.id }}</span>
                                </div>
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="收款方钱包ID" prop="toWalletId">
                        <el-input v-model.number="transferForm.toWalletId" type="number" :prefix-icon="Wallet" 
                            :disabled="isEncrypted" @change="handleToWalletIdChange">
                        </el-input>
                    </el-form-item>

                    <!-- 新增收款方用户名表单项 -->
                    <el-form-item label="收款方用户名">
                        <el-input v-model="receiverInfo.userName" disabled>
                            <template #prepend v-if="loadingReceiverInfo">
                                <el-icon class="is-loading"><Loading /></el-icon>
                            </template>
                        </el-input>
                    </el-form-item>

                    <el-form-item label="转账金额" prop="amount">
                        <el-input-number v-model="transferForm.amount" :precision="2" :step="0.1" :min="0.01"
                            controls-position="right" style="width: 100%;" :disabled="isEncrypted"/>
                    </el-form-item>

                    <!-- 新增：转账备注 -->
                    <el-form-item label="转账备注" prop="memo">
                        <el-input
                            v-model="transferForm.memo"
                            type="textarea"
                            :rows="2"
                            placeholder="请输入转账备注信息"
                            :disabled="isEncrypted"
                        />
                    </el-form-item>
                </div>

                <!-- 加密部分 -->
                <div class="encryption-section">
                    <h3 class="section-title">
                        <el-icon><Lock /></el-icon>
                        数据加密
                    </h3>
                    <el-form-item label="加密公钥" prop="publicKey">
                        <div class="key-select-wrapper">
                            <el-select v-model="transferForm.keyId" placeholder="选择加密公钥" :disabled="isEncrypted"
                                style="width: 100%;">
                                <el-option v-for="key in encryptionKeys" :key="key.id" :label="key.name" :value="key.id">
                                    <div class="key-option">
                                        <el-icon><Key /></el-icon>
                                        <span>{{ key.name }}</span>
                                    </div>
                                </el-option>
                            </el-select>
                            <div class="encryption-controls">
                                <el-button type="primary" @click="startEncryption" :disabled="!canEncrypt" style="margin-top: 10px;">
                                    <el-icon><Lock /></el-icon>
                                    加密数据
                                </el-button>
                            </div>
                        </div>
                    </el-form-item>

                    <!-- 加密动画和数据展示 -->
                    <transition name="encrypt-fade">
                        <div v-if="showEncryptionAnimation" class="encryption-animation">
                            <el-icon class="encrypting-icon"><Lock /></el-icon>
                            <span>正在加密...</span>
                        </div>
                    </transition>

                    <transition name="encrypt-fade">
                        <div v-if="isEncrypted" class="encrypted-data-panel">
                            <div class="panel-header">
                                <el-icon><Lock /></el-icon>
                                <span>加密后的转账数据</span>
                            </div>
                            
                            <!-- 添加数据预览部分 -->
                            <div class="encrypted-data-preview">
                                <el-descriptions :column="1" border>
                                    <el-descriptions-item label="付款方">
                                        <el-tag size="small">{{ formatEncryptedData.from }}</el-tag>
                                    </el-descriptions-item>
                                    <el-descriptions-item label="收款方">
                                        <el-tag size="small" type="success">{{ formatEncryptedData.to }}</el-tag>
                                    </el-descriptions-item>
                                    <el-descriptions-item label="转账金额">
                                        <el-tag size="small" type="warning">{{ formatEncryptedData.amount }}</el-tag>
                                    </el-descriptions-item>
                                </el-descriptions>
                            </div>
                            
                            <!-- 原始加密数据 -->
                            <div class="raw-encrypted-data">
                                <div class="raw-data-header">
                                    <span>原始加密数据</span>
                                    <el-button type="primary" link size="small" @click="copyEncryptedData">
                                        <el-icon><Document /></el-icon>复制
                                    </el-button>
                                </div>
                                <el-input type="textarea" v-model="transferForm.encryptedData" readonly rows="3" />
                            </div>
                        </div>
                    </transition>
                </div>

                <!-- 转账按钮 -->
                <div class="transfer-actions">
                    <el-button type="primary" @click="handleTransfer" :loading="transferLoading" :disabled="!isEncrypted">
                        <el-icon><Check /></el-icon>
                        确认转账
                    </el-button>
                    <el-button @click="resetForm" :disabled="transferLoading">
                        重置
                    </el-button>
                </div>
            </el-form>
        </el-card>
    </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Money, Lock, Key, Check, Wallet, Connection, Loading, Document } from '@element-plus/icons-vue'
import { useUserStore } from '@/pinia/modules/user'
import axios from 'axios'

export default {
    name: 'EncryptedTransfer',
    components: {
        Money,
        Lock,
        Key,
        Check,
        Wallet,
        Connection,
        Loading,
        Document
    },
    setup() {
        const userStore = useUserStore()
        const transferFormRef = ref(null)
        const transferLoading = ref(false)
        const isEncrypted = ref(false)
        const encryptionKeys = ref([])

        const walletInfo = ref({
            ID: 0,
            balance: 0
        })

        // 新增：交易代理列表
        const proxyWallets = ref([
            { id: 1 },
            { id: 2 },
            { id: 3 }
        ])

        const transferForm = reactive({
            proxyWalletId: '',
            toWalletId: '',
            amount: 0,
            memo: '',
            keyId: '',
            encryptedData: ''
        })

        const encryptedDataObj = ref({
            from: '',
            to: '',
            amount: ''
        })

        // 更新验证规则
        const transferRules = {
            proxyWalletId: [
                { required: true, message: '请选择交易代理', trigger: 'change' }
            ],
            toWalletId: [
                { required: true, message: '请输入最终接收方钱包ID', trigger: 'blur' },
                { type: 'number', message: '钱包ID必须为数字', trigger: 'blur' }
            ],
            amount: [
                { required: true, message: '请输入转账金额', trigger: 'blur' },
                { type: 'number', message: '金额必须为字', trigger: 'blur' }
            ],
            memo: [
                { required: true, message: '请输入转账备注', trigger: 'blur' }
            ],
            keyId: [
                { required: true, message: '请选择加密公钥', trigger: 'change' }
            ]
        }

        const canEncrypt = computed(() => {
            return transferForm.toWalletId && 
                   transferForm.amount > 0 && 
                   transferForm.keyId && 
                   !isEncrypted.value
        })

        // 获取钱包信息
        const getWalletInfo = async () => {
            try {
                const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/wallet/', {
                    user_name: userStore.userInfo.userName
                })
                walletInfo.value = response.data.data
            } catch (error) {
                console.error('获取钱包信息失败:', error)
                ElMessage.error('获取钱包信息失败')
            }
        }

        // 获取加密公钥
        const getEncryptionKeys = async () => {
            try {
                const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/keys/', {
                    user_name: userStore.userInfo.userName
                })
                const publicKeyStr = response.data.public_key
                encryptionKeys.value = [{
                    id: 1,
                    name: '加密公钥',
                    key: publicKeyStr
                }]
            } catch (error) {
                console.error('获取加密公钥失败:', error)
                ElMessage.error('获取加密公钥失败')
            }
        }

        // 加密数据
        const handleEncrypt = async () => {
            try {
                const encryptData = {
                    from: walletInfo.value.ID.toString(),
                    to: transferForm.toWalletId.toString(),
                    amount: transferForm.amount.toString()
                }

                const response = await axios.post(
                    'http://45.8.113.140:3338/api/v1/hufu/tee/encrypt-transaction',
                    encryptData,
                    {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }
                )

                // 更新加密数据和显示对象
                transferForm.encryptedData = JSON.stringify({
                    from: response.data.from,
                    to: response.data.to,
                    amount: response.data.amount
                })

                // 更新显示对象
                encryptedDataObj.value = {
                    from: response.data.from,
                    to: response.data.to,
                    amount: response.data.amount
                }
                
                isEncrypted.value = true
                ElMessage.success('数据加密成功')
            } catch (error) {
                console.error('加密失败:', error)
                ElMessage.error(error.response?.data?.message || '加密失败')
            }
        }

        // 执行转账
        const handleTransfer = async () => {
            if (!isEncrypted.value) {
                ElMessage.warning('请先加密转账数据')
                return
            }

            transferLoading.value = true
            try {
                // 确保 encryptedData 存在且可以被解析
                if (!transferForm.encryptedData) {
                    throw new Error('加密数据不存在')
                }

                const encryptedDataObj = JSON.parse(transferForm.encryptedData)
                
                // 验证加密数据的完整性
                if (!encryptedDataObj.from || !encryptedDataObj.to || !encryptedDataObj.amount) {
                    throw new Error('加密数据不完整')
                }

                const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/tx/proxy-transfer', {
                    from: encryptedDataObj.from,
                    to: encryptedDataObj.to,
                    amount: encryptedDataObj.amount
                })

                if (response.data.message === 'success') {
                    ElMessage.success('加密转账成功')
                    resetForm()
                } else {
                    throw new Error(response.data.message || '转账失败')
                }
            } catch (error) {
                console.error('转账失败:', error)
                ElMessage.error(error.message || '转账失败')
            } finally {
                transferLoading.value = false
            }
        }

        // 重置表单
        const resetForm = () => {
            transferForm.proxyWalletId = ''
            transferForm.toWalletId = ''
            transferForm.amount = 0
            transferForm.memo = ''
            transferForm.keyId = ''
            transferForm.encryptedData = ''
            isEncrypted.value = false
            if (transferFormRef.value) {
                transferFormRef.value.resetFields()
            }
        }

        // 添加收款方信息
        const receiverInfo = ref({
            userName: ''
        })

        // 处理收款方钱包ID变化
        const handleToWalletIdChange = async () => {
            if (!transferForm.toWalletId) {
                receiverInfo.value.userName = ''
                return
            }

            loadingReceiverInfo.value = true
            try {
                const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/wallet/', {
                    id: transferForm.toWalletId
                })
                receiverInfo.value.userName = response.data.data.user_name
            } catch (error) {
                console.error('获取收款方信息失败:', error)
                ElMessage.error('获取收款方信息失败')
                receiverInfo.value.userName = ''
            } finally {
                loadingReceiverInfo.value = false
            }
        }

        const loadingReceiverInfo = ref(false)

        const formatEncryptedData = computed(() => {
            try {
                return {
                    from: transferForm.encryptedData.substring(10, 110),
                    to: transferForm.encryptedData.substring(110, 210),
                    amount: transferForm.encryptedData.substring(210, 310)
                }
            } catch (e) {
                return {
                    from: '***',
                    to: '***',
                    amount: '***'
                }
            }
        })

        const truncateString = (str) => {
            if (!str) return '***'
            return str.length > 20 ? `${str.substring(0, 100)}...` : str
        }

        // 复制加密数据
        const copyEncryptedData = () => {
            navigator.clipboard.writeText(transferForm.encryptedData)
                .then(() => {
                    ElMessage.success('复制成功')
                })
                .catch(() => {
                    ElMessage.error('复制失败')
                })
        }

        const showEncryptionAnimation = ref(false)

        // 修改加密处理函数
        const startEncryption = async () => {
            showEncryptionAnimation.value = true
            
            try {
                await new Promise(resolve => setTimeout(resolve, 800)) // 动画展示时间
                await handleEncrypt()
            } finally {
                showEncryptionAnimation.value = false
            }
        }

        onMounted(async () => {
            await getWalletInfo()
            await getEncryptionKeys()
        })

        return {
            walletInfo,
            transferForm,
            transferFormRef,
            transferRules,
            transferLoading,
            encryptionKeys,
            isEncrypted,
            canEncrypt,
            handleEncrypt,
            handleTransfer,
            resetForm,
            proxyWallets,
            receiverInfo,
            handleToWalletIdChange,
            loadingReceiverInfo,
            formatEncryptedData,
            copyEncryptedData,
            truncateString,
            showEncryptionAnimation,
            startEncryption
        }
    }
}
</script>

<style scoped>
.encrypted-transfer-container {
    padding: 20px;
}

.transfer-card {
    /* max-width: 800px; */
    margin: 0 auto;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 20px;
    color: #409EFF;
    font-size: 16px;
}

.transfer-section,
.encryption-section {
    margin-bottom: 30px;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 4px;
}

.key-select-wrapper {
    width: 100%;
}

.key-option {
    display: flex;
    align-items: center;
    gap: 8px;
}

.encrypted-data-panel {
    margin-top: 20px;
    padding: 15px;
    background: #F0F9EB;
    border: 1px solid #E1F3D8;
    border-radius: 4px;
}

.panel-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 12px;
    color: #67C23A;
    font-weight: bold;
}

.transfer-actions {
    display: flex;
    justify-content: center;
    gap: 16px;
    margin-top: 30px;
}

:deep(.el-input-number) {
    width: 100%;
}

:deep(.el-form-item__label) {
    font-weight: bold;
}

.proxy-option {
    display: flex;
    align-items: center;
    gap: 12px;
}

.receiver-info {
    margin-top: 8px;
}

.encrypted-data-preview {
    margin: 15px 0;
}

.raw-encrypted-data {
    margin-top: 15px;
}

.raw-data-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
    color: #606266;
    font-size: 14px;
}

:deep(.el-descriptions) {
    padding: 8px;
    background-color: #fff;
    border-radius: 4px;
}

:deep(.el-descriptions__cell) {
    padding: 8px 12px;
}

.encryption-animation {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px;
    background: #F0F9EB;
    border-radius: 4px;
    margin: 15px 0;
}

.encrypting-icon {
    font-size: 24px;
    margin-right: 10px;
    animation: rotate 1s linear infinite;
}

@keyframes rotate {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
}

.encrypt-fade-enter-active,
.encrypt-fade-leave-active {
    transition: all 0.3s ease;
}

.encrypt-fade-enter-from,
.encrypt-fade-leave-to {
    opacity: 0;
    transform: translateY(-10px);
}
</style> 