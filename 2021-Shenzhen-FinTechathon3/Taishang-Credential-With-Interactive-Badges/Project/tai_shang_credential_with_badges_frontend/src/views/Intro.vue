<template>
  <div class="intro">
    <div class="header">
      <div class="main">TaiShang-Verifiable-Credential-with-Badges</div>
      <div class="sub">——渲染验证证书基础设施</div>
    </div>
    <div class="section">
      <div class="section-name"><center>我们的认知</center></div>
      <p class="para">
        太上携带数字徽章的可验证证书（TaiShang Verifiable Credential with
        Badge）是一个 基于 React 框架的 DApp，主要功能包括：
      </p>
      <p class="para">
        区块链技术从诞生之日始，节点和账户就是两个完全解耦的对象模型，从而给分布式数字身份体系的发展带来了新的可能性。在分布式数字身份体系这种新的身份体系下，数字身份不再被联盟方和应用方所垄断，数字身份的所有权和使用权回归用户自身，从而数字身份相关的数据也被用户自身所掌握，被自身所管理，将数据开放给谁也完全由用户自己授权。这种数字身份在权属上也被称为自主型身份（Self-sovereign
        Identity）。
      </p>
      <p class="para">
        在分布式数字身份中，身、份、数据三项内容是解耦的。身也即是主体标识符；份也即是主体的属性，通常表现为可信凭证的形式；数据也即是可信凭证相关联的数据。在w3c所定义的分布式数字身份标准中，这三者通过DID文档（DID
        Document）有机地统一在一起。
      </p>
      <ul>
        <li class="list"><b>安全</b></li>
      </ul>
      <p class="para">
        身份所有者身份信息不被无意泄露，身份可以由身份持有者持久保存，身份信息提供可符合最小披露原则。
      </p>
      <ul>
        <li class="list"><b>身份自主可控</b></li>
      </ul>
      <p class="para">
        用户可以自主管理身份，而非依赖可信第三方；身份所有者可以控制其身份数据的分享。
      </p>
      <ul>
        <li class="list">
          <b>身份可移植</b>
        </li>
      </ul>
      <p class="para">
        身份所有者能够在任何他们需要的地方使用其身份数据，而不需依赖特定的身份服务提供商。
      </p>
      <ul>
        <li class="list"><b>高效</b></li>
      </ul>
      <p class="para">
        由于身份及身份相关数据的所有权和使用权都回归个人，平台所需要做的事情大大减少，需要统一大量操作等传统CA证书中心中可能导致效率短板的原因不再存在，从而提升了整个身份系统的效率。
      </p>
    </div>
    <div class="section">
      <div class="section-name"><center>原则</center></div>
      <p class="para">
        在构建下一代区块链基础设施的时候，我们遵循如下的「Principles」：
      </p>
      <ul>
        <li class="list"><b>不迎合热点</b></li>
      </ul>
      <p class="para">
        我们不会为了迎合热点去随便成立一个项目。有时候看似我们的项目追上了热点，其实是因为我们的战略方向刚好和热点一致：）。
      </p>
      <ul>
        <li class="list"><b>可能反共识</b></li>
      </ul>
      <p class="para">
        我们构建的区块链基础设施可能在现在是「反共识」的。例如很多公链开发者认为联盟链是伪命题，但我们认为基础设施兼容联盟链和公链是必要的。
      </p>
      <ul>
        <li class="list"><b>谨慎</b></li>
      </ul>
      <p class="para">
        就像巴菲特认为假设一生只投资二十次。同样的，我们公布项目的时候也会非常谨慎！我们只公布我们认为极具价值的且我们会100%投入的项目！
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "Intro",
  data() {
    return {};
  },
};
</script>

<style lang="scss" scoped>
.header {
  text-align: center;

  .main {
    font-size: 30px;
    font-weight: bolder;
    font-family: "Minecraft";
    src: url("~@/assets/fonts/Minecraft.ttf");
  }

  .sub {
    font-size: 20px;
    margin-top: 10px;
  }
}

.section {
  .section-name {
    font-size: 18px;
    font-weight: bolder;
    padding: 26px 0 15px;
  }
}
</style>
