/** @file CommitteePrecompiled.cpp
 *  @author Evan_Cley
 *  @date 20210915
 */
#include "CommitteePrecompiled.h"   // 头文件中定义了
#include <libblockverifier/ExecutiveContext.h>
#include <libethcore/ABI.h>
#include <libprecompiled/TableFactoryPrecompiled.h>
#include <typeinfo>

using namespace dev;
using namespace dev::blockverifier;
using namespace dev::storage;
using namespace dev::precompiled;

// 特殊数据结构，一个是字符串到字符串的映射，一个是字符串到浮点数的映射
// unordered_map是无序关联容器（字典），元素在内部不以任何特定顺序排序，而是放进桶中，元素进哪个桶中完全取决于其键的哈希
typedef std::unordered_map<std::string, std::string> Pair;
typedef std::unordered_map<std::string, float> Scores;

/*
contract CommitteePrecompiled{
    function RegisterNode() public;                                 //节点注册
    function QueryState() public view returns(string, int);         //查询状态
    function QueryGlobalModel() public view returns(string, int);   //获取全局模型
    function UploadLocalUpdate(string update, int256 epoch) public; //上传本地模型
    function UploadScores(int256 epoch, string scores) public;      //上传评分
    function QueryAllUpdates() public view returns(string);         //获取所有本地模型
}
*/


// 创建表
// table name
const std::string TABLE_NAME = "CommitteePrecompiled";
// key field 主键字段
const std::string KEY_FIELD = "key";
// 其他键字段
// const std::string NODE_COUNT_FIELD_NAME = "node_count";           //注册的客户端数量(size_t)-->触发链上角色初始化及FL开始(epoch=0)
const std::string UPDATE_COUNT_FIELD_NAME = "update_count";          //当前已上传本地更新的数量(size_t)-->触发链下评分
const std::string SCORE_COUNT_FIELD_NAME = "score_count";            //当前已上传评分数量(size_t)-->触发链上聚合
const std::string ROLES_FIELD_NAME = "roles";                        //保存各个客户端的角色(unordered_map<Adress,string>)
const std::string LOCAL_UPDATES_FIELD_NAME = "local_updates";        //保存所有已上传的模型更新(unordered_map<Adress,string>)
const std::string LOCAL_SCORES_FIELD_NAME = "local_scores";          //保存所有已上传的模型评分(unordered_map<Adress,string>, string=unordered<Adress,float>)
const std::string GLOBAL_MODEL_FIELD_NAME = "global_model";          //保存全局模型(string)
const std::string EPOCH_FIELD_NAME = "epoch";                        //当前迭代轮次(int)-->触发链下训练
const std::string LOCAL_AGGREGATION_FIELD_NAME = "local_aggregation";            //本地聚合模型
const std::string AGGREGATION_COUNT_FIELD_NAME = "aggregation_count";            //本地聚合模型
const std::string VALUE_FIELD = "value";                             // value field

// 定义类中所有的接口
const char* const REGISTER_NODE = "RegisterNode()";                         //节点注册
const char* const QUERY_STATE = "QueryState()";                             //查询状态
const char* const QUERY_GLOBAL_MODEL = "QueryGlobalModel()";                //获取全局模型
const char* const UPLOAD_LOCAL_UPDATE = "UploadLocalUpdate(string,int256)"; //上传本地模型
const char* const UPLOAD_SCORES = "UploadScores(int256,string)";            //上传评分
const char* const QUERY_ALL_UPDATES = "QueryAllUpdates()";                  //获取所有本地模型
const char* const UPLOAD_LOCAL_AGGREGATION = "UploadLocalAggregation(string,int256)";    //获取本地聚合模型

// 参数解析与结果返回
template<class T>
std::string to_json_string(T& t){
    json j = t;
    return j.dump();
}

// compare the struct by value 按值比较函数
bool cmp_by_value(const std::pair<std::string, float>& it1, const std::pair<std::string, float>& it2){
    return it1.second > it2.second;
}

