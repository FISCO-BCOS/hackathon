/**
 * File Name: prove.js
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
import * as path from 'path'  // 添加这一行

function InttoHex(Value, length) {
    // 转换为十六进制，不包括'0x'前缀
    let hex = Value.toString(16);
    // 计算需要填充的零的数量
    const padding = length - hex.length;
    // 如果需要，填充零
    if (padding > 0) {
        hex = '0x' + '0'.repeat(padding) + hex;
    }
    else {
        hex = '0x' + hex;
    }
    return hex;
}

// 生物特征编码的维度
const dim = 2
// 承诺的生成元
const g = 56544564n
const h = 237684576n
let json;  // 在 try 块外定义 json 变量
// 读取用户身份信息
try {
    // 使用绝对路径读取文件内容
const data = fs.readFileSync('identity.json', 'utf8')
// 将字符串转换为JSON对象
 
json = JSON.parse(data);  // 添加这一行
} catch (err) {
    console.error("An error occurred while reading the JSON File.", err)
}
// 获取用户身份信息
const id = json.id
console.log("id: " + id)
let emb_old = new Array(dim)
let rand_old = new Array(dim)
let cmt_old = new Array(dim)
for(let i = 0; i < dim; i++) {
    emb_old[i] = BigInt(json.m[i])
    rand_old[i] = BigInt(json.r[i])
    cmt_old[i] = BigInt(json.cmt[i])
    //console.log(emb_old[i], rand_old[i], cmt_old[i])
}


// 打印数组
console.log("emb_old: " + emb_old.toString());
console.log("old_cmt: " + cmt_old.toString());
console.log("rand_old: " + rand_old.toString());



console.time('time')
// 随机生成认证生物特征向量
const code = new Pedersen(g, h)
let emb_new = new Array(dim)
let rand_new = new Array(dim)
let cmt_new = new Array(dim)
for(let i = 0; i < dim; i++) {
    emb_new[i] = BigInt(Math.floor(Math.random() * 1000))
    rand_new[i] = BigInt(Math.floor(Math.random() * 10000))
    cmt_new[i] = code.Commitment(emb_new[i], rand_new[i])
    //console.log(emb_new[i], rand_new[i], cmt_new[i])
}


// 打印数组
console.log("emb_new: " + emb_new.toString())
console.log("rand_new: " + rand_new.toString())
console.log("cmt_new: " + cmt_new.toString())

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
    // 生成随机数
    const randomEqOld = Math.floor(Math.random() * 1000000);
    const randomEqNew = Math.floor(Math.random() * 1000000);
    const randomMult = Math.floor(Math.random() * 1000);

    // 打印随机数
    console.log("Random for rand_eq_old[" + i + "]: " + randomEqOld);
    console.log("Random for rand_eq_new[" + i + "]: " + randomEqNew);
    console.log("Random for rand_mult[" + i + "]: " + randomMult);

    // 进行运算
    rand_eq_old[i] = emb_old[i] * rand_old[i] + BigInt(randomEqOld);
    rand_eq_new[i] = emb_new[i] * rand_new[i] + BigInt(randomEqNew);
    rand_mult[i] = emb_new[i] * rand_old[i] + BigInt(randomMult);
    //console.log(emb_eq_old[i], emb_eq_new[i], emb_mult[i], rand_eq_old[i], rand_eq_new[i], rand_mult[i])
    cmt_eq_old[i] = code.Commitment(emb_eq_old[i], rand_eq_old[i])
    cmt_eq_new[i] = code.Commitment(emb_eq_new[i], rand_eq_new[i])
    cmt_mult[i] = code.Commitment(emb_mult[i], rand_mult[i])
    //console.log(cmt_eq_old[i], cmt_eq_new[i])
}


// 打印所有数组的内容
console.log("emb_eq_old: " + emb_eq_old.toString());
console.log("emb_eq_new: " + emb_eq_new.toString());
console.log("emb_mult: " + emb_mult.toString());
console.log("rand_eq_old: " + rand_eq_old.toString());
console.log("rand_eq_new: " + rand_eq_new.toString());
console.log("rand_mult: " + rand_mult.toString());
console.log("cmt_eq_old: " + cmt_eq_old.toString());
console.log("cmt_eq_new: " + cmt_eq_new.toString());
console.log("cmt_mult: " + cmt_mult.toString());

// 构造乘法同态的验证因子
const b1 = BigInt(Math.floor(Math.random() * 10000))
const b2 = BigInt(Math.floor(Math.random() * 10000))
const b3 = BigInt(Math.floor(Math.random() * 10000))
const b4 = BigInt(Math.floor(Math.random() * 10000))
const b5 = BigInt(Math.floor(Math.random() * 10000))
const b6 = BigInt(Math.floor(Math.random() * 10000))
const b7 = BigInt(Math.floor(Math.random() * 10000))
const a1 = code.Commitment(b1, b2)
const a2 = code.Commitment(b3, b4)



// 打印验证因子
console.log("b1: ", b1);
console.log("b2: ", b2);
console.log("b3: ", b3);
console.log("b4: ", b4);
console.log("b5: ", b5);
console.log("b6: ", b6);
console.log("b7: ", b7);
console.log("a1: ", a1);
console.log("a2: ", a2);

let p1 = new Array(dim)
let p2 = new Array(dim)
let p3 = new Array(dim)
for(let i = 0; i < dim; i++) {
    let temp_eq_old = new Pedersen(cmt_old[i], h)
    p1[i] = temp_eq_old.Commitment(b1, b5)
    let temp_eq_new = new Pedersen(cmt_new[i], h)
    p2[i] = temp_eq_new.Commitment(b3, b6)
    p3[i] = temp_eq_old.Commitment(b3, b7)
    //console.log(p1[i], p2[i], p3[i])
}


// 打印数组
console.log("p1: " + p1.toString());
console.log("p2: " + p2.toString());
console.log("p3: " + p3.toString());
// 生成挑战因子
let cmt_old_json = Array(dim)
let cmt_new_json = Array(dim)
let cmt_eq_old_json = Array(dim)
let cmt_eq_new_json = Array(dim)
let cmt_mult_json = Array(dim)
for(let i = 0; i < dim; i++) {
    cmt_old_json[i] = InttoHex(cmt_old[i], 16)
    cmt_new_json[i] = InttoHex(cmt_new[i], 16)
    cmt_eq_old_json[i] = InttoHex(cmt_eq_old[i], 16)
    cmt_eq_new_json[i] = InttoHex(cmt_eq_new[i], 16)
    cmt_mult_json[i] = InttoHex(cmt_mult[i], 16)
    //console.log(cmt_old_json[i], cmt_new_json[i], cmt_eq_old_json[i], cmt_eq_new_json[i], cmt_mult_json[i])
}

// 打印数组
console.log("cmt_old_json: " + cmt_old_json.toString());
console.log("cmt_new_json: " + cmt_new_json.toString());
console.log("cmt_eq_old_json: " + cmt_eq_old_json.toString());
console.log("cmt_eq_new_json: " + cmt_eq_new_json.toString());
console.log("cmt_mult_json: " + cmt_mult_json.toString());

var t = new Date().toLocaleString();
const challenge = {cmt_old_json, cmt_new_json, cmt_eq_old_json, cmt_eq_new_json, cmt_mult_json, id, t}
// e -> hash

const test = JSON.stringify(challenge)
console.log("challenge:"+challenge.toString())
console.log("test:"+test.toString())
const hash = CryptoJS.SHA256(test.toString()).toString()
console.log("hash:"+hash)
const e = BigInt('0x' + hash) % 5000n
console.log("e:"+e)
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
    //console.log(z1[i], z2[i], z3[i], z4[i], z5[i], z6[i], z7[i])
}

// 打印数组
console.log("z1: " + z1.toString());
console.log("z2: " + z2.toString());
console.log("z3: " + z3.toString());
console.log("z4: " + z4.toString());
console.log("z5: " + z5.toString());
console.log("z6: " + z6.toString());
console.log("z7: " + z7.toString());

// 计算生物特征编码间的欧式距离 
let emb_dist = 0n
let rand_dist = 0n
for(let i = 0; i < dim; i++) {
    emb_dist = emb_dist + (emb_new[i] - emb_old[i]) * (emb_new[i] - emb_old[i])
    rand_dist = rand_dist + (rand_eq_new[i] + rand_eq_old[i] - 2n * rand_mult[i])
}

// 打印变量
console.log("emb_dist: ", emb_dist);
console.log("rand_dist: ", rand_dist);

//console.log(emb_dist, rand_dist)
const cmt_dist = code.Commitment(emb_dist, rand_dist)
//console.log(t);
// 数据打包
let a_json = new Array(2)
a_json[0] = InttoHex(a1, 16)
a_json[1] = InttoHex(a2, 16)

// 打印数组
console.log("a_json: " + a_json.toString());

let z_json = new Array(dim * 7)
let p_json = new Array(dim * 3)
for(let i = 0; i < dim; i++) {
    z_json[i] = InttoHex(z1[i], 16)
    z_json[dim + i] = InttoHex(z2[i], 16)
    z_json[2 * dim + i] = InttoHex(z3[i], 16)
    z_json[3 * dim + i] = InttoHex(z4[i], 16)
    z_json[4 * dim + i] = InttoHex(z5[i], 16)
    z_json[5 * dim + i] = InttoHex(z6[i], 16)
    z_json[6 * dim + i] = InttoHex(z7[i], 16)
    p_json[i] = InttoHex(p1[i], 16)
    p_json[dim + i] = InttoHex(p2[i], 16)
    p_json[2 * dim + i] = InttoHex(p3[i], 16)
}

// 打印数组
console.log("z_json: " + z_json.toString());
console.log("p_json: " + p_json.toString());

//console.log(a_json, z_json, p_json)
//console.log(cmt_dist)
var proof = {
    "id": id,
    "time": t,
    "proof": {
        "emb": cmt_new_json,
        "cmt": {cmt_eq_old_json, cmt_eq_new_json, cmt_mult_json},
        "a": a_json,
        "z": z_json,
        "p": p_json
    }
}
//var json = JSON.stringify(proof)
//console.log(json)
const proofString = JSON.stringify(proof, null, 2);
// 数据存储
fs.writeFile('proof.json', proofString, 'utf8', (err) => {
    if (err) {
      console.log("An error occured while writing JSON Object to File.");
      return console.log(err);
    }
    console.log("Proof file has been saved.");
  });
console.timeEnd('time')
