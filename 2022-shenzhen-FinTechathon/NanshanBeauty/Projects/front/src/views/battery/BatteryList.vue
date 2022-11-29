
<!-- json文件返回的信息与prop字段对应 -->
<template>
  <div class="app-container scroll-y">
    <!------------------------------- 查询【需要请求数据】---------------------------------------------->
    <div class="search">
      <div class="demo-input-size">
        <!-- 查询搜索过程和v-model 字段息息相关！关注errorlog页面中的searchForm -->
        <el-input v-model="input.BatteryNumber" class="w-50 m-2" size="large" placeholder="请输入电池编号"
          :prefix-icon="Search" clearable />
        <!-- 在searchBtnClick处调用axiosReq，请求电池编号数据,参考Errorlog 中 的serchBthClick的用法-->
        <el-button type="primary" size="" @click="searchBtnClick">查询</el-button>
        <!-- 电池溯源信息查询 -->
        <el-button type="success" size="" @click="searchBtnClick2">电池溯源信息查询</el-button>
      </div>
    </div>
    <!----------------------------- 溯源记录弹窗 -------------------------------------------------------->
    <el-drawer class="search scroll-y" v-model="table" title="电池溯源信息" direction="rtl" size="50%">
      <el-table class="search" :data="gridData" style="width: auto">
        <el-table-column property="address" label="操作账户" width="150" />
        <el-table-column property="remark" label="操作记录" width="200" />
        <el-table-column property="optTime" label="操作时间" width="200" />
      </el-table>
    </el-drawer>
    <!----------------------------- 溯源记录弹窗 -------------------------------------------------------->
    <!----------------------------- 表单 --------------------------------------------------------->
    <!-- el-scrollbar是滚动条 -->
   
      <div class="search">
        <el-table ref="tableRef" :data="tableData" style="width: auto">
          <el-table-column prop="id" label="电池编号" width="auto" />
          <!-- <el-table-column prop="BatteryHistory" label="历史流转记录" width="auto" /> -->
          <el-table-column prop="status" label="状态" width="auto" />
        </el-table>
      </div>
 

    <!----------------------------------------- 分页【需要请求数据】-------------------------------------------->
    <!-----关注 Errorlog 中 v-model的使用 ----------------->
    <!-- 此处需要用axiosReq请求信息，可以参考errorlog.vue 中分页的用法 -->

      <div class="columnCC mt2 " position="bottom" :offset="20">
        <el-pagination :current-page="currentPage" :page-size="pageSize" :page-sizes="[10, 20, 30, 40]"
          :background="true" layout="total, sizes, prev, pager, next, jumper" :total="totalPage" :page-count="pageCount"
          @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
   
    <!----------------------------------------- 分页 --------------------------------------------->
  </div>
</template>

<script lang="ts" setup>
import { ElTable } from 'element-plus'
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { reactive, } from 'vue'
import { ElDrawer, ElMessageBox } from 'element-plus'
import { useBatteryStore } from '@/store/battery'

const batteryStore = useBatteryStore()
// ===================溯源信息弹窗相关===========================
const table = ref(false)
const dialog = ref(false)
const loading = ref(false)
let timer

// 关闭按钮
const cancelForm = () => {
  loading.value = false
  dialog.value = false
  clearTimeout(timer)
}

const handleClose = (done) => {
  if (loading.value) {
    return
  }
  ElMessageBox.confirm('Do you want to submit?')
    .then(() => {
      loading.value = true
      timer = setTimeout(() => {
        done()
        // 动画关闭需要一定的时间
        setTimeout(() => {
          loading.value = false
        }, 400)
      }, 2000)
    })
    .catch(() => {
      // catch error
    })
}

// 数据dto
interface TraceInfo {
  address: string
  remark: string
  optTime: string
}

const gridData = reactive(new Array<TraceInfo>)
// ===================溯源信息弹窗相关===========================

// ===================搜索相关===========================
const searchBtnClick = () => {
  // 写请求，或者直接读列表的元素
  search()
}
const searchBtnClick2 = () => {
  // 让弹窗弹出来
  table.value = true
  // 写请求，或者直接读列表的元素,并将数据渲染到弹窗上的表单上
  let param = {
    id: input.BatteryNumber
  }
  batteryStore.traceInfo(param)
    .then((res: any) => {
      console.log("traceInfo success")
      gridData.splice(0, gridData.length)
      for (let i = 0; i < res.length; i++) {
        let data = res[i]
        gridData.push({
          address: data.address,
          remark: data.remark,
          optTime: data.optTime
        })
      }
    })
    .catch((res) => {
      console.log("traceInfo error")
    })
}
// 输入的数据与查询的数据进行绑定
const input = reactive({
  BatteryNumber: '',
})
// ===================搜索相关============================

// =================== 分页 =========================
// 总条数
const totalPage = ref(10)
// 当前页数
const currentPage = ref(1)
// 总的大小
const pageSize = ref(10)
// 总页数
const pageCount = ref(1)

const handleSizeChange = (val: number) => {
  // console.log(`${val} items per page`)
  pageSize.value = val
  search()
}
const handleCurrentChange = (val: number) => {
  // console.log(`current page: ${val}`)
  currentPage.value = val
  search()
}
// =================== 分页 =========================


// =================== 数据列表 =========================
// 数据dto
interface Battery {
  id: string
  status: any
}

const tagText = (batteryStatus) => {

  if (batteryStatus == '1') {
    return '待安全审查'
  }
  if (batteryStatus == '2') {
    return '正常'
  }
  if (batteryStatus == '3') {
    return '待回收'
  }
  if (batteryStatus == '4') {
    return '回收中'
  }
  if (batteryStatus == '5') {
    return '梯次利用'
  }
  if (batteryStatus == '6') {
    return '拆解'
  }
  if (batteryStatus == '7') {
    return '回收交易中'
  }
  if (batteryStatus == '8') {
    return '梯次利用交易中'
  }
}

const tableData = reactive(new Array<Battery>)

// 调用查询接口
const search = () => {
  let param = {
    pageSize: pageSize.value,
    pageNo: currentPage.value,
    id: input.BatteryNumber,
  }

  batteryStore.list(param)
    .then((res: any) => {
      console.log("list success")
      // 填充分页插件
      pageSize.value = res.pageSize
      totalPage.value = res.total
      pageCount.value = res.pageCount

      // 填充tabledata
      tableData.splice(0, tableData.length)
      for (let i = 0; i < res.data.length; i++) {
        let data = res.data[i]
        let status1 = tagText(data.status)
        tableData.push({
          id: data.id,
          status: status1,
        })
      }

    })
    .catch((res) => {
      console.log("list error")
    })

}


// 初始化加载数据
search()
// ===================数据列表=========================



</script>
<style scoped lang="scss">

</style>