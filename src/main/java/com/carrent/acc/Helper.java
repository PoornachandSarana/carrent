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

/**
 * Helper class provides utility methods for handling WebDriver interactions and date formatting.
 */
public class Helper {

    /**
     * Waits for the visibility of a WebElement based on its XPath.
     * @param wait WebDriverWait instance for waiting
     * @param driver WebDriver instance
     * @param xpath XPath expression of the WebElement
     * @return WebElement that becomes visible
     */
    public static WebElement waitForElementVisible(WebDriverWait wait, WebDriver driver, String xpath) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return element;
    }

    /**
     * Waits for the visibility of a WebElement based on its class name.
     * @param wait WebDriverWait instance for waiting
     * @param driver WebDriver instance
     * @param classname Class name of the WebElement
     * @return WebElement that becomes visible
     */
    public static WebElement waitForClassElementVisible(WebDriverWait wait, WebDriver driver, String classname) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(classname)));
        return element;
    }

    /**
     * Waits for all WebElements with a specific class name to become visible.
     * @param wait WebDriverWait instance for waiting
     * @param driver WebDriver instance
     * @param classname Class name of the WebElements
     * @return List of WebElements that become visible
     */
    public static List<WebElement> waitForClassElementsVisible(WebDriverWait wait, WebDriver driver, String classname) {
        List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className(classname)));
        return elements;
    }
    
    /**
     * Types the specified text into an input field identified by XPath.
     * @param wait WebDriverWait instance for waiting
     * @param driver WebDriver instance
     * @param xpath XPath expression of the input field
     * @param text Text to be typed into the input field
     */
    public static void typeIntoInputField(WebDriverWait wait, WebDriver driver, String xpath, String text) {
        WebElement element = waitForElementVisible(wait, driver, xpath);
        element.sendKeys(text);
    }

    /**
     * Clears the text from an input field identified by XPath.
     * @param wait WebDriverWait instance for waiting
     * @param driver WebDriver instance
     * @param xpath XPath expression of the input field
     */
    public static void clearInputField(WebDriverWait wait, WebDriver driver, String xpath) {
        WebElement element = waitForElementVisible(wait, driver, xpath);
        element.clear();
    }
    
    /**
     * Pauses execution for a specified duration in milliseconds.
     * @param duration Duration to wait in milliseconds
     */
    public static void waiter(int duration){
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the current timestamp in "yyyy-MM-dd HH:mm:ss" format.
     * @return Current timestamp
     */
    public static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
    
    /**
     * Formats the input date string from "yyyy-MM-dd" to "M/d/yyyy".
     * @param inputDate Date string in "yyyy-MM-dd" format
     * @return Formatted date string in "M/d/yyyy" format
     */
    public static String formatDate(String inputDate) {
        LocalDate date = LocalDate.parse(inputDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        return date.format(formatter);
    }
    
    /**
     * Gets input string from the user through the scanner and trims leading and trailing whitespaces.
     * @param scanner Scanner instance for reading input
     * @return Trimmed input string
     */
    public static String getInputString(Scanner scanner) {
        String userInput = scanner.nextLine();
        return userInput.trim();
    }
}
