<template>
  <div class="ma-3">
    <!-- <v-btn @click="getPlatforms">click</v-btn> -->
    <!-- {{form}} -->
    <v-select :items="platforms" item-text="name" item-value="address" label="使用视频平台"
    v-model="platform"></v-select>
    <!-- {{videos}} -->
    <v-simple-table>
    <template v-slot:default>
      <thead>
        <tr>
          <th class="text-left">id</th>
          <th class="text-left">标题</th>
          <th class="text-left">作者地址</th>
          <th class="text-left">平台方</th>
          <th class="text-left">播放量</th>
          <th class="text-left">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in videos" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.title }}</td>
          <td>{{ item.ownerAddress }}</td>
          <td>{{ item.platform }}</td>
          <td>{{ item.play }}</td>
          <td style="width: 300px">
            <v-btn color="primary" @click="play(item)" :disabled="platform==''">播放1000次</v-btn>
            <v-btn class="ml-1" color="error" @click="destroy(item)">删除</v-btn>
          </td>
        </tr>
      </tbody>
    </template>
  </v-simple-table>
  </div>
</template>

<script>
export default {
  created() {
    this.getAssets()
    this.getPlatforms()
  },
  data() {
    return {
      platforms: [],
      videos: [],
      platform: ''
    }
  },
  methods: {
    getPlatforms() {
      this.$api.contract.platOwner.select('', {})
      .then(res => {
        this.platforms = res.list
      })
    },
    getAssets() {
      this.$api.contract.digit.allAsset('', {})
      .then(res => {
        this.videos = res.reverse()
      })
    },
    play(item) {
      this.$api.contract.digit.assetPlay(item.id, this.platform, 1000)
      .then(()=> {
        this.getAssets()
      })
    },
    destroy(item) {
      this.$api.contract.digit.destroy(item.id)
      .then(()=> {
        this.getAssets()
      })
    }
  }
};
</script>