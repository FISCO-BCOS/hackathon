<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 企业端
        </el-breadcrumb-item>
        <el-breadcrumb-item>项目提交</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="container">
      <div class="form-box">
        <el-form ref="formRef" :rules="rules" :model="form" label-width="80px">
          <!-- <el-form-item label="公司名称" prop="companyName">
            <el-input v-model="form.companyName"></el-input>
          </el-form-item> -->

          <el-form-item label="项目属性" prop="property">
            <el-radio-group v-model="form.property">
              <el-radio label="E" />
              <el-radio label="S" />
              <el-radio label="G" />
            </el-radio-group>
          </el-form-item>

          <el-form-item label="项目名称" prop="name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>

          <el-form-item label="项目描述" prop="desc">
            <el-input type="textarea" rows="5" v-model="form.desc"></el-input>
          </el-form-item>

          <!-- action="http://localhost:8090/upload" -->
          <!-- <el-form-item label="文件提交" prop="file"> -->
          <el-form-item label="文件提交">
            <div class="container">
              <!-- <div class="plugins-tips"></div> -->
              <el-upload
                class="upload-demo"
                drag
                :http-request="customRequest"
                multiple="true"
                on-success="handleUploadSuccess"
                on-error="handleUploadError"
              >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">
                  将文件拖到此处，或
                  <em>点击上传</em>
                </div>
                <div class="content-title">支持拖拽上传</div>
                <template #tip>
                  <div class="el-upload__tip"></div>
                  <div class="content-title">
                    <!-- 只能上传 jpg/png 文件，且不超过 500kb -->
                    支持多个文件上传，单个文件不大于10MB的文件
                  </div>
                </template>
              </el-upload>

              <!-- <div class="content-title">支持裁剪</div> -->
            </div>
          </el-form-item>

          <!--  -->

          <!-- <el-form-item label="文件提交" prop="file">
            <el-upload
              class="upload-demo"
              :http-request="uploadFile"
              :on-change="handleChange"
              :file-list="fileList"
            >
              <el-button size="small" type="primary">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">
                只能上传jpg/png文件，且不超过500kb
              </div>
            </el-upload>
          </el-form-item> -->

          <!-- <img :src="'http://localhost:8090/downloads/1.jpg'" alt="图片" /> -->
          <!-- <el-form-item label="查看文件">
            <a :href="'http://localhost:8090/downloads/1.jpg'" target="_blank">
              http://localhost:8090/downloads/1.jpg
            </a>
          </el-form-item> -->

          <el-form-item>
            <el-button type="primary" @click="onSubmit">提交</el-button>
            <el-button @click="onReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <img src="../../assets/logo/logo.png" class="logo-avator" alt="Logo" />
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { upload } from "../../api/upload";
import axios from "axios";

export default {
  name: "ProjectSubmit",
  data() {
    return {
      fileList: [
        {
          name: "1.jpg",
          url: "../../../public/resource/1.jpg",
        },
        {
          name: "2.jpg",
          url: "../../../public/resource/2.jpg",
        },
      ],
    };
  },
  methods: {
    uploadFile(file) {
      let formData = new FormData();
      console.log(file);
      formData.append("file", file.file);
      return axios.put("http://localhost:8090/upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
    },
    handleChange(file, fileList) {
      this.fileList = fileList.slice(-3);
    },
  },
  setup() {
    const rules = {
      name: [{ required: true, message: "请输入项目名称", trigger: "blur" }],
      // companyName: [
      //   { required: true, message: "请输入公司名称", trigger: "blur" },
      // ],
      file: [{ required: false, message: "请提交文件", trigger: "blur" }],
      desc: [{ required: true, message: "请输入项目描述", trigger: "blur" }],
      property: [
        { required: true, message: "请输入项目属性", trigger: "blur" },
      ],
    };
    // 维护所有刚上传的文件
    const formRef = ref(null);
    const form = reactive({
      name: "",
      property: "",
      region: "",
      desc: "",
      file: [],
    });

    const postData = reactive({
      property: "",
      esgName: "",
      description: "",
      fileUrl: [],
    });

    const copyData = () => {
      postData.property = form.property;
      postData.esgName = form.name;
      postData.description = form.desc;
      postData.fileUrl = form.file;
    };

    // 提交
    const onSubmit = () => {
      // 表单校验
      console.log(form.file);
      formRef.value.validate((valid) => {
        if (valid) {
          copyData();
          upload(postData)
            .then((response) => {
              if (response.code === 2000) {
                ElMessage.success("提交成功");
                onReset();
              } else {
                ElMessage.error("提交失败");
              }
            })
            .catch((error) => {
              ElMessage.error("提交错误");
            });
        } else {
          return false;
        }
      });
    };
    // 重置
    const onReset = () => {
      formRef.value.resetFields();
    };

    return {
      rules,
      formRef,
      form,
      onSubmit,
      onReset,
    };
  },
};
</script>

<style scoped>
.logo-avator {
  width: 200px;
  position: fixed;
  right: 0;
  bottom: 0;
}
</style>
