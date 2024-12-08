<template>
    <div>
      <div class="decision-table">
        <el-card>
          <div slot="header" class="table-header">
            <span>区块链决策记录</span>
            <el-tooltip content="数据来源于区块链，不可篡改" placement="top">
              <i class="el-icon-info blockchain-icon"></i>
            </el-tooltip>
          </div>
          <el-table :data="decisionData" style="width: 100%">
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="walletInfo" label="钱包信息" width="120" />
            <el-table-column prop="evidence" label="异常证据" min-width="300">
              <template #default="{ row }">
                <el-tooltip :content="row.evidence" placement="top">
                  <span>{{ row.evidence.length > 50 ? row.evidence.slice(0, 50) + '...' : row.evidence }}</span>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="node" label="节点" width="80" />
            <el-table-column prop="createTime" label="时间" width="160" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.status === '已通过' ? 'success' : 'danger'">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="blockHeight" label="区块高度" width="100" />
            <el-table-column prop="txHash" label="交易哈希" width="200">
              <template #default="{ row }">
                <el-tooltip :content="row.txHash" placement="top">
                  <span class="hash-text">{{ row.txHash.slice(0, 10) + '...' + row.txHash.slice(-8) }}</span>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column prop="address" label="合约地址" width="200">
              <template #default="{ row }">
                <el-tooltip :content="row.address" placement="top">
                  <span class="hash-text">{{ row.address }}</span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
  
      <EventLogs />
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    name: 'DecisionTable',
    data() {
      return {
        decisionData: []
      };
    },
    async created() {
      await this.fetchDecisionData();
    },
    methods: {
      async fetchDecisionData() {
        try {
          const response = await axios.post('http://45.8.113.140:3338/api/v1/regulator/event');
          console.log(response.data);
          if (response.data.code === 0 && response.data.message === 'success') {
            this.decisionData = response.data.data.map((item, index) => {
              const { log } = item;
              
              // 解析data字段
              const parts = log.data.split(',');
              // 获取最后三个元素
              const nodeId = parts[parts.length - 3];
              const timestamp = parts[parts.length - 2];
              const status = parts[parts.length - 1];
              
              // 获取异常证据（除去最后三个元素的所有内容）
              const evidence = parts.slice(0, -3).join(',');

              return {
                id: index,
                walletInfo: evidence.split('-')[0] + '-' + evidence.split('-')[1], // 提取wallet-12部分
                evidence: evidence,
                createTime: new Date(parseInt(timestamp)).toLocaleString(),
                node: nodeId,
                timestamp: new Date(parseInt(timestamp)).toLocaleString(),
                status: status === 'true' ? '已通过' : '已拒绝',
                blockHeight: log.blockNumber,
                txHash: log.transactionHash,
                address: log.address
              };
            });
          }
        } catch (error) {
          console.error('获取区块链决策记录失败:', error);
          this.$message.error('获取区块链决策记录失败');
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .decision-table {
    padding: 20px;
  }
  
  .table-header {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    display: flex;
    align-items: center;
  }
  
  .blockchain-icon {
    margin-left: 8px;
    color: #409EFF;
    cursor: help;
  }
  
  .hash-link {
    font-family: monospace;
  }
  
  .hash-link:hover {
    text-decoration: underline;
  }
  
  .el-table th {
    background-color: #f5f7fa;
    color: #333;
  }
  
  .el-table td {
    color: #666;
  }
  </style>