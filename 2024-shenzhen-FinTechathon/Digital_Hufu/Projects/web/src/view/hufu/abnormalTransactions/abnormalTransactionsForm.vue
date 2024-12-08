
<template>
  <div>
    <div class="gva-form-box">
      <el-form :model="formData" ref="elFormRef" label-position="right" :rules="rule" label-width="80px">
        <el-form-item label="钱包序号:" prop="wallet_id">
          <el-input v-model.number="formData.wallet_id" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="交易序号:" prop="transaction_id">
          <el-input v-model.number="formData.transaction_id" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="异常证据:" prop="evidence">
          <el-input v-model="formData.evidence" :clearable="true"  placeholder="请输入异常证据" />
       </el-form-item>
        <el-form-item label="监管签名:" prop="signature">
          <el-input v-model="formData.signature" :clearable="true"  placeholder="请输入监管签名" />
       </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">保存</el-button>
          <el-button type="primary" @click="back">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {
  createAbnormalTransactions,
  updateAbnormalTransactions,
  findAbnormalTransactions
} from '@/api/hufu/abnormalTransactions'

defineOptions({
    name: 'AbnormalTransactionsForm'
})

// 自动获取字典
import { getDictFunc } from '@/utils/format'
import { useRoute, useRouter } from "vue-router"
import { ElMessage } from 'element-plus'
import { ref, reactive } from 'vue'


const route = useRoute()
const router = useRouter()

const type = ref('')
const formData = ref({
            wallet_id: undefined,
            transaction_id: undefined,
            evidence: '',
            signature: '',
        })
// 验证规则
const rule = reactive({
})

const elFormRef = ref()

// 初始化方法
const init = async () => {
 // 建议通过url传参获取目标数据ID 调用 find方法进行查询数据操作 从而决定本页面是create还是update 以下为id作为url参数示例
    if (route.query.id) {
      const res = await findAbnormalTransactions({ ID: route.query.id })
      if (res.code === 0) {
        formData.value = res.data
        type.value = 'update'
      }
    } else {
      type.value = 'create'
    }
}

init()
// 保存按钮
const save = async() => {
      elFormRef.value?.validate( async (valid) => {
         if (!valid) return
            let res
           switch (type.value) {
             case 'create':
               res = await createAbnormalTransactions(formData.value)
               break
             case 'update':
               res = await updateAbnormalTransactions(formData.value)
               break
             default:
               res = await createAbnormalTransactions(formData.value)
               break
           }
           if (res.code === 0) {
             ElMessage({
               type: 'success',
               message: '创建/更改成功'
             })
           }
       })
}

// 返回按钮
const back = () => {
    router.go(-1)
}

</script>

<style>
</style>
