
<template>
  <div>
    <div class="gva-search-box">
      <el-form ref="elSearchFormRef" :inline="true" :model="searchInfo" class="demo-form-inline" :rules="searchRule" @keyup.enter="onSubmit">
      <el-form-item label="创建日期" prop="createdAt">
      <template #label>
        <span>
          创建日期
          <el-tooltip content="搜索范围是开始日期（包含）至结束日期（不包含）">
            <el-icon><QuestionFilled /></el-icon>
          </el-tooltip>
        </span>
      </template>
      <el-date-picker v-model="searchInfo.startCreatedAt" type="datetime" placeholder="开始日期" :disabled-date="time=> searchInfo.endCreatedAt ? time.getTime() > searchInfo.endCreatedAt.getTime() : false"></el-date-picker>
       —
      <el-date-picker v-model="searchInfo.endCreatedAt" type="datetime" placeholder="结束日期" :disabled-date="time=> searchInfo.startCreatedAt ? time.getTime() < searchInfo.startCreatedAt.getTime() : false"></el-date-picker>
      </el-form-item>
      

        <template v-if="showAllQuery">
          <!-- 将需要控制显示状态的查询条件添加到此范围内 -->
        </template>

        <el-form-item>
          <el-button type="primary" icon="search" @click="onSubmit">查询</el-button>
          <el-button icon="refresh" @click="onReset">重置</el-button>
          <el-button link type="primary" icon="arrow-down" @click="showAllQuery=true" v-if="!showAllQuery">展开</el-button>
          <el-button link type="primary" icon="arrow-up" @click="showAllQuery=false" v-else>收起</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="gva-table-box">
        <div class="gva-btn-list">
            <el-button  type="primary" icon="plus" @click="openDialog">新增</el-button>
            <el-button  icon="delete" style="margin-left: 10px;" :disabled="!multipleSelection.length" @click="onDelete">删除</el-button>
            
        </div>
        <el-table
        ref="multipleTable"
        style="width: 100%"
        tooltip-effect="dark"
        :data="tableData"
        row-key="ID"
        @selection-change="handleSelectionChange"
        >
        <el-table-column type="selection" width="55" />
        
        <el-table-column align="left" label="日期" prop="createdAt" width="180">
            <template #default="scope">{{ formatDate(scope.row.CreatedAt) }}</template>
        </el-table-column>
        
          <el-table-column align="left" label="发票号码" prop="invoice_id" width="120" />
          <el-table-column align="left" label="开票日期" prop="issue_date" width="120" />
          <el-table-column align="left" label="卖方名称" prop="seller_name" width="120" />
          <el-table-column align="left" label="卖方税号" prop="seller_tax_id" width="120" />
          <el-table-column align="left" label="项目名称" prop="item_name" width="120" />
          <el-table-column align="left" label="单价" prop="unit_price" width="120" />
          <el-table-column align="left" label="数量" prop="quantity" width="120" />
          <el-table-column align="left" label="金额" prop="amount" width="120" />
          <el-table-column align="left" label="税率" prop="tax_rate" width="120" />
          <el-table-column align="left" label="税额" prop="tax_amount" width="120" />
          <el-table-column align="left" label="合计金额" prop="total_amount" width="120" />
          <el-table-column align="left" label="发票状态" prop="invoice_status" width="120" />
          <el-table-column align="left" label="备注" prop="remarks" width="120" />
          <el-table-column align="left" label="发票类型" prop="invoice_type" width="120" />
        <el-table-column align="left" label="操作" fixed="right" min-width="240">
            <template #default="scope">
            <el-button  type="primary" link class="table-button" @click="getDetails(scope.row)"><el-icon style="margin-right: 5px"><InfoFilled /></el-icon>查看详情</el-button>
            <el-button  type="primary" link icon="edit" class="table-button" @click="updateInvoicesFunc(scope.row)">变更</el-button>
            <el-button  type="primary" link icon="delete" @click="deleteRow(scope.row)">删除</el-button>
            </template>
        </el-table-column>
        </el-table>
        <div class="gva-pagination">
            <el-pagination
            layout="total, sizes, prev, pager, next, jumper"
            :current-page="page"
            :page-size="pageSize"
            :page-sizes="[10, 30, 50, 100]"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
            />
        </div>
    </div>
    <el-drawer destroy-on-close size="800" v-model="dialogFormVisible" :show-close="false" :before-close="closeDialog">
       <template #header>
              <div class="flex justify-between items-center">
                <span class="text-lg">{{type==='create'?'添加':'修改'}}</span>
                <div>
                  <el-button type="primary" @click="enterDialog">确 定</el-button>
                  <el-button @click="closeDialog">取 消</el-button>
                </div>
              </div>
            </template>

          <el-form :model="formData" label-position="top" ref="elFormRef" :rules="rule" label-width="80px">
            <!-- <el-form-item label="发票号码:"  prop="invoice_id" >
              <el-input v-model="formData.invoice_id" :clearable="true"  placeholder="请输入发票号码" />
            </el-form-item> -->
            <el-form-item label="开票日期:"  prop="issue_date" >
              <el-input v-model="formData.issue_date" :clearable="true"  placeholder="请输入开票日期" />
            </el-form-item>
            <el-form-item label="卖方名称:"  prop="seller_name" >
              <el-input v-model="formData.seller_name" :clearable="true"  placeholder="请输入卖方名称" />
            </el-form-item>
            <el-form-item label="卖方税号:"  prop="seller_tax_id" >
              <el-input v-model="formData.seller_tax_id" :clearable="true"  placeholder="请输入卖方税号" />
            </el-form-item>
            <el-form-item label="项目名称:"  prop="item_name" >
              <el-input v-model="formData.item_name" :clearable="true"  placeholder="请输入项目名称" />
            </el-form-item>
            <el-form-item label="单价:"  prop="unit_price" >
              <el-input-number v-model="formData.unit_price"  style="width:100%" :precision="2" :clearable="true"  />
            </el-form-item>
            <el-form-item label="数量:"  prop="quantity" >
              <el-input v-model.number="formData.quantity" :clearable="true" placeholder="请输入数量" />
            </el-form-item>
            <el-form-item label="金额:"  prop="amount" >
              <el-input-number v-model="formData.amount"  style="width:100%" :precision="2" :clearable="true"  />
            </el-form-item>
            <el-form-item label="税率:"  prop="tax_rate" >
              <el-input-number v-model="formData.tax_rate"  style="width:100%" :precision="2" :clearable="true"  />
            </el-form-item>
            <el-form-item label="税额:"  prop="tax_amount" >
              <el-input-number v-model="formData.tax_amount"  style="width:100%" :precision="2" :clearable="true"  />
            </el-form-item>
            <el-form-item label="合计金额:"  prop="total_amount" >
              <el-input-number v-model="formData.total_amount"  style="width:100%" :precision="2" :clearable="true"  />
            </el-form-item>
            <el-form-item label="发票状态:"  prop="invoice_status" >
              <el-input v-model="formData.invoice_status" :clearable="true"  placeholder="请输入发票状态" />
            </el-form-item>
            <el-form-item label="备注:"  prop="remarks" >
              <el-input v-model="formData.remarks" :clearable="true"  placeholder="请输入备注" />
            </el-form-item>
            <el-form-item label="发票类型:"  prop="invoice_type" >
              <el-input v-model="formData.invoice_type" :clearable="true"  placeholder="请输入发票类型" />
            </el-form-item>
          </el-form>
    </el-drawer>

    <el-drawer destroy-on-close size="800" v-model="detailShow" :show-close="true" :before-close="closeDetailShow">
            <el-descriptions :column="1" border>
                    <el-descriptions-item label="发票号码">
                        {{ detailFrom.invoice_id }}
                    </el-descriptions-item>
                    <el-descriptions-item label="开票日期">
                        {{ detailFrom.issue_date }}
                    </el-descriptions-item>
                    <el-descriptions-item label="卖方名称">
                        {{ detailFrom.seller_name }}
                    </el-descriptions-item>
                    <el-descriptions-item label="卖方税号">
                        {{ detailFrom.seller_tax_id }}
                    </el-descriptions-item>
                    <el-descriptions-item label="项目名称">
                        {{ detailFrom.item_name }}
                    </el-descriptions-item>
                    <el-descriptions-item label="单价">
                        {{ detailFrom.unit_price }}
                    </el-descriptions-item>
                    <el-descriptions-item label="数量">
                        {{ detailFrom.quantity }}
                    </el-descriptions-item>
                    <el-descriptions-item label="金额">
                        {{ detailFrom.amount }}
                    </el-descriptions-item>
                    <el-descriptions-item label="税率">
                        {{ detailFrom.tax_rate }}
                    </el-descriptions-item>
                    <el-descriptions-item label="税额">
                        {{ detailFrom.tax_amount }}
                    </el-descriptions-item>
                    <el-descriptions-item label="合计金额">
                        {{ detailFrom.total_amount }}
                    </el-descriptions-item>
                    <el-descriptions-item label="发票状态">
                        {{ detailFrom.invoice_status }}
                    </el-descriptions-item>
                    <el-descriptions-item label="备注">
                        {{ detailFrom.remarks }}
                    </el-descriptions-item>
                    <el-descriptions-item label="发票类型">
                        {{ detailFrom.invoice_type }}
                    </el-descriptions-item>
            </el-descriptions>
        </el-drawer>

  </div>
