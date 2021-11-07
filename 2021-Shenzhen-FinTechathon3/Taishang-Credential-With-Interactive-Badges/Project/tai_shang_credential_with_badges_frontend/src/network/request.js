import axios from 'axios'

export function request(config) {
  const instance = axios.create({
    baseURL:'http://101.35.24.15:5000',
    timeout:5000,
    // method:"POST",
  })
  return instance(config)
}
