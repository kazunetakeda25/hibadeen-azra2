package com.azra2.model;

public class YMCoinGift {
    private int sort;
    public boolean selected = false;
    private int countOfCoins;

    public YMCoinGift(int sort, int count){
        this.sort = sort;
        this.countOfCoins = count;
    }

    public void setSort(int sort1){
        this.sort = sort1;
    }
    public int getSort(){return this.sort;}

    public void setSelected(boolean selected){
        this.selected = selected;
    }
    public boolean isSelected(){return selected;}

    public void setCountOfCoins(int countOfCoins) {
        this.countOfCoins = countOfCoins;
    }
    public int getCountOfCoins() {
        return countOfCoins;
    }
}
