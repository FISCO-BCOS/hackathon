package commandline

import (
	"fmt"
	"strconv"

	"github.com/FISCO-BCOS/go-sdk/precompiled/chaingovernance"
	"github.com/ethereum/go-ethereum/common"
	"github.com/spf13/cobra"
)

var grantCommitteeMember = &cobra.Command{
	Use:   "grantCommitteeMember",
	Short: "[accountAddress]                   Grant a committee member",
	Long: `Grant the permission of committee for a user account, which can add and
delete committee members,modify the weight of the committee members, modify the
effective voting threshold, add and delete nodes, modify chain configuration items,
freeze and unfreeze contracts, freeze and unfreeze accounts,add and cancel operator
accounts, and write permissions for user tables.
Arguments:
          [accountAddress]: string

For example:

    [grantCommitteeMember] [0x83309d045a19c44dc3722d15a6abd472f95866ac]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#grantcommitteemember`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("grantCommitteeMember failed, chaingovernance.NewService err, err: %v\n", err)
			return
		}
		result, err := chainGovernanceService.GrantCommitteeMember(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("grantCommitteeMember failed, chainGovernanceService.GrantCommitteeMember err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("grantCommitteeMember failed, the result is: %v\n", result)
			return
		}
		fmt.Println("success")
	},
}

var revokeCommitteeMember = &cobra.Command{
	Use:   "revokeCommitteeMember",
	Short: "[accountAddress]                   Revoke a committee member",
	Long: `Revoke the permission of committee for a user account.
Arguments:
          [accountAddress]: string

For example:

    [revokeCommitteeMember] [0x83309d045a19c44dc3722d15a6abd472f95866ac]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#revokecommitteemember`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("revokeCommitteeMember failed, chaingovernance.NewService err: %v\n", err)
			return
		}
		result, err := chainGovernanceService.RevokeCommitteeMember(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("revokeCommitteeMember failed, chainGovernanceService.RevokeCommitteeMember err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("revokeCommitteeMember failed, the result is: %v\n", result)
			return
		}
		fmt.Println("success")
	},
}

var listCommitteeMembers = &cobra.Command{
	Use:   "listCommitteeMembers",
	Short: "                                   List all committee members",
	Long: `List all committee members in blockchain.
For example:

    [listCommitteeMembers]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#listcommitteemembers`,
	Args: cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("listCommitteeMembers failed, chaingovernance.NewService err: %v", err)
			return
		}
		committeeMembers, err := chainGovernanceService.ListCommitteeMembers()
		if err != nil {
			fmt.Printf("listCommitteeMembers failed, chainGovernanceService.ListCommitteeMembers err: %v", err)
			return
		}
		jsonStr, err := ListToJSONStr(committeeMembers, "committee_members")
		if err != nil {
			fmt.Printf("listCommitteeMembers failed, ListToJsonStr err: %v\n", err)
			return
		}
		fmt.Println(jsonStr)
	},
}

var queryCommitteeMemberWeight = &cobra.Command{
	Use:   "queryCommitteeMemberWeight",
	Short: "[accountAddress]                   Query committee member weight",
	Long: `query the weight of the committee member.
Arguments:
          [accountAddress]: string

For example:

    [queryCommitteeMemberWeight] [0x83309d045a19c44dc3722d15a6abd472f95866ac]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#querycommitteememberweight`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("queryCommitteeMemberWeight failed, chaingovernance.NewService err: %v\n", err)
			return
		}
		result, err := chainGovernanceService.QueryCommitteeMemberWeight(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("queryCommitteeMemberWeight failed, chainGovernanceService.QueryCommitteeMemberWeight err: %v\n", err)
			return
		}
		fmt.Printf("success, the weight %v is %v\n", accountAddress, result)
	},
}

