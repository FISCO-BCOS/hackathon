<template>
  <div class="pagination">
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="pageNo"
      :page-sizes="pageSizes"
      :page-size.sync="curSize"
      :layout="layout"
      :total="total">
    </el-pagination>
  </div>
</template>

<script>
export default {
  props: {
    currentPage: {
      type: Number,
      default: 1
    },
    pageSizes: {
      type: Array,
      default() {
        return [10, 20, 30, 40];
      }
    },
    pageSize: {
      type: Number,
      default: 10
    },
    total: {
      type: Number
    },
    layout: {
      type: String,
      default: "total, sizes, prev, pager, next, jumper"
    }
  },
  computed: {
    pageNo: {
      get() {
        return this.currentPage;
      },
      set(val) {
        this.$emit('update:currentPage', val);
      }
    },
    curSize: {
      get() {
        return this.pageSize;
      },
      set(val) {
        this.$emit('update:pageSize', val);
      }
    }
  },
  methods: {
    handleSizeChange() {
      this.$emit('getList');
    },
    handleCurrentChange() {
      this.$emit('getList');
    }
  }
}
</script>

<style scoped>
.el-pagination {
  margin-top: 20px;
}
</style>