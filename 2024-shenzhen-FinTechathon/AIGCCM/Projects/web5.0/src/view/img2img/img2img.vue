<template>
  <div>
    <div class="gva-search-box">
      <el-form ref="elSearchFormRef" :inline="true" :model="searchInfo" class="demo-form-inline" :rules="searchRule" @keyup.enter="onSubmit">
      <el-form-item label="创建日期" prop="createdAt">
      <template #label>
        <span>
          参考图片
          <el-tooltip content="用于辅助AIGC作品风格确定">
            <el-icon><QuestionFilled /></el-icon>
          </el-tooltip>
        </span>
        <div class="demo-image__placeholder">
  <div class="block">
    <span class="demonstration">检索导入</span>
  </div>
</div>
      </template>
      <!-- <el-input v-model="formInline.user" placeholder="关键词" style="width: 150px;"></el-input>    -
      <el-select v-model="formInline.region" placeholder="来源" style="width: 150px;">
      <el-option label="原创作品" value="original"></el-option>
      <el-option label="AIGC作品" value="aigc"></el-option>
     </el-select> -->
      </el-form-item>
        <!-- <el-form-item>
          <el-button type="primary" icon="search" @click="onSubmit">查询</el-button>
          <el-button icon="refresh" @click="onReset">重置</el-button>
        </el-form-item> -->
      </el-form>
      

  <div style="display: block; border-right: none;">
  <div style="padding: 30px 0;text-align: center;border-right: 1px solid #eff2f6;
    display: inline-block;width: 20%;
    box-sizing: border-box;vertical-align: top;" v-for="pic in pics" :key="fit">
    <span style=" display: block;
    color: #8492a6;
    font-size: 14px;
    margin-bottom: 20px;">{{pic.name}}</span>
    <el-image
      style="width: 100px; height: 100px"
      :src="pic.url"
      :fit="contain"></el-image>
  </div>
</div>


    </div>
    <div class="gva-search-box">
      <div> 图片生成 </div>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
  <el-form-item label="作品名称" prop="name">
    <el-input v-model="ruleForm.name"></el-input>
  </el-form-item>
  <el-form-item label="描述词" prop="desc">
    <el-input v-model="ruleForm.desc"></el-input>
  </el-form-item>
  <el-form-item label="反向描述词" prop="rdesc">
    <el-input v-model="ruleForm.rdesc"></el-input>
  </el-form-item>

<el-form-item label="生成数量" required>
<el-slider
v-model="ruleForm.number" :min="1" :max="10"
show-input>
</el-slider>
</el-form-item>

  <el-form-item label="输出分辨率" required>
      宽度：
      <el-slider 
      v-model="ruleForm.weight" :min="960" :max="2048" 
      show-input>
    </el-slider>
    高度:
    <el-slider
      v-model="ruleForm.height" :min="960" :max="2048"
      show-input>
    </el-slider>
  </el-form-item>
  生成式模型：
  <el-form-item label="" prop="region" >
    <el-select v-model="ruleForm.region" placeholder="请选择生成模型" style="width:300px;">
      <el-option label="realcartoon3d_ v11.safetensors [408a4d0253]" value="realcartoon3d_ v11.safetensors [408a4d0253]"></el-option>
      <el-option label="realismFromHades_v81HQ.safetensors [fb4cdf649b]" value="realismFromHades_v81HQ.safetensors [fb4cdf649b]"></el-option>
      <el-option label="v1-5-pruned-emaonly.ckpt[cc6cb27103]" value="v1-5-pruned-emaonly.ckpt[cc6cb27103]"></el-option>
    </el-select>
  </el-form-item>

<el-form-item label="种子" prop="seed">
    <el-input v-model="ruleForm.seed"></el-input>
  </el-form-item>

  <el-form-item label="步数" prop="steps">
    <el-slider
      v-model="ruleForm.steps" :min="1" :max="100"
      show-input>
    </el-slider>
  </el-form-item>
  <el-form-item label="文本引导强度" prop="cfg_scale">
    <el-slider
      v-model="ruleForm.cfg_scale" :min="1" :max="10"
      show-input>
    </el-slider>
  </el-form-item>


  <div style="display: block; border-right: none;" v-if="out_show">

  <el-image v-for="pic in out_pics" :key="url" :src="pic" lazy></el-image>

