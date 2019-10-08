// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
const vscode = require('vscode');
const {
	checkRemote,
	ensureTerminalExists,
} = require('./helper/env');

const { installDependcy, setNodeBase, buildChain, startNodes } = require('./helper/install');

// import starter project
const { loadScaffold } = require('./helper/scaffold');

// this method is called when your extension is activated
// your extension is activated the very first time the command is executed

/**
 * @param {vscode.ExtensionContext} context
 */
function activate(context) {

	// Use the console to output diagnostic information (console.log) and errors (console.error)
	// This line of code will only be executed once when your extension is activated
	console.log('Congratulations, your extension "fboost-extension" is now active!');

	// The command has been defined in the package.json file
	// Now provide the implementation of the command with  registerCommand
	// The commandId parameter must match the command field in package.json
	let disposable = vscode.commands.registerCommand('extension.helloWorld', function () {
		// The code you place here will be executed every time your command is executed

		// Display a message box to the user
		vscode.window.showInformationMessage('Hello World!');
	});

	context.subscriptions.push(disposable);

	this.terminal = undefined;
	this.node_base = 'fisco';

	context.subscriptions.push(vscode.commands.registerCommand('fboost.initialEnv', () => {

		this.terminal = ensureTerminalExists();
		this.terminal.show(true);
		checkRemote(this.terminal);
	}));


	context.subscriptions.push(vscode.commands.registerCommand('fboost.installDependency', () => {

		if (this.terminal) {
			installDependcy(this.terminal);
		}

	}));

	context.subscriptions.push(vscode.commands.registerCommand('fboost.setNodeBase', () => {

		if (this.terminal) {
			this.node_base = setNodeBase(this.terminal);
		}

	}));

	context.subscriptions.push(vscode.commands.registerCommand('fboost.buildChain', () => {

		if (this.terminal) {
			buildChain(this.terminal);
		}

	}));

	context.subscriptions.push(vscode.commands.registerCommand('fboost.startNodes', () => {

		if (this.terminal) {
			startNodes(this.terminal);
		}

	}));


	context.subscriptions.push(vscode.commands.registerCommand('fboost.scaffold.spring-boot', () => {

		if (this.terminal) {
			loadScaffold(this.terminal, "https://github.com/FISCO-BCOS/spring-boot-starter.git");
		}

	}));

	const terminalCommands = [
		// about check
		registerTerminalCommand('fboost.checkProcess', "ps -ef | grep -v grep | grep fisco-bcos"),
		registerTerminalCommand('fboost.checkLog', `tail -f nodes/127.0.0.1/node0/log/log*  | grep connected`),
		registerTerminalCommand('fboost.checkConsensus', `tail -f nodes/127.0.0.1/node0/log/log*  | grep +++`),
		// about console
		registerTerminalCommand('fboost.installConsole', `bash <(curl -s https://raw.githubusercontent.com/FISCO-BCOS/console/master/tools/download_console.sh)`),
		registerTerminalCommand('fboost.copyConsoleConfig', `cp -n console/conf/applicationContext-sample.xml console/conf/applicationContext.xml`),
		registerTerminalCommand('fboost.copyConsoleCert', `cp nodes/127.0.0.1/sdk/* console/conf/`),
		registerTerminalCommand('fboost.startConsole', `cd ~/fisco/console && bash start.sh`, false),

	];

	terminalCommands.map(tc => {
		context.subscriptions.push(tc);
	});




}
exports.activate = activate;

// this method is called when your extension is deactivated
function deactivate() { }

function registerTerminalCommand(command, script, execute_im = true) {
	return vscode.commands.registerCommand(command, () => {
		if (this.terminal) {
			this.terminal.sendText(script, execute_im);
		}
	});
}

module.exports = {
	activate,
	deactivate
}




