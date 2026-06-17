/*! 
 Build based on gin-vue-admin 
 Time : 1730113336000 */
import{M as e,j as t,s as a}from"./087AC4D233B64EB0index.js";const s=e.create({baseURL:"/aaaa",timeout:1e3});s.interceptors.request.use((e=>(e.headers={"Content-Type":"application/json",...e.headers},e)),(e=>(t({showClose:!0,message:e,type:"error"}),e))),s.interceptors.response.use((e=>e.data),(e=>{}));const m=e=>a({url:"/img2img/createImg2img",method:"post",data:e}),r=e=>a({url:"/img2img/updateImg2img",method:"put",data:e}),g=e=>a({url:"/img2img/findImg2img",method:"get",params:e}),i=e=>a({url:"/img2img/getImg2imgList",method:"get",params:e});export{m as c,g as f,i as g,r as u};
