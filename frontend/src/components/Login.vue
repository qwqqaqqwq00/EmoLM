<template>
  <div class="login-box">
    <form @submit.prevent="handleLogin">
      <div class="user-box">
        <input
            v-model="username"
            required
        />
        <label>用户名</label>
      </div>
      <div class="user-box">
        <input
            v-model="password"
            type="password"
            required
        />
        <label>密码</label>
      </div>
      <el-button type="primary" round @click="handleLogin">登录</el-button>
      <el-button type="primary" round @click="gotoReg">注册</el-button>
    </form>
  </div>
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
      this.$axios.post("/api/login", new URLSearchParams({
        username: this.username,
        password: this.password,
      })).then(() => { // 移除未使用的 response
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

.login-box {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 400px;
  padding: 40px;
  transform: translate(-50%, -50%);
  background: rgba(0,0,0,.5);
  box-sizing: border-box;
  box-shadow: 0 15px 25px rgba(0,0,0,.6);
  border-radius: 10px;
}

.login-box .user-box {
  position: relative;
}

.login-box .user-box input {
  width: 100%;
  padding: 10px 0;
  font-size: 16px;
  color: #fff;
  margin-bottom: 30px;
  border: none;
  border-bottom: 1px solid #fff;
  outline: none;
  background: transparent;
}
.login-box .user-box label {
  position: absolute;
  top:0;
  left: 0;
  padding: 10px 0;
  font-size: 16px;
  color: #fff;
  pointer-events: none;
  transition: .5s;
}

.login-box .user-box input:focus ~ label,
.login-box .user-box input:valid ~ label {
  top: -20px;
  left: 0;
  color: #03e9f4;
  font-size: 12px;
}

.login-box form a {
  position: relative;
  display: inline-block;
  padding: 10px 20px;
  color: #03e9f4;
  font-size: 16px;
  text-decoration: none;
  text-transform: uppercase;
  overflow: hidden;
  transition: .5s;
  margin-top: 40px;
  letter-spacing: 4px
}

</style>