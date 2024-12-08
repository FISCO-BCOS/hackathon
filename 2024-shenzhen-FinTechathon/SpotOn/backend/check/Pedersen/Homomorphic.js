class Homomorphic {
    //#p = 67108859;
    #p = 67108859n;
    Add(ComA, ComB) {
        return ComA * ComB % this.#p
    }
    Pow(Com, n) {
        let res = 1n;
        let quot = BigInt(n);
        let rem = 0n;
        let table = [];
        table.push(Com);
        for(let i = 1; i < 64; i++) {
            table.push(table[i - 1] * table[i - 1] % this.#p);
        }
        for(let j = 0; j < 64; j++) {
            rem = quot % 2n;
            quot = quot / 2n;
            if(rem == 1n) {
                res = res * table[j] % this.#p;
            } else {}
        }
        return res
    }
    Sub(ComA, ComB, ComC) {
        if(ComB * ComC % this.#p == ComA) {
            return 1
        }
        else {
            return 0
        }
    }
}

export {Homomorphic};
