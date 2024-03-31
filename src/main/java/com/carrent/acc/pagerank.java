package com.carrent.acc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
public class pagerank 
{

	static void displayPageRanking(String filePath1, String filePath2, String targetColumn) throws IOException 
	{
        // Get the page ranking for the specified column (Vehicle Type) in both files
        Map<String, Integer> pageRanking1 = getPageRanking(filePath1, targetColumn);
        Map<String, Integer> pageRanking2 = getPageRanking(filePath2, targetColumn);

        // Compare occurrences and display the sorted result
        compareAndDisplayPageRanking(pageRanking1, pageRanking2);
    }
	private static Map<String, Integer> getPageRanking(String filePath, String targetColumn) throws IOException {
        Map<String, Integer> keywordOccurrences = new HashMap<>();

        try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fileInputStream)) 
        {

            Sheet sheet = workbook.getSheetAt(0);

            // Find the header row to get the column index
            Row headerRow = sheet.getRow(0);
            int targetColumnIndex = findColumnIndex(headerRow, targetColumn);

            if (targetColumnIndex == -1) 
            {
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

	private static int findColumnIndex(Row headerRow, String targetColumn)
	{
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase(targetColumn)) {
                    return cell.getColumnIndex();
                }
            }
        }
        return -1;
    }
	private static void compareAndDisplayPageRanking(Map<String, Integer> pageRanking1, Map<String, Integer> pageRanking2) {
	    // Merge the two page rankings
	    Map<String, Integer> mergedPageRanking = new HashMap<>(pageRanking1);
	    for (Map.Entry<String, Integer> entry : pageRanking2.entrySet()) {
	        mergedPageRanking.merge(entry.getKey(), entry.getValue(), Integer::sum);
	    }

	    // Sort the merged page ranking by occurrences in descending order
	    Map<String, Integer> sortedMergedPageRanking = mergedPageRanking.entrySet().stream()
	            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
	            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	                    (oldValue, newValue) -> oldValue, LinkedHashMap::new));

	    // Display the sorted merged page ranking along with occurrences in each file
	    System.out.println("\n-----------------------------------------");
	    System.out.println("Ranking based on Vehicle Types Occurrences");
	    System.out.println("-----------------------------------------");

	    for (Map.Entry<String, Integer> entry : sortedMergedPageRanking.entrySet()) {
	        String vehicleType = entry.getKey();
	        int occurrences1 = pageRanking1.getOrDefault(vehicleType, 0);
	        int occurrences2 = pageRanking2.getOrDefault(vehicleType, 0);
	        int totalOccurrences = occurrences1 + occurrences2;

	        System.out.println(vehicleType + ": " + totalOccurrences + " (File 1: " + occurrences1 + ", File 2: " + occurrences2 + ")");
	    }
	}
}
