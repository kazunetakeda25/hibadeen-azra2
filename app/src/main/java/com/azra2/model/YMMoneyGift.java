package com.azra2.model;

public class YMMoneyGift {
    private int sort;
    public boolean selected = false;
    private double worth;

    public YMMoneyGift(int sort, double worth){
        this.sort = sort;
        this.worth = worth;
    }

    public void setSort(int sort1){
        this.sort = sort1;
    }
    public int getSort(){return this.sort;}

    public void setSelected(boolean selected){
        this.selected = selected;
    }
    public boolean isSelected(){return selected;}

    public void setWorth(float worth){
        this.worth = worth;
    }
    public double getWorth(){return this.worth;}
}
