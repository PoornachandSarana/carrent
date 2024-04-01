package com.carrent.acc;
import com.carrent.acc.MostFrequentlySearchedCars;


import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // if using XLSX files
import org.apache.poi.hssf.usermodel.HSSFWorkbook; // if using XLS files

public class InvertedIndex {
    private Map<String, List<String[]>> invertedIndex;

    public InvertedIndex() {
        invertedIndex = new HashMap<>();
    }

    public void buildIndex(String[] excelFiles) {
        for (String file : excelFiles) {
            try {
                Workbook workbook;
                if (file.endsWith(".xlsx")) {
                    workbook = new XSSFWorkbook(new File(file));
                } else if (file.endsWith(".xls")) {
                    workbook = new HSSFWorkbook(new FileInputStream(new File(file)));
                } else {
                    System.out.println("Unsupported file format: " + file);
                    continue;
                }

                Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

                // Iterate over rows in the sheet
                for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header row
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        // Extract data from cells
                        String type = row.getCell(0).getStringCellValue();
                        String model = row.getCell(1).getStringCellValue();
                        String passengers = row.getCell(2).getStringCellValue();
                        String transmission = row.getCell(3).getStringCellValue();
                        String cost = row.getCell(4).getStringCellValue();
                        String link = row.getCell(5).getStringCellValue();

                        String[] attributes = {type, model, passengers, transmission, cost, link};

                        // Update inverted index
                        if (!invertedIndex.containsKey(model)) {
                            invertedIndex.put(model, new ArrayList<>());
                        }
                        invertedIndex.get(model).add(attributes);
                    }
                }

                workbook.close();
            } catch (Exception e) {
                System.out.println("Error processing file: " + file);
                e.printStackTrace();
            }
        }
    }

    public List<String[]> filterByCriteria(String criteria, String value) {
        List<String[]> filteredResults = new ArrayList<>();
        // Iterate over values in inverted index
        for (List<String[]> modelAttributes : invertedIndex.values()) {
            // Iterate over attributes for each model
            for (String[] attributes : modelAttributes) {
                switch (criteria.toLowerCase()) {
                    // Check if attribute matches the value for the specified criteria
                    case "vehicle type":
                        if (attributes[0].equalsIgnoreCase(value)) {
                            filteredResults.add(attributes);
                        }
                        break;
                    case "number of passengers":
                        if (attributes[2].equalsIgnoreCase(value)) {
                            filteredResults.add(attributes);
                        }
                        break;
                    case "transmission":
                        if (attributes[3].equalsIgnoreCase(value)) {
                            filteredResults.add(attributes);
                        }
                        break;
                    case "cost":
                        String[] costRange = value.split("-");
                        if (costRange.length == 2) {
                            double minCost = Double.parseDouble(costRange[0]);
                            double maxCost = Double.parseDouble(costRange[1]);
                            double itemCost = Double.parseDouble(attributes[4]);
                            if (itemCost >= minCost && itemCost <= maxCost) {
                                filteredResults.add(attributes);
                            }
                        }
                        break;
                    case "link":
                        if (attributes[5].equalsIgnoreCase(value)) {
                            filteredResults.add(attributes);
                        }
                        break;
                    default:
                        System.out.println("Invalid criteria.");
                }
            }
        }
        return filteredResults;
    }

    public void displayOptions(String criteria, MostFrequentlySearchedCars wordTracker, Scanner scanner, TreeMap<String, Integer> vehicleTypeCount) {
        if (criteria.equals("Vehicle Type") || criteria.equals("Vehicle Model")
                || criteria.equals("Number of Passengers") || criteria.equals("Transmission")
                || criteria.equals("Cost")) {
            if (criteria.equals("Cost")) {
                System.out.println("Enter the cost range in the format 'min-max' (e.g., 100-500):");
            } else {
                System.out.println("All available " + criteria + ":");
                List<String> options = new ArrayList<>();
                for (List<String[]> modelAttributes : invertedIndex.values()) {
                    for (String[] attributes : modelAttributes) {
                        switch (criteria.toLowerCase()) {
                            case "vehicle type":
                                options.add(attributes[0]);
                                break;
                            case "number of passengers":
                                options.add(attributes[2]);
                                break;
                            case "transmission":
                                options.add(attributes[3]);
                                break;
                            case "cost":
                                options.add(attributes[4]);
                                break;
                        }
                    }
                }
                Set<String> uniqueOptions = new HashSet<>(options);
                int count = 1;
                Map<Integer, String> optionMap = new HashMap<>();
                for (String option : uniqueOptions) {
                    optionMap.put(count, option);
                    System.out.println(count + ". " + option);
                    count++;
                }

                System.out.println("Choose a number from the list:");
                String userChoiceStr = scanner.nextLine();
                int userChoice;
                try {
                    userChoice = Integer.parseInt(userChoiceStr);
                } catch (Exception e) {
                    userChoice = optionMap.size() + 1;
                }
                String selectedOption = optionMap.get(userChoice);

                if (selectedOption != null) {
                    // Call the searchWords method from MostFrequentlySearchedCars class      
                    if(criteria.equals("Vehicle Type"))  {
                        if(vehicleTypeCount.get(selectedOption) != null) {
                            System.out.println("There are "+ vehicleTypeCount.get(selectedOption)+ " options of type " + selectedOption);
                        }
                        wordTracker.searchWords(selectedOption);
                    }

                    System.out.println("Filtered Results based on " + criteria + " - " + selectedOption + ":");
                    List<String[]> filteredResults = filterByCriteria(criteria, selectedOption);
                    for (String[] attributes : filteredResults) {
                        // Format and print each attribute
                        System.out.println("Vehicle Type: " + attributes[0]);
                        System.out.println("Vehicle Model: " + attributes[1]);
                        System.out.println("Number of Passengers: " + attributes[2]);
                        System.out.println("Transmission: " + attributes[3]);
                        System.out.println("Cost: " + attributes[4]);
                        System.out.println("Link: " + attributes[5]);
                        System.out.println(); // Add an empty line for separation
                    }
                } else {
                    System.out.println("You have entered an invalid input");
                }

                return;
            }
            String value = scanner.nextLine();
            List<String[]> filteredResults = filterByCriteria(criteria, value);
            if (filteredResults.isEmpty()) {
                if(criteria.equalsIgnoreCase("cost")) {
                    String regex = "\\d+-\\d+";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(value);
                    if (!matcher.matches()) {
                        System.out.println("Invalid Format Entered");
                    } else {
                        System.out.println("No results found");
                    }
                }
                else
                System.out.println("No results found.");
            } else {
                System.out.println("Filtered Results based on " + criteria + " - " + value + ":");
                for (String[] attributes : filteredResults) {
                    // Format and print each attribute
                    System.out.println("Vehicle Type: " + attributes[0]);
                    System.out.println("Vehicle Model: " + attributes[1]);
                    System.out.println("Number of Passengers: " + attributes[2]);
                    System.out.println("Transmission: " + attributes[3]);
                    System.out.println("Cost: " + attributes[4]);
                    System.out.println("Link: " + attributes[5]);
                    System.out.println(); // Add an empty line for separation
                }
            }
        }
    }

    public static void FilterFromExcel(Scanner scanner, MostFrequentlySearchedCars wordTracker, TreeMap<String, Integer> vehicleTypeCount) {

        InvertedIndex index = new InvertedIndex();
        String[] excelFiles = {"Web_Crawl_CarRentals.xlsx", "Web_Crawl_Orbitz.xlsx", "Web_Crawl_Expedia.xlsx"}; // Provide the paths to your Excel files
        index.buildIndex(excelFiles);

        System.out.println("Enter the filtering criteria:");
        System.out.println("1. Vehicle Type");
        System.out.println("2. Number of Passengers");
        System.out.println("3. Transmission");
        System.out.println("4. Cost");

        String enteredChoice = scanner.nextLine();

        int choice;
        try {
            choice = Integer.parseInt(enteredChoice);
        } catch (Exception e) {
            choice = 5;
        }

        String criteria = "";
        switch (choice) {
            case 1:
                criteria = "Vehicle Type";
                wordTracker.displayTopFrequentWords();
                break;
            case 2:
                criteria = "Number of Passengers";
                break;
            case 3:
                criteria = "Transmission";
                break;
            case 4:
                criteria = "Cost";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        index.displayOptions(criteria, wordTracker, scanner, vehicleTypeCount);

    }
}