// 在构造函数进行接口注册
CommitteePrecompiled ::CommitteePrecompiled ()                              //构造函数，初始化函数选择器对应的函数
{
    // name2Selector是基类Precompiled类中成员，保存接口调用的映射关系
    name2Selector[REGISTER_NODE] = getFuncSelector(REGISTER_NODE);
    name2Selector[QUERY_STATE] = getFuncSelector(QUERY_STATE);
    name2Selector[QUERY_GLOBAL_MODEL] = getFuncSelector(QUERY_GLOBAL_MODEL);
    name2Selector[UPLOAD_LOCAL_UPDATE] = getFuncSelector(UPLOAD_LOCAL_UPDATE);
    name2Selector[UPLOAD_SCORES] = getFuncSelector(UPLOAD_SCORES);
    name2Selector[QUERY_ALL_UPDATES] = getFuncSelector(QUERY_ALL_UPDATES);
    name2Selector[UPLOAD_LOCAL_AGGREGATION] = getFuncSelector(UPLOAD_LOCAL_AGGREGATION);
}

// call函数
PrecompiledExecResult::Ptr CommitteePrecompiled::call(
    dev::blockverifier::ExecutiveContext::Ptr _context, bytesConstRef _param,
    Address const& _origin, Address const&)
{
	PRECOMPILED_LOG(TRACE) << LOG_BADGE("CommitteePrecompiled") << LOG_DESC("call")
                           << LOG_KV("param", toHex(_param));

    // parse function name 解析函数名；通过getParamFunc解析_param可以区分调用的接口。注意：合约接口一定要先在构造函数中注册
    uint32_t func = getParamFunc(_param);                                           //六位函数选选择变量，用于判断调用的是哪个函数
    bytesConstRef data = getParamData(_param);                                      //参数
    auto callResult = m_precompiledExecResultFactory->createPrecompiledResult();    //声明调用结果的变量callResult，代表了各类调用函数的返回值
    callResult->gasPricer()->setMemUsed(_param.size());                             //分配内存
    // 参数的解析与返回
    dev::eth::ContractABI abi;                                                      //声明abi，获取传进智能合约参数的重要对象

    // the hash as a user-readable hex string with 0x prefix
    // _origin就是是一个调用智能合约的用户地址
    std::string _origin_str = _origin.hexPrefixed();

    // open table if table is exist  call函数中，表存在时打开，否则首先创建表
    // Table是库文件里面的
    Table::Ptr table = openTable(_context, precompiled::getTableName(TABLE_NAME));
    callResult->gasPricer()->appendOperation(InterfaceOpcode::OpenTable);
    if (!table)
    {//  表不存在时，首先创建表table。表里面只有键，值，固定的用户地址
        table = createTable(_context, precompiled::getTableName(TABLE_NAME),
            KEY_FIELD, VALUE_FIELD, _origin);
        callResult->gasPricer()->appendOperation(InterfaceOpcode::CreateTable);
        if (!table)
        {// 创建表失败，返回错误码
#if OUTPUT
        std::clog<<_origin<<"表创建失败"<<std::endl;
#endif
            PRECOMPILED_LOG(ERROR) << LOG_BADGE("HelloWorldPrecompiled") << LOG_DESC("set")
                                   << LOG_DESC("open table failed.");
            getErrorCodeOut(callResult->mutableExecResult(), storage::CODE_NO_AUTHORIZED);
            return callResult;
        }
        else
        {
#if OUTPUT
        std::clog<<_origin<<"已经创建完表"<<std::endl;
#endif
        }
        // 初始化全局模型
        InitGlobalModel(table, _origin, callResult);
    }

    // 区分调用接口/函数：
    /*
    节点注册函数接口
    */
    if(func == name2Selector[REGISTER_NODE]){
    	std::string roles_str = GetVariable(table, _origin, callResult, ROLES_FIELD_NAME);
        Pair roles = json::parse(roles_str);
#if OUTPUT
        std::clog<<_origin<<"准备判断节点类型"<<std::endl;
#endif
        //这应该是先将所有节点先置为trainer，再挑选leader
        roles[_origin_str] = "trainer";
        // 开始FL训练，如果有足够的客户，随机挑选委员会
        if(roles.size() == CLIENT_NUM){
            int i = 1;
            for(auto & client : roles){
                if(i > COMM_COUNT)
                    break;
                client.second = "comm";
                i++;
            }
            int epoch = 0;
            std::string epoch_str = to_json_string(epoch);
            UpdateVariable(table, _origin, callResult, EPOCH_FIELD_NAME, epoch_str);
#if OUTPUT
        std::clog<<_origin<<"leader选择完毕："<<roles[_origin_str]<<std::endl;
#endif
        }
        roles_str = to_json_string(roles);
        UpdateVariable(table, _origin, callResult, ROLES_FIELD_NAME, roles_str);
#if OUTPUT
        std::clog<<_origin<<"角色为"<<roles[_origin_str]<<"qwe"<<std::endl;
#endif
    /*
    查询全局状态函数接口
    */
    }else if (func == name2Selector[QUERY_STATE]){
    	/*return (global_state['roles'].get(node_id, ROLE_TRAINER),
            global_state['epoch'],
            global_state['update_count'],
            global_state['score_count'])*/
    	std::string roles_str = GetVariable(table, _origin, callResult, ROLES_FIELD_NAME);
        Pair roles = json::parse(roles_str);
//        if(roles.find(_origin_str)==roles.end()){
//            roles[_origin_str] = "trainer";         //这里是怎么选择的trainer的？--文章里说的是选出CMM后自动确定了trainer
//        }

        std::string epoch_str = GetVariable(table, _origin, callResult, EPOCH_FIELD_NAME);
        int epoch = json::parse(epoch_str);

#if OUTPUT
        std::clog<<_origin<<"查询到自己得角色和epoch："<<roles[_origin_str]<<epoch<<std::endl;
#endif

        callResult->setExecResult(abi.abiIn("", roles[_origin_str], s256(epoch)));

    /*
    获取全局模型函数接口
    */
    }else if (func == name2Selector[QUERY_GLOBAL_MODEL]){
        // 获取全局模型变量参数（字符串）
        std::string global_model_str = GetVariable(table, _origin, callResult, GLOBAL_MODEL_FIELD_NAME);
        // 获取这一轮模型变量参数（字符串）
        std::string epoch_str = GetVariable(table, _origin, callResult, EPOCH_FIELD_NAME);
        int epoch = json::parse(epoch_str);
#if OUTPUT
        std::clog<<_origin<<"查询到全局模型和epoch："<<global_model_str<<epoch<<std::endl;
#endif

        callResult->setExecResult(abi.abiIn("", global_model_str, s256(epoch)));

    /*
    上传本地更新函数接口
    */
    }else if (func == name2Selector[UPLOAD_LOCAL_UPDATE]){
        // 获取参数
        std::string update;
        s256 ep;
        abi.abiOut(data, update, ep);

        // 查表获得当前轮次
        std::string epoch_str = GetVariable(table, _origin, callResult, EPOCH_FIELD_NAME);
        int epoch = json::parse(epoch_str);
        // if not current epoch
        if(ep!=epoch)
            return callResult;
        // 获取所有训练节点的本地更新
        std::string local_updates_str = GetVariable(table, _origin, callResult, LOCAL_UPDATES_FIELD_NAME);
        Pair local_updates = json::parse(local_updates_str);
        // if local update is exist
        if(local_updates.find(_origin_str)!=local_updates.end())
            return callResult;
        // 获取更新次数
        std::string update_count_str = GetVariable(table, _origin, callResult, UPDATE_COUNT_FIELD_NAME);
        size_t update_count = json::parse(update_count_str);
        // 如果已经有足够多的更新了
//        if(update_count >= NEEDED_UPDATE_COUNT){
//#if OUTPUT
//            std::clog<<"模型数已够，本地模型更新不再收集"<<std::endl;
//#endif
                // 更新次数+1
        update_count += 1;
        update_count_str = to_json_string(update_count);
        // 本地模型更新
        local_updates[_origin_str] = update;
        local_updates_str = to_json_string(local_updates);
        // 具体更新模型当中的变量
        UpdateVariable(table, _origin, callResult, UPDATE_COUNT_FIELD_NAME, update_count_str);
        UpdateVariable(table, _origin, callResult, LOCAL_UPDATES_FIELD_NAME, local_updates_str);
#if OUTPUT
        std::clog<<"本地模型的更新已被收集，更新次次数为"<<local_updates[_origin_str]<<update_count<<std::endl;
#endif
            return callResult;

     /*
     上传分数函数接口
      */
    }else if (func == name2Selector[UPLOAD_SCORES]){
        // 0. 获取参数
        s256 ep;
        std::string strValue;
        // abi.abiOut是获得用户调用智能合约传进来的参数
        abi.abiOut(data, ep, strValue);

        // 1. if ep == epoch    epoch是通过查表得到的，ep是委员调用智能合约传进来的
        std::string epoch_str = GetVariable(table, _origin, callResult, EPOCH_FIELD_NAME);
        int epoch = json::parse(epoch_str);
        if(ep!=epoch)
            return callResult;

        // 2. 如果客户端是训练节点
    	std::string roles_str = GetVariable(table, _origin, callResult, ROLES_FIELD_NAME);
        Pair roles = json::parse(roles_str);
        if(roles.find(_origin_str)==roles.end()||roles[_origin_str]=="trainer")
            return callResult;

        // 3. 获取本地打分并更新，再插入表中。本地打分是一个列表，存储的是这个委员对所有训练节点的打分
        std::string local_scores_str = GetVariable(table, _origin, callResult, LOCAL_SCORES_FIELD_NAME);
        Pair local_scores = json::parse(local_scores_str);
        local_scores[_origin_str] = strValue;
        local_scores_str = to_json_string(local_scores);
        UpdateVariable(table, _origin, callResult, LOCAL_SCORES_FIELD_NAME, local_scores_str);

        // 4. 获取表中打分次数并+1在，再重新写入表中
        std::string score_count_str = GetVariable(table, _origin, callResult, SCORE_COUNT_FIELD_NAME);
        size_t score_count = json::parse(score_count_str);
        score_count += 1;
        score_count_str = to_json_string(score_count);
        UpdateVariable(table, _origin, callResult, SCORE_COUNT_FIELD_NAME, score_count_str);

#if OUTPUT
        std::clog<<score_count<<"打分已经被上传，打分为"<<local_scores[_origin_str]<<score_count<<std::endl;
#endif
//        // 8. 如果所有的分数都上传了 --> 开始聚合
//        if(score_count == COMM_COUNT)
//            Aggregate(table, _origin, callResult, local_scores);

    /*
    对本地聚合模型进行全局聚合
     */
    }else if (func == name2Selector[UPLOAD_LOCAL_AGGREGATION]){
        // 获取参数
        std::string update;
        s256 ep;
        abi.abiOut(data, update, ep);

#if OUTPUT
        std::clog<<_origin<<"进入了上传本地更新的接口"<<update<<epoch<<std::endl;
#endif

        // 1. if ep == epoch    epoch是通过查表得到的，ep是委员调用智能合约传进来的
        std::string epoch_str = GetVariable(table, _origin, callResult, EPOCH_FIELD_NAME);
        int epoch = json::parse(epoch_str);
        if(ep!=epoch)
            return callResult;

        // 2. 如果客户端是训练节点
    	std::string roles_str = GetVariable(table, _origin, callResult, ROLES_FIELD_NAME);
        Pair roles = json::parse(roles_str);
        if(roles.find(_origin_str)==roles.end()||roles[_origin_str]=="trainer")
            return callResult;

        // 3. 获取本地打分并更新，再插入表中。本地打分是一个列表，存储的是这个委员对所有训练节点的打分
        std::string local_aggregation_str = GetVariable(table, _origin, callResult, LOCAL_AGGREGATION_FIELD_NAME);
        Pair local_aggregation = json::parse(local_aggregation_str);
        local_aggregation[_origin_str] = update;
        local_aggregation_str = to_json_string(local_aggregation_str);
        UpdateVariable(table, _origin, callResult, LOCAL_AGGREGATION_FIELD_NAME, local_aggregation_str);

        // 4. 获取表中本地聚合次数并+1后，再重新写入表中
        std::string aggregation_count_str = GetVariable(table, _origin, callResult, AGGREGATION_COUNT_FIELD_NAME);
        size_t aggregation_count = json::parse(aggregation_count_str);
        aggregation_count += 1;
        aggregation_count_str = to_json_string(aggregation_count);
        UpdateVariable(table, _origin, callResult, AGGREGATION_COUNT_FIELD_NAME, aggregation_count_str);
#if OUTPUT
        std::clog<<_origin<<"准备开始全局聚合，本地聚合模型、轮数为："<<local_aggregation[_origin_str]<<epoch<<std::endl;
#endif
        if(aggregation_count == COMM_COUNT){
            GlobeAggregate(table, _origin, callResult);
        }
    /*
    获取所有本地更新函数接口
     */
    }else if (func == name2Selector[QUERY_ALL_UPDATES]){
        std::string update_count_str = GetVariable(table, _origin, callResult, UPDATE_COUNT_FIELD_NAME);
        size_t update_count = json::parse(update_count_str);
#if OUTPUT
        std::clog<<_origin<<"查询所有模型更新，更新次数为："<<update_count<<std::endl;
#endif
        // 如果没有足够多的更新，则返回空字符串
        if(update_count < NEEDED_UPDATE_COUNT){
            std::string res = "";
            callResult->setExecResult(abi.abiIn("",res));;
        }
        else{
            std::string local_updates_str = GetVariable(table, _origin, callResult, LOCAL_UPDATES_FIELD_NAME);
            callResult->setExecResult(abi.abiIn("", local_updates_str));
        }

    /*
    未知函数的调用，抛出错误
     */
    }else{
        PRECOMPILED_LOG(ERROR) << LOG_BADGE("CommitteePrecompiled") << LOG_DESC("unknown func")
                               << LOG_KV("func", func);
        callResult->setExecResult(abi.abiIn("", u256(CODE_UNKNOW_FUNCTION_CALL)));
    }
    // callResult代表了各类调用函数的返回值
    return callResult;
}


