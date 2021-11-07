package com.media.service;

import com.media.pojo.ImageKey;

import java.util.List;

public interface ImageKeyService {

    List<ImageKey> findImageKeyByPhoneNumber(long phoneNumber);

}
