<template>
  <header class="chat-header">
    <a href="/chat" class="chat-link">返回聊天</a>
    <div class="config-menu">
      <svg class="icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <circle cx="12" cy="12" r="3"/>
        <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33
             1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09a1.65 1.65 0 0 0-1-1.51
             1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0
             .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09a1.65 1.65 0 0 0
             1.51-1 1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06a1.65
             1.65 0 0 0 1.82.33h.09a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65
             1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83
             2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82v.09a1.65 1.65 0 0 0
             1.51 1H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"/>
      </svg>
    </div>
    <div class="user-avatar" @click="toggleDropdown">
      {{ usernameInitial }}
      <ul v-if="dropdownVisible" class="dropdown-menu">
        <li @click="goToDashboard">个人页面</li>
        <li @click="logout">退出登录</li>
      </ul>
    </div>
  </header>
</template>

<script>
export default {
  name: "ChatHeader",
  data() {
    return {
      username: "username", // 默认用户名
      dropdownVisible: false,
    };
  },
  computed: {
    usernameInitial() {
      return this.username.charAt(0).toUpperCase();
    },
  },
  methods: {
    toggleDropdown() {
      this.dropdownVisible = !this.dropdownVisible;
    },
    goToDashboard() {
      this.$router.push("/dashboard");
    },
    logout() {
      this.$axios.get("/api/logout")
          .then(() => {
            alert("已成功退出登录");
            this.$router.push("/");
          })
          .catch(error => {
            alert(error.response?.data?.error || "退出登录失败，请重试！");
          });
    },
  },
};
</script>

<style scoped>
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #2e2e2e;
  position: fixed;
  top: 0;
  left: 0;
  width: 98%;
  z-index: 100;
}

.chat-link {
  text-decoration: none;
  color: #CFD8DC;
  font-size: 16px;
}

.chat-link:hover {
  color: #ECEFF1;
}

.user-avatar {
  position: relative;
  width: 40px;
  height: 40px;
  background-color: #455A64;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.config-menu {
  position: absolute;
  top: 20px;
  right: 80px; /* 确保在 user-avatar 的右边 */
  z-index: 200; /* 确保不被遮挡 */
  display: flex;
  align-items: center;
  justify-content: center;
}

.config-menu .icon {
  width: 24px; /* 设置 svg 的宽度 */
  height: 24px; /* 设置 svg 的高度 */
  color: #fff; /* 设置颜色 */
}

.dropdown-menu {
  position: absolute;
  top: 50px;
  right: 0;
  background-color: #fff;
  color: #000;
  border: 1px solid #ccc;
  border-radius: 5px;
  list-style: none;
  padding: 1px 0;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.dropdown-menu li {
  padding: 10px 20px;
  cursor: pointer;
  white-space: nowrap;
  display: flex;
  flex-direction: row;
  gap: 10px;
  font-size: 12px;
}

.dropdown-menu li:hover {
  background-color: #f5f5f5;
}
</style>