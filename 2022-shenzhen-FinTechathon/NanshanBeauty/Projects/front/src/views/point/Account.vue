<!-- 积分明细 -->
<template>
    <div class="app-container scroll-y">
    <!-- el-scrollbar是滚动条 -->

        <!----------------------------- 表单 ------------------------>
        <div class="search">
            <el-row>
                <el-col :span="4">
                    <el-descriptions title="" :column="2" size="default" border>
                        <el-descriptions-item label="我的总积分 "><span>{{ info.Socre }}</span></el-descriptions-item>
                    </el-descriptions>
                </el-col>
            </el-row>
            <el-table ref="tableRef" row-key="date" :data="tableData" style="width: flex">
                <el-table-column prop="Time" label="时间" width="flex" />
                <el-table-column prop="name" label="操作账户地址" width="flex" />
                <el-table-column prop="beforePoint" label="操作前积分" width="flex" />
                <el-table-column prop="afterPoint" label="操作后积分" width="flex" />
                <el-table-column prop="remark" label="操作备注" width="flex" />
            </el-table>
        </div>

    <!----------------------------------------- 分页 --------------------------------------------->
    <!-- 此处需要用axiosReq请求信息，可以参考errorlog.vue 中分页的用法 -->
    <!-----关注 Errorlog 中 v-model的使用 ----------------->
    <!-- <el-affix position="bottom" :offset="20">
        <div class="columnCC mt2 ">
            <el-pagination :current-page="currentPage" :page-size="pageSize" :page-sizes="[10, 20, 30, 40]"
                :background="true" layout="total, sizes, prev, pager, next, jumper" :total="totalPage"
                :page-count="pageCount" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>
    </el-affix> -->
    <!----------------------------------------- 分页 --------------------------------------------->
    </div>
</template>
  
<script lang="ts" setup>
import { ref } from 'vue'
import { ElTable } from 'element-plus'
import type { TableColumnCtx } from 'element-plus/es/components/table/src/table-column/defaults'
import { usePointStore } from '@/store/point'
import { stringLiteral } from '@babel/types';



// ===================分页=========================
// // 总条数
// const totalPage = ref(10)
// // 当前页数
// const currentPage = ref(1)
// // 总的大小
// const pageSize = ref(10)
// // 总页数
// const pageCount = ref(1)

// const handleSizeChange = (val: number) => {
//     // console.log(`${val} items per page`)
//     pageSize.value = val
//     search()
// }
// const handleCurrentChange = (val: number) => {
//     // console.log(`current page: ${val}`)
//     currentPage.value = val
//     search()
// }


// 列表数据
const tableData = reactive(new Array<User>)

const pointStore = usePointStore()

// 调用接口
const initPoint = () => {
    // let param = {
    //     Time: searchParam.userName,
    //     name: searchParam.userName,
    //     Type: searchParam.userName,
    //     ScoreChange: searchParam.userName,
    //     Surplus: searchParam.userName,
    // }

    pointStore.myPoint()
        .then((res: any) => {
            console.log("myPoint success" + res)
            info.Socre = res
        })
    pointStore.myPointLogs()
        .then((res: any) => {
            console.log("myPoint success")

            let result: Array<User> = new Array<User>()
            // 填充tabledata
            tableData.splice(0, tableData.length)
            for (let i = 0; i < res.length; i++) {
                let data = res[i]
                tableData.push({
                    // 积分交易时间、企业名称、交易类型、积分变动明细、剩余积分
                    Time: data.optTime,
                    name: data.addr,
                    beforePoint: data.beforePoint,
                    afterPoint: data.afterPoint,
                    remark: data.remark,
                })
            }

            // tableData.value = result
        })

}

// ===================分页=========================

// ===================列表=========================
// 我的总积分---此处应该有数据请求
let info = reactive({
    Socre: 50,
})

interface User {
    // date: string
    // 积分交易时间、企业名称、交易类型、积分变动明细、剩余积分
    Time: string,
    name: string,
    beforePoint: string,
    afterPoint: string,
    remark: string,
  
}
const tableRef = ref<InstanceType<typeof ElTable>>()

// 查询条件
const searchParam = reactive({
    userName: '',
    entName: '',
    accessStatus: ''
})

// 初始化加载数据
initPoint()



</script>
<style scoped lang="scss">
.el-row {
    margin-bottom: 20px;
}

.el-row:last-child {
    margin-bottom: 0;
}

.el-col {
    border-radius: 4px;
}

.grid-content {
    border-radius: 4px;
    min-height: 36px;
}

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