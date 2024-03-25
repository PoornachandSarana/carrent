package com.carrent.acc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Helper {
	public static WebElement waitForElementVisible(WebDriverWait wait, WebDriver driver, String xpath) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return element;
    }

    public static WebElement waitForClassElementVisible(WebDriverWait wait, WebDriver driver, String classname) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(classname)));
        return element;
    }

    public static List<WebElement> waitForClassElementsVisible(WebDriverWait wait, WebDriver driver, String classname) {
        List<WebElement> element = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className(classname)));
        return element;
    }
	
	public static void typeIntoInputField(WebDriverWait wait, WebDriver driver, String xpath, String text) {
        // Type the specified text into the input field
        WebElement element = waitForElementVisible(wait, driver, xpath);

        element.sendKeys(text);
    }

    public static void clearInputField(WebDriverWait wait, WebDriver driver, String xpath) {
        // Type the specified text into the input field
        WebElement element = waitForElementVisible(wait, driver, xpath);

        element.clear();
    }
	
	public static void waiter(int duration){
        try {
            Thread.sleep(duration);  // 5000 milliseconds = 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
	public static String getCurrentTimestamp() {
        // Get current timestamp in a specific format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
	
	public static String formatDate(String inputDate) {
        // Parse input date string
        LocalDate date = LocalDate.parse(inputDate);

        // Define the desired output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

        // Format the date into the desired format
        String formattedDate = date.format(formatter);

        return formattedDate;
    }
    public static String getInputString(Scanner scanner) {
        String userInput = scanner.nextLine();
        return userInput.trim();
    }
}
