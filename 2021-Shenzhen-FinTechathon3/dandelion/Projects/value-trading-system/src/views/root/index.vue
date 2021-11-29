<template>
  <v-card class="pa-0 container grey lighten-4">
    <v-app-bar dark app color="primary">
      <v-btn icon @click="$router.push('/account')">
        <v-icon>mdi-account</v-icon>
      </v-btn>
      <!-- <v-toolbar-title>联盟链价值交易平台</v-toolbar-title> -->

      <v-spacer></v-spacer>
      <v-btn icon>
        <v-icon>mdi-magnify</v-icon>
      </v-btn>

      <!-- <v-btn icon>
        <v-icon>mdi-dots-vertical</v-icon>
      </v-btn> -->
      <template v-slot:extension>
        <v-tabs grow v-model="tabIndex">
          <v-tab>积分募集</v-tab>
          <v-tab>广告位</v-tab>
        </v-tabs>
      </template>
    </v-app-bar>
    <v-main app>
      <v-tabs-items v-model="tabIndex" class="transparent">
        <v-tab-item>
          <panel-ads></panel-ads>
        </v-tab-item>
        <v-tab-item>
          <panel-billboard ref="billboard"></panel-billboard>
        </v-tab-item>
      </v-tabs-items>
      <v-card-text
        style="height: 0px; position: fixed; bottom: 100px"
        class="pa-0"
      >
        <v-fab-transition>
          <v-btn color="white" v-show="activeFab != ''" absolute fab top right
          @click="sheet.show=!sheet.show">
            <v-icon color="primary">{{ activeFab }}</v-icon>
          </v-btn>
        </v-fab-transition>
      </v-card-text>
      <v-bottom-sheet v-model="sheet.show">
        <v-sheet>
          <v-card elevation="0">
            <v-card-actions class="px-4">
              <v-card-subtitle class="pa-0">发布广告位</v-card-subtitle>
              <v-spacer></v-spacer>
              <v-btn icon @click="sheet.show=!sheet.show">
                <v-icon small>mdi-close</v-icon>
              </v-btn>
            </v-card-actions>
            <v-card-text>
              <v-text-field label="标题" v-model="form.title"
              prepend-icon="mdi-bookmark-outline"></v-text-field>
              <v-text-field label="封面url" v-model="form.coverPic"
              prepend-icon="mdi-link-variant"></v-text-field>
              <v-text-field label="主要面向人群" v-model="form.target"
              prepend-icon="mdi-face"></v-text-field>
              <v-row>
                <v-col class="pb-0">
                  <v-text-field label="积分总额度" v-model="form.integralNeed"
                  prepend-icon="mdi-coins"></v-text-field>
                </v-col>
                <v-col class="pb-0">
                  <v-text-field label="计划参与方数" v-model="form.participantNeed"
                  prepend-icon="mdi-sofa"></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col class="py-0">
                  <date-input label="投放开始日期" v-model="form.startTime"
                  prepend-icon="mdi-calendar-today"></date-input>
                </v-col>
                <v-col class="py-0">
                  <date-input label="投放结束日期" v-model="form.endTime"
                  prepend-icon="mdi-calendar"></date-input>
                </v-col>
              </v-row>
              
              <v-textarea label="投放规则" v-model="form.rule" maxlength="60" counter
              prepend-icon="mdi-gamepad-variant" rows="3"></v-textarea>
              <v-btn block color="primary" class="mt-3" rounded large
              @click="publish">发布</v-btn>
              
            </v-card-text>
          </v-card>
          <!-- <div class="py-3">This is a bottom sheet using the controlled by v-model instead of activator</div> -->
        </v-sheet>
      </v-bottom-sheet>
    </v-main>
    <!-- <svg
      t="1633431410233"
      class="icon"
      viewBox="0 0 1024 1024"
      version="1.1"
      xmlns="http://www.w3.org/2000/svg"
      p-id="1931"
      width="200"
      height="200"
    >
      <path
        d="M648.533333 740.266667c6.4 0 10.666667-2.133333 17.066667-4.266667C806.4 676.266667 896 537.6 896 384c0-211.2-172.8-384-384-384S128 172.8 128 384c0 145.066667 81.066667 275.2 209.066667 341.333333 21.333333 10.666667 46.933333 2.133333 57.6-19.2s2.133333-46.933333-19.2-57.6C275.2 599.466667 213.333333 497.066667 213.333333 384c0-164.266667 134.4-298.666667 298.666667-298.666667s298.666667 134.4 298.666667 298.666667c0 119.466667-70.4 226.133333-179.2 273.066667-21.333333 8.533333-32 34.133333-21.333334 55.466666 6.4 17.066667 21.333333 27.733333 38.4 27.733334"
        fill="green"
        p-id="1932"
      ></path>
      <path
        d="M332.8 951.466667C420.266667 864 469.333333 746.666667 469.333333 620.8v-72.533333c-72.533333-19.2-128-85.333333-128-164.266667 0-93.866667 76.8-170.666667 170.666667-170.666667s170.666667 76.8 170.666667 170.666667c0 78.933333-55.466667 145.066667-128 164.266667v72.533333c0 147.2-57.6 285.866667-162.133334 390.4-8.533333 8.533333-19.2 12.8-29.866666 12.8s-21.333333-4.266667-29.866667-12.8c-17.066667-17.066667-17.066667-42.666667 0-59.733333zM426.666667 384c0 46.933333 38.4 85.333333 85.333333 85.333333s85.333333-38.4 85.333333-85.333333-38.4-85.333333-85.333333-85.333333-85.333333 38.4-85.333333 85.333333z"
        fill="green"
        p-id="1933"
      ></path>
    </svg> -->
  </v-card>
