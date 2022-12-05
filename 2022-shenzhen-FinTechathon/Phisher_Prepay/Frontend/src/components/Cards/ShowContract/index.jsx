import React , { useState } from 'react'
import {AutoCenter, Form, Input, NavBar, Checkbox, Space,DatePicker, List, TextArea,Button, Toast } from 'antd-mobile'
import { useLocation, useNavigate } from 'react-router-dom'
import dayjs from 'dayjs'
import axios from 'axios'
const now=new Date()
export default function ShowContract(props) {
    const nav=useNavigate()
    const Props=useLocation()
    // console.log(Props.state.cardID)
    const cid=Props.state.cardID
    const userN=Props.state.userName
    const shopN=Props.state.shopName
    const bal=Props.state.balance
    // console.log(typeof(cid))
    var formData=new FormData()
    let msg={
      'cardID':cid
    }
    for(const key in msg){
      formData.append(key,msg[key])
    }
    // axios.post('http://1.12.238.107:8080/card/one',{
    //   cardId:cid
    // }).then(response=>{
    //   console.log(response)
    // }).catch(error=>{
    //   console.log(error)
    // })
    // axios({
    //   method:'get',
    //   url:'http://1.12.238.107:8080/card/one',
    //   data:formData
    // }).then(response=>{
    //   console.log(response)
    // }).catch(error=>{
    //   console.log(error)
    // })
    axios.get(`http://1.12.238.107:8080/card/one?cardId=${cid}`)
    .then(response=>{
      console.log(response)
    }).catch(error=>{
      console.log(error)
    })
    
    const back=()=>{
        nav('/tabs/Cards',{state:{
          balance:bal,
          userName:userN,
          shopName:shopN,
          cardID:cid
        }})
    }
    // const handleClick=()=>{
    //   Toast.show('提交成功，等待商家确认合同！')
    //   back()
    // }
  return (
    <div>
      <NavBar onBack={back} 
      style={{'--border-bottom':'1px #eee solid'}}>合同详情</NavBar>
      <AutoCenter><span style={{fontSize:'x-large',fontWeight:'bold',margin:'5px'}}>行业预付式合同</span></AutoCenter>
      <Form requiredMarkStyle='asterisk' >
        <Form.Header>基本信息</Form.Header>
        <Form.Item name='userName' label='甲方(消费者)姓名' rules={[{required:true}]}>
            <span>{userN}</span>
        </Form.Item>
        <Form.Item name='userName' label='甲方(消费者)联系方式' rules={[{required:true}]}>
            <span>1545451</span>
        </Form.Item>
        <Form.Item name='userName' label='乙方(商家)姓名' rules={[{required:true}]}>
            <span>{shopN}</span>
        </Form.Item>
        <Form.Item name='userName' label='乙方(商家)联系方式' rules={[{required:true}]}>
            <span>5456441</span>
        </Form.Item>
        <Form.Item>
            <span>根据《中华人民共和国民法典》《中华人民共和国消费者权益保护法》及其他相关法律、法规的规定，就预付费式消费事宜，甲乙双方在平等、自愿、公平、诚实信用的基础上，经协商一致，达成如下协议：</span>
        </Form.Item>
        <Form.Header>第一条 预付式消费服务</Form.Header>
        <Form.Item name='type' label='种类'rules={[{required:true}]}>
                <Checkbox defaultChecked value='1'>时效卡</Checkbox>
                <Checkbox value='2'>计次卡</Checkbox>
        </Form.Item>
        <Form.Item  name='useScale' label='使用范围'rules={[{required:true}]}>
            <Input defaultValue='甲方及甲方授权的人可使用'readOnly/>
        </Form.Item>
        <Form.Item name='beginTime'label='开始时间'>
          <span>2022年7月3日</span>
        </Form.Item>
        <Form.Item name='endTime'label='结束时间'>
          <span>2022年12月3日</span>
        </Form.Item>
        <Form.Item name='shopScale'label='使用门店范围'rules={[{required:true}]}>
            <Checkbox value='1'>限本地使用</Checkbox>
            <Checkbox defaultChecked value='2'>各连锁门店可用</Checkbox>
        </Form.Item>
        <Form.Item name='services'label='乙方承诺提供的服务'rules={[{required:true}]}>
            <span>全套健身课程</span>
        </Form.Item>
        <Form.Header>第二条 会籍卡金额及支付金额</Form.Header>
        <Form.Item name='bookMoney'label='卡面金额'rules={[{required:true}]}>
            <span>{bal}元</span>
        </Form.Item>
        <Form.Item name='discount' label='优惠内容'rules={[{required:true}]}>
            <span>8折</span>
        </Form.Item>
        <Form.Item name='actualCost'label='实际收费'rules={[{required:true}]}>
            <span>1600元</span>
        </Form.Item>
        <Form.Header>第三条 重要事项及告知方式</Form.Header>
            <Form.Item>
                    <span style={{fontSize:'large'}}>乙方若要关店终止经营活动，搬离原营业地址、调整营业时间、或经营主体变更，乙方需提前三十日在链上上传相关信息，链上以公告的形式通知消费者。</span>
            </Form.Item>
            <Form.Header>第四条 双方权利和义务</Form.Header>
            <Form.Item>
                <List>
                    <List.Item>
                    1.甲方有权在办卡及购买卡前向乙方详细了其种类、费用、使用权限、包含项目等相关内容。
                    </List.Item>
                    <List.Item>2.甲方应当遵守乙方有关安全管理的规章制度，不得实施危害社会秩序、公共安全和他人健康的行为。</List.Item>
                    <List.Item>3.甲方应当按照乙方指导或提示使用器械、设施及健身，对器械、设施有疑问或身体不适时应当立即停止健身并向乙方求助。</List.Item>
                    <List.Item>4.乙方应当为甲方提供合格的设施、器材。国家实行强制性体育服务标准的体育运动项目的设施、器材，应当符合国家标准或行业标准，并在显著位置设置相应的使用说明和警示标志。</List.Item>
                    <List.Item>5.乙方应在提供服务的场所的醒目位置公示营业执照、教练员信息、收费项目、收费标准、培训事项、退费办法、经营时间和服务承诺等信息。</List.Item>
                    <List.Item>6.本合同履行期限内,乙方需提供与其承诺相符的服务，不得在公示的项目和标准外向甲方收取其他费用,不得单方提高承诺的服务价格或增加服务限制条件。</List.Item>
                    <List.Item>7.对国家实行强制性体育服务标准的体育运动项目，乙方应当按照国家法律法规及标准的相关要求配备具有相应资质的从业人员和服务指导人员，向甲方告知或提示参加体育健身项目、使用器材或设施可能引发的风险及后果，配备符合规定的救护救生设备及救护人员。</List.Item>
                    <List.Item>8.合同中不得出现“最终解释权归经营者”等严重侵犯甲方权益的条款</List.Item>
                </List>
            </Form.Item>
            <Form.Header>第五条 合同解除及争议解决</Form.Header>
            <Form.Item>
              <span>本合同在履行过程中发生争议，双方可选择链下协商解决，若链下协商不成功，一方在链上提交相应证明以及原因申请解约。</span>
            </Form.Item>
            <Form.Item label='一、七天冷静期'>
              <span>
              甲方自签署本合同的次日起7日为“冷静期”，甲方在七天内不论有没有使用卡，都有权无理由解除合同，乙方退回卡内所有剩余余额。
              </span>
            </Form.Item>
            <Form.Item label='二、因乙方原因导致的解约解除'>
              <List header='1.因乙方关闭、转让、合并、搬迁原因变更会籍卡的使用。'>
                <List.Item>
                甲方可以解除合同，并要求乙方退还剩余余额和支付违约费，甲方将在链上提交退卡申请后的五日内收到卡内剩余余额，合同失效。
                </List.Item>
                <List.Item>
                若消费者不申请退款，可提交继续使用服务的申请，商家则需继续向持有预付凭证的消费者继续提供服务，且不得在合同中新设立其他条件。链上自动将消费者的权益转移到搬离的商家、或变更主体的商家处。若转移、或变更主体后的商家没有预付卡业务，则只能退款，合同失效。
                </List.Item>
                <List header='2.因（1）乙方未遵准合同约定的内容、提供的服务质量不达标、或改变服务内容；'>
                  <List.Item>
                  （2）未经甲方同意，擅自将本合同约定的服务转给其他服务方
                  </List.Item>
                  <List.Item>
                  （3）被吊销营业执照、被责令停业整顿、被纳入体育市场黑名单等，严重侵害甲方利益的。
                  </List.Item>
                  <List.Item>
                  甲方有权解除合同，并要求乙方退还剩余余额和支付违约费，甲方将在链上提交退卡申请后的五日内收到卡内剩余余额，合同失效。
                  </List.Item>
                </List>
              </List>
            </Form.Item>
            <Form.Item label='三．因甲方原因导致的解约'>
              <List>
                <List.Item>
                甲方因重大疾病、伤残等身体健康因素，或因居住地变化等客观因素所导致的解约：甲方需在链上提交相关证明，经核实后可以解除合合同，甲方不需支付违约费，甲方收到预付费余额，合同失效。
                </List.Item>
                <List.Item>
                因甲方自身主观因素导致的解约：（1）无商家过错，甲方想解约；或（2）甲方隐瞒患有严重危及自己或他人安全、健康疾病的；或（3）甲方严重违反乙方规章制度，经劝阻拒不改正的；或（3）甲方严重违反国家法律法规、危害社会秩序、公共安全和他人健康行为的。
                <br/>
                乙方有权解除合同，并在链上提供相关证明，经核实后，乙方将在五日内收到违约费，而甲方将会收到退款(卡内剩余余额扣除违约费)。
                </List.Item>
              </List>
            </Form.Item>
            <Form.Header>退还余额计算方法</Form.Header>
            <Form.Item>
              <List>
                <List.Item>
                (1)	违约费：卡内剩余余额的20%
                </List.Item>
                <List.Item>
                (2) 超过七天冷静期后退卡，退卡只退剩余的余额*80%*购卡的优惠率(购卡优惠率为购卡费除以购卡费与赠送金额的和)
                </List.Item>
              </List>
            </Form.Item>
            <Form.Item>
              <span style={{color:'red',fontSize:'large',fontWeight:'bold'}}>购卡有风险，消费者须知：甲方每完成一次服务，合同是按杠杆方案给乙方转移资金。所以如遇乙方违约，且乙方已取走除保证金外的资金，此时甲方只能收到预付卡实付金额的40%的作为赔偿。</span>
            </Form.Item>
            <Form.Header>第六条 其他约定</Form.Header>
            <Form.Item name='otherItems'rules={[{required:true}]}>
              <span>无</span>
            </Form.Item>
            <Form.Header>第七条 附则</Form.Header>
            <Form.Item>
              <List>
                <List.Item>
                1、双方可就本合同未尽事宜另行签订补充协议，补充协议与本合同具有同等法律效力。
                </List.Item>
                <List.Item>
                2、合同自双方签字之日起生效。
                </List.Item>
              </List>
            </Form.Item>
            <Form.Item>
            <span style={{fontSize:'x-large',fontWeight:'bold'}}>请甲乙双方仔细阅读本合同条款，经确认了解无误后，签署并交易。</span>
            </Form.Item>
            <Form.Item name='consumerSign'label='甲方签名' rules={[{required:true}]}>
              <span>yyy</span>
            </Form.Item>
            <Form.Item name='shopSign'label='乙方签名'rules={[{required:true}]}>
              <span>xxx</span>
            </Form.Item>
            <Form.Item name='signDate'label='签约时间'rules={[{required:true}]}>
              <span>2022年7月3日</span>
            </Form.Item>
      </Form>

    </div>
  )
}

function RenderChildrenDemo() {
  const [visible, setVisible] = useState(false)
  return (
    <Space align='center'>
      <Button
        onClick={() => {
          setVisible(true)
        }}
      >
        选择
      </Button>
      <DatePicker
        visible={visible}
        onClose={() => {
          setVisible(false)
        }}
        defaultValue={now}
        max={now}
      >
        {value => value?.toDateString()}
      </DatePicker>
    </Space>
  )
}