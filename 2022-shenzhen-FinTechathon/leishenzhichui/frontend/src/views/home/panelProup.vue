<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="24" :sm="12" :lg="6" class="card-panel-col" v-for="(list, i) in lists" :key="i">
      <div class="card-panel" :style="{background: list.color}">
        <div class="card-panel-description">
          <div class="card-panel-text">
            {{list.name}}
          </div>
          <count-to :start-val="list.starVal" :end-val="list.endVal" :duration="list.duration" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>

</template>

<script>
import CountTo from 'vue-count-to'

export default {
  components: {
    CountTo
  },
  data() {
    return {
      lists: [
        {starVal: 0,endVal: 70000,duration: 4000,name: '最高可借金额',color: '#67c23a'},
        {starVal: 0,endVal: 13600,duration: 3600,name: '回报率',color: '#e6a23c'},
        {starVal: 0,endVal: 6666,duration: 2000,name: '业绩领跑',color: '#f56c6c'},
        {starVal: 0,endVal: 8888,duration: 1600,name: '安稳底薪战队',color: '#409eff'},
      ]
    }
  }
}
</script>

<style scoped lang="scss">
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    .card-panel-description {
        font-weight: bold;
        text-align: center;
        margin-top: 20px;
        display: flex;
        flex-direction: column;
      .card-panel-text {
        line-height: 18px;
        color: #333;
        font-size: 16px;
        margin-bottom: 18px;
      }

      .card-panel-num {
        font-size: 20px;
        line-height: 18px;
      }
    }
  }
}
</style>