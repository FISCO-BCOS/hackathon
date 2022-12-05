<template>
  <div class="loan-application">
    <el-form status-icon :label-position="labelPosition" :model="temp" :rules="rules" ref="temp" label-width="100px">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>用户基本信息</span>
        </div>
        <el-row :gutter="10">
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="temp.name" placeholder="请输入姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="车牌号" prop="station">
              <el-input v-model="temp.carId" placeholder="车牌号"/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="8">
            <el-form-item label="汽车电池容量" prop="parkid">
              <el-input v-model="temp.totelEle" placeholder="汽车电池容量"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :xs="24" :sm="12" :md="12" :lg="12">
            <el-form-item label="汽车剩余电量" prop="workrate">
              <el-input v-model="temp.remainEle" placeholder="汽车剩余电量"/>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="10" :lg="10">
            <el-form-item label="剩余金额" prop="lon_lat">
              <el-input v-model="temp.money" placeholder="剩余金额"/>
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
import {queryUser, userCreate} from '@/api/api'

export default {
  data() {
    return {
      labelPosition: 'right',
      temp: {
        id: 'node_2',//姓名
        name: '',
        carId: '',
        totelEle: '',
        remainEle: '',
        money: ''
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
            userCreate(this.temp).then(res => {
              this.$message({
                type: 'success',
                message: '提交成功',
                showClose: true
              })
              let {code, data} = res.data;
              if (code === 20000) {

              }
            })
            queryUser({'id': 'node_2'}).then(res => {
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
