package commandline

import (
	"fmt"

	"github.com/FISCO-BCOS/go-sdk/precompiled/cns"
	"github.com/spf13/cobra"
)

var queryCNS = &cobra.Command{
	Use:   "queryCNS",
	Short: "[name] [version]                   Query CNS information by contract name and contract version",
	Long: `Query CNS information by contract name and contract version.
Arguments:
          [name]: string
          [version]: string

For example:

    [queryCNS] [HelloWorld] [1.0]

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#querycns`,
	Args: cobra.RangeArgs(1, 2),
	Run: func(cmd *cobra.Command, args []string) {
		var contractName string
		var version string
		var infos []cns.Info
		var err error
		cnsService, err := cns.NewCnsService(RPC)
		if err != nil {
			fmt.Printf("queryCNS failed, cns.NewCnsService err: %v\n", err)
			return
		}
		contractName = args[0]
		if len(args) == 1 {
			infos, err = cnsService.QueryCnsByName(contractName)
		} else {
			version = args[1]
			infos, err = cnsService.QueryCnsByNameAndVersion(contractName, version)
		}
		if err != nil {
			fmt.Printf("queryCNS failed, cnsService.QueryCnsByName or cnsService.QueryCnsByNameAndVersion err: %v\n", err)
			return
		}
		if len(args) == 1 && len(infos) == 0 {
			fmt.Printf("contract %v is not existed\n", args[0])
		} else if len(args) == 2 && len(infos) == 0 {
			fmt.Printf("contract %v, version %v is not existed\n", args[0], args[1])
		}
		// abi info is not printed
		for i := 0; i < len(infos); i++ {
			fmt.Println("name: " + infos[i].Name + ", version: " + infos[i].Version + ", address: " + infos[i].Address)
		}
	},
}

var getAddressByContractNameAndVersion = &cobra.Command{
	Use:   "getAddressByContractNameAndVersion",
	Short: "[name] [version]                   Get address by contract name and version",
	Long: `Get address by contract name and version.
Arguments:
          [name]: string
          [version]: string

For example:

    [getAddressByContractNameAndVersion] [HelloWorld] [1.0]
`,
	Args: cobra.ExactArgs(2),
	Run: func(cmd *cobra.Command, args []string) {
		var contractName string
		var version string
		var err error
		cnsService, err := cns.NewCnsService(RPC)
		if err != nil {
			fmt.Printf("getAddressByContractNameAndVersion failed, cns.NewCnsService err: %v\n", err)
			return
		}
		contractName = args[0]
		version = args[1]
		address, err := cnsService.GetAddressByContractNameAndVersion(contractName, version)
		if err != nil {
			fmt.Printf("getAddressByContractNameAndVersion failed, cnsService.GetAddressByContractNameAndVersion err: %v\n", err)
			return
		}
		fmt.Println(address.Hex())
	},
}

func init() {
	rootCmd.AddCommand(queryCNS, getAddressByContractNameAndVersion)
}
