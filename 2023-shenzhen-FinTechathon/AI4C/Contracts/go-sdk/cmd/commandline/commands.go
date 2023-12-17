package commandline

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"strconv"
	"strings"

	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/common/hexutil"
	"github.com/spf13/cobra"
)

var info = ", you can type console help for more information"

const (
	indent = "  "
)

// commands
// var bashCompletionCmd = &cobra.Command{
// 	Use:   "bashCompletion",
// 	Short: "Generates bash completion scripts",
// 	Long: `A script "console.sh" will get you completions of the console commands.
// Copy it to

//     /etc/bash_completion.d/

// as described here:

//     https://debian-administration.org/article/316/An_introduction_to_bash_completion_part_1

// and reset your terminal to use autocompletion.`,
// 	Run: func(cmd *cobra.Command, args []string) {
// 		rootCmd.GenBashCompletionFile("console.sh");
// 		fmt.Println("console.sh created on your current diretory successfully.")
// 	},
// }

// var zshCompletionCmd = &cobra.Command{
// 	Use:   "zshCompletion",
// 	Short: "Generates zsh completion scripts",
// 	Long: `A script "console.zsh" will get you completions of the console commands.
// The recommended way to install this script is to copy to '~/.zsh/_console', and
// then add the following to your ~/.zshrc file:

//     fpath=(~/.zsh $fpath)

// as described here:

//     https://debian-administration.org/article/316/An_introduction_to_bash_completion_part_1

// and reset your terminal to use autocompletion.`,
// 	Run: func(cmd *cobra.Command, args []string) {
// 		rootCmd.GenZshCompletionFile("_console");
// 		fmt.Println("zsh file _console had created on your current diretory successfully.")
// 	},
// }

// =========== account ==========
var newAccountCmd = &cobra.Command{
	Use:   "newAccount",
	Short: "Create a new account",
	Long:  `Create a new account and save the results to ./bin/account/yourAccountName.keystore in encrypted form.`,
	Args:  cobra.ExactArgs(2),
	Run: func(cmd *cobra.Command, args []string) {
		clientVer, err := RPC.GetClientVersion(context.Background())
		if err != nil {
			fmt.Printf("client version not found: %v\n", err)
			return
		}
		cv, err := json.MarshalIndent(clientVer, "", indent)
		if err != nil {
			fmt.Printf("client version marshalIndent error: %v", err)
		}
		fmt.Printf("Client Version: \n%s\n", cv)
	},
}

// ======= node =======

var getClientVersionCmd = &cobra.Command{
	Use:   "getClientVersion",
	Short: "                                   Get the blockchain version",
	Long:  `Returns the specific FISCO BCOS version that runs on the node you connected.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		clientVer, err := RPC.GetClientVersion(context.Background())
		if err != nil {
			fmt.Printf("client version not found: %v\n", err)
			return
		}
		cv, err := json.MarshalIndent(clientVer, "", indent)
		if err != nil {
			fmt.Printf("client version marshalIndent error: %v", err)
		}
		fmt.Printf("Client Version: \n%s\n", cv)
	},
}

var getGroupIDCmd = &cobra.Command{
	Use:   "getGroupID",
	Short: "                                   Get the group ID of the client",
	Long:  `Returns the group ID that the console had connected to.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		groupID := RPC.GetGroupID()
		fmt.Printf("Group ID: \n%s\n", groupID)
	},
}

var getBlockNumberCmd = &cobra.Command{
	Use:   "getBlockNumber",
	Short: "                                   Get the latest block height of the blockchain",
	Long: `Returns the latest block height in the specified group.
The block height is encoded in hex`,
	Args: cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		blockNumber, err := RPC.GetBlockNumber(context.Background())
		if err != nil {
			fmt.Printf("block number not found: %v\n", err)
			return
		}
		fmt.Printf("blocknumber: %d\n", blockNumber)
	},
}

var getPbftViewCmd = &cobra.Command{
	Use:   "getPbftView",
	Short: "                                   Get the latest PBFT view(PBFT consensus only)",
	Long: `Returns the latest PBFT view in the specified group where the node is located.
The PBFT view is encoded in hex`,
	Args: cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		pbft, err := RPC.GetPBFTView(context.Background())
		if err != nil {
			fmt.Printf("PBFT view not found: %v\n", err)
			return
		}
		fmt.Printf("PBFT view: \n%s\n", pbft)
	},
}

