const Server = require('ws').Server;

const ws = new Server({ port: 8888 });
ws.on('connection', function(socket) {
    // 监听客户端发来的消息
    socket.on('message', function(msg) {
        console.log(msg);   // 这个就是客户端发来的消息
		// ws.send("yes");
    });
})
var Socket = {
// 监听服务端和客户端的连接情况
first:function(option){
	console.log(ws.clients);
	ws.clients.forEach(function(conn){
		conn.send(JSON.stringify(option));
	})
},
}
module.exports = Socket;



