package com.carrent.acc;

public class CarRentalDetails {
    private String vehicleType;
    private String vehicleModel;
    private String numberOfPassengers;
    private String transmission;
    private String cost;
    private String link;

    // Parameterized constructor
    public CarRentalDetails(String vehicleType, String vehicleModel, String numberOfPassengers, String transmission, String cost, String link) {
        this.vehicleType = vehicleType;
        this.vehicleModel = vehicleModel;
        this.numberOfPassengers = numberOfPassengers;
        this.transmission = transmission;
        this.cost = cost;
        this.link = link;
    }

    // Getters and setters
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(String numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    // toString method to represent object as a string
    @Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Vehicle Type: ").append(vehicleType).append("\n");
    sb.append("Vehicle Model: ").append(vehicleModel).append("\n");
    sb.append("Number of Passengers: ").append(numberOfPassengers).append("\n");
    sb.append("Transmission: ").append(transmission).append("\n");
    sb.append("Cost: ").append(cost).append("\n");
    sb.append("Link: ").append(link).append("\n");
    return sb.toString();
}
}
