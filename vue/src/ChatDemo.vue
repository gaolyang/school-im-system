<template>
  <div class="simulation-container">
    <h1 class="title">工单 #{{ currentOrder }} 现场处置模拟</h1>
    <p class="subtitle">左边是学生端(手机)，右边是老师端(后台)</p>

    <div class="controls">
      当前模拟工单：
      <button @click="changeOrder(1001)" :class="{active: currentOrder===1001}">1001 (网络故障)</button>
      <button @click="changeOrder(1002)" :class="{active: currentOrder===1002}">1002 (病毒爆发)</button>
    </div>

    <FloatingChat
        :key="`student-${currentOrder}`"
        :orderId="currentOrder"
        :currentUserId="1"
        targetRoleName="二线指导老师"
        position="left"
    />

    <FloatingChat
        :key="`teacher-${currentOrder}`"
        :orderId="currentOrder"
        :currentUserId="2"
        targetRoleName="张三同学"
        position="right"
    />

  </div>
</template>

<script setup>
import { ref } from 'vue';
import FloatingChat from './components/FloatingChat.vue'; // 确保路径正确

const currentOrder = ref(1001);

const changeOrder = (id) => {
  currentOrder.value = id;
};
</script>

<style scoped>
.simulation-container {
  text-align: center;
  padding-top: 50px;
  background-color: #f0f2f5;
  min-height: 100vh;
}
.title { color: #333; }
.subtitle { color: #666; margin-bottom: 20px; }
.controls button {
  padding: 10px 20px;
  margin: 0 10px;
  cursor: pointer;
  border: 1px solid #ccc;
  background: white;
}
.controls button.active {
  background: #3b82f6;
  color: white;
  border-color: #3b82f6;
}
</style>
