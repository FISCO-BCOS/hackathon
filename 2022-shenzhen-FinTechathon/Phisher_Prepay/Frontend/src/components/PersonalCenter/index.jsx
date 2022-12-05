import React,{useState} from 'react'
import { List,Switch,Avatar, Space,Popup,AutoCenter } from 'antd-mobile'
import QRcode from 'qrcode.react'
import {
  UnorderedListOutline,
  PayCircleOutline,
  SetOutline,
} from 'antd-mobile-icons'

export default function PersonalCenter() {
  const handleClick=()=>{

  }
  // 设置弹出层初始值
  const [visible1,setVisible1]=useState(false)

  return (
    <div>
      {/* 头像 */}
      {/* <Space block direction='vertical'>
          <Avatar src='' />
        </Space> */}

        {/* 剩余个人中心内容 */}
      <List mode='card' header='基本信息'>
        <List.Item extra='xxx'>姓名</List.Item>
        <List.Item extra='1111111'>绑定手机号</List.Item> 
        <List.Item onClick={()=>setVisible1(true)}>我的二维码</List.Item>  
      </List>

      <Popup visible={visible1}
      onMaskClick={()=>{
        setVisible1(false)
      }}
      bodyStyle={{height:'60vh'}}>
        <AutoCenter>
          <QRcode
              value='这是一个二维码'
              size={300}
              fgColor='#000000'
              style={{top:'100px',position:'relative'}}>
          </QRcode>
        </AutoCenter>
      </Popup>

      <List mode='card' header='基础设置'>
        <List.Item extra='按照支付设置的顺序扣款' onClick={handleClick}>
          扣款方式
        </List.Item>
        <List.Item extra='200元' onClick={handleClick}>
          月限额
        </List.Item>
        <List.Item onClick={handleClick}>帮助中心</List.Item>
        <List.Item onClick={handleClick}>关闭服务</List.Item>
      </List>
      <List header="个人偏好">
        <List.Item extra={<Switch defaultChecked />}>新消息通知</List.Item>
        <List.Item extra='未开启' clickable>
          大字号模式
        </List.Item>
        <List.Item description='管理已授权的产品和设备' clickable>
          授权管理
        </List.Item>
        <List.Item title='隐私协议' description='第三方信息共享清单' clickable>
          个人隐私
        </List.Item>
      </List>


    </div>
  )
}
