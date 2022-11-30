import React from 'react'
import { SearchBar,Divider,Dropdown,Radio,Space,Card,Rate } from 'antd-mobile'
import {  RightOutline } from 'antd-mobile-icons'
import { useNavigate } from 'react-router-dom'
import "./demo2.css"
// 商家子组件
function ShopItem(props){
  const nav=useNavigate()
  const headerClick=(prop)=>{
    // 导航至商家详情界面
    // 还差商家详情、签约、在卡包添加一个查看合约、
    nav('/shopDetail',{
      state:{
        name:prop.name,
        rate:prop.rate,
      }
    })
  }
  return (
    <Card title={props.name} extra={<RightOutline/>}
    onHeaderClick={()=>headerClick(props)}
    className="card">
      全深圳最牛的店铺！
      <br/>评分：
      <Rate readOnly value={props.rate}></Rate>
    </Card>
  )
}
export default function index() {
  const shop=[
    {
      id:0,
      name:'xxx洗剪吹',
      rate:4,
    },
    {
      id:1,
      name:'yyy洗剪吹',
      rate:4.5,
    },
    {
      id:2,
      name:'xx早教中心',
      rate:2,
    },
    {
      id:3,
      name:'xxx培训组',
      rate:1.5,
    }
  ]
  return (
    <div>
       <SearchBar
          placeholder='请输入内容'
          showCancelButton
          style={{
            '--border-radius': '100px',
            '--background': '#ffffff',
            '--height': '42px',
            '--padding-left': '20px',
          }}
        />
         <Dropdown style={{fontSize:'xx-large'}}>
          <Dropdown.Item key='sorter' title='筛选' >
            <div style={{ padding: 12 }}>
              <Radio.Group defaultValue='default'>
                <Space direction='vertical' block>
                  <Radio block value='default'>
                    综合排序
                  </Radio>
                  <Radio block value='nearest'>
                    距离最近
                  </Radio>
                  <Radio block value='top-rated'>
                    评分最高
                  </Radio>
                </Space>
              </Radio.Group>
            </div>
          </Dropdown.Item>
        </Dropdown>
        <Divider style={{fontSize:'large',color:'black'}}>搜索与筛选结果</Divider>
          {
            shop.map(item=>(
              <ShopItem key={item.id} name={item.name} rate={item.rate}/>
            ))
          }
    </div>
  )
}
