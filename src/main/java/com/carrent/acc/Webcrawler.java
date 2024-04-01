package com.carrent.acc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;



public class Webcrawler
{
	public static void WebCrawlCarRentals(WebDriver driver, String startDate, String endDate, int duration, String location) {
	    String excelFileName = "Web_Crawl_CarRentals.xlsx";
	    String convertedFromDate = convertDateFormat(startDate, "M/d/yyyy");
	    String convertedToDate = convertDateFormat(endDate, "M/d/yyyy");
	    String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
	    String encodedFromDate = URLEncoder.encode(convertedFromDate, StandardCharsets.UTF_8);
	    String encodedToDate = URLEncoder.encode(convertedToDate, StandardCharsets.UTF_8);
	    String url = String.format("https://www.carrentals.com/carsearch?locn=%s&date1=%s&date2=%s",
	            encodedLocation, encodedFromDate, encodedToDate);
	    driver.get(url);
		driver.manage().window().fullscreen();

	    try {
	        Workbook workbook = new XSSFWorkbook();
	        Sheet sheet = workbook.createSheet("Vehicle Information");

	        // Create headers
	        Row headerRow = sheet.createRow(0);
	        String[] headers = {"Vehicle Type", "Vehicle Model", "Number of Passengers", "Transmission", "Cost", "Link"};
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	        }

	        WebDriverWait wait = new WebDriverWait(driver, 20);
	        List<WebElement> offerCards = Helper.waitForClassElementsVisible(wait, driver, "offer-card-desktop");
	        for(WebElement offerCard: offerCards) {

	            WebElement carTypeElement = offerCard.findElement(By.cssSelector("h3.uitk-heading"));
	            String carType = carTypeElement.getText();
				if(carType.contains("Special")) continue;
	            WebElement carModelElement = offerCard.findElement(By.cssSelector("div.uitk-text"));
	            String carModel = carModelElement.getText();
				carModel = carModel.replace(" or similar", "");
	            WebElement noPersonsElement = offerCard.findElement(By.cssSelector("span.uitk-spacing.text-attribute"));
	            String noPersons = noPersonsElement.getText();
	            WebElement transmissionElement = offerCard.findElement(By.xpath("//span[contains(text(), 'Automatic') or contains(text(), 'Manual')]"));
	            String transmission = transmissionElement.getText();
	            WebElement priceElement = offerCard.findElement(By.cssSelector(".total-price"));
	            String price = priceElement.getText().replaceAll("\\$", "");
	            WebElement linkElement = offerCard.findElement(By.cssSelector("a[data-stid='default-link']"));
	            String link = linkElement.getAttribute("href");
	            int priceNumber = Integer.parseInt(price) / duration;
				price = String.valueOf(priceNumber);

	            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);

	            // Write data to cells
	            Cell cell0 = newRow.createCell(0);
	            cell0.setCellValue(carType);
	            Cell cell1 = newRow.createCell(1);
	            cell1.setCellValue(carModel);
	            Cell cell2 = newRow.createCell(2);
	            cell2.setCellValue(noPersons);
	            Cell cell3 = newRow.createCell(3);
	            cell3.setCellValue(transmission);
	            Cell cell4 = newRow.createCell(4);
	            cell4.setCellValue(price);
	            Cell cell5 = newRow.createCell(5);
	            cell5.setCellValue(link);
	        }

