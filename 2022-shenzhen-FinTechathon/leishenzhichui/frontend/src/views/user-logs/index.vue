<template>
  <div class="management">
    <div class="filter-container">
      <el-input prefix-icon="el-icon-search" v-model="listQuery.name" @input="searchInput" clearable placeholder="请输入姓名"
                style="width: 200px;" class="filter-item"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" style="margin-left: 10px">
        搜索
      </el-button>
    </div>

    <el-table :data="tableData" ref="table" height="70vh" style="width: 100%;">
      <el-table-column align="center" type="index" label="序号" width="50"></el-table-column>
      <el-table-column align="center" prop="name" label="姓名" width="120"></el-table-column>
      <el-table-column align="center" prop="birthday" label="出生日期" width="120">
        <template v-slot="{row}">
          <span>{{ row.birthday | getDate }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="sex" label="性别" width="80">
        <template v-slot="{row}">
          <span>{{ row.sex | getSex }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="mobile_phone" label="手机号" width="120"></el-table-column>
      <el-table-column align="center" prop="address1" label="居住地址" width="200"></el-table-column>
      <el-table-column align="center" prop="status" label="行为操作" >
        <template v-slot="{row}">
          <el-tag :type="row.status | getStatusStyle" effect="plain">{{ row.status | getStatus }}</el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column align="center" label="操作">-->
<!--        <template slot-scope="{row}">-->
<!--          <el-button size="mini" type="primary" @click="editRow(row)"-->
<!--                     :disabled="row.status != 0 && row.status != 3 && row.status != 6">编辑-->
<!--          </el-button>-->
<!--          <el-button size="mini" type="danger" @click="delRow(row)"-->
<!--                     :disabled="row.status != 0 && row.status != 3 && row.status != 6">删除-->
<!--          </el-button>-->
<!--          <el-button size="mini" type="success" @click="submit(row)"-->
<!--                     :disabled="row.status != 0 && row.status != 3 &&row.status != 6">提交审核-->
<!--          </el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <!-- 编辑 -->
<!--    <el-dialog title="申请管理-编辑" :visible.sync="dialogFormVisible" center>-->
<!--      <el-form size="small" ref="updateForm" :model="temp" label-position="left" label-width="70px"-->
<!--               style="margin-left:20%;">-->
<!--        <el-form-item label="ID" prop="id" v-show="false">-->
<!--          <el-input v-model="temp.id"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="姓名" prop="name">-->
<!--          <el-input v-model="temp.name" style="width: 60%"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="手机号" prop="mobile_phone">-->
<!--          <el-input v-model.number="temp.mobile_phone" style="width: 60%"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="居住地址" prop="address1">-->
<!--          <el-input v-model="temp.address1" style="width: 60%"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="出生日期" prop="birthday">-->
<!--          <el-date-picker v-model="temp.birthday" style="width: 60%"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="性别" prop="sex">-->
<!--          <el-select v-model="temp.sex" class="filter-item" placeholder="Please select" style="width: 60%">-->
<!--            <el-option v-for="item in sexOptions" :key="item.key" :label="item.display_name" :value="item.key"/>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="教育程度" prop="education">-->
<!--          <el-select v-model="temp.education" class="filter-item" placeholder="Please select" style="width: 60%">-->
<!--            <el-option v-for="item in educationOptions" :key="item.key" :label="item.display_name" :value="item.key"/>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="dialogFormVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="updateData()">确 定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->

    <!-- 分页 -->
    <Pagination
        :current-page.sync="listQuery.pageNo"
        :page-size.sync="listQuery.pageSize"
        :total="rows"
        @getList="getList">
    </Pagination>
  </div>
</template>

<script>
import {loanList, submitToApprove, deleteRow, updateRow} from '@/api/api.js'
import {sexOptions, educationOptions} from '@/utils/getData.js'
import Pagination from '@/components/pagination'

export default {
  components: {
    Pagination
  },
  data() {
    return {
      sexOptions,
      educationOptions,
      tableData: [],
      listQuery: {
        pageNo: 1, // 页码
        pageSize: 10, // 每页条数
        name: '' // 查询条件
      },
      rows: 1, //总的条数
      dialogFormVisible: false,
      temp: {}
    }
  },
  methods: {
    getList() {
      loanList(this.listQuery).then(res => {
        let {code} = res.data;
        if (code === 20000) {
          let {data, rows, pages} = res.data.data.data;
          this.tableData = data;
          this.rows = rows;
        }
      })
    },
    submit(row) {
      submitToApprove({id: row.id}).then(res => {
        this.getList();
        this.$notify({
          type: 'success',
          title: '提交审核',
          message: '提交成功',
          duration: 2000
        })
      })
    },
    delRow(row) {
      this.$confirm('此操作将永久删除该数据，是否继续？', '申请管理-删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRow({id: row.id}).then(res => {
          console.log(res);
          let {code} = res.data;
          if (code === 20000) {
            this.getList();
            this.$message({
              type: 'success',
              message: '删除成功',
              duration: 2000,
              showClose: true
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '删除已取消！',
          duration: 2000,
          showClose: true
        })
      })
    },
    editRow(row) {
      this.dialogFormVisible = true;
      this.temp = {...row};
    },
    updateData() {
      updateRow(this.temp).then(res => {
        let {code} = res.data;
        if (code === 20000) {
          this.getList();
          this.$message({
            type: 'success',
            message: '编辑成功',
            duration: 2000,
            showClose: true
          })
        }
        this.dialogFormVisible = false;
      })
    },
    searchInput() {
      // 搜索到的信息从第一页开始展示，如果大于一且搜索到的数据比较少就会没有数据展示
      this.listQuery.pageNo = 1;
      this.getList();
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
    },
    getStatus(data) {// 状态过滤
      switch (data) {
        case 0:
          return '等待审核';
        case 1:
          return '提交初审';
        case 2:
          return '初审通过';
        case 3:
          return '初审拒绝';
        case 4:
          return '提交终审';
        case 5:
          return '终审通过';
        case 6:
          return '终审拒绝';
        case 7:
          return '生成合同';
        default:
          return data;
      }
    },
    getStatusStyle(data) {// 状态颜色过滤
      switch (data) {
        case 0:
          return 'info';
        case 1:
          return 'warning';
        case 2:
          return 'success';
        case 3:
          return 'danger';
        case 4:
          return 'warning';
        case 5:
          return 'success';
        case 6:
          return 'danger';
        case 7:
          return '';
        default:
          return data;
      }
    }
  }
}
</script>
