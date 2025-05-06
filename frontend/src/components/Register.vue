<template>
  <el-container>
    <el-form @submit.prevent="handleRegister">
      <el-text  type="info">用户名</el-text>
      <el-input
          v-model="username"
          label="用户名"
          required
      ></el-input>
      <el-text  type="info">密码</el-text>
      <el-input
          v-model="password"
          label="密码"
          type="password"
          required
      ></el-input>
      <el-text  type="info">确认密码</el-text>
      <el-input
          v-model="confirmPassword"
          label="确认密码"
          type="password"
          required
      ></el-input>
      <el-text  type="info">邮箱</el-text>
      <el-input
          v-model="email"
          label="邮箱"
          required
      ></el-input>
      <el-text  type="info">验证码</el-text>
      <el-input type="primary" v-model="verifyCode" label="验证码" required></el-input><el-button type="primary" @click="sendVerifyCode">发送验证码</el-button>
      <el-button type="primary" @click="handleRegister">注册</el-button>
    </el-form>
  </el-container>
</template>

<script>
export default {
  name: "UserRegister",
  data() {
    return {
      username: "",
      password: "",
      confirmPassword: "",
      email: "",
      verifyCode: "",
    };
  },
  methods: {
    sendVerifyCode() {
      this.$axios.post("/api/sendVerifyCode", {
        email: this.email,
      }).then(() => {
        alert("验证码已发送，请检查邮箱！");
      }).catch(error => {
        alert(error.response.data.error || "发送验证码失败，请重试！");
      });
    },
    handleRegister() {
      const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,16}$/;
      if (!passwordRegex.test(this.password)) {
        alert("密码长度需为8-16位，且必须包含数字和字母！");
        return;
      }
      if (this.password !== this.confirmPassword) {
        alert("两次输入的密码不一致！");
        return;
      }
      this.$axios.post("/api/register", {
        username: this.username,
        password: this.password,
        confirmPassword: this.confirmPassword,
      }).then(() => {
        alert("注册成功，请登录！");
        this.$router.push("/");
      }).catch(error => {
        alert(error.response.data.error || "注册失败，请重试！");
      });
    },
  },
};
</script>

<style scoped>
.el-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  height: 100vh; /* 设置容器高度为视口高度 */
}

</style>