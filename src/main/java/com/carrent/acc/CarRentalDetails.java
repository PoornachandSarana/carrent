package com.carrent.acc;

public class CarRentalDetails {
    // Class fields
    private String vehicleType; // Type of the vehicle
    private String vehicleModel; // Model of the vehicle
    private String numberOfPassengers; // Number of passengers the vehicle can accommodate
    private String transmission; // Transmission type of the vehicle
    private String cost; // Cost of renting the vehicle
    private String link; // Link to additional information about the vehicle

    // Parameterized constructor
    public CarRentalDetails(String vehicleType, String vehicleModel, String numberOfPassengers, String transmission, String cost, String link) {
        this.vehicleType = vehicleType;
        this.vehicleModel = vehicleModel;
        this.numberOfPassengers = numberOfPassengers;
        this.transmission = transmission;
        this.cost = cost;
        this.link = link;
    }

    // Getters and setters for accessing and modifying class fields

    // Getter for vehicleType
    public String getVehicleType() {
        return vehicleType;
    }

    // Setter for vehicleType
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    // Getter for vehicleModel
    public String getVehicleModel() {
        return vehicleModel;
    }

    // Setter for vehicleModel
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    // Getter for numberOfPassengers
    public String getNumberOfPassengers() {
        return numberOfPassengers;
    }

    // Setter for numberOfPassengers
    public void setNumberOfPassengers(String numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    // Getter for transmission
    public String getTransmission() {
        return transmission;
    }

    // Setter for transmission
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    // Getter for cost
    public String getCost() {
        return cost;
    }

    // Setter for cost
    public void setCost(String cost) {
        this.cost = cost;
    }

    // Getter for link
    public String getLink() {
        return link;
    }

    // Setter for link
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
