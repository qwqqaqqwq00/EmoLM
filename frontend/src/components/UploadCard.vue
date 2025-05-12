<script>
export default {
  name: "UploadCard",
  data() {
    return {
      showUploadCard: true,
      fileList: [], // 暂存文件列表
      uploadUrl: "/api/files/upload",
      uploadHeaders: {
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
    }
  },
  methods: {
    toggleUploadCard() {
      this.showUploadCard = !this.showUploadCard;
    },
    handleBeforeUpload(file) {
      this.fileList.push(file); // 暂存文件
      this.$emit('update-file-list', this.fileList);
      return false; // 阻止自动上传
    },
    handleFileRemove(file) {
      this.fileList = this.fileList.filter(f => f !== file); // 移除文件
      this.$emit('update-file-list', this.fileList);
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
}
</script>

<template>
  <div v-if="showUploadCard" class="upload-card extern-card">
    <h3>文件上传</h3>
    <el-upload
        class="upload-demo"
        :file-list="fileList"
        :before-upload="handleBeforeUpload"
        :on-remove="handleFileRemove"
        :auto-upload="true"
        :http-request="uploadFiles"
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
    <button class="btn-secondary" @click="toggleUploadCard">关闭</button>
<!--    <button class="btn-primary" @click="uploadFiles">上传</button>-->
  </div>
</template>

<style scoped>

.upload-card {
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translate(-50%, -20%);
  width: 400px;
  padding: 20px;
  background: rgba(0, 0, 0, 0.75); /* 半透明背景 */
  opacity: 0.75;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  z-index: 1000;
  border: 1px solid rgba(255, 255, 255, 0.3); /* 边框增强效果 */
}
.extern-card {
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translate(-50%, -20%);
  width: 400px;
  padding: 20px;
  opacity: 0.75;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  z-index: 1000;
  border: 1px solid rgba(255, 255, 255, 0.3); /* 边框增强效果 */
}

.upload-area {
  margin-top: 1.25rem;
  border: .25rem;
  background-image: url("data:image/svg+xml,%3csvg width='100%25' height='100%25' xmlns='http://www.w3.org/2000/svg'%3e%3crect width='100%25' height='100%25' fill='none' stroke='%23ccc' stroke-width='3' stroke-dasharray='6%2c 14' stroke-dashoffset='0' stroke-linecap='square'/%3e%3c/svg%3e");
  background-color: transparent;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  padding: 3rem;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.upload-area:hover, .upload-area:focus {
  background-image: url("data:image/svg+xml,%3csvg width='100%25' height='100%25' xmlns='http://www.w3.org/2000/svg'%3e%3crect width='100%25' height='100%25' fill='none' stroke='%232e44ff' stroke-width='3' stroke-dasharray='6%2c 14' stroke-dashoffset='0' stroke-linecap='square'/%3e%3c/svg%3e");
  cursor: pointer;
}

.upload-area-title {
  margin-top: 1rem;
  display: block;
  font-weight: 700;
  color: #FFFFFF;
}

.upload-area-description {
  display: block;
  color: #FFFFFF;
  strong {
    color: #FFFFFF;
    font-weight: 700;
  }
}

.btn-secondary {
  padding: .5rem 1rem;
  font-weight: 500;
  border: 2px solid orangered; /* 品红色边框 */
  border-radius: .25rem;
  color: #FFFFFF;
  background-color: orangered; /* 默认品红色背景 */
  transition: background-color 0.3s ease, border-color 0.3s ease, transform 0.3s ease;
}

.btn-secondary:hover {
  background-color: transparent; /* 鼠标悬停时背景透明 */
  border-color: orangered; /* 保持品红色边框 */
  cursor: pointer;
}

.btn-primary {
  padding: .5rem 1rem;
  font-weight: 500;
  border: 2px solid deepskyblue; /* 品红色边框 */
  border-radius: .25rem;
  color: #FFFFFF;
  background-color: deepskyblue; /* 默认品红色背景 */
  transition: background-color 0.3s ease, border-color 0.3s ease, transform 0.3s ease;
}

.btn-primary:hover {
  background-color: dodgerblue;
  cursor: pointer;
}

</style>