var getSealerListCmd = &cobra.Command{
	Use:   "getSealerList",
	Short: "                                   Get the sealers' ID list",
	Long:  `Returns an ID list of the sealer nodes within the specified group.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		sealerList, err := RPC.GetSealerList(context.Background())
		if err != nil {
			fmt.Printf("sealer list not found: %v\n", err)
			return
		}
		fmt.Printf("Sealer List: \n%s\n", sealerList)
	},
}

var getObserverListCmd = &cobra.Command{
	Use:   "getObserverList",
	Short: "                                   Get the observers' ID list",
	Long:  `Returns an ID list of observer nodes within the specified group.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		observerList, err := RPC.GetObserverList(context.Background())
		if err != nil {
			fmt.Printf("observer list not found: %v\n", err)
			return
		}
		fmt.Printf("Observer List: \n%s\n", observerList)
	},
}

var getConsensusStatusCmd = &cobra.Command{
	Use:   "getConsensusStatus",
	Short: "                                   Get consensus status of nodes",
	Long:  `Returns consensus status information within the specified group.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		raw, err := RPC.GetConsensusStatus(context.Background())
		if err != nil {
			fmt.Printf("consensus status not found: %v\n", err)
			return
		}
		fmt.Printf("Consensus Status: \n%s\n", raw)
	},
}

var getSyncStatusCmd = &cobra.Command{
	Use:   "getSyncStatus",
	Short: "                                   Get synchronization status of nodes",
	Long:  `Returns synchronization status information within the specified group.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		syncStatus, err := RPC.GetSyncStatus(context.Background())
		if err != nil {
			fmt.Printf("synchronization status not found: %v\n", err)
			return
		}
		raw, err := json.MarshalIndent(syncStatus, "", indent)
		if err != nil {
			fmt.Printf("synchronization status marshalIndent error: %v", err)
		}
		fmt.Printf("Synchronization Status: \n%s\n", raw)
	},
}

var getPeersCmd = &cobra.Command{
	Use:   "getPeers",
	Short: "                                   Get the connected peers' information",
	Long:  `Returns the information of connected p2p nodes.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		nodes, err := RPC.GetPeers(context.Background())
		if err != nil {
			fmt.Printf("peers not found: %v\n", err)
			return
		}
		peers, err := json.MarshalIndent(nodes, "", indent)
		if err != nil {
			fmt.Printf("peers marshalIndent error: %v", err)
		}
		fmt.Printf("Peers: \n%s\n", peers)
	},
}

var getGroupPeersCmd = &cobra.Command{
	Use:   "getGroupPeers",
	Short: "                                   Get all peers' ID within the group",
	Long:  `Returns an ID list of consensus nodes and observer nodes within the specified group.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		peers, err := RPC.GetGroupPeers(context.Background())
		if err != nil {
			fmt.Printf("peers not found: %v\n", err)
			return
		}
		fmt.Printf("Peers: \n%s\n", peers)
	},
}

var getNodeIDListCmd = &cobra.Command{
	Use:   "getNodeIDList",
	Short: "                                   Get ID list of nodes",
	Long:  `Returns an ID list of the node itself and the connected p2p nodes.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		peers, err := RPC.GetNodeIDList(context.Background())
		if err != nil {
			fmt.Printf("node ID list not found: %v\n", err)
			return
		}
		fmt.Printf("Node ID list: \n%s\n", peers)
	},
}

var getGroupListCmd = &cobra.Command{
	Use:   "getGroupList",
	Short: "                                   Get ID list of groups that the node belongs",
	Long:  `Returns an ID list of groups that the node belongs.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		peers, err := RPC.GetGroupList(context.Background())
		if err != nil {
			fmt.Printf("group IDs list not found: %v\n", err)
			return
		}
		fmt.Printf("Group List: \n%s\n", peers)
	},
}

// ========= block access ==========

