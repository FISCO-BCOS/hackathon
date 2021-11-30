#!/bin/bash

set -e

scan_code_script="python ~/cobra/cobra.py -t "
ignore_files=(ci README.md console.py event_callback.py eth_account eth_abi \
    eth_hash eth_keys eth_rlp eth_typing eth_utils tests gmssl \
    hexbytes utils rlp client eth_account/account.py bin/accounts/gm_account.json)

LOG_ERROR() {
    content=${1}
    echo -e "\033[31m${content}\033[0m"
}

LOG_INFO() {
    content=${1}
    echo -e "\033[32m${content}\033[0m"
}

execute_cmd() {
    command="${1}"
    eval ${command}
    ret=$?
    if [ $ret -ne 0 ];then
        LOG_ERROR "FAILED of command: ${command}"
        exit 1
    else
        LOG_INFO "SUCCESS of command: ${command}"
    fi
}

should_ignore()
{
    local file=${1}
    for ignore in ${ignore_files[*]}; do
        if echo ${file} | grep ${ignore} &>/dev/null; then
            echo "ignore ${file} ${ignore}"
            return 0
        fi
    done
    return 1
}

scan_code()
{
    # Redirect output to stderr.
    exec 1>&2
    for file in $(git diff-index --name-status HEAD^ | grep -v .ci | awk '{print $2}'); do
        if should_ignore ${file}; then continue; fi
        LOG_INFO "check file ${file}"
        execute_cmd "${scan_code_script} $file -f json -o /tmp/report.json"
        trigger_rules=$(jq -r '.' /tmp/report.json | grep 'trigger_rules' | awk '{print $2}' | sed 's/,//g')
        echo "trigger_rules is ${trigger_rules}"
        rm /tmp/report.json
        if [ ${trigger_rules} -ne 0 ]; then
            echo "######### ERROR: Scan code failed, please adjust them before commit"
            exit 1
        fi
    done
}

install_cobra() {
   git clone https://github.com/WhaleShark-Team/cobra.git ~/cobra
   pip install -r ~/cobra/requirements.txt
   cp ~/cobra/config.template ~/cobra/config
}

install_cobra
scan_code
