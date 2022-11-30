import {Message} from 'element-ui'

const duration = 2000;
const showClose = true;

export function getMessage(type, message) {
    return Message({
        type,
        message,
        duration,
        showClose
    })
}
