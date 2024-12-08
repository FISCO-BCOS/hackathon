/**
 * File Name: generate.js
 * Project Name: ZeroID
 * Author(s): Norton
 * Creation Date: 2024-02-01
 * Copyright: Copyright (C) 2024 Example Corporation. All rights reserved.
 * License: This code is distributed under the MIT license.
 * Modification History:
 *    - 2024-02-02: Initial version by Norton.
 *    - 2024-02-04: Minor bug fixes by Norton.
 * Description: This module is used to simulate the process of users generating biometric encoding 
 *              commitments during the registration process.
 * Contact Information: [None]
 */

import * as fs from 'fs'
import { Pedersen } from './Pedersen.js'

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
// 随机生成用的注册生物特征
let m = new Array(dim)
let r = new Array(dim)
for(let i = 0; i < dim; i++) {
    m[i] = BigInt(Math.floor(Math.random() * 1000000))
    r[i] = BigInt(Math.floor(Math.random() * 1000000))
    //console.log(m[i].toString(16), r[i].toString(16)),
}

// 生成生物特征编码的承诺
console.time('time')
let cmt = Array(dim)
const code = new Pedersen(g, h)
for(let i = 0; i < dim; i++) {
    cmt[i] = code.Commitment(m[i], r[i])
    //console.log(cmt[i].toString())
}
// 数据转化
let cmt_json = Array(dim)
let m_json = Array(dim)
let r_json = Array(dim)
for(let i = 0; i < dim; i++) {
    cmt_json[i] = InttoHex(cmt[i], 16)
    m_json[i] = InttoHex(m[i], 16)
    r_json[i] = InttoHex(r[i], 16)
    //console.log(cmt_json[i], m_json[i], r_json[i])
}
// 数据打包
const json = {
    id: 'User',
    m: m_json,
    r: r_json,
    cmt: cmt_json
}
const jsonString = JSON.stringify(json, null, 2);
// 数据存储
fs.writeFile('identity.json', jsonString, 'utf8', (err) => {
    if (err) {
      console.log("An error occured while writing JSON Object to File.");
      return console.log(err);
    }
    console.log("Identity file has been saved.");
  });
console.timeEnd('time')
