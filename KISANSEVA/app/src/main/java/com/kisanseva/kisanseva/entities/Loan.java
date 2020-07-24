package com.kisanseva.kisanseva.entities;


public class Loan {

    private String bankName;
    private Double loanAmt;
    private int tenureTime;
    private Double interestrate;
    private String loanDate;
    private String description;
    private String firbaseObjectId;
    private String loantype;


    public Loan() {
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Double getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(Double loanAmt) {
        this.loanAmt = loanAmt;
    }

    public int getTenureTime() {
        return tenureTime;
    }

    public void setTenureTime(int tenureTime) {
        this.tenureTime = tenureTime;
    }

    public Double getInterestrate() {
        return interestrate;
    }

    public void setInterestrate(Double interestrate) {
        this.interestrate = interestrate;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirbaseObjectId() {
        return firbaseObjectId;
    }

    public String getLoantype() {
        return loantype;
    }

    public void setLoantype(String loantype) {
        this.loantype = loantype;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "bankName='" + bankName + '\'' +
                ", loanAmt=" + loanAmt +
                ", tenureTime='" + tenureTime + '\'' +
                ", interestrate=" + interestrate +
                ", loanDate='" + loanDate + '\'' +
                ", description='" + description + '\'' +
                ", firbaseObjectId='" + firbaseObjectId + '\'' +
                ", loantype='" + loantype + '\'' +
                '}';
    }

    public void setFirbaseObjectId(String firbaseObjectId) {
        this.firbaseObjectId = firbaseObjectId;
    }

}
