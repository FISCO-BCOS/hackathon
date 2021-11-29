package com.media.dao;

import com.media.pojo.TradeHistory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TradeHistoryDao {

    void save(TradeHistory tradeHistory);

}
