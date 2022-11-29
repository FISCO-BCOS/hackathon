<!-- 拆解回收-我的交易 -->
<!-- tag!!! 是企业名字的标签信息 -->
<!-- json文件返回的信息与prop字段对应 -->
<!-- 分页需要后端信息返回关键字才好写成动态的，所以这里都写成静态的了 -->
<!-- 和v-model 字段息息相关！看errorlog页面中的searchForm即可 -->

<template>
  <!-- el-scrollbar是滚动条 -->
  <div class="app-container scroll-y">
    <!----------------------------- 表单 ------------------------>
    <div class="search">
      <el-table ref="tableRef" row-key="date" :data="tableData" style="width: auto">
        <!-- <el-table-column  prop="name" label="企业名称" width="auto" /> -->
        <el-table-column prop="id" label="交易编号" width="auto" />
        <el-table-column prop="info" label="交易内容" width="auto" />
        <el-table-column prop="sellerName" label="交易卖方" width="auto" />
        <el-table-column prop="buyerName" label="交易买方" width="auto" />
        <el-table-column prop="amount" label="最终成交金额" width="auto" />
        <el-table-column prop="status" label="交易状态" width="auto" :filters="[
          { text: '竞价中', value: '1' },
          { text: '交易成功', value: '2' },
          { text: '撤回', value: '3' }
        ]" :filter-method="filterTag" filter-placement="bottom-end">

          <template #default="scope">
            <!-- 实现功能：表格做判断，后端返回数字值，根据数字写三元表达式 教程网址↓ -->
            <!-- https://blog.csdn.net/cdd9527/article/details/126501032?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-126501032-blog-125242450.pc_relevant_3mothn_strategy_recovery&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-126501032-blog-125242450.pc_relevant_3mothn_strategy_recovery&utm_relevant_index=1 -->
            <!-- 可以用于写是否通过质检的状态返回 -->
            <!-- 要实现不同标签，不同颜色 -->
            <el-tag :type="tagType(scope.row.status)" disable-transitions>{{ tagText(scope.row.status) }}</el-tag>
            <!-- <el-tag type="type="scope.row.tag === 'home' ? '' : 'success'" disable-transitions>{{ scope.row.tag }}</el-tag> -->
          </template>
        </el-table-column>
        <!-- <el-table-column prop="opt" label="交易内容" width="auto">
                <template #default="scope">
                  <div style="line-height: 1; font-size: 0;">
                    <el-button v-show="scope.row.accessStatus == '2'" type="info" @click="showDetail(scope.row.id)">点击查看</el-button>
                  </div>
                </template>
              </el-table-column> -->
      </el-table>
    </div>
    <!----------------------------------------- 分页 --------------------------------------------->
    <!-- 此处需要用axiosReq请求信息，可以参考errorlog.vue 中分页的用法 -->
    <!-----关注 Errorlog 中 v-model的使用 ----------------->

    <div class="columnCC mt2 " position="bottom" :offset="20">
      <el-pagination :current-page="currentPage" :page-size="pageSize" :page-sizes="[10, 20, 30, 40]" :background="true"
        layout="total, sizes, prev, pager, next, jumper" :total="totalPage" :page-count="pageCount"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

    <!----------------------------------------- 分页 --------------------------------------------->
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { ElTable } from 'element-plus'
import { useBatteryStore } from '@/store/battery'


// ===================分页=========================
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
// ===================分页=========================

// 查询条件
const searchParam = reactive({
  userName: '',
  entName: '',
  accessStatus: ''
})

// ===================列表=========================
interface Trade {
  // date: string
  id: number
  info: string
  sellerName: string
  buyerName: string
  amount: number
  status: string
}
const tableRef = ref<InstanceType<typeof ElTable>>()

// 标签颜色
const tagType = (status) => {
  if (status == '1') {
    return 'warning'
  }
  if (status == '2') {
    return 'success'
  }
  if (status == '3') {
    return 'danger'
  }
}

// 枚举映射
const tagText = (status) => {
  if (status == '1') {
    return '竞价中'
  }
  if (status == '2') {
    return '交易成功'
  }
  if (status == '3') {
    return '撤回'
  }
}

// const formatter = (row: User, column: TableColumnCtx<User>) => {
//   return row.license
// }
const filterTag = (value: string, row: Trade) => {
  return row.status === value
}


// 列表数据
const tableData = reactive(new Array<Trade>)

const batteryStore = useBatteryStore()
// 调用查询接口
const search = () => {
  let param = {
    pageSize: pageSize.value,
    pageNo: currentPage.value,
  }

  batteryStore.secondRecycleMyTrade(param)
    .then((res: any) => {
      console.log("list success")
      // 填充分页插件
      // pageSize.value = res.pageSize
      totalPage.value = res.total
      pageCount.value = res.pageCount

      // let result:Array<User> = new Array<User>()
      // 填充tabledata
      tableData.splice(0, tableData.length)
      for (let i = 0; i < res.data.length; i++) {
        let data = res.data[i]
        tableData.push({
          id: data.id,
          info: data.info,
          sellerName: data.sellerName,
          buyerName: data.buyerName,
          amount: data.tradeAmt,
          status: data.status
        })
      }
      // tableData.value = result
    })
    .catch((res) => {
      console.log("list error")
    })

}

// 初始化加载数据
search()
// ===================列表=========================



</script>
<style scoped lang="scss">
.demo-pagination-block+.demo-pagination-block {
  margin-top: 10px;
}

.demo-pagination-block .demonstration {
  margin-bottom: 16px;
}

.detail-container {
  flex-wrap: wrap;
}

.detail-container-item {
  min-width: 40%;
  margin-bottom: 20px;
}

.detailDialog-title {
  margin-bottom: 14px;
  font-weight: bold;
  font-size: 16px;
}

// 滚动条
.scrollbar-demo-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  margin: 10px;
  text-align: center;
  border-radius: 4px;
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}
</style>