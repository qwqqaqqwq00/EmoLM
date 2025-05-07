<template>
  <div>
    <ul class="chat-thread">
      <li v-for="message in messages" :key="message.id" :class="{ 'chat-thread-odd': message.isMine, 'chat-thread-even': !message.isMine }">
        {{ message.text }}
      </li>
    </ul>
    <form class="chat-window" @submit.prevent="sendMessage">
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

    <div v-if="showUploadCard" class="upload-card">
      <h3>文件上传</h3>
      <el-upload
          class="upload-demo"
          :file-list="fileList"
          :before-upload="handleBeforeUpload"
          :on-remove="handleFileRemove"
          :auto-upload="false"
          accept="video/*,image/*"
          drag
      >
        <div>
          <div>
            <button class="upload-area">
              <span class="upload-area-title">拖拽文件到此处上传</span>
              <span class="upload-area-description">或<strong>点击选择文件</strong></span>
            </button>
          </div>
        </div>
      </el-upload>
      <el-button class="btn-secondary" type="danger" @click="toggleUploadCard">关闭</el-button>
      <el-button class="btn-primary" type="primary" @click="uploadFiles">上传</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'VueChatApp',
  data() {
    return {
      messages: [
        { id: 1, text: 'Are we meeting today?', isMine: false },
        { id: 2, text: 'Yes, what time suits you?', isMine: true },
        { id: 3, text: 'I was thinking after lunch, I have a meeting in the morning.', isMine: false },
      ],
      newMessage: '',
      showUploadCard: false,
      fileList: [], // 暂存文件列表
      uploadUrl: "/api/files/upload",
      uploadHeaders: {
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
    };
  },
  methods: {
    sendMessage() {
      if (this.newMessage.trim() === '') return;
      this.messages.push({
        id: this.messages.length + 1,
        text: this.newMessage,
        isMine: true,
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
    handleBeforeUpload(file) {
      this.fileList.push(file); // 暂存文件
      return false; // 阻止自动上传
    },
    handleFileRemove(file) {
      this.fileList = this.fileList.filter(f => f !== file); // 移除文件
    },
    uploadFiles() {
      const uploadPromises = this.fileList.map(file => {
        const formData = new FormData();
        formData.append('file', file);

        return this.$axios.post(this.uploadUrl, formData, {
          headers: this.uploadHeaders,
        });
      });

      Promise.all(uploadPromises)
        .then(responses => {
          responses.forEach((response, index) => {
            this.messages.push({
              id: this.messages.length + 1,
              text: `文件已上传: ${this.fileList[index].name}`,
              isMine: true,
            });
          });
          alert("所有文件上传成功！");
          this.fileList = []; // 清空文件列表
          this.toggleUploadCard();
        })
        .catch(() => {
          alert("部分文件上传失败，请重试！");
        });
    },
  },
};
</script>

<style lang="scss" scoped>
@import 'https://fonts.googleapis.com/css?family=Noto+Sans';

$scrollbar-width: 10px;
$chat-thread-bgd-color: rgba(25, 147, 147, 0.2);
$chat-thread-msg-arrow-size: 15px;
$chat-thread-avatar-size: 50px;
$chat-thread-offset: #{$chat-thread-avatar-size + 30px};

body {
  padding: 0;
  margin: 0;
  background: -webkit-linear-gradient(-45deg, #183850 0, #183850 25%, #192c46 50%, #22254c 75%, #22254c 100%);
  background-repeat: no-repeat;
  background-attachment: fixed;
}

.chat-thread {
  margin: 24px auto 0 auto;
  padding: 0 20px 0 0;
  list-style: none;
  overflow-y: scroll;
  overflow-x: hidden;
}

.chat-thread li {
  position: relative;
  clear: both;
  display: inline-block;
  padding: 16px 40px 16px 20px;
  margin: 0 0 20px 0;
  font: 16px/20px 'Noto Sans', sans-serif;
  border-radius: 10px;
  background-color: $chat-thread-bgd-color;
}

.chat-thread-odd {
  float: right;
  margin-right: $chat-thread-offset;
  color: #0ad5c1;
}

.chat-thread-even {
  float: left;
  margin-left: $chat-thread-offset;
  color: #0ec879;
}

.chat-window {
  position: fixed;
  bottom: 18px;
  left: 25%;
  width: 50%;
}

.chat-window-message {
  width: 100%;
  height: 48px;
  font: 32px/48px 'Noto Sans', sans-serif;
  background: none;
  color: #0ad5c1;
  border: 0;
  border-bottom: 1px solid $chat-thread-bgd-color;
  outline: none;
}

@media all and (max-width: 767px) {
  .chat-thread {
    width: 90%;
    height: 260px;
  }
  .chat-window {
    left: 5%;
    width: 90%;
  }
}

@media all and (min-width: 768px) {
  .chat-thread {
    width: 50%;
    height: 320px;
  }
}

.upload-card {
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translate(-50%, -20%);
  width: 400px;
  padding: 20px;
  background: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  z-index: 1000;
}
.chat-input-container {
  display: flex;
  align-items: center;
}
.bi.bi-upload {
  cursor: pointer;
}
.modal {
  width: 90%;
  max-width: 500px;
  margin-left: auto;
  margin-right: auto;
  margin-top: 10vh;
  margin-bottom: 10vh;
  background-color: #FFF;
  border-radius: .5rem;
  box-shadow: 0 5px 15px rgba(#000, .2);
}

.modal-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: 1.5rem 1.5rem 1rem;
}

.logo-circle {
  width: 3.5rem;
  height: 3.5rem;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  background-color: var(--c-action-primary-accent);
  svg {
    max-width: 1.5rem;
  }
}

.btn-close {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 2.25rem;
  height: 2.25rem;
  border-radius: .25rem;
  border: none;
  background-color: transparent;
  &:hover, &:focus {
    background-color: var(--c-action-primary-accent);
  }
}

.modal-body {
  padding: 1rem 1.5rem;
}

.modal-title {
  font-weight: 700;
}

.modal-description {
  color: var(--c-text-secondary);
}

.upload-area {
  margin-top: 1.25rem;
  border: none;
  background-image: url("data:image/svg+xml,%3csvg width='100%25' height='100%25' xmlns='http://www.w3.org/2000/svg'%3e%3crect width='100%25' height='100%25' fill='none' stroke='%23ccc' stroke-width='3' stroke-dasharray='6%2c 14' stroke-dashoffset='0' stroke-linecap='square'/%3e%3c/svg%3e");
  background-color: transparent;
  padding: 3rem;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  &:hover, &:focus {
    background-image: url("data:image/svg+xml,%3csvg width='100%25' height='100%25' xmlns='http://www.w3.org/2000/svg'%3e%3crect width='100%25' height='100%25' fill='none' stroke='%232e44ff' stroke-width='3' stroke-dasharray='6%2c 14' stroke-dashoffset='0' stroke-linecap='square'/%3e%3c/svg%3e");
  }
}

.upload-area-icon {
  display: block;
  width: 2.25rem;
  height: 2.25rem;
  svg {
    max-height: 100%;
    max-width: 100%;
  }
}

.upload-area-title {
  margin-top: 1rem;
  display: block;
  font-weight: 700;
  color: var(--c-text-primary);
}

.upload-area-description {
  display: block;
  color: var(--c-text-secondary);
  strong {
    color: var(--c-action-primary);
    font-weight: 700;
  }
}


.btn-secondary, .btn-primary {
  padding: .5rem 1rem;
  font-weight: 500;
  border: 2px solid var(--c-action-secondary);
  border-radius: .25rem;
  background-color: transparent;
}

.btn-primary {
  color: #FFF;
  background-color: var(--c-action-primary);
  border-color: var(--c-action-primary);
}
</style>