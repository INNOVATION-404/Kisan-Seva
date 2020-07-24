package com.kisanseva.kisanseva.entities;

import java.util.Arrays;
import java.util.List;



public class Land {
    private int acresofLand;
    private String soiltype;
    private List<String> crops;
    private List<String>infrastructure;
    private String measureunit;
    private String landName;
    private String firbaseObjectId;
    private String soiltypeImage;
    private String cropsGrown;

    public String getCropsGrown() {
        return cropsGrown;
    }

    public void setCropsGrown(String cropsGrown) {
        this.cropsGrown = cropsGrown;
    }

    public String getSoiltypeImage() {
        return soiltypeImage;
    }

    public void setSoiltypeImage(String soiltypeImage) {
        this.soiltypeImage = soiltypeImage;
    }

    public String getFirbaseObjectId() {
        return firbaseObjectId;
    }

    public void setFirbaseObjectId(String firbaseObjectId) {
        this.firbaseObjectId = firbaseObjectId;
    }

    public String getLandName() {
        return landName;
    }

    public void setLandName(String landName) {
        this.landName = landName;
    }


    public String getMeasureunit() {
        return measureunit;
    }

    public void setMeasureunit(String measureunit) {
        this.measureunit = measureunit;
    }

    public Land() {
    }

    public Land(int acresofLand, String soiltype, List<String> crops, List<String> infrastructure) {
        this.acresofLand = acresofLand;
        this.soiltype = soiltype;
        this.crops = crops;
        this.infrastructure = infrastructure;
    }

    public int getAcresofLand() {
        return acresofLand;
    }

    public void setAcresofLand(int acresofLand) {
        this.acresofLand = acresofLand;
    }

    public String getSoiltype() {
        return soiltype;
    }

    public void setSoiltype(String soiltype) {
        this.soiltype = soiltype;
    }

    public List<String> getCrops() {
        return crops;
    }

    public void setCrops(List<String> crops) {
        this.crops = crops;
    }

    public List<String> getInfrastructure() {
        return infrastructure;
    }

    public void setInfrastructure(List<String> infrastructure) {
        this.infrastructure = infrastructure;
    }

    @Override
    public String toString() {
        return "Land{" +
                "acresofLand=" + acresofLand +
                ", soiltype='" + soiltype + '\'' +
                ", crops=" + getCrops() +
                ", infrastructure=" + getInfrastructure() +getFirbaseObjectId()+getMeasureunit()+
                '}';
    }
}
