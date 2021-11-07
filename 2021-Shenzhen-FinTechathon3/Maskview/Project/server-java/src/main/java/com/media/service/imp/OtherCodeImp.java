package com.media.service.imp;

import com.media.dao.OtherCodeDao;
import com.media.pojo.OtherCode;
import com.media.service.OtherCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

/**
 * @author YZR
 * @date 2020/11/10 14:44
 */

@Service
@Transactional
public class OtherCodeImp implements OtherCodeService {

    @Autowired
    OtherCodeDao otherCodeDao;

    @Override
    public void insertOtherCode(OtherCode otherCode) {
        otherCodeDao.insertOtherCode(otherCode);
    }

    @Override
    public int getHerCode(BigInteger phoneNumber) {
        return otherCodeDao.getHerCode(phoneNumber);
    }

    @Override
    public boolean isExist(BigInteger phoneNumber) {
        return otherCodeDao.isExist(phoneNumber) != null;
    }

    @Override
    public void updateCode(OtherCode otherCode) {
        otherCodeDao.updateCode(otherCode);
    }

}
