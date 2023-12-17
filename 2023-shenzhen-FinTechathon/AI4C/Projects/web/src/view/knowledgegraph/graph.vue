<template>
    <div id="graph">
        <KnowledgeGraph />
    </div>
</template>

<script setup>
import KnowledgeGraph from '@/components/knowledgeGraph/KnowledgeGraph.vue';
</script>

<style>
#graph {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color:#0b2838;
}
body,html{
    margin: 0;
    padding: 0;
}
input{
    outline: none;
    padding-left: 24px;
    padding-right: 41px;
}
 input::-webkit-input-placeholder{
      color: #ccc;
      font-size: 14px;
  }
  input::-moz-placeholder{
      color: #ccc;
      font-size: 14px;
  }
  input:-ms-input-placeholder{
      color: #ccc;
      font-size: 14px;
  }
</style>