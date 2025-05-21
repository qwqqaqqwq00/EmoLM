<template>
  <CodeDriveAnimTitle :titles="titles"/>
  <div class="login-box">
    <form @submit.prevent="handleLogin">
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
      <el-button type="primary" round @click="handleLogin">Login</el-button>
      <el-button type="primary" round @click="gotoReg">Register</el-button>
    </form>
  </div>
</template>

<script>
import CodeDriveAnimTitle from "@/components/CodeDriveAnimTitle.vue";

export default {
  name: "UserLogin",
  components: {CodeDriveAnimTitle},
  data() {
    return {
      username: "",
      password: "",
      titles: ["LOGIN", "TO", "EmoLM"],
    };
  },
  methods: {
    async handleLogin() {
        try {
            const response = await this.$axios.post('/api/login', new URLSearchParams({
                username: this.username,
                password: this.password
            }));
            if (response.data.success) {
                // Store token in localStorage
                localStorage.setItem('token', response.data.token);
                alert("Login successful!");
                this.$router.push('/dashboard');
            } else {
                alert(response.data.error || "Login failed, please try again!");
            }
        } catch (error) {
            console.error("Login failed:", error);
            alert("Login failed, please check your network connection or try again later!");
        }
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