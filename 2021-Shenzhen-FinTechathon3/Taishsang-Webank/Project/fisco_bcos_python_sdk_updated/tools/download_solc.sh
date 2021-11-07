#!/bin/bash
set -e

source="https://github.com/FISCO-BCOS/solidity/releases/download"
cdn_link_header="https://osp-1257653870.cos.ap-guangzhou.myqcloud.com/FISCO-BCOS/solidity/releases"
install_path="${HOME}/.fisco/solc"
version="0.4.25"
OS="linux"
crypto=
extension=
download_timeout=240
versions=(0.4.25 0.5.2 0.6.10)

LOG_WARN()
{
    local content="${1}"
    echo -e "\033[31m[WARN] ${content}\033[0m"
}

LOG_INFO()
{
    local content="${1}"
    echo -e "\033[32m[INFO] ${content}\033[0m"
}

help() {
    cat << EOF
Usage:
    -v <solc version>           Default 0.4.25, 0.5.2, 0.6.10 is supported
    -g <gm version>             if set download solc gm version
    -h Help
e.g
    $0 -v 0.4.25 -g
EOF

exit 0
}

check_env() {
    if [ "$(uname)" == "Darwin" ];then
        OS="mac"
    elif [ "$(uname -s)" == "Linux" ];then
        OS="linux"
        if [[ "$(uname -p)" == "aarch64" ]];then
            OS="linux-aarch64"
        fi
    elif [ "$(uname -m)" != "x86_64" ];then
        LOG_WARN "We only offer x86_64 precompiled solc binary, your OS architecture is not x86_64. Please compile from source."
        exit 1
    else
        OS="win"
        extension=".exe"
    fi
}

parse_params()
{
    while getopts "v:o:gh" option;do
        case $option in
        v) [ -n "$OPTARG" ] && version="$OPTARG"
            if ! echo "${versions[*]}" | grep -i "${version}" &>/dev/null; then
                LOG_WARN "${version} is not supported. Please set one of ${versions[*]}"
                exit 1;
            fi
        ;;
        o) [ -n "$OPTARG" ] && install_path="$OPTARG";;
        g) crypto="-gm";;
        h) help;;
        *) LOG_WARN "invalid option $option";;
        esac
    done
}

main()
{
    package_name="solc-${OS}${crypto}.tar.gz"
    download_link="${source}/v${version}/${package_name}"
    echo "Downloading solc ${version} ${package_name} from ${download_link}"
    local cdn_download_link="${cdn_link_header}/v${version}/${package_name}"
    if [ ! -f "${install_path}/solc-${version}${crypto}" ];then
        if [ $(curl -IL -o /dev/null -s -w "%{http_code}" "${cdn_download_link}") == 200 ];then
            curl -#LO "${download_link}" --speed-time 20 --speed-limit 102400 -m "${download_timeout}" || {
                LOG_INFO "Download speed is too low, try ${cdn_download_link}"
                curl -#LO "${cdn_download_link}"
            }
        else
            curl -#LO "${download_link}"
        fi

        tar -zxf "${package_name}"
        rm -rf "${package_name}"
        mkdir -p "${install_path}"
        mv "solc${extension}" "${install_path}/solc-${version}${crypto}${extension}"
    fi
    if [ ! -f "./solc-${version}${crypto}" ];then
        ln -s "${install_path}/solc-${version}${crypto}${extension}" "./solc-${version}${crypto}"
    fi
}

print_result()
{
    echo "=============================================================="
    LOG_INFO "os            : ${OS}"
    LOG_INFO "solc version  : ${version}"
    LOG_INFO "solc location : ./solc-${version}${crypto}"
    echo "=============================================================="
    LOG_INFO "./solc-${version}${crypto} --version"
    "./solc-${version}${crypto}" --version
}

parse_params "$@"
check_env
main
print_result