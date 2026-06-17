<template>
  <div
    id="userLayout"
    class="w-full h-full relative"
  >
    <div
      class="rounded-lg flex items-center justify-evenly w-full h-full md:w-screen md:h-screen md:bg-[#194bfb] bg-white"
    >
      <div class="md:w-3/5 w-10/12 h-full flex items-center justify-evenly">
        <div class="oblique h-[130%] w-3/5 bg-white dark:bg-slate-900 transform -rotate-12 absolute -ml-52" />
        <!-- 分割斜块 -->
        <div class="z-[999] pt-12 pb-10 md:w-96 w-full  rounded-lg flex flex-col justify-between box-border">
          <div>
            <div class="flex items-center justify-center">

              <img
                class="w-24"
                :src="$GIN_VUE_ADMIN.appLogo"
                alt
              >
            </div>
            <div class="mb-9">
              <p class="text-center text-4xl font-bold">{{ $GIN_VUE_ADMIN.appName }}</p>
            </div>
            <el-form
              ref="loginForm"
              :model="loginFormData"
              :rules="rules"
              :validate-on-rule-change="false"
              @keyup.enter="submitForm"
            >
              <el-form-item
                prop="username"
                class="mb-6"
              >
                <el-input
                  v-model="loginFormData.username"
                  size="large"
                  placeholder="请输入用户名"
                  suffix-icon="user"
                />
              </el-form-item>
              <el-form-item
                prop="password"
                class="mb-6"
              >
                <el-input
                  v-model="loginFormData.password"
                  show-password
                  size="large"
                  type="password"
                  placeholder="请输入密码"
                />
              </el-form-item>
              <el-form-item
                v-if="loginFormData.openCaptcha"
                prop="captcha"
                class="mb-6"
              >
                <div class="flex w-full justify-between">
                  <el-input
                    v-model="loginFormData.captcha"
                    placeholder="请输入验证码"
                    size="large"
                    class="flex-1 mr-5"
                  />
                  <div class="w-1/3 h-11 bg-[#c3d4f2] rounded">
                    <img
                      v-if="picPath"
                      class="w-full h-full"
                      :src="picPath"
                      alt="请输入验证码"
                      @click="loginVerify()"
                    >
                  </div>
                </div>
              </el-form-item>
              <el-form-item class="mb-6">
                <el-button
                  class="shadow shadow-active h-11 w-full"
                  type="primary"
                  size="large"
                  @click="submitForm"
                >登 录</el-button>
              </el-form-item>
              <!-- <el-form-item class="mb-6">
                <el-button
                  class="shadow shadow-active h-11 w-full"
                  type="primary"
                  size="large"
                  @click="checkInit"
                >前往初始化</el-button>

              </el-form-item> -->
              <el-form-item class="mb-6">
                <el-button
                  class="shadow shadow-active h-11 w-full"
                  type="primary"
                  size="large"
                  @click="register"
                >注册</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
      <!-- <div class="hidden md:block w-1/2 h-full float-right bg-[#194bfb]"><img
        class="h-full"
        src="@/assets/login_right_banner.jpg"
        alt="banner"
      ></div> -->
    </div>

    <BottomInfo class="left-0 right-0 absolute bottom-3 mx-auto  w-full z-20">
      <div class="links items-center justify-center gap-2 hidden md:flex">
      </div>
    </BottomInfo>

    <el-dialog
      v-model="registerVisible"
      title="用户注册"
      width="30%"
    >
      <el-form
        ref="registerForm"
        :model="registerFormData"
        :rules="registerRules"
        label-width="80px"
      >
        <el-form-item label="用户类型" prop="userType">
          <el-select v-model="registerFormData.userType" placeholder="请选择用户类型" class="w-full">
            <el-option label="普通用户" value="user" />
            <el-option label="商家" value="business" />
            <el-option label="监管机构" value="regulator" />
          </el-select>
        </el-form-item>
        <el-form-item 
          v-if="['user', 'business'].includes(registerFormData.userType)"
          label="钱包名称" 
          prop="walletName"
        >
          <el-input v-model="registerFormData.walletName" placeholder="请输入钱包名称" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerFormData.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerFormData.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerFormData.confirmPassword" type="password" placeholder="请确认密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="registerVisible = false">取消</el-button>
          <el-button type="primary" @click="handleRegister">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { captcha } from '@/api/user'
import { checkDB } from '@/api/initdb'
import BottomInfo from '@/components/bottomInfo/bottomInfo.vue'
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/pinia/modules/user'
import axios from 'axios'

