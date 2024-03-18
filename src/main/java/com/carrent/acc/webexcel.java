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
