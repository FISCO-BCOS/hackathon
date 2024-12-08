<template>
  <header class="header">
    <div class="lines">
      <div class="line1" />

    </div>
    <div class="content">
      <div class="logo">
        <span class="material-icons rotate">block</span>
        <span class="material-icons rotate">chain</span>

      </div>
      <div class="text-center text-3xl font-bold">基于区块链的 AIGC 全生命周期版权管理系统</div>
    </div>
    <div class="lines2">

      <div class="line2" />

    </div>
  </header>
</template>

<script>
export default {
  name: 'Header'
}
</script>

<style>
.header {
  height: 15vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.lines {
  position: absolute;
  top: 45%;
  left: 0;
  width: 100%;
  display: flex;
  justify-content: space-around;
  align-items: center;
  transform: translateY(-50%);
}
.lines2 {
  position: absolute;
  top: 70%;
  left: 0;
  width: 100%;
  display: flex;
  justify-content: space-around;
  align-items: center;
  transform: translateY(-50%);
}
.line1 {
  width: 100%;
  height: 4px;
  background-color: cornflowerblue;
  animation: move 3s infinite;
}
.line2 {
  width: 100%;
  height: 4px;
  background-color: gray;
  animation: move 3s infinite;
}

@keyframes move {
  0% {
    transform: translateX(-50%);
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
  100% {
    transform: translateX(50%);
    opacity: 1;
  }
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  z-index: 0;
}

.logo {
  display: flex;
}

.title {
  font-size: 1.5rem;
  font-family: 'Arial', sans-serif;
  color: black;
}

.rotate {
  animation: spin 4s infinite linear;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
