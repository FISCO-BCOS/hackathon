package commandline

import (
	"fmt"

	"github.com/FISCO-BCOS/go-sdk/precompiled/consensus"
	"github.com/spf13/cobra"
)

var addObserver = &cobra.Command{
	Use:   "addObserver",
	Short: "[nodeID]                           Add an observer node",
	Long: `Add an observer node from sealer list or free node list.
Arguments:
          [nodeID]: string

For example:

    [addObserver] [67f01658fe24d9cc24dce0af580a4646b8e4a229d9cb7f445b16253232a0f4013426ca16d587b610bc0c70a1f741ce7448abef58d98ef73557b669de29fa3f26]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#addobserver`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		nodeID := args[0]
		consensusService, err := consensus.NewConsensusService(RPC)
		if err != nil {
			fmt.Printf("addObserver failed, consensus.NewConsensusService err: %v\n", err)
			return
		}
		result, err := consensusService.AddObserver(nodeID)
		if err != nil {
			fmt.Printf("addObserver failed, consensusService.AddObserver err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("addObserver failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var addSealer = &cobra.Command{
	Use:   "addSealer",
	Short: "[nodeID]                           Add a sealer node",
	Long: `Add a sealer node from observer list in group.
Arguments:
          [nodeID]: string

For example:

    [addSealer] [67f01658fe24d9cc24dce0af580a4646b8e4a229d9cb7f445b16253232a0f4013426ca16d587b610bc0c70a1f741ce7448abef58d98ef73557b669de29fa3f26]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#addsealer`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		nodeID := args[0]
		consensusService, err := consensus.NewConsensusService(RPC)
		if err != nil {
			fmt.Printf("addSealer failed, consensus.NewConsensusService err: %v\n", err)
			return
		}
		result, err := consensusService.AddSealer(nodeID)
		if err != nil {
			fmt.Printf("addSealer failed, consensusService.AddSealer err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("addSealer failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var removeNode = &cobra.Command{
	Use:   "removeNode",
	Short: "[nodeID]                           Remove a node",
	Long: `Remove a node from sealer list or observer list in group.
Arguments:
          [nodeID]: string

For example:

    [removeNode] [67f01658fe24d9cc24dce0af580a4646b8e4a229d9cb7f445b16253232a0f4013426ca16d587b610bc0c70a1f741ce7448abef58d98ef73557b669de29fa3f26]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#removenode`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		nodeID := args[0]
		consensusService, err := consensus.NewConsensusService(RPC)
		if err != nil {
			fmt.Printf("removeNode failed, consensus.NewConsensusService err:%v\n", err)
			return
		}
		result, err := consensusService.RemoveNode(nodeID)
		if err != nil {
			fmt.Printf("removeNode failed, consensusService.RemoveNode err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("removeNode failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

func init() {
	rootCmd.AddCommand(addObserver, addSealer, removeNode)
}