var getBlockByHashCmd = &cobra.Command{
	Use:   "getBlockByHash",
	Short: "[blockHash]   [true/false]         Query the block by its hash",
	Long: `Returns the block information according to the block hash.
Arguments:
          [blockHash]: hash string
[includeTransactions]: must be "true" or "false".

For example:

    [getBlockByHash] [0x910ea44e2a83618c7cc98456678c9984d94977625e224939b24b3c904794b5ec] [true]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html#`,
	Args: cobra.RangeArgs(1, 2),
	Run: func(cmd *cobra.Command, args []string) {
		var includeTx bool

		_, err := isValidHex(args[0])
		if err != nil {
			fmt.Println(err)
			return
		}

		if len(args) == 1 {
			includeTx = true
		} else {
			_includeTx, err := strconv.ParseBool(args[1])
			if err != nil {
				fmt.Printf("Arguments error: please check your input: %s%s: %v\n", args[1], info, err)
				return
			}
			includeTx = _includeTx
		}

		blockHash := common.HexToHash(args[0])
		block, err := RPC.GetBlockByHash(context.Background(), blockHash, includeTx)
		if err != nil {
			fmt.Printf("block not found: %v\n", err)
			return
		}
		peers, err := json.MarshalIndent(block, "", indent)
		fmt.Printf("Block: \n%s\n", peers)
	},
}

var getBlockByNumberCmd = &cobra.Command{
	Use:   "getBlockByNumber",
	Short: "[blockNumber] [true/false]         Query the block by its number",
	Long: `Returns the block information according to the block number.
Arguments:
       [blockNumber]: can be input in a decimal or in hex(prefix with "0x").
[includeTransactions]: must be "true" or "false".

For example:

    [getBlockByNumber] [0x9] [true]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html#`,
	Args: cobra.RangeArgs(1, 2),
	Run: func(cmd *cobra.Command, args []string) {
		var includeTx bool

		blockNumber, err := strconv.ParseInt(args[0], 0, 64)
		if err != nil {
			fmt.Printf("parse block number failed, err: %v", err)
			return
		}
		_, err = isBlockNumberOutOfRange(blockNumber)
		if err != nil {
			fmt.Println(err)
			return
		}

		if len(args) == 1 {
			includeTx = true
		} else {
			_includeTx, err := strconv.ParseBool(args[1])
			if err != nil {
				fmt.Printf("Arguments error: please check your input: %s%s: %v\n", args[1], info, err)
				return
			}
			includeTx = _includeTx
		}

		block, err := RPC.GetBlockByNumber(context.Background(), blockNumber, includeTx)
		if err != nil {
			fmt.Printf("block not found: %v\n", err)
			return
		}
		js, err := json.MarshalIndent(block, "", indent)
		fmt.Printf("Block: \n%s\n", js)
	},
}

var getBlockHashByNumberCmd = &cobra.Command{
	Use:   "getBlockHashByNumber",
	Short: "[blockNumber]                      Query the block hash by its number",
	Long: `Returns the block hash according to the block number.
Arguments:
[blockNumber]: can be input in a decimal format or in hex(prefix with "0x").

For example:

    [getBlockHashByNumber] [0x9]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html#`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		blockNumber, err := strconv.ParseInt(args[0], 0, 64)
		if err != nil {
			fmt.Printf("parse block number failed, %s", args[0])
			return
		}

		_, err = isBlockNumberOutOfRange(blockNumber)
		if err != nil {
			fmt.Println(err)
			return
		}

		blockHash, err := RPC.GetBlockHashByNumber(context.Background(), blockNumber)
		if err != nil {
			fmt.Printf("block not found: %v\n", err)
			return
		}
		fmt.Printf("Block Hash: \n%s\n", blockHash.Hex())
	},
}

// ======== transaction access ========

var getTransactionByHashCmd = &cobra.Command{
	Use:   "getTransactionByHash",
	Short: "[transactionHash]                  Query the transaction by its hash",
	Long: `Returns the transaction according to the transaction hash.
Arguments:
[transactionHash]: hash string.

For example:

    [getTransactionByHash] [0x7536cf1286b5ce6c110cd4fea5c891467884240c9af366d678eb4191e1c31c6f]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html#`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		_, err := isValidHex(args[0])
		if err != nil {
			fmt.Println(err)
			return
		}

		txHash := common.HexToHash(args[0])
		transaction, err := RPC.GetTransactionByHash(context.Background(), txHash)
		if err != nil {
			fmt.Printf("transaction not found: %v\n", err)
			return
		}
		tx, err := json.MarshalIndent(transaction, "", indent)
		if err != nil {
			fmt.Printf("transaction marshalIndent error: %v\n", err)
			return
		}
		fmt.Printf("Transaction: \n%s\n", tx)
	},
}

