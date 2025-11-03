<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script></script>

<style>
@import url("~@/assets/css/default.css");
@import url("~@/assets/css/style.css");
@import url("~@/assets/css/ssh.css");
#app {
  font-family: Pretendard, SEBANG_Gothic_Regular, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #000;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #000;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
