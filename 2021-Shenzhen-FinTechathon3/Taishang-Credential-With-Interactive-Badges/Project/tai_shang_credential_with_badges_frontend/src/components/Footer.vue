<template>
  <div class="footer">
    <span class="copyright">© 2021</span>
    <span class="project-name">NONCE GEEK STUDIO.</span>
    <span class="all-rights">All rights reserved</span>
  </div>
</template>

<script>

export default {
  name: 'Footer',
};
</script>

<style lang="scss" scoped>
.footer {
  margin-top: 40px;
  padding: 40px 0 60px;
  border-top: 1px solid #ccc;
  text-align: center;

  .project-name {
    font-weight: bold;
    margin: 0 .5em;
  }
}
</style>
