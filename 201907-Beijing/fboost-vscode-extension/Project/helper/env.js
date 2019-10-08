const vscode = require('vscode');

function checkRemote(terminal) {
    vscode.window.showInputBox({ placeHolder: "Is remote connect to host? (y/N)" }).then((value = "N") => {
        if (value.toLowerCase() === 'y') {

            vscode.window.showInputBox({ placeHolder: "Enter host: ssh username@ip:port" }).then(ssh => {
                terminal.sendText(ssh);
            });

        } else {
            return;
        }

    });
}

function ensureTerminalExists() {
    if (vscode.window.terminals.length === 0) {
        return vscode.window.createTerminal(`FBoost`);
    } else {
        return vscode.window.terminals[0];
    }
}

module.exports = {
    checkRemote,
    ensureTerminalExists,
}