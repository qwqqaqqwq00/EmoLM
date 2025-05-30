<script>
export default {
  name: "UploadCard",
  data() {
    return {
      showUploadCard: true,
      fileList: [], // Temporary file list
      uploadUrl: "/api/files/upload",
      uploadHeaders: {
        Authorization: "Bearer " + localStorage.getItem("token"),
        "Content-Type": "multipart/form-data",
      },
    }
  },
  methods: {
    toggleUploadCard() {
      this.showUploadCard = !this.showUploadCard;
      this.fileList = [];
      this.$emit('update-file-list', this.fileList);
    },
    handleBeforeUpload(file) {
      this.fileList.push(file); // Store file temporarily
      this.$emit('update-file-list', this.fileList);
      return false; // Prevent automatic upload
    },
    handleFileRemove(file) {
      this.fileList = this.fileList.filter(f => f !== file); // Remove file
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
          responses.forEach(response => {
            this.$emit('stage-file-url', response.data.filePath);
          });
          alert("All files uploaded successfully!");
          this.fileList = [];
          this.toggleUploadCard();
        })
        .catch((e) => {
          alert(e);
          alert("Some files failed to upload, please try again!");
        });
    },
  },
}
</script>

<template>
  <div v-if="showUploadCard" class="upload-card extern-card">
    <h3>File Upload</h3>
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
      <div class="box1">
        <div class="box2">
          <button class="upload-area">
            <span class="upload-area-title">Drag files here to upload</span>
            <span class="upload-area-description">or <strong>click to select files</strong></span>
          </button>
        </div>
      </div>
    </el-upload>
    <div class="btn-box">
      <button class="btn-secondary" @click="toggleUploadCard">Close</button>
      <button class="btn-primary" @click="uploadFiles">Upload</button>
    </div>
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
  background: rgba(0, 0, 0, 0.75); /* Semi-transparent background */
  opacity: 0.75;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  z-index: 1000;
  border: 1px solid rgba(255, 255, 255, 0.3); /* Border enhancement */
  color: #fff;
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
  border: 1px solid rgba(255, 255, 255, 0.3); /* Border enhancement */
}
.upload-demo {
  background-color: #1e1e1e; /* Set dark background */
  border: 1px solid rgba(255, 255, 255, 0.1); /* Adjust border color */
}

:deep .el-upload-dragger {
  background-color: #2e2e2e !important;
  border-color: #555 !important;
  color: #ccc !important;
}

.upload-area {
  margin-top: 1.25rem;
  border: .25rem;
  background-image: url("data:image/svg+xml,%3csvg width='100%25' height='100%25' xmlns='http://www.w3.org/2000/svg'%3e%3crect width='100%25' height='100%25' fill='none' stroke='%23ccc' stroke-width='3' stroke-dasharray='6%2c 14' stroke-dashoffset='0' stroke-linecap='square'/%3e%3c/svg%3e");
  background-color: #1e1e1e;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  padding: 3rem;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.upload-area:hover, .upload-area:focus {
  background-color: #2a2a2a;
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

.btn-box {
  display: flex;
  gap: 30px; /* Set button spacing to 30px */
  justify-content: center;
}

.btn-secondary {
  padding: .5rem 1rem;
  font-weight: 500;
  border: 2px solid orangered; /* Orange-red border */
  border-radius: .25rem;
  color: #FFFFFF;
  background-color: orangered; /* Default orange-red background */
  transition: background-color 0.3s ease, border-color 0.3s ease, transform 0.3s ease;
}

.btn-secondary:hover {
  background-color: transparent; /* transparent on hover */
  border-color: orangered; /* keep orange-red border */
  cursor: pointer;
}

.btn-primary {
  padding: .5rem 1rem;
  font-weight: 500;
  border: 2px solid deepskyblue; /* Deep sky blue border */
  border-radius: .25rem;
  color: #FFFFFF;
  background-color: deepskyblue; /* Default deep sky blue background */
  transition: background-color 0.3s ease, border-color 0.3s ease, transform 0.3s ease;
}

.btn-primary:hover {
  background-color: transparent;
  border-color: dodgerblue;
  cursor: pointer;
}

</style>