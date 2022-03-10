const vscode = require('vscode');

function loadScaffold(terminal, git_repo) {

    vscode.window.showInputBox({ placeHolder: "Enter Scaffold directory, default is current directory" }).then(value => {

        if (!value) {
            value = ''
        }

        terminal.sendText(`git clone ${git_repo} ${value}`);

    });
}

module.exports = {
    loadScaffold,
}