// ======================================================================================================================
// ======================================================================================================================
// ======================================================================================================================


// 初始化全局模型函数,并插入到表中
void CommitteePrecompiled::InitGlobalModel(Table::Ptr table, Address const& _origin, PrecompiledExecResult::Ptr callResult){
#if OUTPUT
        std::clog<<_origin<<"开始初始化模型"<<std::endl;
#endif
    int epoch = -999;
    std::string epoch_str = to_json_string(epoch);
    InsertVariable(table, _origin, callResult, EPOCH_FIELD_NAME, epoch_str);

    Model ser_model;
    std::string ser_model_str = ser_model.to_json_string();
    InsertVariable(table, _origin, callResult, GLOBAL_MODEL_FIELD_NAME, ser_model_str);

    // size_t node_count = 0;
    // std::string node_count_str = to_json_string(node_count);
    // InsertVariable(table, _origin, callResult, NODE_COUNT_FIELD_NAME, node_count_str);
    size_t update_count = 0;
    std::string update_count_str = to_json_string(update_count);
    InsertVariable(table, _origin, callResult, UPDATE_COUNT_FIELD_NAME, update_count_str);

    size_t score_count = 0;
    std::string score_count_str = to_json_string(score_count);
    InsertVariable(table, _origin, callResult, SCORE_COUNT_FIELD_NAME, score_count_str);

    Pair roles;
    std::string roles_str = to_json_string(roles);
    InsertVariable(table, _origin, callResult, ROLES_FIELD_NAME, roles_str);

    Pair local_updates;
    std::string local_updates_str = to_json_string(local_updates);
    InsertVariable(table, _origin, callResult, LOCAL_UPDATES_FIELD_NAME, local_updates_str);

    Pair local_scores;
    std::string local_scores_str = to_json_string(local_scores);
    InsertVariable(table, _origin, callResult, LOCAL_SCORES_FIELD_NAME, local_scores_str);

    Pair local_aggregation;
    std::string local_aggregation_str = to_json_string(local_aggregation);
    InsertVariable(table, _origin, callResult, LOCAL_AGGREGATION_FIELD_NAME, local_aggregation_str);

    size_t aggregation_count = 0;
    std::string aggregation_count_str = to_json_string(aggregation_count);
    InsertVariable(table, _origin, callResult, AGGREGATION_COUNT_FIELD_NAME, aggregation_count_str);
}

