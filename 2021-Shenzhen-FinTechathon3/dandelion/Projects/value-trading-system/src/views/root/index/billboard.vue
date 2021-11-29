<template>
  <div>
    <!-- <v-btn @click="txBillboard">更新广告位</v-btn> -->
    <!-- <v-btn @click="txPlat">平台方</v-btn> -->
    <v-card dense class="ma-2" v-for="(item, index) in list" :key="index" elevation="0"
    @click="toDetail(item)">
      <v-img :src="item.coverPic"
      class="align-end">
      <v-list-item class="background-black-transparent white--text">
        <v-list-item-avatar color="white">
          <v-img :src="item.logo" contain></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title class="title white--text">
            {{item.title}}
          </v-list-item-title>
          <v-list-item-subtitle class="white--text">{{item.brand}}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      </v-img>
      <v-card-actions>
        <v-btn small text class="ma-0" color="primary">
          <v-icon left>mdi-sofa</v-icon> {{item.participantNeed}}
        </v-btn>
        <v-btn small text class="ma-0" color="primary">
          <v-icon left>mdi-account-group</v-icon> {{participantsCount(item.participants)}}
        </v-btn>
        <v-btn small text class="ma-0" color="primary">
          <v-icon left>mdi-coins</v-icon> {{$splited(item.integralNeed)}}
        </v-btn>
        <v-spacer></v-spacer>
        <p-btn :value="item.progress" elevation="2">申请</p-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
import PBtn from '@/components/vuetify/ProgressBtn.vue'
export default {
  name: "",
  components: {
    PBtn
  },
  created() {
    this.getBillboard()
  },
  data() {
    return {
      list: [
        /*{
          brand: '哔哩哔哩',
          title: '首页推荐广告位',
          logo: 'https://cdn.freelogovectors.net/wp-content/uploads/2020/08/bilibili-logo.png',
          cover: 'https://pic1.zhimg.com/v2-e594e0500140016d4d7141179803fd59_r.jpg'
        },
        {
          brand: '抖音',
          title: '推荐广告',
          logo: 'https://tse1-mm.cn.bing.net/th/id/R-C.1c14ee07f0a9e7276b26ce0494e13f12?rik=9TcoRv8W8BAaWQ&riu=http%3a%2f%2fwww.juzidsw.com%2fblog%2fzb_users%2fupload%2f2019%2f03%2f201903201553084163189053.png&ehk=s8mN7KRKqxpkA5%2bjlEnrlFzA1GMLznretI1PZOnyCNQ%3d&risl=&pid=ImgRaw&r=0',
          cover: 'https://tse1-mm.cn.bing.net/th/id/R-C.513719680693adb161bab774ef334824?rik=LuQ78EeOdI7ewg&riu=http%3a%2f%2fwww.ermacn.com%2fuploads%2fueditor%2f20201022%2f1-2010221K600J6.png&ehk=DNd8foJQV8%2fTn0zdGvCPFiDAAyeJKfa2dREAPbelGHk%3d&risl=&pid=ImgRaw&r=0'
        },
        {
          brand: '哔哩哔哩',
          title: '首页推荐广告位',
          logo: 'https://cdn.freelogovectors.net/wp-content/uploads/2020/08/bilibili-logo.png',
          cover: 'https://pic1.zhimg.com/v2-e594e0500140016d4d7141179803fd59_r.jpg'
        },*/
      ]
    };
  },
  methods: {
    getBillboard() {
      this.$api.contract.advertise.select('', '')
      .then(async res => {
        for (let billboard of res.list) {
          await this.$api.contract.platOwner.select('', {
            address: billboard.adOwner
          })
          .then(res => {
            billboard.logo = res.list[0].avatar
          })
          billboard.progress = 100 * billboard.integralGet/billboard.integralNeed
        }
        this.list = res.list
      })
    },
    participantsCount(str) {
      if(str.length == 0) return 0
      return str.split(',').length
    },
    txBillboard() {
      // this.$api.contract.advertise.insert(
      //   '',
      //   {
      //     title: "片头广告位",
      //     adOwner: '0x9865ac4d5608e12d24d4ca6c9e68c171dbd011c9',
      //     coverPic: 'https://tse2-mm.cn.bing.net/th/id/OIP-C.ovwyEzVvR8W2rQExFEf65AAAAA?pid=ImgDet&rs=1',
      //     integralNeed: '4000000000',
      //     integralGet: '3000000000',
      //     participantNeed: '3',
      //     participants: '0xe565efe39618a68e1de15936e61706f83c222fb1',
      //     startTime: `${1633856709780+1000*60*60*24*10}`,
      //     endTime: `${1633856709780+1000*60*60*24*70}`
      //   },
      // )
      this.$api.contract.advertise.remove(
        '',
        {
          title: "片头广告位测试",
          // participants: '0x6732ad3a68b25ecd874b4e6d7d57d31572073a1e'
        },
        // {
        //   // title: '片头广告位'
        //   id: "3"
        // }
      )
    },
    txPlat() {
      this.$api.contract.platOwner.update('', {
        avatar: 'https://tse3-mm.cn.bing.net/th/id/OIP-C.t_km_I0O-asr3a-bNrejjQHaHa?pid=ImgDet&rs=1'
      },{
        address: "0xcce3632fe7e76ac09f5d71bf1ccd9886fbdd8cd7"
      })
    },
    toDetail(item) {
      this.$router.push(`/explorer/detail-billboard/${item.id}`)
    }
  }
};
</script>

<style scoped>
.background-black-transparent {
  background: linear-gradient(to top, rgba(10, 10, 10, 0.5), transparent) !important;
}
</style>