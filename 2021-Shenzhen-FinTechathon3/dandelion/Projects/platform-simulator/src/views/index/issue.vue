<template>
  <div class="ma-3">
    <!-- <v-btn @click="getPlatforms">click</v-btn> -->
    <!-- {{form}} -->
    <v-select :items="platforms" item-text="name" item-value="address" label="发布平台"
    v-model="platform"></v-select>
		<v-text-field label="标题" v-model="title"></v-text-field>
    <!-- {{videos}} -->
		<v-btn @click="coin" color="primary">发布</v-btn>
  </div>
</template>

<script>
export default {
  created() {
    this.getPlatforms()
  },
  data() {
    return {
      platforms: [],
      platform: '',
			title: '',
    }
  },
  methods: {
    getPlatforms() {
      this.$api.contract.platOwner.select('', {})
      .then(res => {
        this.platforms = res.list
        console.log(res)
      })
    },
    coin() {
      this.$api.contract.digit.issueWithAssetURI(this.platform, this.title)
      .then(res=> {
				console.log(res)
      })
    }
  }
};
</script>