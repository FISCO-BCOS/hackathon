/**
 * 网站配置文件
 */
const greenText = (text) => `\x1b[32m${text}\x1b[0m`;

const config = {
  appName: '数字虎符',
  appLogo: 'logo.png',
  showViteLogo: true,
  logs: [],
}

export const viteLogo = (env) => {
  if (config.showViteLogo) {

    console.log('\n');
  }
}

export default config
