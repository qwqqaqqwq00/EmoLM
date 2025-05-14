<template>
  <el-container class="container">
    <CodeDriveAnimTitle class="background"/>
    <div class="content">
      <div class="buttons">
        <div class="primary" @click="handleChat">开始聊天</div><div class="secondary" @click="handleLogout">退出登录</div>
      </div>
    </div>
  </el-container>
</template>

<script>
import CodeDriveAnimTitle from "@/components/CodeDriveAnimTitle.vue";

export default {
  name: "UserDashboard",
  components: {CodeDriveAnimTitle},
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

<style>
.container {
  position: relative;
  width: 100%;
  height: 93vh;
  overflow: hidden;
  flex-direction: column;
}

.background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0; /* 背景层 */
}

.content {
  position: relative;
  z-index: 1; /* 内容层，确保在背景上方 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  color: #fff;
}

.buttons {
  display: flex;
  flex-direction: row;
  gap: 20px;
}

.primary, .secondary {
  background: linear-gradient(90deg, #21c407, #3164f8);
  cursor: pointer;
  padding: 10px;
  border-radius: 15px;
  border: 2px solid transparent; /* 默认边框透明 */
  background-clip: padding-box;
  transition: border-color 0.3s ease; /* 添加过渡效果 */
}

.secondary {
  background: linear-gradient(90deg, #ee8020, #ee2421);
  background-clip: padding-box;
}

.primary:hover, .secondary:hover {
  border-color: #fff; /* 悬停时显示白色边框 */
}
</style>