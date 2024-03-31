package com.carrent.acc;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HTMLDownloader {
    public static void main(String[] args) {
        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\HP\\Downloads\\chromedriver-win64\\chromedriver.exe");

        // Instantiate ChromeOptions to set preferences such as headless mode
        ChromeOptions options = new ChromeOptions();

        // Instantiate ChromeDriver with ChromeOptions
        WebDriver driver = new ChromeDriver(options);

        // URL to download HTML from
        String url = "https://www.expedia.ca/carsearch?locn=Windsor&loc2=&date1=13%2F04%2F2024&date2=14%2F04%2F2024";

        try {
            // Open the URL in the WebDriver
            driver.get(url);
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

            System.out.println("HTML content downloaded and saved to: " + destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the WebDriver
            driver.quit();
        }
    }
}
