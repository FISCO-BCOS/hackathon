<template>
  <div class="app-container scroll-y">
  <el-row class="mb-4">
     <el-button type="primary" round size="large" @click="open">点击此处查询审批状态</el-button>
     <!-- <el-alert class=el-alert title="你已通过审批" type="success" description="" center effect="dark" show-icon />
     <el-alert class=el-alert title="未通过审批" type="error" description="请重新提交文档" center effect="dark" show-icon /> -->
 </el-row>
 </div>
</template>

<script lang="ts" setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { useEntAccessStore } from '@/store/entAccess'
// import type { Action } from 'element-plus'

const open = () => {
  // 查询调用请求后，弹窗，弹结果
  const entAccessStore = useEntAccessStore()
  entAccessStore.accessResult()
  .then((res: any) => {
      console.log("accessResult:" + res.code)
      let accessResult
      if (res.accessStatus == '1') {
        accessResult = '等待准入申请中'
      }
      if (res.accessStatus == '2') {
        accessResult = '审批中'
      }
      if (res.accessStatus == '3') {
        accessResult = '审批拒绝'
      }
      if (res.accessStatus == '4') {
        accessResult = '审批通过'
      }
      let msg = '审批结果：' + accessResult + '，审批机构：' + res.approvalEntName
      ElMessageBox({
        message: msg,
        title: '查询结果',
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        showCancelButton: false,
        type: 'info'
      })
    })
    .catch((res) => {
      console.log("accessResult error")
    })

}
</script>

<style scoped>
.el-alert {
 margin: 20px 0 0;
}

.el-alert:first-child {
 margin: 0;
}
</style>