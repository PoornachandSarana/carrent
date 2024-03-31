package com.carrent.acc;

import java.util.List;
import java.util.TreeMap;

public class CarWordCount {
    public static TreeMap<String, Integer> countCarTypes(List<CarRentalDetails> carRentalDetailsList) {
        TreeMap<String, Integer> vehicleTypeCount = new TreeMap<>();
        
        for (CarRentalDetails details : carRentalDetailsList) {
            String vehicleType = details.getVehicleType();
            vehicleTypeCount.put(vehicleType, vehicleTypeCount.getOrDefault(vehicleType, 0) + 1);
        }
        return vehicleTypeCount;
    }

    public static void printAllVehiclesCounted(TreeMap<String, Integer> vehicleTypeCount) {
        // Print the count of vehicle types
        for (String vehicleType : vehicleTypeCount.keySet()) {
            int count = vehicleTypeCount.get(vehicleType);
            System.out.println(vehicleType + ": " + count);
        }
    }
}
