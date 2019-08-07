const fs = require('fs')
const cfg = require("../truffle-config")

const PATH_TRUFFLE_WK = './build/contracts/'
const truffleFile = JSON.parse(fs.readFileSync(PATH_TRUFFLE_WK + cfg.contract + ".json"))

const abi = JSON.stringify(truffleFile["abi"])
const bin = truffleFile['bytecode'].slice(2)

fs.writeFile("./build/" + cfg.contract + ".bin", bin, (err) =>{
  if (err) {
    console.log(err)
  }
})

fs.writeFile("./build/" + cfg.contract + ".abi", abi, (err) => {
  if (err) {
    console.log(err)
  }
})

