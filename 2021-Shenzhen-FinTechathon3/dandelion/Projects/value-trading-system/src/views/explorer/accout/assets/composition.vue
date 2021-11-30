<template>
  <div>
    <v-card class="ma-3 gradient"
      v-for="(item, index) in assetsList" :key="index">
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="title text--primary">
            {{ item.title }}
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-list-item>
        <v-list-item-avatar tile>
          <v-img :src="platformMapping[item.platform].avatar" contain></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-subtitle>托管平台</v-list-item-subtitle>
          <v-list-item-title class="title py-2 text--primary">
            {{ platformMapping[item.platform].name }}
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-card-actions>
        <v-btn small text color="primary" class="ma-0">
          <v-icon>mdi-play-box-outline</v-icon> {{ item.play }}
        </v-btn>
        <v-btn small text color="primary" class="ma-0">
          <v-icon>mdi-coins</v-icon> {{ $splited(item.coins) }}
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="toApprove(item)">转移</v-btn>
      </v-card-actions>
    </v-card>
    <v-bottom-sheet v-model="sheet.show">
      <v-sheet>
        <v-card elevation="0">
          <v-card-actions class="px-4">
            <v-card-subtitle class="pa-0">平台选择</v-card-subtitle>
            <v-spacer></v-spacer>
            <v-btn icon @click="sheet.show = false">
              <v-icon small>mdi-close</v-icon>
            </v-btn>
          </v-card-actions>
          <v-card-text class="pa-0">
            <v-list>
              <v-list-item-group v-model="platformChoosedIndex" color="primary">
                <v-list-item v-for="(platform, index) in platforms" :key="index"
                :disabled="platform.address == assetsToApprove.platform">
                  <v-list-item-content>
                    <v-list-item-title>
                      {{platform.name}} - {{platform.portion}}
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </v-list-item-group>
            </v-list>
            <v-btn block color="primary" class="mt-3" rounded large
            :disabled="assetsToApprove.platform==platformChoosed.address || platformChoosed.address==undefined"
            @click="approve">转移</v-btn>
          </v-card-text>
        </v-card>
      </v-sheet>
    </v-bottom-sheet>
  </div>
</template>

<script>
export default {
  name: "",
  async created() {
    this.$emit("title-declare", "数字资产");
    this.$api.contract.integral.getAllProportion()
    await this.getPlatforms();
    await this.getAssets();
  },
  methods: {
    getAssets() {
      let { address } = this.$store.getters.account;
      return this.$api.contract.digit.assetOfOwner(address).then((res) => {
        console.log(res);
        this.assetsList = res;
      });
    },
    getPlatforms() {
      return this.$api.contract.platOwner.select("", {}).then((res) => {
        this.platforms = res.list;
        for (const platform of res.list) {
          this.platformMapping[platform.address] = platform;
        }
        console.log(res);
      });
    },
    toApprove(item) {
      this.assetsToApprove = item;
      this.sheet.show = true;
    },
    approve() {
      this.$api.contract.digit.approve(
        this.platformChoosed.address,
        this.assetsToApprove.id
      ).then(() => {
        this.sheet.show = false
        this.getAssets()
      })
    }
  },
  computed: {
    platformChoosed() {
      return this.platforms[this.platformChoosedIndex] || {}
    }
  },
  data() {
    return {
      assetsToApprove: {},
      platformChoosedIndex: '',
      sheet: {
        show: false,
      },
      platforms: [],
      platformMapping: {},
      assetsList: [
        // {
        //   title: '量子波动速读法实践-半分钟精通区块链',
        //   platform: '腾讯视频',
        //   watched: '3M',
        //   thumb: '3k',
        //   coins: '30M',
        //   time: '2021-10-1',
        //   platformLogo: 'https://puui.qpic.cn/vupload/0/common_logo_square.png/0'
        // },
        // {
        //   title: '量子波动速读法从入门到精通',
        //   platform: '哔哩哔哩',
        //   watched: '3M',
        //   thumb: '3k',
        //   coins: '30M',
        //   time: '2021-10-1',
        //   platformLogo: 'https://cdn.freelogovectors.net/wp-content/uploads/2020/08/bilibili-logo.png'
        // }
      ],
    };
  },
};
</script>

<style scoped>
.gradient {
  background: linear-gradient(
    215deg,
    rgba(255, 50, 50, 0.05),
    rgba(0, 73, 243, 0.05)
  );
}
</style>