<template>
  <div class="background2">
    <canvas ref="canvas"></canvas>
  </div>
</template>

<script>
import { ref, onMounted, watchEffect } from 'vue';

export default {
  name: 'DynamicBackground',
  props: {
    lineCount: {
      type: Number,
      default: 10,
    },
    lineColor: {
      type: String,
      default: '#ff0000',
    },
    animationSpeed: {
      type: Number,
      default: 5,
    },
  },
  setup(props) {
    const canvas = ref(null);
    const ctx = ref(null);

    onMounted(() => {
      if (canvas.value) {
        ctx.value = canvas.value.getContext('2d');
        drawBackground();
      }
    });

    watchEffect(() => {
      if (ctx.value) {
        drawBackground();
      }
    });

    const drawBackground = () => {
      const width = canvas.value.width = window.innerWidth;
      const height = canvas.value.height = window.innerHeight;
      ctx.value.clearRect(0, 0, width, height);
      ctx.value.lineWidth = 1;
      ctx.value.strokeStyle = props.lineColor;

      for (let i = 0; i < props.lineCount; i++) {
        const x1 = Math.random() * width;
        const y1 = Math.random() * height;
        const x2 = Math.random() * width;
        const y2 = Math.random() * height;
        ctx.value.beginPath();
        ctx.value.moveTo(x1, y1);
        ctx.value.lineTo(x2, y2);
        ctx.value.stroke();
      }

      // Animate lines (optional) -  This example just redraws, for more sophisticated animation, consider using requestAnimationFrame
      setTimeout(drawBackground, 1000 / props.animationSpeed);
    };

    return { canvas };
  },
};
</script>

<style scoped>
.background2 {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1; /* Ensure it's behind other content */
  pointer-events: none; /* Prevent lines from interfering with other elements */

}

canvas {
  width: 100%;
  height: 100%;
}

</style>
