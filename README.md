## Technique Used
### Background visual element
GSAP (GreenSock Animation Platform): A JavaScript library for high-performance animations, used to create complex animations for SVG elements, including timeline-based animations and pointer-based interactions.  

SVG (Scalable Vector Graphics): Used for rendering the animated text with masks and transformations, providing scalable and resolution-independent visuals.  

It listen to pointer movement(`window.onpointermove`), use GSAP library to control animation timeline progress based on 
pointer horizontal position `e.x` projection in range of `[0, 1]`: $`\text{progress} = \frac{\text{e.x}}{\text{window.innerWidth}}`$

reference:
https://codepen.io/creativeocean/pen/qEWOgjR

![login.png](frontend/public/login.png)

![register.png](frontend/public/register.png)

![dashboard.png](frontend/public/dashboard.png)

![chat.png](frontend/public/chat.png)

