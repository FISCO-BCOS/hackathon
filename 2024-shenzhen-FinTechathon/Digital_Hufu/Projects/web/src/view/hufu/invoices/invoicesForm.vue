
<template>
  <div>
    <div class="gva-form-box">
      <el-form :model="formData" ref="elFormRef" label-position="right" :rules="rule" label-width="80px">
        <el-form-item label="发票号码:" prop="invoice_id">
          <el-input v-model="formData.invoice_id" :clearable="true"  placeholder="请输入发票号码" />
       </el-form-item>
        <el-form-item label="开票日期:" prop="issue_date">
          <el-input v-model="formData.issue_date" :clearable="true"  placeholder="请输入开票日期" />
       </el-form-item>
        <el-form-item label="卖方名称:" prop="seller_name">
          <el-input v-model="formData.seller_name" :clearable="true"  placeholder="请输入卖方名称" />
       </el-form-item>
        <el-form-item label="卖方税号:" prop="seller_tax_id">
          <el-input v-model="formData.seller_tax_id" :clearable="true"  placeholder="请输入卖方税号" />
       </el-form-item>
        <el-form-item label="项目名称:" prop="item_name">
          <el-input v-model="formData.item_name" :clearable="true"  placeholder="请输入项目名称" />
       </el-form-item>
        <el-form-item label="单价:" prop="unit_price">
          <el-input-number v-model="formData.unit_price" :precision="2" :clearable="true"></el-input-number>
       </el-form-item>
        <el-form-item label="数量:" prop="quantity">
          <el-input v-model.number="formData.quantity" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="金额:" prop="amount">
          <el-input-number v-model="formData.amount" :precision="2" :clearable="true"></el-input-number>
       </el-form-item>
        <el-form-item label="税率:" prop="tax_rate">
          <el-input-number v-model="formData.tax_rate" :precision="2" :clearable="true"></el-input-number>
       </el-form-item>
        <el-form-item label="税额:" prop="tax_amount">
          <el-input-number v-model="formData.tax_amount" :precision="2" :clearable="true"></el-input-number>
       </el-form-item>
        <el-form-item label="合计金额:" prop="total_amount">
          <el-input-number v-model="formData.total_amount" :precision="2" :clearable="true"></el-input-number>
       </el-form-item>
        <el-form-item label="发票状态:" prop="invoice_status">
          <el-input v-model="formData.invoice_status" :clearable="true"  placeholder="请输入发票状态" />
       </el-form-item>
        <el-form-item label="备注:" prop="remarks">
          <el-input v-model="formData.remarks" :clearable="true"  placeholder="请输入备注" />
       </el-form-item>
        <el-form-item label="发票类型:" prop="invoice_type">
          <el-input v-model="formData.invoice_type" :clearable="true"  placeholder="请输入发票类型" />
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
  createInvoices,
  updateInvoices,
  findInvoices
} from '@/api/hufu/invoices'

defineOptions({
    name: 'InvoicesForm'
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
            invoice_id: '',
            issue_date: '',
            seller_name: '',
            seller_tax_id: '',
            item_name: '',
            unit_price: 0,
            quantity: undefined,
            amount: 0,
            tax_rate: 0,
            tax_amount: 0,
            total_amount: 0,
            invoice_status: '',
            remarks: '',
            invoice_type: '',
        })
// 验证规则
const rule = reactive({
})

const elFormRef = ref()

// 初始化方法
const init = async () => {
 // 建议通过url传参获取目标数据ID 调用 find方法进行查询数据操作 从而决定本页面是create还是update 以下为id作为url参数示例
    if (route.query.id) {
      const res = await findInvoices({ ID: route.query.id })
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
               res = await createInvoices(formData.value)
               break
             case 'update':
               res = await updateInvoices(formData.value)
               break
             default:
               res = await createInvoices(formData.value)
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
