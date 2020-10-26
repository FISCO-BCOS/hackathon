package org.fisco.bcos.model;

import java.math.BigInteger;

public class DAOEntry {
    private String title;
    private String description;
    private BigInteger fundingGoal;
    private String beneficiary;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigInteger getFundingGoal() {
        return fundingGoal;
    }

    public void setFundingGoal(BigInteger fundingGoal) {
        this.fundingGoal = fundingGoal;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
}
