package com.carrent.acc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HTMLFileDownloader {
    public static void DownloadHTML(WebDriver driver, String startDate, String endDate, String location) {

		String convertedFromDate = Webcrawler.convertDateFormat(startDate, "dd/MM/yyyy");
        String convertedToDate = Webcrawler.convertDateFormat(endDate, "dd/MM/yyyy");

        String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
		String encodedFromDate = URLEncoder.encode(convertedFromDate, StandardCharsets.UTF_8);
		String encodedToDate = URLEncoder.encode(convertedToDate, StandardCharsets.UTF_8);
		String url = String.format("https://www.expedia.ca/carsearch?locn=%s&loc2=&date1=%s&date2=%s",
                    encodedLocation, encodedFromDate, encodedToDate);


        try {
            // Open the URL in the WebDriver
            driver.get(url);
    		driver.manage().window().fullscreen();

            try {
                Thread.sleep(10000); // You can remove this line if you don't need to pause
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Get the HTML content of the page
            String htmlContent = driver.getPageSource();

            // Save HTML content to a file
            String destinationFile = "html_content_selenium.html";
            BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile));
            writer.write(htmlContent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the WebDriver
            driver.quit();
        }
    }
}
