package com.carrent.acc;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class webexcel {

    private static final String FILE_NAME = "vehicle_info.xlsx";

    public static void main(String[] args) {
        // Setup WebDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\USER\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Crawl and Process Car Rentals Website
        processCarRentalsWebsite(driver);

        // Close WebDriver
        driver.quit();

        // Read and Print the Excel file
        readExcel();
    }

    public static void processCarRentalsWebsite(WebDriver driver) 
    {
        // Opening the carrentals website
        driver.get("https://www.carrentals.com/");
        try {
            // Initializing explicit wait
            WebDriverWait wait = new WebDriverWait(driver, 20);

            String pickUpLocation = "//*[@id=\"wizard-car-pwa-1\"]/div[1]/div[1]/div/div/div/div/div[2]/div[1]/button";
            WebElement pickUpLocationButton = Helper.waitForElementVisible(wait, driver, pickUpLocation);
            pickUpLocationButton.click();

            String pickUpLocationInput = "//*[@id=\"location-field-locn\"]";
            String pickUpLocationVal = "Windsor";
            Helper.typeIntoInputField(wait, driver, pickUpLocationInput, pickUpLocationVal);

            String selectLocationOption = "//*[@id=\"location-field-locn-menu\"]/section/div/div[2]/div/ul/li[1]/div/button";
            WebElement selectLocationOptionButton = Helper.waitForElementVisible(wait, driver, selectLocationOption);
            selectLocationOptionButton.click();

            String search = "//*[@id=\"wizard-car-pwa-1\"]/div[3]/div[2]/button";
            WebElement searchButton = Helper.waitForElementVisible(wait, driver, search);
            searchButton.click();

            Helper.waiter(5000);

            WebElement olElement = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/main/div[2]/div[2]/div/div[1]/div[2]/div[2]/div[2]/ol"));

            // Locate all li elements within the ol element
            java.util.List<WebElement> liElements = olElement.findElements(By.tagName("li"));

            // Create a new Excel workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Vehicle Information");

            // Create headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Vehicle Type", "Vehicle Model", "Number of Passengers", "Transmission", "Cost", "Link"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Iterate over each li element and perform actions
            int a = 1;
            for (WebElement liElement : liElements) {
                try {
                    Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
                    
                    try {
                    WebElement typeEl = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/main/div[2]/div[2]/div/div[1]/div[2]/div[2]/div[2]/ol/li[" + a + "]/div/div/div[2]/div/div[1]/div[2]/section[1]/div[1]/div[1]/div"));
                    String carType = typeEl.getText().trim();

                    WebElement modelEl = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/main/div[2]/div[2]/div/div[1]/div[2]/div[2]/div[2]/ol/li[" + a + "]/div/div/div[2]/div/div[1]/div[1]/h3"));
                    String model = modelEl.getText().trim();

                    WebElement passengerEl = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/main/div[2]/div[2]/div/div[1]/div[2]/div[2]/div[2]/ol/li[" + a + "]/div/div/div[2]/div/div[1]/div[2]/section[1]/div[1]/div[2]/div/span[1]"));
                    String passengers = passengerEl.getText().trim();

                    WebElement transmissionEl = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/main/div[2]/div[2]/div/div[1]/div[2]/div[2]/div[2]/ol/li[" + a + "]/div/div/div[2]/div/div[1]/div[2]/section[1]/div[1]/div[2]/div/span[3]"));
                    String transmission = transmissionEl.getText().trim();

                    WebElement priceEl = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/main/div[2]/div[2]/div/div[1]/div[2]/div[2]/div[2]/ol/li[" + a + "]/div/div/div[2]/div/div[2]/div[1]/div/section/div[1]/span"));
                    String cost = priceEl.getText().trim();
                                        
                    Cell cell0 = newRow.createCell(0);
                    cell0.setCellValue(carType.replace("or", "").replace("similar", "").replace("- Vehicle determined upon pick-up", ""));
                    Cell cell1 = newRow.createCell(1);
                    cell1.setCellValue(model);
                    Cell cell2 = newRow.createCell(2);
                    cell2.setCellValue(passengers);
                    Cell cell3 = newRow.createCell(3);
                    cell3.setCellValue(transmission);
                    Cell cell4 = newRow.createCell(4);
                    cell4.setCellValue(cost.replaceAll("\\$", ""));
                    Cell cell5 = newRow.createCell(5);
                    cell5.setCellValue("carrentals");
                    } catch(Exception e) {
                    	System.out.println("Error at index "+a);
                    }

                  

                    a++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Write the workbook to file
            try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME)) {
                workbook.write(fileOut);
                System.out.println("Excel file created successfully.");
                workbook.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred during web automation. Retrying...");
        }
    }

    public static void readExcel() {
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
            readWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//==================
//package com.carrent.acc;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
//import java.util.Comparator;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//public class pagerank 
//{
//
//	static void displayPageRanking(String filePath1, String filePath2, String targetColumn) throws IOException 
//	{
//        // Get the page ranking for the specified column (Vehicle Type) in both files
//        Map<String, Integer> pageRanking1 = getPageRanking(filePath1, targetColumn);
//        Map<String, Integer> pageRanking2 = getPageRanking(filePath2, targetColumn);
//
//        // Compare occurrences and display the sorted result
//        compareAndDisplayPageRanking(pageRanking1, pageRanking2);
//    }
//	private static Map<String, Integer> getPageRanking(String filePath, String targetColumn) throws IOException {
//        Map<String, Integer> keywordOccurrences = new HashMap<>();
//
//        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
//             Workbook workbook = WorkbookFactory.create(fileInputStream)) 
//        {
//
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // Find the header row to get the column index
//            Row headerRow = sheet.getRow(0);
//            int targetColumnIndex = findColumnIndex(headerRow, targetColumn);
//
//            if (targetColumnIndex == -1) 
//            {
//                throw new IllegalArgumentException("Column not found: " + targetColumn);
//            }
//            // Iterate through rows and update keyword occurrences for the specified column
//            for (Row row : sheet) {
//                Cell cell = row.getCell(targetColumnIndex);
//
//                if (cell != null && cell.getCellType() == CellType.STRING) {
//                    String cellValue = cell.getStringCellValue().toLowerCase();
//
//                    // Split the cell content into keywords
//                    String[] keywords = cellValue.split("\\s+");
//
//                    // Update the occurrences of each keyword
//                    for (String keyword : keywords) {
//                        keywordOccurrences.put(keyword, keywordOccurrences.getOrDefault(keyword, 0) + 1);
//                    }
//                }
//            }
//        }
//
//        return keywordOccurrences;
//    }
//
//	private static int findColumnIndex(Row headerRow, String targetColumn)
//	{
//        if (headerRow != null) {
//            for (Cell cell : headerRow) {
//                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase(targetColumn)) {
//                    return cell.getColumnIndex();
//                }
//            }
//        }
//        return -1;
//    }
//	private static void compareAndDisplayPageRanking(Map<String, Integer> pageRanking1, Map<String, Integer> pageRanking2) {
//	    // Merge the two page rankings
//	    Map<String, Integer> mergedPageRanking = new HashMap<>(pageRanking1);
//	    for (Map.Entry<String, Integer> entry : pageRanking2.entrySet()) {
//	        mergedPageRanking.merge(entry.getKey(), entry.getValue(), Integer::sum);
//	    }
//
//	    // Sort the merged page ranking by occurrences in descending order
//	    Map<String, Integer> sortedMergedPageRanking = mergedPageRanking.entrySet().stream()
//	            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//	            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
//	                    (oldValue, newValue) -> oldValue, LinkedHashMap::new));
//
//	    // Display the sorted merged page ranking along with occurrences in each file
//	    System.out.println("\n-----------------------------------------");
//	    System.out.println("Ranking based on Vehicle Types Occurrences");
//	    System.out.println("-----------------------------------------");
//
//	    for (Map.Entry<String, Integer> entry : sortedMergedPageRanking.entrySet()) {
//	        String vehicleType = entry.getKey();
//	        int occurrences1 = pageRanking1.getOrDefault(vehicleType, 0);
//	        int occurrences2 = pageRanking2.getOrDefault(vehicleType, 0);
//	        int totalOccurrences = occurrences1 + occurrences2;
//
//	        System.out.println(vehicleType + ": " + totalOccurrences + " (File 1: " + occurrences1 + ", File 2: " + occurrences2 + ")");
//	    }
//	}
//}
//
