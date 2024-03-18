package com.carrent.acc;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	public static void typeIntoInputField(WebDriverWait wait, WebDriver driver, String xpath, String text) {
        // Type the specified text into the input field
        WebElement element = waitForElementVisible(wait, driver, xpath);

        element.sendKeys(text);
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
}
