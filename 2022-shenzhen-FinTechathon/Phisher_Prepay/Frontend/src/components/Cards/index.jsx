import axios from 'axios'
import React, { useEffect } from 'react'
// import { useLocation } from 'react-router-dom'
import CardItem from './CardItem'

export default function Cards() {
  var cards=[
    {
      id:'89aeff9aade64547a625d461e9f75f45',
      name:'gt1',
      shopName:"Tao Gao",
      balance:2000
    },
    {
      id:'944725e5ed2a49ccadd150a15f7fbdb4',
      name:'gt2',
      shopName:'Tao Gao',
      balance: 1111
    },
  ]
  var cardList=[];
  useEffect(()=>{
   async function fetchData(){
      await axios.get('http://1.12.238.107:8080/card/list',{})
    .then(response=>{
      // console.log(response)
      const balance=response.data[3];
      const userName=response.data[1];
      const shopName=response.data[2];
      const cardID=response.data[0];
      for (let i=0;i<cardID.length;i++){
        const cardI=[{
          id:cardID[i],
          name:userName[i],
          shopName:shopName[i],
          balance:balance[i]
        }]
        cardList.push(cardI)
      };
    }).catch(error=>{
      console.log(error)
    })
    };
    fetchData()
  })
  // const Props=useLocation()
  // console.log(Props.state)
  // const balance =Props.state.balance
  // const userName =Props.state.userName
  // const shopName =Props.state.shopName
  // const cardID =Props.state.cardID
  // console.log(cardID)
  // 将4个数组的内容重新组合成每张卡片的内容

  // console.log(cardList)
  // componentWillMount(){
  //   console.log('componentWillMount')
  // }
  // console.log('新数据',cardList)
  // console.log('旧数据',cards)
  return (
    <div style={{top:'-0px',position:'relative'}}>
      {
        cards.map(item =>(
          // console.log("item",item)
          <CardItem key={item.id} cardID={item.id} userName={item.name} shopName={item.shopName} bal={item.balance}/>
        ))
      }
    </div>
  )
}
