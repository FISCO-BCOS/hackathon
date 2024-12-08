<template>
    <div class="wallet-container">
        <!-- 钱包信息卡片 -->
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card class="wallet-card" shadow="hover">
                    <template #header>
                        <div class="card-header">
                            <span>钱包信息</span>
                            <el-button type="primary" link @click="handleEdit">
                                编辑名称
                            </el-button>
                        </div>
                    </template>
                    <div class="wallet-info">
                        <div class="info-item">
                            <span class="label">钱包ID：</span>
                            <span class="value">{{ walletInfo.ID }}</span>
                        </div>
                        <div class="info-item">
                            <span class="label">用户名：</span>
                            <span class="value">{{ walletInfo.user_name }}</span>
                        </div>
                        <div class="info-item">
                            <span class="label">钱包名称：</span>
                            <span class="value">{{ walletInfo.wallet_name }}</span>
                        </div>
                        <div class="info-item">
                            <span class="label">创建时间：</span>
                            <span class="value">{{ formatDate(walletInfo.CreatedAt) }}</span>
                        </div>
                    </div>
                </el-card>
            </el-col>

            <el-col :span="8">
                <el-card class="balance-card" shadow="hover">
                    <template #header>
                        <div class="card-header">
                            <span>账户余额</span>
                        </div>
                    </template>
                    <div class="balance-info">
                        <div class="balance-amount">
                            {{ formatBalance(walletInfo.balance) }}
                        </div>
                        <div class="balance-actions">
                            <el-button type="primary" @click="handleTransfer">转账</el-button>
                            <el-button type="success" @click="handleReceive">收款</el-button>
                        </div>
                    </div>
                </el-card>
            </el-col>

            <el-col :span="8">
                <el-card class="stats-card" shadow="hover">
                    <template #header>
                        <div class="card-header">
                            <span>交易统计</span>
                        </div>
                    </template>
                    <div class="stats-info">
                        <div class="stats-item">
                            <div class="stats-label">今日交易</div>
                            <div class="stats-value">{{ stats.todayCount || 0 }}</div>
                        </div>
                        <div class="stats-item">
                            <div class="stats-label">总交易次数</div>
                            <div class="stats-value">{{ stats.totalCount || 0 }}</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="24">
                <TransactionsChart :wallet-id="walletInfo.ID" />
            </el-col>
        </el-row>

        <!-- 转账弹窗简化 -->
        <el-dialog v-model="transferDialogVisible" title="转账" width="500px">
            <el-form :model="transferForm" :rules="transferRules" ref="transferFormRef" label-width="100px">
                <el-form-item label="接收钱包ID" prop="toWalletId">
                    <el-input v-model.number="transferForm.toWalletId" type="number" :prefix-icon="Wallet" />
                </el-form-item>
                <el-form-item label="转账额" prop="amount">
                    <el-input-number v-model="transferForm.amount" :precision="2" :step="0.1" :min="0.01"
                        controls-position="right" style="width: 100%;" />
                </el-form-item>
            </el-form>

            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="transferDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirmTransfer" :loading="transferLoading">
                        确认转账
                    </el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 编辑名称弹窗 -->
        <el-dialog v-model="editNameDialogVisible" title="编钱包名称" width="400px">
            <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="100px">
                <el-form-item label="钱包名称" prop="name">
                    <el-input v-model="editForm.name" maxlength="100" show-word-limit />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="editNameDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="confirmEdit" :loading="editLoading">
                        确认
                    </el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import axios from 'axios'
import TransactionsChart from './chart.vue'
import { useUserStore } from '@/pinia/modules/user'
import { Lock, Key, CircleCheck, Wallet } from '@element-plus/icons-vue'
import JsEncrypt from 'jsencrypt'