var getTransactionByBlockHashAndIndexCmd = &cobra.Command{
	Use:   `getTransactionByBlockHashAndIndex`,
	Short: "[blockHash]   [transactionIndex]   Query the transaction by block hash and transaction index",
	Long: `Returns transaction information based on block hash and transaction index inside the block.
Arguments:
       [blockHash]: block hash string.
[transactionIndex]: can be input in a decimal or in hex(prefix with "0x").

For example:

    [getTransactionByBlockHashAndIndex] [0x10bfdc1e97901ed22cc18a126d3ebb8125717c2438f61d84602f997959c631fa] [0x0]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html#`,
	Args: cobra.ExactArgs(2),
	Run: func(cmd *cobra.Command, args []string) {
		_, err := isValidHex(args[0])
		if err != nil {
			fmt.Println(err)
			return
		}

		txIndex, err := strconv.ParseInt(args[1], 0, 0)
		if err != nil {
			fmt.Printf("parse txIndex failed, please check your input: %s: %v", args[1], err)
			return
		}
		blockHash := common.HexToHash(args[0])
		transaction, err := RPC.GetTransactionByBlockHashAndIndex(context.Background(), blockHash, int(txIndex))
		if err != nil {
			fmt.Printf("transaction not found: %v\n", err)
			return
		}
		tx, err := json.MarshalIndent(transaction, "", indent)
		if err != nil {
			fmt.Printf("transaction marshalIndent error: %v\n", err)
			return
		}
		fmt.Printf("Transaction: \n%s\n", tx)
	},
}

var getTransactionByBlockNumberAndIndexCmd = &cobra.Command{
	Use:   "getTransactionByBlockNumberAndIndex",
	Short: "[blockNumber] [transactionIndex]   Query the transaction by block number and transaction index",
	Long: `Returns transaction information based on block number and transaction index inside the block.
Arguments:
     [blockNumber]: block number encoded in decimal format or in hex(prefix with "0x").
[transactionIndex]: can be input in a decimal or in hex(prefix with "0x").

For example:

    [getTransactionByBlockNumberAndIndex] [0x9] [0x0]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html#`,
	Args: cobra.ExactArgs(2),
	Run: func(cmd *cobra.Command, args []string) {
		blockNumber, err := strconv.ParseInt(args[0], 0, 64)
		if err != nil {
			fmt.Printf("parse block number failed, please check your input: %s: %v", args[0], err)
			return
		}

		_, err = isBlockNumberOutOfRange(blockNumber)
		if err != nil {
			fmt.Println(err)
			return
		}

		txIndex, err := strconv.Atoi(args[1])
		if err != nil {
			fmt.Printf("parse txIndex failed, please check your input: %s: %v", args[1], err)
			return
		}
		tx, err := RPC.GetTransactionByBlockNumberAndIndex(context.Background(), blockNumber, txIndex)
		if err != nil {
			fmt.Printf("transaction not found: %v\n", err)
			return
		}
		raw, err := json.MarshalIndent(tx, "", indent)
		if err != nil {
			fmt.Printf("transaction marshalIndent error: %v", err)
		}
		fmt.Printf("Transaction: \n%s\n", raw)
	},
}

var getTransactionReceiptCmd = &cobra.Command{
	Use:   "getTransactionReceipt",
	Short: "[transactionHash]                  Query the transaction receipt by transaction hash",
	Long: `Returns transaction receipt information based on transaction hash.
Arguments:
[transactionHash]: transaction hash string.

For example:

    [getTransactionReceipt] [0x708b5781b62166bd86e543217be6cd954fd815fd192b9a124ee9327580df8f3f]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html#`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		_, err := isValidHex(args[0])
		if err != nil {
			fmt.Println(err)
			return
		}

		txHash := common.HexToHash(args[0])
		tx, err := RPC.GetTransactionReceipt(context.Background(), txHash)
		if err != nil {
			fmt.Printf("transaction receipt not found: %v\n", err)
			return
		}
		fmt.Printf("Transaction Receipt: \n%s\n", tx)
	},
}