</div>



<div class="block"
v-else>
    <el-empty :image-size="100"></el-empty>
      </div>

  <el-form-item>
    <el-button type="primary"
@click="txt2imgFunc()">开始生成</el-button>
    <el-button @click="BuyAigc()">购买</el-button>
  </el-form-item>
</el-form>
    </div>

    <!--购买弹窗-->
    <el-dialog
      v-model="isBuy"
      title="是否确认购买"
      color="red"
      align="center"
      width="720px"
    >
    <div class="dialog-footer">
          <el-button

            @click="cancel"
          >取 消</el-button>
          <el-button

            type="primary"
            @click="confirm"
          >确 定</el-button>
        </div>
    </el-dialog>

  </div>
</template>

<script setup>
import {
  createImg2img,
  deleteImg2img,
  deleteImg2imgByIds,
  updateImg2img,
  findImg2img,
  getImg2imgList,
  txt2img
} from '@/api/img2img'
import { getUrl } from '@/utils/image'
// 图片选择组件
import SelectImage from '@/components/selectImage/selectImage.vue'

// 全量引入格式化工具 请按需保留
import { getDictFunc, formatDate, formatBoolean, filterDict, ReturnArrImg, onDownloadFile } from '@/utils/format'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref, reactive } from 'vue'
import axios from 'axios'
import router from '@/router/index'
import { doPurchase } from '@/api/fileUploadAndDownload'
import { useUserStore} from '@/pinia/modules/user'

defineOptions({
    name: 'Img2img'
})

const userStore = useUserStore()

// 自动化生成的字典（可能为空）以及字段
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
const page = ref()
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
  const table = await getImg2imgList({ page: page.value, pageSize: pageSize.value, ...searchInfo.value })
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
            deleteImg2imgFunc(row)
        })
    }


// 批量删除控制标记
const deleteVisible = ref(false)

// 多选删除
const onDelete = async() => {
      const ids = []
      if (multipleSelection.value.length === 0) {
        ElMessage({
          type: 'warning',
          message: '请选择要删除的数据'
        })
        return
      }
      multipleSelection.value &&
        multipleSelection.value.map(item => {
          ids.push(item.ID)
        })
      const res = await deleteImg2imgByIds({ ids })
      if (res.code === 0) {
        ElMessage({
          type: 'success',
          message: '删除成功'
        })
        if (tableData.value.length === ids.length && page.value > 1) {
          page.value--
        }
        deleteVisible.value = false
        getTableData()
      }
    }

// 行为控制标记（弹窗内部需要增还是改）
const type = ref('')

// 更新行
const updateImg2imgFunc = async(row) => {
    const res = await findImg2img({ ID: row.ID })
    type.value = 'update'
    if (res.code === 0) {
        formData.value = res.data.reimg2img
        dialogFormVisible.value = true
    }
}


// 删除行
const deleteImg2imgFunc = async (row) => {
    const res = await deleteImg2img({ ID: row.ID })
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


// 查看详情控制标记
const detailShow = ref(false)


// 打开详情弹窗
const openDetailShow = () => {
  detailShow.value = true
}


// 打开详情
const getDetails = async (row) => {
  // 打开弹窗
  const res = await findImg2img({ ID: row.ID })
  if (res.code === 0) {
    formData.value = res.data.reimg2img
    openDetailShow()
  }
}


// 关闭详情弹窗
const closeDetailShow = () => {
  detailShow.value = false
  formData.value = {
          desc: '',
          rdesc: '',
          number: '',
          height: '',
          width: '',
          seed: '',
          steps: '',
          cfg_scale: '',
          picurl: '',
          outputurl: '',
          }
}


// 打开弹窗
const openDialog = () => {
    type.value = 'create'
    dialogFormVisible.value = true
}

// 关闭弹窗
const closeDialog = () => {
    dialogFormVisible.value = false
    formData.value = {
        desc: '',
        rdesc: '',
        number: '',
        height: '',
        width: '',
        seed: '',
        steps: '',
        cfg_scale: '',
        picurl: '',
        outputurl: '',
        }
}
// 弹窗确定
const enterDialog = async () => {
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
                closeDialog()
                getTableData()
              }
      })
}
// 更新行

