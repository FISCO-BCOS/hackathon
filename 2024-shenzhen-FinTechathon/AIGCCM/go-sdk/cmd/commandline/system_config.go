package commandline

import (
	"fmt"

	"github.com/FISCO-BCOS/go-sdk/precompiled/config"
	"github.com/spf13/cobra"
)

var setSystemConfigByKey = &cobra.Command{
	Use:   "setSystemConfigByKey",
	Short: "[system_configuration_item]        Set the system configuration through key-value",
	Long: `Returns the system configuration through key-value.
Arguments:
	  [key]: currently only support four key: "tx_count_limit", "tx_gas_limit", "rpbft_epoch_sealer_num", "rpbft_epoch_block_num", "consensus_timeout".
[key value]: the value of corresponding key.

For example:

    [setSystemConfigByKey] [tx_count_limit] 10000

For more information please refer:

    https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html#`,
	Args: cobra.ExactArgs(2),
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
		value := args[1]
		sysConfig, err := config.NewSystemConfigService(RPC)
		if err != nil {
			fmt.Printf("setSystemConfigByKeyCmd failed, config.NewSystemConfigService err: %v\n", err)
			return
		}
		result, err := sysConfig.SetValueByKey(key, value)
		if err != nil {
			fmt.Printf("setSystemConfigByKeyCmd failed, sysConfig.SetValueByKey err: %v\n", err)
			return
		}
		if result != 1 {
			fmt.Printf("setSystemConfigByKeyCmd failed, the result is: %v", result)
			return
		}
		fmt.Println("success")
	},
}

func init() {
	rootCmd.AddCommand(setSystemConfigByKey)
}
