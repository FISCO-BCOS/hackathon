<template>
  <div>
    <div class="grid grid-cols-12 w-full gap-2">
      <div class="col-span-3 h-full">
        <div class="w-full h-full bg-white px-4 py-8 rounded-lg shadow-lg box-border">
          <div class="user-card px-6 text-center bg-white shrink-0">
            <div class="flex justify-center">
              <SelectImage
                v-model="userStore.userInfo.headerImg"
                file-type="image"
              />
            </div>
            <div class="py-6 text-center">
              <p
                v-if="!editFlag"
                class="text-3xl flex justify-center items-center gap-4"
              >
                {{ userStore.userInfo.nickName }}
                <el-icon
                  class="cursor-pointer text-sm"
                  color="#66b1ff"
                  @click="openEdit"
                >
                  <edit />
                </el-icon>
              </p>
              <p
                v-if="editFlag"
                class="flex justify-center items-center gap-4"
              >
                <el-input v-model="nickName" />
                <el-icon
                  class="cursor-pointer"
                  color="#67c23a"
                  @click="enterEdit"
                >
                  <check />
                </el-icon>
                <el-icon
                  class="cursor-pointer"
                  color="#f23c3c"
                  @click="closeEdit"
                >
                  <close />
                </el-icon>
              </p>
              <p class="text-gray-500 mt-2 text-md">这个家伙很懒，什么都没有留下</p>
            </div>
            <div class="w-full h-full text-left">
              <ul class="inline-block h-full w-full">
                <li class="info-list">
                  <el-icon>
                    <user />
                  </el-icon>
                  {{ userStore.userInfo.nickName }}
                </li>
                <el-tooltip
                  class="item"
                  effect="light"
                  placement="top"
                >
                  <li class="info-list">
                    <el-icon>
                      <data-analysis />
                    </el-icon>
                    {{ userStore.userInfo.organization}}
                  </li>
                </el-tooltip>
                <li class="info-list">
                  <el-icon>
                    <video-camera />
                  </el-icon>
                  中国·北京市·海淀区
                </li>
                <el-tooltip
                  class="item"
                  effect="light"
                  placement="top"
                >
                  <li class="info-list">
                    <el-icon>
                      <medal />
                    </el-icon>
                    GoLang/JavaScript/Vue/Gorm
                  </li>
                </el-tooltip>

               <!--添加余额功能-->
               <li class="info-list">
                  <el-icon>
                    <money />
                  </el-icon>
                  {{ userStore.userInfo.money }}
                </li>

              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="col-span-9 ">
        <div class="bg-white h-full px-4 py-8 rounded-lg shadow-lg box-border">
          <el-tabs
            v-model="activeName"
            @tab-click="handleClick"
          >
            <el-tab-pane
              label="账号绑定"
              name="second"
            >
              <ul>
                <li class="borderd">
                  <p class="pb-2.5 text-xl text-gray-600">密保手机</p>
                  <p class="pb-2.5 text-lg text-gray-400">
                    已绑定手机:{{ userStore.userInfo.phone }}
                    <a
                      href="javascript:void(0)"
                      class="float-right text-blue-400"
                      @click="changePhoneFlag = true"
                    >立即修改</a>
                  </p>
                </li>
                <li class="borderd pt-2.5">
                  <p class="pb-2.5 text-xl text-gray-600">密保邮箱</p>
                  <p class="pb-2.5 text-lg text-gray-400">
                    已绑定邮箱：{{ userStore.userInfo.email }}
                    <a
                      href="javascript:void(0)"
                      class="float-right text-blue-400"
                      @click="changeEmailFlag = true"
                    >立即修改</a>
                  </p>
                </li>
                <li class="borderd pt-2.5">
                  <p class="pb-2.5 text-xl text-gray-600">密保问题</p>
                  <p class="pb-2.5 text-lg text-gray-400">
                    未设置密保问题
                    <a
                      href="javascript:void(0)"
                      class="float-right text-blue-400"
                    >去设置</a>
                  </p>
                </li>
                <li class="borderd pt-2.5">
                  <p class="pb-2.5 text-xl text-gray-600">修改密码</p>
                  <p class="pb-2.5 text-lg text-gray-400">
                    修改个人密码
                    <a
                      href="javascript:void(0)"
                      class="float-right text-blue-400"
                      @click="showPassword = true"
                    >修改密码</a>
                  </p>
                </li>
                <li class="borderd pt-2.5">
                  <p class="pb-2.5 text-xl text-gray-600">区块链身份标识</p>
                  <p class="pb-2.5 text-lg text-gray-400">
                    {{ userStore.userInfo.biaoshi }}
                  </p>
                </li>
              </ul>
            </el-tab-pane>
          
          <!-- 版权管理 -->
            <el-tab-pane label="版权管理" name="xxx">
              <el-table :data="tableData">
        <el-table-column
          align="center"
          label="预览"
          width="90"
        >
          <template #default="scope">
            <CustomPic
              pic-type="file"
              :pic-src="scope.row.url"
              preview
            />
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="购买日期"
          prop="UpdatedAt"
          width="160"
        >
          <template #default="scope">
            <div>{{ formatDate(scope.row.UpdatedAt) }}</div>
          </template>
        </el-table-column>
        
        <el-table-column
          align="center"
          label="版权标识"
          prop="url"
          min-width="500"
        >
        <template #default="scope">
              <div
                class="标识"
              >{{ scope.row.biaoshi }}</div>
            </template>
        </el-table-column>
        
        <el-table-column
          align="center"
          label="详情"
          min-width="110"
        >
        <template #default="scope">
          <el-button
            type="primary"
            icon="view"
            @click="showDetail(scope.row.url)"
          >查看详情</el-button>
        </template>
          
        </el-table-column>

      </el-table>
      <div class="gva-pagination">
          <el-pagination
            :current-page="page"
            :page-size="pageSize"
            :page-sizes="[3, 5, 10, 30, 50, 100]"
            :style="{ float: 'right', padding: '20px' }"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>

     

    <el-dialog
      v-model="showPassword"
      title="修改密码"
      width="360px"
      @close="clearPassword"
    >
      <el-form
        ref="modifyPwdForm"
        :model="pwdModify"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item
          :minlength="6"
          label="原密码"
          prop="password"
        >
          <el-input
            v-model="pwdModify.password"
            show-password
          />
        </el-form-item>
        <el-form-item
          :minlength="6"
          label="新密码"
          prop="newPassword"
        >
          <el-input
            v-model="pwdModify.newPassword"
            show-password
          />
        </el-form-item>
        <el-form-item
          :minlength="6"
          label="确认密码"
          prop="confirmPassword"
        >
          <el-input
            v-model="pwdModify.confirmPassword"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button

            @click="showPassword = false"
          >取 消</el-button>
          <el-button

            type="primary"
            @click="savePassword"
          >确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
      v-model="changePhoneFlag"
      title="绑定手机"
      width="600px"
    >
      <el-form :model="phoneForm">
        <el-form-item
          label="手机号"
          label-width="120px"
        >
          <el-input
            v-model="phoneForm.phone"
            placeholder="请输入手机号"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item
          label="验证码"
          label-width="120px"
        >
          <div class="flex w-full gap-4">
            <el-input
              v-model="phoneForm.code"
              class="flex-1"
              autocomplete="off"
              placeholder="请自行设计短信服务，此处为模拟随便写"
              style="width:300px"
            />
            <el-button
              type="primary"
              :disabled="time>0"
              @click="getCode"
            >{{ time>0?`(${time}s)后重新获取`:'获取验证码' }}</el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button

            @click="closeChangePhone"
          >取消</el-button>
          <el-button
            type="primary"

            @click="changePhone"
          >更改</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog
      v-model="changeEmailFlag"
      title="绑定邮箱"
      width="600px"
    >
      <el-form :model="emailForm">
        <el-form-item
          label="邮箱"
          label-width="120px"
        >
          <el-input
            v-model="emailForm.email"
            placeholder="请输入邮箱"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item
          label="验证码"
          label-width="120px"
        >
          <div class="flex w-full gap-4">
            <el-input
              v-model="emailForm.code"
              class="flex-1"
              placeholder="请自行设计邮件服务，此处为模拟随便写"
              autocomplete="off"
              style="width:300px"
            />
            <el-button
              type="primary"
              :disabled="emailTime>0"
              @click="getEmailCode"
            >{{ emailTime>0?`(${emailTime}s)后重新获取`:'获取验证码' }}</el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button

            @click="closeChangeEmail"
          >取消</el-button>
          <el-button
            type="primary"

            @click="changeEmail"
          >更改</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog
      v-model="showPassword"
      title="修改密码"
      width="360px"
      @close="clearPassword"
    >
      <el-form
        ref="modifyPwdForm"
        :model="pwdModify"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item
          :minlength="6"
          label="原密码"
          prop="password"
        >
          <el-input
            v-model="pwdModify.password"
            show-password
          />
        </el-form-item>
        <el-form-item
          :minlength="6"
          label="新密码"
          prop="newPassword"
        >
          <el-input
            v-model="pwdModify.newPassword"
            show-password
          />
        </el-form-item>
        <el-form-item
          :minlength="6"
          label="确认密码"
          prop="confirmPassword"
        >
          <el-input
            v-model="pwdModify.confirmPassword"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button

            @click="showPassword = false"
          >取 消</el-button>
          <el-button

            type="primary"
            @click="savePassword"
          >确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看详情对话框-->

    <el-dialog
      v-model="isShowDetail"
      title="文化数据版权详情"
      width="720px"
    >
      <el-form
        align="left"
        label-width="80px"
      >
        
        <el-form-item
          label="创建时间:"
        >
          <div>
            {{ detail_data.CreatedAt }}
          </div>
        </el-form-item>

        <el-form-item
          label="更新时间:"
        >
          <div>
            {{ detail_data.UpdatedAt }}
          </div>
        </el-form-item>

        <el-form-item
          label="所有者:"
        >
          <div>
            {{ detail_data.owner }}
          </div>
        </el-form-item>

        <el-form-item
          label="存证机构:"
        >
          <div>
            {{ detail_data.certi_organization }}
          </div>
        </el-form-item>

        <el-form-item
          label="语义信息:"
        >
          <div>
            {{ detail_data.keywords }}
          </div>
        </el-form-item>

        <el-form-item
          label="版权标识:"
        >
          <div>
            {{ detail_data.biaoshi }}
          </div>
        </el-form-item>

        <el-form-item
          label="版权价值:"
        >
          <div>
            {{ detail_data.price }}
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button
            type="primary"
            @click="isShowDetail = false"
          >确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { setSelfInfo, changePassword } from '@/api/user.js'
