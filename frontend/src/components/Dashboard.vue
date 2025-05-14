<template>
  <el-container class="container">
    <CodeDriveAnimTitle class="background" :titles="titles"/>
    <div class="content">
      <div class="buttons">
        <div class="btn primary" @click="handleChat">开始聊天</div><div class="btn secondary" @click="handleLogout">退出登录</div>
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
      titles: ["WELCOME", "TO", "DASHBOARD"],
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

<style lang="scss">
$white: #fff;

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
  top: 50%;
  color: #fff;
}

.buttons {
  display: flex;
  flex-direction: row;
  gap: 20px;
}

.btn {
  color: #fff;
  cursor: pointer;
  font-size: 32px;
  font-weight: 400;
  line-height: 45px;
  margin: 0 0 2em;
  max-width: 160px;
  position: relative;
  text-decoration: none;
  text-transform: uppercase;
  width: 100%;
  border: none; /* 默认无边框 */
  transition: all 1000ms linear;

  &:hover {
    border-top: 0px solid #fff !important; /* 添加上边框 */
    border-bottom: 0px solid #fff !important; /* 添加下边框 */
    border-left: none !important; /* 确保无左边框 */
    border-right: none !important; /* 确保无右边框 */
  }

  &::before,
  &::after {
    content: '';
    position: absolute;
    left: 50%;
    width: 0;
    height: 2px;
    background-color: #fff;
    transition: all 0.3s ease;
  }

  &::before {
    top: 0; /* 上边框 */
  }

  &::after {
    bottom: 0; /* 下边框 */
  }

  &:hover::before,
  &:hover::after {
    width: 100%; /* 从中间扩展到两侧 */
    left: 0;
  }
}

.btn.secondary {
  color: #fff;
  background-color: transparent;
  border: none;
  transition: all 0.3s ease;

  &:hover {
    color: red; /* 设置 hover 时的文字颜色为红色 */
  }

  &::before,
  &::after {
    content: '';
    position: absolute;
    left: 50%;
    width: 0;
    height: 2px;
    background-color: #fff;
    transition: all 0.3s ease;
  }

  &::before {
    top: 0; /* 上边框 */
  }

  &::after {
    bottom: 0; /* 下边框 */
  }

  &:hover {
    color: red; /* 设置文字颜色变红 */
  }

  &:hover::before,
  &:hover::after {
    width: 100%; /* 从中间扩展到两侧 */
    left: 0;
    background-color: red; /* 设置上下边框变红 */
  }
}
</style>