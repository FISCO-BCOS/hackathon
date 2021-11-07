<template>
  <div class="supporters">
    <div class="header">支持机构</div>
    <a-card
      v-for="supporter in supporters"
      :key="supporter.name"
      class="supporter"
      hoverable
    >
      <img slot="cover" :src="supporter.logo" alt="">
      <span class="intro">{{ supporter.name }}</span>
    </a-card>
  </div>
</template>

<script>
export default {
  name: 'Supporters',
  data() {
    return {
      supporters: [
        {
          name: '上海对外经贸大学区块链 SIG',
          logo: 'https://via.placeholder.com/100',
        },
        {
          name: '南京大学金融x区块链 SIG',
          logo: 'https://via.placeholder.com/100',
        },
        {
          name: '微芒社区',
          logo: 'https://via.placeholder.com/100',
        },
        {
          name: 'BeWater 开发者社区',
          logo: 'https://via.placeholder.com/100',
        },
      ],
    };
  },
};
</script>

<style lang="scss" scoped>
.header {
  text-align: center;
  font-size: 30px;
  font-weight: bolder;
}

.supporter {
  margin-top: 30px;
  padding: 20px;
  display: flex;
  align-items: center;

  .intro {
    margin-left: 10px;
    font-size: 18px;
  }
}
</style>
