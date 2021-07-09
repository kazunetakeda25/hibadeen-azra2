package com.azra2.model;

public class YMCoinPack {
    private int count;
    public boolean selected = false;
    private double price;

    public YMCoinPack(int count, double price){
        this.count = count;
        this.price = price;
    }

    public void setCount(int count1){
        this.count = count1;
    }
    public int getCount(){return this.count;}

    public void setSelected(boolean selected){
        this.selected = selected;
    }
    public boolean isSelected(){return selected;}

    public void setPrice(float price){
        this.price = price;
    }
    public double getPrice(){return this.price;}


}