const cancel = () => {
    isBuy = false
    ElMessage({
      type: 'warning',
      message: '交易取消!',
      duration: 1000,
    });
  }

  const purchase = async(data) => {
    isBuy = true
    const purchase_data = {
      Buyer: userStore.userInfo.nickName,
      Seller: pics.owner,
      Work: pics.url,
      Goods: pics.price
    }
    const ret = await doPurchase(purchase_data)
    return ret.code
  }

const confirm = async() => {
    const ret_code = await purchase(pics)
    console.log(tmp)
    if (ret_code === 0) {
      ElMessage({
      type: 'success',
      message: '交易成功！!',
      duration: 1000,
    });
      isBuy = false
    }
    else {
      ElMessage({
      type: 'error',
      message: '交易失败！!',
      duration: 1000,
    });
      isBuy = false
      //失败
    }
    
  }

</script>


<script>
  export default {
    data() {
      return {
        fits: ['fill', 'contain', 'cover', 'none', 'scale-down'],
        out_show:false,
        isBuy:false,
        keywords:'',
        pics:[],
        out_pics:[],
        formInline: {
          user: '',
          region: ''
        },
        ruleForm: {
          name: '',
          desc: '',
          rdesc: '',
          number:1,
          height: 960,
          weight: 960,
          seed: 2,
          steps: 2,
          cfg_scale: 2,
          delivery: false,
          type: [],
          resource: '',
          desc: ''
        },
        rules: {
          name: [
            { required: true, message: '请输入活动名称', trigger: 'blur' },
            { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
          region: [
            { required: true, message: '请选择活动区域', trigger: 'change' }
          ],
          number: [
            {  required: true, message: '请选择生成数量', min: 1, max: 10}
          ],
          steps: [
            {  required: true, message: '请选择生成步数', min: 1, max: 10}
          ],
          cfg_scale: [
            {  required: true, message: '请选择文本引导强度', min: 1, max: 10}
          ],
          weight: [
            {  required: true, message: '请选择宽度', min: 960, max: 2048}
          ],
          height: [
            { required: true, message: '请选择高度', min: 960, max: 2048 }
          ],
          type: [
            { type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
          ],
          resource: [
            { required: true, message: '请选择活动资源', trigger: 'change' }
          ],
          desc: [
            { required: true, message: '请填写活动形式', trigger: 'blur' }
          ]
        }
      }
    },
    mounted() {
    console.log(this.$route.query)
    const req= history.state.req;
    this.pics = req.data
    this.keywords = req.keywords
    console.log(this.pics)
  },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!');
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      BuyAigc(formName) {
        ElMessage({
        type: 'success',
        message: '交易成功！!',
        duration: 1000,
        })
        this.$refs[formName].resetFields();
      },
      txt2imgFunc() {
        console.log(this.out_show);
        let that = this
        console.log(that.out_show);
      axios.defaults.baseURL = "/sdapi";
      let tpic=[]
      let n=that.number
      axios.post('/v1/txt2img', {
        "prompt":  this.keywords+this.ruleForm.desc,"negative_prompt":this.ruleForm.rdesc,"seed": this.ruleForm.seed,
  "batch_size": this.ruleForm.number,
  "n_iter": 1,
  "steps": this.ruleForm.steps,
  "cfg_scale": this.ruleForm.cfg_scale,
  "width": this.ruleForm.weight,
  "height": this.ruleForm.height})
        .then(function (response) {
          console.log(response.data.images)
          n=response.data.images.length
          for (let i = 0; i < n; i++) {
            var p='data:image/png;base64,'
            p+=response.data.images[i]
            tpic.push(p)
            console.log(p)
            console.log(tpic)
          }
          console.log(tpic)
          that.out_pics=tpic
          that.out_show = true
        })
        .catch(function (error) {
          console.log(error);
        });
    }
    },
  }
</script>

<style>

</style>
