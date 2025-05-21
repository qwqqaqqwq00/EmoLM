<template>
  <header class="chat-header">
    <a class="chat-link" @click="createNewChatHistory">EmoLM</a>
    <div class="config-menu">
      <svg class="icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <circle cx="12" cy="12" r="3"/>
        <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33
             1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09a1.65 1.65 0 0 0-1-1.51
             1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06a1.65
             1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09a1.65 1.65 0 0 0
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
        <li @click="goToDashboard">Dashboard</li>
        <li @click="logout">Logout</li>
      </ul>
    </div>
  </header>
</template>

<script>
export default {
  name: "ChatHeader",
  data() {
    return {
      username: "username", // default username
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
            localStorage.removeItem('token');
            alert("Successfully logged out");
            this.$router.push("/");
          })
          .catch(error => {
            alert(error.response?.data?.error || "Failed to logout, please try again!");
          });
    },
    createNewChatHistory() {
      const token = localStorage.getItem('token');
      if (!token) {
        console.error("User token not found in localStorage");
        return;
      }

      // 假设我们有一个 API 来创建新的聊天历史记录
      this.$axios.post('/api/chat/create', new URLSearchParams({}), {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
        .then(response => {
          if (response.data && response.data.hid) {
            // 跳转到新的聊天页面
            this.$router.push({ path: '/chat', query: { hid: response.data.hid } });
          } else {
            console.error("Failed to create new chat history");
          }
        })
        .catch(error => {
          console.error("Failed to create new chat history:", error);
        });
    }
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
  cursor: pointer;
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
  color: #ccc; /* 设置颜色 */
}

.config-menu .icon:hover {
  color: #fff;
  cursor: pointer;
}

.dropdown-menu {
  position: absolute;
  top: 50px;
  right: 0;
  background-color: #444;
  color: #fff;
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
  background-color: #222;
}
</style>