var updateCommitteeMemberWeight = &cobra.Command{
	Use:   "updateCommitteeMemberWeight",
	Short: "[accountAddress] [weight]          Update committee member weight",
	Long: `update the weight of the committee member.
Arguments:
          [accountAddress]: string
                  [weight]: must be positive integer.

For example:

    [updateCommitteeMemberWeight] [0x83309d045a19c44dc3722d15a6abd472f95866ac] [2]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#updatecommitteememberweight`,
	Args: cobra.ExactArgs(2),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		num, err := strconv.Atoi(args[1])
		if err != nil {
			fmt.Println("weight should be integer")
			return
		}
		if num < 1 {
			fmt.Println("updateCommitteeMemberWeight failed, weight must be positive integer")
			return
		}
		weight := uint64(num)
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("updateCommitteeMemberWeight failed, chaingovernance.NewService err: %v\n", err)
			return
		}
		result, err := chainGovernanceService.UpdateCommitteeMemberWeight(common.HexToAddress(accountAddress), weight)
		if err != nil {
			fmt.Printf("updateCommitteeMemberWeight failed, chainGovernanceService.UpdateCommitteeMemberWeight err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("updateCommitteeMemberWeight failed, the result is: %v\n", result)
			return
		}
		fmt.Println("success")
	},
}

var updateThreshold = &cobra.Command{
	Use:   "updateThreshold",
	Short: "[threshold]                        Update the threshold",
	Long: `Update voting effective threshold.
Arguments:
          [threshold]: must be in the range of [0, 100).

For example:

    [updateThreshold] [3]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#updatethreshold`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		num, err := strconv.Atoi(args[0])
		if err != nil {
			fmt.Println("threshold should be integer")
			return
		}
		if num < 0 || num > 99 {
			fmt.Println("updateThreshold failed, threshold must be in the range of [0, 100)")
			return
		}
		threshold := uint64(num)
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("updateThreshold failed, chaingovernance.NewService err: %v\n", err)
			return
		}
		result, err := chainGovernanceService.UpdateThreshold(threshold)
		if err != nil {
			fmt.Printf("updateThreshold failed, chainGovernanceService.UpdateThreshold err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("updateThreshold failed, the result is: %v\n", result)
			return
		}
		fmt.Println("success")
	},
}

var queryThreshold = &cobra.Command{
	Use:   "queryThreshold",
	Short: "                                   Query the threshold",
	Long: `Query the effective threshold of voting.
For example:

    [queryThreshold]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#querythreshold`,
	Args: cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("queryThreshold failed, chaingovernance.NewService err: %v\n", err)
			return
		}
		result, err := chainGovernanceService.QueryThreshold()
		if err != nil {
			fmt.Printf("queryThreshold failed, chainGovernanceService.QueryThreshold err: %v\n", err)
			return
		}
		fmt.Printf("success, the effective threshold of voting is %v\n", result)
	},
}

var grantOperator = &cobra.Command{
	Use:   "grantOperator",
	Short: "                                   Grant the account operator",
	Long: `Add an operator account, and the operator role has the permissions to deploy contracts, create user tables, and manage CNS.
Arguments:
          [accountAddress]: string

For example:

    [grantOperator] [0x112fb844934c794a9e425dd6b4e57eff1b519f17]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#grantoperator`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("grantOperator failed, chaingovernance.NewService err, err: %v\n", err)
			return
		}
		result, err := chainGovernanceService.GrantOperator(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("grantOperator failed, chainGovernanceService.GrantOperator err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("grantOperator failed, the result is: %v\n", result)
			return
		}
		fmt.Println("success")
	},
}

var revokeOperator = &cobra.Command{
	Use:   "revokeOperator",
	Short: "[accountAddress]                   Revoke the operator",
	Long: `Revoke the permission of operator.
Arguments:
         [accountAddress]: string

For example:

    [revokeOperator] [0x112fb844934c794a9e425dd6b4e57eff1b519f17]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#revokeoperator`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("revokeOperator failed, chaingovernance.NewService err: %v\n", err)
			return
		}
		result, err := chainGovernanceService.RevokeOperator(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("revokeOperator failed, chainGovernanceService.RevokeOperator err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("revokeOperator failed, the result is: %v\n", result)
			return
		}
		fmt.Println("success")
	},
}

