import { Image, List,NoticeBar, Space } from 'antd-mobile'
import React from 'react'
import { users } from './users'
import "./demo2.less"

export default function Messages(){
  const handleClick=()=>{

  }
  return (
    <>
      <List header='消息' className='list'>
      <NoticeBar
      extra={
        <Space style={{'--gap':'12px'}}>
          <span>查看详情</span>
          <span>关闭</span>
        </Space>
      }
      content={'商家一服务接近到期！'}
      color='info'>
      </NoticeBar>
      {users.map(user => (
        <List.Item
          key={user.name}
          prefix={
            <Image
              src={user.avatar}
              style={{ borderRadius: 20 }}
              fit='cover'
              width={40}
              height={40}
            />
          }
          description={user.description}
          className="listItem"
          onClick={handleClick}
          arrow={false}
        >
          {user.name}
        </List.Item>
      ))}
    </List>
    </>
    
  )
}
