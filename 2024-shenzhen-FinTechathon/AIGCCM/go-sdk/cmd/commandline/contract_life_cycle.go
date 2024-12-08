package commandline

import (
	"fmt"

	"github.com/FISCO-BCOS/go-sdk/precompiled/contractlifecycle"
	"github.com/ethereum/go-ethereum/common"

	"github.com/spf13/cobra"
)

var freezeContract = &cobra.Command{
	Use:   "freezeContract",
	Short: "[contractAddress]                  Freeze the contract",
	Long: `Freeze the contract.
Arguments:
	  [contractAddress]:

For example:

    [freezeContract] [0x0a68F060B46e0d8f969383D260c34105EA13a9dd]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#freezecontract`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		contractAddress := args[0]
		if !IsValidAccount(contractAddress) {
			fmt.Printf("the format of contractAddress %v is unvalid\n", contractAddress)
			return
		}
		contractLifeCycleService, err := contractlifecycle.NewService(RPC)
		if err != nil {
			fmt.Printf("freezeContract failed, contractlifecycle.NewService err: %v\n", err)
			return
		}
		result, err := contractLifeCycleService.Freeze(common.HexToAddress(contractAddress))
		if err != nil {
			fmt.Printf("freezeContract failed, contractLifeCycleService.Freeze err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("freezeContract failed, the result is: %v", result)
			return
		}
		fmt.Println("success")
	},
}

var unfreezeContract = &cobra.Command{
	Use:   "unfreezeContract",
	Short: "[contractAddress]                  Unfreeze the contract",
	Long: `Unfreeze the contract.
Arguments:
	  [contractAddress]:

For example:

    [unfreezeContract] [0x0a68F060B46e0d8f969383D260c34105EA13a9dd]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#unfreezecontract`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		contractAddress := args[0]
		if !IsValidAccount(contractAddress) {
			fmt.Printf("the format of contractAddress %v is unvalid\n", contractAddress)
			return
		}
		contractLifeCycleService, err := contractlifecycle.NewService(RPC)
		if err != nil {
			fmt.Printf("unfreezeContract failed, contractlifecycle.NewService err: %v\n", err)
			return
		}
		result, err := contractLifeCycleService.Unfreeze(common.HexToAddress(contractAddress))
		if err != nil {
			fmt.Printf("unfreezeContract failed, contractLifeCycleService.Unfreeze err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("unfreezeContract failed, the result is: %v", result)
			return
		}
		fmt.Println("success")
	},
}

var grantContractStatusManager = &cobra.Command{
	Use:   "grantContractStatusManager",
	Short: "[contractAddress] [accountAddress] Grant contract authorization to the user",
	Long: `Grant contract authorization to the user.
Arguments:
	  [contractAddress]:
      [accountAddress]:

For example:

    [grantContractStatusManager] [0x0a68F060B46e0d8f969383D260c34105EA13a9dd] [0xae66fbe9ee2b5007e245d98bf7cf9904cc61e394]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#grantcontractstatusmanager`,
	Args: cobra.ExactArgs(2),
	Run: func(cmd *cobra.Command, args []string) {
		contractAddress := args[0]
		if !IsValidAccount(contractAddress) {
			fmt.Printf("the format of contractAddress %v is unvalid\n", contractAddress)
			return
		}
		if !IsValidAccount(contractAddress) {
			fmt.Printf("the format of contractAddress %v is unvalid\n", contractAddress)
			return
		}
		accountAddress := args[1]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		contractLifeCycleService, err := contractlifecycle.NewService(RPC)
		if err != nil {
			fmt.Printf("grantContractStatusManager failed, contractlifecycle.NewService err: %v\n", err)
			return
		}
		result, err := contractLifeCycleService.GrantManager(common.HexToAddress(contractAddress), common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("grantContractStatusManager failed, contractLifeCycleService.GrantManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("grantContractStatusManager failed, the result is: %v", result)
			return
		}
		fmt.Println("success")
	},
}

var getContractStatus = &cobra.Command{
	Use:   "getContractStatus",
	Short: "[contractAddress]                  Get the status of the contract",
	Long: `Get the status of the contract, whether the contract is frozen.
Arguments:
	  [contractAddress]:

For example:

    [getContractStatus] [0x0a68F060B46e0d8f969383D260c34105EA13a9dd]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#getcontractstatus`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		contractAddress := args[0]
		if !IsValidAccount(contractAddress) {
			fmt.Printf("the format of contractAddress %v is unvalid\n", contractAddress)
			return
		}
		contractLifeCycleService, err := contractlifecycle.NewService(RPC)
		if err != nil {
			fmt.Printf("getContractStatus failed, contractlifecycle.NewService err: %v\n", err)
			return
		}
		num, status, err := contractLifeCycleService.GetStatus(common.HexToAddress(contractAddress))
		if err != nil {
			fmt.Printf("getContractStatus failed, contractLifeCycleService.GetStatus err: %v\n", err)
			return
		}
		if num != 0 {
			fmt.Printf("getContractStatus failed, the num is: %v, the status is: %v", num, status)
			return
		}
		fmt.Println(status)
	},
}

var listContractStatusManager = &cobra.Command{
	Use:   "listContractStatusManager",
	Short: "[contractAddress]                  List the authorization of the contract",
	Long: `List managers that have the permission to manage contract.
Arguments:
	  [contractAddress]:

For example:

    [listContractStatusManager] [0x0a68F060B46e0d8f969383D260c34105EA13a9dd]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#listcontractstatusmanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		contractAddress := args[0]
		if !IsValidAccount(contractAddress) {
			fmt.Printf("the format of contractAddress %v is unvalid\n", contractAddress)
			return
		}
		contractLifeCycleService, err := contractlifecycle.NewService(RPC)
		if err != nil {
			fmt.Printf("listContractStatusManager failed, contractlifecycle.NewService err: %v\n", err)
			return
		}
		num, managers, err := contractLifeCycleService.ListManager(common.HexToAddress(contractAddress))
		if err != nil {
			fmt.Printf("listContractStatusManager failed, contractLifeCycleService.ListManager err: %v\n", err)
			return
		}
		if num != 0 {
			fmt.Printf("listContractStatusManager failed, the num is: %v, the managers is: %v", num, managers)
			return
		}
		jsonStr, err := ListToJSONStr(managers, "managers")
		if err != nil {
			fmt.Printf("listDeployAndCreateManager failed, ListToJsonStr err: %v\n", err)
			return
		}
		fmt.Println(jsonStr)
	},
}

func init() {
	rootCmd.AddCommand(freezeContract, unfreezeContract, grantContractStatusManager, getContractStatus, listContractStatusManager)
}
