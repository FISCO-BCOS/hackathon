<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 企业端
        </el-breadcrumb-item>
        <el-breadcrumb-item>项目管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="container">
      <div class="handle-box">
        <el-input
          v-model="searchParam.esgName"
          placeholder="请输入项目名称"
          class="handle-input mr10"
        >
        </el-input>
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
          width="55"
          align="center"
        ></el-table-column>

        <el-table-column prop="esgName" label="项目名称"></el-table-column>
        <el-table-column prop="property" label="项目属性"></el-table-column>
        <el-table-column
          prop="esgDescription"
          label="项目描述"
        ></el-table-column>

        <el-table-column label="项目评分">
          <template #default="scope">
            <!-- {{ scope.row.score }} -->
            <span v-if="scope.row.score > 0">{{ scope.row.score }}</span>
          </template>
        </el-table-column>

        <el-table-column label="详情" width="180" align="center">
          <template #default="scope">
            <el-button
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row)"
              >编辑
            </el-button>
            <!-- <el-button
              type="text"
              icon="el-icon-delete"
              class="red"
              @click="handleDelete(scope.$index, scope.row)"
              >删除</el-button
            > -->
          </template>
        </el-table-column>

        <!-- 这里需要添加一个 fileUrl -->
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
      <!-- 调整这个值来改变空间的大小 -->
    </div>

    <!-- 编辑弹出框 -->
    <el-dialog title="项目信息修改" v-model="editVisible" width="60%">
      <el-form ref="formRef" :rules="rules" :model="form" label-width="70px">
        <el-form-item label="ID">
          <el-input v-model="form.id" disabled></el-input>
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
          <el-input v-model="form.esgDescription"></el-input>
        </el-form-item>
        <!-- 修改的时候禁止修改评分，且评分没有，不予显示 -->
        <!-- <el-form-item label="项目评分">
          <el-input v-model="form.score" disabled></el-input>
        </el-form-item> -->

        <!-- <el-form-item label="文件提交" prop="file"> -->
        <el-form-item label="文件提交" prop="file">
          <div class="container">
            <!-- <div class="plugins-tips"></div> -->
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

            <!-- <div class="content-title">支持裁剪</div> -->
          </div>
        </el-form-item>
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
import { fetchData } from "../../api/getProjectTab";
import { update } from "../../api/update";

export default {
  name: "projectManage",
  setup() {
    const query = reactive({
      pageIndex: 1,
      pageSize: 10,
    });

    // 用于存储搜索的参数
    const searchParam = reactive({
      esgName: "",
      property: "",
    });

    const tableData = ref([]);
    const allData = ref([]);
    const searchData = ref([]);
    const Total = ref(0);
    const formRef = ref(null);

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
      searchData.value = allData.value.filter((item) =>
        item.esgName.toLowerCase().includes(searchParam.esgName.toLowerCase())
      );
      handlePageChange(1);
    };

    const searchProperty = () => {
      searchParam.esgName = "";
      searchData.value = allData.value.filter((item) =>
        item.property.toLowerCase().includes(searchParam.property.toLowerCase())
      );
      handlePageChange(1);
    };

    const resetSearch = () => {
      searchParam.esgName = "";
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

    const rules = {
      id: [{ required: true, message: "请输入id", trigger: "blur" }],
      // pictureUrl: [{ required: false, message: "请提交文件", trigger: "blur" }],
      // fileUrl: [{ required: false, message: "请提交文件", trigger: "blur" }],
      esgDescription: [
        { required: true, message: "请输入项目描述", trigger: "blur" },
      ],
      esgName: [{ required: true, message: "请输入项目名称", trigger: "blur" }],
      property: [
        { required: true, message: "请输入项目属性", trigger: "blur" },
      ],
    };

    // 删除操作
    const handleDelete = (index) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          ElMessage.success("删除成功");
          tableData.value.splice(index, 1);
        })
        .catch(() => {});
    };

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    const form = reactive({
      id: 0,
      esgName: "",
      property: "",
      esgDescription: "",
      score: 0,
      file: [],
      fileUrl: ["测试fileUrl"],
      pictureUrl: ["测试pictureUrl1", "测试pictureUrl2"],
    });

    let idx = -1;
    const handleEdit = (index, row) => {
      idx = index;
      Object.keys(form).forEach((item) => {
        if (item in row) {
          form[item] = row[item];
        }
      });
      if (form.score > 0) {
        editVisible.value = false;
        ElMessage.error(`已有评分，无法修改`);
      } else {
        editVisible.value = true;
      }
    };

    const postData = reactive({
      id: 0,
      description: "",
      // companyName: "",
      fileUrl: [""],
      // pictureUrl: [""],
    });

    const copyData = () => {
      postData.id = form.id;
      postData.description = form.esgDescription;
      postData.fileUrl = form.fileUrl;
      // postData.pictureUrl = form.pictureUrl;
    };

    // 提交更新
    const saveEdit = () => {
      formRef.value.validate((valid) => {
        if (valid) {
          copyData();
          update(postData)
            .then((response) => {
              if (response.code === 2000) {
                console.log(postData);
                console.log(postData.fileUrl);
                console.log(postData.pictureUrl);
                console.log(response);
                Object.keys(form).forEach((item) => {
                  // tableData.value[idx][item] = form[item];
                  if (item in tableData.value[idx]) {
                    tableData.value[idx][item] = form[item];
                  }
                });
                ElMessage.success("更新成功");
              } else {
                ElMessage.error("更新失败");
              }
            })
            .catch((error) => {
              ElMessage.error("更新错误");
            });
        } else {
          return false;
        }
      });
      editVisible.value = false;
    };

    return {
      query,
      tableData,
      Total,
      editVisible,
      form,
      formRef,
      rules,
      searchParam,
      searchEsgName,
      searchProperty,
      resetSearch,
      handlePageChange,
      handleDelete,
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
  width: 200px;
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
