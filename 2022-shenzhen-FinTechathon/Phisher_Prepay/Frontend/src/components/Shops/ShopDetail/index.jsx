import { Divider, NavBar, Rate, SideBar,Badge,List, Popover, Card } from 'antd-mobile'
import React from 'react'
import { useState } from 'react'
import { useNavigate,useLocation } from 'react-router-dom'
import styles from'./demo2.less'
import classNames from 'classnames'
import {EditFill,ArrowsAltOutline} from 'antd-mobile-icons'

export default function ShopDetail() {
    const nav=useNavigate()
    const {state:{name,rate}}=useLocation()
    const back=()=>{
        nav('/tabs/Shops')
    }
    const [activeKey,setActiveKey]=useState('key3')
    const tabs=[
        {
            key: 'key1',
            title: '商家简介',
            badge: Badge.dot,
          },
          {
            key: 'key2',
            title: '爆款推荐',
            badge: '5',
          },
          {
            key: 'key3',
            title: '超值套餐',
            badge: '99+',
            disabled: true,
          },
    ]
    const actions= [
        { key: 'detail', icon: <ArrowsAltOutline />, url:'',text: '套餐详情' },
        { key: 'sign', icon: <EditFill />,url:'/contractDetail', text: '申请预付卡' },
      ]
  return (
    <div>
      <NavBar onBack={back}>商家详情</NavBar>
      <div style={{margin:'5px'}}>
        <div style={{fontSize:'xx-large',fontWeight:'bold'}}> {name}</div>
        <span style={{fontSize:'x-large'}}>大众评分:<Rate readOnly value={rate}/></span>
      </div>
      <Divider/>
      <div className="container">
      <div className="side">
        <SideBar activeKey={activeKey} onChange={setActiveKey}>
          {tabs.map(item => (
            <SideBar.Item key={item.key} title={item.title} style={{fontSize:'large'}}/>
          ))}
        </SideBar>
      </div>
      <div className="main">
        <div
          className={classNames(
            "content",
            activeKey === 'key1' && "active"
          )}
        >
          <span style={{margin:'5px'}}>这家主人很懒，没有添加额外介绍哦！</span>
        </div>
        <div
          className={classNames(
            "content",
            activeKey === 'key2' && "active"
          )}
        >
          <span>爆款还得是线下聊</span>
        </div>
        <div
          className={classNames(
            "combo",
            activeKey === 'key3' && "active"
          )}
        >
          <List header='套餐清单' style={{'--header-font-size':'32px',margin:'5px',width:'100%'}}>
            <Popover.Menu mode='dark' placement='bottom-start' trigger='click'
            actions={actions} onAction={node=>(
                nav(node.url)
            )}
            style={{margin:'2px'}}>
                {/* <List.Item clickable>套餐一</List.Item> */}
                <Card title='套餐一' className='card'><span style={{fontSize:'small'}}>销量：99+</span></Card>
            </Popover.Menu>
            <Popover.Menu mode='dark' placement='bottom-start' trigger='click'
            actions={actions} onAction={node=>(
                nav(node.url)
            )}
            style={{margin:'2px'}}>
                {/* <List.Item clickable>套餐一</List.Item> */}
                <Card title='套餐二'className='card'><span style={{fontSize:'small'}}>销量：55</span></Card>
            </Popover.Menu>
            <Popover.Menu mode='dark' placement='bottom-start' trigger='click'
            actions={actions} onAction={node=>(
                nav(node.url)
            )}
            style={{margin:'2px'}}>
                {/* <List.Item clickable>套餐一</List.Item> */}
                <Card title='套餐三'className='card'><span style={{fontSize:'small'}}>销量：999+</span></Card>
            </Popover.Menu>
            
          </List>
        </div>
      </div>
    </div>
    </div>
  )
}
