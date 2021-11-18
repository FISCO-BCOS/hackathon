<template>
  <div class="ma-3">
    <v-simple-table>
      <thead>
        <tr class="text-left">
          <th>平台名称</th>
          <th>地址</th>
          <th>logo</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="platform in platforms" :key="platform.address">
          <td>{{platform.name}}</td>
          <td>{{platform.address}}</td>
          <td>
            <img :src="platform.avatar" width="30">
          </td>
          <td>
            <v-btn class="primary" @click="toEdit(platform)">编辑</v-btn>
            <v-btn class="error ml-1" @click="toRemove(platform)">删除</v-btn>
          </td>
        </tr>
      </tbody>
    </v-simple-table>
    <v-btn class="primary" @click="toInsert">申请成为平台方</v-btn>
    <v-dialog v-model="dialog" max-width="600">
      <v-card>
        <v-card-title class="headline">平台方信息</v-card-title>
        <v-card-text>
          <v-text-field label="平台名称" v-model="form.name"></v-text-field>
          <v-row>
            <v-col cols="10">
              <v-text-field label="平台logo" v-model="form.avatar"></v-text-field>
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
      platforms: [],
      form: {
        mode: 'insert',
        name: '',
        avatar: '',
      }
    }
  },
  created() {
    this.getPlatforms()
  },
  methods: {
    getPlatforms() {
      this.$api.contract.platOwner.select('', {})
      .then(res => {
        this.platforms = res.list
      })
    },
    toRemove({address}) {
      this.$api.contract.platOwner.remove('', {address})
      .then(() => {
        this.getPlatforms()
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
      this.$api.contract.platOwner.insert('', {
        name, avatar, address
      })
      .then(() => {
        this.dialog = false
        this.getPlatforms()
      })
    },
    async submitUpdate() {
      console.log('this.$account', this.$account)
      let {address} = await this.$account()
      let {name, avatar} = this.form
      this.$api.contract.platOwner.update('',
        {name, avatar},
        {address}
      )
      .then(() => {
        this.dialog = false
        this.getPlatforms()
      })
    }
  }
}
</script>

<style scoped>
</style>