package com.ethersg.javabackend.service;

import com.ethersg.javabackend.pojo.*;
import org.fisco.bcos.sdk.model.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

public interface MainService {
    void register(RegisterInfo registerInfo);

    UserInfo login(String password);

    TransactionReceipt upload(ESGProject esgProject);

    TransactionReceipt update(ESGProjectWithID id);

    List<ESGProjectListByCompany> companyLookup();

    List<ESGProjectByOrganization> organizationLookup();

    List<ESGScoreList> organizationGetScores();

    TransactionReceipt putScore(ESGScore score);

    BigInteger getScore(String companyName);

    void verify(String credential);
}
