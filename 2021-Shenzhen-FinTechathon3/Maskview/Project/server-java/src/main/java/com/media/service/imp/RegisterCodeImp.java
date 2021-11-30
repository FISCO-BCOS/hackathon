package com.media.service.imp;

import com.media.dao.RegisterCodeDao;
import com.media.pojo.RegisterCode;
import com.media.service.RegisterCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author YZR
 * @date 2020/11/10 10:06
 */
@Service
@Transactional
public class RegisterCodeImp implements RegisterCodeService {

    @Autowired
    public RegisterCodeDao registerCodeDao;

    @Override
    public void insertRegisterCode(RegisterCode registerCode) {
        registerCodeDao.insertRegisterCode(registerCode);
    }
}
