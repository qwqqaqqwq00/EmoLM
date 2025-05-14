<script>
import { gsap } from "gsap";

export default {
  props: {
    titles: {
      type: Array,
      required: true,
    },
  },
  mounted() {
    const container = this.$el; // 限制动画范围到当前组件
    const tl = gsap.timeline({
      defaults: {
        duration: 2,
        yoyo: true,
        ease: "power2.inOut",
      },
    })
        .fromTo(
            container.querySelectorAll(".left, .right"),
            {
              svgOrigin: "640 360",
              skewY: (i) => [-30, 15][i],
              scaleX: (i) => [0.6, 0.85][i],
              x: 200,
            },
            {
              skewY: (i) => [-15, 30][i],
              scaleX: (i) => [0.85, 0.6][i],
              x: -200,
            }
        )
        .play(0.5);

    const tl2 = gsap.timeline();

    container.querySelectorAll("text").forEach((t, i) => {
      tl2.add(
          gsap.fromTo(
              t,
              {
                xPercent: -100,
                x: 700,
              },
              {
                duration: 1,
                xPercent: 0,
                x: 575,
                ease: "sine.inOut",
              }
          ),
          (i % 3) * 0.2
      );
    });

    window.onpointermove = (e) => {
      tl.pause();
      tl2.pause();
      gsap.to([tl, tl2], {
        duration: 2,
        ease: "power4",
        progress: e.x / window.innerWidth,
      });
    };
  },
};
</script>

<template>
  <div class="code-drive-anim-title">
    <svg viewBox="0 0 1280 720">
      <mask id="maskLeft">
        <rect x="-50%" width="100%" height="100%" fill="#fff" />
      </mask>
      <mask id="maskRight">
        <rect x="50%" width="100%" height="100%" fill="#fff" />
      </mask>
      <g font-size="150">
        <g mask="url(#maskLeft)" fill="#fff" class="left">
          <text v-for="(title, index) in titles" :key="'left-' + index" :y="120 + index * 130">{{ title }}</text>
        </g>
        <g mask="url(#maskRight)" fill="#aaa" class="right">
          <text v-for="(title, index) in titles" :key="'left-' + index" :y="120 + index * 130">{{ title }}</text>
        </g>
      </g>
    </svg>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Montserrat:wght@900&display=swap");

.code-drive-anim-title {
  width: 100%;
  height: 100vh;
  background: #000;
  overflow: hidden;
}

svg {
  width: 100%;
  height: 100%;
}

@media (prefers-color-scheme: dark) {
  .code-drive-anim-title {
    background: #000; /* 强制背景为黑色 */
    color: #fff; /* 强制字体为白色 */
  }
}
</style>