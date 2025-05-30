<template>
  <div class="chat-thread">
    <div class="messages-box" ref="chatContainer">
      <div class="messages-content"
           v-for="msg in messages"
           :key="msg.id"
           :class="{ 'message-personal': msg.role === 'human', 'message': msg.role !== 'human', 'new': msg.isNew }"
      >
        <div class="avatar" v-if="msg.role === 'assistant'">
          <img :src="require('@/assets/robot.jpg')" alt="Avatar" />
        </div>
        <span>
          <template v-if="msg.value.includes('<File>')">
            <template v-for="file in parseFileTokens(msg.value)" :key="file.url">
              <span
                class="file-token-card"
                :class="{ clickable: true }"
                @click="previewFile(file)"
                style="display:inline-block;margin:0 4px 4px 0;padding:4px 12px;border-radius:12px;background:#2c3e50;color:#fff;cursor:pointer;box-shadow:0 2px 4px rgba(0,0,0,0.12);font-size:13px;vertical-align:middle;"
              >
              {{ file.url.split('_').pop().toLowerCase() }}
              </span>
            </template>
            <span v-html="removeFileTokens(msg.value)"></span>
          </template>
          <template v-else>
            {{ msg.value }}
          </template>
        </span>
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
      <div class="staged-file-list-container" v-if="stagedFiles.length">
        <div v-for="file in stagedFiles" :key="file.name" class="file-card staged">
          <svg class="file-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8l-6-6zM13 3.5L18.5 9H14V3.5zM6 20V4h6v5h5v11H6z"/>
          </svg>
          <span class="file-name">{{ file.name }}</span>
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
    <UploadCard v-if="showUploadCard" @close="toggleUploadCard" @update-file-list="updateFileList" @staging-files="stageFile" @stage-file-url="stageFileUrl"/>
    <!-- 新增：padding-space -->
  </div>
  <div class="padding-space">
  <div v-if="previewFiles.length" class="file-preview-container" style="flex-direction:column;">
    <div v-for="preview in previewFiles" :key="preview.url" class="file-preview-item" style="width:100%;">
      <img v-if="preview.type==='png'" :src="preview.url" alt="图片预览" class="image-preview" style="width:100%;height:auto;max-width:100%;" />
      <video v-else-if="preview.type==='mp4'" :src="preview.url" controls class="video-preview" style="width:100%;height:auto;max-width:100%;"></video>
      <audio v-else-if="preview.type==='mp3'" :src="preview.url" controls class="audio-preview" style="width:100%;"></audio>
      <div class="file-name">{{ preview.type.toUpperCase() }}</div>
    </div>
  </div>
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
        { id: 1, value: 'Are we meeting today?', role: 'assistant', timestamp: new Date(), isNew: false, files: [] },
        { id: 2, value: '<File>http://localhost:8080/uploads/9ac726e9-e484-42a6-882b-201ea3731585_阿能.png</File>Yes, what time suits you?', role: 'human', timestamp: new Date(), isNew: false, files: [] },
        { id: 3, value: 'I was thinking after lunch, I have a meeting in the morning.', role: 'assistant', timestamp: new Date(), isNew: true, files: [] },
      ],
      newMessage: '',
      showUploadCard: false,
      fileList: [],
      stagedFiles: [],
      fileTokens: '',
      previewFiles: [], // 新增：用于存储当前预览的文件
    };
  },
  mounted() {
    this.initMessage();
  },
  watch: {
    '$route.query.hid': {
      immediate: true,
      handler(newHid) {
        if (newHid) {
          this.initMessage();
        }
      }
    }
  },
  methods: {
    initMessage() {
        const token = localStorage.getItem('token');
        const hid = this.$route.query.hid || 0;

        if (!token) {
            console.error("User token not found in localStorage");
            return;
        }

        this.$axios.post('/api/chat/history', new URLSearchParams({ hid }), {
            headers: {
                Authorization: `Bearer ${token}` 
            }
        })
            .then(response => {
                if (response.data && Array.isArray(response.data)) {
                    this.messages = response.data.map(item => ({
                        id: item.id,
                        value: item.message,
                        role: item.role,
                        timestamp: new Date(item.timestamp),
                        isNew: false,
                        files: item.files || []
                    }));
                    this.$nextTick(this.scrollToBottom);
                } else {
                    console.error("Unexpected response format from /api/chat/history");
                }
            })
            .catch(error => {
                console.error("Failed to get chat history:", error);
            });
    },

    sendMessage() {
      if (this.newMessage.trim() === '') return;

      const token = localStorage.getItem('token');
      if (!token) {
        console.error("User token not found in localStorage");
        return;
      }

      const userMessage = {
        id: this.messages.length + 1,
        value: this.fileTokens + this.newMessage,
        role: 'human',
        timestamp: new Date(),
        isNew: true,
        files: this.stagedFiles.length > 0 ? this.stagedFiles.map(file => file.name) : []
      };
      this.messages.push(userMessage);

      const messagePayload = new URLSearchParams({
        message: this.fileTokens + this.newMessage,
        hid: this.$route.query.hid || 0
      });
      this.$axios.post('/api/chat/generate', messagePayload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
        .then(response => {
          const newMessage = {
            id: this.messages.length + 1,
            value: response.data.message || "No response from server",
            role: 'assistant',
            timestamp: new Date(),
            isNew: true,
            files: response.data.files || []
          };
          this.messages.push(newMessage);
          this.newMessage = '';
          this.fileTokens = '';
          this.stagedFiles = [];
          this.scrollToBottom();
        })
        .catch(error => {
          console.error("Failed to send message:", error);
          alert("Failed to send message. Please try again later.");
        });
    },
    toggleUploadCard() {
      this.showUploadCard = !this.showUploadCard;
    },
    updateFileList(newFileList) {
      this.fileList = newFileList;
    },
    stageFile(uploadedFileList) {
      this.stagedFiles = [...uploadedFileList];
    },
    stageFileUrl(fileUrl) {
      this.fileTokens += `<File>${fileUrl}</File>`;
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
    parseFileTokens(str) {
      // 解析 <File>...</File>，返回文件对象数组
      const regex = /<File>(.*?)<\/File>/g;
      let match;
      const files = [];
      while ((match = regex.exec(str))) {
        const url = match[1];
        const ext = url.split('.').pop().toLowerCase();
        let type = ext;
        files.push({ url, type });
      }
      return files;
    },
    removeFileTokens(str) {
      // 移除 <File>...</File> 标签，仅保留文本
      return str.replace(/<File>.*?<\/File>/g, '');
    },
    previewFile(file) {
      // 预览最后 0-3 个文件，按垂直方向，重复点击不添加
      let previews = [...this.previewFiles];
      if (previews.find(f => f.url === file.url)) return;
      previews.push(file);
      if (previews.length > 3) previews = previews.slice(-3);
      this.previewFiles = previews;
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
  word-break: break-all;
  overflow-wrap: break-word;
  white-space: pre-wrap;
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
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

/* Upload icon */
.bi-upload {
  cursor: pointer;
  color: #ccc;
}

.bi-upload :hover {
  color: #fff;
}

/* File list */
.chat-window > div {
  padding: 5px 0;
}

.messages-box::-webkit-scrollbar {
  width: 8px; 
}

.messages-box::-webkit-scrollbar-track {
  background: transparent; 
}

.messages-box::-webkit-scrollbar-thumb {
  background: #aaa; 
  border-radius: 4px; 
}

.file-list-container {
  display: flex;
  flex-wrap: wrap; 
  gap: 10px; 
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

.staged-file-list-container .file-card.staged {
  background: #253237;
  border: 1px dashed #03a9f4;
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

/* 新增：padding-space 样式 */
.padding-space {
  width: 20%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #35393b;
}

.file-preview-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding: 10px;
}

.file-preview-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.image-preview {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 5px;
}

.video-preview {
  width: 100px;
  height: 100px;
  border-radius: 5px;
}

.file-name {
  font-size: 12px;
  color: #CFD8DC;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100px;
}
</style>
