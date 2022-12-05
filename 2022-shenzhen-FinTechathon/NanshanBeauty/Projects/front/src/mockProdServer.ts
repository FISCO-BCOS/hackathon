import { createProdMockServer } from 'vite-plugin-mock/es/createProdMockServer'
// import.meta.glob 进行全部导入
const modulesFiles:any= import.meta.globEager('../mock/*.ts')
let modules: Array<any> = []
for (const path in modulesFiles) {
  modules = modules.concat(modulesFiles[path].default)
}
export function setupProdMockServer() {
  createProdMockServer([...modules])
}
