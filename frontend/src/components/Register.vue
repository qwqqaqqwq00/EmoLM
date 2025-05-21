<template>
  <CodeDriveAnimTitle :titles="titles" />
  <div class="login-box">
    <form @submit.prevent="handleRegister">
      <div class="user-box">
        <input
            v-model="username"
            required
        />
        <label>Username</label>
      </div>
      <div class="user-box">
        <input
            v-model="password"
            type="password"
            required
        />
        <label>Password</label>
      </div>
      <div class="user-box">
        <input
            v-model="confirmPassword"
            type="password"
            required
        />
        <label>Confirm Password</label>
      </div>
      <div class="user-box">
        <input
            v-model="email"
            required
        />
        <label>Email</label>
      </div>
      <div class="user-box">
        <input
            v-model="verifyCode"
            required
        />
        <label>Verification Code</label>
      </div>
      <el-button type="primary" round :disabled="isSending" @click="sendVerifyCode">
        {{ isSending ? `${countdown}s` : "Send Code" }}
      </el-button>
      <el-button type="primary" round @click="handleLogin">Login</el-button>
      <el-button type="primary" round @click="handleRegister">Register</el-button>
    </form>
  </div>
</template>

<script>
import CodeDriveAnimTitle from "@/components/CodeDriveAnimTitle.vue";

export default {
  name: "UserRegister",
  components: {CodeDriveAnimTitle},
  data() {
    return {
      username: "",
      password: "",
      confirmPassword: "",
      email: "",
      verifyCode: "",
      isSending: false,
      countdown: 60,
      titles: ["REGISTER", "TO", "EmoLM"]
    };
  },
  methods: {
    sendVerifyCode() {
      if(this.isSending) return;
      this.isSending = true;
      this.$axios.post("/api/sendVerifyCode", new URLSearchParams({
        email: this.email,
      })).then(() => {
        alert("Verification code sent, please check your email!");
      }).catch(error => {
        alert(error.response.data.error || "Failed to send verification code, please try again!");
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
    handleLogin() {
      this.$router.push("/");
    },
    handleRegister() {
      const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,16}$/;
      if (!passwordRegex.test(this.password)) {
        alert("Password must be 8-16 characters long and contain both letters and numbers!");
        return;
      }
      if (this.password !== this.confirmPassword) {
        alert("The passwords you entered do not match!");
        return;
      }
      this.$axios.post("/api/register", new URLSearchParams({
        username: this.username,
        password: this.password,
        confirmPassword: this.confirmPassword,
        email: this.email,
        verifyCode: this.verifyCode,
      })).then(() => {
        alert("Registration successful, please login!");
        this.$router.push("/");
      }).catch(error => {
        alert(error.response.data.error || "Registration failed, please try again!");
      });
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