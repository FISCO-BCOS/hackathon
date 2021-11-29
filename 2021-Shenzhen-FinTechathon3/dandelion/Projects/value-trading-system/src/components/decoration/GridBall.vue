<template>
  <div class="box-grid" :style="`width: ${size*order}px; height: ${size*order}px;`">
    <div class="grid grid1" :style="`transform: rotate(${deg}deg);`">
      <div class="row" v-for="(row, index_row) in order" :key="index_row">
        <div class="col" v-for="(col, index_col) in order" :key="index_col"
        :style="`height: ${size}px; width: ${size}px;
        background: hsl(${hsl(order-index_row, index_col)},100%,75%)`">
        </div>
      </div>
    </div>
    <div class="grid grid2" :style="`transform: rotate(${-deg}deg);`">
      <div class="row" v-for="(row, index_row) in order" :key="index_row">
        <div class="col" v-for="(col, index_col) in order" :key="index_col"
        :style="`height: ${size}px; width: ${size}px; 
        background: hsl(${hsl(index_row, order-index_col) * 0.75},100%,75%)`">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "grid_ball",
  props: {
    // 阶数
    order: {
      type: Number,
      default: 4
    },
    // 每一个格子的大小
    size: {
      type: Number,
      default: window.innerWidth/4
    },
    // 光谱距离
    distance: {
      type: Number,
      default: 2
    }
  },
  data() {
    return {
      deg: parseInt(Math.random()*360),
      offset_saturation: parseInt(Math.random()*360)
    }
  },
  computed: {
    influence() {
      return 360/(this.order*this.order) / this.distance
    },
    hsl() {
      return (row, col) =>{
        return (row*row + col*col) * this.influence + this.offset_saturation
      }
    }
  },
  mounted() {
    setInterval(()=> {
      this.offset_saturation += this.order;
      this.deg += 3;
    }, 600);
  }
};
</script>

<style scoped>
.box-grid {
  position: relative;
  /* border-radius: 100%;
  overflow: hidden; */
}
.grid{
  position: absolute;
  top: 0; left: 0; bottom: 0; right: 0;
  transition: all 1.5s;
}
.grid2{
  opacity: 0.7;
}
.row {
  display: flex;
}
.col {
  transition: all 1.5s;
}
</style>