/*! 
 Build based on gin-vue-admin 
 Time : 1730113336000 */
import{r as e,o as l,c as a,b as o,e as u,w as s,h as t,K as n,L as p,D as d,aV as r,cO as i,f as m,g as c,aX as b,j as V,T as B,k as f,E as y,H as k}from"./087AC4D233B64EB0index.js";import{a as C,E as h}from"./087AC4D233B64EB0form-item.js";/* empty css                      */import"./087AC4D233B64EB0tag.js";import{E as g,a as v}from"./087AC4D233B64EB0select.js";import"./087AC4D233B64EB0scrollbar.js";/* empty css                      */import{E}from"./087AC4D233B64EB0checkbox.js";/* empty css                     */import{b as j}from"./087AC4D233B64EB0stringFun.js";import{k as A}from"./087AC4D233B64EB0autoCode.js";import{t as _}from"./087AC4D233B64EB0doc.js";import{_ as D}from"./087AC4D233B64EB0_plugin-vue_export-helper.js";import"./087AC4D233B64EB0castArray.js";import"./087AC4D233B64EB0_baseClone.js";import"./087AC4D233B64EB0_Uint8Array.js";import"./087AC4D233B64EB0_initCloneObject.js";import"./087AC4D233B64EB0isEqual.js";import"./087AC4D233B64EB0index20.js";import"./087AC4D233B64EB0index21.js";import"./087AC4D233B64EB0index22.js";import"./087AC4D233B64EB0token.js";import"./087AC4D233B64EB0strings.js";import"./087AC4D233B64EB0debounce.js";const U={class:"gva-table-box"},R=D({__name:"autoPlug",setup(D){const R=e({plugName:"",routerGroup:"",hasGlobal:!0,hasRequest:!0,hasResponse:!0,global:[{key:"",type:"",desc:""}],request:[{key:"",type:"",desc:""}],response:[{key:"",type:"",desc:""}]}),q=()=>{R.plugName=j(R.plugName)},w=async()=>{if(!R.plugName||!R.routerGroup)return void V.error("插件名称和插件路由组为必填项");if(R.hasGlobal){if(R.global.some((e=>{if(!e.key||!e.type)return!0})))return void V.error("全局属性的key和type为必填项")}if(R.hasRequest){if(R.request.some((e=>{if(!e.key||!e.type)return!0})))return void V.error("请求属性的key和type为必填项")}if(R.hasResponse){if(R.response.some((e=>{if(!e.key||!e.type)return!0})))return void V.error("响应属性的key和type为必填项")}0===(await A(R)).code&&B("创建成功，插件已自动写入后端plugin目录下，请按照自己的逻辑进行创造")},x=e=>{e.push({key:"",value:""})},G=(e,l)=>{1!==e.length?e.splice(l,1):V.warning("至少有一个全局属性")};return(e,V)=>{const B=f,j=C,A=E,D=g,N=v,O=y,T=k,F=h;return l(),a("div",null,[o("div",U,[u(F,{"label-width":"140px",class:"w-[680px]"},{default:s((()=>[u(j,{label:"插件名"},{default:s((()=>[u(B,{modelValue:R.plugName,"onUpdate:modelValue":V[0]||(V[0]=e=>R.plugName=e),placeholder:"必填（英文大写字母开头）",onBlur:q},null,8,["modelValue"])])),_:1}),u(j,{label:"路由组"},{default:s((()=>[u(B,{modelValue:R.routerGroup,"onUpdate:modelValue":V[1]||(V[1]=e=>R.routerGroup=e),placeholder:"将会作为插件路由组使用"},null,8,["modelValue"])])),_:1}),u(j,{label:"使用全局属性"},{default:s((()=>[u(A,{modelValue:R.hasGlobal,"onUpdate:modelValue":V[2]||(V[2]=e=>R.hasGlobal=e)},null,8,["modelValue"])])),_:1}),R.hasGlobal?(l(),t(j,{key:0,label:"全局属性"},{default:s((()=>[(l(!0),a(n,null,p(R.global,((e,t)=>(l(),a("div",{key:t,class:"plug-row"},[o("span",null,[u(B,{modelValue:e.key,"onUpdate:modelValue":l=>e.key=l,placeholder:"key 必填"},null,8,["modelValue","onUpdate:modelValue"])]),o("span",null,[u(N,{modelValue:e.type,"onUpdate:modelValue":l=>e.type=l,placeholder:"type 必填"},{default:s((()=>[u(D,{label:"string",value:"string"}),u(D,{label:"int",value:"int"}),u(D,{label:"float32",value:"float32"}),u(D,{label:"float64",value:"float64"}),u(D,{label:"bool",value:"bool"})])),_:2},1032,["modelValue","onUpdate:modelValue"])]),o("span",null,[u(B,{modelValue:e.desc,"onUpdate:modelValue":l=>e.desc=l,placeholder:"备注 必填"},null,8,["modelValue","onUpdate:modelValue"])]),o("span",null,[u(O,{icon:d(r),circle:"",onClick:V[3]||(V[3]=e=>x(R.global))},null,8,["icon"])]),o("span",null,[u(O,{icon:d(i),circle:"",onClick:e=>G(R.global,t)},null,8,["icon","onClick"])])])))),128))])),_:1})):m("",!0),u(j,{label:"使用Request"},{default:s((()=>[u(A,{modelValue:R.hasRequest,"onUpdate:modelValue":V[4]||(V[4]=e=>R.hasRequest=e)},null,8,["modelValue"])])),_:1}),R.hasRequest?(l(),t(j,{key:1,label:"Request"},{default:s((()=>[(l(!0),a(n,null,p(R.request,((e,t)=>(l(),a("div",{key:t,class:"plug-row"},[o("span",null,[u(B,{modelValue:e.key,"onUpdate:modelValue":l=>e.key=l,placeholder:"key 必填"},null,8,["modelValue","onUpdate:modelValue"])]),o("span",null,[u(N,{modelValue:e.type,"onUpdate:modelValue":l=>e.type=l,placeholder:"type 必填"},{default:s((()=>[u(D,{label:"string",value:"string"}),u(D,{label:"int",value:"int"}),u(D,{label:"float32",value:"float32"}),u(D,{label:"float64",value:"float64"}),u(D,{label:"bool",value:"bool"})])),_:2},1032,["modelValue","onUpdate:modelValue"])]),o("span",null,[u(B,{modelValue:e.desc,"onUpdate:modelValue":l=>e.desc=l,placeholder:"备注 必填"},null,8,["modelValue","onUpdate:modelValue"])]),o("span",null,[u(O,{icon:d(r),circle:"",onClick:V[5]||(V[5]=e=>x(R.request))},null,8,["icon"])]),o("span",null,[u(O,{icon:d(i),circle:"",onClick:e=>G(R.request,t)},null,8,["icon","onClick"])])])))),128))])),_:1})):m("",!0),u(j,{label:"使用Response"},{default:s((()=>[u(A,{modelValue:R.hasResponse,"onUpdate:modelValue":V[6]||(V[6]=e=>R.hasResponse=e)},null,8,["modelValue"])])),_:1}),R.hasResponse?(l(),t(j,{key:2,label:"Response"},{default:s((()=>[(l(!0),a(n,null,p(R.response,((e,t)=>(l(),a("div",{key:t,class:"plug-row"},[o("span",null,[u(B,{modelValue:e.key,"onUpdate:modelValue":l=>e.key=l,placeholder:"key 必填"},null,8,["modelValue","onUpdate:modelValue"])]),o("span",null,[u(N,{modelValue:e.type,"onUpdate:modelValue":l=>e.type=l,placeholder:"type 必填"},{default:s((()=>[u(D,{label:"string",value:"string"}),u(D,{label:"int",value:"int"}),u(D,{label:"float32",value:"float32"}),u(D,{label:"float64",value:"float64"}),u(D,{label:"bool",value:"bool"})])),_:2},1032,["modelValue","onUpdate:modelValue"])]),o("span",null,[u(B,{modelValue:e.desc,"onUpdate:modelValue":l=>e.desc=l,placeholder:"备注 必填"},null,8,["modelValue","onUpdate:modelValue"])]),o("span",null,[u(O,{icon:d(r),circle:"",onClick:V[7]||(V[7]=e=>x(R.response))},null,8,["icon"])]),o("span",null,[u(O,{icon:d(i),circle:"",onClick:e=>G(R.response,t)},null,8,["icon","onClick"])])])))),128))])),_:1})):m("",!0),u(j,null,{default:s((()=>[u(O,{type:"primary",onClick:w},{default:s((()=>V[9]||(V[9]=[c("创建")]))),_:1}),u(T,{class:"cursor-pointer ml-3",onClick:V[8]||(V[8]=e=>d(_)("https://www.bilibili.com/video/BV1kv4y1g7nT?p=13&vd_source=f2640257c21e3b547a790461ed94875e"))},{default:s((()=>[u(d(b))])),_:1})])),_:1})])),_:1})])])}}},[["__scopeId","data-v-6e693785"]]);export{R as default};
