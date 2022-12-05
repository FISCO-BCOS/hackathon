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
                        发起回收拆解交易
                    </h2>

                </el-header>
                <el-container>
                    <!-- <el-aside width="400px"></el-aside> -->
                    <el-main>
                        <!-- <div class="app-container" style="margin-top:0px;"> -->

                        <!-- </div> -->
                        <el-form ref="ruleFormRef" :model="ruleForm" label-position="top" status-icon :rules="rules"
                            class="demo-ruleForm" style="max-width: 460px">
                            <el-form-item>
                                <el-link type="primary" @click.prevent="download">下载上传模板</el-link>
                                <!-- <el-button type="primary" size=""> 下载上传模板 </el-button> -->
                            </el-form-item>
                            <el-form-item>
                                <el-upload ref="uploadRef" class="upload-demo" :on-change="(file) => changeHandler(file)"
                                    :auto-upload="false">
                                    <el-button type="primary"> 批量上传电池编号 </el-button>
                                </el-upload>
                            </el-form-item>

                            <el-form-item label="电池编号" prop="id">
                                <el-input v-model="ruleForm.id" clearable />
                            </el-form-item>

                            <el-radio-group v-model="diabaled" prop="hasTraget" size="large" @change="change">
                                <el-radio :label="true">指定回收商</el-radio>
                                <el-radio :label="false">不指定回收商</el-radio>
                            </el-radio-group>
                            <el-form-item label="指定回收商" prop="Recycler">
                                <el-input v-model="ruleForm.Recycler" :disabled="!diabaled" clearable />
                            </el-form-item>
                            <el-form-item label="最低成交金额/元" prop="MinimumTransactionAmount">
                                <el-input v-model="ruleForm.MinimumTransactionAmount" clearable />
                            </el-form-item>

                            <el-form-item label="期望成交金额/元" prop="ExceptTransactionAmount">
                                <el-input v-model="ruleForm.ExceptTransactionAmount" clearable />
                            </el-form-item>

                            <el-form-item label="竞价时间/天" prop="BiddingDay">
                                <el-input v-model="ruleForm.BiddingDay" clearable />
                            </el-form-item>
                        </el-form>
                        <!-- 选项 参考链接：https://element-plus.gitee.io/zh-CN/component/radio.html#单选框组-->
                        <!-- 通过 change 事件来响应变化，它会传入一个参数 value 来表示改变之后的值。 -->

                        <br />
                        <el-form-item>
                            <el-button round type="primary" @click="submitForm(ruleFormRef)">提交</el-button>
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
// 提交文件
import { UploadInstance, valueEquals } from 'element-plus'
import { useBatteryStore } from '@/store/battery'
const router = useRouter()

// //返回 
let ToList = () => {
    router.push(`/secondRecycle/myTrade`)
}
const uploadRef = ref<UploadInstance>()
const batteryStore = useBatteryStore()
let fileParam
let hasFile = false

const download = () => {
    batteryStore.downloadTemplate()
        .then((res: any) => {
            // 文件类型固定为excel
            console.log(res)
            let blob = res.data
            let url = window.URL.createObjectURL(blob); // 创建一个临时的url指向blob对象
            let a = document.createElement("a");
            a.href = url;
            a.download = "批量新增电池模板";
            a.click();
            // 释放这个临时的对象url
            window.URL.revokeObjectURL(url);
        })
        .catch((res) => {
            console.log("download error")
        })
}

const changeHandler = (file) => {
    fileParam = file
    hasFile = true
}

// 选项
const diabaled = ref(true)
const change = (params) => {
    const label = params;
    // change 可以得到label信息，也就是选项信息。
    console.log(label);

}
// handleregister 处理注册的方法：提交数据给后端【可参考登录处理方法】，返回一个弹框：注册信息已提交
const handleApply = (valid) => {
    //注册信息提交弹窗 
    if (valid) {
        let formData = new FormData()
        if (hasFile) {
            formData.append("file", fileParam.raw)
        }
        formData.append("batteryId", ruleForm.id)
        formData.append("hasTarget", "" + diabaled.value)
        formData.append("name", ruleForm.Recycler)
        formData.append("lowestAmt", ruleForm.MinimumTransactionAmount)
        formData.append("expectAmt", ruleForm.ExceptTransactionAmount)
        formData.append("bidDays", ruleForm.BiddingDay)
        batteryStore.secondRecycleApply(formData)
            .then(() => {
                console.log("handleApply success")
                ElMessage({ message: '录入成功。', type: 'success', })
                useCommon()
                    .sleep(700)
                    .then(() => {
                        ToList()
                    })
            })
            .catch((res) => {
                console.log("handleApply error")
            })
    } else {
        ElMessage.error('申请信息无法提交，请检查后重新提交。')
    }
}


// ↓ ↓ ======表单涉及的代码=======
const ruleFormRef = ref<FormInstance>()

// 电池编号 
const BatteryNumber = (rule: any, value: any, callback: any) => {
    // if (!value) {
    // return callback(new Error('请输入电池编号'))
    // } else {
    callback()
    // }
}

// 最低成交金额
const MinimumTransactionAmount = (rule: any, value: any, callback: any) => {
    if (!value) {
        return callback(new Error('请输入最低成交金额'))
    } else {
        callback()
    }
}


// 竞价时间
const BiddingDay = (rule: any, value: any, callback: any) => {
    if (!value) {
        return callback(new Error('请输入竞价时间/天'))
    } else {
        callback()
    }
}

const ruleForm = reactive({
    id: '',
    BiddingDay: '',
    Recycler: '',
    MinimumTransactionAmount: '',
    ExceptTransactionAmount: ''
})

const rules = reactive({
    BatteryNumber: [{ validator: BatteryNumber, trigger: 'blur' }],
    BiddingDay: [{ validator: BiddingDay, trigger: 'blur' }],
    // Recycler: [{ validator: Recycler, trigger: 'blur' }],
    MinimumTransactionAmount: [{ validator: MinimumTransactionAmount, trigger: 'blur' }],

})

// 提交表单的处理方法，注册信息接口
const submitForm = (formEl: FormInstance | undefined) => {

    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            console.log('submit!');
            // 提交后的信息弹窗方法
            handleApply(valid);
        } else {
            console.log('error submit!');
            handleApply(valid);
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
  