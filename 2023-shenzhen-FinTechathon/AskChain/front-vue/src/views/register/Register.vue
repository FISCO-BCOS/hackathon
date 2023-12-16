<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">ESG账号注册</div>
      <el-form
        :model="param"
        :rules="rules"
        ref="registerForm"
        label-width="0px"
        class="ms-content"
      >
        <el-form-item prop="usertype" class="form-usertype">
          <div class="icon-select">
            <!-- <el-icon class="icon-select__icon" name="el-icon-user"></el-icon> -->
            <el-select
              v-model="param.usertype"
              class="icon-select__select"
              placeholder="请选择用户类型"
            >
              <el-option label="企业端" value="company" />
              <el-option label="机构端" value="institution" />
            </el-select>
          </div>
        </el-form-item>

        <el-form-item prop="organizationCode">
          <el-input v-model="param.organizationCode" placeholder="组织机构代码">
            <template #prepend>
              <el-button icon="el-icon-house"></el-button>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="registerAddress">
          <el-input v-model="param.registerAddress" placeholder="注册地址">
            <template #prepend>
              <el-button icon="el-icon-position"></el-button>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="industryType">
          <el-input v-model="param.industryType" placeholder="行业类别">
            <template #prepend>
              <el-button icon="el-icon-discount"></el-button>
            </template>
          </el-input>
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
            placeholder="请输入密码"
            v-model="param.password"
          >
            <template #prepend>
              <el-button icon="el-icon-lock"></el-button>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password_repeat">
          <el-input
            type="password"
            show-password
            placeholder="请确认密码"
            v-model="param.password_repeat"
            @keyup.enter="submitForm()"
          >
            <template #prepend>
              <el-button icon="el-icon-lock"></el-button>
            </template>
          </el-input>
        </el-form-item>

        <div class="login-btn">
          <el-button type="primary" @click="submitForm()">点击注册</el-button>
        </div>

        <div class="login-btn">
          <router-link to="/login">
            <el-button type="primary" size="large">返回登录</el-button>
          </router-link>
        </div>

        <!-- <p class="login-tips">Tips : 请输入注册用户名和密码。</p> -->
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
import { register } from "../../api/register";

export default {
  setup() {
    const router = useRouter();
    const param = reactive({
      username: "",
      password: "",
      password_repeat: "",
      usertype: "",
      organizationCode: "",
      registerAddress: "",
      industryType: "",
    });

    const returnData = reactive({
      code: 0,
      message: "",
      data: {},
    });

    const rules = {
      username: [
        {
          required: true,
          message: "请输入用户名",
          trigger: "blur",
        },
      ],
      password_repeat: [
        { required: true, message: "请输入密码", trigger: "blur" },
      ],
      password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      usertype: [{ required: true, message: "请选择类型", trigger: "blur" }],
      organizationCode: [
        { required: true, message: "请输入组织机构代码", trigger: "blur" },
      ],
      registerAddress: [
        { required: true, message: "请输入注册地址", trigger: "blur" },
      ],
      industryType: [
        {
          required: true,
          message: "请输入行业类别(机构端用户请填无)",
          trigger: "blur",
        },
      ],
    };

    const registerForm = ref(null);

    const postData = reactive({
      accountType: "",
      organizationCode: "",
      location: "",
      field: "",
      name: "",
      password: "",
    });

    const copyData = () => {
      postData.accountType = param.usertype;
      postData.organizationCode = param.organizationCode;
      postData.location = param.registerAddress;
      postData.field = param.industryType;
      postData.name = param.username;
      postData.password = param.password;
    };

    const submitForm = () => {
      registerForm.value.validate((valid) => {
        if (valid && param.password === param.password_repeat) {
          copyData();
          register(postData)
            .then((response) => {
              if (response.code === 2000) {
                ElMessage.success("注册成功");
                console.log(response);
                router.push("/login");
              } else {
                console.log(response);
                ElMessage.error("注册失败");
              }
            })
            .catch((error) => {
              ElMessage.error("注册错误");
            });
          // ElMessage.success("注册成功");
          // localStorage.setItem("name", param.username);
          // localStorage.setItem("accountType", param.usertype);
          // router.push("/login");
        } else {
          ElMessage.error("注册失败");
          return false;
        }
      });
    };

    const store = useStore();
    store.commit("clearTags");

    return {
      param,
      rules,
      registerForm,
      submitForm,
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
  padding: 30px 30px;
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
