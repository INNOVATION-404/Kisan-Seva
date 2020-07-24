package com.kisanseva.kisanseva.entities;


public class Expenses {
    private double cost;
    private String category;
    private String subcategory;
    private String date;
    private String firebaseObjectId;
    private String expenseDesc;
    private String landid;
    public Expenses() {

    }

    public double getCost() {
        return cost;
    }

    public String getLandid() {
        return landid;
    }

    public void setLandid(String landid) {
        this.landid = landid;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFirebaseObjectId() {
        return firebaseObjectId;
    }

    public void setFirebaseObjectId(String firebaseObjectId) {
        this.firebaseObjectId = firebaseObjectId;
    }

    public String getExpenseDesc() {
        return expenseDesc;
    }

    public void setExpenseDesc(String expenseDesc) {
        this.expenseDesc = expenseDesc;
    }

    @Override
    public String toString() {
        return "Expenses{" +
                "cost=" + cost +
                ", category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", date='" + date + '\'' +
                ", firebaseObjectId='" + firebaseObjectId + '\'' +
                ", expenseDesc='" + expenseDesc + '\'' +
                '}';
    }

}
