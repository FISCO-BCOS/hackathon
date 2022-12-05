<!--suppress ALL -->
<template>
  <div class="entRegist-container columnCC scroll-y">
    <el-form ref="ruleFormRef" class="entRegist-form" :model="ruleForm" label-position="top" status-icon :rules="rules">
      <div class="title-container">
        <h3 class="title text-center">企业注册信息表</h3>
      </div>
        <el-form-item size="large" label="账号" prop="AccountNumber">
          <el-input v-model="ruleForm.AccountNumber" clearable />
        </el-form-item>
        <el-form-item label="密码" prop="pass">
          <el-input v-model="ruleForm.pass" type="password" autocomplete="off" clearable />
        </el-form-item>
        <el-form-item label="再次输入密码" prop="checkPass">
          <el-input v-model="ruleForm.checkPass" type="password" autocomplete="off" clearable />
        </el-form-item>
        <el-form-item label="企业名称" prop="companyID">
          <el-input v-model="ruleForm.companyID" type="text" clearable />
        </el-form-item>
        <el-form-item label="统一社会信用代码" prop="TrustNumber">
          <el-input v-model.number="ruleForm.TrustNumber" clearable />
        </el-form-item>
        <el-form-item label="电话" prop="PhoneNumber">
          <el-input v-model.number="ruleForm.PhoneNumber" clearable />
        </el-form-item>
        <el-form-item label="地址" prop="Address">
          <el-input v-model="ruleForm.Address" type="text" clearable />
        </el-form-item>
      <!-- 选择器 -->
      <el-form-item label="企业类型" prop="CompanyType" size="large">
        <el-select v-model="ruleForm.CompanyType" placeholder="请选择企业类型">
          <el-option label="车企" value="2" />
          <el-option label="电池生产企业" value="3" />
          <el-option label="电池租赁商" value="4" />
          <el-option label="电池回收商" value="5" />
          <el-option label="储能企业" value="6" />
          <el-option label="电池原材料生产企业" value="10" />
        </el-select>
      </el-form-item>
      <el-form-item v-show="ruleForm.CompanyType == '2'" label="上一年汽车产量" prop="CarProductRegist">
        <el-input v-show="ruleForm.CompanyType == '2'" v-model="ruleForm.CarProductRegist" type="text" clearable />
      </el-form-item>
      <el-form-item v-show="ruleForm.CompanyType == '3'" label="上一年电池产量" prop="BatteryProductRegist">
        <el-input v-show="ruleForm.CompanyType == '3'" v-model="ruleForm.BatteryProductRegist" type="text" clearable />
      </el-form-item>
      <!-- 空一行！ -->
      <br />
      <el-row :gutter="24">
        <el-col :span="6">
          <el-button size="large" type="primary" @click="submitForm(ruleFormRef)">提交</el-button>
        </el-col>
        <el-col :span="6">
          <el-button size="large" @click="resetForm(ruleFormRef)">重置</el-button>
        </el-col>
        <el-col :span="6">
          <el-button size="large" @click.prevent="backLogin">返回</el-button>
        </el-col>
      </el-row>
      <br />
      <br />
      <br /><br />
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'
import type { FormInstance } from 'element-plus'
import { useUserStore } from '@/store/user'
import { Md5 } from 'ts-md5'

// handleregister 处理注册的方法：提交数据给后端【可参考登录处理方法】，返回一个弹框：注册信息已提交

const handleregister = (valid) => {
  let params = {
    userName: ruleForm.AccountNumber,
    password: ruleForm.pass,
    name: ruleForm.companyID,
    phone: ruleForm.PhoneNumber,
    idno: ruleForm.TrustNumber,
    address: ruleForm.Address,
    type: ruleForm.CompanyType,
    info: ""
  }
  params.password = Md5.hashStr(params.password)
  if (params.type == '2') {
    let extInfo = {
      carProductRegist: ruleForm.CarProductRegist
    }
    params.info = JSON.stringify(extInfo)
  }
  if (params.type == '3') {
    let extInfo = {
      batteryProductRegist: ruleForm.BatteryProductRegist
    }
    params.info = JSON.stringify(extInfo)
  }

  const userStore = useUserStore()
  userStore
    .entRegist(params)
    .then(() => {
      console.log("entRegist success")
      ElMessage({ message: '注册成功。', type: 'success', })
      useCommon()
        .sleep(3)
        .then(() => {
          router.push('/login')
        })
    })
    .catch((res) => {
      console.log("entRegist error")
      ElMessage.error(res.msg)
    })
}

//use the auto import from vite.config.js of AutoImport
const router = useRouter()

//返回
let backLogin = () => {
  router.push(`/login`)
}


// ↓ ↓ ======表单涉及的代码=======
const ruleFormRef = ref<FormInstance>()

// 用户名
const checkAccountNumber = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('请输入英文和数字的组合'))
  } else {
    callback()
  }
}
// 电话号码
const checkPhoneNumber = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('请输入正确的电话号码'))
  }
  setTimeout(() => {
    if (!Number.isInteger(value)) {
      callback(new Error('请输入数字'))
    }
    else {
      callback()
    }
  }, 1000)
}
// 密码
const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (ruleForm.checkPass !== '') {
      if (!ruleFormRef.value) return
      ruleFormRef.value.validateField('checkPass', () => null)
    }
    callback()
  }
}
// 密码匹配
const validatePass2 = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== ruleForm.pass) {
    callback(new Error("两次输入的密码不一样!"))
  } else {
    callback()
  }
}

// 地址
const Address = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('请输入企业地址'))
  } else {
    callback()
  }
}

// 社会信用代码
const TrustNumber = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('请输入统一社会信用代码'))
  } else {
    callback()
  }
}

// 公司名称

const companyID = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('请输入公司名称'))
  } else {
    callback()
  }
}

const ruleForm = reactive({
  AccountNumber: '',
  pass: '',
  checkPass: '',
  TrustNumber: '',
  companyID: '',
  Address: '',
  PhoneNumber: '',
  CompanyType: '',
  CarProductRegist: '',
  BatteryProductRegist: ''
})

const rules = reactive({
  AccountNumber: [{ validator: checkAccountNumber, trigger: 'blur' }],
  pass: [{ validator: validatePass, trigger: 'blur' }],
  checkPass: [{ validator: validatePass2, trigger: 'blur' }],
  PhoneNumber: [{ validator: checkPhoneNumber, trigger: 'blur' }],
  companyID: [{ validator: companyID, trigger: 'blur' }],
  TrustNumber: [{ validator: TrustNumber, trigger: 'blur' }],
  Address: [{ validator: Address, trigger: 'blur' }],
  CompanyType: [
    {
      // 决定了是否必须填
      required: true,
      message: '请选择企业类型',
      trigger: 'change',
    },
  ],
})

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      console.log('submit!');
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
// $bg: #2d3a4b;
// $dark_gray: #889aa4;
// $light_gray: #eee;
.entRegist-container {
  height: 100vh;
  width: 100%;

  // background-color: #2d3a4b;
  .entRegist-form {
    margin-bottom: 7vh;
    width: 600px;
  }

  .title-container {
    .title {
      font-size: 22px;
      // color: #eee;
      margin: 620px auto 25px auto;
      text-align: center;
      font-weight: bold;
    }
  }
}
</style>
