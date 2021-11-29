import os
import sys


def tassl_ssock_wrap_factory(print_err=False):
    wrap = None
    sys.path.append(os.path.dirname(__file__))
    # print(os.path.dirname(__file__))
    # print(sys.path)
    try:
        from cython_tassl_sock_wrap import PyTasslSockWrap
        wrap = PyTasslSockWrap()
    except Exception as e:
        if print_err:
            print("init PyTasslSockWrap fail")
            import traceback
            traceback.print_exc()
        pass

    if wrap is not None:
        return wrap
    # --------------try native_tassl_sock_wrap ------------------------
    try:
        from native_tassl_sock_wrap import NativeTasslSockWrap
        wrap = NativeTasslSockWrap()
    except Exception as e:
        print("init PyTasslSockWrap fail")
        import traceback
        traceback.print_exc()
        pass
    return wrap