const userStore = useUserStore()
export default {
    name: 'Wallet',
    components: {
        TransactionsChart,
        Lock,
        Key,
        CircleCheck,
        Wallet
    },
    setup() {
        const walletInfo = ref({
            ID: 0,
            CreatedAt: '',
            UpdatedAt: '',
            DeletedAt: null,
            user_name: '',
            wallet_name: '',
            balance: 0
        })
        const stats = ref({})
        const transferDialogVisible = ref(false)
        const editNameDialogVisible = ref(false)
        const transferLoading = ref(false)
        const editLoading = ref(false)
        const transferFormRef = ref(null)
        const editFormRef = ref(null)

        const transferForm = reactive({
            toWalletId: '',
            amount: 0
        })

        const editForm = reactive({
            name: ''
        })

        const transferRules = {
            toWalletId: [
                { required: true, message: '请输入接收钱包ID', trigger: 'blur' },
                { type: 'number', message: '钱包ID必须为数字', trigger: 'blur' }
            ],
            amount: [
                { required: true, message: '请输入转账金额', trigger: 'blur' },
                { type: 'number', message: '金额必须为数字', trigger: 'blur' }
            ]
        }

        const editRules = {
            name: [
                { required: true, message: '请输入钱包名称', trigger: 'blur' },
                { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
            ]
        }

        const formatDate = (date) => {
            return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
        }

        const formatBalance = (balance) => {
            return Number(balance).toFixed(2)
        }

        // 获取钱包信息
        const getWalletInfo = async () => {
            try {
                const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/wallet/', {
                    user_name: userStore.userInfo.userName
                })
                walletInfo.value = response.data.data
                console.log('钱包信息:', walletInfo.value)
            } catch (error) {
                console.error('获取钱包信息失败:', error)
                ElMessage.error('获取钱包信息失败')
            }
        }

        // 获取钱包统计信息
        const getWalletStats = async () => {
            if (!walletInfo.value.ID) {
                console.warn('钱包ID不存在，跳过获取统计信息')
                return
            }

            try {
                const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/wallet/stats', {
                    wallet_id: walletInfo.value.ID
                })
                stats.value.todayCount = response.data.data.today_transactions
                stats.value.totalCount = response.data.data.total_transactions
            } catch (error) {
                console.error('获取统计信息失败:', error)
                ElMessage.error('获取统计信息失败')
            }
        }

        const handleTransfer = () => {
            transferForm.amount = 0
            transferForm.toWalletId = ''
            transferDialogVisible.value = true
        }

        const handleEdit = () => {
            editForm.name = walletInfo.value.WalletName
            editNameDialogVisible.value = true
        }

        const confirmTransfer = async () => {
            if (!transferFormRef.value) return

            await transferFormRef.value.validate(async (valid) => {
                if (valid) {
                    transferLoading.value = true
                    try {
                        const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/tx/transfer', {
                            from_wallet_id: walletInfo.value.ID,
                            to_wallet_id: transferForm.toWalletId,
                            amount: transferForm.amount,
                        })

                        const transferStatus = response.data.data.status;
                        if (transferStatus === 'completed') {
                            ElMessage.success('转账成功');
                            transferDialogVisible.value = false;
                            getWalletInfo(); // 刷新钱包信息
                        } else {
                            ElMessage.error('转账失败');
                        }
                    } catch (error) {
                        console.error('转账失败:', error)
                        ElMessage.error(error.response?.data?.message || '转账失败')
                    } finally {
                        transferLoading.value = false
                    }
                }
            })
        }

        const confirmEdit = async () => {
            if (!editFormRef.value) return

            await editFormRef.value.validate(async (valid) => {
                if (valid) {
                    editLoading.value = true
                    try {
                        await axios.post('http://45.8.113.140:3338/api/v1/hufu/wallet/update', {
                            id: walletInfo.value.ID,
                            wallet_name: editForm.name,
                            balance: walletInfo.value.balance
                        })
                        ElMessage.success('更新成功')
                        editNameDialogVisible.value = false
                        getWalletInfo()
                    } catch (error) {
                        console.error('更新失败:', error)
                        ElMessage.error('更新失败')
                    } finally {
                        editLoading.value = false
                    }
                }
            })
        }

        onMounted(async () => {
            try {
                await getWalletInfo()      // 等待获取钱包信息完成
                await getWalletStats()     // 等待获取统计信息完成
            } catch (error) {
                console.error('初始化数据失败:', error)
                ElMessage.error('初始化数据失败')
            }
        })

        return {
            walletInfo,
            stats,
            transferDialogVisible,
            editNameDialogVisible,
            transferLoading,
            editLoading,
            transferForm,
            editForm,
            transferFormRef,
            editFormRef,
            transferRules,
            editRules,
            formatDate,
            formatBalance,
            handleTransfer,
            handleEdit,
            confirmTransfer,
            confirmEdit
        }
    }
}
</script>

<style scoped>
.wallet-container {
    padding: 20px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.wallet-info {
    padding: 10px;
}

.info-item {
    margin-bottom: 15px;
    display: flex;
    align-items: center;
}

.info-item .label {
    width: 100px;
    color: #666;
}

.info-item .value {
    flex: 1;
    color: #333;
}

.balance-info {
    text-align: center;
    padding: 20px;
}

.balance-amount {
    font-size: 32px;
    font-weight: bold;
    color: #409EFF;
    margin-bottom: 20px;
}

.balance-actions {
    display: flex;
    justify-content: center;
    gap: 20px;
}

.stats-info {
    display: flex;
    justify-content: space-around;
    padding: 20px;
}

.stats-item {
    text-align: center;
}

.stats-label {
    color: #666;
    margin-bottom: 10px;
}

.stats-value {
    font-size: 24px;
    font-weight: bold;
    color: #409EFF;
}

.el-card {
    margin-bottom: 20px;
}

.wallet-card,
.balance-card,
.stats-card {
    height: 100%;
}

.transfer-content {
    position: relative;
    padding: 20px;
    border-radius: 4px;
    transition: all 0.3s;
}

.transfer-content.is-encrypted {
    background: #F0F9EB;
    border: 1px solid #E1F3D8;
}

.status-icon {
    font-size: 24px;
    color: #909399;
}

.status-icon .is-active {
    color: #67C23A;
}

.status-info {
    flex: 1;
}

.status-title {
    font-weight: bold;
    margin-bottom: 4px;
}

.status-desc {
    font-size: 12px;
    color: #909399;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    margin-top: 20px;
}

.dialog-footer .el-button {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    margin-left: 0;
}

:deep(.el-dialog__body) {
    padding: 20px 24px;
}

:deep(.el-dialog__footer) {
    padding: 0 24px 20px;
}
</style>