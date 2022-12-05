import React, { useState } from 'react'
import { NavLink,useRoutes } from 'react-router-dom'
import { Form, Input,Button,AutoCenter } from 'antd-mobile'
// import { DemoBlock } from 'demos'
import styles from './demo2.less'
import { EyeInvisibleOutline, EyeOutline,SmileOutline } from 'antd-mobile-icons'


export default (props) => {
  const [visible, setVisible] = useState(false);
  // const element=useRoutes(routes)

  return (
      <div>
        <Form layout='horizontal' footer={
          <NavLink to="/tabs/Shops" style={{textDecoration:'none'}}>
            <Button block color='warning' size='large' shape='rounded' type='submit'>
              <p className='text'>Log In</p>
            </Button>
          </NavLink>  
        }>
          <AutoCenter>
          <div className='title'>
          {/* <SmileOutline /> */}
            <p>Welcome to PrePay</p>
          </div>
          </AutoCenter>
          
          <Form.Header>用户登录界面</Form.Header>
          <Form.Item label='用户名' name='username'>
            <Input placeholder='请输入用户名' clearable />
          </Form.Item>
          <Form.Item
            label='密码'
            name='password'
            extra={
              <div className="eye">
                {!visible ? (
                  <EyeInvisibleOutline onClick={() => setVisible(true)} />
                ) : (
                  <EyeOutline onClick={() => setVisible(false)} />
                )}
              </div>
            }
          >
            <Input
              placeholder='请输入密码'
              clearable
              type={visible ? 'text' : 'password'}
            />
          </Form.Item>
        </Form>
      </div>
        
  )
}