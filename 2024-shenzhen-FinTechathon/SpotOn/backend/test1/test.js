// 引入第三方库
import CryptoJS from 'crypto-js'
import { Pedersen } from './Pedersen.js'
import { Homomorphic } from './Homomorphic.js'

// // 同态减法的验证
// const g = 56544564
// const h = 237684576
// let a = new Pedersen(g, h)
// const test1 = a.Commitment(800, 1000)
// const test2 = a.Commitment(400, 600)
// const test3 = a.Commitment(400, 400)
// let b = new Homomorphic()
// console.log(b.VerifySub(test1, test2, test3) == 1)
// // 测试逆元
// const test2_in = b.Pow(test2, 67108857)
// const test3_in = b.Add(test1, test2_in)
// console.log(test3_in == test3)

// ZeroID

// 生物特征编码的维度
const dim = 512
// 承诺的生成元
const g = 56544564n
const h = 237684576n
// 随机生成注册生物特征与认证生物特征向量
let emb_old = new Array(dim)
let emb_new = new Array(dim)
let rand_old = new Array(dim)
let rand_new = new Array(dim)
for(let i = 0; i < dim; i++) {
    emb_old[i] = BigInt(Math.floor(Math.random() * 1000))
    emb_new[i] = BigInt(Math.floor(Math.random() * 1000))
    rand_old[i] = BigInt(Math.floor(Math.random() * 1000))
    rand_new[i] = BigInt(Math.floor(Math.random() * 10000))
    //console.log(emb_old[i], emb_new[i], rand_old[i], rand_new[i])
}
// 初始化注册生物特征承诺
console.time('time11')
let a = new Pedersen(g, h)
console.timeEnd('time11')
let cmt_old = new Array(dim)
for(let i = 0; i < dim; i++) {
    cmt_old[i] = a.Commitment(emb_old[i], rand_old[i])
    //console.log(cmt_old[i])
}

// 生成身份认证所需要的证明
console.time('time')
// 生成认证用生物特征承诺
let b = new Pedersen(g, h)
let cmt_new = new Array(dim)
for(let i = 0; i < dim; i++) {
    cmt_new[i] = b.Commitment(emb_new[i], rand_new[i])
    //console.log(cmt_new[i])
}
// 生成前后生物特征元素的平方的承诺
let emb_eq_old = new Array(dim)
let emb_eq_new = new Array(dim)
let rand_eq_old = new Array(dim)
let rand_eq_new = new Array(dim)
let cmt_eq_old = new Array(dim)
let cmt_eq_new = new Array(dim)
let emb_mult = new Array(dim)
let rand_mult = new Array(dim)
let cmt_mult = new Array(dim)
for(let i = 0; i < dim; i++) {
    emb_eq_old[i] = emb_old[i] * emb_old[i]
    emb_eq_new[i] = emb_new[i] * emb_new[i]
    emb_mult[i] = emb_old[i] * emb_new[i]
    rand_eq_old[i] = emb_old[i] * rand_old[i] + BigInt(Math.floor(Math.random() * 1000000))
    rand_eq_new[i] = emb_new[i] * rand_new[i] + BigInt(Math.floor(Math.random() * 1000000))
    rand_mult[i] = emb_new[i] * rand_old[i] + BigInt(Math.floor(Math.random() * 1000))
    console.log(emb_eq_old[i], emb_eq_new[i], emb_mult[i], rand_eq_old[i], rand_eq_new[i], rand_mult[i])
    cmt_eq_old[i] = b.Commitment(emb_eq_old[i], rand_eq_old[i])
    cmt_eq_new[i] = b.Commitment(emb_eq_new[i], rand_eq_new[i])
    cmt_mult[i] = b.Commitment(emb_mult[i], rand_mult[i])
    //console.log(cmt_eq_old[i], cmt_eq_new[i])
}
// 构造乘法同态的验证因子
const b1 = BigInt(Math.floor(Math.random() * 10000))
const b2 = BigInt(Math.floor(Math.random() * 10000))
const b3 = BigInt(Math.floor(Math.random() * 10000))
const b4 = BigInt(Math.floor(Math.random() * 10000))
const b5 = BigInt(Math.floor(Math.random() * 10000))
const b6 = BigInt(Math.floor(Math.random() * 10000))
const b7 = BigInt(Math.floor(Math.random() * 10000))
const a1 = b.Commitment(b1, b2)
const a2 = b.Commitment(b3, b4)
let p1 = new Array(dim)
let p2 = new Array(dim)
let p3 = new Array(dim)
for(let i = 0; i < dim; i++) {
    let c = new Pedersen(cmt_old[i], h)
    p1[i] = c.Commitment(b1, b5)
    let d = new Pedersen(cmt_new[i], h)
    p2[i] = d.Commitment(b3, b6)
    p3[i] = c.Commitment(b3, b7)
    //console.log(p1[i], p2[i], p3[i])
}
// 生成挑战因子
const e = 1464n
//const msg = JSON.stringify(cmt_old, cmt_new, cmt_eq_old, cmt_eq_new, cmt_mult)
// e -> hash
//const hash = CryptoJS.SHA256(msg).toString()
//console.log(hash)
// 生成其余辅助证明因子
let z1 = new Array(dim)
let z2 = new Array(dim)
let z3 = new Array(dim)
let z4 = new Array(dim)
let z5 = new Array(dim)
let z6 = new Array(dim)
let z7 = new Array(dim)
for(let i = 0; i < dim; i++) {
    z1[i] = b1 + e * emb_old[i]
    z2[i] = b2 + e * rand_old[i]
    z3[i] = b3 + e * emb_new[i]
    z4[i] = b4 + e * rand_new[i]
    z5[i] = b5 + e * (rand_eq_old[i] - emb_old[i] * rand_old[i])
    z6[i] = b6 + e * (rand_eq_new[i] - emb_new[i] * rand_new[i])
    z7[i] = b7 + e * (rand_mult[i] - emb_new[i] * rand_old[i])
    console.log(z1[i], z2[i], z3[i], z4[i], z5[i], z6[i], z7[i])
}
// 计算生物特征编码间的欧式距离
let emb_dist = 0n
let rand_dist = 0n
for(let i = 0; i < dim; i++) {
    emb_dist = emb_dist + (emb_new[i] - emb_old[i]) * (emb_new[i] - emb_old[i])
    rand_dist = rand_dist + (rand_eq_new[i] + rand_eq_old[i] - 2n * rand_mult[i])
}
console.log(emb_dist, rand_dist)
const cmt_dist = b.Commitment(emb_dist, rand_dist)
// 打包需要使用的证明
var proof = {
    "g": g,
    "h": h,
    "p": 67108859,
    "proof": {
        "facecode": cmt_new,
        "commitment": {cmt_eq_old, cmt_eq_new, cmt_mult},
        "a": {a1, a2},
        "z": {z1, z2, z3, z4, z5, z6, z7}
    }
}
//var json = JSON.stringify(proof)
//console.log(json)
console.timeEnd('time')


