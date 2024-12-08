<template>
    <div v-loading.fullscreen.lock="fullscreenLoading">
      <div class="gva-table-box">
        <div class="gva-btn-list">
          <el-input
            v-model="search.keyword"
            class="keyword"
            placeholder="请输入待查询关键词"
          />
          <el-button
            type="primary"
            icon="search"
            @click="getTableData"
          >查询</el-button>
        </div>
  
        <el-table :data="tableData" @selection-change="handleSelectionChange">
          <el-table-column
            align="center"
            label="预览"
            width="150"
          >
            <template #default="scope">
              <CustomPic
                pic-type="file"
                :pic-src="scope.row.faker"
                preview
              />
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="日期"
            prop="CreatedBy"
            width="200"
          >
            <template #default="scope">
              <div>{{ formatDate(scope.row.CreatedAt) }}</div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="语义信息"
            prop="name"
            width="200"
          >
            <template #default="scope">
              <div
                class="name"
                @click="editFileNameFunc(scope.row)"
              >{{ scope.row.keywords }}</div>
            </template>
          </el-table-column>
          
          <el-table-column
            align="center"
            label="标签"
            prop="tag"
            width="200"
          >
            <template #default="scope">
              <el-tag
                :type="scope.row.tag === 'jpg' ? 'primary' : 'success'"
                disable-transitions
              >{{ scope.row.tag }}
              </el-tag>
            </template>
          </el-table-column>


          <el-table-column
          align="center"
          label="详情"
          min-width="120"
        >
        <template #default="scope">
          <el-button
            type="primary"
            icon="view"
            @click="showDetail(scope.row.url)"
          >查看详情</el-button>
        </template>
          
        </el-table-column>

          <el-table-column
            align="center"
            label="操作"
            width="160"
          >
            <template #default="scope">
              <el-button
                icon="download"
                type="primary"
                link
                @click="storage_tmp(scope.row)"
              >购买</el-button>
            </template>
          </el-table-column>
      <!--勾选框-->
      <el-table-column type="selection">

      </el-table-column>
        </el-table>
        <div class="gva-pagination">
      
      <div class="center-button">

      <el-button  type="primary" @click="img_to_img">
        AIGC创作 &nbsp;&nbsp;&nbsp; <el-icon><goods /></el-icon> {{ aigc_set.length }}
      </el-button>
      </div>

          <el-pagination
            :current-page="page"
            :page-size="pageSize"
            :page-sizes="[10, 30, 50, 100]"
            :style="{ float: 'right', padding: '20px' }"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </div>
      </div>
      <!--详情弹窗-->
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

    <!--购买确认弹窗-->
    <el-dialog
      v-model="isBuy"
      title="是否确认购买"
      color="red"
      align="center"
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
  import { getFileList, deleteFile, editFileName, getDetail } from '@/api/fileUploadAndDownload'
  import { downloadImage } from '@/utils/downloadImg'
  import CustomPic from '@/components/customPic/index.vue'
  import UploadImage from '@/components/upload/image.vue'
  import UploadCommon from '@/components/upload/common.vue'
  import { formatDate } from '@/utils/format'
  import WarningBar from '@/components/warningBar/warningBar.vue'
  import { useUserStore} from '@/pinia/modules/user'
  import { doPurchase } from '@/api/fileUploadAndDownload'
  import router from '@/router/index'

  import { ref } from 'vue'
  import { ElMessage, ElMessageBox, ElNotification } from 'element-plus'
  

  defineOptions({
    name: 'Upload',
  })
  
  const userStore = useUserStore()

  const path = ref(import.meta.env.VITE_BASE_API)
  
  const imageUrl = ref('')
  const imageCommon = ref('')
  
  const page = ref(1)
  const total = ref(0)
  const pageSize = ref(10)
  const search = ref({})
  const tableData = ref([])
  const detail_data = ref([])
  const isShowDetail = ref(false)
  const isBuy = ref(false)
  const aigc_set = ref([])
  const tmp = ref([])
  
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
    console.log(search)
    const table = await getFileList({ page: page.value, pageSize: pageSize.value, ...search.value })
    if (table.code === 0) {
      tableData.value = table.data.list
      total.value = table.data.total
      page.value = table.data.page
      pageSize.value = table.data.pageSize
    }
  }
  getTableData()
  
  const deleteFileFunc = async(row) => {
    ElMessageBox.confirm('此操作将永久删除文件, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
      .then(async() => {
        const res = await deleteFile(row)
        if (res.code === 0) {
          ElMessage({
            type: 'success',
            message: '删除成功!',
          })
          if (tableData.value.length === 1 && page.value > 1) {
            page.value--
          }
          getTableData()
        }
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消删除',
        })
      })
  }
  
  const downloadFile = (row) => {
    if (row.url.indexOf('http://') > -1 || row.url.indexOf('https://') > -1) {
      downloadImage(row.url, row.name)
    } else {
      debugger
      downloadImage(path.value + '/' + row.url, row.name)
    }
  }
  
  /**
   * 编辑文件名或者备注
   * @param row
   * @returns {Promise<void>}
   */
  const editFileNameFunc = async(row) => {
    ElMessageBox.prompt('请输入文件名或者备注', '编辑', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /\S/,
      inputErrorMessage: '不能为空',
      inputValue: row.name
    }).then(async({ value }) => {
      row.name = value
      // console.log(row)
      const res = await editFileName(row)
      if (res.code === 0) {
        ElMessage({
          type: 'success',
          message: '编辑成功!',
        })
        getTableData()
      }
    }).catch(() => {
      ElMessage({
        type: 'info',
        message: '取消修改'
      })
    })
  }

  const showDetail = async(url) => {
    const detail = await getDetail(url)
    if (detail.code === 0) {
      detail_data.value = detail.data.filedetail
      isShowDetail.value = true
    }
  }

  const purchase = async(data) => {
    isBuy.value = true
    const purchase_data = {
      Buyer: userStore.userInfo.nickName,
      Seller: data.owner,
      Work: data.url,
      Goods: data.price
    }
    const ret = await doPurchase(purchase_data)
    return ret.code
  }

  const handleSelectionChange = (selection_data) => {
    console.log(selection_data)
    aigc_set.value = selection_data
  }

  const cancel = () => {
    isBuy.value = false
    ElMessage({
      type: 'warning',
      message: '交易取消!',
      duration: 1000,
    });
  }

  const confirm = async() => {
    const ret_code = await purchase(tmp._rawValue)
    console.log(tmp)
    if (ret_code === 0) {
      ElMessage({
      type: 'success',
      message: '交易成功！!',
      duration: 1000,
    });
      isBuy.value = false
    }
    else {
      ElMessage({
      type: 'error',
      message: '交易失败！!',
      duration: 1000,
    });
      isBuy.value = false
      //失败
    }
    
  }

  const storage_tmp = async(data) => {
    const detail = await getDetail(data.url)
    if (detail.code === 0) {
      detail_data.value = detail.data.filedetail
    }
    isBuy.value = true
    tmp.value = data
    console.log(tmp.value)
  }

  const img_to_img = () => {
    var pic = []
    let n=aigc_set._rawValue.length
    var keys=''
    for(let i=0;i<n;++i){
      keys+=';'+aigc_set._rawValue[i].keywords
      console.log(keys)
      pic.push({
        id:aigc_set._rawValue[i].ID,
        name:aigc_set._rawValue[i].name,
        url:"http://106.13.124.168:8080/api"+aigc_set._rawValue[i].url,
        owner:aigc_set._rawValue[i].owner,
        price:aigc_set._rawValue[i].price
      })   
    }
    const req = {    // 参数构建
        data: pic,
        keywords:keys,
      }
    const query = { id: "11" }
    const state = {req:req}
    router.push({ name: 'img2img', query, state })
  }
  </script>
  
  <style scoped>
  .name {
    cursor: pointer;
  }
  
  .center-button {
    margin-top: 50px;
  }
  </style>
  