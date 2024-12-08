<template>
    <div class="page-wrapper">
      <div class="header">
        <Header />
      </div>
      <div class="main-content">
        <div class="search-section">
          <div class="search-container">
            <h3 class="search-title">区块查询</h3>
            <div class="input-group">
              <input
                  type="text"
                  v-model="collectionId"
                  class="form-control custom-input"
                  placeholder="输入ID"
              >
              <div class="input-group-append">
                <button class="btn btn-search" @click="searchBlock">搜索</button>
              </div>
            </div>
            <div v-if="isLoading" class="mt-3 status-message success">加载中...</div>
            <div v-else-if="error" class="mt-3 status-message error">{{ error }}</div>
            <div v-else-if="blockInfo">
              <h4>区块信息</h4>
              <div class="info-content">
                <p><strong>Collection ID:</strong> {{ blockInfo.collection_id }}</p >
                <p><strong>Collection Name:</strong> {{ blockInfo.collection_name }}</p >
                <!-- <p><strong>Collection Matrix:</strong> {{ blockInfo.collection_matrix }}</p >
                <p><strong>Collection Make:</strong> {{ blockInfo.collection_make }}</p >
                <p><strong>Collection Record:</strong> {{ blockInfo.collection_record }}</p > -->
                <p><strong>Owner ID:</strong> {{ blockInfo.owner_id }}</p >
                <p><strong>Certificate Time:</strong> {{ blockInfo.certificate_time }}</p >
                <p><strong>Certificate Organization:</strong> {{ blockInfo.certificate_organization }}</p >
                <p><strong>Collection Semantic:</strong> {{ blockInfo.collection_semantic }}</p >
              </div>
              <button class="btn btn-delete" @click="deleteBlock">删除区块</button>
              <div v-if="deleteSuccess" class="mt-3 status-message success">
                <p><strong>删除成功</strong></p >
            </div>
            <button class="btn btn-decode" @click="decodeBlock">解码区块</button>
            <div v-if="decodeSuccess" class="mt-3 status-message success">
                <p><strong>解码成功:</strong> {{ decodeResult }}</p >
            </div>
          </div>
        </div>
        <div class="background-section">
          <Background :cols="5" />
        </div>
      </div>
      <div class="navigation-section">
        <button class="btn-navigate" @click="navigateToTxt2img">
          <span class="btn-text">返回主页</span>
          <span class="btn-icon">←</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import { useRouter } from 'vue-router';
import Background from '@/view/background/index.vue'
import Header from '@/view/header/index.vue'

interface BlockInfo {
    collection_id: string;
    collection_name: string;
    collection_matrix: string;
    collection_make: string;
    collection_record: string;
    owner_id: string;
    certificate_time: string;
    certificate_organization: string;
    collection_semantic: string;
}

export default defineComponent({
    name: 'BlockInfoQuery',
    components: {
        Background,
        Header,
    },
    setup() {
        const router = useRouter();
        const collectionId = ref('');
        const blockInfo = ref<BlockInfo | null>(null);
        const isLoading = ref(false);
        const error = ref<string | null>(null);
        const deleteSuccess = ref(false);
        const decodeSuccess = ref(false);
        const decodeResult = ref<string>('123');
        const searchBlock = async () => {
            if (!collectionId.value.trim()) {
                error.value = '请输入区块ID';
                return;
            }

            isLoading.value = true;
            error.value = null;
            blockInfo.value = null;

            try {
                const response = await fetch(`http://127.0.0.1:8887/selectfromchains`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        collectionid: collectionId.value,
                    }),
                });

                if (!response.ok) {
                    throw new Error(`查询失败: ${response.status}`);
                }

                const data = await response.json();
                blockInfo.value = data;
            } catch (e: any) {
                error.value = e.message || '查询发生错误';
            } finally {
                isLoading.value = false;
            }
        };

        let currentRequestId = 0;
        const deleteBlock = async () => {
            const requestId = ++currentRequestId; // 唯一请求标识
            if (!blockInfo.value?.collection_id) {
                error.value = '区块信息不存在';
                return;
            }
            isLoading.value = true;
            error.value = null;
            deleteSuccess.value = false;
            try {
                const response = await fetch(`http://127.0.0.1:8887/deletefromchains`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        collectionid: blockInfo.value.collection_id,
                    }),
                });

                if (!response.ok) {
                    throw new Error(`请求失败: ${response.status}`);
                }

                const data = await response.json();
                if (requestId !== currentRequestId) {
                    console.log('Stale request, ignoring result');
                    return; // 忽略过时请求
                }

                if (data) {
                    deleteSuccess.value = true;
                    blockInfo.value = null; // 清空区块信息
                    error.value = data.message || '删除成功';
                } else {
                    throw new Error('删除失败');

                }
            } catch (e: any) {
                error.value = e.message || '删除发生错误';
            } finally {
                isLoading.value = false;
            }
        };
        const decodeBlock = async () => {
            console.log(decodeResult)
            if (!blockInfo.value?.collection_id) {
                error.value = '区块信息不存在';
                return;
            }
            isLoading.value = true;
            error.value = null;
            decodeSuccess.value = false;
            decodeResult.value = '';

            try {
                const response = await fetch(`http://127.0.0.1:8887/selectforwatermark`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        collectionname: blockInfo.value.collection_name,
                    }),
                });

                if (!response.ok) {
                    throw new Error(`请求失败: ${response.status}`);
                }

                const data = await response.json();

                decodeResult.value = data; // 假设后端返回的解码字符串在decoded_string字段
                console.log("luanboyue Debug: ", decodeResult.value);
                console.log("Received data:", data); // 输出整个响应数据，检查是否包含 watermark
                decodeSuccess.value = true;
                console.log(decodeSuccess)
                decodeResult.value = typeof data === 'string' ? data : JSON.stringify(data);

            } catch (e: any) {
                error.value = e.message || '解码发生错误';
            } finally {
                isLoading.value = false;
            }
            console.log("luanboyue Debug2: ", decodeResult.value);
        };
        const navigateToTxt2img = () => {
            router.push({ name: 'dashboard' });
        };

        return {
            collectionId,
            searchBlock,
            blockInfo,
            isLoading,
            error,
            navigateToTxt2img,
            deleteBlock,
            deleteSuccess,
            decodeBlock,
            decodeSuccess,
            decodeResult,
        };
    },
});
</script>

