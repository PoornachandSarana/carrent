package com.carrent.acc;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class excel {

    private static final String FILE_NAME = "vehicle_info.xlsx";

    public static void main(String[] args) {

        // Create a new Excel workbook
        try (Workbook workbook = new XSSFWorkbook()) {

            // Create a new sheet
            Sheet sheet = workbook.createSheet("Vehicle Information");

            // Create headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Vehicle Type", "Vehicle Model", "Number of Passengers", "Transmission", "Cost", "Link"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Add some sample data
            addVehicleInfo(sheet, "Car", "Toyota Camry", 5, "Automatic", 25000, "https://www.example.com/toyota_camry");
            addVehicleInfo(sheet, "SUV", "Honda CR-V", 7, "Automatic", 32000, "https://www.example.com/honda_cr-v");

            // Write the workbook to file
            try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME)) {
                workbook.write(fileOut);
                System.out.println("Excel file created successfully.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read the workbook
        try (FileInputStream fis = new FileInputStream(FILE_NAME)) {
            Workbook readWorkbook = new XSSFWorkbook(fis);
            Sheet readSheet = readWorkbook.getSheetAt(0);
            for (Row row : readSheet) {
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
                        // Handle other cell types if needed
                        default:
                            System.out.print("\t");
                    }
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addVehicleInfo(Sheet sheet, String vehicleType, String vehicleModel, int numPassengers,
                                       String transmission, double cost, String link) {
        int lastRowNum = sheet.getLastRowNum();
        Row newRow = sheet.createRow(lastRowNum + 1);
        newRow.createCell(0).setCellValue(vehicleType);
        newRow.createCell(1).setCellValue(vehicleModel);
        newRow.createCell(2).setCellValue(numPassengers);
        newRow.createCell(3).setCellValue(transmission);
        newRow.createCell(4).setCellValue(cost);
        newRow.createCell(5).setCellValue(link);
    }
}
