<template>
    <div class="about">
      <div class="header">Contact Us</div>
      <br>
      <img class="qr"src="qr_code.png" />
    </div>
</template>

<script>
export default {
  name: 'About',
};
</script>

<style lang="scss" scoped>
  .about {
    text-align: center;
    .header {
      font-size: 30px;
      font-weight: bolder;
      font-family: 'Minecraft';
      src: url('~@/assets/fonts/Minecraft.ttf');
    }
  }
  .qr {
    height: 200px;
  }
</style>
