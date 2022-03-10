const vscode = require('vscode');

function installDependcy(terminal) {
    terminal.sendText("sudo apt install -y openssl curl default-jdk");
}

function setNodeBase(terminal) {

    vscode.window.showInputBox({ placeHolder: "Enter fisco dir, default: fisco" }).then(value => {

        if (!value) {
            value = 'fisco'
        }

        const build_chiain_script = 'curl -LO https://github.com/FISCO-BCOS/FISCO-BCOS/releases/download/`curl -s https://api.github.com/repos/FISCO-BCOS/FISCO-BCOS/releases | grep "\\"v2\\.[0-9]\\.[0-9]\\\"\" | sort -u | tail -n 1 | cut -d \\" -f 4`/build_chain.sh && chmod u+x build_chain.sh';


        terminal.sendText(`cd ~ && mkdir -p ${value} && cd ${value} && ${build_chiain_script}`);
        return value;
    });

}

function buildChain(terminal) {

    vscode.window.showInputBox({ placeHolder: "Enter p2p_port, channel_port, jsonrpc_port, default: 30300,20200,8545" }).then(ports => {

        if (!ports) {
            ports = '30300,20200,8545'
        }

        const build_chain_script = `bash build_chain.sh -l "127.0.0.1:4" -p ${ports}`;
        terminal.sendText(build_chain_script);

    });
}


function startNodes(terminal) {
    const start_nodes_script = "bash nodes/127.0.0.1/start_all.sh";
    terminal.sendText(start_nodes_script);
}

function checkProcess(terminal) {
    terminal.sendText("ps -ef | grep -v grep | grep fisco-bcos");
}



module.exports = {
    installDependcy,
    setNodeBase,
    buildChain,
    startNodes,
    checkProcess
}