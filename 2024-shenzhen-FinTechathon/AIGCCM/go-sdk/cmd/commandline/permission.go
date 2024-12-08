package commandline

import (
	"fmt"

	"github.com/FISCO-BCOS/go-sdk/client"
	"github.com/FISCO-BCOS/go-sdk/precompiled/permission"
	"github.com/ethereum/go-ethereum/common"
	"github.com/spf13/cobra"
)

var grantUserTableManager = &cobra.Command{
	Use:   "grantUserTableManager",
	Short: "[tableName]   [accountAddress]     Grant manager permission of user table",
	Long: `Grant manager permission of user table. After the user obtains this permission, the user can write to the table.
Arguments:
          [tableName]: string
          [accountAddress]: string

For example:

    [grantUserTableManager] [t_test] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#grantusertablemanager`,
	Args: cobra.ExactArgs(2),
	Run: func(cmd *cobra.Command, args []string) {
		tableName := args[0]
		accountAddress := args[1]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("grantUserTableManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.GrantUserTableManager(tableName, common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("grantUserTableManager failed, permissionService.GrantUserTableManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("grantUserTableManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var revokeUserTableManager = &cobra.Command{
	Use:   "revokeUserTableManager",
	Short: "[tableName]   [accountAddress]     Revoke manager permission of user table",
	Long: `Revoke manager permission of user table. After the user is revoked this permission, the user can't write to the table.
Arguments:
          [tableName]: string
          [accountAddress]: string

For example:

    [revokeUserTableManager] [t_test] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#revokeusertablemanager`,
	Args: cobra.ExactArgs(2),
	Run: func(cmd *cobra.Command, args []string) {
		tableName := args[0]
		accountAddress := args[1]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("revokeUserTableManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.RevokeUserTableManager(tableName, common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("revokeUserTableManager failed, permissionService.RevokeUserTableManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("revokeUserTableManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var listUserTableManager = &cobra.Command{
	Use:   "listUserTableManager",
	Short: "[tableName]                        List all managers of user table",
	Long: `List all managers in user table.
Arguments:
          [tableName]: string

For example:

    [listUserTableManager] [t_test]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#listusertablemanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		tableName := args[0]
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("listUserTableManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		managers, err := permissionService.ListUserTableManager(tableName)
		if err != nil {
			fmt.Printf("listUserTableManager failed, permissionService.ListUserTableManager err: %v\n", err)
			return
		}
		jsonStr, err := ListToJSONStr(managers, "user table managers")
		if err != nil {
			fmt.Printf("listUserTableManager failed, ListToJsonStr err: %v\n", err)
			return
		}
		fmt.Println(jsonStr)
	},
}

var grantDeployAndCreateManager = &cobra.Command{
	Use:   "grantDeployAndCreateManager",
	Short: "[accountAddress]                   Grant permission for deploy contract and create user table by address",
	Long: `Grant permission for deploy contract and create user table by address.
Arguments:
          [accountAddress]: string

For example:

    [grantDeployAndCreateManager] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#grantdeployandcreatemanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("grantDeployAndCreateManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.GrantDeployAndCreateManager(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("grantDeployAndCreateManager failed, permissionService.GrantDeployAndCreateManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("grantDeployAndCreateManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var revokeDeployAndCreateManager = &cobra.Command{
	Use:   "revokeDeployAndCreateManager",
	Short: "[accountAddress]                   Revoke permission for deploy contract and create user table by address",
	Long: `Revoke permission for deploy contract and create user table by address.
Arguments:
         [accountAddress]: string

For example:

    [revokeDeployAndCreateManager] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#revokedeployandcreatemanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("revokeDeployAndCreateManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.RevokeDeployAndCreateManager(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("revokeDeployAndCreateManager failed, permissionService.RevokeDeployAndCreateManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("revokeDeployAndCreateManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var listDeployAndCreateManager = &cobra.Command{
	Use:   "listDeployAndCreateManager",
	Short: "                                   Query permission information for deploy contract and create user table",
	Long: `Query permission information for deploy contract and create user table.
For example:

    [listDeployAndCreateManager]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#listdeployandcreatemanager`,
	Args: cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("listDeployAndCreateManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		managers, err := permissionService.ListDeployAndCreateManager()
		if err != nil {
			fmt.Printf("listDeployAndCreateManager failed, permissionService.ListDeployAndCreateManager err: %v\n", err)
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

var grantPermissionManager = &cobra.Command{
	Use:   "grantPermissionManager",
	Short: "[accountAddress]                   Grant permission to manage blockchain by address",
	Long: `Grant permission to manage blockchain by address.
Arguments:
          [accountAddress]: string

For example:

    [grantPermissionManager] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#grantpermissionmanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		if RPC.GetCompatibleVersion() >= client.V2_5_0 {
			fmt.Println("this command only supports versions less than 2.5.0")
			return
		}
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("grantPermissionManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.GrantPermissionManager(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("grantPermissionManager failed, permissionService.GrantPermissionManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("grantPermissionManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var revokePermissionManager = &cobra.Command{
	Use:   "revokePermissionManager",
	Short: "[accountAddress]                   Revoke permission to manage blockchain by address",
	Long: `Revoke permission to manage blockchain by address.
Arguments:
        [accountAddress]: string

For example:

    [revokePermissionManager] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

  	https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#revokepermissionmanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		if RPC.GetCompatibleVersion() >= client.V2_5_0 {
			fmt.Println("this command only supports versions less than 2.5.0")
			return
		}
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("revokePermissionManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.RevokePermissionManager(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("revokePermissionManager failed, permissionService.RevokePermissionManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("revokePermissionManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var listPermissionManager = &cobra.Command{
	Use:   "listPermissionManager",
	Short: "                                   Query permission information that can manage blockchain",
	Long: `Query permission information that can manage blockchain.
For example:

    [listPermissionManager]

For more information please refer:

  	https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#revokepermissionmanager`,
	Args: cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("listPermissionManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		managers, err := permissionService.ListPermissionManager()
		if err != nil {
			fmt.Printf("listPermissionManager failed, permissionService.ListPermissionManager err: %v\n", err)
			return
		}
		jsonStr, err := ListToJSONStr(managers, "managers")
		if err != nil {
			fmt.Printf("listPermissionManager failed, ListToJsonStr err: %v\n", err)
			return
		}
		fmt.Println(jsonStr)
	},
}

var grantNodeManager = &cobra.Command{
	Use:   "grantNodeManager",
	Short: "[accountAddress]                   Grant permission for node configuration by address",
	Long: `Grant permission for node configuration by address.
Arguments:
          [accountAddress]: string

For example:

    [grantNodeManager] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#grantnodemanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("grantNodeManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.GrantNodeManager(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("grantNodeManager failed, permissionService.GrantNodeManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("grantNodeManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var revokeNodeManager = &cobra.Command{
	Use:   "revokeNodeManager",
	Short: "[accountAddress]                   Revoke permission for node configuration by address",
	Long: `Revoke permission for node configuration by address.
Arguments:
       [accountAddress]: string

For example:

    [revokeNodeManager] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

 	https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#revokenodemanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("revokeNodeManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.RevokeNodeManager(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("revokeNodeManager failed, permissionService.RevokeNodeManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("revokeNodeManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var listNodeManager = &cobra.Command{
	Use:   "listNodeManager",
	Short: "                                   Query permission information for node configuration",
	Long: `Query permission information for node configuration.
For example:

 	[listNodeManager]

For more information please refer:

 	https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#listnodemanager`,
	Args: cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("listNodeManager failed, permission.NewPermissionService err:%v", err)
			return
		}
		managers, err := permissionService.ListNodeManager()
		if err != nil {
			fmt.Printf("listNodeManager failed, permissionService.ListNodeManager err: %v", err)
			return
		}
		jsonStr, err := ListToJSONStr(managers, "managers")
		if err != nil {
			fmt.Printf("listNodeManager failed, ListToJsonStr err: %v", err)
			return
		}
		fmt.Println(jsonStr)
	},
}

var grantCNSManager = &cobra.Command{
	Use:   "grantCNSManager",
	Short: "[accountAddress]                   Grant permission for CNS by address",
	Long: `Grant permission for CNS by address.
Arguments:
          [accountAddress]: string

For example:

    [grantCNSManager] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#grantcnsmanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("grantCNSManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.GrantCNSManager(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("grantCNSManager failed, permissionService.GrantCNSManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("grantCNSManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var revokeCNSManager = &cobra.Command{
	Use:   "revokeCNSManager",
	Short: "[accountAddress]                   Revoke permission for CNS by address",
	Long: `Revoke permission for CNS by address.
Arguments:
      [accountAddress]: string

For example:

	[revokeCNSManager] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

	https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#revokenodemanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("revokeCNSManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.RevokeCNSManager(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("revokeCNSManager failed, permissionService.RevokeCNSManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("revokeCNSManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var listCNSManager = &cobra.Command{
	Use:   "listCNSManager",
	Short: "                                   Query permission information for CNS",
	Long: `Query permission information for CNS.
For example:

	[listCNSManager]

For more information please refer:

	https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#listcnsmanager`,
	Args: cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("listCNSManager failed, permission.NewPermissionService err:%v", err)
			return
		}
		managers, err := permissionService.ListCNSManager()
		if err != nil {
			fmt.Printf("listCNSManager failed, permissionService.ListCNSManager err: %v", err)
			return
		}
		jsonStr, err := ListToJSONStr(managers, "managers")
		if err != nil {
			fmt.Printf("listCNSManager failed, ListToJsonStr err: %v", err)
			return
		}
		fmt.Println(jsonStr)
	},
}

var grantSysConfigManager = &cobra.Command{
	Use:   "grantSysConfigManager",
	Short: "[accountAddress]                   Grant permission for system configuration by address",
	Long: `Grant permission for system configuration by address.
Arguments:
          [accountAddress]: string

For example:

    [grantSysConfigManager] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#grantsysconfigmanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("grantSysConfigManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.GrantSysConfigManager(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("grantSysConfigManager failed, permissionService.GrantSysConfigManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("grantSysConfigManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var revokeSysConfigManager = &cobra.Command{
	Use:   "revokeSysConfigManager",
	Short: "[accountAddress]                   Revoke permission for system configuration by address",
	Long: `Revoke permission for system configuration by address.
Arguments:
     [accountAddress]: string

For example:

	[revokeSysConfigManager] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]

For more information please refer:

	https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#revokesysconfigmanager`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("revokeSysConfigManager failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.RevokeSysConfigManager(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("revokeSysConfigManager failed, permissionService.RevokeSysConfigManager err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("revokeSysConfigManager failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var listSysConfigManager = &cobra.Command{
	Use:   "listSysConfigManager",
	Short: "                                   Query permission information for system configuration",
	Long: ` Query permission information for system configuration.
For example:

	[listSysConfigManager]

For more information please refer:

	https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#listsysconfigmanager`,
	Args: cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("listSysConfigManager failed, permission.NewPermissionService err:%v", err)
			return
		}
		managers, err := permissionService.ListSysConfigManager()
		if err != nil {
			fmt.Printf("listSysConfigManager failed, permissionService.ListSysConfigManager err: %v", err)
			return
		}
		jsonStr, err := ListToJSONStr(managers, "managers")
		if err != nil {
			fmt.Printf("listSysConfigManager failed, ListToJsonStr err: %v", err)
			return
		}
		fmt.Println(jsonStr)
	},
}

var grantContractWritePermission = &cobra.Command{
	Use:   "grantContractWritePermission",
	Short: "[contractAddress] [accountAddress] Grant permission for write contract by address",
	Long: `Grant permission for system configuration by address.
Arguments:
          [contractAddress]: string
          [accountAddress]: string

For example:

    [grantContractWritePermission] [0xBEAB43be5B1Cc361495D61A5f53e4AC01ecac353] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]
`,
	Args: cobra.ExactArgs(2),
	Run: func(cmd *cobra.Command, args []string) {
		contractAddress := args[0]
		if !IsValidAccount(contractAddress) {
			fmt.Printf("the format of contractAddress %v is unvalid\n", contractAddress)
			return
		}
		accountAddress := args[1]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("grantWrite failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.GrantContractWritePermission(common.HexToAddress(contractAddress), common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("grantWrite failed, permissionService.GrantWrite err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("grantWrite failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var revokeContractWritePermission = &cobra.Command{
	Use:   "revokeContractWritePermission",
	Short: "[contractAddress] [accountAddress] Revoke permission for write contract by address",
	Long: `Revoke permission for write contract by address.
Arguments:
          [contractAddress]: string
          [accountAddress]: string

For example:

	[revokeContractWritePermission] [0xBEAB43be5B1Cc361495D61A5f53e4AC01ecac353] [0xFbb18d54e9Ee57529cda8c7c52242EFE879f064F]
`,
	Args: cobra.ExactArgs(2),
	Run: func(cmd *cobra.Command, args []string) {
		contractAddress := args[0]
		if !IsValidAccount(contractAddress) {
			fmt.Printf("the format of contractAddress %v is unvalid\n", contractAddress)
			return
		}
		accountAddress := args[1]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("revokeWrite failed, permission.NewPermissionService err:%v\n", err)
			return
		}
		result, err := permissionService.RevokeContractWritePermission(common.HexToAddress(contractAddress), common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("revokeWrite failed, permissionService.RevokeWrite err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Println("revokeWrite failed")
			return
		}
		fmt.Println(DefaultSuccessMessage)
	},
}

var listContractWritePermission = &cobra.Command{
	Use:   "listContractWritePermission",
	Short: "[contractAddress]                  Query permission for write contract by address",
	Long: `Query permission for write contract by address.
Arguments:
          [contractAddress]: string

For example:

	[listContractWritePermission] [0xBEAB43be5B1Cc361495D61A5f53e4AC01ecac353]
`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		contractAddress := args[0]
		if !IsValidAccount(contractAddress) {
			fmt.Printf("the format of contractAddress %v is unvalid\n", contractAddress)
			return
		}
		permissionService, err := permission.NewPermissionService(RPC)
		if err != nil {
			fmt.Printf("queryPermission failed, permission.NewPermissionService err:%v", err)
			return
		}
		managers, err := permissionService.ListContractWritePermission(common.HexToAddress(contractAddress))
		if err != nil {
			fmt.Printf("queryPermission failed, permissionService.QueryPermission err: %v", err)
			return
		}
		jsonStr, err := ListToJSONStr(managers, "managers")
		if err != nil {
			fmt.Printf("queryPermission failed, ListToJsonStr err: %v", err)
			return
		}
		fmt.Println(jsonStr)
	},
}

func init() {
	// the permission of manager user table
	rootCmd.AddCommand(grantUserTableManager, revokeUserTableManager, listUserTableManager)
	// the permission of deploy contract and create user table
	rootCmd.AddCommand(grantDeployAndCreateManager, revokeDeployAndCreateManager, listDeployAndCreateManager)
	// the permission of blockchain manager
	rootCmd.AddCommand(grantPermissionManager, revokePermissionManager, listPermissionManager)
	// the permission of node configuration
	rootCmd.AddCommand(grantNodeManager, revokeNodeManager, listNodeManager)
	// the permission of CNS
	rootCmd.AddCommand(grantCNSManager, revokeCNSManager, listCNSManager)
	// the permission of system configuration
	rootCmd.AddCommand(grantSysConfigManager, revokeSysConfigManager, listSysConfigManager)
	// the permission of writing contract
	rootCmd.AddCommand(grantContractWritePermission, revokeContractWritePermission, listContractWritePermission)
}
