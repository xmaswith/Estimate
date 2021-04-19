package com.pri.estimate.Model;

public class InputModel {

    private int result;
    private int price;
    private int count;
    private int number;

    public InputModel(int result, int price, int count, int number) {
        this.result = result;
        this.price = price;
        this.count = count;
        this.number = number;
    }

    public InputModel() {
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