</template>

<script setup>
import {
  createInvoices,
  deleteInvoices,
  deleteInvoicesByIds,
  updateInvoices,
  findInvoices,
  getInvoicesList
} from '@/api/hufu/invoices'

// 全量引入格式化工具 请按需保留
import { getDictFunc, formatDate, formatBoolean, filterDict ,filterDataSource, returnArrImg, onDownloadFile } from '@/utils/format'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref, reactive } from 'vue'




defineOptions({
    name: 'Invoices'
})

// 控制更多查询条件显示/隐藏状态
const showAllQuery = ref(false)

// 自动化生成的字典（可能为空）以及字段
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

const searchRule = reactive({
  createdAt: [
    { validator: (rule, value, callback) => {
      if (searchInfo.value.startCreatedAt && !searchInfo.value.endCreatedAt) {
        callback(new Error('请填写结束日期'))
      } else if (!searchInfo.value.startCreatedAt && searchInfo.value.endCreatedAt) {
        callback(new Error('请填写开始日期'))
      } else if (searchInfo.value.startCreatedAt && searchInfo.value.endCreatedAt && (searchInfo.value.startCreatedAt.getTime() === searchInfo.value.endCreatedAt.getTime() || searchInfo.value.startCreatedAt.getTime() > searchInfo.value.endCreatedAt.getTime())) {
        callback(new Error('开始日期应当早于结束日期'))
      } else {
        callback()
      }
    }, trigger: 'change' }
  ],
})

