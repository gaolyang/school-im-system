<template>
  <div id="app">
    <ChatDemo />
  </div>
</template>

<script>
import ChatDemo from './ChatDemo.vue'

export default {
  name: 'App',
  components: {
    ChatDemo
  }
}
</script>

<style>
/* 重置一下浏览器的默认边距，让页面更好看 */
body {
  margin: 0;
  padding: 0;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
