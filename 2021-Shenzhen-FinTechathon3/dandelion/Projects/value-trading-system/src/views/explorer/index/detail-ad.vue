<template>
  <div class="grey lighten-4" style="height: 100%;">
    <v-img :src="ad.coverPic" class="align-end">
      <v-list-item class="background-black-transparent white--text">
        <v-list-item-avatar color="white">
          <v-img :src="ad.avatar" contain></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title class="title">
            {{ ad.title }}
          </v-list-item-title>
          <v-list-item-subtitle class="white--text">
            {{ ad.brand }}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-img>
    <v-card class="mb-1" elevation="0" tile>
      <v-card-subtitle> 详细信息 </v-card-subtitle>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-subtitle> 剩余额度 </v-list-item-subtitle>
          <v-list-item-title class="title py-2">
            <v-icon>mdi-coins</v-icon> {{$splited(ad.integralNeed-ad.integralGet)}}
          </v-list-item-title>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn text class="pa-2" color="primary">
            <v-progress-linear :value="100*ad.integralGet/ad.integralNeed" color="primary"> </v-progress-linear>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
      <v-img
        :src="billboard.coverPic"
        height="169"
        gradient="to top, rgba(255, 255, 255, 0.5), rgba(255, 255, 255, 1)"
      >
        <v-list-item>
          <v-list-item-content>
            <v-list-item-subtitle> 募集基数 </v-list-item-subtitle>
            <v-list-item-title class="title py-2">
              <v-icon>mdi-coins</v-icon> {{$splited(ad.roe)}}
              <!-- = <v-icon>mdi-currency-cny</v-icon>1 -->
            </v-list-item-title>
          </v-list-item-content>
          <v-list-item-action>
            <v-btn color="primary" @click="sheet.show=true"> 交易 </v-btn>
          </v-list-item-action>
        </v-list-item>
        <v-list-item>
          <v-list-item-content>
            <v-list-item-subtitle> 目标广告位 </v-list-item-subtitle>
            <v-list-item-title class="title py-2">
              {{billboard.title}}
            </v-list-item-title>
          </v-list-item-content>
          <v-list-item-action>
            <v-btn color="white" @click="toDetailBillboard">
              <v-img :src="billboard.avatar" width="30" contain></v-img>
            </v-btn>
          </v-list-item-action>
        </v-list-item>
      </v-img>
    </v-card>
    <v-card class="mb-1" tile elevation="0">
      <v-card-subtitle> 交易记录 </v-card-subtitle>
      <v-list-item v-for="(tx, index) in txList" :key="index">
        <v-list-item-content>
          <v-list-item-subtitle>
            {{tx.from}}
          </v-list-item-subtitle>
          <v-list-item-title class="py-2">
            <v-icon>mdi-coins</v-icon> {{$splited(tx.amount)}}
          </v-list-item-title>
        </v-list-item-content>
        <v-list-item-action class="text-body-2">
          {{tx.time}}
        </v-list-item-action>
      </v-list-item>
    </v-card>
    <v-bottom-sheet v-model="sheet.show">
      <v-sheet>
        <v-card elevation="0">
          <v-card-actions class="px-4">
            <v-card-subtitle class="pa-0">积分交易</v-card-subtitle>
            <v-spacer></v-spacer>
            <v-btn icon @click="sheet.show=!sheet.show">
              <v-icon small>mdi-close</v-icon>
            </v-btn>
          </v-card-actions>
          <v-card-text>
            <v-row align="end">
              <v-col>
                <v-text-field :label="`交易笔数（${ad.roe}积分/笔）`" v-model="sheet.amount"></v-text-field>
              </v-col>
              <v-col cols="5">
                <v-slider class="mx-0" v-model="sheet.amount" thumb-label dense
                :max="txMax"></v-slider>
              </v-col>
            </v-row>
            <v-text-field label="交易量" append-icon="mdi-coins" disabled
            :value="$splited(sheet.amount*ad.roe)"></v-text-field>
            <v-btn block color="primary" class="mt-3" rounded large
            @click="transact">确认</v-btn>
            
          </v-card-text>
        </v-card>
        <!-- <div class="py-3">This is a bottom sheet using the controlled by v-model instead of activator</div> -->
      </v-sheet>
    </v-bottom-sheet>
  </div>
</template>

<script>
export default {
  name: "detail-ad",
  created() {
    this.getDetail()
    this.$balance()
    .then(res => {
      this.points = res
    })
  },
  data() {
    return {
      ad: {
        brand: "",
        title: "",
        logo: "",
        roe: 0,
      },
      billboard: {
        id: '',
        avatar: "",
        cover: "",
        title: ''
      },
      txList: [],
      sheet: {
        show: false,
        amount: 0 //交易笔数
      },
      points: 0 //用户积分数

    };
  },
  computed: {
    txPointsMax() {
      return this.ad.integralNeed-this.ad.integralGet
    },
    txMax() {
      return parseInt(Math.min(this.txPointsMax,this.points) / this.ad.roe)
    }
  },
  methods: {
    toDetailBillboard() {
      this.$router.push(`/explorer/detail-billboard/${this.billboard.id}`)
    },
    
    getDetail() {
      this.$emit('title-declare', '募集详情')
      let id = this.$route.params.id
      // 获取详情
      this.$api.contract.adInvesment.select('', {id})
      .then(async res => {
        let ad = res.list[0]
        // 获取广告商信息
        await this.$api.contract.adIssuer.select('',{address: ad.adOwner})
        .then(res => {
          ad.brand = res.list[0].name
          ad.avatar = res.list[0].avatar
        })
        this.ad = ad
        // 获取交易历史
        this.$api.contract.tradeHistory.select(id, {})
        .then(res => {
          console.log('trade', res)
          this.txList = res.list.map(tx => ({
            from: tx.fromAddress,
            amount: tx.value,
            time: new Date(parseInt(tx.timestamp)).toLocaleString()
          }))
        })
        this.$api.contract.advertise.select('', {id: ad.targetAd})
        .then(res => {
          let {title, coverPic, adOwner} = res.list[0]
          this.$api.contract.platOwner.select('', {address: adOwner})
          .then(res => {
            let {avatar} = res.list[0]
            this.billboard = {id: ad.targetAd, title, coverPic, avatar}
          })
        })
      })
    },
    // 发送交易
    transact() {
      let id = this.$route.params.id
      let {adOwner, roe} = this.ad
      this.$api.contract.integral.transfer(adOwner, this.sheet.amount*roe, id)
      .then(res => {
        console.log(res)
        let {data} = res
        if(data.message == 'Success') {
          this.sheet.show = false
          this.$prompt({type: 'success', message: '交易成功'})
          this.getDetail()
        }
        else {
          this.$prompt({type: 'error', message: '交易失败'})
        }
      })
    }
  }
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