var getPendingTransactionsCmd = &cobra.Command{
	Use:   "getPendingTransactions",
	Short: "                                   Get the pending transactions",
	Long:  `Return the transactions that are pending for packaging.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		tx, err := RPC.GetPendingTransactions(context.Background())
		if err != nil {
			fmt.Printf("transaction not found: %v\n", err)
			return
		}
		fmt.Printf("Pending Transactions: \n%+v\n", *tx)
	},
}

var getPendingTxSizeCmd = &cobra.Command{
	Use:   "getPendingTxSize",
	Short: "                                   Get the count of pending transactions",
	Long:  `Return the total count of pending transactions.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		tx, err := RPC.GetPendingTxSize(context.Background())
		if err != nil {
			fmt.Printf("transactions not found: %v\n", err)
			return
		}
		fmt.Printf("Pending Transactions Count: \n    hex: %s\n", tx)
	},
}

// ======== contracts =======

var getCodeCmd = &cobra.Command{
	Use:   "getCode",
	Short: "[contract address]                 Get the contract data from contract address",
	Long: `Return contract data queried according to contract address.
Arguments:
[contract address]: contract hash string.

For example:

    [getCode] [0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html#`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		_, err := isValidHex(args[0])
		if err != nil {
			fmt.Println(err)
			return
		}

		contractAdd := common.HexToAddress(args[0])
		code, err := RPC.GetCode(context.Background(), contractAdd)
		if err != nil {
			fmt.Printf("This address does not exist: %v\n", err)
			return
		}

		if len(string(code)) < 5 {
			fmt.Println("This address does not exist: ", args[0])
			return
		}

		fmt.Printf("Contract Code: \n%s\n", code)
	},
}

var getTotalTransactionCountCmd = &cobra.Command{
	Use:   "getTotalTransactionCount",
	Short: "                                   Get the total transactions and the latest block height",
	Long:  `Returns the current total number of transactions and block height.`,
	Args:  cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		counts, err := RPC.GetTotalTransactionCount(context.Background())
		if err != nil {
			fmt.Printf("information not found: %v\n", err)
			return
		}
		raw, err := json.MarshalIndent(counts, "", indent)
		if err != nil {
			fmt.Printf("totalTransactionCount MarshalIndent error: %v", err)
		}
		fmt.Printf("Latest Statistics on Transaction and Block Height: \n%s\n", raw)
	},
}

var getSystemConfigByKeyCmd = &cobra.Command{
	Use:   "getSystemConfigByKey",
	Short: "[configurationItem]                Get the system configuration through key-value",
	Long: `Returns the system configuration through key-value.
Arguments:
[key to query]: currently only support four key: "tx_count_limit", "tx_gas_limit", "rpbft_epoch_sealer_num", "rpbft_epoch_block_num", "consensus_timeout".

For example:

    [getSystemConfigByKey] [tx_count_limit]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html#`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		configMap := make(map[string]struct{})
		configMap["tx_count_limit"] = struct{}{}
		configMap["tx_gas_limit"] = struct{}{}
		configMap["rpbft_epoch_sealer_num"] = struct{}{}
		configMap["rpbft_epoch_block_num"] = struct{}{}
		configMap["consensus_timeout"] = struct{}{}
		if _, ok := configMap[args[0]]; !ok {
			fmt.Println("The key not found: ", args[0], ", currently only support [tx_count_limit], [tx_gas_limit], [rpbft_epoch_sealer_num], [rpbft_epoch_block_num] and [consensus_timeout]")
			return
		}
		key := args[0]
		value, err := RPC.GetSystemConfigByKey(context.Background(), key)
		if err != nil {
			fmt.Printf("information not found: %v\n", err)
			return
		}
		fmt.Printf("Result: \n%s\n", value)
	},
}

