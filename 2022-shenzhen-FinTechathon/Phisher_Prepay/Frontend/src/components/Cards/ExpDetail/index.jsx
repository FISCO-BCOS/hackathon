import React from 'react'
import { useLocation,useNavigate} from 'react-router-dom'
import { NavBar,List,Button } from 'antd-mobile'
export default function ExpDetail() {
    const nav=useNavigate()
    const {state:{moneyLeft,date,channel,cost}}=useLocation()
    const back=()=>{
        nav('/expRec')
    }
    const handleClick=()=>{
      nav('/appeal')
    }
  return (
    <div>
      <NavBar onBack={back}>消费详情</NavBar>
      <List style={{fontWeight:'bold',fontSize:'xx-large',height:'15vh'}}>
        <List.Item title='消费金额(人民币)' clickable>{cost}</List.Item>
        <List.Item extra='商家一' >对方账号</List.Item>
      </List>
      <List mode='card'>
        <List.Item extra={date}>交易时间</List.Item>
        <List.Item extra={moneyLeft}>账户余额</List.Item>
        <List.Item extra={channel}>交易渠道</List.Item>
      </List>
      <Button block color='success' size='large' style={{margin:'10px'}}
      onClick={handleClick}>对本次服务提出申诉</Button>
    </div>
  )
}
