<script>
import Chat from '@/components/Chat.vue'
import ChatHeader from "@/components/ChatHeader.vue";

export default {
  name: "ChatDashboard",
  components: {ChatHeader, Chat},
  data() {
    return {
      chatHistoryTitles: [
        {title: "A normal chat example.", hid: 1, uid: 1},
        {title: "A normal chat example.", hid: 2, uid: 1},
      ],
    };
  },
  mounted() {
    this.fetchChatHistoryTitles();
  },
  methods: {
    fetchChatHistoryTitles() {
      const token = localStorage.getItem('token');
      if (!token) {
        console.error("User token not found in localStorage");
        return;
      }

      this.$axios.post('/api/chat/history/titles', new URLSearchParams({token}), {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
        .then(response => {
          if (response.data && Array.isArray(response.data)) {
            this.chatHistoryTitles = response.data;
          } else {
            console.error("Unexpected response format from /api/chat/history/titles");
          }
        })
        .catch(error => {
          console.error("Failed to fetch chat history titles:", error);
        });
    }
  }
};
</script>

<template>
  <ChatHeader/>
  <div class="chat-board">
    <div class="nav-list">
      <ul v-for="title in chatHistoryTitles" :key="title.hid" class="nav">
        <li>
          <a href="/chat">
            <div>{{ title.title }}</div>
          </a>
        </li>
      </ul>
    </div>
    <Chat class="chat-card"/>
    <div class="padding-space"></div>
  </div>
</template>

<style>
body {
  background-color: #000;
  color: #fff; /* 建议也设置字体颜色为白色，确保可读性 */
  margin: 0; /* 避免默认白边 */
  padding: 0;
}

.chat-board {
  display:  flex;
  flex-direction: row;
  align-items: stretch;
  height: 93vh; /* 100% of viewport height */
  width: 100%; /* Full width */
  background: #35393b;
}

.nav-list {
  flex: 1; /* Takes remaining width */
  width: 20%;
  height: 100%; /* Full height of .chat-board */
  overflow-y: auto; /* Scroll if content overflows */
  background: #35393b;
  padding: 0;
  margin: 0;
}

/* Style for .chat-window: 50% width, full height */
.chat-card {
  width: 60%; /* 50% of parent width */
  height: 100%; /* Full height of .chat-board */
}

.padding-space {
  width: 20%;
  height: 100%;
}

.nav-list li{
  border-radius: .7rem;
  border-color: #455A64;
  background: #455A64;
  margin-right: 10px;
}

.nav-list .nav a{
  text-decoration: none;
  background: none;
  color: #CFD8DC;
  font-size: 14px;
  padding: 16px 24px;
}

.nav-list .nav a:hover{
  background: none;
  color: #ECEFF1;
}

.nav-list .nav a i{
  margin-right: 16px;
}
</style>