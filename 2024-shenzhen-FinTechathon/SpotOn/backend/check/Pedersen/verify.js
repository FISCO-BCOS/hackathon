/**
 * File Name: verify.js
 * Project Name: ZeroID
 * Author(s): Norton
 * Creation Date: 2024-02-01
 * Copyright: Copyright (C) 2024 Example Corporation. All rights reserved.
 * License: This code is distributed under the MIT license.
 * Modification History:
 *    - 2024-02-02: Initial version by Norton.
 *    - 2024-02-10: Minor bug fixes by Norton.
 * Description: This is an implementation of Pedersen's Commitment in the multiplicative group, written
 *              in JavaScript language.
 * Contact Information: [None]
 */

import * as fs from 'fs'
import CryptoJS from 'crypto-js'
import { Pedersen } from './Pedersen.js'
import { Homomorphic } from './Homomorphic.js'

// 生物特征编码的维度
const dim = 128
// 承诺的生成元
const g = 56544564n
const h = 237684576n
// 假设所有变量都已定义并赋值
console.log("G: " + g);
console.log("h: " + h);

// 读取数据
try {
    // 同步读取文件内容
    //const data = fs.readFileSync(path.resolve('D:/java/code/check/Pedersen/identity.json'), 'utf8')
    const data_user = fs.readFileSync('identity.json', 'utf8')
    const data_proof = fs.readFileSync('proof.json', 'utf8')
    // 将字符串转换为JSON对象
    var json_user = JSON.parse(data_user)
    var json_proof = JSON.parse(data_proof)
} catch (err) {
    console.error("An error occurred while reading the JSON File.", err)
}

// 开始验证
console.time('time')
let pass = 1
// 获取用户身份信息
const id = json_proof.id
const t = json_proof.time
const cmt_old_json = json_user.cmt//有点问题
const cmt_new_json = json_proof.proof.emb
const cmt_eq_old_json = json_proof.proof.cmt.cmt_eq_old_json
const cmt_eq_new_json = json_proof.proof.cmt.cmt_eq_new_json
const cmt_mult_json = json_proof.proof.cmt.cmt_mult_json
const a_json = json_proof.proof.a
const z_json = json_proof.proof.z
const p_json = json_proof.proof.p



console.log("ID: " + id);
console.log("Time: " + t); // 使用JavaScript变量名
console.log("cmtOldJson: ", cmt_old_json); // 使用JavaScript变量名
console.log("cmtNewJson: ", cmt_new_json); // 使用JavaScript变量名
console.log("cmtEqOldJson: ", cmt_eq_old_json); // 使用JavaScript变量名
console.log("cmtEqNewJson: ", cmt_eq_new_json); // 使用JavaScript变量名
console.log("cmtMultJson: ", cmt_mult_json); // 使用JavaScript变量名
console.log("aJson: ", a_json); // 使用JavaScript变量名
console.log("zJson: ", z_json); // 使用JavaScript变量名
// 打印 z_json 的长度

console.log("pJson: ", p_json); // 使用JavaScript变量名
// 时间有效性判断
var t_end = new Date()
var t_start = new Date(t)
// 打印时间信息
console.log("Start Time: " + t_start);
console.log("End Time: " + t_end);
console.log("Time Difference (ms): " + (t_end - t_start));
if(t_end - t_start <= 3600000000) {
    pass = 1 * pass
}
else {
    pass = 0 * pass
}
// 自行生成挑战e
const challenge = {cmt_old_json, cmt_new_json, cmt_eq_old_json, cmt_eq_new_json, cmt_mult_json, id, t}
// e -> hash

const test = JSON.stringify(challenge)
const hash = CryptoJS.SHA256(test.toString()).toString()
const e = BigInt('0x' + hash) % 5000n
// 打印挑战信息
console.log("Challenge: ", challenge);
console.log("e: " + e);
//console.log(e)
// 数据整理
let cmt_old = new Array(dim)
let cmt_new = new Array(dim)
let cmt_eq_old = new Array(dim)
let cmt_eq_new = new Array(dim)
let cmt_mult = new Array(dim)
let z1 = new Array(dim)
let z2 = new Array(dim)
let z3 = new Array(dim)
let z4 = new Array(dim)
let z5 = new Array(dim)
let z6 = new Array(dim)
let z7 = new Array(dim)
let p1 = new Array(dim)
let p2 = new Array(dim)
let p3 = new Array(dim)
const a1 = BigInt(a_json[0])
const a2 = BigInt(a_json[1])

