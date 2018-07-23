package com.ingic.auditix.entities;

/**
 * Created on 7/21/18.
 */
public class EnableFilterDataEnt {
    private int minDuration = 0;
    private int maxDuration = 0;
    private int minSubscriber = 0;
    private int maxSubscriber = 0;
    private String countryIDS = null;

    public EnableFilterDataEnt(int minDuration, int maxDuration, int minSubscriber, int maxSubscriber, String countryIDS) {
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.minSubscriber = minSubscriber;
        this.maxSubscriber = maxSubscriber;
        this.countryIDS = countryIDS;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public int getMinSubscriber() {
        return minSubscriber;
    }

    public int getMaxSubscriber() {
        return maxSubscriber;
    }

    public String getCountryIDS() {
        if (countryIDS == null)
            return countryIDS;
        if (countryIDS.equalsIgnoreCase(""))
            return null;
        return countryIDS;
    }
}