import { reactive, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore} from '@/pinia/modules/user'
import SelectImage from '@/components/selectImage/selectImage.vue'
import { SelectByUser, getDetail } from '@/api/fileUploadAndDownload'
import { formatDate } from '@/utils/format'
import CustomPic from '@/components/customPic/index.vue'

defineOptions({
  name: 'Person',
})



const activeName = ref('second')
const rules = reactive({
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '最少6个字符', trigger: 'blur' },
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '最少6个字符', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请输入确认密码', trigger: 'blur' },
    { min: 6, message: '最少6个字符', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== pwdModify.value.newPassword) {
          callback(new Error('两次密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
})


const userStore = useUserStore()
const modifyPwdForm = ref(null)
const showPassword = ref(false)
const isShowDetail = ref(false)
const pwdModify = ref({})
const nickName = ref('')
const editFlag = ref(false)
const savePassword = async() => {
  modifyPwdForm.value.validate((valid) => {
    if (valid) {
      changePassword({
        password: pwdModify.value.password,
        newPassword: pwdModify.value.newPassword,
      }).then((res) => {
        if (res.code === 0) {
          ElMessage.success('修改密码成功！')
        }
        showPassword.value = false
      })
    } else {
      return false
    }
  })
}

const clearPassword = () => {
  pwdModify.value = {
    password: '',
    newPassword: '',
    confirmPassword: '',
  }
  modifyPwdForm.value.clearValidate()
}

watch(() => userStore.userInfo.headerImg, async(val) => {
  const res = await setSelfInfo({ headerImg: val })
  if (res.code === 0) {
    userStore.ResetUserInfo({ headerImg: val })
    ElMessage({
      type: 'success',
      message: '设置成功',
    })
  }
})

const openEdit = () => {
  nickName.value = userStore.userInfo.nickName
  editFlag.value = true
  console.log(userStore.userInfo)
}

const closeEdit = () => {
  nickName.value = ''
  editFlag.value = false
}

const enterEdit = async() => {
  const res = await setSelfInfo({
    nickName: nickName.value
  })
  if (res.code === 0) {
    userStore.ResetUserInfo({ nickName: nickName.value })
    ElMessage({
      type: 'success',
      message: '设置成功',
    })
  }
  nickName.value = ''
  editFlag.value = false
}

const handleClick = (tab, event) => {
  console.log(tab, event)
}

const changePhoneFlag = ref(false)
const time = ref(0)
const phoneForm = reactive({
  phone: '',
  code: ''
})

const getCode = async() => {
  time.value = 60
  let timer = setInterval(() => {
    time.value--
    if (time.value <= 0) {
      clearInterval(timer)
      timer = null
    }
  }, 1000)
}

const closeChangePhone = () => {
  changePhoneFlag.value = false
  phoneForm.phone = ''
  phoneForm.code = ''
}

const changePhone = async() => {
  const res = await setSelfInfo({ phone: phoneForm.phone })
  if (res.code === 0) {
    ElMessage.success('修改成功')
    userStore.ResetUserInfo({ phone: phoneForm.phone })
    closeChangePhone()
  }
}

const changeEmailFlag = ref(false)
const emailTime = ref(0)
const emailForm = reactive({
  email: '',
  code: ''
})

const getEmailCode = async() => {
  emailTime.value = 60
  let timer = setInterval(() => {
    emailTime.value--
    if (emailTime.value <= 0) {
      clearInterval(timer)
      timer = null
    }
  }, 1000)
}

const closeChangeEmail = () => {
  changeEmailFlag.value = false
  emailForm.email = ''
  emailForm.code = ''
}

const changeEmail = async() => {
  const res = await setSelfInfo({ email: emailForm.email })
  if (res.code === 0) {
    ElMessage.success('修改成功')
    userStore.ResetUserInfo({ email: emailForm.email })
    closeChangeEmail()
  }
}

  const page = ref(1)
  const total = ref(0)
  const pageSize = ref(3)
  const tableData = ref([])
  const detail_data = ref([])

  // 分页
  const handleSizeChange = (val) => {
    pageSize.value = val
    getTableData()
  }
  
  const handleCurrentChange = (val) => {
    page.value = val
    getTableData()
  }
  
  // 查询
  const getTableData = async() => {
    const table = await SelectByUser({ page: page.value, pageSize: pageSize.value, keyword: userStore.userInfo.nickName })
    console.log(table)
    if (table.code === 0) {
      tableData.value = table.data.list
      total.value = table.data.total
      page.value = table.data.page
      pageSize.value = table.data.pageSize
    }
  }
  getTableData()

  const showDetail = async(url) => {
    const detail = await getDetail(url)
    if (detail.code === 0) {
      detail_data.value = detail.data.filedetail
      isShowDetail.value = true
    }
  }


</script>

<style lang="scss">
.borderd {
  @apply border-b-2 border-solid border-gray-100 border-t-0 border-r-0 border-l-0;
    &:last-child{
      @apply border-b-0;
    }
 }

.info-list{
  @apply w-full whitespace-nowrap overflow-hidden text-ellipsis py-3 text-lg text-gray-700
}

</style>
