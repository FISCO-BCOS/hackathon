<template>
  <div id="loan-application">
    <div>
      <center>
        博弈后：
        <el-tag>充电桩单位电价:3.26</el-tag>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <el-tag>电网公司电价抽成比例:17%</el-tag>
      </center>
    </div>
    <el-table :data="tableData" ref="table" height="70vh" style="width: 100%;">
      <el-table-column align="center" type="index" label="序号" width="100">
        1
      </el-table-column>
      <el-table-column align="center" prop="name" label="用户名" width="160">
        廖松
      </el-table-column>
      <el-table-column align="center" prop="carId" label="车牌号" width="150">
        津N 235
      </el-table-column>
      <el-table-column align="center" prop="totelEle" label="汽车电池容量" width="150">
        45kw
      </el-table-column>
      <el-table-column align="center" prop="remainEle" label="汽车剩余电量" width="150">
        65%
      </el-table-column>
      <el-table-column align="center" prop="money" label="剩余金额(元)" width="150">
        103.3
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template v-slot="{row}">
          <el-select v-model="value" placeholder="请选择充电桩"
                     @change="changeDevice(row)">
            <el-option
                v-for="item in devicesOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
        :current-page.sync="firstQuery.pageNo"
        :page-size.sync="firstQuery.pageSize"
        :total="rows">
    </Pagination>
  </div>
</template>

<script>
import {
  queryUser
} from '@/api/api'

export default {
  data() {
    return {
      firstQuery: {
        pageNo: 1,
        pageSize: 10,
        name: ''
      },
      tableData: [{'name': "zhangsan"}],
      rows: 1,
      temp: {},
      dialogFormVisible: false,
      devicesOptions: [{
        label: "充电桩4",
        value: "充电桩4"
      }, {
        label: "充电桩5",
        value: "充电桩5"
      }],
      value: ''
    }
  },
  methods: {
    query(row) {
      this.dialogFormVisible = true;
      this.temp = row;
    },
    evaluate(row) {
      alert("这是信用评估：" + row)
    },
    changeDevice(row) {
      this.$confirm('是否提交交易？', '温馨提示', {
        confirmButtonText: '确定',
        cancelBurronText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$message({
          type: 'success',
          message: '提交成功',
          showClose: true
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '提交已取消',
          showClose: true
        })
      })
    },
    getNormalList() {
      queryUser({'id': 'node_2'}).then(res => {
        console.log(res);
        this.tableData = res.data.data.data
      })
    }
  },
  created() {
  },
  mounted() {
    this.getNormalList();
  },
}
</script>
