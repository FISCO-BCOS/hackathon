package com.itheima.service;


import com.itheima.domain.CreditAgent;

public interface CreditService {

    boolean sendCredit(CreditAgent creditAgent) throws Exception;
    //boolean getBalance()throws Exception;

    int getBalance() throws Exception;
}
