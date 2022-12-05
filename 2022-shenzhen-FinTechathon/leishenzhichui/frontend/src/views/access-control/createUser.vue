<template>
  <div class="createUser">
    <el-card class="box-card" shadow="always">
      <div slot="header" class="clearfix">
        <span>创建管理员</span>
      </div>
      <el-form :model="listQuery" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="用户名" prop="account">
          <el-input v-model="listQuery.account" prefix-icon="el-icon-user-solid" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="listQuery.password" autocomplete="off" prefix-icon="el-icon-s-goods" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="listQuery.checkPass" autocomplete="off" prefix-icon="el-icon-s-goods" placeholder="请重新输入密码"></el-input>
        </el-form-item>
        <el-form-item label="权限" prop="role_id">
          <el-radio-group v-model="listQuery.role_id">
            <el-radio label="3">初审人员</el-radio>
            <el-radio label="2">销售人员</el-radio>
            <el-radio label="1">管理人员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { createPermission } from '@/api/api.js'

export default {
  data() {
    // 姓名验证
    var checkAccount = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('用户名不能为空'));
      }
      setTimeout(() => {
        if (/^[a-zA-Z0-9_-]{4,16}$/.test(value)) {
          callback();
        } else {
          callback(new Error('由数字，字母，下划线，减号组成'));
        }
      }, 100);
    };

    // 验证密码
    var checkPassword = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('密码不能为空'));
      }
      setTimeout(() => {
        if (/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,16}$/.test(value)) {
          callback();
        } else {
          callback(new Error('由8到16位数字，字母，特殊字符组成'));
        }
      }, 100);
    };

    var checkPassword2 = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('密码不能为空'));
      }
      setTimeout(() => {
          if (value !== this.listQuery.password) {
            callback(new Error('两次输入密码不一致!'));
          } else {
            callback();
          }
      }, 100);
    }
    return {
      listQuery: {
        account: '',
        password: '',
        checkPass: '',  
        role_id: '',
      },
      rules: {
        account:[
          { required: true, trigger: 'blur', validator: checkAccount }
        ],
        password: [
          { required: true, trigger: 'blur', validator: checkPassword }
        ],
        checkPass: [
          { required: true, trigger: 'blur', validator: checkPassword2 }
        ],
        role_id: [
          { required: true }
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$confirm('是否创建用户？', '用户创建', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              createPermission({account:this.listQuery.account,password:this.listQuery.password,role_id:this.listQuery.role_id})
              .then(res => {
                if (res.data.code == 20000) {
                  this.$refs[formName].resetFields();
                  this.$store.dispatch('REMOVEUSERLISTS');
                  this.$message({
                    type: 'success',
                    message: '创建成功',
                    duration: 1000,
                    showClose: true
                  })
                } else {
                  this.$message({
                    type: 'warning',
                    message: '请求失败！',
                    duration: 1000,
                    showClose: true
                  })
                }
              }).catch(err => {
                this.$message({
                    type: 'error',
                    message: '创建失败',
                    duration: 1000,
                    showClose: true
                  })
              })
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '创建取消',
                duration: 1000,
                showClose: true
              })
            })
          } else {
            return false;
          }
        });
      },
    resetForm(formName) {
      this.$confirm('是否重置所有信息？', '信息重置', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$refs[formName].resetFields();
        this.$message({
          type: 'success',
          message: '重置成功',
          duration: 1000,
          showClose: true
        })
      }).catch(() => {
        this.$message({
          type: 'error',
          message: '重置失败',
          duration: 1000,
          showClose: true
        })
      })
      }
  }
}
</script>