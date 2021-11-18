<template>
  <div>
    <v-card class="ma-2" v-for="(item, index) in list" :key="index" elevation="0"
    @click="toDetail(item)">
      <v-list-item>
        <v-list-item-avatar tile>
          <v-img :src="item.logo" contain></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title class="title">
            {{item.title}}
          </v-list-item-title>
          <v-list-item-subtitle>{{item.brand}}</v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn icon>
            <v-icon>mdi-heart</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>

      <v-card-actions>
        <v-card-text class="grey--text text--darken-1">
          {{issueTime(item.timestamp)}}
        </v-card-text>
        <v-spacer></v-spacer>
        <p-btn :value="item.progress" style="width: 60px;" elevation="2">
          <v-icon left>mdi-coins</v-icon>{{$unit(item.roe)}}
        </p-btn>
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
  methods: {
    issueTime(timestamp) {
      return new Date(parseInt(timestamp)).toLocaleString()
    },
    toDetail(item) {
      this.$router.push(`/explorer/detail-ad/${item.id}`)
    },
    txAd() {
      this.$api.contract.adInvesment.remove('',{
        id: '6'
      })
    },
    txAdIssuer() {
      this.$api.contract.adIssuer.select('', {})
    }
  },
  created() {
    this.$api.contract.adInvesment.select('', {})
    .then(async res => {
      for (let ad of res.list) {
        ad.progress = 100 * ad.integralGet/ad.integralNeed
        await this.$api.contract.adIssuer.select('',{address: ad.adOwner})
        .then(res => {
          if(res.list.length == 0) return
          ad.brand = res.list[0].name
          ad.logo = res.list[0].avatar
        })
      }
      this.list = res.list
      // console.log(JSON.stringify(this.list))
    })
  },
  data() {
    return {
      list: [
      /*  {
          brand: 'BBK',
          title: '步步高点读机',
          roe: '100',
          progress: 30,
          intro: '步步高点读机，哪里不会点哪里。',
          logo: 'https://static.eebbk.com/static/pc/public/img/logo_f204ba7.png'
        }*/
      ]
    };
  },
};
</script>

<style scoped>
</style>