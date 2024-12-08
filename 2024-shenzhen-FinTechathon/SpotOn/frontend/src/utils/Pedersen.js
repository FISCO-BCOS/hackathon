/**
 * File Name: Pedersen.js
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

export class Pedersen {
    #g;
    #h;
    //#p = 67108859;
    #p = 67108859n;
    #g_table = [];
    #h_table = [];
    constructor(g, h) {
        this.#g = g % this.#p;
        this.#h = h % this.#p;
        this.#g_table.push(this.#g)
        this.#h_table.push(this.#h)
        for(let i = 1; i < 64; i++) {
            this.#g_table.push(this.#g_table[i - 1] * this.#g_table[i - 1] % this.#p);
            this.#h_table.push(this.#h_table[i - 1] * this.#h_table[i - 1] % this.#p);
        }
        // bebug
        // console.log('g:' + this.#g + ' h:' + this.#h)
        // console.log('g_table:' + this.#g_table)
        // console.log('h_table:' + this.#h_table)
    }
    Commitment(v, r) {
        let v_quot = v;
        let r_quot = r;
        let v_rem = 0n;
        let r_rem = 0n;
        let G = 1n;
        let H = 1n;
        for(let i = 0; i < 64; i++) {
            v_rem = v_quot % 2n;
            v_quot = v_quot / 2n;
            r_rem = r_quot % 2n;
            r_quot = r_quot / 2n;
            // debug
            // console.log(i + ' v_rem:' + v_rem + ' v_qout:' + v_quot);
            // console.log(i + ' r_rem:' + r_rem + ' r_qout:' + r_quot);
            if(v_rem == 1n) {
                G = G * this.#g_table[i] % this.#p;
            } else {}
            if(r_rem == 1n) {
                H = H * this.#h_table[i] % this.#p;
            } else {}
        }
        return G * H % this.#p
    }
}
