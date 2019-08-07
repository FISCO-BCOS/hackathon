module.exports = {
  networks: {
    development: {
      host: "127.0.0.1",
      port: 8917,
      network_id: "*",
    },
  },

  compilers: {
    solc: {
      version: "0.4.25",
    }
  },

  privateKey: "526ccb243b5e279a3ce30c08e4d091a0eb2c3bb5a700946d4da47b28df8fe6d5",
  privateKey2: "3058f2f8626a7445cecd3075eb8da9bb3284256d2737c4a185a96f79daca9441",
  blockKey1: "6baed1bb91fc7d37223aee677aff54d7f6b30b73f817b1df474c1c42b1fb2b9a",
  blockKey2: "b93f663a6f382c852c4f2e4464de0ef0203f3e62523837741e78fd19c88ff26c",
  contract: "HelloWorld"
}
