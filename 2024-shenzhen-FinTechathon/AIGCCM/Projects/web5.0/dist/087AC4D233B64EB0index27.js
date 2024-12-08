/*! 
 Build based on gin-vue-admin 
 Time : 1730113336000 */
import{A as e,$ as t,q as a,v as s,C as r,ci as n,cJ as o,bn as c,cK as i,bJ as l,a$ as u,ag as d,o as p,c as f,n as h,D as g,b as y,F as v,z as b,t as k,f as x,h as m,w,G as B,H as D,_ as F,I}from"./087AC4D233B64EB0index.js";const N=e({type:{type:String,default:"line",values:["line","circle","dashboard"]},percentage:{type:Number,default:0,validator:e=>e>=0&&e<=100},status:{type:String,default:"",values:["","success","exception","warning"]},indeterminate:Boolean,duration:{type:Number,default:3},strokeWidth:{type:Number,default:6},strokeLinecap:{type:t(String),default:"round"},textInside:Boolean,width:{type:Number,default:126},showText:{type:Boolean,default:!0},color:{type:t([String,Array,Function]),default:""},striped:Boolean,stripedFlow:Boolean,format:{type:t(Function),default:e=>"".concat(e,"%")}}),$=a({name:"ElProgress"});const S=I(F(a({...$,props:N,setup(e){const t=e,a={success:"#13ce66",exception:"#ff4949",warning:"#e6a23c",default:"#20a0ff"},F=s("progress"),I=r((()=>{const e={width:"".concat(t.percentage,"%"),animationDuration:"".concat(t.duration,"s")},a=M(t.percentage);return a.includes("gradient")?e.background=a:e.backgroundColor=a,e})),N=r((()=>(t.strokeWidth/t.width*100).toFixed(1))),$=r((()=>["circle","dashboard"].includes(t.type)?Number.parseInt("".concat(50-Number.parseFloat(N.value)/2),10):0)),S=r((()=>{const e=$.value,a="dashboard"===t.type;return"\n          M 50 50\n          m 0 ".concat(a?"":"-").concat(e,"\n          a ").concat(e," ").concat(e," 0 1 1 0 ").concat(a?"-":"").concat(2*e,"\n          a ").concat(e," ").concat(e," 0 1 1 0 ").concat(a?"":"-").concat(2*e,"\n          ")})),T=r((()=>2*Math.PI*$.value)),W=r((()=>"dashboard"===t.type?.75:1)),_=r((()=>{const e=-1*T.value*(1-W.value)/2;return"".concat(e,"px")})),A=r((()=>({strokeDasharray:"".concat(T.value*W.value,"px, ").concat(T.value,"px"),strokeDashoffset:_.value}))),C=r((()=>({strokeDasharray:"".concat(T.value*W.value*(t.percentage/100),"px, ").concat(T.value,"px"),strokeDashoffset:_.value,transition:"stroke-dasharray 0.6s ease 0s, stroke 0.6s ease, opacity ease 0.6s"}))),E=r((()=>{let e;return e=t.color?M(t.percentage):a[t.status]||a.default,e})),L=r((()=>"warning"===t.status?n:"line"===t.type?"success"===t.status?o:c:"success"===t.status?i:l)),z=r((()=>"line"===t.type?12+.4*t.strokeWidth:.111111*t.width+2)),J=r((()=>t.format(t.percentage)));const M=e=>{var a;const{color:s}=t;if(u(s))return s(e);if(d(s))return s;{const t=function(e){const t=100/e.length;return e.map(((e,a)=>d(e)?{color:e,percentage:(a+1)*t}:e)).sort(((e,t)=>e.percentage-t.percentage))}(s);for(const a of t)if(a.percentage>e)return a.color;return null==(a=t[t.length-1])?void 0:a.color}};return(e,t)=>(p(),f("div",{class:h([g(F).b(),g(F).m(e.type),g(F).is(e.status),{[g(F).m("without-text")]:!e.showText,[g(F).m("text-inside")]:e.textInside}]),role:"progressbar","aria-valuenow":e.percentage,"aria-valuemin":"0","aria-valuemax":"100"},["line"===e.type?(p(),f("div",{key:0,class:h(g(F).b("bar"))},[y("div",{class:h(g(F).be("bar","outer")),style:v({height:"".concat(e.strokeWidth,"px")})},[y("div",{class:h([g(F).be("bar","inner"),{[g(F).bem("bar","inner","indeterminate")]:e.indeterminate},{[g(F).bem("bar","inner","striped")]:e.striped},{[g(F).bem("bar","inner","striped-flow")]:e.stripedFlow}]),style:v(g(I))},[(e.showText||e.$slots.default)&&e.textInside?(p(),f("div",{key:0,class:h(g(F).be("bar","innerText"))},[b(e.$slots,"default",{percentage:e.percentage},(()=>[y("span",null,k(g(J)),1)]))],2)):x("v-if",!0)],6)],6)],2)):(p(),f("div",{key:1,class:h(g(F).b("circle")),style:v({height:"".concat(e.width,"px"),width:"".concat(e.width,"px")})},[(p(),f("svg",{viewBox:"0 0 100 100"},[y("path",{class:h(g(F).be("circle","track")),d:g(S),stroke:"var(".concat(g(F).cssVarName("fill-color-light"),", #e5e9f2)"),"stroke-linecap":e.strokeLinecap,"stroke-width":g(N),fill:"none",style:v(g(A))},null,14,["d","stroke","stroke-linecap","stroke-width"]),y("path",{class:h(g(F).be("circle","path")),d:g(S),stroke:g(E),fill:"none",opacity:e.percentage?1:0,"stroke-linecap":e.strokeLinecap,"stroke-width":g(N),style:v(g(C))},null,14,["d","stroke","opacity","stroke-linecap","stroke-width"])]))],6)),!e.showText&&!e.$slots.default||e.textInside?x("v-if",!0):(p(),f("div",{key:2,class:h(g(F).e("text")),style:v({fontSize:"".concat(g(z),"px")})},[b(e.$slots,"default",{percentage:e.percentage},(()=>[e.status?(p(),m(g(D),{key:1},{default:w((()=>[(p(),m(B(g(L))))])),_:1})):(p(),f("span",{key:0},k(g(J)),1))]))],6))],10,["aria-valuenow"]))}}),[["__file","progress.vue"]]));export{S as E};
