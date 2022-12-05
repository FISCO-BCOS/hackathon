package com.brecycle;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author cmgun
 */
public class Md5Test {

    @Test
    public void test() throws Exception {
        String result = DigestUtils.md5Hex("123456");
        System.out.println(result);
        Assert.assertEquals("e10adc3949ba59abbe56e057f20f883e", result);
    }
}
