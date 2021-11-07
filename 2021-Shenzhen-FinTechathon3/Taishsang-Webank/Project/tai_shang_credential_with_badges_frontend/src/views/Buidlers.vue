<template>
  <div class="buidlers">
    <div class="header">
      <div class="main">{{ header.main }}</div>
      <div class="sub">{{ header.sub }}</div>
    </div>
    <div class="buidler-list">
      <a-card
        class="buidler"
        v-for="buidler in buidlers"
        :key="buidler.name"
        :title="buidler.name"
        hoverable
      >
        <img class="logo" :src="buidler.logo" alt="">
        <span class="intro">{{ buidler.intro }}</span>
        <span class="github">
          GitHub: <a :href="buidler.github">{{ buidler.github }}</a>
        </span>
        <span class="articles">
          Articles:
          <a
            class="article"
            v-for="article in buidler.articles"
            :key="article.url"
            :href="article.url"
          >{{ article.title }}</a>
        </span>
      </a-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Buidlers',
  data() {
    return {
      header: {
        main: 'Buidlers',
        sub: 'Nonce Geek Studio 核心成员',
      },
      buidlers: [
        {
          name: '李大狗',
          logo: 'avatars/leeduckgo.jpeg',
          intro: '面向炫酷编程',
          github: 'https://github.com/leeduckgo',
          articles: [
            {
              title: '1 小时理解比特币系统',
              url: 'https://learnblockchain.cn/article/2595',
            },
            {
              title: '区块链与共识机制演变史',
              url: 'https://learnblockchain.cn/article/2741',
            },
          ],
        },
        {
          name: '王宁波',
          logo: 'avatars/wangningbo.jpeg',
          intro: '学习区块链开发途中的平头哥。',
          github: 'https://github.com/hqwangningbo',
          articles: [
            {
              title: '构建你的第一条Substrate chain并与之交互',
              url: 'https://blog.csdn.net/qq_45332753/article/details/120627087',
            },
            {
              title: 'Substrate-node-template模板中的pallet-template源码分析',
              url: 'https://blog.csdn.net/qq_45332753/article/details/120627981',
            },
          ],
        },
        {
          name: '汤会枫',
          logo: 'avatars/tanghuifeng.png',
          intro: '懂点区块链和云计算的后端开发。',
          github: 'https://github.com/99Kies',
          articles: [
            {
              title: '分布式数字身份-从创建一个Weidentity开始',
              url: 'https://huifeng.blog.csdn.net/article/details/119681063',
            },
            {
              title: '理解和验证Pbft共识机制',
              url: 'https://huifeng.blog.csdn.net/article/details/112223484',
            },
          ],
        },
        {
          name: '何伟',
          logo: 'avatars/hewei.png',
          intro: '生命不息，学习不止。',
          github: 'https://github.com/Dream4ever',
          articles: [
            {
              title: '手写一个音频播放器',
              url: 'https://github.com/Dream4ever/Knowledge-Base/issues/139',
            },
            {
              title: '解决 MySQL CPU 占用高的问题',
              url: 'https://github.com/Dream4ever/Knowledge-Base/issues/149',
            },
          ],
        },

        {
          name: '姚溯宁',
          logo: 'avatars/yaosuning.png',
          intro: '一直是个非传统的人，数学专业的前端开发。',
          github: 'https://github.com/fewwwww',
          articles: [
            {
              title: '浅谈Dfinity的云计算+区块链',
              url: 'https://blog.suningyao.com/docs/Blockchain/dfinity',
            },
            {
              title: 'React组件拖拽改变大小功能',
              url: 'https://blog.suningyao.com/docs/Frontend/draggable',
            },
          ],
        },
        {
          name: '肖越',
          logo: 'avatars/xiaoyue.png',
          intro: '一个懂点网络安全和区块链的信安大三学僧。',
          github: 'https://github.com/xiaoyue2019',
          articles: [
            {
              title: '合约安全之越权攻击',
              url: 'https://blog.csdn.net/xiaoyue2019/article/details/107898092?spm=1001.2014.3001.5502',
            },
            {
              title: 'FISCO BCOS多机部署之单群组双机构双节点组网模式',
              url: 'https://blog.csdn.net/xiaoyue2019/article/details/107500811',
            },
          ],
        },
        {
          name: 'Henry Liu',
          logo: 'avatars/henryliu.png',
          intro: '到现在都没搞懂啥是区块链的菜鸟。',
          github: 'https://github.com/Zombieliu?tab=repositories',
          // articles: [
          //   {
          //     title: '从比特币到以太坊到波卡',
          //     url: 'https://zhuanlan.zhihu.com/p/341318279',
          //   },
          //   {
          //     title: '为什么我们需要波卡？波卡的跨链网络效应是什么？',
          //     url: 'https://www.zvstus.com/article/news/1/75d87ffffe85571af600e15826a50000.html',
          //   },
          // ],
        },
        {
          name: '叶开',
          logo: 'avatars/yekai.png',
          intro: '非著名区块链技术讲师。',
          github: 'https://github.com/yekai1003',
          articles: [
            {
              title: '看过《斯巴达克斯》吗？里面的反派竟是密码学鼻祖',
              url: 'https://blog.csdn.net/weixin_42175021/article/details/118500078?spm=1001.2014.3001.5501',
            },
            {
              title: '比特币和区块链的基石：哈希函数',
              url: 'https://blog.csdn.net/weixin_42175021/article/details/118500078?spm=1001.2014.3001.5501',
            },
          ],
        },
        {
          name: '蓉儿',
          logo: 'avatars/ronger.png',
          intro: '生成艺术家。',
          github: 'https://github.com/lovelyrosa',
          articles: [
            {
              title: 'cv exhibition',
              url: 'https://www.cryptovoxels.com/play?coords=N@10W,475S',
            },
          ],
        },
        {
          name: '黄杰',
          logo: 'avatars/huangjie.png',
          intro: '区块链编程爱好者。',
          github: 'https://github.com/Blockchain-Key',
          articles: [
            {
              title: '智能合约实例开发（1）——众筹',
              url: 'https://blog.csdn.net/weixin_43402353/article/details/109275960',
            },
            {
              title: '智能合约实例开发（2）——食品溯源',
              url: 'https://blog.csdn.net/weixin_43402353/article/details/109409249',
            },
            {
              title: '智能合约开发实例（3）——结婚证书',
              url: 'https://blog.csdn.net/weixin_43402353/article/details/119190719',
            },
          ],
        },
        // {
        //   name: 'WANGNINGBO',
        //   logo: 'https://via.placeholder.com/150',
        //   intro: 'BalaBala',
        //   github: 'https://github.com/hqwangningbo',
        //   articles: [
        //     {
        //       title: '从比特币到以太坊到波卡',
        //       url: 'https://zhuanlan.zhihu.com/p/341318279',
        //     },
        //     {
        //       title: '为什么我们需要波卡？波卡的跨链网络效应是什么？',
        //       url: 'https://www.zvstus.com/article/news/1/75d87ffffe85571af600e15826a50000.html',
        //     },
        //   ],
        // },
        // {
        //   name: 'WANGNINGBO',
        //   logo: 'https://via.placeholder.com/150',
        //   intro: 'BalaBala',
        //   github: 'https://github.com/hqwangningbo',
        //   articles: [
        //     {
        //       title: '从比特币到以太坊到波卡',
        //       url: 'https://zhuanlan.zhihu.com/p/341318279',
        //     },
        //     {
        //       title: '为什么我们需要波卡？波卡的跨链网络效应是什么？',
        //       url: 'https://www.zvstus.com/article/news/1/75d87ffffe85571af600e15826a50000.html',
        //     },
        //   ],
        // },
        // {
        //   name: 'WANGNINGBO',
        //   logo: 'https://via.placeholder.com/150',
        //   intro: 'BalaBala',
        //   github: 'https://github.com/hqwangningbo',
        //   articles: [
        //     {
        //       title: '从比特币到以太坊到波卡',
        //       url: 'https://zhuanlan.zhihu.com/p/341318279',
        //     },
        //     {
        //       title: '为什么我们需要波卡？波卡的跨链网络效应是什么？',
        //       url: 'https://www.zvstus.com/article/news/1/75d87ffffe85571af600e15826a50000.html',
        //     },
        //   ],
        // },
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

.buidler-list {
  margin-top: 40px;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;

  .buidler {
    width: 48%;
    margin-top: 20px;

    ::v-deep .ant-card-body {
      display: flex;
      flex-direction: column;

      .logo {
        height: 150px;
        margin: 0 auto;
      }

      .intro, .github {
        margin-top: 10px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }

      .articles {
        margin-top: 10px;
        display: flex;
        flex-direction: column;

        .article {
          margin-top: 5px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }

      // TODO: 如果设置 a 标签的父元素只占一行
      // 则 a 标签之后的 … 字符和 a 标签样式不一致
      // 如果像下面这样设置 a 标签只占一行就更不行了
      // https://stackoverflow.com/questions/34294054/how-to-implement-single-line-ellipsis-with-css
      // .github>a {
      //   display: block;
      //   white-space: nowrap;
      //   overflow: hidden;
      //   text-overflow: ellipsis;
      // }
    }
  }
}
</style>
