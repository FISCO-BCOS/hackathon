<template>
    <div>
        <div class="transactions-container">
            <el-table :data="transactionList" border style="width: 100%" v-loading="loading">
                <el-table-column prop="ID" label="交易ID" width="100">
                    <template #default="{ row }">
                        {{ row.ID }}
                    </template>
                </el-table-column>


                <el-table-column prop="CreatedAt" label="日期" width="250">
                    <template #default="{ row }">
                        {{ formatDate(row.CreatedAt) }}
                    </template>
                </el-table-column>

                <el-table-column prop="from_username" label="付款方" width="160">
                    <template #default="{ row }">
                        {{ `${row.from_username} (钱包ID: ${row.from_wallet_id})` }}
                    </template>
                </el-table-column>

                <el-table-column prop="to_username" label="收款方" width="160">
                    <template #default="{ row }">
                        {{ `${row.to_username} (钱包ID: ${row.to_wallet_id})` }}
                    </template>
                </el-table-column>

                <el-table-column prop="amount" label="交易金额" min-width="120">
                    <template #default="{ row }">
                        {{ row.amount }}
                    </template>
                </el-table-column>

                <el-table-column prop="status" label="交易状况" min-width="100">
                    <template #default="{ row }">
                        <el-tag :type="getStatusType(row.status)">
                            {{ getStatusText(row.status) }}
                        </el-tag>
                    </template>
                </el-table-column>

                <el-table-column prop="type" label="交易类型" min-width="120">
                    <template #default="{ row }">
                        {{ getTypeText(row.type) }}
                    </template>
                </el-table-column>

                <el-table-column label="操作" min-width="200">
                    <template #default="{ row }">
                        <el-button size="small" @click="handleView(row)">查看</el-button>
                        <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
                    </template>
                </el-table-column>
            </el-table>

        </div>
        <div class="pagination-container">
            <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-size="pageSize"
                :current-page="currentPage" :page-sizes="[10, 20, 50, 100]" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" />
        </div>
    </div>
</template>

<script>
import axios from 'axios'
import dayjs from 'dayjs'
import { useUserStore } from '@/pinia/modules/user'
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
const userStore = useUserStore()


export default {
    name: 'Transactions',
    setup() {
        const walletInfo = ref({
            ID: null,
            name: '',
            balance: 0,
            CreatedAt: '',
            UpdatedAt: ''
        })
        const transactionList = ref([])
        const loading = ref(false)
        const total = ref(0)
        const currentPage = ref(1)
        const pageSize = ref(10)

        // 获取交易列表
        const getTransactionList = async () => {
            try {
                loading.value = true
                const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/tx/history', {
                    wallet_id: walletInfo.value.ID,
                    page: currentPage.value,
                    page_size: pageSize.value,
                })

                if (response.data.code === 0) {
                    transactionList.value = response.data.data.list || []
                    total.value = response.data.data.total || 0
                }
            } catch (error) {
                console.error('获取交易列表失败:', error)
                ElMessage.error('获取交易列表失败')
            } finally {
                loading.value = false
            }
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

        const handleSizeChange = (val) => {
            pageSize.value = val
            currentPage.value = 1
            getTransactionList()
        }

        const handleCurrentChange = (val) => {
            currentPage.value = val
            getTransactionList()
        }

        onMounted(async () => {
            try {
                await getWalletInfo()
                await getTransactionList()
            } catch (error) {
                console.error('初始化数据失败:', error)
                ElMessage.error('初始化数据失败')
            }
        })

        // 返回需要在模板中使用的数据和方法
        return {
            transactionList,
            loading,
            total,
            currentPage,
            pageSize,
            handleSizeChange,
            handleCurrentChange,
            formatDate(date) {
                return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
            },
            getStatusText(status) {
                const statusMap = {
                    completed: '已完成',
                    pending: '处理中',
                    failed: '失败',
                }
                return statusMap[status] || status
            },
            getStatusType(status) {
                const statusMap = {
                    completed: 'success',
                    pending: 'warning',
                    failed: 'danger',
                }
                return statusMap[status] || 'info'
            },
            getTypeText(type) {
                const typeMap = {
                    direct: '直接转账',
                }
                return typeMap[type] || type
            },
            handleView(row) {
                console.log('查看交易详情', row)
            },
            handleEdit(row) {
                console.log('编辑交易', row)
            },
        }
    }
}
</script>

<style scoped>
.transactions-container {
    padding: 20px;
}

.pagination-container {
    margin-top: 20px;
    margin-right: 20px;
    display: flex;
    justify-content: flex-end;
}
</style>