<script>
import Chat from '@/components/Chat.vue'
import ChatHeader from "@/components/ChatHeader.vue";
export default {
  name: "ChatDashboard",
  components: {ChatHeader, Chat},
  data() {
    return {
      chatHistoryTitle: [
        "A normal chat example.",
        "A normal chat example.",
      ],
    };
  }
};
</script>

<template>
  <ChatHeader/>
  <div class="chat-board">
    <div class="nav-list">
      <ul v-for="chatTitle in chatHistoryTitle" :key="chatTitle" class="nav">
        <li>
          <a href="/chat">
            <div>{{ chatTitle }}</div>
          </a>
        </li>
      </ul>
    </div>
    <Chat class="chat-card"/>
    <div class="padding-space"></div>
  </div>
</template>

<style>
.chat-board {
  display:  flex;
  flex-direction: row;
  align-items: center;
  height: 100vh; /* 100% of viewport height */
  width: 102%; /* Full width */
  background: #35393b;
}

.nav-list {
  flex: 1; /* Takes remaining width */
  width: 20%;
  height: 100%; /* Full height of .chat-board */
  overflow-y: auto; /* Scroll if content overflows */
  background: #35393b;
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