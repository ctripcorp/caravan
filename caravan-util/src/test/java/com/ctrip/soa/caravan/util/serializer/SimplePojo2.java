package com.ctrip.soa.caravan.util.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class SimplePojo2 {

    public static final SimplePojo2 SAMPLE;

    static {
        SAMPLE = new SimplePojo2();
        SAMPLE.setaInt(10);
        SAMPLE.setbLong(20L);
        SAMPLE.setcFloat(5f);
        SAMPLE.setdDouble(10);
        SAMPLE.seteString("test");
        SAMPLE.setfStringArray(new String[] { "1", "2", "a" });
        SAMPLE.setgStringList(new ArrayList<String>());
        SAMPLE.sethStringMap(new HashMap<String, String>());
    }

    private int aInt;
    private long bLong;
    private float cFloat;
    private double dDouble;
    private String eString;
    private String[] fStringArray;
    private List<String> gStringList;
    private Map<String, String> hStringMap;

    public int getaInt() {
        return aInt;
    }

    public void setaInt(int aInt) {
        this.aInt = aInt;
    }

    public long getbLong() {
        return bLong;
    }

    public void setbLong(long bLong) {
        this.bLong = bLong;
    }

    public float getcFloat() {
        return cFloat;
    }

    public void setcFloat(float cFloat) {
        this.cFloat = cFloat;
    }

    public double getdDouble() {
        return dDouble;
    }

    public void setdDouble(double dDouble) {
        this.dDouble = dDouble;
    }

    public String geteString() {
        return eString;
    }

    public void seteString(String eString) {
        this.eString = eString;
    }

    public String[] getfStringArray() {
        return fStringArray;
    }

    public void setfStringArray(String[] fStringArray) {
        this.fStringArray = fStringArray;
    }

    public List<String> getgStringList() {
        return gStringList;
    }

    public void setgStringList(List<String> gStringList) {
        this.gStringList = gStringList;
    }

    public Map<String, String> gethStringMap() {
        return hStringMap;
    }

    public void sethStringMap(Map<String, String> hStringMap) {
        this.hStringMap = hStringMap;
    }

}
