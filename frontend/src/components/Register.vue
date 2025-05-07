<template>
  <div class="login-box">
    <form @submit.prevent="handleRegister">
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
      <div class="user-box">
        <input
            v-model="confirmPassword"
            type="password"
            required
        />
        <label>确认密码</label>
      </div>
      <div class="user-box">
        <input
            v-model="email"
            required
        />
        <label>邮箱</label>
      </div>
      <div class="user-box">
        <input
            v-model="verifyCode"
            required
        />
        <label>验证码</label>
      </div>
      <el-button type="primary" :disabled="isSending" @click="sendVerifyCode">
        {{ isSending ? `${countdown}s` : "发送验证码" }}
      </el-button>
      <el-button type="primary" @click="handleRegister">注册</el-button>
    </form>
  </div>
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
      isSending: false,
      countdown: 60,
    };
  },
  methods: {
    sendVerifyCode() {
      if(this.isSending) return;
      this.isSending = true;
      this.$axios.post("/api/sendVerifyCode", new URLSearchParams({
        email: this.email,
      })).then(() => {
        alert("验证码已发送，请检查邮箱！");
      }).catch(error => {
        alert(error.response.data.error || "发送验证码失败，请重试！");
        this.isSending = false;
      });
    },
    startCountdown() {
      const timer = setInterval(() => {
        if(this.countdown>1){
          this.countdown--;
        }else{
          clearInterval(timer);
          this.isSending = false;
          this.countdown = 60;
        }
      })
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
      this.$axios.post("/api/register", new URLSearchParams({
        username: this.username,
        password: this.password,
        confirmPassword: this.confirmPassword,
        email: this.email,
        verifyCode: this.verifyCode,
      })).then(() => {
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
html {
  height: 100%;
}

.login-box {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 400px;
  padding: 40px;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.5);
  box-sizing: border-box;
  box-shadow: 0 15px 25px rgba(0, 0, 0, 0.6);
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
  top: 0;
  left: 0;
  padding: 10px 0;
  font-size: 16px;
  color: #fff;
  pointer-events: none;
  transition: 0.5s;
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
  transition: 0.5s;
  margin-top: 40px;
  letter-spacing: 4px;
}
</style>