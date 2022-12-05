<!--suppress ALL -->
<template>
  <div class="app-container scroll-y">
      <!-- 使用了container来布局 -->
      <div class="common-layout">
          <el-container>
              <el-header height="100xp">
                  <br />
                  <h2>
                      <!--  class="text-center " -->
                      电池信息录入
                  </h2>
              </el-header>
              <el-container>
                  <!-- <el-aside width="400px"></el-aside> -->
                  <el-main>
                      <el-form ref="ruleFormRef" :model="ruleForm" label-position="top" status-icon :rules="rules"
                          class="demo-ruleForm" style="max-width: 460px">

                          <el-form-item label="电池编号" prop="BatteryNumber">
                              <el-input v-model="ruleForm.BatteryNumber" clearable />
                          </el-form-item>

                          <!-- <el-form-item label="电池厂商代码" prop="BatteryCompanyCode">
                              <el-input v-model="ruleForm.BatteryCompanyCode" clearable />
                          </el-form-item> -->
                          <el-form-item label="生产批号" prop="BatchNumber">
                              <el-input v-model="ruleForm.BatchNumber" clearable />
                          </el-form-item>

                          <el-form-item label="规格代码" prop="Coding">
                              <el-input v-model="ruleForm.Coding" clearable />
                          </el-form-item>

                          <el-form-item label="标称电压" prop="NominalVoltage">
                              <el-input v-model="ruleForm.NominalVoltage" clearable />
                          </el-form-item>
                          <el-form-item label="标称容量" prop="NominalCapacity">
                              <el-input v-model="ruleForm.NominalCapacity" clearable />
                          </el-form-item>
                          <el-form-item label="预计充放电次数" prop="ExpectedNumber">
                              <el-input v-model.number="ruleForm.ExpectedNumber" clearable />
                          </el-form-item>
                 
                      </el-form>    
                      <!-- 选项 参考链接：https://element-plus.gitee.io/zh-CN/component/radio.html#单选框组-->
                      <!-- 通过 change 事件来响应变化，它会传入一个参数 value 来表示改变之后的值。 -->
                      <el-row :gutter="20">
                          <el-col :span="16">
                              <el-radio-group v-model="radio" size="large" @change="change">
                                  <el-radio :label="1">铅酸动力电池</el-radio>
                                  <el-radio :label="2">镍镉动力电池</el-radio>
                                  <el-radio :label="3">石墨烯电池</el-radio>
                                  <el-radio :label="4">锂离子动力电池</el-radio>
                                  <el-radio :label="5">镍氢动力电池</el-radio>
                              </el-radio-group>
                          </el-col>
                      </el-row>
                      <br />
                      <el-form-item  > 
                          <el-button round type="primary" @click="submitForm(ruleFormRef)" >提交</el-button>
                          <el-button round @click="resetForm(ruleFormRef)">重置</el-button>
                          <!-- <el-button @click.prevent="backLogin">返回</el-button> -->
                      </el-form-item>
                  </el-main>
              </el-container>
          </el-container>
      </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'
import type { FormInstance } from 'element-plus'
import { useBatteryStore } from '@/store/battery'

// 选项
const radio = ref(4)
const change = (label) =>{
  // change 可以得到label信息，也就是选项信息。
  console.log(label)

}

// handleregister 处理注册的方法：提交数据给后端【可参考登录处理方法】，返回一个弹框：注册信息已提交
const handleregister = (valid) => {
  let params = {
    id: ruleForm.BatteryNumber,
    batchNo: ruleForm.BatchNumber,
    code: ruleForm.Coding,
    type: radio.value,
    vdc: ruleForm.NominalVoltage,
    kah: ruleForm.NominalCapacity,
    chargeTimes: ruleForm.ExpectedNumber
  }


  const batteryStore = useBatteryStore()
  batteryStore.add(params)
    .then(() => {
      console.log("batteryAdd success")
      ElMessage({message: '录入成功。', type: 'success',})
      useCommon()
        .sleep(700)
        .then(() => {
            ToList()
        })
 
    })
    .catch((res) => {
      console.log("batteryAdd error")
      ElMessage.error(res.msg)

    })
}

//use the auto import from vite.config.js of AutoImport
const router = useRouter()

// //返回 
let ToList = () => {
    router.push(`/battery/list`)
}


// ↓ ↓ ======表单涉及的代码=======
const ruleFormRef = ref<FormInstance>()

// 电池编号 
const BatteryNumber = (rule: any, value: any, callback: any) => {
  if (!value) {
      return callback(new Error('请输入电池编号'))
  } else {
      callback()
  }
}


// 厂家代码
const BatteryCompanyCode = (rule: any, value: any, callback: any) => {
  if (!value) {
      return callback(new Error('请输入电池厂商代码'))
  } else {
      callback()
  }
}

// 标称容量
const NominalCapacity = (rule: any, value: any, callback: any) => {
  if (!value) {
      return callback(new Error('请输入电池的标称容量'))
  } else {
      callback()
  }
}
// 规格代码
const Coding = (rule: any, value: any, callback: any) => {
  if (!value) {
      return callback(new Error('请输入电池的规格代码'))
  } else {
      callback()
  }
}


// 预计充放电次数
const ExpectedNumber = (rule: any, value: any, callback: any) => {
  if (!value) {
      return callback(new Error('请输入电池的预计充放电次数'))
  } else {
      callback()
  }
}

// 标称电压
const NominalVoltage = (rule: any, value: any, callback: any) => {
  if (!value) {
      return callback(new Error('请输入电池的标称电压'))
  } else {
      callback()
  }
}

const BatchNumber = (rule: any, value: any, callback: any) => {
  if (!value) {
      return callback(new Error('请输入电池的生产批号'))
  } else {
      callback()
  }
}

const ruleForm = reactive({
  NominalCapacity: '',
  ExpectedNumber: '',
  NominalVoltage: '',
  Coding: '',
  BatchNumber: '',
  BatteryCompanyCode: '',
  BatteryNumber: '',
})

const rules = reactive({
  NominalCapacity: [{ validator: NominalCapacity, trigger: 'blur' }],
  ExpectedNumber: [{ validator: ExpectedNumber, trigger: 'blur' }],
  NominalVoltage: [{ validator: NominalVoltage, trigger: 'blur' }],
  Coding: [{ validator: Coding, trigger: 'blur' }],
  BatteryCompanyCode: [{ validator: BatteryCompanyCode, trigger: 'blur' }],
  BatteryNumber: [{ validator: BatteryNumber, trigger: 'blur' }],
  BatchNumber: [{ validator: BatchNumber, trigger: 'blur' }]
})

// 提交表单的处理方法，注册信息接口
const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
      if (valid) {
          console.log('submit!');
          // 提交后的信息弹窗方法
          handleregister(valid);
      } else {
          console.log('error submit!');
          handleregister(valid);
          return false
      }

  })
}
// 重置内容
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
// ↑ ↑ ======表单涉及的代码=======
</script>

<style lang="scss" scoped>

</style>
