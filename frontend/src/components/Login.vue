<template>
  <el-container>
    <el-form @submit.prevent="handleLogin">
      <el-text type="info">用户登录</el-text>
      <el-input
          v-model="username"
          label="用户名"
          required
      ></el-input>
      <el-text type="info">用户密码</el-text>
      <el-input
          v-model="password"
          label="密码"
          type="password"
          required
      ></el-input>
      <el-button type="primary">登录</el-button>
      <el-button type="primary" @click="gotoReg">注册</el-button>
    </el-form>
  </el-container>
</template>

<script>
export default {
  name: "UserLogin", // 修改为多单词名称
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    handleLogin() {
      // 调用后端 API 进行登录
      this.$axios.post("/api/login", {
        username: this.username,
        password: this.password,
      }).then(() => { // 移除未使用的 response
        alert("登录成功");
        this.$router.push("/dashboard");
      }).catch(error => {
        alert(error.response.data.error || "登录失败");
      });
    },
    gotoReg() {
      this.$router.push("/register");
    },
  },
};
</script>

<style scoped>
.el-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  height: 10vh; /* 设置容器高度为视口高度 */
}

</style>