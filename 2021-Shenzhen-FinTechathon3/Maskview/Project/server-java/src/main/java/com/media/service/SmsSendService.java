package com.media.service;

import java.math.BigInteger;

/**
 * @author ：Yzr
 * @date ：2020/11/6 9:34
 */

public interface SmsSendService {

    boolean sendCode(BigInteger phoneNumber, String templateCode, int code);

}
