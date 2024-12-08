<template>
  <div class="container">
    <div ref="gridsContainerEl" class="cluster">
      <div class="grids" :style="gridsStyle">
        <div v-for="i in gridsNum" :key="i" class="grid block">
          <div class="grid-layer block"
               :style="[gridLayerStyle, 'animationDelay:' + (i % 4 / gridsNum) * 2 + 's']"
               @click="layerClicked(i)"
          ></div>
          <div v-if="eggShow" class="board" :style="[styles[i - 1]]">
            <p class="shining neonText"> {{ names[i - 1] }} </p>
          </div>
        </div>
      </div>
      <canvas ref="canvasEl" class="path-canvas" :style="canvasLayerStyle"></canvas>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { neonPath } from "./neon.js";
//import { main } from "./index";

const props = defineProps({
  width: {
    type: Number,
    default: 800
  },
  height: {
    type: Number,
    default: 500
  },
  blockSize: {
    type: Number,
    default: 100
  },
  gap: {
    type: Number,
    default: 80
  },
  rows: {
    type: Number,
    default: 4
  },
  cols: {
    type: Number,
    default: 4
  },
  layerHeight: {
    type: Number,
    default: 15
  }
})

const gridsNum = computed(() => props.cols * props.rows)

//const { names, styles } = main(gridsNum.value);
let eggShow = ref(false)
const picked = Math.round(Math.random() * gridsNum.value)

function layerClicked(index:number) {
  // if (index === picked) {
  //     eggShow.value = true;
  // }
}

const gridsStyle = computed(() => {
  return {
    gap: `${props.gap}px`,
    gridTemplateRows: `repeat(${props.rows}, ${props.blockSize}px)`,
    gridTemplateColumns: `repeat(${props.cols}, ${props.blockSize}px)`
  }
})

const canvasLayerStyle = computed(() => {
  return {
    transform: `translateZ(${props.layerHeight}px)`
  }
})

const gridLayerStyle = computed(() => {
  return {
    transform: `translateZ(${2 * props.layerHeight}px)`
  }
})

const canvasEl = ref<HTMLCanvasElement | null>(null)
const gridsContainerEl = ref<HTMLDivElement | null>(null)

onMounted(() => {
  const w = gridsContainerEl.value?.offsetWidth
  const h = gridsContainerEl.value?.offsetHeight
  const canvas = canvasEl.value
  if (canvas) {
    canvas.width = w ?? props.width;
    canvas.height = h ?? props.height;
    var neon = neonPath(canvas, {
      blockWidth: props.blockSize,
      gap: props.gap,
      rows: props.rows,
      cols: props.cols,
      speed: 2.5,
      neonSize: 3
    })
    neon.init()
  }
});
</script>

<style scoped>
.container {
  transform-style: preserve-3d;
  pointer-events: all;
}

.cluster {
  margin-top: 60px;
  margin-left: auto;
  width: fit-content;
  transform-style: preserve-3d;
  transform: rotateX(60deg) rotateZ(45deg);
}

.grids {
  display: grid;
  transform-style: preserve-3d;
}

.grid {
  position: relative;
  background-color: white;
  transform-style: preserve-3d;
  box-shadow: 40px 40px 40px 10px #87878788;
}

.grid-layer {
  width: 100%;
  height: 100%;
  position: absolute;
  transform: translateZ(30px);
  backdrop-filter: blur(10px);
  animation: float 2s ease-in-out infinite;
  background-color: rgba(255, 255, 255, .1);
}

.block {
  border-radius: 6px;
  border: solid rgb(84, 84, 84) 1px;
}

.path-canvas {
  transform: translateZ(15px);
  position: absolute;
  left: 0;
  top: 0;
}

@keyframes float {
  0% {
    transform: translateZ(30px);
  }

  50% {
    transform: translateZ(40px);
  }

  100% {
    transform: translateZ(30px);
  }
}

.board {
  opacity: 0;
  animation: fade-in 2s ease;
}

@keyframes fade-in {
  0% {
    opacity: 0;
    transform: translateZ(40px) rotateZ(-45deg) rotateX(-60deg);
  }

  40% {
    opacity: 1;
  }

  80% {
    opacity: 1;
    transform: translateZ(60px) rotateZ(-45deg) rotateX(-60deg);
  }

  100% {
    opacity: 0;
    transform: translateZ(70px) rotateZ(-45deg) rotateX(-60deg);
  }
}

.neonText {
  color: #000;
  font-weight: bolder;
  text-shadow: 0 0 6px #000
}

.shining {
  animation: flicker 1.5s infinite alternate;
}

/* Flickering animation */
@keyframes flicker {

  0%,
  18%,
  22%,
  25%,
  53%,
  57%,
  100% {
    text-shadow: 0 0 8px #000;
    color: #000;
  }

  20%,
  24%,
  55% {
    text-shadow: none;
    color: gray;
  }
}
</style>
