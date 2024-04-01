package com.carrent.acc;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Parser {
   public static void ParseHTML(int duration) {
    // Provide the path to your HTML file using either escaped backslashes or forward slashes
    File input = new File("html_content_selenium.html");
    // Alternatively, you can use forward slashes, which don't need escaping
    // File input = new File("C:/Users/HP/Documents/GitHub/TesterForCode/html_content_selenium.html");

        try {
        // Parse the HTML string into a Document
        Document doc = Jsoup.parse(input, "UTF-8", "");

        // Create a new Excel workbook
        Workbook workbook = new XSSFWorkbook();
        // Create a sheet within the workbook
        Sheet sheet = workbook.createSheet("Vehicle Details");
        // Create a row for the header
        Row headerRow = sheet.createRow(0);
        // Create headers
        String[] headers = {"Vehicle Type", "Vehicle Model", "Number of Passengers", "Transmission", "Cost", "Link"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Select all offer cards
        Elements offerCards = doc.select("li.offer-card-desktop");
        System.out.println(offerCards.size());

        // Iterate through each offer card
        int rowNum = 1;
        for (Element card : offerCards) {
            // Extract details from the card
            String vehicleType = card.select("h3").text(); // Vehicle type
            Elements details = card.select(".uitk-text");
            String model = details.get(0).text().replaceAll(" or similar", ""); // Model (remove "or similar")
            String baseUrl = "https://www.expedia.ca";
            // Extract capacity, transmission, and cost
            Element capacityElement = details.get(1);
            String capacity = capacityElement.text().substring(0, 1); // Capacity (first character)
            String transmission = capacityElement.text().substring(8).replaceAll("\\d+ person.*", "").trim(); // Transmission (remove first 8 characters, "X person" and trim)
            String cost = card.select("span.uitk-lockup-price").text();
            String reserveLink = card.selectFirst("a").attr("href");


            // Remove the first 4 characters from the cost
            if (cost.length() > 4) {
                cost = cost.substring(4);
                cost = cost.replaceAll(",", "");
            }

            cost = String.valueOf(Integer.parseInt(cost)/duration);
            // Create a new row in the sheet
            Row row = sheet.createRow(rowNum++);
            // Write data to the cells
            row.createCell(0).setCellValue(vehicleType);
            row.createCell(1).setCellValue(model);
            row.createCell(2).setCellValue(capacity);
            row.createCell(3).setCellValue(transmission);
            row.createCell(4).setCellValue(cost);
            row.createCell(5).setCellValue(baseUrl+reserveLink); // Link column
        }

        // Write the workbook content to a file
        FileOutputStream fileOut = new FileOutputStream("Web_Crawl_Expedia.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Close the workbook to release resources
        workbook.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
