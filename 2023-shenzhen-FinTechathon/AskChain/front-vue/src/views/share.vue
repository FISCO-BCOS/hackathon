<template>
  <!-- <div class="header">
    <el-row :gutter="30">
      <el-col :span="24">
        <el-card shadow="hover" class="mgb20"> </el-card>
      </el-col>
    </el-row>
    <img src="../assets/logo/logo.png" class="logo-avator" alt="Logo" />
  </div> -->
  <div class="login-wrap">
    <div class="ms-login">
      <!-- <div class="ms-title">ESG管理系统</div> -->
      <el-row :gutter="30">
        <el-col :span="24">
          <el-card shadow="hover">
            <div class="goto-login">
              <div class="handle-box">
                <el-button type="primary" @click="gotoLogin1()">
                  评估</el-button
                >
                <el-button type="primary" @click="gotoLogin2()">
                  申报</el-button
                >
              </div>
            </div>

            <div class="bigTitle">
              <div class="bigTitle-text">链上ESG</div>
            </div>

            <div class="container">
              <div class="handle-box">
                <el-input
                  v-model="searchParam.name"
                  placeholder="请输入企业名称"
                  class="handle-input mr10"
                ></el-input>
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  @click="search()"
                  >搜索</el-button
                >
                <el-button type="default" @click="resetSearch()"
                  >重置</el-button
                >
              </div>
            </div>

            <!-- <el-table
              :data="tableData"
              class="table"
              board
              ref="multipleTable"
              header-cell-class-name="table-header"
            >
              <el-table-column
                prop="id"
                label="ID"
                width="100"
                align="center"
              ></el-table-column>

              <el-table-column prop="name" label="企业名称"></el-table-column>
              <el-table-column label="企业评分">
                <template #default="scope">{{ scope.row.score }}</template>
              </el-table-column>
              <el-table-column label="等级" align="center">
                <template #default="scope">
                  <el-tag
                    :type="
                      scope.row.level === 'A'
                        ? 'success'
                        : scope.row.level === 'B'
                        ? 'info'
                        : scope.row.level === 'C'
                        ? 'warning'
                        : 'danger'
                    "
                    >{{ scope.row.level }}</el-tag
                  >
                </template>
              </el-table-column>

              <el-table-column
                prop="certificate"
                label="证书(点击查看)"
                align="center"
              >
                <template #default="scope">
                  <el-image
                    class="table-td-certificate"
                    :src="scope.row.certificate"
                    :preview-src-list="[scope.row.certificate]"
                  >
                  </el-image>
                </template>
              </el-table-column>
            </el-table> -->

            <el-table
              :data="tableData"
              class="table"
              board
              ref="multipleTable"
              header-cell-class-name="table-header"
            >
              <!-- <el-table-column
                prop="id"
                label="ID"
                width="100"
                align="center"
              ></el-table-column> -->

              <el-table-column
                prop="companyName"
                label="企业名称"
              ></el-table-column>
              <el-table-column
                prop="companyAddress"
                label="企业地址"
              ></el-table-column>
              <!-- <el-table-column label="企业评分">
                <template #default="scope">{{ scope.row.score }}</template>
              </el-table-column> -->
              <el-table-column label="企业等级" align="center">
                <template #default="scope">
                  <el-tag
                    :type="
                      scope.row.level === 'A'
                        ? 'success'
                        : scope.row.level === 'B'
                        ? 'info'
                        : scope.row.level === 'C'
                        ? 'warning'
                        : 'danger'
                    "
                    >{{ scope.row.level }}</el-tag
                  >
                </template>
              </el-table-column>
              <el-table-column label="详情" width="180" align="center">
                <template #default="scope">
                  <el-button
                    type="text"
                    icon="el-icon-edit"
                    @click="showAllInfo(scope.$index, scope.row)"
                    >点击查看
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

            <el-dialog
              title="项目评分"
              v-model="editVisible"
              width="50%"
              height="60%"
            >
              <el-form label-width="70px">
                <!-- <el-form-item label="ID">
                  <div class="form-text">{{ form.id }}</div>
                </el-form-item> -->
                <!-- <el-form-item label="机构名称">
                  <div class="form-text">{{ form.organization }}</div>
                </el-form-item> -->
                <el-form-item label="企业名称">
                  <div class="form-text">{{ form.companyName }}</div>
                </el-form-item>
                <el-form-item label="企业地址">
                  <div class="form-text">{{ form.companyAddress }}</div>
                </el-form-item>
                <el-form-item label="企业等级">
                  <div class="form-text">{{ form.level }}</div>
                </el-form-item>
                <el-form-item label="企业项目">
                  <div
                    class="form-text"
                    v-for="(item, index) in form.esgList"
                    :key="index"
                  >
                    <div>项目名称：{{ item.esgName }}</div>
                    <div>项目属性：{{ item.property }}</div>
                  </div>
                </el-form-item>
                <!-- <el-form-item label="项目名称">
                  <div class="form-text">{{ form.esgName }}</div>
                </el-form-item> -->
                <!-- <el-form-item label="项目属性">
                  <div class="form-text">{{ form.property }}</div>
                </el-form-item> -->
                <!-- <el-form-item label="项目描述">
                  <div class="form-text">{{ form.esgDescription }}</div>
                </el-form-item> -->
                <!-- <el-form-item label="评分标准">
                  <div class="form-text">{{ form.standard }}</div>
                </el-form-item> -->
                <!-- <el-form-item label="项目评分">
                  <div class="form-text">{{ form.score }}</div>
                </el-form-item> -->
                <!-- <el-form-item label="文件地址">
                  <div
                    class="form-text"
                    v-for="(url, index) in form.fileUrl"
                    :key="index"
                  >
                    <a :href="'http://' + url" target="_blank">{{ url }}</a>
                  </div>
                </el-form-item> -->
              </el-form>
              <template #footer>
                <span class="dialog-footer">
                  <el-button type="primary" @click="editVisible = false"
                    >点 击 关 闭</el-button
                  >
                  <!-- <el-button type="primary" @click="saveEdit">确 定</el-button> -->
                </span>
              </template>
            </el-dialog>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <img src="../assets/logo/file.png" class="logo-avator" alt="Logo" />
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { fetchData } from "../api/getCompanyTab";
import request from "../api/request";

