<!-- tag!!! 是企业名字的标签信息 -->
<!-- json文件返回的信息与prop字段对应 -->
<!-- 分页需要后端信息返回关键字才好写成动态的，所以这里都写成静态的了 -->
<!-- 和v-model 字段息息相关！看errorlog页面中的searchForm即可 -->

<template>
  <div class="app-container scroll-y">
  <!--  查询条件   -->
  <el-form ref="tableConfig" inline="true" :model="searchParam" label-width="80px">
    <el-form-item label="账户名">
      <el-input v-model="searchParam.userName" clearable />
    </el-form-item>
    <el-form-item label="企业名称">
      <el-input v-model="searchParam.entName" clearable />
    </el-form-item>
    <el-form-item label="准入状态">
      <el-select v-model="searchParam.accessStatus" placeholder="请选择准入状态" >
        <el-option label="无需准入" value="0" />
        <el-option label="等待准入申请" value="1" />
        <el-option label="审批中" value="2" />
        <el-option label="审批拒绝" value="3" />
        <el-option label="审批通过" value="4" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="search">查询</el-button>
    </el-form-item>
  </el-form>
  <!-- el-scrollbar是滚动条 -->
  <el-scrollbar>
      <!----------------------------- 表单 ------------------------>
      <div>
          <el-table ref="tableRef" row-key="date" :data="tableData" style="width: auto">
              <!-- <el-table-column  prop="name" label="企业名称" width="auto" /> -->
              <el-table-column prop="userName" label="账户名称" width="auto" />
              <el-table-column prop="entName" label="企业名称" width="auto" />
              <!-- <el-table-column prop="accessStatus" label="准入审核状态" :formatter="formatter" width="auto" /> -->

              <el-table-column prop="accessStatus" label="准入状态" width="auto" :filters="[
                  { text: '无需准入', value: '0' },
                  { text: '等待准入申请', value: '1' },
                  { text: '审批中', value: '2' },
                  { text: '审批拒绝', value: '3' },
                  { text: '审批通过', value: '4' }
                ]" :filter-method="filterTag" filter-placement="bottom-end">

                    <template #default="scope">
                        <!-- 实现功能：表格做判断，后端返回数字值，根据数字写三元表达式 教程网址↓ -->
                        <!-- https://blog.csdn.net/cdd9527/article/details/126501032?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-126501032-blog-125242450.pc_relevant_3mothn_strategy_recovery&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-126501032-blog-125242450.pc_relevant_3mothn_strategy_recovery&utm_relevant_index=1 -->
                        <!-- 可以用于写是否通过质检的状态返回 -->
                        <!-- 要实现不同标签，不同颜色 -->
                        <el-tag
                            :type="tagType(scope.row.accessStatus)"
                            disable-transitions>{{ tagText(scope.row.accessStatus) }}</el-tag>
                        <!-- <el-tag type="type="scope.row.tag === 'home' ? '' : 'success'" disable-transitions>{{ scope.row.tag }}</el-tag> -->
                    </template>
              </el-table-column>

              <el-table-column prop="opt" label="操作" width="auto">
                <template #default="scope">
                  <div style="line-height: 1; font-size: 0;">
                    <el-button v-show="scope.row.accessStatus == '2'" type="success" @click="pass(scope.row.userName)">审批通过</el-button>
                    <el-button v-show="scope.row.accessStatus == '2'" type="danger" @click="reject(scope.row.userName)">审批拒绝</el-button>
                  </div>
                </template>
              </el-table-column>
          </el-table>
      </div>
  </el-scrollbar>
  <!----------------------------------------- 分页 --------------------------------------------->
  <!-- 此处需要用axiosReq请求信息，可以参考errorlog.vue 中分页的用法 -->
   <!-----关注 Errorlog 中 v-model的使用 ----------------->
  <el-affix position="bottom" :offset="20">
      <div class="columnCC mt2 ">
          <el-pagination :current-page="currentPage" :page-size="pageSize" :page-sizes="[10, 20, 30, 40]"
              :background="true" layout="total, sizes, prev, pager, next, jumper" :total="totalPage" :page-count="pageCount"
              @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
  </el-affix>
  <!----------------------------------------- 分页 --------------------------------------------->
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { ElTable } from 'element-plus'
import { useEntAccessStore } from '@/store/entAccess'


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
interface User {
  // date: string
  userName: string
  entName: string
  accessStatus: string
}
const tableRef = ref<InstanceType<typeof ElTable>>()

// 标签颜色
const tagType = (accessStatus) => {
  if (accessStatus == '0') {
    return 'info'
  }
  if (accessStatus == '1') {
    return 'info'
  }
  if (accessStatus == '2') {
    return 'warning'
  }
  if (accessStatus == '3') {
    return 'danger'
  }
  if (accessStatus == '4') {
    return 'success'
  }
}

// 枚举映射
const tagText = (accessStatus) => {
  if (accessStatus == '0') {
    return '无需准入'
  }
  if (accessStatus == '1') {
    return '等待准入申请'
  }
  if (accessStatus == '2') {
    return '审批中'
  }
  if (accessStatus == '3') {
    return '审批拒绝'
  }
  if (accessStatus == '4') {
    return '审批通过'
  }
}

// const formatter = (row: User, column: TableColumnCtx<User>) => {
//   return row.license
// }
const filterTag = (value: string, row: User) => {
  return row.accessStatus === value
}


// 列表数据
const tableData = reactive(new Array<User>)

const entAccessStore = useEntAccessStore()
// 调用查询接口
const search = () => {
  let param = {
    pageSize: pageSize.value,
    pageNo: currentPage.value,
    userName: searchParam.userName,
    entName: searchParam.entName,
    accessStatus: searchParam.accessStatus,
  }

  entAccessStore.entList(param)
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
          userName: data.userName,
          entName: data.entName,
          accessStatus: data.accessStatus
        })
      }
      // tableData.value = result
    })
    .catch((res) => {
      console.log("list error")
    })

}

const pass = (userName) => {
  let param = {
    userName: userName,
    remark: '审批通过'
  }
  entAccessStore.pass(param)
  .then((res: any) => {
      // 填充分页插件
      currentPage.value = 1
      search()
    })
    .catch((res) => {
      console.log("pass error")
    })
}

const reject = (userName) => {
  let param = {
    userName: userName,
    remark: '审批拒绝'
  }
  entAccessStore.reject(param)
  .then((res: any) => {
      // 填充分页插件
      currentPage.value = 1
      search()
    })
    .catch((res) => {
      console.log("reject error")
    })
}

const download = (userName) => {
  console.log('download' + userName)
  entAccessStore.download(userName)
  .then((res: any) => {
      console.log(res)
      let blob = res.data
      let url = window.URL.createObjectURL(blob); // 创建一个临时的url指向blob对象
      let a = document.createElement("a");
      a.href = url;
      a.download = userName;
      a.click();
      // 释放这个临时的对象url
      window.URL.revokeObjectURL(url); 
    })
    .catch((res) => {
      console.log("download error")
    })
}


// 初始化加载数据
// search()
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