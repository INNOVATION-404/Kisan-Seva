package com.kisanseva.kisanseva.entities;

/**
 * Created by Saikrishna on 20/02/2019.
 */

public class Crop {
    private int cropimage;
    private String cropname;

    public Crop() {
    }

    public Crop(int cropimage, String cropname) {
        this.cropimage = cropimage;
        this.cropname = cropname;
    }

    public int getCropimage() {
        return cropimage;
    }

    public void setCropimage(int cropimage) {
        this.cropimage = cropimage;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    @Override
    public String toString() {
        return "Crop{" +
                "cropimage=" + cropimage +
                ", cropname='" + cropname + '\'' +
                '}';
    }
}
