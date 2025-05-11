<template>
  <el-container>
    <h1>欢迎来到仪表盘</h1>
    <div class="buttons">
      <el-button type="primary" @click="handleChat">开始聊天</el-button>
      <el-button type="danger" @click="handleLogout">退出登录</el-button>
    </div>

    <el-upload
        class="upload-demo"
        :action="uploadUrl"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :headers="uploadHeaders"
        :before-upload="validateFile"
        accept="video/*,image/*"
        drag
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div class="el-upload__tip">仅支持视频和图像文件，大小不超过10MB</div>
    </el-upload>
  </el-container>
</template>

<script>
export default {
  name: "UserDashboard",
  data() {
    return {
      uploadUrl: "/api/files/upload",
      uploadHeaders: {
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
    };
  },
  methods: {
    handleChat() {
      this.$router.push("/chat");
    },
    handleLogout() {
      this.$axios.get("/api/logout")
          .then(() => {
            alert("已成功退出登录");
            this.$router.push("/");
          })
          .catch(error => {
            alert(error.response?.data?.error || "退出登录失败，请重试！");
          });
    },
    handleUploadSuccess(response) {
      alert("文件上传成功：" + response.message);
    },
    handleUploadError(error) {
      alert("文件上传失败：" + (error.response?.data?.error || "请重试！"));
    },
    validateFile(file) {
      const isValidType = file.type.startsWith("image/") || file.type.startsWith("video/");
      const isValidSize = file.size / 1024 / 1024 < 10;
      if (!isValidType) {
        this.$message.error("仅支持上传图片或视频文件！");
      }
      if (!isValidSize) {
        this.$message.error("文件大小不能超过10MB！");
      }
      return isValidType && isValidSize;
    },
  },
};
</script>

<style scoped>
.el-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: linear-gradient(135deg, #00bcd4, #2196f3); /* 渐变背景 */
  color: #fff;
  position: relative;
}

.a-waves {

}

.upload-demo {
  margin-top: 20px;
  width: 50%;
}
</style>