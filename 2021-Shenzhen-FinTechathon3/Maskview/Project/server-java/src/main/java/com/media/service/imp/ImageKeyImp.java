package com.media.service.imp;

import com.media.dao.ImageKeyDao;
import com.media.pojo.ImageKey;
import com.media.service.ImageKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
public class ImageKeyImp implements ImageKeyService {

    @Autowired
    public ImageKeyDao imageKeyDao;

    @Override
    public List<ImageKey> findImageKeyByPhoneNumber(long phoneNumber) {
        return imageKeyDao.findImageKeyByPhoneNumber(phoneNumber);
    }

}