var completionCmd = &cobra.Command{
	Use:   "completion [bash|zsh|fish|powershell]",
	Short: "Generate completion script",
	Long: `To load completions:

Bash:

$ source <(yourprogram completion bash)

# To load completions for each session, execute once:
Linux:
  $ yourprogram completion bash > /etc/bash_completion.d/yourprogram
MacOS:
  $ yourprogram completion bash > /usr/local/etc/bash_completion.d/yourprogram

Zsh:

$ source <(yourprogram completion zsh)

# To load completions for each session, execute once:
$ yourprogram completion zsh > "${fpath[1]}/_yourprogram"

Fish:

$ yourprogram completion fish | source

# To load completions for each session, execute once:
$ yourprogram completion fish > ~/.config/fish/completions/yourprogram.fish
`,
	DisableFlagsInUseLine: true,
	Hidden:                true,
	PersistentPreRun:      nil,
	ValidArgs:             []string{"bash", "zsh", "fish", "powershell"},
	Args:                  cobra.ExactValidArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		switch args[0] {
		case "bash":
			err := cmd.Root().GenBashCompletion(os.Stdout)
			if err != nil {
				log.Fatal(err)
			}
		case "zsh":
			err := cmd.Root().GenZshCompletion(os.Stdout)
			if err != nil {
				log.Fatal(err)
			}
		case "powershell":
			err := cmd.Root().GenPowerShellCompletion(os.Stdout)
			if err != nil {
				log.Fatal(err)
			}
		}
	},
}

func init() {
	// add common command
	rootCmd.AddCommand(completionCmd)
	rootCmd.AddCommand(newAccountCmd)
	// add node command
	rootCmd.AddCommand(getClientVersionCmd, getGroupIDCmd, getBlockNumberCmd, getPbftViewCmd, getSealerListCmd)
	rootCmd.AddCommand(getObserverListCmd, getConsensusStatusCmd, getSyncStatusCmd, getPeersCmd, getGroupPeersCmd)
	rootCmd.AddCommand(getNodeIDListCmd, getGroupListCmd)
	// add block access command
	rootCmd.AddCommand(getBlockByHashCmd, getBlockByNumberCmd, getBlockHashByNumberCmd)
	// add transaction command
	rootCmd.AddCommand(getTransactionByHashCmd, getTransactionByBlockHashAndIndexCmd, getTransactionByBlockNumberAndIndexCmd)
	rootCmd.AddCommand(getTransactionReceiptCmd, getPendingTransactionsCmd, getPendingTxSizeCmd)
	// add contract command
	rootCmd.AddCommand(getCodeCmd, getTotalTransactionCountCmd, getSystemConfigByKeyCmd)
	// add contract command

	// cobra.OnInitialize(initConfig)

	// FIXME: add a custom help command or find a way to make help command work without network

	// Here you will define your flags and configuration settings.
	// Cobra supports persistent flags, which, if defined here,
	// will be global for your application.

	rootCmd.PersistentFlags().StringVar(&cfgFile, "config", "", "config file (default is the project directory ./config.toml)")

	// Cobra also supports local flags, which will only run
	// when this action is called directly.
	// rootCmd.Flags().BoolP("toggle", "t", false, "Help message for toggle")
}

func isValidHex(str string) (bool, error) {
	// starts with "0x"
	if strings.HasPrefix(str, "0x") {
		if len(str) == 2 {
			return false, fmt.Errorf("Not a valid hex string: arguments error: please check your inpunt: %s%s", str, info)
		}
		// is hex string
		_, err := hexutil.Decode(str)
		if err != nil {
			return false, fmt.Errorf("Not a valid hex string: arguments error: please check your inpunt: %s%s: %v", str, info, err)
		}
		return true, nil
	}
	return false, fmt.Errorf("Arguments error: Not a valid hex string, please check your inpunt: %s%s", str, info)
}

func isBlockNumberOutOfRange(blockNumber int64) (bool, error) {
	// compare with the current block number
	currentBlockNumber, err := RPC.GetBlockNumber(context.Background())
	if err != nil {
		return false, fmt.Errorf("Client error: cannot get the block number: %v", err)
	}
	if currentBlockNumber < blockNumber {
		return false, fmt.Errorf("BlockNumber does not exist")
	}
	return true, nil
}
