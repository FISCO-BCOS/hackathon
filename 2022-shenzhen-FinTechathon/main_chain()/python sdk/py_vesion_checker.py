import platform
py_version_min = 3.6
py_version_max = 4.0


def compare_py_version():
    ver_tuple = platform.python_version_tuple()
    floatver = float(ver_tuple[0]) + float(ver_tuple[1]) / 10
    if floatver < py_version_min:
        return -1
    if floatver > py_version_max:
        return 1
    return 0


def check_py_version_with_exception():
    res = compare_py_version()
    desc = ""
    if res < 0:
        desc = "version {} lower than {}".format(platform.python_version(), py_version_min)
        raise Exception(desc)
    if res > 0:
        desc = "version {} higher than {}".format(platform.python_version(), py_version_max)
        raise Exception(desc)
    return True

# check_py_version_with_exception()