// 开始验证
console.time('verify')
let pass = 1
// 自行生成挑战e

// 验证乘法同态的准确性
let d1 = new Array(dim)
let d2 = new Array(dim)
let d3 = new Array(dim)
let d4 = new Array(dim)
let d5 = new Array(dim)
for(let i = 0; i < dim; i++) {
    d1[i] = b.Commitment(z1[i], z2[i])
    let f = new Pedersen(cmt_old[i], h)
    d2[i] = f.Commitment(z1[i], z5[i])
    d3[i] = b.Commitment(z3[i], z4[i])
    let ff = new Pedersen(cmt_new[i], h)
    d4[i] = ff.Commitment(z3[i], z6[i])
    d5[i] = f.Commitment(z3[i], z7[i])
}
// 注册用生物特征编码的平方项的准确性验证
const hp = new Homomorphic()
for(let i = 0; i < dim; i++) {
    if(d1[i] == hp.Add(a1, hp.Pow(cmt_old[i], e)) && d2[i] == hp.Add(p1[i], hp.Pow(cmt_eq_old[i], e))) {
        pass = pass * 1
    }
    else {
        pass = pass * 0
    }
}
console.log("m->m^2: ", pass == 1)
// 验证用生物特征编码的平方项的准确性验证
for(let i = 0; i < dim; i++) {
    if(d3[i] == hp.Add(a2, hp.Pow(cmt_new[i], e)) && d4[i] == hp.Add(p2[i], hp.Pow(cmt_eq_new[i], e))) {
        pass = pass * 1
    }
    else {
        pass = pass * 0
    }
}
console.log("m‘->m’^2: ", pass == 1)
// 不同生物特征编码的乘积项的准确性验证
for(let i = 0; i < dim; i++) {
    if(d5[i] == hp.Add(p3[i], hp.Pow(cmt_mult[i], e))) {
        pass = pass * 1
    }
    else {
        pass = pass * 0
    }
}
console.log("mm‘->mm’^2: ", pass == 1)
// 自行计算生物特征间欧式距离的承诺
let sum_old = 1n
let sum_new = 1n
let sum_mult = 1n
for(let i = 0; i < dim; i++) {
    sum_old = hp.Add(sum_old, cmt_eq_old[i])
    sum_new = hp.Add(sum_new, cmt_eq_new[i])
    sum_mult = hp.Add(sum_mult, cmt_mult[i])
}
console.log(sum_old, sum_new, sum_mult)
const cmt_sum = hp.Add(hp.Add(sum_old, sum_new), hp.Pow(hp.Add(sum_mult, sum_mult), 67108857))
if(cmt_dist == cmt_sum) {
    console.log('Verify: Pass')
}
else {
    console.log('Verify: Flase')
}
// for(let i = 0; i < dim; i++) {
//     if(flag128[i] == 1) {
//         pass = pass * b.VerifySub(emb[i], emb_new[i], emb_dif[i])
//         //console.log(pass)
//     }
//     else {
//         pass = pass * b.VerifySub(emb_new[i], emb[i], emb_dif[i])
//         //console.log(pass)
//     }
// }
// // 验证欧式距离的承诺
// let cmt_sum = 1
// for(let i = 0; i < 128; i++) {
//     cmt_sum = cmt_sum * emb_sq[i] % 67108859 // p
// }
// if(pass == 1) {
//     console.log('Verify: Pass')
// }
// else {
//     console.log('Verify: Flase')
// }
// console.log(cmt_sum)
console.timeEnd('verify')
