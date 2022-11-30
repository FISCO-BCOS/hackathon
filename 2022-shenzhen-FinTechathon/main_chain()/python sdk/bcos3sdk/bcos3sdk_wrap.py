import ctypes
import json
import queue
from ctypes import *
#----------------------------
# C语言sdk文档：https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/sdk/c_sdk/index.html
# C语言SDK接口:https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/sdk/c_sdk/api.html
# 动态库下载CDN连接
# https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/sdk/c_sdk/dylibs.html
#-----------------------------

# 自定义回调上下文结构体，可以在里面再塞一些有需要的上下文内容
from client.local_lib_helper import LocalLibHelper

class BcosReqContext(Structure):
    _fields_ = [('seq', c_int),
                ('name', c_char_p),
                ('msg', c_char_p),
                ]
    
    def __init__(self, s, n, m):
        self.seq = s
        self.name = n.encode("utf-8")
        self.msg = m.encode("utf-8")
    
    def detail(self):
        s = f"seq:{self.seq},name:{self.name},msg:{self.msg}";
        return s


'''
//c语言定义的返回结构体
struct bcos_sdk_c_struct_response
{
    int error;   // 返回状态, 0成功, 其他失败
    char* desc;  // 失败时描述错误信息
    void* data;   // 返回数据, error=0 时有效
    size_t size;  // 返回数据大小, error=0 时有效
    void* context;  // 回调上下文,调用接口时传入的`context`参数
};
'''


# bcos sdk返回结构体
class BcosResponse(Structure):
    _fields_ = [('error', c_int),
                ('desc', c_char_p),
                ('data', c_void_p),
                ('size', c_size_t),
                ('context', c_void_p),
                ]
    
    def get_data_str(self):
        pool = create_string_buffer(self.size)
        memmove(pool, self.data, self.size)
        return str(pool, "utf-8")
    
    def get_desc(self):
        if self.desc is None:
            return ""
        return str(self.desc, "utf-8")
    
    def get_size(self):
        return self.size
    
    def get_error(self):
        return self.error
    
    def get_context(self):
        c = ctypes.cast(self.context, POINTER(BcosReqContext))
        return c.contents


def s2b(input):
    if type(input) is str:
        return bytes(input, "UTF-8")
    return input


def b2s(input):
    if type(input) is bytes:
        return str(input, "UTF-8")
    return input


G_SEQ = 0


# 模拟一个Future对象，用于接收回调，提供一个wait方法，把异步变成同步
# 采用queue来模拟wait，原因是一个future里有可能多次被回调,回调的消息put到queue里，应用端可以用wait方法依次将消息pop出来
# 注意，在此版本里，建议对bcossdk的一次接口调用，就用一个单独的CallbackFuture,不要混用
# 否则sdk并发时，回调的消息会复用callback入口,导致返回的消息不对应刚才发送的req,或者需要用消息唯一序列号来对应是哪个请求包
# 所以，一次调用一个future，基本能保证返回的消息，对应的是发送的消息，代码读起来也比较简单
#todo: 可以扩展一些特性，比如，收到sdk回调后，立刻再递归回调应用层设置的callback，
# 在event监听场景比较有意义，目前先统一用wait，参见tests/testbcos3event.py

class BcosCallbackFuture:
    queue = queue.Queue(100)
    context: BcosReqContext = None
    is_timeout: False
    
    def __init__(self, context_name=None, context_msg=None):
        self.data = ""
        self.error = 0
        self.desc = ""
        if context_name is not None or context_msg is not None:
            self.context = BcosReqContext(self.next_seq(), context_name, context_msg)
            # print(self.context.detail())
        self.callback = BCOS_CALLBACK_FUNC(self.bcos_callback)
    
    def next_seq(self, inc=1):
        global G_SEQ
        G_SEQ = G_SEQ + inc
        return G_SEQ
    
    def bcos_callback(self, resp):
        #print("bcos_callback-->",resp)
        self.size = resp.contents.get_size()
        pool = create_string_buffer(resp.contents.size)
        memmove(pool, resp.contents.data, resp.contents.size)
        self.data = str(pool, "utf-8")
        self.error = resp.contents.get_error()
        self.desc = resp.contents.get_desc()
        self.context_callback = resp.contents.get_context()
        # print(f"context_callback {self.context_callback.detail()}")
        self.queue.put_nowait(1)
        #print(f"--->QSIZE::{self.queue.qsize()}------<<<<",)
    
    def wait(self, timeout=5):
        try:
            self.is_timeout = False
            self.queue.get(True, timeout)
        except:
            self.is_timeout = True
            pass
        return self
    
    def display(self):
        if not hasattr(self, 'data'):
            print("empty")
            return
        # print("datastr:",datastr)
        if len(self.data) > 0:
            j = json.loads(self.data)
            print(json.dumps(j, indent=4))
        else:
            print(">> data is empty", )
        print(">> datasize:  ", self.size)
        print(">> error: ", self.error)
        print(">> desc:  ", self.desc)
        c = self.context_callback
        if c is not None:
            print(">> context:({}),{},[{}]".format(c.seq, str(c.name, "utf-8"), str(c.msg, "utf-8")))


