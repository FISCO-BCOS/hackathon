/*! 
 Build based on gin-vue-admin 
 Time : 1730113336000 */
import{c7 as e,r as t,aQ as n,cd as d,_ as o,q as a,v as r,ab as s,y as i,z as l,x as c,a as h,aG as u,aF as p,H as f,cb as g,X as y,Q as k,d3 as N,N as v,S as C,Y as x,o as b,c as m,b as E,h as D,w as K,G as w,n as S,ak as A,f as B,e as L,F as T,K as O,L as _,ap as $,a$ as q,ag as I,O as M,P as z,b1 as F,a9 as j,ac as R,B as H,ah as P,C as U,b0 as G,t as Q,I as W}from"./087AC4D233B64EB0index.js";import{s as Y}from"./087AC4D233B64EB0token.js";import{E as X}from"./087AC4D233B64EB0index30.js";import{E as J}from"./087AC4D233B64EB0checkbox.js";const V="$treeNodeId",Z=function(e,t){t&&!t[V]&&Object.defineProperty(t,V,{value:e.id,enumerable:!1,configurable:!1,writable:!1})},ee=function(e,t){return e?t[e]:t[V]},te=(e,t,n)=>{const d=e.value.currentNode;n();const o=e.value.currentNode;d!==o&&t("current-change",o?o.data:null,o)},ne=e=>{let t=!0,n=!0,d=!0;for(let o=0,a=e.length;o<a;o++){const a=e[o];(!0!==a.checked||a.indeterminate)&&(t=!1,a.disabled||(d=!1)),(!1!==a.checked||a.indeterminate)&&(n=!1)}return{all:t,none:n,allWithoutDisable:d,half:!t&&!n}},de=function(e){if(0===e.childNodes.length||e.loading)return;const{all:t,none:n,half:d}=ne(e.childNodes);t?(e.checked=!0,e.indeterminate=!1):d?(e.checked=!1,e.indeterminate=!0):n&&(e.checked=!1,e.indeterminate=!1);const o=e.parent;o&&0!==o.level&&(e.store.checkStrictly||de(o))},oe=function(e,t){const n=e.store.props,d=e.data||{},o=n[t];if("function"==typeof o)return o(d,e);if("string"==typeof o)return d[o];if(void 0===o){const e=d[t];return void 0===e?"":e}};let ae=0;class re{constructor(t){this.id=ae++,this.text=null,this.checked=!1,this.indeterminate=!1,this.data=null,this.expanded=!1,this.parent=null,this.visible=!0,this.isCurrent=!1,this.canFocus=!1;for(const n in t)e(t,n)&&(this[n]=t[n]);this.level=0,this.loaded=!1,this.childNodes=[],this.loading=!1,this.parent&&(this.level=this.parent.level+1)}initialize(){const e=this.store;if(!e)throw new Error("[Node]store is required!");e.registerNode(this);const t=e.props;if(t&&void 0!==t.isLeaf){const e=oe(this,"isLeaf");"boolean"==typeof e&&(this.isLeafByUser=e)}if(!0!==e.lazy&&this.data?(this.setData(this.data),e.defaultExpandAll&&(this.expanded=!0,this.canFocus=!0)):this.level>0&&e.lazy&&e.defaultExpandAll&&!this.isLeafByUser&&this.expand(),Array.isArray(this.data)||Z(this,this.data),!this.data)return;const n=e.defaultExpandedKeys,d=e.key;d&&n&&n.includes(this.key)&&this.expand(null,e.autoExpandParent),d&&void 0!==e.currentNodeKey&&this.key===e.currentNodeKey&&(e.currentNode=this,e.currentNode.isCurrent=!0),e.lazy&&e._initDefaultCheckedNode(this),this.updateLeafState(),!this.parent||1!==this.level&&!0!==this.parent.expanded||(this.canFocus=!0)}setData(e){let t;Array.isArray(e)||Z(this,e),this.data=e,this.childNodes=[],t=0===this.level&&Array.isArray(this.data)?this.data:oe(this,"children")||[];for(let n=0,d=t.length;n<d;n++)this.insertChild({data:t[n]})}get label(){return oe(this,"label")}get key(){const e=this.store.key;return this.data?this.data[e]:null}get disabled(){return oe(this,"disabled")}get nextSibling(){const e=this.parent;if(e){const t=e.childNodes.indexOf(this);if(t>-1)return e.childNodes[t+1]}return null}get previousSibling(){const e=this.parent;if(e){const t=e.childNodes.indexOf(this);if(t>-1)return t>0?e.childNodes[t-1]:null}return null}contains(e,t=!0){return(this.childNodes||[]).some((n=>n===e||t&&n.contains(e)))}remove(){const e=this.parent;e&&e.removeChild(this)}insertChild(e,n,d){if(!e)throw new Error("InsertChild error: child is required.");if(!(e instanceof re)){if(!d){const t=this.getChildren(!0);t.includes(e.data)||(void 0===n||n<0?t.push(e.data):t.splice(n,0,e.data))}Object.assign(e,{parent:this,store:this.store}),(e=t(new re(e)))instanceof re&&e.initialize()}e.level=this.level+1,void 0===n||n<0?this.childNodes.push(e):this.childNodes.splice(n,0,e),this.updateLeafState()}insertBefore(e,t){let n;t&&(n=this.childNodes.indexOf(t)),this.insertChild(e,n)}insertAfter(e,t){let n;t&&(n=this.childNodes.indexOf(t),-1!==n&&(n+=1)),this.insertChild(e,n)}removeChild(e){const t=this.getChildren()||[],n=t.indexOf(e.data);n>-1&&t.splice(n,1);const d=this.childNodes.indexOf(e);d>-1&&(this.store&&this.store.deregisterNode(e),e.parent=null,this.childNodes.splice(d,1)),this.updateLeafState()}removeChildByData(e){let t=null;for(let n=0;n<this.childNodes.length;n++)if(this.childNodes[n].data===e){t=this.childNodes[n];break}t&&this.removeChild(t)}expand(e,t){const n=()=>{if(t){let e=this.parent;for(;e.level>0;)e.expanded=!0,e=e.parent}this.expanded=!0,e&&e(),this.childNodes.forEach((e=>{e.canFocus=!0}))};this.shouldLoadData()?this.loadData((e=>{Array.isArray(e)&&(this.checked?this.setChecked(!0,!0):this.store.checkStrictly||de(this),n())})):n()}doCreateChildren(e,t={}){e.forEach((e=>{this.insertChild(Object.assign({data:e},t),void 0,!0)}))}collapse(){this.expanded=!1,this.childNodes.forEach((e=>{e.canFocus=!1}))}shouldLoadData(){return!0===this.store.lazy&&this.store.load&&!this.loaded}updateLeafState(){if(!0===this.store.lazy&&!0!==this.loaded&&void 0!==this.isLeafByUser)return void(this.isLeaf=this.isLeafByUser);const e=this.childNodes;!this.store.lazy||!0===this.store.lazy&&!0===this.loaded?this.isLeaf=!e||0===e.length:this.isLeaf=!1}setChecked(e,t,n,d){if(this.indeterminate="half"===e,this.checked=!0===e,this.store.checkStrictly)return;if(!this.shouldLoadData()||this.store.checkDescendants){const{all:n,allWithoutDisable:o}=ne(this.childNodes);this.isLeaf||n||!o||(this.checked=!1,e=!1);const a=()=>{if(t){const n=this.childNodes;for(let r=0,s=n.length;r<s;r++){const o=n[r];d=d||!1!==e;const a=o.disabled?o.checked:d;o.setChecked(a,t,!0,d)}const{half:o,all:a}=ne(n);a||(this.checked=a,this.indeterminate=o)}};if(this.shouldLoadData())return void this.loadData((()=>{a(),de(this)}),{checked:!1!==e});a()}const o=this.parent;o&&0!==o.level&&(n||de(o))}getChildren(e=!1){if(0===this.level)return this.data;const t=this.data;if(!t)return null;const n=this.store.props;let d="children";return n&&(d=n.children||"children"),void 0===t[d]&&(t[d]=null),e&&!t[d]&&(t[d]=[]),t[d]}updateChildren(){const e=this.getChildren()||[],t=this.childNodes.map((e=>e.data)),n={},d=[];e.forEach(((e,o)=>{const a=e[V];!!a&&t.findIndex((e=>e[V]===a))>=0?n[a]={index:o,data:e}:d.push({index:o,data:e})})),this.store.lazy||t.forEach((e=>{n[e[V]]||this.removeChildByData(e)})),d.forEach((({index:e,data:t})=>{this.insertChild({data:t},e)})),this.updateLeafState()}loadData(e,t={}){if(!0!==this.store.lazy||!this.store.load||this.loaded||this.loading&&!Object.keys(t).length)e&&e.call(this);else{this.loading=!0;const n=n=>{this.childNodes=[],this.doCreateChildren(n,t),this.loaded=!0,this.loading=!1,this.updateLeafState(),e&&e.call(this,n)},d=()=>{this.loading=!1};this.store.load(this,n,d)}}eachNode(e){const t=[this];for(;t.length;){const n=t.shift();t.unshift(...n.childNodes),e(n)}}reInitChecked(){this.store.checkStrictly||de(this)}}class se{constructor(t){this.currentNode=null,this.currentNodeKey=null;for(const n in t)e(t,n)&&(this[n]=t[n]);this.nodesMap={}}initialize(){if(this.root=new re({data:this.data,store:this}),this.root.initialize(),this.lazy&&this.load){(0,this.load)(this.root,(e=>{this.root.doCreateChildren(e),this._initDefaultCheckedNodes()}))}else this._initDefaultCheckedNodes()}filter(e){const t=this.filterNodeMethod,n=this.lazy,d=function(o){const a=o.root?o.root.childNodes:o.childNodes;if(a.forEach((n=>{n.visible=t.call(n,e,n.data,n),d(n)})),!o.visible&&a.length){let e=!0;e=!a.some((e=>e.visible)),o.root?o.root.visible=!1===e:o.visible=!1===e}e&&o.visible&&!o.isLeaf&&(n&&!o.loaded||o.expand())};d(this)}setData(e){e!==this.root.data?(this.nodesMap={},this.root.setData(e),this._initDefaultCheckedNodes()):this.root.updateChildren()}getNode(e){if(e instanceof re)return e;const t=n(e)?ee(this.key,e):e;return this.nodesMap[t]||null}insertBefore(e,t){const n=this.getNode(t);n.parent.insertBefore({data:e},n)}insertAfter(e,t){const n=this.getNode(t);n.parent.insertAfter({data:e},n)}remove(e){const t=this.getNode(e);t&&t.parent&&(t===this.currentNode&&(this.currentNode=null),t.parent.removeChild(t))}append(e,t){const n=d(t)?this.root:this.getNode(t);n&&n.insertChild({data:e})}_initDefaultCheckedNodes(){const e=this.defaultCheckedKeys||[],t=this.nodesMap;e.forEach((e=>{const n=t[e];n&&n.setChecked(!0,!this.checkStrictly)}))}_initDefaultCheckedNode(e){(this.defaultCheckedKeys||[]).includes(e.key)&&e.setChecked(!0,!this.checkStrictly)}setDefaultCheckedKey(e){e!==this.defaultCheckedKeys&&(this.defaultCheckedKeys=e,this._initDefaultCheckedNodes())}registerNode(e){const t=this.key;if(e&&e.data)if(t){void 0!==e.key&&(this.nodesMap[e.key]=e)}else this.nodesMap[e.id]=e}deregisterNode(e){this.key&&e&&e.data&&(e.childNodes.forEach((e=>{this.deregisterNode(e)})),delete this.nodesMap[e.key])}getCheckedNodes(e=!1,t=!1){const n=[],d=function(o){(o.root?o.root.childNodes:o.childNodes).forEach((o=>{(o.checked||t&&o.indeterminate)&&(!e||e&&o.isLeaf)&&n.push(o.data),d(o)}))};return d(this),n}getCheckedKeys(e=!1){return this.getCheckedNodes(e).map((e=>(e||{})[this.key]))}getHalfCheckedNodes(){const e=[],t=function(n){(n.root?n.root.childNodes:n.childNodes).forEach((n=>{n.indeterminate&&e.push(n.data),t(n)}))};return t(this),e}getHalfCheckedKeys(){return this.getHalfCheckedNodes().map((e=>(e||{})[this.key]))}_getAllNodes(){const t=[],n=this.nodesMap;for(const d in n)e(n,d)&&t.push(n[d]);return t}updateChildren(e,t){const n=this.nodesMap[e];if(!n)return;const d=n.childNodes;for(let o=d.length-1;o>=0;o--){const e=d[o];this.remove(e.data)}for(let o=0,a=t.length;o<a;o++){const e=t[o];this.append(e,n.data)}}_setCheckedKeys(e,t=!1,n){const d=this._getAllNodes().sort(((e,t)=>e.level-t.level)),o=Object.create(null),a=Object.keys(n);d.forEach((e=>e.setChecked(!1,!1)));const r=t=>{t.childNodes.forEach((t=>{var n;o[t.data[e]]=!0,(null==(n=t.childNodes)?void 0:n.length)&&r(t)}))};for(let s=0,i=d.length;s<i;s++){const n=d[s],i=n.data[e].toString();if(a.includes(i)){if(n.childNodes.length&&r(n),n.isLeaf||this.checkStrictly)n.setChecked(!0,!1);else if(n.setChecked(!0,!0),t){n.setChecked(!1,!1);const e=function(t){t.childNodes.forEach((t=>{t.isLeaf||t.setChecked(!1,!1),e(t)}))};e(n)}}else n.checked&&!o[i]&&n.setChecked(!1,!1)}}setCheckedNodes(e,t=!1){const n=this.key,d={};e.forEach((e=>{d[(e||{})[n]]=!0})),this._setCheckedKeys(n,t,d)}setCheckedKeys(e,t=!1){this.defaultCheckedKeys=e;const n=this.key,d={};e.forEach((e=>{d[e]=!0})),this._setCheckedKeys(n,t,d)}setDefaultExpandedKeys(e){e=e||[],this.defaultExpandedKeys=e,e.forEach((e=>{const t=this.getNode(e);t&&t.expand(null,this.autoExpandParent)}))}setChecked(e,t,n){const d=this.getNode(e);d&&d.setChecked(!!t,n)}getCurrentNode(){return this.currentNode}setCurrentNode(e){const t=this.currentNode;t&&(t.isCurrent=!1),this.currentNode=e,this.currentNode.isCurrent=!0}setUserCurrentNode(e,t=!0){const n=e[this.key],d=this.nodesMap[n];this.setCurrentNode(d),t&&this.currentNode.level>1&&this.currentNode.parent.expand(null,!0)}setCurrentNodeKey(e,t=!0){if(null==e)return this.currentNode&&(this.currentNode.isCurrent=!1),void(this.currentNode=null);const n=this.getNode(e);n&&(this.setCurrentNode(n),t&&this.currentNode.level>1&&this.currentNode.parent.expand(null,!0))}}var ie=o(a({name:"ElTreeNodeContent",props:{node:{type:Object,required:!0},renderContent:Function},setup(e){const t=r("tree"),n=s("NodeInstance"),d=s("RootTree");return()=>{const o=e.node,{data:a,store:r}=o;return e.renderContent?e.renderContent(i,{_self:n,node:o,data:a,store:r}):l(d.ctx.slots,"default",{node:o,data:a},(()=>[i("span",{class:t.be("node","label")},[o.label])]))}}}),[["__file","tree-node-content.vue"]]);function le(e){const t=s("TreeNodeMap",null),n={treeNodeExpand:t=>{e.node!==t&&e.node.collapse()},children:[]};return t&&t.children.push(n),c("TreeNodeMap",n),{broadcastExpanded:t=>{if(e.accordion)for(const e of n.children)e.treeNodeExpand(t)}}}const ce=Symbol("dragEvents");const he=a({name:"ElTreeNode",components:{ElCollapseTransition:X,ElCheckbox:J,NodeContent:ie,ElIcon:f,Loading:g},props:{node:{type:re,default:()=>({})},props:{type:Object,default:()=>({})},accordion:Boolean,renderContent:Function,renderAfterExpand:Boolean,showCheckbox:{type:Boolean,default:!1}},emits:["node-expand"],setup(e,t){const n=r("tree"),{broadcastExpanded:d}=le(e),o=s("RootTree"),a=h(!1),i=h(!1),l=h(null),u=h(null),p=h(null),f=s(ce),g=$();c("NodeInstance",g),e.node.expanded&&(a.value=!0,i.value=!0);const v=o.props.props.children||"children";y((()=>{const t=e.node.data[v];return t&&[...t]}),(()=>{e.node.updateChildren()})),y((()=>e.node.indeterminate),(t=>{C(e.node.checked,t)})),y((()=>e.node.checked),(t=>{C(t,e.node.indeterminate)})),y((()=>e.node.childNodes.length),(()=>e.node.reInitChecked())),y((()=>e.node.expanded),(e=>{k((()=>a.value=e)),e&&(i.value=!0)}));const C=(t,n)=>{l.value===t&&u.value===n||o.ctx.emit("check-change",e.node.data,t,n),l.value=t,u.value=n},x=()=>{e.node.isLeaf||(a.value?(o.ctx.emit("node-collapse",e.node.data,e.node,g),e.node.collapse()):e.node.expand((()=>{t.emit("node-expand",e.node.data,e.node,g)})))},b=(t,n)=>{e.node.setChecked(n.target.checked,!o.props.checkStrictly),k((()=>{const t=o.store.value;o.ctx.emit("check",e.node.data,{checkedNodes:t.getCheckedNodes(),checkedKeys:t.getCheckedKeys(),halfCheckedNodes:t.getHalfCheckedNodes(),halfCheckedKeys:t.getHalfCheckedKeys()})}))};return{ns:n,node$:p,tree:o,expanded:a,childNodeRendered:i,oldChecked:l,oldIndeterminate:u,getNodeKey:e=>ee(o.props.nodeKey,e.data),getNodeClass:t=>{const n=e.props.class;if(!n)return{};let d;if(q(n)){const{data:e}=t;d=n(e,t)}else d=n;return I(d)?{[d]:!0}:d},handleSelectChange:C,handleClick:t=>{te(o.store,o.ctx.emit,(()=>o.store.value.setCurrentNode(e.node))),o.currentNode.value=e.node,o.props.expandOnClickNode&&x(),o.props.checkOnClickNode&&!e.node.disabled&&b(null,{target:{checked:!e.node.checked}}),o.ctx.emit("node-click",e.node.data,e.node,g,t)},handleContextMenu:t=>{o.instance.vnode.props.onNodeContextmenu&&(t.stopPropagation(),t.preventDefault()),o.ctx.emit("node-contextmenu",t,e.node.data,e.node,g)},handleExpandIconClick:x,handleCheckChange:b,handleChildNodeExpand:(e,t,n)=>{d(t),o.ctx.emit("node-expand",e,t,n)},handleDragStart:t=>{o.props.draggable&&f.treeNodeDragStart({event:t,treeNode:e})},handleDragOver:t=>{t.preventDefault(),o.props.draggable&&f.treeNodeDragOver({event:t,treeNode:{$el:p.value,node:e.node}})},handleDrop:e=>{e.preventDefault()},handleDragEnd:e=>{o.props.draggable&&f.treeNodeDragEnd(e)},CaretRight:N}}});const ue=W(o(a({name:"ElTree",components:{ElTreeNode:o(he,[["render",function(e,t,n,d,o,a){const r=v("el-icon"),s=v("el-checkbox"),i=v("loading"),l=v("node-content"),c=v("el-tree-node"),h=v("el-collapse-transition");return C((b(),m("div",{ref:"node$",class:S([e.ns.b("node"),e.ns.is("expanded",e.expanded),e.ns.is("current",e.node.isCurrent),e.ns.is("hidden",!e.node.visible),e.ns.is("focusable",!e.node.disabled),e.ns.is("checked",!e.node.disabled&&e.node.checked),e.getNodeClass(e.node)]),role:"treeitem",tabindex:"-1","aria-expanded":e.expanded,"aria-disabled":e.node.disabled,"aria-checked":e.node.checked,draggable:e.tree.props.draggable,"data-key":e.getNodeKey(e.node),onClick:A(e.handleClick,["stop"]),onContextmenu:e.handleContextMenu,onDragstart:A(e.handleDragStart,["stop"]),onDragover:A(e.handleDragOver,["stop"]),onDragend:A(e.handleDragEnd,["stop"]),onDrop:A(e.handleDrop,["stop"])},[E("div",{class:S(e.ns.be("node","content")),style:T({paddingLeft:(e.node.level-1)*e.tree.props.indent+"px"})},[e.tree.props.icon||e.CaretRight?(b(),D(r,{key:0,class:S([e.ns.be("node","expand-icon"),e.ns.is("leaf",e.node.isLeaf),{expanded:!e.node.isLeaf&&e.expanded}]),onClick:A(e.handleExpandIconClick,["stop"])},{default:K((()=>[(b(),D(w(e.tree.props.icon||e.CaretRight)))])),_:1},8,["class","onClick"])):B("v-if",!0),e.showCheckbox?(b(),D(s,{key:1,"model-value":e.node.checked,indeterminate:e.node.indeterminate,disabled:!!e.node.disabled,onClick:A((()=>{}),["stop"]),onChange:e.handleCheckChange},null,8,["model-value","indeterminate","disabled","onClick","onChange"])):B("v-if",!0),e.node.loading?(b(),D(r,{key:2,class:S([e.ns.be("node","loading-icon"),e.ns.is("loading")])},{default:K((()=>[L(i)])),_:1},8,["class"])):B("v-if",!0),L(l,{node:e.node,"render-content":e.renderContent},null,8,["node","render-content"])],6),L(h,null,{default:K((()=>[!e.renderAfterExpand||e.childNodeRendered?C((b(),m("div",{key:0,class:S(e.ns.be("node","children")),role:"group","aria-expanded":e.expanded},[(b(!0),m(O,null,_(e.node.childNodes,(t=>(b(),D(c,{key:e.getNodeKey(t),"render-content":e.renderContent,"render-after-expand":e.renderAfterExpand,"show-checkbox":e.showCheckbox,node:t,accordion:e.accordion,props:e.props,onNodeExpand:e.handleChildNodeExpand},null,8,["render-content","render-after-expand","show-checkbox","node","accordion","props","onNodeExpand"])))),128))],10,["aria-expanded"])),[[x,e.expanded]]):B("v-if",!0)])),_:1})],42,["aria-expanded","aria-disabled","aria-checked","draggable","data-key","onClick","onContextmenu","onDragstart","onDragover","onDragend","onDrop"])),[[x,e.node.visible]])}],["__file","tree-node.vue"]])},props:{data:{type:Array,default:()=>[]},emptyText:{type:String},renderAfterExpand:{type:Boolean,default:!0},nodeKey:String,checkStrictly:Boolean,defaultExpandAll:Boolean,expandOnClickNode:{type:Boolean,default:!0},checkOnClickNode:Boolean,checkDescendants:{type:Boolean,default:!1},autoExpandParent:{type:Boolean,default:!0},defaultCheckedKeys:Array,defaultExpandedKeys:Array,currentNodeKey:[String,Number],renderContent:Function,showCheckbox:{type:Boolean,default:!1},draggable:{type:Boolean,default:!1},allowDrag:Function,allowDrop:Function,props:{type:Object,default:()=>({children:"children",label:"label",disabled:"disabled"})},lazy:{type:Boolean,default:!1},highlightCurrent:Boolean,load:Function,filterNodeMethod:Function,accordion:Boolean,indent:{type:Number,default:18},icon:{type:H}},emits:["check-change","current-change","node-click","node-contextmenu","node-collapse","node-expand","check","node-drag-start","node-drag-end","node-drop","node-drag-leave","node-drag-enter","node-drag-over"],setup(e,t){const{t:n}=P(),d=r("tree"),o=s(Y,null),a=h(new se({key:e.nodeKey,data:e.data,lazy:e.lazy,props:e.props,load:e.load,currentNodeKey:e.currentNodeKey,checkStrictly:e.checkStrictly,checkDescendants:e.checkDescendants,defaultCheckedKeys:e.defaultCheckedKeys,defaultExpandedKeys:e.defaultExpandedKeys,autoExpandParent:e.autoExpandParent,defaultExpandAll:e.defaultExpandAll,filterNodeMethod:e.filterNodeMethod}));a.value.initialize();const i=h(a.value.root),l=h(null),f=h(null),g=h(null),{broadcastExpanded:k}=le(e),{dragState:N}=function({props:e,ctx:t,el$:n,dropIndicator$:d,store:o}){const a=r("tree"),s=h({showDropIndicator:!1,draggingNode:null,dropNode:null,allowDrop:!0,dropType:null});return c(ce,{treeNodeDragStart:({event:n,treeNode:d})=>{if("function"==typeof e.allowDrag&&!e.allowDrag(d.node))return n.preventDefault(),!1;n.dataTransfer.effectAllowed="move";try{n.dataTransfer.setData("text/plain","")}catch(o){}s.value.draggingNode=d,t.emit("node-drag-start",d.node,n)},treeNodeDragOver:({event:o,treeNode:r})=>{const i=r,l=s.value.dropNode;l&&l.node.id!==i.node.id&&u(l.$el,a.is("drop-inner"));const c=s.value.draggingNode;if(!c||!i)return;let h=!0,f=!0,g=!0,y=!0;"function"==typeof e.allowDrop&&(h=e.allowDrop(c.node,i.node,"prev"),y=f=e.allowDrop(c.node,i.node,"inner"),g=e.allowDrop(c.node,i.node,"next")),o.dataTransfer.dropEffect=f||h||g?"move":"none",(h||f||g)&&(null==l?void 0:l.node.id)!==i.node.id&&(l&&t.emit("node-drag-leave",c.node,l.node,o),t.emit("node-drag-enter",c.node,i.node,o)),s.value.dropNode=h||f||g?i:null,i.node.nextSibling===c.node&&(g=!1),i.node.previousSibling===c.node&&(h=!1),i.node.contains(c.node,!1)&&(f=!1),(c.node===i.node||c.node.contains(i.node))&&(h=!1,f=!1,g=!1);const k=i.$el.querySelector(".".concat(a.be("node","content"))).getBoundingClientRect(),N=n.value.getBoundingClientRect();let v;const C=h?f?.25:g?.45:1:-1,x=g?f?.75:h?.55:0:1;let b=-9999;const m=o.clientY-k.top;v=m<k.height*C?"before":m>k.height*x?"after":f?"inner":"none";const E=i.$el.querySelector(".".concat(a.be("node","expand-icon"))).getBoundingClientRect(),D=d.value;"before"===v?b=E.top-N.top:"after"===v&&(b=E.bottom-N.top),D.style.top="".concat(b,"px"),D.style.left="".concat(E.right-N.left,"px"),"inner"===v?p(i.$el,a.is("drop-inner")):u(i.$el,a.is("drop-inner")),s.value.showDropIndicator="before"===v||"after"===v,s.value.allowDrop=s.value.showDropIndicator||y,s.value.dropType=v,t.emit("node-drag-over",c.node,i.node,o)},treeNodeDragEnd:e=>{const{draggingNode:n,dropType:d,dropNode:r}=s.value;if(e.preventDefault(),e.dataTransfer&&(e.dataTransfer.dropEffect="move"),n&&r){const s={data:n.node.data};"none"!==d&&n.node.remove(),"before"===d?r.node.parent.insertBefore(s,r.node):"after"===d?r.node.parent.insertAfter(s,r.node):"inner"===d&&r.node.insertChild(s),"none"!==d&&(o.value.registerNode(s),o.value.key&&n.node.eachNode((e=>{var t;null==(t=o.value.nodesMap[e.data[o.value.key]])||t.setChecked(e.checked,!o.value.checkStrictly)}))),u(r.$el,a.is("drop-inner")),t.emit("node-drag-end",n.node,r.node,d,e),"none"!==d&&t.emit("node-drop",n.node,r.node,d,e)}n&&!r&&t.emit("node-drag-end",n.node,null,d,e),s.value.showDropIndicator=!1,s.value.draggingNode=null,s.value.dropNode=null,s.value.allowDrop=!0}}),{dragState:s}}({props:e,ctx:t,el$:f,dropIndicator$:g,store:a});!function({el$:e},t){const n=r("tree"),d=M([]),o=M([]);z((()=>{a()})),F((()=>{d.value=Array.from(e.value.querySelectorAll("[role=treeitem]")),o.value=Array.from(e.value.querySelectorAll("input[type=checkbox]"))})),y(o,(e=>{e.forEach((e=>{e.setAttribute("tabindex","-1")}))})),j(e,"keydown",(o=>{const a=o.target;if(!a.className.includes(n.b("node")))return;const r=o.code;d.value=Array.from(e.value.querySelectorAll(".".concat(n.is("focusable"),"[role=treeitem]")));const s=d.value.indexOf(a);let i;if([R.up,R.down].includes(r)){if(o.preventDefault(),r===R.up){i=-1===s?0:0!==s?s-1:d.value.length-1;const e=i;for(;!t.value.getNode(d.value[i].dataset.key).canFocus;){if(i--,i===e){i=-1;break}i<0&&(i=d.value.length-1)}}else{i=-1===s?0:s<d.value.length-1?s+1:0;const e=i;for(;!t.value.getNode(d.value[i].dataset.key).canFocus;){if(i++,i===e){i=-1;break}i>=d.value.length&&(i=0)}}-1!==i&&d.value[i].focus()}[R.left,R.right].includes(r)&&(o.preventDefault(),a.click());const l=a.querySelector('[type="checkbox"]');[R.enter,R.space].includes(r)&&l&&(o.preventDefault(),l.click())}));const a=()=>{var t;d.value=Array.from(e.value.querySelectorAll(".".concat(n.is("focusable"),"[role=treeitem]"))),o.value=Array.from(e.value.querySelectorAll("input[type=checkbox]"));const a=e.value.querySelectorAll(".".concat(n.is("checked"),"[role=treeitem]"));a.length?a[0].setAttribute("tabindex","0"):null==(t=d.value[0])||t.setAttribute("tabindex","0")}}({el$:f},a);const v=U((()=>{const{childNodes:e}=i.value,t=!!o&&0!==o.hasFilteredOptions;return(!e||0===e.length||e.every((({visible:e})=>!e)))&&!t}));y((()=>e.currentNodeKey),(e=>{a.value.setCurrentNodeKey(e)})),y((()=>e.defaultCheckedKeys),(e=>{a.value.setDefaultCheckedKey(e)})),y((()=>e.defaultExpandedKeys),(e=>{a.value.setDefaultExpandedKeys(e)})),y((()=>e.data),(e=>{a.value.setData(e)}),{deep:!0}),y((()=>e.checkStrictly),(e=>{a.value.checkStrictly=e}));const C=()=>{const e=a.value.getCurrentNode();return e?e.data:null};return c("RootTree",{ctx:t,props:e,store:a,root:i,currentNode:l,instance:$()}),c(G,void 0),{ns:d,store:a,root:i,currentNode:l,dragState:N,el$:f,dropIndicator$:g,isEmpty:v,filter:t=>{if(!e.filterNodeMethod)throw new Error("[Tree] filterNodeMethod is required when filter");a.value.filter(t)},getNodeKey:t=>ee(e.nodeKey,t.data),getNodePath:t=>{if(!e.nodeKey)throw new Error("[Tree] nodeKey is required in getNodePath");const n=a.value.getNode(t);if(!n)return[];const d=[n.data];let o=n.parent;for(;o&&o!==i.value;)d.push(o.data),o=o.parent;return d.reverse()},getCheckedNodes:(e,t)=>a.value.getCheckedNodes(e,t),getCheckedKeys:e=>a.value.getCheckedKeys(e),getCurrentNode:C,getCurrentKey:()=>{if(!e.nodeKey)throw new Error("[Tree] nodeKey is required in getCurrentKey");const t=C();return t?t[e.nodeKey]:null},setCheckedNodes:(t,n)=>{if(!e.nodeKey)throw new Error("[Tree] nodeKey is required in setCheckedNodes");a.value.setCheckedNodes(t,n)},setCheckedKeys:(t,n)=>{if(!e.nodeKey)throw new Error("[Tree] nodeKey is required in setCheckedKeys");a.value.setCheckedKeys(t,n)},setChecked:(e,t,n)=>{a.value.setChecked(e,t,n)},getHalfCheckedNodes:()=>a.value.getHalfCheckedNodes(),getHalfCheckedKeys:()=>a.value.getHalfCheckedKeys(),setCurrentNode:(n,d=!0)=>{if(!e.nodeKey)throw new Error("[Tree] nodeKey is required in setCurrentNode");te(a,t.emit,(()=>{k(n),a.value.setUserCurrentNode(n,d)}))},setCurrentKey:(n,d=!0)=>{if(!e.nodeKey)throw new Error("[Tree] nodeKey is required in setCurrentKey");te(a,t.emit,(()=>{k(),a.value.setCurrentNodeKey(n,d)}))},t:n,getNode:e=>a.value.getNode(e),remove:e=>{a.value.remove(e)},append:(e,t)=>{a.value.append(e,t)},insertBefore:(e,t)=>{a.value.insertBefore(e,t)},insertAfter:(e,t)=>{a.value.insertAfter(e,t)},handleNodeExpand:(e,n,d)=>{k(n),t.emit("node-expand",e,n,d)},updateKeyChildren:(t,n)=>{if(!e.nodeKey)throw new Error("[Tree] nodeKey is required in updateKeyChild");a.value.updateChildren(t,n)}}}}),[["render",function(e,t,n,d,o,a){const r=v("el-tree-node");return b(),m("div",{ref:"el$",class:S([e.ns.b(),e.ns.is("dragging",!!e.dragState.draggingNode),e.ns.is("drop-not-allow",!e.dragState.allowDrop),e.ns.is("drop-inner","inner"===e.dragState.dropType),{[e.ns.m("highlight-current")]:e.highlightCurrent}]),role:"tree"},[(b(!0),m(O,null,_(e.root.childNodes,(t=>(b(),D(r,{key:e.getNodeKey(t),node:t,props:e.props,accordion:e.accordion,"render-after-expand":e.renderAfterExpand,"show-checkbox":e.showCheckbox,"render-content":e.renderContent,onNodeExpand:e.handleNodeExpand},null,8,["node","props","accordion","render-after-expand","show-checkbox","render-content","onNodeExpand"])))),128)),e.isEmpty?(b(),m("div",{key:0,class:S(e.ns.e("empty-block"))},[l(e.$slots,"empty",{},(()=>{var t;return[E("span",{class:S(e.ns.e("empty-text"))},Q(null!=(t=e.emptyText)?t:e.t("el.tree.emptyText")),3)]}))],2)):B("v-if",!0),C(E("div",{ref:"dropIndicator$",class:S(e.ns.e("drop-indicator"))},null,2),[[x,e.dragState.showDropIndicator]])],2)}],["__file","tree.vue"]]));export{ue as E};
