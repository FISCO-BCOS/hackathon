<template>
  <div id="login">
    <!--粒子效果-->
    <vue-particles
        color="#dedede"
        :particleOpacity="0.7"
        :particlesNumber="77"
        shapeType="circle"
        :particleSize="4"
        linesColor="#dedede"
        :linesWidth="1"
        :lineLinked="true"
        :lineOpacity="0.4"
        :linesDistance="150"
        :moveSpeed="3"
        :hoverEffect="true"
        hoverMode="grab"
        :clickEffect="true"
        clickMode="push">
    </vue-particles>

    <div class="input">
      <div class="title">
        <h1>新能源汽车众充运维平台</h1>
      </div>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0" class="demo-ruleForm">
        <el-form-item prop="account">
          <el-input prefix-icon="el-icon-user-solid" v-model="ruleForm.account" placeholder="4到16位字符"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-s-goods" type="password" v-model="ruleForm.password" show-password
                    placeholder="8到16位字符"></el-input>
        </el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
      </el-form>
    </div>
  </div>
</template>

<script>
import {getMessage} from '@/utils/getMessage.js'

export default {
  data() {
    // 验证用户名
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
    }
    // 验证密码
    var checkPassword = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('密码不能为空'));
      }
      setTimeout(() => {
        if (/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,16}$/.test(value)) {
          callback();
        } else {
          callback(new Error('由数字，字母，特殊字符组成'));
        }
      }, 100);
    }
    return {
      ruleForm: {//初始值
        account: 'admin',
        password: 'admin@123'
      },
      rules: {//校验规则
        account: [
          {required: true, trigger: 'blur', validator: checkAccount}
        ],
        password: [
          {required: true, trigger: 'blur', validator: checkPassword}
        ]
      }
    }
  },
  methods: {
    submitForm(formName) {
      let self = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$store.dispatch('ULOGIN', this.ruleForm).then(res => {
            self.ruleForm = {};
            self.$router.push('/home');
            setTimeout(() => {
              getMessage('success', '登录成功！');
            }, 700);

          }).catch(err => {
            console.log(err);
          })
        } else {
          getMessage('error', '请输入正确的用户名和密码！');
          this.ruleForm = {};
          return false;
        }
      })
    },
    // Login(data) {
    //   login(data).then(res => {
    //         if (res.data.code === "20000") {
    //           this.$router.push('/home');
    //         }
    //       }
    //   )
    // }
  }
}
</script>

<style scoped lang="scss">
#login {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('../../assets/img/bg6.jpg') no-repeat fixed;
  background-size: cover;
  -moz-background-size: cover;
  -webkit-background-size: cover;

  #particles-js {
    position: relative;
    width: 100vw;
    height: 100vh;
  }

  .input {
    width: 450px;
    height: 200px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: rgba(255, 255, 255, 0.171);
    border: 2px solid rgba(255, 255, 255, 0.432);

    .title {
      position: absolute;
      top: -100px;
      width: inherit;
      text-align: center;

      h1 {
        margin: 0;
        color: white;
        font-size: 40px;
      }
    }

    .el-form {
      width: inherit;
      height: inherit;
      display: flex;
      flex-direction: column;
      justify-content: space-around;

      .el-form-item {
        width: 75%;
        margin: 0;
        margin: 0 auto;
      }

      .el-button--primary {
        width: 75%;
        margin: 0 auto;
      }
    }
  }
}

</style>
