package com.pri.estimate.Model;

public class PriceModel {
    int hotelPrice;
    int airPrice;
    int busPrice;
    int foodPrice;
    int ticketPrice;
    int guidePrice;
    int driverPrice;
    int totalPrice;

    public PriceModel(int hotelPrice, int airPrice, int busPrice, int foodPrice, int ticketPrice, int guidePrice, int driverPrice, int totalPrice) {
        this.hotelPrice = hotelPrice;
        this.airPrice = airPrice;
        this.busPrice = busPrice;
        this.foodPrice = foodPrice;
        this.ticketPrice = ticketPrice;
        this.guidePrice = guidePrice;
        this.driverPrice = driverPrice;
        this.totalPrice = totalPrice;
    }

    public PriceModel() {
    }

    public int getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(int hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public int getAirPrice() {
        return airPrice;
    }

    public void setAirPrice(int airPrice) {
        this.airPrice = airPrice;
    }

    public int getBusPrice() {
        return busPrice;
    }

    public void setBusPrice(int busPrice) {
        this.busPrice = busPrice;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getGuidePrice() {
        return guidePrice;
    }

    public void setGuidePrice(int guidePrice) {
        this.guidePrice = guidePrice;
    }

    public int getDriverPrice() {
        return driverPrice;
    }

    public void setDriverPrice(int driverPrice) {
        this.driverPrice = driverPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