# bcos sdk回调函数定义
# typedef void (*bcos_sdk_c_struct_response_cb)(struct bcos_sdk_c_struct_response* resp)
BCOS_CALLBACK_FUNC = CFUNCTYPE(None, POINTER(BcosResponse))

# typedef void (*bcos_sdk_c_amop_subscribe_cb)(
# const char* endpoint, const char* seq, struct bcos_sdk_c_struct_response* resp);
BCOS_AMOP_SUB_CALLBACK_FUNC = CFUNCTYPE(None, c_char_p, c_char_p, POINTER(BcosResponse))


# 用dl load的方式封装接口，在windows、linux平台实测通过
class NativeBcos3sdk:
    # libname_win = 'bcos-c-sdk.dll'
    libname = "bcos-c-sdk"
    libpath = "bcos3sdklib"
    # libname_linux = "libbcos-c-sdk.so"
    # libpath_linux = "bcos3sdklib"
    nativelib = None
    
    def __init__(self, cfgfile="bcos3_sdk_config.ini", libpath="bcos3sdklib"):
        self.init_sdk(cfgfile, libpath)
    
    def init_sdk(self, cfgfile="bcos3_sdk_config.ini", libpath="bcos3sdklib"):
        self.libpath = libpath
        self.configfile = cfgfile
        locallib = LocalLibHelper(self.libname, self.libpath)
        self.nativelib = locallib.load_lib()
        if self.nativelib is None:
            return -1
        
        self.init_func()
        
        return self.start_sdk()
    
    # 启动sdk
    def start_sdk(self):
        configfile = self.configfile
        if type(configfile) is str:
            configfile = configfile.encode("utf-8")
        # print("create sdk by configfile:", configfile)
        self.sdk = self.bcos_sdk_create_by_config_file(configfile)
        # print("sdk is ",self.sdk)
        if self.sdk is None:
            # print(self.bcos_sdk_get_last_error_msg())
            err = self.bcos_sdk_get_last_error()
            return err
        self.bcos_sdk_start(self.sdk)
        err = self.bcos_sdk_get_last_error()
        errmsg = self.bcos_sdk_get_last_error_msg()
        #print("start sdk error ,",str(errmsg,encoding='GBK'))
        return err
    
    def finish(self):
        if self.sdk is None:
            return
        self.bcos_sdk_stop(self.sdk)
        self.bcos_sdk_destroy(self.sdk)
        self.sdk = None
    
    # 初始化所有的方法，包括入参和出参定义
    def init_func(self):
        
        self.nativelib.bcos_sdk_version.restype = c_char_p
        self.bcos_sdk_version = self.nativelib.bcos_sdk_version
        # -------------------------------------------------
        # 初始化和停止接口
        self.nativelib.bcos_sdk_create_by_config_file.argtypes = [c_char_p]
        self.nativelib.bcos_sdk_create_by_config_file.restype = c_void_p
        self.bcos_sdk_create_by_config_file = self.nativelib.bcos_sdk_create_by_config_file
        self.nativelib.bcos_sdk_start.argtypes = [ctypes.c_void_p]
        self.bcos_sdk_start = self.nativelib.bcos_sdk_start
        self.nativelib.bcos_sdk_stop.argtypes = [c_void_p]
        self.bcos_sdk_stop = self.nativelib.bcos_sdk_stop
        self.nativelib.bcos_sdk_destroy.argtypes = [c_void_p]
        self.bcos_sdk_destroy = self.nativelib.bcos_sdk_destroy
        
        # -------------------------------------------------
        # 错误处理
        self.nativelib.bcos_sdk_get_last_error.restype = ctypes.c_int
        self.bcos_sdk_get_last_error = self.nativelib.bcos_sdk_get_last_error
        
        self.nativelib.bcos_sdk_get_last_error_msg.restype = ctypes.c_char_p
        self.bcos_sdk_get_last_error_msg = self.nativelib.bcos_sdk_get_last_error_msg
        self.nativelib.bcos_sdk_is_last_opr_success.restype = ctypes.c_int
        self.bcos_sdk_is_last_opr_success = self.nativelib.bcos_sdk_is_last_opr_success
        
        # ----------------------------------------------------------------
        # 区块查询接口
        # void bcos_rpc_get_block_by_number(void* sdk, const char* group, const char* node, int64_t block_number, int only_header, int only_tx_hash, bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_block_by_number.argtypes = [c_void_p, c_char_p, c_char_p, c_long, c_uint, c_uint,
                                                                BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_block_by_number = self.nativelib.bcos_rpc_get_block_by_number
        # void bcos_rpc_get_block_by_hash(void * sdk, const  char * group, const char * node, const  char * block_hash, int only_header, int    only_tx_hash, bcos_sdk_c_struct_response_cb    callback, void * context)
        self.nativelib.bcos_rpc_get_block_by_hash.argtypes = [c_void_p, c_char_p, c_char_p, c_char_p, c_uint, c_uint,
                                                              BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_block_by_hash = self.nativelib.bcos_rpc_get_block_by_hash
        # void bcos_rpc_get_block_hash_by_number(void* sdk, const char* group, const char* node, int64_t block_number, bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_block_hash_by_number.argtypes = [c_void_p, c_char_p, c_char_p, c_uint,
                                                                     BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_block_hash_by_number = self.nativelib.bcos_rpc_get_block_hash_by_number
        # --------
        # int64_t bcos_rpc_get_block_limit(void* sdk, const char* group)
        self.nativelib.bcos_rpc_get_block_limit.argtypes = [c_void_p, c_char_p]
        self.nativelib.bcos_rpc_get_block_limit.restype = c_long
        self.bcos_rpc_get_block_limit = self.nativelib.bcos_rpc_get_block_limit
        
        # void bcos_rpc_get_block_number(void* sdk, const char* group, const char* node,bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_block_number.argtypes = [
            c_void_p, c_char_p, c_char_p, BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_block_number = self.nativelib.bcos_rpc_get_block_number
        
        # ----------------------------------------------------------------
        # 交易查询接口
        # void bcos_rpc_get_transaction(void* sdk, const char* group, const char* node, const char* tx_hash,int proof, bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_transaction.argtypes = [c_void_p, c_char_p, c_char_p, c_char_p, c_int,
                                                            BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_transaction = self.nativelib.bcos_rpc_get_transaction
        # void bcos_rpc_get_transaction_receipt(void* sdk, const char* group, const char* node, const char* tx_hash, int proof, bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_transaction_receipt.argtypes = [c_void_p, c_char_p, c_char_p, c_char_p, c_int,
                                                                    BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_transaction_receipt = self.nativelib.bcos_rpc_get_transaction_receipt
        # -------------------------------------------------
        # 一些链级信息查询接口
        # void bcos_rpc_get_code(void* sdk, const char* group, const char* node, const char* address,bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_code.argtypes = [c_void_p, c_char_p, c_char_p, c_char_p, BCOS_CALLBACK_FUNC,
                                                     c_void_p]
        self.bcos_rpc_get_code = self.nativelib.bcos_rpc_get_code
        
        # void bcos_rpc_get_sealer_list(void* sdk, const char* group, const char* node,bcos_sdk_c_struct_response_cb callback, void* context
        self.nativelib.bcos_rpc_get_sealer_list.argtypes = [
            c_void_p, c_char_p, c_char_p, BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_sealer_list = self.nativelib.bcos_rpc_get_sealer_list
        
        # bcos_rpc_get_observer_list
        self.nativelib.bcos_rpc_get_observer_list.argtypes = [c_void_p, c_char_p, c_char_p, BCOS_CALLBACK_FUNC,
                                                              c_void_p]
        self.bcos_rpc_get_observer_list = self.nativelib.bcos_rpc_get_observer_list
        
        # void bcos_rpc_get_pbft_view(void* sdk, const char* group, const char* node,bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_pbft_view.argtypes = [
            c_void_p, c_char_p, c_char_p, BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_pbft_view = self.nativelib.bcos_rpc_get_pbft_view
        
        # void bcos_rpc_get_pending_tx_size(void* sdk, const char* group, const char* node,bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_pending_tx_size.argtypes = [c_void_p, c_char_p, c_char_p, BCOS_CALLBACK_FUNC,
                                                                c_void_p]
        self.bcos_rpc_get_pending_tx_size = self.nativelib.bcos_rpc_get_pending_tx_size
        
        # void bcos_rpc_get_sync_status(void* sdk, const char* group, const char* node,bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_sync_status.argtypes = [
            c_void_p, c_char_p, c_char_p, BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_sync_status = self.nativelib.bcos_rpc_get_sync_status
        
        # void bcos_rpc_get_consensus_status(void* sdk, const char* group, const char* node,bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_consensus_status.argtypes = [c_void_p, c_char_p, c_char_p, BCOS_CALLBACK_FUNC,
                                                                 c_void_p]
        self.bcos_rpc_get_consensus_status = self.nativelib.bcos_rpc_get_consensus_status
        
        # void bcos_rpc_get_system_config_by_key(void* sdk, const char* group, const char* node,const char* key,bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_system_config_by_key.argtypes = [c_void_p, c_char_p, c_char_p, c_char_p,
                                                                     BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_system_config_by_key = self.nativelib.bcos_rpc_get_system_config_by_key
        
        # void bcos_rpc_get_total_transaction_count(void* sdk, const char* group, const char* node, bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_total_transaction_count.argtypes = [c_void_p, c_char_p, c_char_p,
                                                                        BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_total_transaction_count = self.nativelib.bcos_rpc_get_total_transaction_count
        
        # void bcos_rpc_get_group_peers(void* sdk, const char* group, bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_group_peers.argtypes = [
            c_void_p, c_char_p, BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_group_peers = self.nativelib.bcos_rpc_get_group_peers
        
        # void bcos_rpc_get_peers(void* sdk, bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_peers.argtypes = [c_void_p, BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_peers = self.nativelib.bcos_rpc_get_peers
        
        # group查询系列接口
        # void bcos_rpc_get_group_list(void* sdk, bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_group_list.argtypes = [c_void_p, BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_group_list = self.nativelib.bcos_rpc_get_group_list
        
        # void bcos_rpc_get_group_info(void* sdk, const char* group, bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_group_info.argtypes = [
            c_void_p, c_char_p, BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_group_info = self.nativelib.bcos_rpc_get_group_info
        
        # void bcos_rpc_get_group_info_list(void* sdk,  bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_group_info_list.argtypes = [
            c_void_p, BCOS_CALLBACK_FUNC, c_void_p]
        self.bcos_rpc_get_group_info_list = self.nativelib.bcos_rpc_get_group_info_list
        
        # void bcos_rpc_get_group_node_info(void* sdk, const char* group, const char* node,bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_rpc_get_group_node_info.argtypes = [c_void_p, c_char_p, c_char_p, BCOS_CALLBACK_FUNC,
                                                                c_void_p]
        self.bcos_rpc_get_group_node_info = self.nativelib.bcos_rpc_get_group_node_info
        
        # ------------------------------------------------------------------
        # AMOP类sdk （todo: 在多节点环境里测试）
        # void bcos_amop_subscribe_topic(void* sdk, char** topics, size_t count)
        # 有个指针数组参数，有待测试
        self.nativelib.bcos_amop_subscribe_topic.argtypes = [c_void_p, POINTER(c_char_p), c_int]
        self.bcos_amop_subscribe_topic = self.nativelib.bcos_amop_subscribe_topic
        
        #  void bcos_amop_subscribe_topic_with_cb(void* sdk, const char* topic, bcos_sdk_c_amop_subscribe_cb cb, void* context)
        self.nativelib.bcos_amop_subscribe_topic_with_cb.argtypes = [c_void_p, c_char_p, BCOS_AMOP_SUB_CALLBACK_FUNC,
                                                                     c_void_p]
        self.bcos_amop_subscribe_topic_with_cb = self.nativelib.bcos_amop_subscribe_topic_with_cb
        
        # void bcos_amop_set_subscribe_topic_cb(void* sdk, bcos_sdk_c_amop_subscribe_cb cb, void* context)
        self.nativelib.bcos_amop_set_subscribe_topic_cb.argtypes = [c_void_p, BCOS_AMOP_SUB_CALLBACK_FUNC,
                                                                    c_void_p]
        self.bcos_amop_set_subscribe_topic_cb = self.nativelib.bcos_amop_set_subscribe_topic_cb
        
        # void bcos_amop_unsubscribe_topic(void* sdk, char** topics, size_t count)
        self.nativelib.bcos_amop_unsubscribe_topic.argtypes = [c_void_p, POINTER(c_char_p), c_long]
        self.bcos_amop_unsubscribe_topic = self.nativelib.bcos_amop_unsubscribe_topic
        
        # void bcos_amop_publish(void* sdk, const char* topic, void* data, size_t size, uint32_t timeout,bcos_sdk_c_amop_publish_cb cb, void* context)
        self.nativelib.bcos_amop_publish.argtypes = [
            c_void_p, c_char_p, c_long, c_int, BCOS_AMOP_SUB_CALLBACK_FUNC, c_void_p]
        self.bcos_amop_publish = self.nativelib.bcos_amop_publish
        
        # void bcos_amop_broadcast(void* sdk, const char* topic, void* data, size_t size)
        self.nativelib.bcos_amop_broadcast.argtypes = [c_void_p, c_char_p, c_char_p, c_long]
        self.bcos_amop_broadcast = self.nativelib.bcos_amop_broadcast
        
        # void bcos_amop_send_response(void* sdk, const char* peer, const char* seq, void* data, size_t size)
        self.nativelib.bcos_amop_send_response.argtypes = [
            c_void_p, c_char_p, c_char_p, c_char_p, c_long]
        self.bcos_amop_send_response = self.nativelib.bcos_amop_send_response
        
        # --------------------------------------------------
        # EventSub系列接口
        # tech ref: https://fisco-bcos-doc.readthedocs.io/zh_CN/latest/docs/develop/sdk/java_sdk/event_sub.html
        # const char* bcos_event_sub_subscribe_event(void* sdk, const char* group, const char* params,bcos_sdk_c_struct_response_cb callback, void* context)
        self.nativelib.bcos_event_sub_subscribe_event.argtypes = [c_void_p, c_char_p, c_char_p, BCOS_CALLBACK_FUNC,
                                                                  c_void_p]
        self.nativelib.bcos_event_sub_subscribe_event.restype = c_char_p
        self.bcos_event_sub_subscribe_event = self.nativelib.bcos_event_sub_subscribe_event
        # void bcos_event_sub_unsubscribe_event(void* sdk, const char* id)
        self.nativelib.bcos_event_sub_unsubscribe_event.argtypes = [c_void_p, c_char_p]
        self.bcos_event_sub_unsubscribe_event = self.nativelib.bcos_event_sub_unsubscribe_event
        ''' [sample for event sub params:]
            event_param = dict()
            event_param["fromBlock"]=0
            event_param["toBlock"]=10000000
            event_param["address"]=["44a8a3cf7e5f2ba1555152968ec536d2a15817faq;:q"] #sample helloWorld address
            event_param["topics"]=[]
            parser = DatatypeParser("contracts/HelloWorld.abi")
            event_name = "onset"
            eventtopic = parser.topic_from_event_name(event_name)
            event_param["topics"].append(eventtopic)
            event_param_json = json.dumps(event_param)
            l = bcossdk.bcos_event_sub_subscribe_event(bcossdk.sdk, group_id,s2b(event_param_str), cb_func, byref(cb_context))
        '''
        
        # -----------------------------------------------------------------------
        # KeyPair 接口
        # void* bcos_sdk_create_keypair(int crypto_type); 0: ecdsa 1: sm
        self.nativelib.bcos_sdk_create_keypair.argtypes = [c_int]
        self.nativelib.bcos_sdk_create_keypair.restype = c_void_p
        self.bcos_sdk_create_keypair = self.nativelib.bcos_sdk_create_keypair
        
        # void* bcos_sdk_create_keypair_by_private_key(int crypto_type, void* private_key, unsigned length)
        self.nativelib.bcos_sdk_create_keypair_by_private_key.argtypes = [c_int, c_char_p, c_int]
        self.nativelib.bcos_sdk_create_keypair_by_private_key.restype = c_void_p
        self.bcos_sdk_create_keypair_by_private_key = self.nativelib.bcos_sdk_create_keypair_by_private_key
        
        # void* bcos_sdk_create_keypair_by_hex_private_key(int crypto_type, const char* private_key)
        self.nativelib.bcos_sdk_create_keypair_by_hex_private_key.argtypes = [c_int, c_char_p]
        self.nativelib.bcos_sdk_create_keypair_by_hex_private_key.restype = c_void_p
        self.bcos_sdk_create_keypair_by_hex_private_key = self.nativelib.bcos_sdk_create_keypair_by_hex_private_key
        
        # int bcos_sdk_get_keypair_type(void* key_pair)
        self.nativelib.bcos_sdk_get_keypair_type.argtypes = [c_char_p]
        self.nativelib.bcos_sdk_get_keypair_type.restype = c_void_p
        self.bcos_sdk_get_keypair_type = self.nativelib.bcos_sdk_get_keypair_type
        
        # void* bcos_sdk_load_keypair(const char* pem_path);
        # self.nativelib.bcos_sdk_load_keypair.argtypes = [c_char_p]
        # self.nativelib.bcos_sdk_load_keypair.restype = c_void_p
        # self.bcos_sdk_load_keypair = self.nativelib.bcos_sdk_load_keypair
        # const char* bcos_sdk_get_keypair_address(void* key_pair);
        self.nativelib.bcos_sdk_get_keypair_address.argtypes = [c_void_p]
        self.nativelib.bcos_sdk_get_keypair_address.restype = c_char_p
        self.bcos_sdk_get_keypair_address = self.nativelib.bcos_sdk_get_keypair_address
        
        # const char* bcos_sdk_get_keypair_public_key(void* key_pair);
        self.nativelib.bcos_sdk_get_keypair_public_key.argtypes = [c_void_p]
        self.nativelib.bcos_sdk_get_keypair_public_key.restype = c_char_p
        self.bcos_sdk_get_keypair_public_key = (self.nativelib.bcos_sdk_get_keypair_public_key)
        
        # const char* bcos_sdk_get_keypair_private_key(void* key_pair);
        self.nativelib.bcos_sdk_get_keypair_private_key.argtypes = [c_void_p]
        self.nativelib.bcos_sdk_get_keypair_private_key.restype = c_char_p
        self.bcos_sdk_get_keypair_private_key = (self.nativelib.bcos_sdk_get_keypair_private_key)
        
        # void bcos_sdk_destroy_keypair(void* key_pair);
        self.nativelib.bcos_sdk_destroy_keypair.argtypes = [c_void_p]
        self.bcos_sdk_destroy_keypair = self.nativelib.bcos_sdk_destroy_keypair
        
        # -----------------------------------------------------------------------------------------
        # 编码交易
        # void bcos_sdk_create_signed_transaction
        # (void* key_pair, const char* group_id, const char* chain_id,
        # const char* to, const char* data, const char* abi, int64_t block_limit, int32_t attribute,
        # char** tx_hash, char** signed_tx)
        # 创建签名的交易,BCOS3用了新的交易编码方式，组装交易数据（含ABI格式的function input）需要用BCOS3的CAPI，ABI部分则通用兼容。
        self.nativelib.bcos_sdk_create_signed_transaction.argtypes = [c_void_p, c_char_p, c_char_p,
                                                                      c_char_p, c_char_p, c_char_p, c_int64, c_int32,
                                                                      POINTER(c_char_p), POINTER(c_char_p)
                                                                      ]
        self.bcos_sdk_create_signed_transaction = self.nativelib.bcos_sdk_create_signed_transaction
        # bcos_sdk_create_signed_transaction相当于下面几个接口功能的组合，创建交易、交易哈希、交易签名流程需要分开处理时，使用下面几个接口
        
        # 部分不是立刻用到的接口先占位，用到的时候实现
        # void* bcos_sdk_create_transaction_data(const char* group_id, const char* chain_id, const char* to, const char* data, const char* abi, int64_t block_limit)
        # 创建TransactionData对象
        # const char* bcos_sdk_calc_transaction_data_hash(int crypto_type, void* transaction_data)
        # 计算TransactionData对象哈希
        # const char* bcos_sdk_sign_transaction_data_hash(void* keypair, const char* transcation_hash)
        # 交易哈希签名
        # const char* bcos_sdk_create_signed_transaction_with_signed_data(void* transaction_data, const char* signed_transaction_data, const char* transaction_data_hash, int32_t attribute)
        # 创建签名的交易
        
        # ------------------------------------------------------------------------------------------------
        # call和发送交易
        # void bcos_rpc_call(void* sdk, const char* group, const char* node, const char* to, const char* data,bcos_sdk_c_struct_response_cb callback, void* context);
        self.nativelib.bcos_rpc_call.argtypes = [c_void_p, c_char_p, c_char_p, c_char_p, c_char_p, BCOS_CALLBACK_FUNC,
                                                 c_void_p]
        self.bcos_rpc_call = self.nativelib.bcos_rpc_call
        
        # sendTransaction
        # void bcos_rpc_send_transaction(void* sdk, const char* group, const char* node, const char* data,
        #    int proof, bcos_sdk_c_struct_response_cb callback, void* context);
        self.nativelib.bcos_rpc_send_transaction.argtypes = [c_void_p, c_char_p, c_char_p, c_char_p, c_int,
                                                             BCOS_CALLBACK_FUNC,
                                                             c_void_p]
        self.bcos_rpc_send_transaction = self.nativelib.bcos_rpc_send_transaction
        
        # 群组信息查询
        # void bcos_sdk_get_group_wasm_and_crypto(void* sdk, const char* group, int* wasm, int* sm_crypto);
        self.nativelib.bcos_sdk_get_group_wasm_and_crypto.argtypes = [c_void_p, c_char_p, POINTER(c_int),
                                                                      POINTER(c_int), ]
        self.bcos_sdk_get_group_wasm_and_crypto = (self.nativelib.bcos_sdk_get_group_wasm_and_crypto)
        
        # const char* bcos_sdk_get_group_chain_id(void* sdk, const char* group);
        self.nativelib.bcos_sdk_get_group_chain_id.argtypes = [c_void_p, c_char_p]
        self.nativelib.bcos_sdk_get_group_chain_id.restype = c_char_p
        self.bcos_sdk_get_group_chain_id = self.nativelib.bcos_sdk_get_group_chain_id
        
        # 内存管理
        # void        bcos_sdk_c_free(void * p)
        self.nativelib.bcos_sdk_c_free.argtypes = [c_void_p]
        self.bcos_sdk_c_free = self.nativelib.bcos_sdk_c_free
        
        # ABI编码类
        # Python sdk内置了python的编解码能力，所以csdk里的编解码暂时不需要实现
        ##创建签名的交易,BCOS3用了新的交易编码方式，组装交易数据（含ABI格式的function input）需要用BCOS3的CAPI，ABI部分则通用兼容。
        # const char* bcos_sdk_abi_encode_constructor(const char* abi, const char* bin, const char* params, int crypto_type)
        # 功能:编码构造函数参数
        # const char* bcos_sdk_abi_encode_method(const char* abi, const char* method_name, const char* params, int crypto_type)
        # 编码接口参数
        # const char* bcos_sdk_abi_encode_method_by_method_id(const char* abi, const char* method_id, const char* params, int crypto_type)
        # 根据methodID编码参数
        # const char* bcos_sdk_abi_encode_method_by_method_sig(const char* method_sig, const char* params, int crypto_type)
        # 根据接口signature编码参数
        # const char* bcos_sdk_abi_decode_method_input(const char* abi, const char* method_name, const char* data, int crypto_type)
        # 根据接口名解析输入参数
        # const char* bcos_sdk_abi_decode_method_input_by_method_id(const char* abi, const char* method_id, const char* data, int crypto_type)
        # 根据methodID解析输入参数
        # const char* bcos_sdk_abi_decode_method_input_by_method_sig(const char* method_sig, const char* data, int crypto_type)
        # 根据接口signature解析输入参数
        # const char* bcos_sdk_abi_decode_method_output(const char* abi, const char* method_name, const char* data, int crypto_type)
        # 根据接口名解析返回参数
        # const char* bcos_sdk_abi_decode_method_output_by_method_id(const char* abi, const char* method_id, const char* data, int crypto_type)
        # 根据methodID解析返回参数
        # const char* bcos_sdk_abi_decode_event(const char* abi, const char* event_name, const char* data, int crypto_type)
        # 根据event名解析event参数
        # const char* bcos_sdk_abi_decode_event_by_topic(const char* abi, const char* topic, const char* data, int crypto_type)
        # 根据topic解析event参数
