import { data } from './data'

/**
 * 图谱数据源
 */



/**
 * 模糊查询大类
 * @param {*} name 
 */
export const search = (name)=>{
    return new Promise((resolve,reject)=>{
        let result = []
        let list = data.filter(item=>item.name.indexOf(name)>=0)
        if(list&&list.length>0){
            result = list||[]
        }
        if(result.length>0){
            resolve(result)
        }else{
            reject()
        }
    })
}

/**
 * 点击节点展开
 * @param {*} id 
 */
export const expendNodes = (id)=>{
    return new Promise((resolve,reject)=>{
        let totalList = []
        //拆除来所有的children到第一层
        data.forEach(item=>{
            getDeepChildrens(totalList,item,0)
        })

        let list = []
        for(let item of totalList){
            if(item.parentId.toString() === id){
                const {children,...reset} = item
                list.push({
                    ...reset
                })
            }
        }
        if(list.length>0){
            resolve(list)
        }else{
            reject(new Error('节点展开失败'))
        }
    })
}

/**
 * 递归数组,把所有children都拆出来到第一层
 * @param {*} list 
 * @param {*} item 
 */
function getDeepChildrens(list,item,parentId){
    const {children,...reset} =  item
    list.push(
        {
            ...reset,
            parentId,
        }
    )
    if(children&&children.length>0){
        children.forEach(child=>{
            getDeepChildrens(list,child,item.id)
        })
    }
}
