import * as fs from 'fs';
import CryptoJS from 'crypto-js';
import { Pedersen } from './Pedersen.js';

function InttoHex(Value, length) {
    let hex = Value.toString(16);
    const padding = length - hex.length;
    if (padding > 0) {
        hex = '0x' + '0'.repeat(padding) + hex;
    } else {
        hex = '0x' + hex;
    }
    return hex;
}

function generateCommitmentProof(identityFilePath, newEmb) {
    const dim = 128;
    const g = 56544564n;
    const h = 237684576n;

    let emb_old = new Array(dim);
    let rand_old = new Array(dim);
    let cmt_old = new Array(dim);
    let id;

    try {
        const data = fs.readFileSync(identityFilePath, 'utf8');
        const json = JSON.parse(data);
        id = json.id;
        for (let i = 0; i < dim; i++) {
            emb_old[i] = BigInt(json.m[i]);
            rand_old[i] = BigInt(json.r[i]);
            cmt_old[i] = BigInt(json.cmt[i]);
        }
    } catch (err) {
        console.error("An error occurred while reading the JSON File.", err);
        return null;
    }

    const code = new Pedersen(g, h);
    let emb_new = newEmb.map(e => BigInt(e));
    let rand_new = new Array(dim);
    let cmt_new = new Array(dim);
    for (let i = 0; i < dim; i++) {
        rand_new[i] = BigInt(Math.floor(Math.random() * 10000));
        cmt_new[i] = code.Commitment(emb_new[i], rand_new[i]);
    }

    let emb_eq_old = new Array(dim);
    let emb_eq_new = new Array(dim);
    let rand_eq_old = new Array(dim);
    let rand_eq_new = new Array(dim);
    let cmt_eq_old = new Array(dim);
    let cmt_eq_new = new Array(dim);
    let emb_mult = new Array(dim);
    let rand_mult = new Array(dim);
    let cmt_mult = new Array(dim);
    for (let i = 0; i < dim; i++) {
        emb_eq_old[i] = emb_old[i] * emb_old[i];
        emb_eq_new[i] = emb_new[i] * emb_new[i];
        emb_mult[i] = emb_old[i] * emb_new[i];
        rand_eq_old[i] = emb_old[i] * rand_old[i] + BigInt(Math.floor(Math.random() * 1000000));
        rand_eq_new[i] = emb_new[i] * rand_new[i] + BigInt(Math.floor(Math.random() * 1000000));
        rand_mult[i] = emb_new[i] * rand_old[i] + BigInt(Math.floor(Math.random() * 1000));
        cmt_eq_old[i] = code.Commitment(emb_eq_old[i], rand_eq_old[i]);
        cmt_eq_new[i] = code.Commitment(emb_eq_new[i], rand_eq_new[i]);
        cmt_mult[i] = code.Commitment(emb_mult[i], rand_mult[i]);
    }

    const b1 = BigInt(Math.floor(Math.random() * 10000));
    const b2 = BigInt(Math.floor(Math.random() * 10000));
    const b3 = BigInt(Math.floor(Math.random() * 10000));
    const b4 = BigInt(Math.floor(Math.random() * 10000));
    const b5 = BigInt(Math.floor(Math.random() * 10000));
    const b6 = BigInt(Math.floor(Math.random() * 10000));
    const b7 = BigInt(Math.floor(Math.random() * 10000));
    const a1 = code.Commitment(b1, b2);
    const a2 = code.Commitment(b3, b4);

    let p1 = new Array(dim);
    let p2 = new Array(dim);
    let p3 = new Array(dim);
    for (let i = 0; i < dim; i++) {
        let temp_eq_old = new Pedersen(cmt_old[i], h);
        p1[i] = temp_eq_old.Commitment(b1, b5);
        let temp_eq_new = new Pedersen(cmt_new[i], h);
        p2[i] = temp_eq_new.Commitment(b3, b6);
        p3[i] = temp_eq_old.Commitment(b3, b7);
    }

    let cmt_old_json = Array(dim);
    let cmt_new_json = Array(dim);
    let cmt_eq_old_json = Array(dim);
    let cmt_eq_new_json = Array(dim);
    let cmt_mult_json = Array(dim);
    for (let i = 0; i < dim; i++) {
        cmt_old_json[i] = InttoHex(cmt_old[i], 16);
        cmt_new_json[i] = InttoHex(cmt_new[i], 16);
        cmt_eq_old_json[i] = InttoHex(cmt_eq_old[i], 16);
        cmt_eq_new_json[i] = InttoHex(cmt_eq_new[i], 16);
        cmt_mult_json[i] = InttoHex(cmt_mult[i], 16);
    }

    const t = new Date().toLocaleString();
    const challenge = { cmt_old_json, cmt_new_json, cmt_eq_old_json, cmt_eq_new_json, cmt_mult_json, id, t };
    const test = JSON.stringify(challenge);
    const hash = CryptoJS.SHA256(test.toString()).toString();
    const e = BigInt('0x' + hash) % 5000n;

    let z1 = new Array(dim);
    let z2 = new Array(dim);
    let z3 = new Array(dim);
    let z4 = new Array(dim);
    let z5 = new Array(dim);
    let z6 = new Array(dim);
    let z7 = new Array(dim);
    for (let i = 0; i < dim; i++) {
        z1[i] = b1 + e * emb_old[i];
        z2[i] = b2 + e * rand_old[i];
        z3[i] = b3 + e * emb_new[i];
        z4[i] = b4 + e * rand_new[i];
        z5[i] = b5 + e * (rand_eq_old[i] - emb_old[i] * rand_old[i]);
        z6[i] = b6 + e * (rand_eq_new[i] - emb_new[i] * rand_new[i]);
        z7[i] = b7 + e * (rand_mult[i] - emb_new[i] * rand_old[i]);
    }

    let emb_dist = 0n;
    let rand_dist = 0n;
    for (let i = 0; i < dim; i++) {
        emb_dist = emb_dist + (emb_new[i] - emb_old[i]) * (emb_new[i] - emb_old[i]);
        rand_dist = rand_dist + (rand_eq_new[i] + rand_eq_old[i] - 2n * rand_mult[i]);
    }
    const cmt_dist = code.Commitment(emb_dist, rand_dist);

    let a_json = new Array(2);
    a_json[0] = InttoHex(a1, 16);
    a_json[1] = InttoHex(a2, 16);
    let z_json = new Array(dim * 7);
    let p_json = new Array(dim * 3);
    for (let i = 0; i < dim; i++) {
        z_json[i] = InttoHex(z1[i], 16);
        z_json[dim + i] = InttoHex(z2[i], 16);
        z_json[2 * dim + i] = InttoHex(z3[i], 16);
        z_json[3 * dim + i] = InttoHex(z4[i], 16);
        z_json[4 * dim + i] = InttoHex(z5[i], 16);
        z_json[5 * dim + i] = InttoHex(z6[i], 16);
        z_json[6 * dim + i] = InttoHex(z7[i], 16);
        p_json[i] = InttoHex(p1[i], 16);
        p_json[dim + i] = InttoHex(p2[i], 16);
        p_json[2 * dim + i] = InttoHex(p3[i], 16);
    }

    const proof = {
        "id": id,
        "time": t,
        "proof": {
            "emb": cmt_new_json,
            "cmt": { cmt_eq_old_json, cmt_eq_new_json, cmt_mult_json },
            "a": a_json,
            "z": z_json,
            "p": p_json
        }
    };

    // 返回打包好的 JSON 字符串
    return JSON.stringify(proof, null, 2);
}

