package com.carrent.acc;

import java.util.List;
import java.util.TreeMap;

/*  The CarWordCount has two static methods. The first method, 
 *  countCarTypes, accepts a list of CarRentalDetails objects and
 *  returns a TreeMap<String, Integer>. The CarRentalDetails class 
 *  contains the vehicle properties like type, model, no of passengers, transmission, cost and link.
 */
public class CarWordCount {
    public static TreeMap<String, Integer> countCarTypes(List<CarRentalDetails> carRentalDetailsList) {
        TreeMap<String, Integer> vehicleTypeCount = new TreeMap<>();
        
        for (CarRentalDetails details : carRentalDetailsList) {
            String vehicleType = details.getVehicleType();
            vehicleTypeCount.put(vehicleType, vehicleTypeCount.getOrDefault(vehicleType, 0) + 1);
        }
        return vehicleTypeCount;
    }
    /* printAllVehiclesCounted, takes a TreeMap<String, Integer> as input
     *  and prints out the count of each vehicle type stored in the map. 
     *  It iterates through the keys of the TreeMap, representing the vehicle types,
     *   retrieves the count associated with each type, and prints it to the console 
     *   alongside the respective vehicle type.
     */
    public static void printAllVehiclesCounted(TreeMap<String, Integer> vehicleTypeCount) {
        // Print the count of vehicle types
        for (String vehicleType : vehicleTypeCount.keySet()) {
            int count = vehicleTypeCount.get(vehicleType);
            System.out.println(vehicleType + ": " + count);
        }
    }
}
