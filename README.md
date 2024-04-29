# Car Rental Price Analyzer

## Objective
The Car Rental Price Analyzer project is designed to provide users with insights into car rental prices, aiding them in finding the most cost-effective deals available. By analyzing data from various rental websites, the application assists users in making informed decisions when renting a vehicle.

## Features
### Web Crawling
Utilizes web crawling techniques to gather rental information from popular websites such as Orbitz and Carrentals.

### HTML Parsing
Extracts detailed information about available vehicles from Expedia using Selenium WebDriver and JSoup.

### Spell Checker
Implements a specialized spell-checking algorithm tailored for Canadian city names, ensuring accurate data processing.

### Word Completion
Offers suggestions for city names based on user input, enhancing user experience and efficiency.

### Data Validation
Validates date formats and rental lengths to prevent errors and ensure data integrity.

### Frequency Count
Counts occurrences of different car types and presents the results in a user-friendly format.

### Page Ranking
Prioritizes rental options based on the frequency of vehicle types or models, helping users quickly identify popular choices.

### Inverted Indexing
Efficiently organizes and presents data from Excel files, optimizing data retrieval and presentation.

### Search Frequency
Tracks user-selected options for vehicle types, providing insights into user preferences and trends.

## Usage
To use the Car Rental Price Analyzer, follow these steps:
1. Clone the repository to your local machine.
2. Download the latest chromedriver and specify its path at line 306 in Main.java.
3. Build and run as a maven application.
4. Follow the on-screen instructions to input your search criteria and view rental options.
