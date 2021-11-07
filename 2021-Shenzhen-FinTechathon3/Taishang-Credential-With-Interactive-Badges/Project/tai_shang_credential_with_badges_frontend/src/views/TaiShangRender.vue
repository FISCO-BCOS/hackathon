<template>
  <div class="about">
    <Upload @render-click="renderClick"/>
    <Button @click="renderCertificate">渲染证书</Button>
      <div id="cert" class="container" v-bind="certificateInfo" v-if="status">
        <div class="wrapper">
          <div class="cert-top">
            <img src="@/assets/images/background-top.jpg" alt="bg-top">
          </div>
          <div class="cert-middle">
            <div class="cert-title">
              <h1>
                <center>
                  {{certificateInfo.lesson_name}}
                  <br>信用等级证书
                </center>
              </h1>
            </div>
            <div class="cert-context">
              <div class="info">
                <div class="info-label">姓名：</div>
                <div class="info-value">{{ certificateInfo.ownerName }}</div>
              </div>
              <div class="info">
                <div class="info-label">weid：</div>
                <div class="info-value">{{ certificateInfo.weid }}</div>
              </div>
              <div class="info">
                <div class="info-label">信用证书名称：</div>
                <div class="info-value">{{ certificateInfo.lesson_name }} 信用等级证书</div>
              </div>
              <div class="info">
                <div class="info-label">信用评级：</div>
                <div class="info-value">⭐️⭐️⭐️</div>
              </div>
              <div class="info">
                <div class="info-label">发放日期：</div>
                <div class="info-value">{{ certificateInfo.date }}</div>
              </div>
              <div class="info">
                <div class="info-label">失效日期：</div>
                <div class="info-value">{{ certificateInfo.expirationDate }}</div>
              </div>
              <div class="info flex-column">
                <div class="info-label">存证合约地址：</div>
                <div class="info-value">{{ certificateInfo.nftAddr }}</div>
              </div>
              <div class="info flex-column">
                <div class="info-label">颁证方：</div>
                <div class="info-value issue">
                  <img class="issuer" src="../assets/images/weimang.png" alt="issuer">
                  <span>微芒金融管理委员会{{ certificateInfo.issuer }}</span>
                </div>
              </div>
              <div class="info flex-column">
                <div class="info-label">徽章（不可转让）：</div>
                <div class="badge--transferable">
                  <div class="card">
                    <img src="../assets/images/cms.png" style="width: 200px;height: 200px;border-radius:5px" class="card-img-top" alt="...">
                    <div class="card-body">
                      <h5 class="card-title">招行证券白金用户</h5>
                      <h5 class="card-title">
                        did:weid:3:0xbf29d133d0b45b2db1f</h5>
                      <h5 class="card-title">
                        89394813e5a384088ce40</h5>
                      <a href="#" class="btn btn-primary">Check it</a>
                    </div>
                  </div>
                  <div class="card">
                    <img src="../assets/images/bcos.png" style="width: 200px;height: 200px;border-radius:5px" class="card-img-top" alt="...">
                    <div class="card-body">
                      <h5 class="card-title">FISCO BCOS MVP</h5>
                      <h5 class="card-title">
                        did:weid:3:0xbf29d133d0b45b2db1f</h5>
                      <h5 class="card-title">
                   89394813e5a384088ce40</h5>
                      <a href="http://101.35.24.15/bcos.html" class="btn btn-primary">Check it</a>
                    </div>
                  </div>
                </div>
              </div>
              <div class="info flex-column">
                <div class="info-label">权益（可转让）：</div>
                <div class="badge--transferable">
                  <div class="card">
                    <img src="../assets/images/macdonalds.png" style="width: 200px;height: 200px" class="card-img-top" alt="...">
                    <div class="card-body">
                      <h5 class="card-title">金拱门白金卡</h5>
                      <h5 class="card-title">
                        did:weid:3:0xbf29d133d0b45b2db1f</h5>
                      <h5 class="card-title">
                        89394813e5a384088ce40</h5>
                      <a href="#" class="btn btn-primary">Check it</a>
<!--                      <a href="#" class="btn btn-primary">Transfer</a>-->
                    </div>
                  </div>
                  <div class="card">
                    <img src="../assets/images/jay.png" style="width: 200px;height: 200px;-webkit-filter: grayscale(1); /* Webkit */
filter: gray; /* IE6-9 */
filter: grayscale(1); /* W3C */" class="card-img-top" alt="...">
                    <div class="card-body">
                      <h5 class="card-title">周杰伦握手会</h5>
                      <h5 class="card-title">
                        did:weid:3:0x3f6a4fc9f8f36c534a</h5>
                      <h5 class="card-title">
                        4511c237bc805ad88da881</h5>
                      <a href="http://101.35.24.15/jay.html" class="btn btn-primary">Check it </a>
<!--                      <a href="#" class="btn btn-primary">Transfer</a>-->
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="cert-bottom">
            <img src="@/assets/images/background-bottom.jpg" alt="bg-top">
          </div>
        </div>
      </div>
</div>
</template>

<script>
import Upload from '@/components/Upload';
import { getTokenURI } from '../network/home';
import axios from 'axios';
import { Button } from 'ant-design-vue';

export default {
  name: 'TaiShangRender',
  data() {
    return {
      credentialData: {},
      status: false,
      certificateInfo: {},
    };
  },
  components: {
    Upload,
    Button
  },
  methods: {
    renderClick(certificateInfo) {
      this.certificateInfo = certificateInfo;
    },
    renderCertificate(){
      this.status = true
    },
  },
};
</script>

<style lang="scss" scoped>

#cert {
  margin-top: 40px;

  .cert-top img,
  .cert-bottom img {
    width: 100%;
  }

  .cert-middle {
    background: url(~@/assets/images/background-middle.jpg) repeat-y;
    background-size: 100%;

    .cert-context {
      padding: 0 110px;
      text-align: left;

      .info {
        margin-top: 2em;
        display: flex;

        &.flex-column {
          flex-direction: column;
        }

        .info-label {
          font-weight: bolder;
          margin-bottom: .5em;
        }

        .issue {
          display: flex;
          justify-content: flex-start;
          align-items: center;

          .issuer {
            width: 50px;
            height: 50px;
          }
        }
      }

      .badge--transferable {
        display: flex;
        justify-content: space-between;

        .card {
          width: 40%;
        }
      }
    }
  }
}

.about {
  text-align: center;
  .header {
    font-size: 30px;
    font-weight: bolder;
    font-family: "Minecraft";
    src: url("~@/assets/fonts/Minecraft.ttf");
  }
}
.qr {
  height: 200px;
}
//@import "./pacman.css";

</style>
