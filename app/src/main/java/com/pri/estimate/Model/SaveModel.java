package com.pri.estimate.Model;

public class SaveModel {

    private int person;
    private int foc;
    private int guide;
    private int hotelCount;
    private int dayCount;
    private double currency;
    private String symbol;
    private String symbolGoal;

    public SaveModel(int person, int foc, int guide, int hotelCount, int dayCount, double currency, String symbol, String symbolGoal) {
        this.person = person;
        this.foc = foc;
        this.guide = guide;
        this.hotelCount = hotelCount;
        this.dayCount = dayCount;
        this.currency = currency;
        this.symbol = symbol;
        this.symbolGoal = symbolGoal;
    }

    public SaveModel() {
    }

    public String getSymbolGoal() {
        return symbolGoal;
    }

    public void setSymbolGoal(String symbolGoal) {
        this.symbolGoal = symbolGoal;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public int getFoc() {
        return foc;
    }

    public void setFoc(int foc) {
        this.foc = foc;
    }

    public int getGuide() {
        return guide;
    }

    public void setGuide(int guide) {
        this.guide = guide;
    }

    public int getHotelCount() {
        return hotelCount;
    }

    public void setHotelCount(int hotelCount) {
        this.hotelCount = hotelCount;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public double getCurrency() {
        return currency;
    }

    public void setCurrency(double currency) {
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
