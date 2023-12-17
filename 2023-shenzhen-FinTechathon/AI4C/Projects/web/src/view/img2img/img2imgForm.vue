<template>
  <div>
    <div class="gva-form-box">
      <el-form :model="formData" ref="elFormRef" label-position="right" :rules="rule" label-width="80px">
        <el-form-item label="描述词:" prop="desc">
          <el-input v-model="formData.desc" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="反向描述词:" prop="rdesc">
          <el-input v-model="formData.rdesc" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="生成数量:" prop="number">
          <el-input v-model="formData.number" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="高度:" prop="height">
          <el-input v-model="formData.height" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="宽度:" prop="width">
          <el-input v-model="formData.width" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="种子:" prop="seed">
          <el-input v-model="formData.seed" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="步数:" prop="steps">
          <el-input v-model="formData.steps" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="文本引导强度:" prop="cfg_scale">
          <el-input v-model="formData.cfg_scale" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="参考图片:" prop="pic">
          <SelectImage v-model="formData.pic" file-type="image"/>
       </el-form-item>
        <el-form-item label="输出:" prop="output">
          <SelectImage v-model="formData.output" file-type="image"/>
       </el-form-item>
        <el-form-item label="参考图片地址:" prop="picurl">
          <el-input v-model="formData.picurl" :clearable="true" placeholder="请输入" />
       </el-form-item>
        <el-form-item label="输出地址:" prop="outputurl">
          <el-input v-model="formData.outputurl" :clearable="true" placeholder="请输入" />
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
  createImg2img,
  updateImg2img,
  findImg2img
} from '@/api/img2img'

defineOptions({
    name: 'Img2imgForm'
})

// 自动获取字典
import { getDictFunc } from '@/utils/format'
import { useRoute, useRouter } from "vue-router"
import { ElMessage } from 'element-plus'
import { ref, reactive } from 'vue'
import SelectImage from '@/components/selectImage/selectImage.vue'

const route = useRoute()
const router = useRouter()

const type = ref('')
const formData = ref({
            desc: '',
            rdesc: '',
            number: '',
            height: '',
            width: '',
            seed: '',
            steps: '',
            cfg_scale: '',
            pic: "",
            output: "",
            picurl: '',
            outputurl: '',
        })
// 验证规则
const rule = reactive({
})

const elFormRef = ref()

// 初始化方法
const init = async () => {
 // 建议通过url传参获取目标数据ID 调用 find方法进行查询数据操作 从而决定本页面是create还是update 以下为id作为url参数示例
    if (route.query.id) {
      const res = await findImg2img({ ID: route.query.id })
      if (res.code === 0) {
        formData.value = res.data.reimg2img
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
               res = await createImg2img(formData.value)
               break
             case 'update':
               res = await updateImg2img(formData.value)
               break
             default:
               res = await createImg2img(formData.value)
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
