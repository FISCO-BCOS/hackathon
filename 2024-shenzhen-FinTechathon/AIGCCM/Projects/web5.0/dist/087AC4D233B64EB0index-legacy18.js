/*! 
 Build based on gin-vue-admin 
 Time : 1730113336000 */
System.register(["./087AC4D233B64EB0index-legacy.js","./087AC4D233B64EB0upload-legacy2.js","./087AC4D233B64EB0progress-legacy.js","./087AC4D233B64EB0index-legacy26.js","./087AC4D233B64EB0index-legacy27.js","./087AC4D233B64EB0cloneDeep-legacy.js","./087AC4D233B64EB0_baseClone-legacy.js","./087AC4D233B64EB0_Uint8Array-legacy.js","./087AC4D233B64EB0_initCloneObject-legacy.js","./087AC4D233B64EB0isEqual-legacy.js"],(function(e,n){"use strict";var l,a,t,u,c,o,s,i,r,d,B,f,g;return{setters:[function(e){l=e.l,a=e.a,t=e.N,u=e.o,c=e.c,o=e.e,s=e.w,i=e.b,r=e.g,d=e.D,B=e.j,f=e.H},null,null,function(e){g=e.E},null,null,null,null,null,null],execute:function(){e("default",{__name:"index",setup:function(e){var n=l(),p=a("/api"),C=function(e){if(0===e.code){var n="";e.data&&e.data.forEach((function(e,l){n+="".concat(l+1,".").concat(e.msg,"\n")})),alert(n)}else B.error(e.msg)};return function(e,l){var a=t("upload-filled"),B=f,E=g;return u(),c("div",null,[o(E,{drag:"",action:"".concat(p.value,"/autoCode/installPlugin"),headers:{"x-token":d(n).token},"show-file-list":!1,"on-success":C,"on-error":C,name:"plug"},{tip:s((function(){return l[0]||(l[0]=[i("div",{class:"el-upload__tip"}," 请把安装包的zip拖拽至此处上传 ",-1)])})),default:s((function(){return[o(B,{class:"el-icon--upload"},{default:s((function(){return[o(a)]})),_:1}),l[1]||(l[1]=i("div",{class:"el-upload__text"},[r(" 拖拽或"),i("em",null,"点击上传")],-1))]})),_:1},8,["action","headers"])])}}})}}}));
