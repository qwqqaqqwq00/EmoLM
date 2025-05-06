<template>
  <el-container>
    <h1>欢迎来到仪表盘</h1>
    <el-button type="danger" @click="handleLogout">退出登录</el-button>
  </el-container>
</template>

<script>
export default {
  name: "UserDashboard",
  methods: {
    handleLogout() {
      this.$axios.post("/api/logout")
          .then(() => {
            alert("已成功退出登录");
            this.$router.push("/");
          })
          .catch(error => {
            alert(error.response.data.error || "退出登录失败，请重试！");
          });
    },
  },
};
</script>

<style scoped>
.el-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  height: 10vh; /* 设置容器高度为视口高度 */
}

</style>