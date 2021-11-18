<template>
  <div class="ma-3">
    <v-simple-table>
      <template v-slot:default>
        <thead>
          <tr>
            <th>平台方</th>
            <th>积分分成比例(n/10)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="platform in platforms" :key="platform.address">
            <td>{{platform.name}}</td>
            <td>{{portionMapping[platform.address]}}</td>
          </tr>
        </tbody>
      </template>
    </v-simple-table>
    <v-btn class="primary" @click="dialog=true">更改本平台分成比例</v-btn>
    <v-dialog v-model="dialog" max-width="290">
      <v-card>
        <v-card-title class="headline">分成比例调整</v-card-title>
        <v-card-text>
          <v-text-field v-model="form.portion"></v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text
          @click="dialog = false">
            取消
          </v-btn>
          <v-btn color="primary"
          @click="proportionChange">
            确定
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  name: '',
  created() {
    this.getAllProportion()
    this.getPlatforms()
  },
  data () {
    return {
      platforms: [],
      portionMapping: {},

      dialog: false,
      form: {
        portion: 5
      }
    }
  },
  methods: {
    getPlatforms() {
      return this.$api.contract.platOwner.select("", {}).then((res) => {
        this.platforms = res.list;
      });
    },
    getAllProportion() {
      return this.$api.contract.integral.getAllProportion()
      .then(res => {
        for (const item of res) {
          this.$set(this.portionMapping, item.address, item.portion)
        }
        console.log('pm', this.portionMapping)
      })
    },
    proportionChange() {
      this.$api.contract.integral.proportionChange(this.form.portion)
      .then(() => {
        this.dialog = false
        this.getAllProportion()
      })
    }
  }
}
</script>

<style scoped>
</style>