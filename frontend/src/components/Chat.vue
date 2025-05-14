<template>
  <div class="chat-thread">
    <div class="messages-box" ref="chatContainer">
      <div class="messages-content"
           v-for="msg in messages"
           :key="msg.id"
           :class="{ 'message-personal': msg.role === 'human', 'message': msg.role !== 'human', 'new': msg.isNew }"
      >
        <div class="avatar" v-if="msg.avatar">
          <img :src="msg.avatar" alt="Avatar" />
        </div>
        <span>{{ msg.value }}</span>
        <div class="timestamp">{{ formatTimestamp(msg.timestamp) }}</div>
      </div>
    </div>
    <form class="chat-window" @submit.prevent="sendMessage">
      <div class="file-list-container">
        <div v-for="file in fileList" :key="file.name">
          <div class="file-card">
            <svg class="file-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8l-6-6zM13 3.5L18.5 9H14V3.5zM6 20V4h6v5h5v11H6z"/>
            </svg>
            <span class="file-name">{{ file.name }}</span>
          </div>
        </div>
      </div>
      <div class="chat-input-container">
        <input
            class="chat-window-message"
            v-model="newMessage"
            name="chat-window-message"
            type="text"
            autocomplete="off"
            autofocus
            placeholder="Type a message..."
        />
        <i class="bi bi-upload" @click="toggleUploadCard">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-upload" viewBox="0 0 16 16">
            <path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5z"/>
            <path d="M7.646 1.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1-.708.708L8.5 2.707V11.5a.5.5 0 0 1-1 0V2.707L5.354 4.854a.5.5 0 1 1-.708-.708l3-3z"/>
          </svg>
        </i>
      </div>
    </form>
    <UploadCard v-if="showUploadCard" @close="toggleUploadCard" @update-file-list="updateFileList"/>
  </div>
</template>

<script>
import UploadCard from "@/components/UploadCard.vue";
export default {
  name: 'VueChatApp',
  components: {
    UploadCard,
  },
  data() {
    return {
      messages: [
        { id: 1, value: 'Are we meeting today?', role: 'assistant', timestamp: new Date(), avatar: require('@/assets/robot.jpg'), isNew: false },
        { id: 2, value: 'Yes, what time suits you?', role: 'human', timestamp: new Date(), avatar: null, isNew: false },
        { id: 3, value: 'I was thinking after lunch, I have a meeting in the morning.', role: 'assistant', timestamp: new Date(), avatar: require('@/assets/robot.jpg'), isNew: true },
      ],
      newMessage: '',
      showUploadCard: false,
      fileList: [],
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.scrollToBottom();
    });
  },
  methods: {
    sendMessage() {
      if (this.newMessage.trim() === '') return;
      this.messages.push({
        id: this.messages.length + 1,
        value: this.newMessage,
        role: 'human',
        timestamp: new Date(),
        avatar: null,
        isNew: true,
      });
      this.newMessage = '';
      this.$nextTick(() => {
        const chatThread = document.querySelector('.chat-thread');
        chatThread.scrollTop = chatThread.scrollHeight;
        this.scrollToBottom();
      });
    },
    toggleUploadCard() {
      this.showUploadCard = !this.showUploadCard;
    },
    updateFileList(newFileList) {
      this.fileList = newFileList;
    },
    formatTimestamp(date) {
      return new Date(date).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    },
    scrollToBottom() {
      const container = this.$refs.chatContainer;
      if (container) {
        container.scrollTop = container.scrollHeight;
      }
    },
  },
};
</script>

<style>
.chat-thread {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  align-items: center;
}

/* Messages box: 50% width, scrollable */
.messages-box {
  width: 100%;
  height: 80%;
  flex: 1;
  background-color: #454545;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  padding: 10px;
  box-sizing: border-box;
  border-radius: 15px;
}

/* Messages content: Base bubble styles */
.messages-content {
  max-width: 100%;
  margin: 8px 0 8px 40px;
  padding: 6px 10px 7px;
  border-radius: 10px 10px 10px 0;
  background: lightslategray;
  font-size: 14px;
  line-height: 1.4;
  position: relative;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  color: rgba(255, 255, 255, 0.9);
  align-self: flex-start; /* Default: left for assistant */
}

