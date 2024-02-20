<template>
    <div id="chart" class="chart">
    </div>
</template>
<script>
    import{ expendNodes } from './mock'

    // 引入 ECharts 主模块
    import * as echarts from 'echarts';

    export default {
        name:'Charts',
        components:{
            
        },
        mixins:[
            
        ],
        props:{
            chartList:{
                type:Array,
                required:true
            }
        },
        watch:{
            chartList:{
                handler(val){
                    this.formatData(val||[],true)
                },
            }
        },
        data(){
            return {
                myChart:'',
                seriesData:[],
                seriesLinks:[],
                lastClickId:'',
            };
        },
        computed:{
            
        },
        methods:{
            /**
             * 节点点击事件
             */
            async nodeClick(params){
                const index = this.seriesData.findIndex(item=>item.id === params.data.id)
                const info = this.seriesData[index]
                if(info.isRoot) return
                if(!info.isClicked){
                    this.lastClickId = info.id
                    info.isClicked = true
                    this.seriesData.splice(index,1,info)
                    let result = await expendNodes(info.id)
                    this.formatData(result)
                }else{ //已经点击过  当前点击需要折叠
                    info.isClicked = false
                    this.seriesData.splice(index,1,info)
                    this.removeChilds(info.id)
                }
                
            },
            /**
             * 设置echarts配置项,重绘画布
             */
            initCharts(){
                if(!this.myChart){
                    this.myChart = echarts.init(document.getElementById('chart'));
                    this.myChart.on('click', (params)=> {
                        if (params.dataType === "node") { //判断点击的是图表的节点部分
                            this.nodeClick(params)
                            console.log('params',params)
                        }
                    })
                }
                
                // 指定图表的配置项和数据
                let option = {
                    // 动画更新变化时间
                    animationDurationUpdate: 1500,                       
                    animationEasingUpdate: 'quinticInOut',  
                    tooltip:{
                        show:false
                    },
                    series: [
                        {
                            type:'graph',
                            layout:'force',
                            legendHoverLink: true, //是否启用图例 hover(悬停) 时的联动高亮。
                            hoverAnimation: true, //是否开启鼠标悬停节点的显示动画
                            edgeLabel: {   
                                position:'middle',       //边上的文字样式
                                normal: {
                                    formatter:"{c}",
                                    show:true,
                                    
                                }
                            },
                            edgeSymbol: ['arrow', ''],
                            force:{
                                gravity: 0.1,
                                edgeLength: [100,300],
                                repulsion:350
                            },
                            roam:true,
                            draggable : true,//每个节点的拖拉
                            itemStyle:{
                                normal: {
                                    color:'#00FAE1',
                                    cursor:'pointer',
                                    //color:Math.floor(Math.random()*16777215).toString(16),
                                    // color: ['#fc853e','#28cad8','#9564bf','#bd407e','#28cad8','#fc853e','#e5a214'],
                                    label: { 
                                            //formatter: "{c}",为什么这个写上就不打开了？
                                            show: true ,
                                            position:[-10, -15],
                                            textStyle : { //标签的字体样式
                                                color : '#fff', //字体颜色
                                                fontStyle : 'normal',//文字字体的风格 'normal'标准 'italic'斜体 'oblique' 倾斜
                                                fontWeight : 'bolder',//'normal'标准'bold'粗的'bolder'更粗的'lighter'更细的或100 | 200 | 300 | 400...
                                                fontFamily : 'sans-serif', //文字的字体系列
                                                fontSize : 12, //字体大小
                                            }
                                    },
                                    nodeStyle: {
                                        brushType: "both",
                                        borderColor: "rgba(255,215,0,0.4)",
                                        borderWidth: 1,
                                    },
                                },
                                //鼠标放上去有阴影效果
                                emphasis: {
                                    shadowColor: '#00FAE1',
                                    shadowOffsetX: 0,
                                    shadowOffsetY: 0,
                                    shadowBlur: 40,
                                    focus: 'adjacency',
                                },
                            },
                            lineStyle:{
                                width:2,
                                color: 'source',
                                curveness: 0.05
                            },
                            label:{
                                color:'#00FAE1',
                                fontSize:18,
                            },
                            symbolSize:14,//节点大小
                            links: this.seriesLinks,
                            data:this.seriesData,
                            cursor: 'pointer',
                        }
                    ]
                };
                console.log('option',option)
                // 使用刚指定的配置项和数据显示图表。
                this.myChart.setOption(option);
            },
            /**
             * 格式化数据到表格需要的数据
             */
            formatData(list,reset = false){
                let nodes = []
                const data = []
                const links = []
                let target = ''
                if(reset){
                    this.seriesData = []
                    this.seriesLinks = []
                    nodes = [].concat(list[0].children)
                    target = list[0].id +''
                    const dataInfo = {
                        "id": target,
                        "name": list[0].name,
                        category:list[0].categary,
                        isClicked:true,
                        isRoot:true,
                        symbolSize:30,
                    }
                    data.push(dataInfo)
                }else{
                    nodes = [].concat(list)
                    target = this.lastClickId
                }

                nodes.forEach((item,index)=>{
                    // 注意 id必须是string  否则连线连不上
                    const id = item.id + ''
                    const dataInfo = {
                        "id": id,
                        parentId:target,
                        category:item.categary,
                        "name": item.name,
                        isClicked:false,
                    }
                    data.push(dataInfo)
                    if(target!=item.id){
                        links.push({
                            value:item.categary,
                            target:target,
                            source:id,
                        })
                    }
                    
                })
                this.seriesData.push(...data)
                this.seriesLinks.push(...links)
                
                this.initCharts()
            },
            /**
             * 点击节点折叠操作
             */
            removeChilds(id){
                // 清除data数据
                let list = []
                let links = []
                let delIds = []
                this.getDeleteParentIds(delIds,[id])
                
                this.seriesData.map(item=>{
                    if(!delIds.includes(item.id)){
                        list.push(item)
                    }
                })
                this.seriesLinks.map(item=>{
                    if(!delIds.includes(item.source)&&!delIds.includes(item.target)){
                        links.push(item)
                    }
                })
                this.seriesData  = [].concat(list)
                this.seriesLinks  = [].concat(links)
                this.initCharts()
            },
            /**
             * 递归获取当前节点以下节点id
             */
            getDeleteParentIds(delIds,ids){
                let list = []
                this.seriesData.map(item=>{
                    if(ids.includes(item.parentId)){
                        list.push(item.id)
                    }
                })
                if(list.length>0){
                    delIds.push(...list)
                    this.getDeleteParentIds(delIds,list)
                }
            }
        },
        created(){
    
        },
        mounted(){

        },
        beforeDestroy(){
            
        }
    }
</script>
<style scoped>
    .chart{
        height:100%;
    }
</style>