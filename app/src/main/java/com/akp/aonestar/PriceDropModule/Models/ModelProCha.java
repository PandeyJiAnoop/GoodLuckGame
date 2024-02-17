package com.akp.aonestar.PriceDropModule.Models;

public class ModelProCha {
    String contNum, proName;

    public ModelProCha(String contNum, String proName) {
        this.contNum = contNum;
        this.proName = proName;
    }

    public String getContNum() {
        return contNum;
    }

    public String getProName() {
        return proName;
    }
}