defineOptions({
  name: "Login",
})

const router = useRouter()
// 验证函数
const checkUsername = (rule, value, callback) => {
  if (value.length < 5) {
    return callback(new Error('请输入正确的用户名'))
  } else {
    callback()
  }
}
const checkPassword = (rule, value, callback) => {
  if (value.length < 6) {
    return callback(new Error('请输入正确的密码'))
  } else {
    callback()
  }
}

// 获取验证码
const loginVerify = async() => {
  const ele = await captcha()
  rules.captcha.push({
    max: ele.data.captchaLength,
    min: ele.data.captchaLength,
    message: `请输入${ele.data.captchaLength}位验证码`,
    trigger: 'blur',
  })
  picPath.value = ele.data.picPath
  loginFormData.captchaId = ele.data.captchaId
  loginFormData.openCaptcha = ele.data.openCaptcha
}
loginVerify()

// 登录相关操作
const loginForm = ref(null)
const picPath = ref('')
const loginFormData = reactive({
  username: 'admin',
  password: '',
  captcha: '',
  captchaId: '',
  openCaptcha: false,
})
const rules = reactive({
  username: [{ validator: checkUsername, trigger: 'blur' }],
  password: [{ validator: checkPassword, trigger: 'blur' }],
  captcha: [
    {
      message: '验证码格式不正确',
      trigger: 'blur',
    },
  ],
})

const userStore = useUserStore()
const login = async() => {
  return await userStore.LoginIn(loginFormData)
}
const submitForm = () => {
  loginForm.value.validate(async(v) => {
    if (!v) {
      // 未通过前端静态验证
      ElMessage({
        type: 'error',
        message: '请正确填写登录信息',
        showClose: true,
      })
      await loginVerify()
      return false
    }

    // 通过验证，请求登陆
    const flag = await login()

    // 登陆失败，刷新验证码
    if (!flag) {
      await loginVerify()
      return false
    }

    // 登陆成功
    return true
  })
}

// 注册相关数据
const registerVisible = ref(false)
const registerForm = ref(null)
const registerFormData = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  userType: '',
  walletName: ''
})

// 注册表单验证规则
const registerRules = reactive({
  userType: [
    { required: true, message: '请选择用户类型', trigger: 'change' }
  ],
  walletName: [
    { required: true, message: '请输入钱包名称', trigger: 'blur' },
    { min: 3, message: '钱包名称长度不能小于3个字符', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, message: '用户名长度不能小于5个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerFormData.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})

// 修改原来的 register 函数
const register = () => {
  registerVisible.value = true
}

// 处理注册提交
const handleRegister = () => {
  registerForm.value.validate(async (valid) => {
    if (valid) {
      // TODO: 调用注册 API，需要传递用户类型和钱包名称
      const registerData = {
        userName: registerFormData.username,
        password: registerFormData.password,
        nickName: registerFormData.username,
        headerImg: '',
        walletName: registerFormData.walletName,
        authorityId: registerFormData.userType === 'user' ? 1 : registerFormData.userType === 'business' ? 3 : 2,
        authorityIds: [registerFormData.userType === 'user' ? 1 : registerFormData.userType === 'business' ? 3 : 2],
        enabled: 1
      }
      console.log('注册信息：', registerData)
      try {
        const response = await axios.post('http://45.8.113.140:3338/api/v1/hufu/register', registerData)
        console.log('注册结果：', response)

        if (response.data.msg === "注册成功") {
        ElMessage({
          type: 'success',
          message: '注册成功'
        })
        } else {
          ElMessage({
            type: 'error',
            message: response.data.msg
          })
        }
      } catch (error) {
        console.error('注册失败:', error)
        ElMessage.error('注册失败')
      }

      registerVisible.value = false
      // 重置表单
      registerFormData.username = ''
      registerFormData.password = ''
      registerFormData.confirmPassword = ''
      registerFormData.userType = ''
      registerFormData.walletName = ''
    } else {
      ElMessage({
        type: 'error',
        message: '请正确填写注册信息'
      })
      return false
    }
  })
}

// 跳转初始化
const checkInit = async() => {
  const res = await checkDB()
  if (res.code === 0) {
    if (res.data?.needInit) {
      userStore.NeedInit()
      await router.push({name: 'Init'})
    } else {
      ElMessage({
        type: 'info',
        message: '已配置数据库信息，无法初始化',
      })
    }
  }
}

</script>