</template>

<script>
import PanelAds from "./index/ads.vue";
import PanelBillboard from "./index/billboard.vue";
import DateInput from '@/components/vuetify/DateInput.vue'
export default {
  name: "",
  components: {
    PanelAds,
    PanelBillboard,
    DateInput
  },
  created() {
    let {address} = this.me
  },
  data() {
    return {
      tabIndex: 0,
      sheet: {
        show: false
      },
      form: {
        title: '片头广告位',
        coverPic: 'https://tse1-mm.cn.bing.net/th/id/R-C.a2fc3213356f47c5b6ad01311447fae4?rik=F%2fi6EicoHf9dXQ&riu=http%3a%2f%2fliulanqi.2018.cn%2fuploads%2f181130%2f1-1Q1300Z416113.jpg&ehk=BRzYIGUORKBAHz%2fAcZ2ZqIxOYcRXmoTUGeDzm8xevbo%3d&risl=&pid=ImgRaw&r=0',
        integralNeed: '5000000000',
        participantNeed: '3',
        target: '小学生',
        startTime: '',
        endTime: '',
        adOwner: '',
        integralGet: '0',
        participants: '',
        rule: '各广告方募集积分比例即投放概率，进入投放期间时，将按对应概率进行投放'
      }
    };
  },
  computed: {
    activeFab() {
      let btns = ["", "mdi-send"];
      return btns[this.tabIndex];
    },
    me() {
      return this.$store.getters.account
    }
  },
  methods: {
    publish() {
      let {address} = this.me
      this.form.adOwner = address
      let startTime = new Date(this.form.startTime).getTime()
      let endTime = new Date(this.form.endTime).getTime()
      this.$api.contract.advertise.insert('', 
      {
        ...this.form,
        startTime,
        endTime
      })
      .then(({data}) => {
        if(data.message == 'Success') {
          this.sheet.show = false
          this.$prompt({type: 'success', message: '发布成功'})
          this.$refs.billboard.getBillboard()
        }
        else {
          this.$prompt({type: 'error', message: '发布失败'})
        }
      })
    }
  }
};
</script>

<style scoped>
.container {
  height: 100%;
  /* border: 1px solid red; */
}
</style>