/* Timestamp */
.messages-content .timestamp {
  position: absolute;
  bottom: -15px;
  font-size: 9px;
  color: rgba(255, 255, 255, 0.3);
  left: 0;
}

/* Triangle tail for assistant messages */
.messages-content::before {
  content: '';
  position: absolute;
  bottom: -6px;
  border-top: 6px solid transparent;
  left: 0;
  border-right: 7px solid transparent;
}

/* Avatar */
.messages-content .avatar {
  position: absolute;
  z-index: 1;
  bottom: -15px;
  left: -40px;
  border-radius: 30px;
  width: 30px;
  height: 30px;
  overflow: hidden;
  margin: 0;
  padding: 0;
  border: 2px solid rgba(255, 255, 255, 0.24);
}

.messages-content .avatar img {
  width: 100%;
  height: auto;
}

/* Human messages: Right-aligned */
.message-personal {
  align-self: flex-end;
  color: #fff;
  text-align: right;
  background: #253237;
  border-radius: 10px 10px 0 10px;
}

.message-personal::before {
  left: auto;
  right: 0;
  border-right: none;
  border-left: 5px solid transparent;
  border-top: 4px solid transparent;
  bottom: -4px;
}

.message-personal .timestamp {
  right: 0;
  left: auto;
}

/* Last message margin */
.messages-content:last-child {
  margin-bottom: 30px;
}

/* Bounce animation for new messages */
.messages-content.new {
  transform: scale(0);
  transform-origin: 0 0;
  animation: bounce 500ms linear both;
}

@keyframes bounce {
  0% {
    transform: matrix3d(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  4.7% {
    transform: matrix3d(0.45, 0, 0, 0, 0, 0.45, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  9.41% {
    transform: matrix3d(0.883, 0, 0, 0, 0, 0.883, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  14.11% {
    transform: matrix3d(1.141, 0, 0, 0, 0, 1.141, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  18.72% {
    transform: matrix3d(1.212, 0, 0, 0, 0, 1.212, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  24.32% {
    transform: matrix3d(1.151, 0, 0, 0, 0, 1.151, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  29.93% {
    transform: matrix3d(1.048, 0, 0, 0, 0, 1.048, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  35.54% {
    transform: matrix3d(0.979, 0, 0, 0, 0, 0.979, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  41.04% {
    transform: matrix3d(0.961, 0, 0, 0, 0, 0.961, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  52.15% {
    transform: matrix3d(0.991, 0, 0, 0, 0, 0.991, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  63.26% {
    transform: matrix3d(1.007, 0, 0, 0, 0, 1.007, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  85.49% {
    transform: matrix3d(0.999, 0, 0, 0, 0, 0.999, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
  100% {
    transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
  }
}

/* Chat window: Pinned to bottom */
.chat-window {
  width: 100%;
  background: #26282a;
  padding: 10px;
  box-sizing: border-box;
  position: sticky;
  bottom: 0;
  z-index: 10;
  border-radius: 15px;
}

/* Chat input container */
.chat-input-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

/* Input styling */
.chat-window-message {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  background: #222;
  color: #fff;
  border-radius: 4px;
}

/* Upload icon */
.bi-upload {
  cursor: pointer;
  color: #ccc;
}

/* File list */
.chat-window > div {
  padding: 5px 0;
}

.messages-box::-webkit-scrollbar {
  width: 8px; /* 滚动条宽度 */
}

.messages-box::-webkit-scrollbar-track {
  background: transparent; /* 滚动条轨道背景 */
}

.messages-box::-webkit-scrollbar-thumb {
  background: #aaa; /* 滚动条颜色 */
  border-radius: 4px; /* 圆角 */
}

.file-list-container {
  display: flex;
  flex-wrap: wrap; /* 允许换行 */
  gap: 10px; /* 控制卡片之间的间距 */
}

.file-card {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 8px 12px;
  background: #2c3e50;
  color: #fff;
  border-radius: 20px;
  font-size: 14px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  margin: 5px 0;
}

.file-icon {
  width: 20px;
  height: 20px;
  fill: #03a9f4;
}

.file-name {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>