for(let i = 0; i < dim; i++) {
    cmt_old[i] = BigInt(cmt_old_json[i])
    cmt_new[i] = BigInt(cmt_new_json[i])
    cmt_eq_old[i] = BigInt(cmt_eq_old_json[i])
    cmt_eq_new[i] = BigInt(cmt_eq_new_json[i])
    cmt_mult[i] = BigInt(cmt_mult_json[i])
    z1[i] = BigInt(z_json[i])  
    z2[i] = BigInt(z_json[dim + i])
    z3[i] = BigInt(z_json[2 * dim + i])
    z4[i] = BigInt(z_json[3 * dim + i])
    z5[i] = BigInt(z_json[4 * dim + i])
    z6[i] = BigInt(z_json[5 * dim + i])
    z7[i] = BigInt(z_json[6 * dim + i])
    p1[i] = BigInt(p_json[i])
    p2[i] = BigInt(p_json[dim + i])
    p3[i] = BigInt(p_json[2 * dim + i])
    //console.log(emb_old[i], rand_old[i], cmt_old[i])
}

console.log("a1 :", a1);
console.log("a2: ", a2);

// 打印BigInteger数组
console.log("cmtOld: ", cmt_old);
console.log("cmtNew: ", cmt_new);
console.log("cmtEqOld: ", cmt_eq_old);
console.log("cmtEqNew: ", cmt_eq_new);
console.log("cmtMult: ", cmt_mult);
console.log("z1: ", z1);
console.log("z2: ", z2);
console.log("z3: ", z3);
console.log("z4: ", z4);
console.log("z5: ", z5);
console.log("z6: ", z6);
console.log("z7: ", z7);
console.log("p1: ", p1);
console.log("p2: ", p2);
console.log("p3: ", p3);


// 验证乘法同态的准确性
const code = new Pedersen(g, h)
let d1 = new Array(dim)
let d2 = new Array(dim)
let d3 = new Array(dim)
let d4 = new Array(dim)
let d5 = new Array(dim)
for(let i = 0; i < dim; i++) {
    d1[i] = code.Commitment(z1[i], z2[i])
    let f = new Pedersen(cmt_old[i], h)
    d2[i] = f.Commitment(z1[i], z5[i])
    d3[i] = code.Commitment(z3[i], z4[i])
    let ff = new Pedersen(cmt_new[i], h)
    d4[i] = ff.Commitment(z3[i], z6[i])
    d5[i] = f.Commitment(z3[i], z7[i])
}


// 打印验证结果数组
console.log("d1: ", d1);
console.log("d2: ", d2);
console.log("d3: ", d3);
console.log("d4: ", d4);
console.log("d5: ", d5);

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
//console.log("m->m^2: ", pass == 1)
// 验证用生物特征编码的平方项的准确性验证
for(let i = 0; i < dim; i++) {
    if(d3[i] == hp.Add(a2, hp.Pow(cmt_new[i], e)) && d4[i] == hp.Add(p2[i], hp.Pow(cmt_eq_new[i], e))) {
        pass = pass * 1
    }
    else {
        pass = pass * 0
    }
}
//console.log("m‘->m’^2: ", pass == 1)
// 不同生物特征编码的乘积项的准确性验证
for(let i = 0; i < dim; i++) {
    if(d5[i] == hp.Add(p3[i], hp.Pow(cmt_mult[i], e))) {
        pass = pass * 1
    }
    else {
        pass = pass * 0
    }
}
//console.log("mm‘->mm’^2: ", pass == 1)
// 自行计算生物特征间欧式距离的承诺
let sum_old = 1n
let sum_new = 1n
let sum_mult = 1n
for(let i = 0; i < dim; i++) {
    sum_old = hp.Add(sum_old, cmt_eq_old[i])
    sum_new = hp.Add(sum_new, cmt_eq_new[i])
    sum_mult = hp.Add(sum_mult, cmt_mult[i])
}


// 打印欧式距离的承诺
console.log("sumOld: " + sum_old);
console.log("sumNew: " + sum_new);
console.log("sumMult: " + sum_mult);
//console.log(sum_old, sum_new, sum_mult)
const cmt_sum = hp.Add(hp.Add(sum_old, sum_new), hp.Pow(hp.Add(sum_mult, sum_mult), 67108857))
if(pass == 1) {
    console.log('Verify: Pass')
}
else {
    console.log('Verify: Flase')
}
console.timeEnd('time')
