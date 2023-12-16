<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 机构端
        </el-breadcrumb-item>
        <el-breadcrumb-item>评分查询</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="container">
      <div class="handle-box">
        <el-input
          v-model="searchParam.organization"
          placeholder="请输入机构名称"
          class="handle-input mr10"
        ></el-input>
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="searchOrganization()"
        >
          搜索
        </el-button>
        <el-button type="default" @click="resetSearch()"> 重置 </el-button>
      </div>

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

      <el-table
        :data="tableData"
        class="table"
        ref="multipleTable"
        board
        header-cell-class-name="table-header"
        :row-style="{ height: '68px' }"
      >
        <el-table-column
          prop="id"
          label="ID"
          width="40"
          align="center"
        ></el-table-column>
        <el-table-column prop="organization" label="评分机构"></el-table-column>
        <el-table-column prop="companyName" label="企业名称"></el-table-column>
        <el-table-column
          v-if="false"
          prop="companyAddress"
          label="企业地址"
        ></el-table-column>
        <el-table-column prop="esgName" label="项目名称"></el-table-column>
        <el-table-column
          prop="property"
          label="项目属性"
          align="center"
        ></el-table-column>
        <el-table-column
          v-if="false"
          prop="esgDescription"
          label="项目描述"
        ></el-table-column>
        <el-table-column
          prop="fileUrl"
          label="文件地址"
          v-if="false"
        ></el-table-column>
        <el-table-column prop="standard" label="评分标准"></el-table-column>
        <el-table-column prop="score" label="项目评分">
          <template #default="scope">{{ scope.row.score }}</template>
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
      <div style="height: 60px"></div>
    </div>

    <el-dialog title="项目评分" v-model="editVisible" width="50%" height="60%">
      <el-form label-width="70px">
        <el-form-item label="ID">
          <div class="form-text">{{ form.id }}</div>
        </el-form-item>
        <el-form-item label="机构名称">
          <div class="form-text">{{ form.organization }}</div>
        </el-form-item>
        <el-form-item label="企业名称">
          <div class="form-text">{{ form.companyName }}</div>
        </el-form-item>
        <el-form-item label="企业地址">
          <div class="form-text">{{ form.companyAddress }}</div>
        </el-form-item>
        <el-form-item label="项目名称">
          <div class="form-text">{{ form.esgName }}</div>
        </el-form-item>
        <el-form-item label="项目属性">
          <div class="form-text">{{ form.property }}</div>
        </el-form-item>
        <el-form-item label="项目描述">
          <div class="form-text">{{ form.esgDescription }}</div>
        </el-form-item>
        <el-form-item label="评分标准">
          <div class="form-text">{{ form.standard }}</div>
        </el-form-item>
        <el-form-item label="项目评分">
          <div class="form-text">{{ form.score }}</div>
        </el-form-item>
        <el-form-item label="文件地址">
          <div
            class="form-text"
            v-for="(url, index) in form.fileUrl"
            :key="index"
          >
            <a :href="'http://' + url" target="_blank">{{ url }}</a>
          </div>
        </el-form-item>
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

    <img src="../../assets/logo/logo.png" class="logo-avator" alt="Logo" />
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { fetchData } from "../../api/getOrganizationScoreTab";

export default {
  setup() {
    const query = reactive({
      address: "",
      name: "",
      pageIndex: 1,
      pageSize: 5,
    });

    // 用于存储搜索的参数
    const searchParam = reactive({
      esgName: "",
      companyName: "",
      organization: "",
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
      searchParam.organization = "";
      searchData.value = allData.value.filter((item) =>
        item.esgName.toLowerCase().includes(searchParam.esgName.toLowerCase())
      );
      handlePageChange(1);
    };

    const searchCompanyName = () => {
      searchParam.property = "";
      searchParam.esgName = "";
      searchParam.organization = "";
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
      searchParam.organization = "";
      searchData.value = allData.value.filter((item) =>
        item.property.toLowerCase().includes(searchParam.property.toLowerCase())
      );
      handlePageChange(1);
    };

    const searchOrganization = () => {
      searchParam.esgName = "";
      searchParam.companyName = "";
      searchParam.property = "";
      searchData.value = allData.value.filter((item) =>
        item.organization
          .toLowerCase()
          .includes(searchParam.organization.toLowerCase())
      );
      handlePageChange(1);
    };

    const resetSearch = () => {
      searchParam.esgName = "";
      searchParam.companyName = "";
      searchParam.property = "";
      searchParam.organization = "";
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
      organization: "",
      companyName: "",
      property: "",
      esgName: "",
      esgDescription: "",
      fileUrl: [""],
      score: 0,
      companyAddress: "",
      standard: "",
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
      query,
      tableData,
      Total,
      form,
      searchParam,
      formRef,
      editVisible,
      searchEsgName,
      searchCompanyName,
      searchProperty,
      searchOrganization,
      resetSearch,
      handlePageChange,
      showAllInfo,
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

.table {
  width: 100%;
  font-size: 14px;
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

.form-text {
  padding: 5px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

/* .table {
  height: 80vh;
} */
/* .container {
  height: 90vh;
} */
</style>
