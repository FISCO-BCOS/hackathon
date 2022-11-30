import React from 'react'
import { List,NavBar } from 'antd-mobile'
import { useNavigate } from 'react-router-dom'

export default function ExpRec() {
    const navigate=useNavigate()
    const back=()=>{
        navigate('/tabs/Cards')
    }
    const handleClick= (prop)=>{
        navigate('/expDetail',{state:{
            moneyLeft:prop.moneyLeft,
            date:prop.date,
            cost:prop.cost,
            channel:prop.channel
        }})
        // console.log(prop)
    }
    const records=[
        {
            month:'2022年7月',
            detail:[
                {
                    id:0,
                    channel:'网上快捷支付',
                    moneyLeft:'100元',
                    date:'2022年7月30日',
                    cost:'-10元',
                },
                {
                    id:1,
                    channel:'网上快捷支付',
                    moneyLeft:'110元',
                    date:'2022年7月20日',
                    cost:'-10元',
                },
                {
                    id:2,
                    channel:'网上快捷支付',
                    moneyLeft:'120元',
                    date:'2022年7月3日',
                    cost:'-10元',
                },
            ]
        },
        {
            month:'2022年6月',
            detail:[
                {
                    id:0,
                    channel:'网上快捷支付',
                    moneyLeft:'100元',
                    date:'2022年6月30日',
                    cost:'-10元',
                },
                {
                    id:1,
                    channel:'网上快捷支付',
                    moneyLeft:'110元',
                    date:'2022年6月20日',
                    cost:'-10元',
                },
                {
                    id:2,
                    channel:'网上快捷支付',
                    moneyLeft:'120元',
                    date:'2022年6月3日',
                    cost:'-10元',
                },
            ]
        },
        {
            month:'2022年5月',
            detail:[
                {
                    id:0,
                    channel:'网上快捷支付',
                    moneyLeft:'100元',
                    date:'2022年5月30日',
                    cost:'-10元',
                },
                {
                    id:1,
                    channel:'网上快捷支付',
                    moneyLeft:'110元',
                    date:'2022年5月20日',
                    cost:'-10元',
                },
                {
                    id:2,
                    channel:'网上快捷支付',
                    moneyLeft:'120元',
                    date:'2022年5月3日',
                    cost:'-10元',
                },
            ]
        },
        {
            month:'2022年4月',
            detail:[
                {
                    id:0,
                    channel:'网上快捷支付',
                    moneyLeft:'100元',
                    date:'2022年4月30日',
                    cost:'-10元',
                },
                {
                    id:1,
                    channel:'网上快捷支付',
                    moneyLeft:'110元',
                    date:'2022年4月20日',
                    cost:'-10元',
                },
                {
                    id:2,
                    channel:'网上快捷支付',
                    moneyLeft:'120元',
                    date:'2022年4月3日',
                    cost:'-10元',
                },
            ]
        },
    ]
  return (
    <div>
        <NavBar onBack={back}>消费记录</NavBar>
        {
            records.map(item=>(
                <List key={item.month}header={item.month} style={{fontSize:'xx-large',fontWeight:'bold'}}>
                    {
                        item.detail.map(i=>(
                            <List.Item key={i.id} title={i.channel}
                            description={i.date} extra={i.cost}
                            onClick={()=>handleClick(i)}
                            >{i.moneyLeft}
                            </List.Item>
                        ))
                    }
                </List>
            ))
        }
    </div>
  )
}