// 全局聚合函数
void CommitteePrecompiled::GlobeAggregate(Table::Ptr table, Address const& _origin, PrecompiledExecResult::Ptr callResult) {
    // 0. 取中位数作为每个客户端的评分
    std::unordered_map <std::string, std::vector<float>> local_scores;   //local_scores是一个字符串到向量的映射，存的是一个训练节点接收到的所有打分
    Scores scores;  //这里的scores是一个字符串到浮点数的映射，存放的是最后取完中位数的值

    // 1. 按训练节点的分数排序(或者直接选择排名前k的训练节点)
    std::vector<std::pair<std::string,float>> scores_vec(scores.begin(), scores.end());     //得到打分向量scores_vec，每个元素的值就是打分中位数
    std::sort(scores_vec.begin(), scores_vec.end(), cmp_by_value);     //将打分向量降序排序

    // 1. 获得所有本地聚合模型更新，全局模型，本地打分，本地模型更新，和节点角色
    std::string local_aggregation_str = GetVariable(table, _origin, callResult, LOCAL_AGGREGATION_FIELD_NAME);
    Pair local_aggregation = json::parse(local_aggregation_str);
    std::string global_model_str = GetVariable(table, _origin, callResult, GLOBAL_MODEL_FIELD_NAME);
    Model global_model = json::parse(global_model_str);
    std::string comm_scores_str = GetVariable(table, _origin, callResult, LOCAL_SCORES_FIELD_NAME);
    Pair comm_scores = json::parse(comm_scores_str);
    std::string local_updates_str = GetVariable(table, _origin, callResult, LOCAL_UPDATES_FIELD_NAME);
    Pair local_updates = json::parse(local_updates_str);
    std::string roles_str = GetVariable(table, _origin, callResult, ROLES_FIELD_NAME);
    Pair roles = json::parse(roles_str);
    std::string epoch_str = GetVariable(table, _origin, callResult, EPOCH_FIELD_NAME);
    int epoch = json::parse(epoch_str);

#if OUTPUT
        std::clog<<_origin<<"进入全局聚合函数，当前轮数、打分vec为："<<epoch<<std::endl;
#endif

    // 2. 将各个本地聚合模型更新进行汇总
    // LocalUpdate是一个结构体，在头文件里面，包含模型更新和meta两个属性
    LocalUpdate total_update;
    for (int k = 0; k < CLIENT_NUM; k++) {
        //获取第k个训练者的模型更新，leader就是第k个领导节点的地址
        auto leader = scores_vec[k].first;
        if(roles[leader] == "comm"){
            std::string update_str = local_aggregation[leader];
            LocalUpdate update = json::parse(update_str);
            //这个for循环要把所有的样本数和损失值加起来
            total_update.meta.n_samples += update.meta.n_samples;
            total_update.meta.avg_cost += update.meta.avg_cost;
            // delta_W和delta_b是本地模型更新参数
            auto &delta_W = update.delta_model.ser_W;
            auto &delta_b = update.delta_model.ser_b;
            // main函数中用的是一个线性回归模型，改成神经网络的时候这里的i、j需要修改过来
            for (int i = 0; i < n_features; i++) {
                for (int j = 0; j < n_class; j++) {
                    total_update.delta_model.ser_W[i][j] += delta_W[i][j] * (float) update.meta.n_samples;
                }
            }
            for (int i = 0; i < n_class; i++) {
                total_update.delta_model.ser_b[i] += delta_b[i] * (float) update.meta.n_samples;
            }
        }
    }
    // 这里是除啥呢？？--除的是样本数量，也就是输入神经元。可是为啥要乘了又除呢？？
    for (int i = 0; i < n_features; i++) {
        for (int j = 0; j < n_class; j++) {
            total_update.delta_model.ser_W[i][j] /= (float) total_update.meta.n_samples;
        }
    }
    for (int i = 0; i < n_class; i++) {
        total_update.delta_model.ser_b[i] /= (float) total_update.meta.n_samples;
    }
    total_update.meta.avg_cost /= (float) AGGREGATE_COUNT;

    // 3. 进行全局聚合，然后上传全局模型
    // 两层循环对应的就是全部的WG个数
    for (int i = 0; i < n_features; i++) {
        for (int j = 0; j < n_class; j++) {
            global_model.ser_W[i][j] -= learning_rate * total_update.delta_model.ser_W[i][j];
        }
    }
    for (int i = 0; i < n_class; i++) {
        global_model.ser_b[i] -= learning_rate * total_update.delta_model.ser_b[i];
    }
    global_model_str = global_model.to_json_string();
    UpdateVariable(table, _origin, callResult, GLOBAL_MODEL_FIELD_NAME, global_model_str);

    // 4. 当前轮数+1后上传至链上
    epoch += 1;
    epoch_str = to_json_string(epoch);
    UpdateVariable(table, _origin, callResult, EPOCH_FIELD_NAME, epoch_str);

#if OUTPUT
    //全局模型的损失值
    std::clog<<"the "<<epoch-1<<" epoch , global loss : "<<total_update.meta.avg_cost<<std::endl;
#endif

    // 5. 清除本地模型更新，聚合模型更新，委员会打分；模型更新次数，打分更新次数，聚合次数都置零
    local_updates.clear();
    local_updates_str = to_json_string(local_updates);
    UpdateVariable(table, _origin, callResult, LOCAL_UPDATES_FIELD_NAME, local_updates_str);

    local_aggregation.clear();
    local_aggregation_str = to_json_string(local_aggregation);
    UpdateVariable(table, _origin, callResult, LOCAL_AGGREGATION_FIELD_NAME, local_aggregation_str);

    comm_scores.clear();
    comm_scores_str = to_json_string(comm_scores);
    UpdateVariable(table, _origin, callResult, LOCAL_SCORES_FIELD_NAME, comm_scores_str);

    size_t update_count = 0;
    std::string update_count_str = to_json_string(update_count);
    UpdateVariable(table, _origin, callResult, UPDATE_COUNT_FIELD_NAME, update_count_str);

    size_t score_count = 0;
    std::string score_count_str = to_json_string(score_count);
    UpdateVariable(table, _origin, callResult, SCORE_COUNT_FIELD_NAME, score_count_str);

    size_t aggregation_count = 0;
    std::string aggregation_count_str = to_json_string(aggregation_count);
    UpdateVariable(table, _origin, callResult, AGGREGATION_COUNT_FIELD_NAME, aggregation_count_str);

    // 7. 重置客户端的角色
    for(auto & client : roles){
        if(client.second == "comm")
            client.second = "trainer";
    }
    // 8. 选取上一轮分数最高的几个客户端作为委员会
    for(int k=0; k<AGGREGATE_COUNT; k++){
        auto trainer = scores_vec[k].first;
        roles[trainer] = "comm";
#if OUTPUT
        std::clog<<_origin<<"选出委员会的节点地址为："<<trainer<<std::endl;
#endif
    }

    // 9. 上传角色变更信息
    roles_str = to_json_string(roles);
    UpdateVariable(table, _origin, callResult, ROLES_FIELD_NAME, roles_str);

}

