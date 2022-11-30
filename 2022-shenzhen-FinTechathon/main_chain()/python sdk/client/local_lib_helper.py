import ctypes
from email.mime import base
import os
import platform
from ctypes import PyDLL


class LocalLibHelper:
    # libname_win = ""
    # libpath_win = ""
    # libname_linux = ""
    # libpath_linux = ""
    # target_libname = ""
    # target_libpath = ""
    nativelib = None

    target_platform = "unknown"
    lib_base_name = ""
    lib_dir_path = ""
    lib_name = None

    def __init__(self, base_name, dir_path) -> None:
        platsys = platform.platform().lower()
        #print("Libhelper->platform {}:, lib: {}, dir: {}".format(platsys, base_name, dir_path))
        self.target_platform = platsys
        self.lib_base_name = base_name
        self.lib_dir_path = dir_path

        lib_prefix = ''
        lib_suffix = ''

        if platsys.startswith("win"):  # Windows
            lib_prefix = ''
            lib_suffix = '.dll'
        elif "mac" in platsys:          # Mac
            if "arm64" in platsys:          # mac m1
                lib_prefix = 'lib'
                lib_suffix = '-arch64.dylib'
            else:                           # mac x86
                lib_prefix = 'lib'
                lib_suffix = '.dylib'
        elif "linux" in platsys:        # Linux
            if "arm64" in platsys or "aarch64" in platsys:  # linux arm, unsupported
                raise Exception('''Unsupported linux arch {}'''.format(platsys))
            elif "x86_64" in platsys:          # linux x86
                lib_prefix = 'lib'
                lib_suffix = '.so'
            else:                               # unknown arch
                raise Exception('''Unsupported linux arch {}'''.format(platsys))
        else:                           # unknown os
            raise Exception('''Unsupported os {}'''.format(platsys))

        self.lib_name = lib_prefix + self.lib_base_name + lib_suffix
        #print(" => libname", self.lib_name)

    # def __init__(self, name_win, path_win, name_linux, path_linux):
    #     self.libname_win = name_win
    #     self.libpath_win = path_win
    #     self.libname_linux = name_linux
    #     self.libpath_linux = path_linux
    #     # 先处理平台兼容性
    #     target_platform = "unknown"
    #     platsys = platform.platform()
    #     if platsys.lower().startswith("win"):
    #         self.target_platform = "win64"
    #         self.target_libname = self.libname_win
    #         self.target_libpath = self.libpath_win
    #     if "linux" in platsys.lower():
    #         self.target_platform = "linux"
    #         self.target_libname = self.libname_linux
    #         self.target_libpath = self.libpath_linux

    #     print("target_libpath", self.target_libpath)

    def load_lib(self):
        # os.add_dll_directory(module_path)
        # print("load_lib ",self.nativelib)
        if self.nativelib is not None:
            return self.nativelib
        currpath = os.getcwd()

        # print("load_lib currpath ", currpath)
        # print("lib name ", self.lib_name)
        #print(f"lib_dir_path {self.lib_dir_path}")
        read_lib_name = os.path.join(currpath, self.lib_name)
        if not os.path.exists(read_lib_name):
            # 当前目录位置未找到，试图加上相对路径再来一次
            read_lib_name = os.path.join(currpath, self.lib_dir_path, self.lib_name)
        #print("load_lib name:",read_lib_name)
        if self.target_platform.lower().startswith("win"):
            os.add_dll_directory(os.path.dirname(read_lib_name))
        #print("start to  load lib")
        self.nativelib = PyDLL(read_lib_name, ctypes.RTLD_GLOBAL)
        #print("load lib obj : ",self.nativelib)
        # print(self.nativelib)
        if self.nativelib is None:
            print("load_lib ", self.lib_name)
            return None
        return self.nativelib
