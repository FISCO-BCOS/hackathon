package com.media.service.imp;

import com.media.dao.TradeHistoryDao;
import com.media.pojo.TradeHistory;
import com.media.service.TradeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TradeHistoryImp implements TradeHistoryService {

    @Autowired
    public TradeHistoryDao tradeHistoryDao;

    @Override
    public void save(TradeHistory tradeHistory) {
        tradeHistoryDao.save(tradeHistory);
    }
}