const elFormRef = ref()
const elSearchFormRef = ref()

// =========== 表格控制部分 ===========
const page = ref(1)
const total = ref(0)
const pageSize = ref(10)
const tableData = ref([])
const searchInfo = ref({})

// 重置
const onReset = () => {
  searchInfo.value = {}
  getTableData()
}

// 搜索
const onSubmit = () => {
  elSearchFormRef.value?.validate(async(valid) => {
    if (!valid) return
    page.value = 1
    pageSize.value = 10
    getTableData()
  })
}

// 分页
const handleSizeChange = (val) => {
  pageSize.value = val
  getTableData()
}

// 修改页面容量
const handleCurrentChange = (val) => {
  page.value = val
  getTableData()
}

// 查询
const getTableData = async() => {
  const table = await getInvoicesList({ page: page.value, pageSize: pageSize.value, ...searchInfo.value })
  if (table.code === 0) {
    tableData.value = table.data.list
    total.value = table.data.total
    page.value = table.data.page
    pageSize.value = table.data.pageSize
  }
}

getTableData()

// ============== 表格控制部分结束 ===============

// 获取需要的字典 可能为空 按需保留
const setOptions = async () =>{
}

// 获取需要的字典 可能为空 按需保留
setOptions()


// 多选数据
const multipleSelection = ref([])
// 多选
const handleSelectionChange = (val) => {
    multipleSelection.value = val
}

// 删除行
const deleteRow = (row) => {
    ElMessageBox.confirm('确定要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
            deleteInvoicesFunc(row)
        })
    }

// 多选删除
const onDelete = async() => {
  ElMessageBox.confirm('确定要删除吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async() => {
      const IDs = []
      if (multipleSelection.value.length === 0) {
        ElMessage({
          type: 'warning',
          message: '请选择要删除的数据'
        })
        return
      }
      multipleSelection.value &&
        multipleSelection.value.map(item => {
          IDs.push(item.ID)
        })
      const res = await deleteInvoicesByIds({ IDs })
      if (res.code === 0) {
        ElMessage({
          type: 'success',
          message: '删除成功'
        })
        if (tableData.value.length === IDs.length && page.value > 1) {
          page.value--
        }
        getTableData()
      }
      })
    }

// 行为控制标记（弹窗内部需要增还是改）
const type = ref('')

// 更新行
const updateInvoicesFunc = async(row) => {
    const res = await findInvoices({ ID: row.ID })
    type.value = 'update'
    if (res.code === 0) {
        formData.value = res.data
        dialogFormVisible.value = true
    }
}


// 删除行
const deleteInvoicesFunc = async (row) => {
    const res = await deleteInvoices({ ID: row.ID })
    if (res.code === 0) {
        ElMessage({
                type: 'success',
                message: '删除成功'
            })
            if (tableData.value.length === 1 && page.value > 1) {
            page.value--
        }
        getTableData()
    }
}

// 弹窗控制标记
const dialogFormVisible = ref(false)

// 打开弹窗
const openDialog = () => {
    type.value = 'create'
    dialogFormVisible.value = true
}

// 关闭弹窗
const closeDialog = () => {
    dialogFormVisible.value = false
    formData.value = {
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
        }
}
// 弹窗确定
const enterDialog = async () => {
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
                closeDialog()
                getTableData()
              }
      })
}


const detailFrom = ref({})

// 查看详情控制标记
const detailShow = ref(false)


// 打开详情弹窗
const openDetailShow = () => {
  detailShow.value = true
}


// 打开详情
const getDetails = async (row) => {
  // 打开弹窗
  const res = await findInvoices({ ID: row.ID })
  if (res.code === 0) {
    detailFrom.value = res.data
    openDetailShow()
  }
}


// 关闭详情弹窗
const closeDetailShow = () => {
  detailShow.value = false
  detailFrom.value = {}
}


</script>

<style>

</style>