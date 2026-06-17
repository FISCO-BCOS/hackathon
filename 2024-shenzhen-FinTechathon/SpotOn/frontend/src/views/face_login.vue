<template>
  <div v-loading="isLoading" element-loading-text="Loading Models...">
    <login @faceInfo="handleFaceInfo" />
  </div>
</template>

<script>
import login from '../components/login.vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { zkp_proof } from '../utils/ZKP.mjs'

export default {
    name: 'face_login',
    components: {
        login
    },
    data() {    
        return {
            faceInfo: null,
            isLoading: false,
        }
    },
    methods: {
        handleFaceInfo(faceInfo) {
            this.faceInfo = faceInfo;
            //console.log('解析后的 faceInfo:', this.faceInfo);

            const faceInfoArray = Array.isArray(this.faceInfo) ? this.faceInfo : Object.values(this.faceInfo);
            const stringifiedFaceInfo = faceInfoArray.map(value => value.toString());

            //console.log('字符串化的 faceInfo:', stringifiedFaceInfo);

            axios.post('http://127.0.0.1:8088/login', {
                newfaceinfo: stringifiedFaceInfo
            }).then(response => {
                if (response.data.success) {
                    let emb_dist = response.data.emb_dist;
                    let rand_dist = response.data.rand_dist;

                    if (typeof emb_dist === 'number') {
                        emb_dist = emb_dist.toString();
                    }
                    if (typeof rand_dist === 'number') {
                        rand_dist = rand_dist.toString();
                    }

                    //console.log("emb_dist:", emb_dist);
                    //console.log("rand_dist:", rand_dist);
                    
                    const self = this;
                    async function zkp_proof_test() {
                        self.isLoading = true;
                        try {
                            const [cmt_zk] = await zkp_proof(emb_dist, rand_dist);
                            const response = await axios.post('http://127.0.0.1:8088/login_zk', {
                                cmt_zk: cmt_zk
                            });
                            if (response.data.success) {
                                ElMessage.success('登录成功');
                                self.$router.push({ name: 'app_choose' });
                            } else {
                                ElMessage.error('服务端验证失败');
                                self.$router.push({ name: 'Login' });
                            }
                        } catch (error) {
                            console.error('请求失败:', error);
                            ElMessage.error('登录失败，请重试');
                        } finally {
                            self.isLoading = false;
                        }
                    }
                    zkp_proof_test();
                } else {
                    ElMessage.error(response.data.message);
                    if (response.data.message.message === "请先注册") {
                        this.$router.push({ name: 'face_register' });
                    } else if (response.data.message.message === "用户生物特征错误") {
                        this.$router.push({ name: 'Login' });
                    }
                }
            }).catch(error => {
                console.error('请求失败:', error);
                ElMessage.error('登录失败，请重试');
            });
        },
    },
    beforeRouteLeave(to, from, next) {
        if (to.name == 'Login' || to.name == 'app_choose' || to.name == 'face_register') {
            next();
        } else {
            next({ name: 'Login' });
        }
    }
}
</script>

<style scoped>
.loading-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.8);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}
</style>
