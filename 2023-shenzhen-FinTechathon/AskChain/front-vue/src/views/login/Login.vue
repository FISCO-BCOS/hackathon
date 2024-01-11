<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">ESG管理系统</div>
      <el-form
        :model="param"
        :rules="rules"
        ref="loginForm"
        label-width="0px"
        class="ms-content"
      >
        <!-- 选择用户类型 -->
        <el-form-item prop="usertype" class="form-usertype">
          <div class="icon-select">
            <!-- <el-icon class="icon-select__icon" name="el-icon-user"></el-icon> -->
            <el-select
              v-model="param.usertype"
              class="icon-select__select"
              placeholder="请选择用户类型"
            >
              <el-option label="企业端" value="company" />
              <el-option label="机构端" value="organization" />
            </el-select>
          </div>
        </el-form-item>

        <el-form-item prop="username">
          <el-input v-model="param.username" placeholder="用户名">
            <template #prepend>
              <el-button icon="el-icon-user"></el-button>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            type="password"
            show-password
            placeholder="密码"
            v-model="param.password"
            @keyup.enter="submitForm()"
          >
            <template #prepend>
              <el-button icon="el-icon-lock"></el-button>
            </template>
          </el-input>
        </el-form-item>

        <div class="login-btn">
          <el-button type="primary" @click="submitForm()">登录</el-button>
        </div>

        <div class="login-btn">
          <router-link to="/register">
            <el-button type="primary" size="large">注册</el-button>
          </router-link>
        </div>

        <!-- <el-button type="primary" @click="guestLogin()">游客登录</el-button> -->
        <div class="login-btn">
          <el-button type="primary" @click="gotoShare()">返回首页</el-button>
        </div>
      </el-form>
    </div>
    <img src="../../assets/logo/file.png" class="logo-avator" alt="Logo" />
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { login } from "../../api/login";

export default {
  setup() {
    const router = useRouter();
    const param = reactive({
      username: "",
      password: "",
      usertype: "",
    });

    param.usertype = localStorage.getItem("NewAccountType");
    const rules = {
      username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
      password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      usertype: [{ required: true, message: "请选择类型", trigger: "blur" }],
    };

    const loginForm = ref(null);

    const postData = reactive({
      accountType: "",
      name: "",
      password: "",
    });

    const copyData = () => {
      postData.accountType = param.usertype;
      postData.name = param.username;
      postData.password = param.password;
    };

    const submitForm = () => {
      loginForm.value.validate((valid) => {
        if (valid) {
          copyData();
          // 实际用的是这个
          // login(postData.password)
          login(postData)
            .then((response) => {
              console.log(response);
              console.log(postData);
              if (response.code === 2000) {
                if (
                  postData.accountType === response.data.identity &&
                  postData.name === response.data.username
                ) {
                  ElMessage.success("登录成功");
                  // localStorage.setItem("name", response.data.username);
                  // localStorage.setItem("identity", response.data.identity);
                  localStorage.setItem("name", response.data.username);
                  // localStorage.setItem("login_name", response.data.name);
                  localStorage.setItem("accountType", response.data.identity);
                  // console.log(response);
                  router.push("/");
                } else {
                  ElMessage.success("登录成功");
                  localStorage.setItem("name", response.data.username);
                  localStorage.setItem("accountType", response.data.identity);
                  router.push("/");
                  // console.log(response);
                  // ElMessage.error("账号类型、账号或密码错误");
                }
              } else {
                console.log(response);
                ElMessage.error("账号类型、账号或密码错误");
              }
            })
            .catch((error) => {
              ElMessage.error("登录失败");
            });
        } else {
          ElMessage.error("登录失败");
          return false;
        }
      });
    };

    const guestLogin = () => {
      localStorage.setItem("name", "guest");
      localStorage.setItem("accountType", "guest");
      router.push("/");
    };
    const gotoShare = () => {
      localStorage.removeItem("NewAccountType");
      router.push("/share");
    };

    const store = useStore();
    store.commit("clearTags");

    return {
      param,
      rules,
      loginForm,
      submitForm,
      guestLogin,
      gotoShare,
    };
  },
};
</script>

<style scoped>
/* 这里是选择组件的样式 */
.icon-select {
  display: flex;
  align-items: center;
}

.icon-select__icon {
  margin-right: 10px;
}

.icon-select__select {
  flex-grow: 1;
}
/* 这里是选择组件的样式 */

.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background-image: url(../../assets/img/bg.jpg);
  background-size: 100%;
}
.ms-title {
  width: 100%;
  line-height: 50px;
  text-align: center;
  font-size: 20px;
  color: #fff;
  border-bottom: 1px solid #ddd;
}
.ms-login {
  position: absolute;
  left: 50%;
  top: 200px;
  width: 350px;
  margin: -190px 0 0 -175px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.3);
  overflow: hidden;
}
.ms-content {
  padding: 30px;
}

.login-btn {
  text-align: center;
}
.login-btn button {
  width: 100%;
  height: 36px;
  margin-bottom: 10px;
}
.login-tips {
  font-size: 15px;
  line-height: 30px;
  color: #fff;
}

.logo-avator {
  width: 200px;
  position: fixed;
  right: 0;
  bottom: 0;
  transform: translateX(-50%);
}
</style>
