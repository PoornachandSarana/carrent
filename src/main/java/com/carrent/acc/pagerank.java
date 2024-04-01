package com.carrent.acc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/*The main aim of our page ranking feature in our project is to make user experience 
 * as easy as possible. To do this what we did is we implemented page ranking so as that 
 * based on “vehicle Type” or “Vehicle Model” the data that is scrapped from 3 websites 
 * namely “WebOrbitz”, “carrentals”, and “Expedia” are ranked according to the greatest 
 * number of occurrences on the top. While generating the output for making the user experience 
 * better we have also mentioned number of occurrences of each preference in all the 3 websites.
 */
public class pagerank {
	/*  This method takes four parameters: three file paths representing the
	 *  paths to the three Excel files (filePath1, filePath2, filePath3), 
	 *  and targetColumn representing the column in the Excel files where the page ranking is to be calculated.
	 *  Inside this method, it retrieves the page ranking for the specified 
	 *  targetColumn from each of the three Excel files using the getPageRanking method and then compares 
	 *  and displays the combined page ranking using the compareAndDisplayPageRanking method
	 */
    static void displayPageRanking(String filePath1, String filePath2, String filePath3, String targetColumn) throws IOException {
        // Get the page ranking for the specified column (Vehicle Type) in all three files
        Map<String, Integer> pageRanking1 = getPageRanking(filePath1, targetColumn);
        Map<String, Integer> pageRanking2 = getPageRanking(filePath2, targetColumn);
        Map<String, Integer> pageRanking3 = getPageRanking(filePath3, targetColumn);

        // Compare occurrences and display the sorted result
        compareAndDisplayPageRanking(pageRanking1, pageRanking2, pageRanking3);
    }
    
    /* This method takes two parameters: the file path (filePath) 
     * and the target column (targetColumn) where the page ranking is 
     * to be calculated.
     * It reads the Excel file located at filePath, searches for the targetColumn, 
     * and updates the keywordOccurrences map with the occurrences of keywords 
     * found in that column.
	 */
    private static Map<String, Integer> getPageRanking(String filePath, String targetColumn) throws IOException {
        Map<String, Integer> keywordOccurrences = new HashMap<>();

        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Find the header row to get the column index
            Row headerRow = sheet.getRow(0);
            int targetColumnIndex = findColumnIndex(headerRow, targetColumn);

            if (targetColumnIndex == -1) {
                throw new IllegalArgumentException("Column not found: " + targetColumn);
            }
            // Iterate through rows and update keyword occurrences for the specified column
            for (Row row : sheet) {
                Cell cell = row.getCell(targetColumnIndex);

                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String cellValue = cell.getStringCellValue().toLowerCase();

                    // Split the cell content into keywords
                    String[] keywords = cellValue.split("\\s+");

                    // Update the occurrences of each keyword
                    for (String keyword : keywords) {
                        keywordOccurrences.put(keyword, keywordOccurrences.getOrDefault(keyword, 0) + 1);
                    }
                }
            }
        }

        return keywordOccurrences;
    }

    private static int findColumnIndex(Row headerRow, String targetColumn) {
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase(targetColumn)) {
                    return cell.getColumnIndex();
                }
            }
        }
        return -1;
    }

    /* This method takes three parameters: maps representing the page rankings 
     * from each of the three Excel files (pageRanking1, pageRanking2, pageRanking3).
     * It merges the three-page rankings into one map (mergedPageRanking) 
     * and sorts this merged map based on occurrences in descending order.
	 */
    private static void compareAndDisplayPageRanking(Map<String, Integer> pageRanking1, Map<String, Integer> pageRanking2, Map<String, Integer> pageRanking3) {
        // Merge the three page rankings
        Map<String, Integer> mergedPageRanking = new HashMap<>(pageRanking1);
        for (Map.Entry<String, Integer> entry : pageRanking2.entrySet()) {
            mergedPageRanking.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
        for (Map.Entry<String, Integer> entry : pageRanking3.entrySet()) {
            mergedPageRanking.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }

        // Sort the merged page ranking by occurrences in descending order
        Map<String, Integer> sortedMergedPageRanking = mergedPageRanking.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        // Display the sorted merged page ranking along with occurrences in each file
        System.out.println("\n-----------------------------------------");
        System.out.println("Ranking based on your preference");
        System.out.println("-----------------------------------------");

        for (Map.Entry<String, Integer> entry : sortedMergedPageRanking.entrySet()) {
            String vehicleType = entry.getKey();
            int occurrences1 = pageRanking1.getOrDefault(vehicleType, 0);
            int occurrences2 = pageRanking2.getOrDefault(vehicleType, 0);
            int occurrences3 = pageRanking3.getOrDefault(vehicleType, 0);
            int totalOccurrences = occurrences1 + occurrences2 + occurrences3;

            System.out.println(vehicleType + ": " + totalOccurrences + " (Orbitz:" + occurrences1 + ", Car rentals:" + occurrences2 + ", Expedia:" + occurrences3 + ")");
        }
    }
}
