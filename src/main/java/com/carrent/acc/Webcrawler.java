package com.carrent.acc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Webcrawler 
{
		public static void processCarRentalsWebsite(WebDriver driver) 
		{
	        // region Opening the carrentals website
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
	            List<WebElement> liElements = olElement.findElements(By.tagName("li"));

	            // Iterate over each li element and perform actions
	            int a = 1;
	            for (WebElement liElement : liElements) {

	                try {
	                    String excelFilePath = "vehicle_info.xlsx";
	                    FileInputStream fileInputStream = new FileInputStream(excelFilePath);
	                    Workbook workbook2 = new HSSFWorkbook(fileInputStream);

	                    Sheet sheet2 = workbook2.getSheetAt(0);

	                    Row newRow = sheet2.createRow(sheet2.getLastRowNum() + 1);

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
	                  

	                    // Save the changes to the Excel file
	                    FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath);
	                    workbook2.write(fileOutputStream);
	                    fileOutputStream.flush();
	                    fileOutputStream.close();
	                    workbook2.close();
	                } catch (Exception e){
	                    e.printStackTrace();
	                }
	                a++;
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error occurred during web automation. Retrying...");
	        }
	    }
	
	
	
	public static void WebCrawlMomondo() {
		System.out.println("Crawling Momondo");
	}
}
