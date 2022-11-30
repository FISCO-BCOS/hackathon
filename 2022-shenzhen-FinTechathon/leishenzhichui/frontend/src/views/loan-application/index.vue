<template>
  <div class="loan-application">
    <el-form status-icon :label-position="labelPosition" :model="temp" :rules="rules" ref="temp" label-width="100px">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>充电桩基本信息</span>
        </div>
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="充电桩设备名" prop="name">
              <el-input v-model="temp.name" placeholder="请输入充电桩名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="充电站ID" prop="station">
              <el-input v-model="temp.station" placeholder="充电站ID"/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="充电桩ID" prop="id">
              <el-input v-model="temp.id" placeholder="充电桩ID"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :xs="24" :sm="12" :md="12" :lg="12">
            <el-form-item label="充电桩功率" prop="workrate">
              <el-input v-model="temp.workrate" placeholder="充电桩功率"/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="10" :lg="10">
            <el-form-item label="经纬度" prop="lon_lat">
              <el-input v-model="temp.lon_lat" placeholder="经纬度"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :xs="24" :sm="12" :md="12" :lg="6">
            <el-form-item label="交流或直流" prop="ac_dc">
              <el-select v-model="temp.ac_dc" class="filter-item" placeholder="Please select">
                <el-option v-for="item in ac_dcOptions" :key="item.key" :label="item.display_name"
                           :value="item.key"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="12" :lg="6">
            <el-form-item label="枪状态" prop="gun_status">
              <el-select v-model="temp.gun_status" placeholder="Please select">
                <el-option v-for="v in gun_statusOptions" :key="v.key" :label="v.display_name"
                           :value="v.key"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <!--业务信息-->
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>充电桩业务信息</span>
        </div>
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="开放时间" prop="start_time">
              <el-input v-model="temp.start_time" placeholder="开放时间"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="关闭时间" prop="end_time">
              <el-input v-model="temp.end_time" placeholder="关闭时间"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="价格" prop="price">
              <el-input v-model="temp.price" placeholder="单位（元度）"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="服务费" prop="service_charge">
              <el-input v-model="temp.service_charge" placeholder="单位（元度）"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="使用状态" prop="use_status">
              <el-input v-model="temp.use_status" placeholder="使用状态"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="在线/离线" prop="on_off">
              <el-input v-model="temp.on_off" placeholder="在线离线"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm('temp')">创建</el-button>
      <el-button type="warning" @click="resetForm('temp')">重置</el-button>
    </div>
  </div>
</template>

<script>
import {
  sexOptions,
  companyOptions,
  marriageOptions,
  educationOptions,
  gun_statusOptions,
  ac_dcOptions
} from "@/utils/getData"
import {deviceCreate} from '@/api/api'

export default {
  data() {
    return {
      labelPosition: 'right',
      temp: {
        id: '',
        name: '',//姓名
        station: '',
        parkid: '',
        workrate: '',
        lon_lat: '',
        ac_dc: '',
        gun_status: '',
        start_time: '',
        end_time: '',
        price: '',
        service_charge: '',
        use_status: '',
        on_off: ''
      },
      rules: {},
      sexOptions,
      companyOptions,
      marriageOptions,
      educationOptions,
      gun_statusOptions,
      ac_dcOptions
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm('是否提交数据？', '温馨提示', {
            confirmButtonText: '确定',
            cancelBurronText: '取消',
            type: 'warning',
            center: true
          }).then(() => {
            deviceCreate(this.temp
                //     {
                //   'nodeid': 'node_4',
                //   'name': this.temp.name,
                //   'id': this.temp.id,
                //   'locationID': 124,
                //   'location': this.temp.station
                // }
            ).then(res => { //数据库参数用 this.temp
              this.$message({
                type: 'success',
                message: '提交成功',
                showClose: true
              })
              let {code, data} = res.data;
              if (code === 20000) {
                this.$router.push('/loan-management');
              }
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '提交已取消',
              showClose: true
            })
          })
        } else {
          this.$message({
            type: 'warning',
            message: '请将所有内容按格式填写完成！',
            showClose: true
          })
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped lang="scss">
.box-card {
  margin-bottom: 15px;
}

.dialog-footer {
  width: 30%;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;

  .el-button {
    width: 40%;
    // text-align: center;
  }
}
</style>
