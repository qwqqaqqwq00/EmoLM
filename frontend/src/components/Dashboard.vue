<template>
  <el-container class="container">
    <CodeDriveAnimTitle class="background" :titles="titles"/>
    <div class="content">
      <div class="buttons">
        <div class="btn primary" @click="handleChat">Start Chat</div><div class="btn secondary" @click="handleLogout">Logout</div>
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
            alert("Successfully logged out");
            this.$router.push("/");
          })
          .catch(error => {
            alert(error.response?.data?.error || "Failed to logout, please try again!");
          });
    },
    handleUploadSuccess(response) {
      alert("File uploaded successfully: " + response.message);
    },
    handleUploadError(error) {
      alert("File upload failed: " + (error.response?.data?.error || "Please try again!"));
    },
    validateFile(file) {
      const isValidType = file.type.startsWith("image/") || file.type.startsWith("video/");
      const isValidSize = file.size / 1024 / 1024 < 10;
      if (!isValidType) {
        this.$message.error("Only image or video files are supported!");
      }
      if (!isValidSize) {
        this.$message.error("File size cannot exceed 10MB!");
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
  z-index: 0; /* Background layer */
}

.content {
  position: relative;
  z-index: 1; /* Content layer, ensure above background */
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
  border: none; /* No border by default */
  transition: all 1000ms linear;

  &:hover {
    border-top: 0px solid #fff !important; /* Add top border */
    border-bottom: 0px solid #fff !important; /* Add bottom border */
    border-left: none !important; /* Ensure no left border */
    border-right: none !important; /* Ensure no right border */
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
    top: 0; /* Top border */
  }

  &::after {
    bottom: 0; /* Bottom border */
  }

  &:hover::before,
  &:hover::after {
    width: 100%; /* Expand from center to sides */
    left: 0;
  }
}

.btn.secondary {
  color: #fff;
  background-color: transparent;
  border: none;
  transition: all 0.3s ease;

  &:hover {
    color: red; /* Set text color to red on hover */
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
    top: 0; /* Top border */
  }

  &::after {
    bottom: 0; /* Bottom border */
  }

  &:hover {
    color: red; /* Set text color to red */
  }

  &:hover::before,
  &:hover::after {
    width: 100%; /* Expand from center to sides */
    left: 0;
    background-color: red; /* Set top and bottom borders to red */
  }
}
</style>