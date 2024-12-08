<template>
    <login @faceInfo="handleFaceInfo" />
</template>

<script>
import login from '../components/login.vue'
export default {
    name: 'face_login',
    components: {
        login
    },
    data() {
        return {
            showLogin: false, // 控制 login.vue 的显示
            faceInfo: null,
        }
    },
    methods: {
        handleFaceInfo(faceInfo) {
            this.faceInfo = faceInfo;
            console.log(this.faceInfo);
        },
    }
}
</script>