export default {
  setup() {
    const router = useRouter();
    // const param = reactive({
    //   companyName: "",
    // });

    const searchParam = reactive({
      name: "",
    });

    const query = reactive({
      desc: "",
      name: "",
      pageIndex: 1,
      pageSize: 5,
    });

    const tableData = ref([]);
    const allData = ref([]); //用来备份存储所有数据的
    const searchData = ref([]); //用来保存当前要展示的所有数据
    const Total = ref(0);

    const gotoLogin1 = () => {
      // localStorage.setItem("NewAccountType", "organization");
      router.push("/login");
    };

    const gotoLogin2 = () => {
      // localStorage.setItem("NewAccountType", "company");
      router.push("/login");
    };

    // const fetchData = (companyName) => {
    //   return request({
    //     url: "/lookup/" + companyName,
    //     method: "get",
    //   });
    // };

    // 重写获取函数
    const getData = async () => {
      // 添加 async 关键字
      // const res = await fetchData(searchParam.name); // 添加 await 关键字
      const res = await fetchData();
      allData.value = res.data;
      Total.value = res.data.length;
      return res.data;
    };

    // 使用 async/await 来调用 getData
    (async () => {
      await getData();
      // 在这里添加你想在 getData 完成后执行的代码
      searchData.value = allData.value;
      handlePageChange(1);
    })();

    // 查询操作
    const search = () => {
      // (async () => {
      //   res = await getData();
      //   // 在这里添加你想在 getData 完成后执行的代码
      //   console.log(res);
      //   searchData.value = allData.value;
      //   handlePageChange(1);
      // })();
      searchData.value = allData.value.filter((item) =>
        item.companyName.toLowerCase().includes(searchParam.name.toLowerCase())
      );
      // res = console.log(res);
      handlePageChange(1);
    };

    const resetSearch = () => {
      searchParam.name = "";
      // tableData.value = [];
      searchData.value = allData.value;
      handlePageChange(1);
    };

    // // 分页导航
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
      // organization: "",
      companyName: "",
      // property: "",
      // esgName: "",
      // esgDescription: "",
      // score: 0,
      level: "",
      companyAddress: "",
      standard: "",
      esgList: [],
    });

    let idx = -1;
    const showAllInfo = (index, row) => {
      idx = index;
      Object.keys(form).forEach((item) => {
        // form[item] = row[item];
        if (item in row) {
          form[item] = row[item];
        }
      });
      editVisible.value = true;
    };

    return {
      searchParam,
      tableData,
      query,
      Total,
      editVisible,
      form,
      formRef,
      showAllInfo,
      gotoLogin1,
      gotoLogin2,
      resetSearch,
      search,
      resetSearch,
      handlePageChange,
    };
  },
};
</script>
<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  background-image: url(../assets/img/bg.jpg);
  background-size: 100%;
}

.table-td-certificate {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}

.table {
  width: 100%;
  font-size: 14px;
}
.form-text {
  padding: 5px;
  background-color: #f5f5f5;
  border-radius: 4px;
}
.goto-login {
  display: flex;
  justify-content: flex-end;
}

.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 60%;
  /* margin: -190px 0 0 -175px; */
  transform: translate(-50%, -50%);
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.3);
  overflow: hidden;
}

.el-button {
  font-size: 1vw; /* 相对于视口宽度的 2% */
  padding: 1vw; /* 相对于视口宽度的 1% 和 2% */
}
.handle-box {
  margin-bottom: 1%;
}
.mr10 {
  margin-right: 1vw;
}

.bigTitle {
  text-align: center;
}

.bigTitle-text {
  font-size: 30px;
}

.handle-input {
  width: 60%;
  display: inline-block;
}

.ms-content {
  padding: 30px;
}

.ms-title {
  width: 100%;
  line-height: 50px;
  text-align: center;
  font-size: 20px;
  color: #fff;
  border-bottom: 1px solid #ddd;
}
.logo-avator {
  width: 200px;
  position: fixed;
  right: 0;
  bottom: 0;
}
/* .mgb20-2 {
  margin-bottom: 60%;
  height: 60%;
  border: 3px solid gray;
} */
</style>