// 从表中插入模型参数函数（用于初始化全局模型表）
void CommitteePrecompiled::InsertVariable(Table::Ptr table, Address const& _origin, PrecompiledExecResult::Ptr callResult, const std::string & Key, std::string & strValue){
    int count = 0;
    auto entry = table->newEntry();
    entry->setField(KEY_FIELD, Key);
    entry->setField(VALUE_FIELD, strValue);
    count = table->insert(
         Key, entry, std::make_shared<AccessOptions>(_origin));
    if (count > 0)
    {
        callResult->gasPricer()->updateMemUsed(entry->capacity() * count);
        callResult->gasPricer()->appendOperation(InterfaceOpcode::Insert, count);
    }
    if (count == storage::CODE_NO_AUTHORIZED)
    {   //  permission denied
        PRECOMPILED_LOG(ERROR) << LOG_BADGE("CommitteePrecompiled") << LOG_DESC("set")
                                << LOG_DESC("permission denied");
    }
    getErrorCodeOut(callResult->mutableExecResult(), count);
}

// 从表中获取模型参数函数（包括轮数、角色，所有本地模型更新、次数，全局模型、次数，本地打分以及次数...）
std::string CommitteePrecompiled::GetVariable(Table::Ptr table, Address const&, PrecompiledExecResult::Ptr callResult, const std::string & Key){
    // 定义了一个结构体，然后申明一个指针指向这个结构体，那么我们要用指针取出结构体中的数据，就要用到“->”
    // 利用entries来自动匹配key，来进行查表
    auto entries = table->select(Key, table->newCondition());   // auto在声明变量的时候可根据变量初始值的数据类型自动为该变量选择与之匹配的数据类型
    std::string retValue = "";
    if (0u != entries->size())
    {
        callResult->gasPricer()->updateMemUsed(getEntriesCapacity(entries));
        callResult->gasPricer()->appendOperation(InterfaceOpcode::Select, entries->size());
        auto entry = entries->get(0);
        retValue = entry->getField(VALUE_FIELD);
    }
    return retValue;
}

// 从表中更新模型参数函数
void CommitteePrecompiled::UpdateVariable(Table::Ptr table, Address const& _origin, PrecompiledExecResult::Ptr callResult, const std::string & Key, std::string & strValue){
    int count = 0;
    // 定义了一个结构体，然后申明一个指针指向这个结构体，那么我们要用指针取出结构体中的数据，就要用到“->”
    auto entry = table->newEntry();
    entry->setField(KEY_FIELD, Key);
    entry->setField(VALUE_FIELD, strValue);
    count = table->update(
         Key, entry, table->newCondition(), std::make_shared<AccessOptions>(_origin));
    if (count > 0)
    {
        callResult->gasPricer()->updateMemUsed(entry->capacity() * count);
        callResult->gasPricer()->appendOperation(InterfaceOpcode::Update, count);
    }
    if (count == storage::CODE_NO_AUTHORIZED)
    {  //  permission denied
        PRECOMPILED_LOG(ERROR) << LOG_BADGE("CommitteePrecompiled") << LOG_DESC("set")
                                << LOG_DESC("permission denied");
    }
    getErrorCodeOut(callResult->mutableExecResult(), count);
}
