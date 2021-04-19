package com.pri.estimate.Model;

public class EstimateModel {
    private String date;
    private long time;
    private String person;
    private String foc;
    private String guide;
    private String hotelPrice;
    private String airPrice;
    private String busPrice;
    private String foodPrice;
    private String ticketPrice;
    private String guidePrice;
    private String driverPrice;
    private String totalPrice;
    private String exchangeTotalPrice;
    private String saveId;
    private String title;
    private String symbol;
    private String symbolGoal;
    private String discount;
    private double currency;

    public EstimateModel(String date, long time, String person, String foc, String guide, String hotelPrice, String airPrice,
                         String busPrice, String foodPrice, String ticketPrice, String guidePrice, String driverPrice, String totalPrice,
                         String saveId, String title, String symbol, String symbolGoal, String exchangeTotalPrice, String discount, double currency) {
        this.date = date;
        this.time = time;
        this.person = person;
        this.foc = foc;
        this.guide = guide;
        this.hotelPrice = hotelPrice;
        this.airPrice = airPrice;
        this.busPrice = busPrice;
        this.foodPrice = foodPrice;
        this.ticketPrice = ticketPrice;
        this.guidePrice = guidePrice;
        this.driverPrice = driverPrice;
        this.totalPrice = totalPrice;
        this.saveId = saveId;
        this.title = title;
        this.symbol = symbol;
        this.symbolGoal = symbolGoal;
        this.exchangeTotalPrice = exchangeTotalPrice;
        this.discount = discount;
        this.currency = currency;
    }

    public double getCurrency() {
        return currency;
    }

    public void setCurrency(double currency) {
        this.currency = currency;
    }

    public String getExchangeTotalPrice() {
        return exchangeTotalPrice;
    }

    public void setExchangeTotalPrice(String exchangeTotalPrice) {
        this.exchangeTotalPrice = exchangeTotalPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSymbolGoal() {
        return symbolGoal;
    }

    public void setSymbolGoal(String symbolGoal) {
        this.symbolGoal = symbolGoal;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSaveId() {
        return saveId;
    }

    public void setSaveId(String saveId) {
        this.saveId = saveId;
    }

    public EstimateModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getFoc() {
        return foc;
    }

    public void setFoc(String foc) {
        this.foc = foc;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public String getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(String hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public String getAirPrice() {
        return airPrice;
    }

    public void setAirPrice(String airPrice) {
        this.airPrice = airPrice;
    }

    public String getBusPrice() {
        return busPrice;
    }

    public void setBusPrice(String busPrice) {
        this.busPrice = busPrice;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getGuidePrice() {
        return guidePrice;
    }

    public void setGuidePrice(String guidePrice) {
        this.guidePrice = guidePrice;
    }

    public String getDriverPrice() {
        return driverPrice;
    }

    public void setDriverPrice(String driverPrice) {
        this.driverPrice = driverPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
