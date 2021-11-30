<template>
  <div class="grey lighten-4" style="height: 100%">
    <v-img :src="billboard.coverPic" class="align-end">
      <v-list-item class="background-black-transparent white--text">
        <v-list-item-avatar color="white">
          <v-img :src="platform.avatar" contain></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title class="title">
            {{ billboard.title }}
          </v-list-item-title>
          <v-list-item-subtitle class="white--text">{{
            platform.name
          }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-img>
    <v-card tile elevation="0" class="mb-1">
      <v-card-subtitle> 详细信息 </v-card-subtitle>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-subtitle> 总额度 </v-list-item-subtitle>
          <v-list-item-title class="py-2">
            <v-icon class="mr-2">mdi-coins</v-icon>
            {{ $splited(billboard.integralNeed) }}
          </v-list-item-title>
        </v-list-item-content>
        <v-list-item-action>
          <p-btn :value="billboard.progress" elevation="2"
          @click.native="sheet.show = true">申请</p-btn>
        </v-list-item-action>
      </v-list-item>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-subtitle> 计划参与方数 </v-list-item-subtitle>
          <v-list-item-title class="py-2">
            <v-icon class="mr-2">mdi-sofa</v-icon>
            {{ billboard.participantNeed }}
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-subtitle>投放时间</v-list-item-subtitle>
          <v-list-item-title class="py-2">
            <v-icon class="mr-2">mdi-calendar-range</v-icon>
            {{ timespan(billboard) }}
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-subtitle>主要面向人群</v-list-item-subtitle>
          <v-list-item-title class="py-2">
            <v-icon class="mr-2">mdi-face</v-icon>
            {{ billboard.target }}
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-list-item three-line>
        <v-list-item-content>
          <v-list-item-subtitle>投放规则</v-list-item-subtitle>
          <v-list-item-subtitle class="py-2 black--text">
            <v-icon class="mr-2">mdi-gamepad-variant</v-icon>
            {{ billboard.rule }}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <!-- <v-list-item>
        <v-list-item-content>
          <v-list-item-subtitle> 数字指纹 </v-list-item-subtitle>
          <v-list-item-title class="py-2">
            <v-icon class="mr-2">mdi-fingerprint</v-icon>
            7215ee9c7d9dc229d2921a40e899ec5f
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item> -->
    </v-card>
    <v-card elevation="0">
      <v-card-subtitle>参与方</v-card-subtitle>
      <v-list-item v-for="(ad, index) in adList" :key="index">
        <v-list-item-avatar>
          <v-img :src="ad.avatar" contain></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <!-- <v-list-item-subtitle>{{ad.brand}}</v-list-item-subtitle> -->
          <v-list-item-title class="title py-2">
            {{ ad.name }}
          </v-list-item-title>
        </v-list-item-content>
        <!-- <v-list-item-action>
          <p-btn :value="ad.progress" elevation="2">
            <v-icon>mdi-coins</v-icon>{{ad.ROE}}
          </p-btn>
        </v-list-item-action> -->
      </v-list-item>
    </v-card>
    <v-bottom-sheet v-model="sheet.show">
      <v-sheet>
        <v-card elevation="0">
          <v-card-actions class="px-4">
            <v-card-subtitle class="pa-0">广告位申请</v-card-subtitle>
            <v-spacer></v-spacer>
            <v-btn icon @click="sheet.show = false">
              <v-icon small>mdi-close</v-icon>
            </v-btn>
          </v-card-actions>
          <v-card-text>
            <v-text-field label="标题" v-model="form.title"
            prepend-icon="mdi-bookmark-outline"></v-text-field>
            <v-text-field label="封面url" v-model="form.coverPic"
            prepend-icon="mdi-link-variant"></v-text-field>
            <v-text-field label="积分募集基数" v-model="form.roe"
            prepend-icon="mdi-swap-horizontal"></v-text-field>
            <v-text-field :label="`目标交易数 (${$splited(form.txCount)})`" v-model="form.txCount"
            prepend-icon="mdi-swap-horizontal"></v-text-field>
            <v-text-field :label="`目标募集额度`" :value="$splited(form.txCount * form.roe)" disabled
            prepend-icon="mdi-coins"></v-text-field>
            <v-btn block color="primary" class="mt-3" rounded large
            @click="publish">申请</v-btn>
          </v-card-text>
        </v-card>
        <!-- <div class="py-3">This is a bottom sheet using the controlled by v-model instead of activator</div> -->
      </v-sheet>
    </v-bottom-sheet>
    <!-- </v-img> -->
  </div>
</template>
<script>
import PBtn from "@/components/vuetify/ProgressBtn.vue";
export default {
  name: "",
  components: {
    PBtn,
  },
  created() {
    this.$emit("title-declare", "广告位详情");
    this.getDetail()
  },
  data() {
    return {
      sheet: {
        show: false
      },
      form: {
        title: '步步高点读机',
        // adOwner: '',
        coverPic: 'https://p1.ssl.qhimg.com/t01794626d79c25971c.jpg',
        roe: '1000',
        // integralNeed: '2000000000',
        txCount: '1000000' // 交易数
        // integralGet: '',
        // tragetAd: ''
      },
      platform: {
        name: "",
        avatar: "",
      },
      billboard: {
        title: "首页推荐广告位",
        coverPic: "",
        progress: 0,
      },
      adList: [
        /*{
          brand: 'BBK',
          title: '步步高点读机',
          ROE: '100',
          intro: '步步高点读机，哪里不会点哪里。',
          logo: 'https://static.eebbk.com/static/pc/public/img/logo_f204ba7.png',
          progress: 90
        },
        {
          brand: '小霸王',
          title: '小霸王游戏机',
          logo: 'https://tse1-mm.cn.bing.net/th/id/R-C.153daaa87c017763615fff92e22b280f?rik=numCYuQwEZ2X8w&riu=http%3a%2f%2fimg.mp.itc.cn%2fupload%2f20170705%2fb7ecea38171c4505951e96e6047c7188_th.jpg&ehk=CS2qRbloXuA2ZmBJWM66t5f%2fD5yvePf%2fieV%2fk3hcx68%3d&risl=&pid=ImgRaw&r=0',
          cover: 'https://tse3-mm.cn.bing.net/th/id/OIP-C.FDeHmY4dN-Ng5FImi_tnpgHaEK?pid=ImgDet&rs=1',
          ROE: '80',
          progress: 20
        },*/
      ],
    };
  },
  methods: {
    timespan(item) {
      return `${new Date(
        parseInt(item.startTime)
      ).toLocaleDateString()} ~ ${new Date(
        parseInt(item.endTime)
      ).toLocaleDateString()}`;
    },
    publish() {
      let {address} = this.$store.getters.account
      this.$api.contract.adInvesment._insert('', {
        id: '1',
        ...this.form,
        adOwner: address,
        integralGet: '',
        integralNeed: (this.form.roe * this.form.txCount).toString(),
        targetAd: this.$route.params.id
      })
      .then(res => {
        // console.log(res)
        let {data} = res
        if(data.message == 'Success') {
          this.sheet.show = false
          this.$prompt({type: 'success', message: '发布成功'})
          this.getDetail()
        }
        else {
          this.$prompt({type: 'error', message: '发布失败'})
        }
      })
    },
    txadIssuer() {
      let {address} = this.$store.getters.account
      if(address==undefined) return
      this.$api.contract.adIssuer.insert(address, {
        name: 'BBK',
        avatar: 'https://static.eebbk.com/static/pc/public/img/logo_f204ba7.png'
      })
    },
    getDetail() {
      
      let id = this.$route.params.id;
      this.$api.contract.advertise.select("", { id }).then(async (res) => {
        let billboard = res.list[0];
        billboard.progress =
          (100 * billboard.integralGet) / billboard.integralNeed;
        this.billboard = billboard;
        this.$api.contract.platOwner
          .select("", {
            address: billboard.adOwner,
          })
          .then((res) => {
            this.platform = res.list[0];
          });

        let participants = [];
        if(billboard.participants.length>0)
        for (const address of billboard.participants.split(",")) {
          // this.$api.contract.adIssuer.select('', {address}).then((res) => {
          //   console.log('res', res)
          //   participants.push(res.list[0]);
          // });
          let res = await this.$api.contract.adIssuer.select('', {address})
            console.log('res', res)
            participants.push(res.list[0]);
          // });
        }
        // 过滤掉undefined
        this.adList = participants.filter(ad => ad != undefined);
      });
    }
  },
};
</script>

<style scoped>
.background-black-transparent {
  background: linear-gradient(
    to top,
    rgba(10, 10, 10, 0.5),
    transparent
  ) !important;
}
</style>