<style scoped>
.page-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    background: white;
    background-size: cover;
    position: relative;
}

.main-content {
    display: flex;
    padding: 20px;
    height: calc(100vh - 60px);
    /* 假设header高度为60px */
}

.search-section {
    flex: 1;
    /* 让搜索框占据左侧的空间 */
    padding: 20px;

}

.search-container > *{
  display: flex;
  flex-direction: column;
  align-items: center; /* 使输入框和按钮水平居中 */
  background: rgba(255, 255, 255, 0.95);
  border-radius: 15px;
  padding: 10px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  width: 400px; /* 设置一个固定的宽度 */
}
.search-container > button {
  margin-bottom: 0; /* 移除按钮的外边距，使其与输入框并排 */
}

.search-title {
  color: blue;
  margin-bottom: 20px; /* 与搜索框保持一定距离 */
  font-weight: bold;
}

.custom-input {
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 8px 12px;
  font-size: 16px;
  width: calc(100% - 100px); /* 减去按钮的宽度，使其与按钮并排 */
}

.btn-search {
  background-color: blue;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  font-weight: bold;
  transition: background-color 0.3s;
  margin-left: -1px; /* 调整按钮左侧的边框，使其与输入框无缝连接 */
}


.btn-search:hover {
    background-color: blue;
}

.navigation-section {
    flex: 0 0 500px;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 20px;
}

/* .btn-navigate {
    background-color: #ffffff;
    border: none;
    border-radius: 12px;
    padding: 20px 40px;
    font-size: 18px;
    font-weight: bold;
    color: blue;
    cursor: pointer;
    transition: all 0.3s;
    display: flex;
    align-items: center;
    gap: 10px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
} */

.btn-navigate {
    position: absolute; /* 使用绝对定位 */
    top: 20px; /* 距离页面顶部20像素 */
    left: 20px; /* 距离页面左侧20像素 */
    /* 其他样式保持不变 */
    background-color: #ffffff;
    border: none;
    border-radius: 12px;
    padding: 20px 40px;
    font-size: 18px;
    font-weight: bold;
    color: blue;
    cursor: pointer;
    transition: all 0.3s;
    display: flex;
    align-items: center;
    gap: 10px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.btn-navigate:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.btn-icon {
    font-size: 24px;
    transition: transform 0.3s;
}

.btn-navigate:hover .btn-icon {
    transform: translateX(5px);
}

.block-info {
    margin-top: 20px;
    background-color: rgba(255, 255, 255, 0.9);
    border-radius: 10px;
    padding: 20px;
}

.info-content {
    margin-top: 15px;
}

.info-content p {
    margin-bottom: 10px;
    padding: 8px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.status-message {
    text-align: center;
    padding: 10px;
    border-radius: 5px;
}

.error {
    color: #dc3545;
    background-color: rgba(220, 53, 69, 0.1); 
    /* color: #28a745; 
    background-color: rgba(40, 167, 69, 0.1);  */
    width: 100%; /* 使输入框宽度占满其父容器的宽度 */
  box-sizing: border-box; /* 确保内边距和边框包含在宽度内 */
}

.success {
    width: 100%; /* 使输入框宽度占满其父容器的宽度 */
    box-sizing: border-box; /* 确保内边距和边框包含在宽度内 */
}

.background-section {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    width: 70%;
    /* 调整背景占据屏幕宽度的比例，这里设置为 50% */
    z-index: -1;
    /* 将背景层级设为 -1，使其位于其他内容之后 */
    background-color: white;
}

.page-wrapper {
    z-index: 1;
}
</style>