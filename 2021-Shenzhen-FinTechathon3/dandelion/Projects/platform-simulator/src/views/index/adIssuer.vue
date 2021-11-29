<template>
  <div class="ma-3">
    <v-simple-table>
      <thead>
        <tr class="text-left">
          <th>广告商名称</th>
          <th>地址</th>
          <th>logo</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="adIssuer in adIssuers" :key="adIssuer.address">
          <td>{{adIssuer.name}}</td>
          <td>{{adIssuer.address}}</td>
          <td>
            <img :src="adIssuer.avatar" width="30">
          </td>
          <td>
            <v-btn class="primary" @click="toEdit(adIssuer)">编辑</v-btn>
            <v-btn class="error ml-1" @click="toRemove(adIssuer)">删除</v-btn>
          </td>
        </tr>
      </tbody>
    </v-simple-table>
    <v-btn class="primary" @click="toInsert">申请成为广告商</v-btn>
    <v-dialog v-model="dialog" max-width="600">
      <v-card>
        <v-card-title class="headline">广告商信息</v-card-title>
        <v-card-text>
          <v-text-field label="广告商名称" v-model="form.name"></v-text-field>
          <v-row>
            <v-col cols="10">
              <v-text-field label="广告商logo" v-model="form.avatar"></v-text-field>
            </v-col>
            <v-col cols="2" class="pa-0">
              <v-img :src="form.avatar" width="60"></v-img>
            </v-col>
          </v-row>
          
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text
          @click="dialog = false">
            取消
          </v-btn>
          <v-btn color="primary"
          @click="submit">
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
  data () {
    return {
      dialog: false,
      adIssuers: [],
      form: {
        mode: 'insert',
        name: '',
        avatar: '',
      }
    }
  },
  created() {
    this.getAdIssuers()
  },
  methods: {
    getAdIssuers() {
      this.$api.contract.adIssuer.select('', {})
      .then(res => {
        this.adIssuers = res.list
      })
    },
    toRemove({address}) {
      this.$api.contract.adIssuer.remove('', {address})
      .then(() => {
        this.getAdIssuers()
      })
    },
    toEdit({name, avatar}) {
      this.form.name = name
      this.form.avatar = avatar
      this.form.mode = 'update'
      this.dialog = true
    },
    toInsert() {
      this.form.mode = 'insert'
      this.dialog = true
    },
    submit() {
      if(this.form.mode == 'insert') {
        this.submitInsert()
      }
      else {
        this.submitUpdate()
      }
    },
    async submitInsert() {
      let {address} = await this.$account()
      let {name, avatar} = this.form
      // console.log({name, avatar, address})
      this.$api.contract.adIssuer.insert('', {
        name, avatar, address
      })
      .then(() => {
        this.dialog = false
        this.getAdIssuers()
      })
    },
    async submitUpdate() {
      console.log('this.$account', this.$account)
      let {address} = await this.$account()
      let {name, avatar} = this.form
      this.$api.contract.adIssuer.update('',
        {name, avatar},
        {address}
      )
      .then(() => {
        this.dialog = false
        this.getAdIssuers()
      })
    }
  }
}
</script>

<style scoped>
</style>