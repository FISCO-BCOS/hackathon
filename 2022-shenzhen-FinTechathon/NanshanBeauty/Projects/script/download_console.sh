#!/bin/bash
package_name="console.tar.gz"
default_version="2.9.1"
download_version="${default_version}"
specify_console=0
solc_suffix=""
supported_solc_versions=(0.4 0.5 0.6)

LOG_WARN()
{
    local content=${1}
    echo -e "\033[31m[WARN] ${content}\033[0m"
}

LOG_INFO()
{
    local content=${1}
    echo -e "\033[32m[INFO] ${content}\033[0m"
}

help() {
    echo "
Usage:
    -c <console version>   Specify the downloaded console version, download the latest version of the console by default 
    -v <solc version>      Download the console with specific solc version, default is 0.4, 0.5 and 0.6 are supported
    -h Help
e.g
    $0 -v 0.6
"

exit 0
}

parse_params(){
while getopts "v:c:h" option;do
    case $option in
    v) solc_suffix="${OPTARG//[vV]/}"
        if ! echo "${supported_solc_versions[*]}" | grep -i "${solc_suffix}" &>/dev/null; then
            LOG_WARN "${solc_suffix} is not supported. Please set one of ${supported_solc_versions[*]}"
            exit 1;
        fi
        package_name="console-${solc_suffix}.tar.gz"
        if [ "${solc_suffix}" == "0.4" ]; then package_name="console.tar.gz";fi
    ;;
    c) specify_console=1
        download_version="${OPTARG//[vV]/}"
        ;;
    h) help;;
    *) help;;
    esac
done
}

# check params
check_params()
{
    local version=${download_version}
    local major_version=$(echo ${version} | awk -F'.' '{print $1}')
    local middle_version=$(echo ${version} | awk -F'.' '{print $2}')
    local minor_version=$(echo ${version} | awk -F'.' '{print $3}')
    if [ -z "${major_version}" ] || [ -z "${middle_version}" ] || [ -z "${minor_version}" ];then
        LOG_WARN "Illegal version \"${version}\", please specify a legal version number, latest version is ${default_version}"
        exit 1;
    fi
    if [ -z "${solc_suffix}" ];then
        return
    fi
    # specify solc version only support after console 1.1.0
    if [ "${major_version}" -lt 1 ];then
         LOG_WARN "The specified solc version is only supported after console 1.1.0 (with -v option), current specified version is \"${version}\""
         LOG_WARN "Please specified console with version no smaller than 1.1.0 when specify -v option"
         exit 1
    fi
    if [ "${middle_version}" -lt 1 ];then
        LOG_WARN "The specified solc version is only supported after console 1.1.0 (with -v option), urrent specified version is \"${version}\""
        LOG_WARN "Please specified console with version no smaller than 1.1.0 when specify -v option"
        exit 1
    fi
}

download_console(){
    check_params
    download_link=https://github.com/FISCO-BCOS/console/releases/download/v${download_version}/${package_name}
    cos_download_link=https://osp-1257653870.cos.ap-guangzhou.myqcloud.com/FISCO-BCOS/console/releases/v${download_version}/${package_name}
    LOG_INFO "Downloading console ${download_version} from ${download_link}"

    if [ $(curl -IL -o /dev/null -s -w %{http_code} "${cos_download_link}") == 200 ];then
        curl -LO ${download_link} --speed-time 30 --speed-limit 102400 -m 150 || {
            LOG_WARN "Download speed is too low, try ${cos_download_link}"
            curl -#LO "${cos_download_link}"
        }
    else
        curl -#LO ${download_link}
    fi
    if [ $? -eq 0 ];then
        LOG_INFO "Download console successfully"
    else
        LOG_WARN "Download console failed, please switch to better network and try again!"
    fi
    tar -zxf ${package_name} && chmod +x console*/*.sh
    if [ $? -eq 0 ];then
        LOG_INFO "unzip console successfully"
    else
        LOG_WARN "unzip console failed, please try again!"
    fi 
}

parse_params "$@"
download_console
