<template>
  <div class="container">
    <div class="background">
      <Background :cols="5" />
    </div>
    </div>

</template>
<script>
import Background2 from '@/view/backgroundtest/index.vue'
import Background from '@/view/background/index.vue'
export default {
  components: {
    // eslint-disable-next-line vue/no-unused-components
    Background2,
    Background
    // Header
  }}
</script>
<style>

.container {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}
.controls input[type="range"] {
  width: 85%; /* 调整拖拉条的宽度，这里设置为 80% */
}

.background {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  width: 70%; /* 调整背景占据屏幕宽度的比例，这里设置为 50% */
  z-index: 100; /* 将背景层级设为 -1，使其位于其他内容之后 */
  background-color: #ffffff;
}
.background2 {
  position: absolute;

  width: 100%;
  height: 100%;
  z-index: 5;
  overflow: hidden;
  pointer-events: none;
}

.controls input[type="range"] {
  width: 85%;
  /* 修改滑块的背景色为红色 */
  -webkit-appearance: none;
  appearance: none;
  height: 10px;
  background: #007bff; /* 默认背景色 */
  border-radius: 5px;
}

.controls input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 20px;
  height: 20px;
  background: #ff0000; /* 修改滑块颜色为红色 */
  border-radius: 50%;
  cursor: pointer;
}
.controls {
  position: absolute;
  top: 50px;
  left: 50px;
  width: 500px;
  height: 600px;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.8);
  z-index: 2;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}



.input-field {
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
  transition: border-color 0.3s;
}

.input-field:focus {
  border-color: #007bff;
}

button {
  display: block;
  width: 100%;
  padding: 10px;
  margin-top: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

label {
  margin-top: 10px;
  display: block;
}

.preview {
  position: absolute;
  top: 100px;
  right: 0;
  left: 700px;
  width: 500px;
  height: 500px;
  padding: 30px;
  background-color: white;
  z-index: 2;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.image-preview img {
  max-width: 100%;
  height: auto;
}

</style>
