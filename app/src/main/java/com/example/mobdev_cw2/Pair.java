package com.example.mobdev_cw2;

public class Pair{
    private int id; // id equals the element in the row
    private boolean boolValue; // true or false for checked or unchecked
    public Pair(int id, boolean boolValue){
        this.id = id;
        this.boolValue = boolValue;

    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public boolean isBoolValue() {
        return boolValue;
    }
    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }
}
