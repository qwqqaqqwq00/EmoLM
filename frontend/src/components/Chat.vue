<template>
  <div class="chat-thread">
    <div class="messages-box">
      <div class="messages-content"
           v-for="msg in messages"
           :key="msg.id"
           :class="{ 'human-message': msg.role === 'human', 'assistant-message': msg.role !== 'human' }"
      >
        {{msg.value}}
      </div>
    </div>
    <form class="chat-window" @submit.prevent="sendMessage">
      <div v-for="file in fileList" :key="file.name">
        <div>{{ file.name }}</div>
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
        { id: 1, value: 'Are we meeting today?', role: 'assistant' },
        { id: 2, value: 'Yes, what time suits you?', role: 'human' },
        { id: 3, value: 'I was thinking after lunch, I have a meeting in the morning.', role: 'assistant' },
      ],
      newMessage: '',
      showUploadCard: false,
      fileList: [],
    };
  },
  methods: {
    sendMessage() {
      if (this.newMessage.trim() === '') return;
      this.messages.push({
        id: this.messages.length + 1,
        value: this.newMessage,
        role: 'human',
      });
      this.newMessage = '';
      this.$nextTick(() => {
        const chatThread = document.querySelector('.chat-thread');
        chatThread.scrollTop = chatThread.scrollHeight;
      });
    },
    toggleUploadCard() {
      this.showUploadCard = !this.showUploadCard;
    },
    updateFileList(newFileList) {
      this.fileList = newFileList;
    },
  },
};
</script>

<style>
.chat-thread {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100%;
  align-items: center; /* Center children horizontally */
}

/* Style for .messages-box: 50% width, full height minus chat-window */
.messages-box {
  width: 100%;
  flex: 1; /* Takes available height minus chat-window */
  background-color: lightgray; /* For visibility */
  overflow-y: auto; /* Scrollable content */
  display: flex;
  flex-direction: column;
}

/* Style for .messages-content: Individual messages */
.messages-content {
  padding: 10px;
  border-bottom: 1px solid #ddd; /* For visibility */
}

.human-message {
  align-self: flex-end;
  background-color: #007bff;
  color: white;
  border-bottom-right-radius: 5px; /* Flatter corner on user side */
}

/* Assistant messages: Left-aligned, gray background */
.assistant-message {
  align-self: flex-start;
  background-color: #e9ecef;
  color: black;
  border-bottom-left-radius: 5px; /* Flatter corner on assistant side */
}

/* Style for .chat-window: 50% width, pinned to bottom */
.chat-window {
  width: 100%;
  background-color: white; /* For visibility */
  padding: 10px;
  box-sizing: border-box;
  position: sticky;
  bottom: 0;
  z-index: 10; /* Ensure it stays above messages */
}

/* Style for .chat-input-container: Input and upload icon */
.chat-input-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

/* Style for input */
.chat-window-message {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

/* Style for upload icon */
.bi-upload {
  cursor: pointer;
}

/* Style for file list */
.chat-window > div {
  padding: 5px 0;
}

/* Style for UploadCard (assumed to be a modal/overlay) */
.upload-card {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border: 1px solid #ccc;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
}
</style>