var listOperators = &cobra.Command{
	Use:   "listOperators",
	Short: "                                   List all operators",
	Long: `List all operators.
For example:

    [listOperators]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#listoperators`,
	Args: cobra.NoArgs,
	Run: func(cmd *cobra.Command, args []string) {
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("listOperators failed, chaingovernance.NewService err: %v", err)
			return
		}
		operators, err := chainGovernanceService.ListOperators()
		if err != nil {
			fmt.Printf("listOperators failed, chainGovernanceService.ListOperators err: %v", err)
			return
		}
		jsonStr, err := ListToJSONStr(operators, "operators")
		if err != nil {
			fmt.Printf("listOperators failed, ListToJsonStr err: %v\n", err)
			return
		}
		fmt.Println(jsonStr)
	},
}

var freezeAccount = &cobra.Command{
	Use:   "freezeAccount",
	Short: "[accountAddress]                   Freeze the account",
	Long: `Freeze the specified account. For accounts that have not sent transactions,
the freezing operation will prompt that the account address does not exist.
Arguments:
         [accountAddress]: string

For example:

    [freezeAccount] [0x112fb844934c794a9e425dd6b4e57eff1b519f17]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#freezeaccount`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("freezeAccount failed, chaingovernance.NewService err: %v\n", err)
			return
		}
		result, err := chainGovernanceService.FreezeAccount(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("freezeAccount failed, chainGovernanceService.FreezeAccount err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("freezeAccount failed, the result is: %v\n", result)
			return
		}
		fmt.Println("success")
	},
}

var unfreezeAccount = &cobra.Command{
	Use:   "unfreezeAccount",
	Short: "[accountAddress]                   Unfreeze the account",
	Long: `Unfreeze the specified account. For accounts that have not sent transactions,
the unfreezing operation will prompt that the account address does not exist.
Arguments:
         [accountAddress]: string

For example:

    [unfreezeAccount] [0x112fb844934c794a9e425dd6b4e57eff1b519f17]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#unfreezeaccount`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("unfreezeAccount failed, chaingovernance.NewService err: %v\n", err)
			return
		}
		result, err := chainGovernanceService.UnfreezeAccount(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("unfreezeAccount failed, chainGovernanceService.UnfreezeAccount err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("unfreezeAccount failed, the result is: %v\n", result)
			return
		}
		fmt.Println("success")
	},
}

var getAccountStatus = &cobra.Command{
	Use:   "getAccountStatus",
	Short: "[accountAddress]                   Get status of the account",
	Long: `Get the user account status and determine whether the account is frozen.
Arguments:
         [accountAddress]: string

For example:

    [getAccountStatus] [0x112fb844934c794a9e425dd6b4e57eff1b519f17]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#getaccountstatus`,
	Args: cobra.ExactArgs(1),
	Run: func(cmd *cobra.Command, args []string) {
		accountAddress := args[0]
		if !IsValidAccount(accountAddress) {
			fmt.Printf("the format of accountAddress %v is unvalid\n", accountAddress)
			return
		}
		chainGovernanceService, err := chaingovernance.NewService(RPC)
		if err != nil {
			fmt.Printf("getAccountStatus failed, chaingovernance.NewService err: %v\n", err)
			return
		}
		result, err := chainGovernanceService.GetAccountStatus(common.HexToAddress(accountAddress))
		if err != nil {
			fmt.Printf("getAccountStatus failed, chainGovernanceService.GetAccountStatus err: %v\n", err)
			return
		}
		fmt.Println(result)
	},
}

func init() {
	rootCmd.AddCommand(grantCommitteeMember, revokeCommitteeMember, listCommitteeMembers, queryCommitteeMemberWeight, updateCommitteeMemberWeight)
	rootCmd.AddCommand(updateThreshold, queryThreshold, grantOperator, revokeOperator, listOperators)
	rootCmd.AddCommand(freezeAccount, unfreezeAccount, getAccountStatus)
}
