<template>
    <div class="container">
        <svg class="keywords" @mousemove="listener($event)">
            <a href="javascript:void(0)" v-for="(tag,index) in tags" :key="index" @click="nodeClick(tag)">
                <text
                    class="text"
                    :x="tag.x"
                    :y="tag.y"
                    :font-size="20 * (600/(600-tag.z))"
                    :fill-opacity="((400+tag.z)/600)"
                    :fill="tag.color"
                    >{{tag.text}}
                </text>
            </a>
        </svg>
    </div>
</template>
<script>
    import { categarys } from './data'
    export default {
        name:'Keywords',
        components:{
            
        },
        mixins:[
            
        ],
        props:{
            
        },
        data(){
            return {
                tags:[],
                RADIUS:200,
                ZRADIUS:200,
                CX:600,
                CY:300,
                speedX: Math.PI / 360,
                speedY: Math.PI / 360,
            };
        },
        computed:{

        },
        methods:{
            /**
             * 标签点击
             */
            nodeClick(tag){
                this.$emit('searchData',tag.text)
            },
            /**
             * 初始化标签数据
             */
            initTags(){
                const tagNames = [...categarys]
                //初始化标签位置
                let tags = [];
                const length = tagNames.length
                for (let i = 0; i < length; i++) {
                    let tag = {};
                    let k = -1 + (2 * (i + 1) - 1) / length;
                    let a = Math.acos(k);
                    let b = a * Math.sqrt(length * Math.PI);
                    tag.text = tagNames[i];
                    tag.x = this.CX + this.RADIUS * Math.sin(a) * Math.cos(b);
                    tag.y = this.CY + this.RADIUS * Math.sin(a) * Math.sin(b);
                    tag.z = this.ZRADIUS * Math.cos(a);
                    tag.color =
                        "rgb(" +
                        parseInt(Math.random() * 255) +
                        "," +
                        parseInt(Math.random() * 255) +
                        "," +
                        parseInt(Math.random() * 255) +
                        ")";
                    tags.push(tag);
                }
                this.tags = [].concat(tags);
            },
            /**
             * 自动滚动效果,计算滚动位置
             */
            rotateX(speedX) {
                var cos = Math.cos(speedX);
                var sin = Math.sin(speedX);
                for (let tag of this.tags) {
                    var y1 = (tag.y - this.CY) * cos - tag.z * sin + this.CY;
                    var z1 = tag.z * cos + (tag.y - this.CY) * sin;
                    tag.y = y1;
                    tag.z = z1;
                }
            },
            /**
             * 自动滚动效果,计算滚动位置
             */
            rotateY(speedY) {
                var cos = Math.cos(speedY);
                var sin = Math.sin(speedY);
                for (let tag of this.tags) {
                    var x1 = (tag.x - this.CX) * cos - tag.z * sin + this.CX;
                    var z1 = tag.z * cos + (tag.x - this.CX) * sin;
                    tag.x = x1;
                    tag.z = z1;
                }
            },
            /**
             * 响应鼠标移动
             */
            listener(event) {
                //
                var x = event.clientX - this.CX;
                var y = event.clientY - this.CY;
                this.speedX =
                    x * 0.0001 > 0
                    ? Math.min(this.RADIUS * 0.00002, x * 0.0001)
                    : Math.max(-this.RADIUS * 0.00002, x * 0.0001);
                this.speedY =
                    y * 0.0001 > 0
                    ? Math.min(this.RADIUS * 0.00002, y * 0.0001)
                    : Math.max(-this.RADIUS * 0.00002, y * 0.0001);
            },
            /**
             * 监听窗体大小变化
             */
            resizeWindow(){
                let height = document.body.clientHeight
                let width = document.body.clientWidth
                width = width*0.85;
                if(width>1200){
                    this.CX = width/2
                }
                height = height - 150
                this.CY = height / 2;
                let radius = Math.min(this.CY,this.CX)/2
                if(radius>200){
                     this.RADIUS = radius
                }
               this.initTags()
               this.$emit('windowResize')
            }
        },
        created(){
            
        },
        mounted(){
            this.resizeWindow()
            window.addEventListener('resize',this.resizeWindow)
            this.initTags()
            //使球开始旋转
            const interval = setInterval(() => {
                this.rotateX(this.speedX);
                this.rotateY(this.speedY);
            }, 17);
        },
        beforeDestroy(){
            if (interval.value) {  
                clearInterval(interval.value);  
            }  
            window.removeEventListener('resize', this.resizeWindow);  
        }
    }
</script>
<style scoped>
    .container,.keywords{
        width: 100%;
        height:100%;
    }
    .keywords .text:hover{
        font-size: 200%;
    }
</style>