<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover" class="mgb20" style="height: 200px">
          <div class="user-info">
            <img src="../assets/img/user-img.png" class="user-avator" alt />
            <div class="user-info-cont">
              <div class="user-info-name">{{ name }}</div>
              <div>{{ role }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="mgb20" style="height: 200px">
          <br />
          <div class="user-info-list">
            系统用户登录日期:
            <span>{{ currentDate }}</span>
          </div>
          <br />
          <!-- <div class="user-info-list">
            项目提交截至时间:
            <span>{{ currentDate }}</span>
          </div>
          <br />
          <div class="user-info-list">
            机构评分截至时间:
            <span>{{ currentDate }}</span>
          </div> -->
        </el-card>
      </el-col>
    </el-row>

    <!-- <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover" class="mgb20" style="height: 1200px">
          <div class="container">
            <div class="handle-box">
              <el-input
                v-model="searchParam.companyName"
                placeholder="请输入企业名称"
                class="handle-input mr10"
              ></el-input>
              <el-button type="primary" icon="el-icon-search" @click="search()"
                >搜索</el-button
              >
              <el-button type="default" @click="resetSearch()">重置</el-button>
            </div>

            <el-table
              :data="tableData"
              class="table"
              board
              ref="multipleTable"
              header-cell-class-name="table-header"
            >
              <el-table-column
                prop="companyName"
                label="企业名称"
              ></el-table-column>
              <el-table-column
                prop="companyAddress"
                label="企业地址"
              ></el-table-column>

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
              </el-form>
              <template #footer>
                <span class="dialog-footer">
                  <el-button type="primary" @click="editVisible = false"
                    >点 击 关 闭</el-button
                  >
                </span>
              </template>
            </el-dialog>
          </div>
        </el-card>
      </el-col>
    </el-row> -->

    <img src="../assets/logo/logo.png" class="logo-avator" alt="Logo" />
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { fetchData } from "../api/getCompanyTab";

export default {
  name: "dashboard",
  setup() {
    const name = localStorage.getItem("name");
    const type = localStorage.getItem("accountType");
    // const role = name === "admin" ? "管理员" : "普通用户";
    const role =
      name === "admin"
        ? "管理员用户"
        : type === "organization"
        ? "机构端用户"
        : type === "company"
        ? "企业端用户"
        : "游客用户";

    // 用于存储搜索的参数
    const searchParam = reactive({
      companyName: "",
    });

    const query = reactive({
      desc: "",
      name: "",
      pageIndex: 1,
      pageSize: 10,
    });

    const tableData = ref([]);
    const allData = ref([]); //用来备份存储所有数据的
    const searchData = ref([]); //用来保存当前要展示的所有数据
    const Total = ref(0);

    // 重写获取函数
    const getData = async () => {
      // 添加 async 关键字
      const res = await fetchData(query); // 添加 await 关键字
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
    const search = () => {
      searchData.value = allData.value.filter((item) =>
        item.companyName
          .toLowerCase()
          .includes(searchParam.companyName.toLowerCase())
      );
      handlePageChange(1);
    };

    const resetSearch = () => {
      searchParam.name = "";
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
      name,
      type,
      role,
      form,
      formRef,
      currentDate: new Date().toLocaleDateString(),
      query,
      tableData,
      Total,
      editVisible,
      search,
      searchParam,
      handlePageChange,
      resetSearch,
      showAllInfo,
    };
  },
};
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}

.handle-box {
  margin-bottom: 20px;
}

.table-td-certificate {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}

.form-text {
  padding: 5px;
  background-color: #f5f5f5;
  border-radius: 4px;
}
.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}

.user-avator {
  width: 120px;
  height: 120px;
  border-radius: 50%;
}

.logo-avator {
  width: 200px;
  position: fixed;
  right: 0;
  bottom: 0;
}

.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 20px;
  color: #777;
}

.user-info-cont div:first-child {
  font-size: 30px;
  color: #222;
}

.user-info-list {
  font-size: 20px;
  color: #999;
  line-height: 25px;
}

.user-info-list span {
  margin-left: 70px;
}

.mgb20 {
  margin-bottom: 20px;
}
</style>
