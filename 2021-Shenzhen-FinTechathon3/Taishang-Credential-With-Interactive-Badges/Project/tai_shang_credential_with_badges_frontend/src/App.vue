<template>
  <div id="app">
    <TopBar />
    <Navs />
    <div class="page">
      <router-view/>
    </div>
    <Footer />
  </div>
</template>

<script>
import TopBar from './components/TopBar.vue';
import Navs from './components/Navs.vue';
import Footer from './components/Footer.vue';

export default {
  components: {
    TopBar,
    Navs,
    Footer,
  },
};
</script>

<style lang="scss">
#app {
  width: 900px;
  margin: 0 auto;
  min-height: 100%;
  display: flex;
  flex-direction: column;

  .page {
    flex: 1;
    margin: 100px 0;
  }
}
</style>
