<template>
  <div class="navs">
    <router-link
      class="nav"
      v-for="route in $router.options.routes"
      :key="route.path"
      :to="route.path"
    >{{ route.meta.title }}</router-link>
  </div>
</template>

<script>

export default {
  name: 'Navs',
};
</script>

<style lang="scss" scoped>
@font-face {
  font-family: 'Minecraft';
  src: url('~@/assets/fonts/Minecraft.ttf');
}

.navs {
  display: flex;
  justify-content: space-between;
  border-top: 1px solid #ccc;
  margin-top: 20px;
  padding-top: 20px;

  .nav {
    font-family: 'Minecraft';
    font-size: 20px;
    color: inherit;

    &.router-link-exact-active {
      color: red;
      font-weight: bold;
    }
  }
}
</style>
