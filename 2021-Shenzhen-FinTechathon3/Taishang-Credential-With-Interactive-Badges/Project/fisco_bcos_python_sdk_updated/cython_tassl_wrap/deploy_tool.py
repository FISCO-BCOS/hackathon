#!/usr/bin/env python

"""
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  @author: kentzhang
  @date: 2021-03
"""
import os
import platform
import re
import shutil
import sys
import time

# 1.1------------
win_runtime_libs = {}
win_runtime_libs["desc"] = "windows mingw64 runtime libs"
win_runtime_libs["path"] = "./cpp_win/runtime_libs_mingw64"
win_runtime_libs["libs"] = []
# 1.2------------
win_tassl_sock_wrap_libs = {}
win_tassl_sock_wrap_libs["desc"] = "windows cpp tassl_sock_wrap libs"
win_tassl_sock_wrap_libs["path"] = "./cpp_win"
win_tassl_sock_wrap_libs["libs"] = ["libtassl_sock_wrap.dll", "native_tassl_sock_wrap.dll"]
# 1.3------------
win_cython_tassl_sock_wrap_libs = {}
win_cython_tassl_sock_wrap_libs["desc"] = "windows cython tassl_sock_wrap libs"
win_cython_tassl_sock_wrap_libs["path"] = "./"
win_cython_tassl_sock_wrap_libs["filter"] = "cython_tassl_sock_wrap(.*).dll"
win_cython_tassl_sock_wrap_libs["libs"] = []
# *************
# 2.1------------
linux_runtime_libs = {}
linux_runtime_libs["desc"] = "linux runtime libs"
linux_runtime_libs["path"] = "./cpp_linux/runtime_libs_linux"
linux_runtime_libs["libs"] = []
# 2.2------------
linux_tassl_sock_wrap_libs = {}
linux_tassl_sock_wrap_libs["desc"] = "linux cpp tassl_sock_wrap libs"
linux_tassl_sock_wrap_libs["path"] = "./cpp_linux"
linux_tassl_sock_wrap_libs["libs"] = ["libtassl_sock_wrap.so","libnativetassl_sock_wrap.so"]
# 2.3------------
linux_cython_tassl_sock_wrap_libs = {}
linux_cython_tassl_sock_wrap_libs["desc"] = "linux cython tassl_sock_wrap libs"
linux_cython_tassl_sock_wrap_libs["path"] = "./"
linux_cython_tassl_sock_wrap_libs["filter"] = "cython_tassl_sock_wrap(.*).so"
linux_cython_tassl_sock_wrap_libs["libs"] = []

target_platform = "unknown"
target_path = "../"
platsys = platform.platform()
if platsys.lower().startswith("win"):
    target_platform = "win64"
if "linux" in platsys.lower():
    target_platform = "linux"

print("current platform : {},using config :{} ".format(platsys, target_platform))

if target_platform == "unknown":
    print("unknown system")
    sys.exit()


def seek_path(job):
    the_path = job["path"]

    list_dirs = os.walk(the_path)

    for root, dirs, files in list_dirs:
        # print("root ",root)
        # print("dirs ",dirs)
        # print("files ",files)
        if root == the_path:
            if "filter" in job:
                for fname in files:
                    if re.match(job["filter"], fname):
                        # print("match ",fname)
                        job["libs"].append(fname)
            else:
                job["libs"].extend(files)
    return job


def copy_job(job):
    for filename in job["libs"]:
        source = os.path.join(job["path"], filename)
        target = os.path.join(target_path, filename)
        print("copy file : from {} to {}".format(source, target))
        try:
            shutil.copyfile(source, target)
        except Exception as e:
            print("copy  : {}, error {} ".format(source ,e) )


def clean_job(job):
    for filename in job["libs"]:
        target = os.path.join(target_path, filename)
        print("remove file : {}".format(target))
        if os.path.exists(target):
            os.remove(target)


def check_job(job):
    # print(json.dumps(job,indent=4) )
    for filename in job["libs"]:
        source = os.path.join(job["path"], filename)
        target = os.path.join(target_path, filename)
        filestat = None
        if os.path.exists(source):
            filestat = os.stat(source)
            info = "modify :" + time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(filestat.st_mtime))
        else:
            info = "\033[31m FILE MISSING !!! \033[0m"
        conflict = ""   
        if os.path.exists(target):
            targetstat = os.stat(target)
            diffstat = "\033[31m(SIZE NOT EQUAL)\033[0m"
            if filestat is not None:
                if targetstat.st_size == filestat.st_size:
                    diffstat = "\033[32m(size equal)\033[0m"
            conflict = "\033[33m [target exists] \033[0m {}".format(diffstat)

        print("{} ,[ {} ] {}".format(info, source, conflict))
        #
        pass


def usage():
    print("usage of tassl_sock_wrap deploy tool:")
    print(__file__ + "\033[33m\tdeploy\t\033[0m[python-sdk path]:\t deploy runtime libs to target dir")
    print(__file__ + "\033[31m\tclean\t\033[0m[python-sdk path]:\t clean  runtime libs from target dir")
    print(__file__ + "\033[32m\tcheck\t\033[0m[python-sdk path]:\t check  runtime libs for deploy")
    print(__file__ + "  help/usage/? : print usage")
    print("default python-sdk target dir: ../")
    print("")
    pass


if __name__ == '__main__':
    if len(sys.argv) == 1:
        usage()
        sys.exit(0)
    cmd = ""
    
    if len(sys.argv) > 1:
        if sys.argv[1].lower() == "help" \
                or sys.argv[1].lower() == "usage" \
                or sys.argv[1] == "?" \
                or sys.argv[1] not in ["deploy","check","clean"] \
                :
            usage()
            sys.exit(0)
        cmd = sys.argv[1]

        if len(sys.argv) > 2:
            target_path = sys.argv[2]
    print("cmd : [ {} ], target_path : [ {} ]".format(cmd, target_path))
    target_libs = []
    if "win" in target_platform:
        target_libs.append(win_runtime_libs)
        target_libs.append(win_tassl_sock_wrap_libs)
        target_libs.append(win_cython_tassl_sock_wrap_libs)

    if "linux" in target_platform:
        target_libs.append(linux_runtime_libs)
        target_libs.append(linux_tassl_sock_wrap_libs)
        target_libs.append(linux_cython_tassl_sock_wrap_libs) 

    idx = 0
    for lib_job in target_libs:
        idx = idx + 1
        print("\n{}) -------------------->".format(idx))
        print("dealing job :\033[32m {} \033[0m".format(lib_job["desc"]))
        if len(lib_job["libs"]) == 0:
            # print("seek path: ",lib_job)
            lib_job = seek_path(lib_job)

        if cmd == "deploy":
            print("\033[33m>>> copying {} libs: >>> \033[0m".format(len(lib_job["libs"])))
            copy_job(lib_job)
        if cmd == "clean":
            print("\033[31m>>> clearing {} libs: >>> \033[0m".format(len(lib_job["libs"])))
            clean_job(lib_job)
        if cmd == "check":
            print("\033[32m>>> checking {} libs: >>> \033[0m".format(len(lib_job["libs"])))
            check_job(lib_job)
    # print(json.dumps(target_libs,indent=4) )
