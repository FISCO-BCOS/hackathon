<template>
  <div class="management">
    <div class="filter-container">
      <el-input prefix-icon="el-icon-search" v-model="listQuery.name" @input="searchInput" clearable
                placeholder="请输入设备名"
                style="width: 200px;" class="filter-item"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" style="margin-left: 10px">
        搜索
      </el-button>
    </div>

    <el-table :data="tableData" ref="table" height="70vh" style="width: 100%;">
      <el-table-column align="center" type="index" label="设备序号" width="100"></el-table-column>
      <el-table-column align="center" prop="name" label="设备名" width="100"></el-table-column>
      <el-table-column align="center" prop="parkid" label="车位号" width="120"></el-table-column>
      <el-table-column align="center" prop="station" label="充电站" width="100"></el-table-column>
      <el-table-column align="center" prop="price" label="充电费用" width="150"></el-table-column>
      <el-table-column align="center" prop="lon_lat" label="经纬度" width="120"></el-table-column>
      <el-table-column align="center" prop="on_off" label="是否工作" width="100">
        <template v-slot="{row}">
          <el-tag :type="row.on_off=== '0' ? 'info' : 'success' " effect='Dark'>{{
              row.on_off === "0" ? "关闭" : "开启"
            }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="status" label="状态" width="100">
        <template v-slot="{row}">
          <el-tag :type="row.status | getStatusStyle" effect="plain">{{ row.status | getStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="{row}">
          <el-button size="mini" type="primary" @click="editRow(row)"
                     :disabled="row.status != 0 && row.status != 1&&row.status != 5">编辑
          </el-button>
          <el-button size="mini" type="danger" @click="delRow(row)"
                     :disabled="row.status != 0 && row.status != 1&&row.status != 5">删除
          </el-button>
          <el-button size="mini" type="warning" @click="errorDetection(row)"
                     :disabled="row.status != 0 &&row.status != 1&&row.status != 5">故障检测
          </el-button>
          <el-button size="mini" type="success" @click="repair(row)"
                     :disabled="row.status != 2">提交运维
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑 -->
    <el-dialog title="充电桩管理-编辑" :visible.sync="dialogFormVisible" center>
      <el-form size="small" ref="updateForm" :model="temp" label-position="left" label-width="80px"
               style="margin-left:20%;">
        <el-form-item label="ID" prop="id" v-show="false">
          <el-input v-model="temp.id"/>
        </el-form-item>
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="temp.name" style="width: 60%"/>
        </el-form-item>
        <el-form-item label="充电站" prop="station">
          <el-input v-model.number="temp.station" style="width: 60%"/>
        </el-form-item>
        <el-form-item label="车位号" prop="parkid">
          <el-input v-model="temp.parkid" style="width: 60%"/>
        </el-form-item>
        <el-form-item label="充电费用" prop="price">
          <el-input v-model="temp.price" style="width: 60%"/>
        </el-form-item>
        <el-form-item label="经纬度" prop="lon_lat">
          <el-input v-model="temp.lon_lat" style="width: 60%"/>
        </el-form-item>
        <el-form-item label="开启/关闭" prop="on_off">
          <el-select style="margin-left: 18px" v-model="temp.on_off" class="filter-item" placeholder="Please select">
            <el-option v-for="item in on_offOptions" :key="item.key" :label="item.display_name"
                       :value="item.key"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateData()">确 定</el-button>
      </div>
    </el-dialog>

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
import {submitToApprove, deleteRow, updateRow, deviceList, commitRepair} from '@/api/api.js'
import {sexOptions, educationOptions, on_offOptions} from '@/utils/getData.js'
import Pagination from '@/components/pagination'

export default {
  components: {
    Pagination
  },
  data() {
    return {
      sexOptions,
      educationOptions,
      on_offOptions,
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
      deviceList().then(res => { //{'id': 'node_4'}
        let {code} = res.data;
        if (code === 20000) {
          let {data, rows, pages} = res.data.data;
          console.log(res.data);
          this.tableData = data;
          this.rows = rows;
        }
        this.tableData = res.data.data.data
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
      this.$confirm('此操作将永久删除该数据，是否继续？', '充电桩管理-删除', {
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
      this.$confirm('此操作将永久删除该数据，是否继续？', '充电桩管理-删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
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
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '删除已取消！',
          duration: 2000,
          showClose: true
        })
      })
    },
    searchInput() {
      // 搜索到的信息从第一页开始展示，如果大于一且搜索到的数据比较少就会没有数据展示
      this.listQuery.pageNo = 1;
      this.getList();
    },
    errorDetection(row) {
      alert("故障检测")
    },
    repair(row) {
      this.$confirm('提交运维，是否继续？', '充电桩管理-提交运维', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        commitRepair({'id': row.id}).then(res => {
          let {code} = res.data;
          if (code === 20000) {
            this.getList();
            this.$message({
              type: 'success',
              message: '提交成功',
              duration: 2000,
              showClose: true
            })
          }
          this.dialogFormVisible = false;
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消！',
            duration: 2000,
            showClose: true
          })
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
    },
    getStatus(data) {// 状态过滤
      switch (data) {
        case 0:
          return '等待检测';
        case 1:
          return '检测通过';
        case 2:
          return '出现故障';
        case 3:
          return '提交运维';
        case 4:
          return '等待维修';
        case 5:
          return '维修成功';
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
          return 'success';
        case 2: //发生故障
          return 'danger';
        case 3:
          return 'warning';
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
