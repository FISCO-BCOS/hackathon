<template>
  <div class="top-bar">
    <div class="logo">
<!--      <div class="nonce">NoNcE</div>-->
      <div class="geek-studio">TaiShang-Verifiable-Credential-with-Badges</div>
    </div>
    <!-- <div class="modes">
      <span>☀️</span>
      <span>🌙</span>
    </div> -->
  </div>
</template>

<script>
export default {
  name: "TopBar",
};
</script>

<style lang="scss" scoped>
@font-face {
  font-family: "Blox";
  src: url("~@/assets/fonts/Blox.ttf");
}

@font-face {
  font-family: "Minecraft";
  src: url("~@/assets/fonts/Minecraft.ttf");
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 20px;
}

.logo {
  display: flex;
  justify-content: flex-start;
  align-items: baseline;
  line-height: 1;

  .nonce {
    font-family: "Blox";
    font-size: 85px;
  }

  .geek-studio {
    font-family: "Minecraft";
    font-size: 40px;
    margin-left: 20px;
  }
}
</style>
