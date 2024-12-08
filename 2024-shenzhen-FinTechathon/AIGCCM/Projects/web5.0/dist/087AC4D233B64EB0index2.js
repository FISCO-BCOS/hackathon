/*! 
 Build based on gin-vue-admin 
 Time : 1730113336000 */
import{u as e,r as a,a as l,o as s,c as t,b as o,n as d,d as r,e as m,w as i,f as p,E as b,g as n,h as c,i as u,j as h,k as g}from"./087AC4D233B64EB0index.js";import{E as y,a as f}from"./087AC4D233B64EB0form-item.js";/* empty css                     */import"./087AC4D233B64EB0tag.js";import{E as w,a as v}from"./087AC4D233B64EB0select.js";import"./087AC4D233B64EB0scrollbar.js";/* empty css                      *//* empty css                      */import{_ as B,i as j}from"./087AC4D233B64EB0initdb.js";import{_ as q}from"./087AC4D233B64EB0_plugin-vue_export-helper.js";import"./087AC4D233B64EB0castArray.js";import"./087AC4D233B64EB0_baseClone.js";import"./087AC4D233B64EB0_Uint8Array.js";import"./087AC4D233B64EB0_initCloneObject.js";import"./087AC4D233B64EB0isEqual.js";import"./087AC4D233B64EB0index20.js";import"./087AC4D233B64EB0index21.js";import"./087AC4D233B64EB0index22.js";import"./087AC4D233B64EB0token.js";import"./087AC4D233B64EB0strings.js";import"./087AC4D233B64EB0debounce.js";const k={class:"rounded-lg flex items-center justify-evenly w-full h-full relative bg-white md:w-screen md:h-screen md:bg-[#194bfb] overflow-hidden"},x={class:"rounded-md w-full h-full flex items-center justify-center overflow-hidden"},C={class:"text-lg"},E={class:"flex items-center justify-between mt-8"},_={style:{"text-align":"right"}},A=q(Object.assign({name:"Init"},{__name:"index",setup(q){const A=e(),V=a({showReadme:!1,showForm:!1}),D=()=>{V.showReadme=!1,setTimeout((()=>{V.showForm=!0}),20)},N=()=>{window.open("https://www.gin-vue-admin.com/guide/start-quickly/env.html")},T=l(!1),U=a({dbType:"mysql",host:"127.0.0.1",port:"3306",userName:"root",password:"",dbName:"gva",dbPath:""}),P=e=>{switch(e){case"mysql":default:Object.assign(U,{dbType:"mysql",host:"127.0.0.1",port:"3306",userName:"root",password:"",dbName:"gva",dbPath:""});break;case"pgsql":Object.assign(U,{dbType:"pgsql",host:"127.0.0.1",port:"5432",userName:"postgres",password:"",dbName:"gva",dbPath:""});break;case"oracle":Object.assign(U,{dbType:"oracle",host:"127.0.0.1",port:"1521",userName:"oracle",password:"",dbName:"gva",dbPath:""});break;case"mssql":Object.assign(U,{dbType:"mssql",host:"127.0.0.1",port:"1433",userName:"mssql",password:"",dbName:"gva",dbPath:""});break;case"sqlite":Object.assign(U,{dbType:"sqlite",host:"",port:"",userName:"",password:"",dbName:"gva",dbPath:""})}},O=async()=>{const e=u.service({lock:!0,text:"正在初始化数据库，请稍候",spinner:"loading",background:"rgba(0, 0, 0, 0.7)"});try{const a=await j(U);0===a.code&&(T.value=!0,h({type:"success",message:a.msg}),A.push({name:"Login"})),e.close()}catch(a){e.close()}};return(e,a)=>{const l=b,u=w,h=v,j=f,q=g,A=y;return s(),t("div",k,[o("div",x,[a[11]||(a[11]=o("div",{class:"oblique h-[130%] w-3/5 bg-white transform -rotate-12 absolute -ml-80"},null,-1)),V.showForm?p("",!0):(s(),t("div",{key:0,class:d([V.showReadme?"slide-out-right":"slide-in-fwd-top"])},[o("div",C,[a[9]||(a[9]=r('<div class="font-sans text-4xl font-bold text-center mb-4" data-v-700abcda>GIN-VUE-ADMIN</div><p class="text-gray-600 mb-2" data-v-700abcda>初始化须知</p><p class="text-gray-600 mb-2" data-v-700abcda>1.您需有用一定的VUE和GOLANG基础</p><p class="text-gray-600 mb-2" data-v-700abcda>2.请您确认是否已经阅读过<a class="text-blue-600 font-bold" href="https://www.gin-vue-admin.com" target="_blank" data-v-700abcda>官方文档</a> <a class="text-blue-600 font-bold" href="https://www.bilibili.com/video/BV1kv4y1g7nT?p=2" target="_blank" data-v-700abcda>初始化视频</a></p><p class="text-gray-600 mb-2" data-v-700abcda>3.请您确认是否了解后续的配置流程</p><p class="text-gray-600 mb-2" data-v-700abcda>4.如果您使用mysql数据库，请确认数据库引擎为<span class="text-red-600 font-bold text-3xl ml-2" data-v-700abcda>innoDB</span></p><p class="text-gray-600 mb-2" data-v-700abcda>注：开发组不为文档中书写过的内容提供无偿服务</p>',7)),o("p",E,[m(l,{type:"primary",size:"large",onClick:N},{default:i((()=>a[7]||(a[7]=[n(" 阅读文档 ")]))),_:1}),m(l,{type:"primary",size:"large",onClick:D},{default:i((()=>a[8]||(a[8]=[n(" 我已确认 ")]))),_:1})])])],2)),V.showForm?(s(),t("div",{key:1,class:d([[V.showForm?"slide-in-left":"slide-out-right"],"w-96"])},[m(A,{ref:"formRef",model:U,"label-width":"100px",size:"large"},{default:i((()=>[m(j,{label:"数据库类型"},{default:i((()=>[m(h,{modelValue:U.dbType,"onUpdate:modelValue":a[0]||(a[0]=e=>U.dbType=e),placeholder:"请选择",class:"w-full",onChange:P},{default:i((()=>[m(u,{key:"mysql",label:"mysql",value:"mysql"}),m(u,{key:"pgsql",label:"pgsql",value:"pgsql"}),m(u,{key:"oracle",label:"oracle",value:"oracle"}),m(u,{key:"mssql",label:"mssql",value:"mssql"}),m(u,{key:"sqlite",label:"sqlite",value:"sqlite"})])),_:1},8,["modelValue"])])),_:1}),"sqlite"!==U.dbType?(s(),c(j,{key:0,label:"host"},{default:i((()=>[m(q,{modelValue:U.host,"onUpdate:modelValue":a[1]||(a[1]=e=>U.host=e),placeholder:"请输入数据库链接"},null,8,["modelValue"])])),_:1})):p("",!0),"sqlite"!==U.dbType?(s(),c(j,{key:1,label:"port"},{default:i((()=>[m(q,{modelValue:U.port,"onUpdate:modelValue":a[2]||(a[2]=e=>U.port=e),placeholder:"请输入数据库端口"},null,8,["modelValue"])])),_:1})):p("",!0),"sqlite"!==U.dbType?(s(),c(j,{key:2,label:"userName"},{default:i((()=>[m(q,{modelValue:U.userName,"onUpdate:modelValue":a[3]||(a[3]=e=>U.userName=e),placeholder:"请输入数据库用户名"},null,8,["modelValue"])])),_:1})):p("",!0),"sqlite"!==U.dbType?(s(),c(j,{key:3,label:"password"},{default:i((()=>[m(q,{modelValue:U.password,"onUpdate:modelValue":a[4]||(a[4]=e=>U.password=e),placeholder:"请输入数据库密码（没有则为空）"},null,8,["modelValue"])])),_:1})):p("",!0),m(j,{label:"dbName"},{default:i((()=>[m(q,{modelValue:U.dbName,"onUpdate:modelValue":a[5]||(a[5]=e=>U.dbName=e),placeholder:"请输入数据库名称"},null,8,["modelValue"])])),_:1}),"sqlite"===U.dbType?(s(),c(j,{key:4,label:"dbPath"},{default:i((()=>[m(q,{modelValue:U.dbPath,"onUpdate:modelValue":a[6]||(a[6]=e=>U.dbPath=e),placeholder:"请输入sqlite数据库文件存放路径"},null,8,["modelValue"])])),_:1})):p("",!0),m(j,null,{default:i((()=>[o("div",_,[m(l,{type:"primary",onClick:O},{default:i((()=>a[10]||(a[10]=[n("立即初始化")]))),_:1})])])),_:1})])),_:1},8,["model"])],2)):p("",!0)]),a[12]||(a[12]=o("div",{class:"hidden md:block w-1/2 h-full float-right bg-[#194bfb]"},[o("img",{class:"h-full",src:B,alt:"banner"})],-1))])}}}),[["__scopeId","data-v-700abcda"]]);export{A as default};
