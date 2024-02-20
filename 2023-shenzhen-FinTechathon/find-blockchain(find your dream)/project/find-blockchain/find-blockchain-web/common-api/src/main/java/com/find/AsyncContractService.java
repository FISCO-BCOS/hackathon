package com.find;

import com.alibaba.fastjson.JSONArray;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;

import java.math.BigInteger;
import java.util.List;

public interface AsyncContractService {

    JSONArray queryData();

    void createData(String id, String times, String modelHash);
}