	        // Write the workbook to file
	        try (FileOutputStream fileOut = new FileOutputStream(excelFileName)) {
	            workbook.write(fileOut);
	            workbook.close();
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Error has occurred during the web crawl");
	    }
	}

		public static String convertDateFormat(String inputDate, String outputFormat) {
	        String formattedDate = "";

	        try {
	            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);

	            Date date = inputDateFormat.parse(inputDate);
	            formattedDate = outputDateFormat.format(date);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }

	        return formattedDate;
	    }
	
		public static void WebCrawlOrbitz(WebDriver driver, String startDate, String endDate, int duration, String location) {
			String excelFileName = "Web_Crawl_Orbitz.xlsx";
			// String location = "Toronto";
			// String fromDate = "2024-03-24";
			// String toDate = "2024-03-28";
			String convertedFromDate = convertDateFormat(startDate, "M/d/yyyy");
			String convertedToDate = convertDateFormat(endDate, "M/d/yyyy");
			String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
			String encodedFromDate = URLEncoder.encode(convertedFromDate, StandardCharsets.UTF_8);
			String encodedToDate = URLEncoder.encode(convertedToDate, StandardCharsets.UTF_8);
			String url = String.format("https://www.orbitz.com/carsearch?locn=%s&date1=%s&date2=%s",
                    encodedLocation, encodedFromDate, encodedToDate);
			driver.get(url);
			driver.manage().window().fullscreen();

			try {
				Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Vehicle Information");

				 // Create headers
				 Row headerRow = sheet.createRow(0);
				 String[] headers = {"Vehicle Type", "Vehicle Model", "Number of Passengers", "Transmission", "Cost", "Link"};
				 for (int i = 0; i < headers.length; i++) {
					 Cell cell = headerRow.createCell(i);
					 cell.setCellValue(headers[i]);
				 }

				WebDriverWait wait = new WebDriverWait(driver, 20);
				// List<WebElement> offerCards = driver.findElements(By.cssSelector(".offer-card-desktop"));
				List<WebElement> offerCards = Helper.waitForClassElementsVisible(wait, driver, "offer-card-desktop");
				for(WebElement offerCard: offerCards) {

					WebElement carNameElement = offerCard.findElement(By.className("uitk-text"));
					String carName = carNameElement.getText();
					carName = carName.replace(" or similar", "");
					carName = carName.replace(" or larger - Vehicle determined upon pick-up", "");
					if(carName.contains("Managers Special")) continue;
					WebElement carTypeElement = offerCard.findElement(By.tagName("h3"));
					String carType = carTypeElement.getText();
					WebElement noPersonsElement = offerCard.findElement(By.cssSelector("div.uitk-text span"));
					String noPersons = noPersonsElement.getText();
					WebElement transmissionElement = offerCard.findElement(By.cssSelector("div.uitk-text span:nth-child(5)"));
					String transmission = transmissionElement.getText();
					WebElement priceElement = offerCard.findElement(By.cssSelector(".total-price"));
					String price = priceElement.getText();
					WebElement linnkElement = offerCard.findElement(By.cssSelector("a[data-stid='default-link']"));
					String link = linnkElement.getAttribute("href");
					// WebElement priceQualifierEle = offerCard.findElement(By.cssSelector(".per-day-price-qualifier"));
					// String priceQualifier = priceQualifierEle.getText();

//					System.out.println(carName + " " +carType+ " " + noPersons + " " + transmission + " " + price);
//					System.out.println("link: "+link);
					Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
					price = price.replaceAll("\\$", "");
					int priceNumber = Integer.parseInt(price) / duration;

					price = String.valueOf(priceNumber);
					
					Cell cell0 = newRow.createCell(0);
                    cell0.setCellValue(carType);
                    Cell cell1 = newRow.createCell(1);
                    cell1.setCellValue(carName);
                    Cell cell2 = newRow.createCell(2);
                    cell2.setCellValue(noPersons);
                    Cell cell3 = newRow.createCell(3);
                    cell3.setCellValue(transmission);
                    Cell cell4 = newRow.createCell(4);
                    cell4.setCellValue(price);
                    Cell cell5 = newRow.createCell(5);
                    cell5.setCellValue(link);
				}

				 // Write the workbook to file
				 try (FileOutputStream fileOut = new FileOutputStream(excelFileName)) {
					workbook.write(fileOut);
					workbook.close();
				}
			} catch (Exception e) {
				throw new RuntimeException("Error has occurred during the web crawl");
			}
		}
		public static void HTMLParse(WebDriver driver, String startDate, String endDate, String location){

		}
}
