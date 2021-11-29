<template>
  <div class="taishang">
    <div class="header">
      <div class="main">{{ header.main }}</div>
      <div class="sub">{{ header.sub }}</div>
    </div>
    <div class="projects">
      <a-card
        class="project"
        v-for="project in projects"
        :key="project.name"
        :title="project.name"
        hoverable
      >
        <!-- <img class="logo" :src="project.logo" alt=""> -->
        <span class="intro">{{ project.intro }}</span>
        <!-- <span class="website">
          Wesite: <a :href="project.website">{{ project.website }}</a>
        </span> -->
        <span class="repo">
          Repo: <a :href="project.repo">{{ project.name }}</a>
        </span>
      </a-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TaiShang',
  data() {
    return {
      header: {
        main: 'Project 0x01 TaiShangVerse',
        sub: '基于DID 与 NFT 的太上 Web 3.0 世界',
      },
      projects: [
        // {
        //   name: 'Tai-Shang',
        //   logo: 'https://via.placeholder.com/150',
        //   intro: '太上老君，基于 Elixir 的 NFT 炼丹炉',
        //   website: 'https://taishang.leeduckgo.com/',
        //   repo: 'https://github.com/WeLightProject/Tai-Shang',
        // },
        {
          name: 'WeLight-Blockchain-OS',
          logo: 'https://via.placeholder.com/150',
          intro: '微芒虚拟区块链操作系统',
          website: 'https://github.com/WeLightProject/WeLightBlockchainOS',
          repo: 'https://github.com/WeLightProject/WeLightBlockchainOS',
        },
        {
          name: 'Tai-Shang-NFT-Wallet',
          logo: 'https://via.placeholder.com/150',
          intro: '为下一代 NFT 范式服务的 NFT 钱包',
          website: 'https://bewater.leeduckgo.com/',
          repo: 'https://github.com/WeLightProject/Tai-Shang-NFT-Wallet',
        },
        {
          name: 'tai-shang-nft-parser',
          logo: 'https://via.placeholder.com/150',
          intro: '太上 NFT 解析器，搭配太上 NFT 钱包一同使用',
          website: 'https://github.com/WeLightProject/tai-shang-nft-parser',
          repo: 'https://github.com/WeLightProject/tai-shang-nft-parser',
        },
      ],
    };
  },
};
</script>

<style lang="scss" scoped>
.header {
  text-align: center;

  .main {
    font-size: 30px;
    font-weight: bolder;
    font-family: 'Minecraft';
    src: url('~@/assets/fonts/Minecraft.ttf');
  }

  .sub {
    font-size: 20px;
    margin-top: 10px;
  }
}

.projects {
  margin-top: 40px;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;

  .project {
    width: 32%;
    margin-top: 20px;

    ::v-deep .ant-card-body {
      display: flex;
      flex-direction: column;

      .logo {
        width: 150px;
        margin: 0 auto;
      }

      .intro, .website, .repo {
        margin-top: 10px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      // TODO: 如果设置 a 标签的父元素只占一行
      // 则 a 标签之后的 … 字符和 a 标签样式不一致
      // 如果像下面这样设置 a 标签只占一行就更不行了
      // https://stackoverflow.com/questions/34294054/how-to-implement-single-line-ellipsis-with-css
      // .website>a, .repo>a {
      //   display: block;
      //   white-space: nowrap;
      //   overflow: hidden;
      //   text-overflow: ellipsis;
      // }
    }
  }
}
</style>
