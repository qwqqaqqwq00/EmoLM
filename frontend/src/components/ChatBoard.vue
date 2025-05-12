<script>
import Chat from '@/components/Chat.vue'
export default {
  name: "ChatDashboard",
  components: {Chat},
  data() {
    return {
      chatHistoryTitle: ["A normal chat example."],
    };
  }
};
</script>

<template>
  <div class="chat-board">
    <div class="nav-list">
      <div v-for="chatTitle in chatHistoryTitle" :key="chatTitle">
        <div>{{ chatTitle }}</div>
      </div>
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
  width: 100%; /* Full width */
}

.nav-list {
  flex: 1; /* Takes remaining width */
  width: 20%;
  height: 100%; /* Full height of .chat-board */
  overflow-y: auto; /* Scroll if content overflows */
}

/* Style for .chat-window: 50% width, full height */
.chat-card {
  width: 50%; /* 50% of parent width */
  height: 100%; /* Full height of .chat-board */
}

.padding-space {
  width: 30%;
  height: 100%;
}
</style>