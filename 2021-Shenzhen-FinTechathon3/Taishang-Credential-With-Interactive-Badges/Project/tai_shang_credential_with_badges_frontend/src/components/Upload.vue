<template>
  <a-upload
    action="http://47.98.100.48:5000/weid/verify/credential"
    :default-file-list="defaultFileList"
    @change="handleUploadSuccess"
  >
    <a-button><a-icon type="upload" /> Upload </a-button>
  </a-upload>
</template>
<script>
export default {
  data() {
    return {
      defaultFileList: [
      ],
      certificateInfo:{}
    };
  },
  methods: {
    handleChange({ file, fileList }) {
      if (file.status !== 'uploading') {
        console.log(file, fileList);
      }
    },
    handleUploadSuccess(info){
      if(info.file.status=='done'){
        console.log(info.fileList[0].response);
        let res = info.fileList[0].response;
        this.certificateInfo.ownerName = res.claim.name;
        this.certificateInfo.date = res.claim.date;
        this.certificateInfo.weid = res.claim.weid;
        this.certificateInfo.lesson_name = res.claim.lesson_name;
        this.certificateInfo.expirationDate = "2035-06-05";
        this.certificateInfo.issuer = res.issuer;
        this.certificateInfo.nftAddr = "0xa7844eb44fd581ff9b9ffc678037fd506f111fb7";
        this.$emit('render-click',this.certificateInfo);
      }
    },
    transformTime(fmt, date) {
      let ret = '';
      date = new Date(date);
      const opt = {
        'Y+': date.getFullYear().toString(), // 年
        'm+': (date.getMonth() + 1).toString(), // 月
        'd+': date.getDate().toString(), // 日
        'H+': date.getHours().toString(), // 时
        'M+': date.getMinutes().toString(), // 分
        'S+': date.getSeconds().toString(), // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
      };
      for (const k in opt) {
        ret = new RegExp(`(${k})`).exec(fmt);
        if (ret) {
          fmt = fmt.replace(
            ret[1],
            ret[1].length == 1 ? opt[k] : opt[k].padStart(ret[1].length, '0'),
          );
        }
      }
      return fmt;
    },
  },
};
</script>
