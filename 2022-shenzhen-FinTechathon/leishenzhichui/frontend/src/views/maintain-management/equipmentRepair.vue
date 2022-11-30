<template>
  <div id="loan-application">
    <div class="filter-container">
      <el-input prefix-icon="el-icon-search" v-model="firstQuery.name" @input="searchInput" clearable
                placeholder="请输入姓名" style="width: 200px;" class="filter-item"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" style="margin-left: 10px">
        搜索
      </el-button>
    </div>
    <el-table :data="tableData" ref="table" height="70vh" style="width: 100%;">
      <el-table-column align="center" type="index" label="序号" width="100"></el-table-column>
      <el-table-column align="center" prop="name" label="姓名" width="160"></el-table-column>
      <el-table-column align="center" prop="staff_no" label="职工编号" width="150"></el-table-column>
      <el-table-column align="center" prop="award" label="维修奖励" width="150"></el-table-column>
      <el-table-column align="center" prop="" label="身份认证结果" width="150">
        <el-tag type="success" effect="dark">{{ "认证成功" }}</el-tag>
      </el-table-column>
      <el-table-column align="center" prop="device" label="维修设备ID" width="150">
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template v-slot="{row}">
          <el-select v-model="value" placeholder="请选择故障设备"
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

    <el-dialog title="申请管理-编辑" :visible.sync="dialogFormVisible" center>
      <el-descriptions title="用户信息">
        <el-descriptions-item label="ID">{{ temp.id }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ temp.name }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ temp.birthday | getDate }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ temp.sex | getSex }}</el-descriptions-item>
        <el-descriptions-item label="居住地址">{{ temp.address1 }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ temp.identity_card }}</el-descriptions-item>
        <el-descriptions-item label="教育程度">{{ temp.education | getEducation }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ temp.phone }}</el-descriptions-item>
        <el-descriptions-item label="收支">{{ temp.income }}k/月</el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="公司详情">
        <el-descriptions-item label="公司名称">{{ temp.company }}</el-descriptions-item>
        <el-descriptions-item label="公司地址">{{ temp.address3 }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ temp.position }}</el-descriptions-item>
        <el-descriptions-item label="公司电话">{{ temp.company_phone }}</el-descriptions-item>
        <el-descriptions-item label="公司邮箱">{{ temp.company_email }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="家庭联系人">
        <el-descriptions-item label="关系">{{ temp.contact }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ temp.contact_name }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ temp.contact_phone }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions title="工作证明人">
        <el-descriptions-item label="关系">{{ temp.contact2 }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ temp.contact2_name }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ temp.contact2_phone }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ temp.contact2_dep }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ temp.contact2_pos }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 分页 -->
    <Pagination
        :current-page.sync="firstQuery.pageNo"
        :page-size.sync="firstQuery.pageSize"
        :total="rows"
        @getList="getList">
    </Pagination>
  </div>
</template>

<script>
import {firstList, firstReject, firstPass, loanQuery, staffList, failureList, selectFailureList} from '@/api/api'
import Pagination from '@/components/pagination'

export default {
  components: {
    Pagination
  },
  data() {
    return {
      firstQuery: {
        pageNo: 1,
        pageSize: 10,
        name: ''
      },
      tableData: [],
      rows: 1,
      temp: {},
      dialogFormVisible: false,
      devicesOptions: [],
      value: ''
    }
  },
  methods: {
    getList() {
      /*获取职工列表*/
      staffList(this.firstQuery).then(res => {
        let {code} = res.data;
        if (code === 20000) {
          let {data, rows} = res.data.data;
          this.tableData = data;
          this.rows = rows;
        }
      })
      /*获取故障设备列表*/
      failureList().then(res => {
        for (let i = 0; i < res.data.data.data.length; i++) {
          this.devicesOptions.push({
            'value': res.data.data.data[i].id,
            'label': "设备名:" + res.data.data.data[i].name,
          })
        }
      })
    },
    searchInput() {
      this.firstQuery.pageNo = 1;
      this.getList();
    },
    reject(row) {
      this.$confirm('是否拒绝充电桩？', '充电桩审批-初审', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        firstReject({id: row.id}).then(res => {
          let {code} = res.data;
          if (code === 20000) {
            this.getList();
            this.$message({
              type: 'success',
              message: '成功拒绝',
              duration: 2000,
              showClose: true
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消',
          duration: 2000,
          showClose: true
        })
      })
    },
    pass(row) {
      this.$confirm('是否通过充电桩？', '充电桩审批-初审', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        firstPass({id: row.id}).then(res => {
          let {code} = res.data;
          if (code === 20000) {
            this.getList();
            this.$message({
              type: 'success',
              message: '充电桩申请-初审通过',
              duration: 2000,
              showClose: true
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消',
          duration: 2000,
          showClose: true
        })
      })
    },
    query(row) {
      this.dialogFormVisible = true;
      this.temp = row;
    },
    evaluate(row) {
      alert("这是信用评估：" + row)
    },
    changeDevice(row) {
      this.$confirm('是否提交维修设备？', '温馨提示', {
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
      selectFailureList({'staffId': row.id, 'deviceId': this.value}).then(res => {
        staffList(this.firstQuery).then(res => {
          let {code} = res.data;
          if (code === 20000) {
            let {data, rows} = res.data.data;
            this.tableData = data;
            this.rows = rows;
          }
        })
      })
    }
  },
  mounted() {
    this.getList();
  },
  filters: {
    getSex(data) { // 性别过滤
      switch (data) {
        case 'man':
          return '男';
        case 'woman':
          return '女';
        default:
          return data;
      }
    },
    getEducation(data) { // 学历过滤
      switch (data) {
        case 'college':
          return '大学';
        case 'highschool':
          return '高中';
        default:
          return data;
      }
    },
    getDate(date) { // 时间过滤
      const nDate = new Date(date);
      //将不满10的数字自动补0
      const year = nDate.getFullYear().toString().padStart(2, 0);
      const month = nDate.getMonth().toString().padStart(2, 0);
      const day = nDate.getDay().toString().padStart(2, 0);
      return year + '-' + month + '-' + day;
    }
  }
}
</script>
