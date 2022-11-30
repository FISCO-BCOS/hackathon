import React from 'react'
import { NavBar,List,Form,TextArea, Button,Dialog,Toast } from 'antd-mobile'
import { useLocation, useNavigate } from 'react-router-dom'
import axios from 'axios'
import { useEffect } from 'react'
export default function CheckOut() {
    const nav=useNavigate()
    const Props=useLocation()
    // console.log(Props.state)
    const cid=Props.state.cardID
   var obj={0:1}
    //先把该卡的内容查询展示出来
    useEffect(()=>{
      async function FetchData(){
        await axios.get(`http://1.12.238.107:8080/card/one?cardId=${cid}`)
        .then(response=>{
          // console.log(response.data)
          // var obj1=response.data
          Object.assign(obj,response.data)
          // console.log(obj)
        }).catch(error=>{
          console.log(error)
        })
      }
      FetchData()
    })
    //然后再删除卡片
    const back=()=>{
        nav('/tabs/Cards')
    }
    const onFinish= async ()=>{
        const result = await Dialog.confirm({
            content: '是否确认退卡?',
          })
          if (result) {
            axios.get(`http://1.12.238.107:8080/card/delete?cardId=${cid}`)
            .then(response=>{
              console.log(response)
            }).catch(error=>{
              console.log(error)
            })
            Toast.show({ content: '退卡成功', position: 'bottom' })
            back()
          } else {
            Toast.show({ content: '退卡失败，原因：...', position: 'bottom' })
          }

    }
    // console.log(obj.value)
  return (
    <div>
      <NavBar onBack={back}>退卡页面</NavBar>
      <List header='卡片信息' mode='card'>
        <List.Item extra='gt1'>持卡人姓名</List.Item>
        <List.Item extra='商家一'>商家姓名</List.Item>
        <List.Item extra='2022年12月1日'>卡到期时间</List.Item>
        <List.Item extra='200元'>卡余额</List.Item>
      </List>
      <Form footer={
        <Button block type='submit' color='warning' size='large'>退卡</Button>
      }
      onFinish={onFinish}>
        <Form.Header>退卡理由</Form.Header>
        <Form.Item name='reason' >
            <TextArea 
            placeholder='请留下您的宝贵意见'
            autoSize={{minRows:3,maxRows:5}}/>
        </Form.Item>
      </Form>
    </div>
  )
}
