import React from 'react'
import { NavBar,List,Form,TextArea, Button,Dialog,Toast } from 'antd-mobile'
import { useNavigate } from 'react-router-dom'

export default function Appeal() {
    const nav=useNavigate()
    const back=()=>{
        nav('/expRec')
    }
    const onFinish= async ()=>{
        const result = await Dialog.confirm({
            content: '是否确认提交?',
          })
          if (result) {
            Toast.show({ content: '提交成功,我们会尽快处理您的申诉！', position: 'bottom' })
            back()
          } else {
            Toast.show({ content: '申诉失败，请稍后再试', position: 'bottom' })
          }

    }
  return (
    <div>
      <NavBar onBack={back}>申诉页面</NavBar>
      <List header='卡片信息' mode='card'>
        <List.Item extra='xxx'>持卡人姓名</List.Item>
        <List.Item extra='商家一'>商家</List.Item>
        <List.Item extra='2022年12月1日'>该笔消费时间</List.Item>
        <List.Item extra='10元'>该笔消费金额</List.Item>
        <List.Item extra='网上快捷支付'>消费渠道</List.Item>
      </List>
      <Form footer={
        <Button block type='submit' color='danger' size='large'>提交</Button>
      }
      onFinish={onFinish}>
        <Form.Header>申诉理由</Form.Header>
        <Form.Item name='reason' >
            <TextArea 
            placeholder='请留下您的宝贵意见'
            autoSize={{minRows:3,maxRows:5}}/>
        </Form.Item>
      </Form>
    </div>
  )
}
