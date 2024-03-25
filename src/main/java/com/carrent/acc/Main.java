package com.carrent.acc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {

	public static void showName() {
		String[] lines = {
			"  ____ ____ ____ ____ ____ ____ _________ _",
			" ||C |||a |||r |||Q |||u |||e ||||s |||t ||",
			" ||__|||__|||__|||__|||__|||__||||__|||__||",
			" |/__\\|/__\\|/__\\|/__\\|/__\\|/__\\||/__\\|/__\\|"
		};
		
		for (String line : lines) {
			System.out.println(line);
		}
		System.out.println("\nWelcome aboard CarQuest! Your gateway to seamless car rentals awaits");
	}

	public static String getLocation(Scanner scanner) {
		System.out.println("Please enter the location where you want to rent your car: ");
		String location = Helper.getInputString(scanner);
		// TODO word completion
		List<String> checkSpelling = WordSuggestions.spellCheck(location);
		if(checkSpelling == null) {
			System.out.println("Please enter a valid city in Canada");
			return getLocation(scanner);
		}
		else if(checkSpelling.size() == 1 && checkSpelling.get(0).equalsIgnoreCase(location)) {
			return location;
		}
		else {
            StringJoiner joiner = new StringJoiner("/");
            for (String correction : checkSpelling) {
				if(location.equalsIgnoreCase(correction)) return location;
            	correction = correction.substring(0, 1).toUpperCase() + correction.substring(1);
                joiner.add(correction);
            }
			System.out.print("Did you mean ");
            String result = joiner.toString();
            System.out.print("\u001B[1m" +result);
			System.out.print("\u001B[0m");
            System.out.println("?\n");
			return getLocation(scanner);
		}
	}

	public static String getFromDate(Scanner scanner) {
		String fromDate;
		do {
			System.out.println("From which date would you like to rent the car? Please enter in yyyy-mm-dd format.");
			fromDate = Helper.getInputString(scanner);
			if (!Validation.validateFromDate(fromDate)) {
				System.out.println("Please provide a valid date. (The date should be from tomorrow onwards and formatted as yyyy-mm-dd.)");
			}
		} while (!Validation.validateFromDate(fromDate));
		return fromDate;
	}

	public static String getRentLength(Scanner scanner) {
		String days;
		do {
		   System.out.println("For how many days would you like to rent a vehicle?");
			days = Helper.getInputString(scanner);
			if (!Validation.validateRentLength(days)) {
				System.out.println("Please provide a valid number of days between 1 and 30");
			}
		} while (!Validation.validateRentLength(days));
		return days;
	}

	public static String calculateEndDate(String startDate, String numberOfDays) {
        // Parse the start date string into a LocalDate object
        LocalDate parsedStartDate = LocalDate.parse(startDate);

		int days = Integer.parseInt(numberOfDays);
        
        // Calculate the end date by adding the number of days to the start date
        LocalDate endDate = parsedStartDate.plusDays(days);
        
        // Format the end date into "yyyy-MM-dd" format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedEndDate = endDate.format(formatter);
        
        return formattedEndDate;
    }

	public static void displayRentalSummary(String startDate, String endDate, String location, String duration, Scanner scanner) {
        // Format dates for display
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String formattedStartDate = LocalDate.parse(startDate).format(formatter);
        String formattedEndDate = LocalDate.parse(endDate).format(formatter);
        
        // Display summary on console
		System.out.println("\n\nTo summarize\n");
        System.out.println("Location of rental: " + location);
        System.out.println("Renting vehicle from: " + formattedStartDate);
        System.out.println("Renting vehicle till: " + formattedEndDate + " (" + duration + " days)");
		confirmRentalDetails(startDate,  endDate,  location,  duration, scanner);
    }

	public static void changeDetails(String startDate, String endDate, String location, String duration, Scanner scanner) {
		System.out.println("Which detail would you like to change? Please select the valid option.\r\n" + //
				"1.Location\n" + 
				"2.From Date\n" + 
				"3.Number of days\n" + 
				"4.No change\n" + 
				"");
		String changeOption = Helper.getInputString(scanner);
		int selectedOption = 0;
		try {
			selectedOption = Integer.parseInt(changeOption);
			if (!(selectedOption == 1 || selectedOption == 2 || selectedOption == 3 || selectedOption == 4)) {
				System.out.println("Please enter a valid option");
				changeDetails(startDate,  endDate,  location,  duration, scanner);
				return;
			}
		} catch (NumberFormatException e) {
			System.out.println("Please enter a valid option");
			changeDetails(startDate,  endDate,  location,  duration, scanner);
			return;
		}
		switch (selectedOption) {
			case 1:
				location = getLocation(scanner);
				break;
			case 2:
			    startDate = getFromDate(scanner);
				endDate = calculateEndDate(startDate, duration);
				break;
			case 3:
				duration = getRentLength(scanner);
				endDate = calculateEndDate(startDate, duration);
				break;
			case 4:
			displayRentalSummary(startDate, endDate, location, duration, scanner);
			return;
		
			default:
				break;
		}
		displayRentalSummary(startDate, endDate, location, duration, scanner);
	}

	public static void getData(String startDate, String endDate, String location, Scanner scanner) {
		System.out.println("Please wait while we get the available vehicles………");
		try {
			System.setProperty("webdriver.chrome.driver",
					"/Users/sheldonkevin/Downloads/chromedriver-mac-arm64/chromedriver");

			WebDriver driver = new ChromeDriver();
			// Webcrawler.processCarRentalsWebsite(driver);
			
			//TODO Webcrawler Carrentals
			//TODO Webcrawler Avis

			Webcrawler.WebCrawlOrbitz(driver, startDate, endDate, location);
			driver.quit();
			System.out.println("Thank you for your patience");
		} catch (Exception e) {
			System.out.println("There seems to be an error retrieving information. Would you like to try again? (Y/N)");
			String tryAgain = Helper.getInputString(scanner);
			if(tryAgain.equalsIgnoreCase("y")) {
				getData(startDate,  endDate,  location, scanner);
				return;
			}
		}
	}

	public static void confirmRentalDetails(String startDate, String endDate, String location, String duration, Scanner scanner) {
		System.out.println("\nAre the above details correct? (Y/N)");
		String correctDetails = Helper.getInputString(scanner);

		if(correctDetails.equalsIgnoreCase("n")) {
			changeDetails(startDate,  endDate,  location,  duration, scanner);
			return;
		}
		else if(correctDetails.equalsIgnoreCase("y")) {
			getData(startDate,  endDate,  location, scanner);
			return;
		}
		else {
			System.out.println("Please Enter a valid option");
			confirmRentalDetails( startDate,  endDate,  location,  duration, scanner);
			return;
		}
	}

	public static void startApp(Scanner scanner) {
		String location = getLocation(scanner);
		String fromDate = getFromDate(scanner);
		String rentLength = getRentLength(scanner);
		String toDate = calculateEndDate(fromDate, rentLength);
		displayRentalSummary(fromDate, toDate, location, rentLength, scanner);
	}

	public static void main(String[] args) {
		showName();
		System.out.println();
		Scanner scanner = new Scanner(System.in);
		startApp(scanner);
		scanner.close();
	}

}
