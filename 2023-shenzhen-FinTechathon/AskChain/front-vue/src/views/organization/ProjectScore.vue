<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 机构端
        </el-breadcrumb-item>
        <el-breadcrumb-item>项目评分</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="container">
      <div class="handle-box">
        <el-input
          v-model="searchParam.companyName"
          placeholder="请输入企业名称"
          class="handle-input mr10"
        ></el-input>
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="searchCompanyName()"
        >
          搜索
        </el-button>
        <el-button type="default" @click="resetSearch()"> 重置 </el-button>
      </div>

      <div class="handle-box">
        <el-input
          v-model="searchParam.esgName"
          placeholder="请输入项目名称"
          class="handle-input mr10"
        ></el-input>
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="searchEsgName()"
        >
          搜索
        </el-button>
        <el-button type="default" @click="resetSearch()"> 重置 </el-button>
      </div>

      <div class="handle-box">
        <el-select
          v-model="searchParam.property"
          placeholder="请选择项目属性"
          class="handle-input mr10"
        >
          <el-option label="E" value="E" />
          <el-option label="S" value="S" />
          <el-option label="G" value="G" />
        </el-select>
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="searchProperty()"
        >
          搜索
        </el-button>
        <el-button type="default" @click="resetSearch()"> 重置 </el-button>
      </div>

      <el-table
        :data="tableData"
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header"
        :row-style="{ height: '68px' }"
      >
        <el-table-column
          prop="id"
          label="ID"
          width="110"
          align="center"
        ></el-table-column>
        <!-- <el-table-column prop="name" label="公司名称"></el-table-column> -->
        <el-table-column prop="companyName" label="企业名称"></el-table-column>
        <el-table-column prop="esgName" label="项目名称"> </el-table-column>
        <el-table-column
          prop="property"
          label="项目属性"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="esgDescription"
          label="项目描述"
        ></el-table-column>
        <el-table-column prop="standard" label="评分标准">
          <template #default="scope">
            <span v-if="scope.row.score > 0">{{ scope.row.standard }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="项目评分">
          <template #default="scope">
            <!-- {{ scope.row.score }} -->
            <span v-if="scope.row.score > 0">{{ scope.row.score }}</span>
          </template>
        </el-table-column>

        <!-- <el-table-column prop="date" label="注册时间"></el-table-column> -->
        <!-- 添加 companyAddress项 -->
        <el-table-column
          prop="companyAddress"
          label="公司地址"
          v-if="false"
        ></el-table-column>

        <el-table-column
          prop="fileUrl"
          label="文件地址"
          v-if="false"
        ></el-table-column>

        <el-table-column label="详情" width="180" align="center">
          <template #default="scope">
            <el-button
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row)"
              >编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="query.pageIndex"
          :page-size="query.pageSize"
          :total="Total"
          @current-change="handlePageChange"
        ></el-pagination>
      </div>
      <div style="height: 60px"></div>
    </div>

    <!-- 编辑弹出框 -->
    <el-dialog title="项目评分" v-model="editVisible" width="60%">
      <el-form label-width="70px">
        <el-form-item label="ID">
          <el-input v-model="form.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="企业名称">
          <el-input v-model="form.companyName" disabled></el-input>
        </el-form-item>
        <el-form-item label="项目地址">
          <el-input v-model="form.companyAddress" disabled></el-input>
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input v-model="form.esgName" disabled></el-input>
        </el-form-item>
        <el-form-item label="项目属性">
          <el-radio-group v-model="form.property" disabled>
            <el-radio label="E"></el-radio>
            <el-radio label="S"></el-radio>
            <el-radio label="G"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="项目描述">
          <el-input v-model="form.esgDescription" disabled></el-input>
        </el-form-item>
        <el-form-item label="项目文件">
          <div v-for="(url, index) in form.fileUrl" :key="index">
            <a :href="'http://' + url" target="_blank">{{ url }}</a>
          </div>
          <!-- </el-input> -->
        </el-form-item>
        <el-form-item label="评分标准">
          <el-radio-group v-model="form.standard">
            <el-radio label="中国企业评级报告标准"></el-radio>
            <el-radio label="GRI标准"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="项目评分">
          <!-- <el-input v-model="form.score" :disabled="form.score > 50"></el-input> -->
          <!-- <el-input v-model="form.score"></el-input> -->
          <el-input v-model="form.newScore"></el-input>
        </el-form-item>

        <!-- <el-form-item label="文件提交" prop="file">
          <div class="container">
            <el-upload
              class="upload-demo"
              drag
              action="http://jsonplaceholder.typicode.com/api/posts/"
              multiple
            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">
                将文件拖到此处，或
                <em>点击上传</em>
              </div>
              <div class="content-title">支持拖拽上传</div>
            </el-upload>

          </div>
        </el-form-item> -->
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <img src="../../assets/logo/logo.png" class="logo-avator" alt="Logo" />
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { fetchData } from "../../api/getProjectScoreTab";
import { setScore } from "../../api/setScore";
export default {
  name: "projectScore",
  setup() {
    const query = reactive({
      address: "",
      name: "",
      pageIndex: 1,
      pageSize: 10,
    });
    // 用于存储搜索的参数
    const searchParam = reactive({
      esgName: "",
      companyName: "",
      property: "",
    });

    const tableData = ref([]);
    const allData = ref([]);
    const searchData = ref([]);
    const Total = ref(0);

    // 重写获取函数
    const getData = async () => {
      // 添加 async 关键字
      const res = await fetchData(); // 添加 await 关键字
      allData.value = res.data;
      Total.value = res.data.length;
    };

    // 使用 async/await 来调用 getData
    (async () => {
      await getData();
      // 在这里添加你想在 getData 完成后执行的代码
      searchData.value = allData.value;
      handlePageChange(1);
    })();

    // 查询操作
    const searchEsgName = () => {
      searchParam.property = "";
      searchParam.companyName = "";
      searchData.value = allData.value.filter((item) =>
        item.esgName.toLowerCase().includes(searchParam.esgName.toLowerCase())
      );
      handlePageChange(1);
    };

    const searchCompanyName = () => {
      searchParam.property = "";
      searchParam.esgName = "";
      searchData.value = allData.value.filter((item) =>
        item.companyName
          .toLowerCase()
          .includes(searchParam.companyName.toLowerCase())
      );
      handlePageChange(1);
    };

    const searchProperty = () => {
      searchParam.esgName = "";
      searchParam.companyName = "";
      searchData.value = allData.value.filter((item) =>
        item.property.toLowerCase().includes(searchParam.property.toLowerCase())
      );
      handlePageChange(1);
    };

    const resetSearch = () => {
      searchParam.esgName = "";
      searchParam.companyName = "";
      searchParam.property = "";
      searchData.value = allData.value;
      handlePageChange(1);
    };

    // 分页导航
    const handlePageChange = (val) => {
      query.pageIndex = val;
      // ElMessage(`当前是第 ${query.pageIndex} 页`);
      const start = (query.pageIndex - 1) * query.pageSize;
      const end = query.pageIndex * query.pageSize;
      tableData.value = searchData.value.slice(start, end);
      Total.value = searchData.value.length;
    };

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);

    const formRef = ref(null);
    let form = reactive({
      id: 0,
      esgName: "",
      companyName: "",
      property: "",
      esgDescription: "",
      standard: "",
      score: 0,
      newScore: 0,
      // 新添加的项
      companyAddress: "",
      fileUrl: [""],
    });

    function validateForm(score, standard) {
      // 验证 standard
      if (!standard) {
        throw new Error("请选择评分标准");
      }

      // 尝试将 score 转换为数字
      const scoreNumber = parseFloat(score);

      // 验证 score
      if (!score) {
        throw new Error("请输入项目评分");
      } else if (isNaN(scoreNumber)) {
        throw new Error("项目评分必须为数字");
      } else if (score < 1 || score > 100) {
        throw new Error("项目评分必须在1到100之间");
      }

      // 如果所有验证都通过，返回 true
      return true;
    }

    let idx = -1;
    const handleEdit = (index, row) => {
      idx = index;
      Object.keys(form).forEach((item) => {
        form[item] = row[item];
        // if (item in row) {
        //   form[item] = row[item];
        // }
      });
      if (form.score > 0) {
        editVisible.value = false;
        ElMessage.error(`已有评分，无法再次评分`);
      } else {
        editVisible.value = true;
      }
    };

    const postData = reactive({
      // companyName: "",
      score: 0,
      // 新加入的项
      companyAddress: "",
      projectId: 0,
    });

    const copyData = () => {
      // postData.companyName = form.companyName;
      postData.score = form.score;
      postData.projectId = form.id;
      postData.companyAddress = form.companyAddress;
    };

    const saveEdit = () => {
      // ElMessage.success(`修改第 ${idx + 1} 行成功`);
      // 表单校验
      try {
        validateForm(form.newScore, form.standard);
        // 如果没有抛出错误，那么验证通过

        editVisible.value = false;
        // 将合法评分输入修改
        form.score = form.newScore;
        // 调用提交项目评分请求
        // PUT打分方法，提交成功后再重新从后端返回数据
        copyData();
        setScore(postData)
          .then((response) => {
            if (response.code === 2000) {
              console.log(postData);
              console.log(postData.score);
              console.log(response);
              // Object.keys(form).forEach((item) => {
              //   // tableData.value[idx][item] = form[item];
              //   if (item in tableData.value[idx]) {
              //     tableData.value[idx][item] = form[item];
              //   }
              // });
              let target = allData.value.find(
                (data) => data.id === tableData.value[idx].id
              );
              Object.keys(form).forEach((item) => {
                // 模拟同步修改后端数据
                if (item !== "newScore") {
                  // tableData.value[idx][item] = form[item];
                  target[item] = form[item];
                }
              });
              ElMessage.success("评分成功");
            } else {
              ElMessage.error("评分失败");
            }
          })
          .catch((error) => {
            ElMessage.error("评分错误");
          });
        resetSearch();
      } catch (error) {
        ElMessage.error(`评分错误：${error.message}`);
      }
    };

    return {
      query,
      tableData,
      Total,
      editVisible,
      form,
      formRef,
      searchParam,
      searchEsgName,
      searchCompanyName,
      searchProperty,
      resetSearch,
      handlePageChange,
      handleEdit,
      saveEdit,
    };
  },
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}
.red {
  color: #ff0000;
}
.mr10 {
  margin-right: 10px;
}
.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}

.logo-avator {
  width: 200px;
  position: fixed;
  right: 0;
  bottom: 0;
}
</style>
