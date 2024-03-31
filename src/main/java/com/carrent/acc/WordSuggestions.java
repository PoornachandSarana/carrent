package com.carrent.acc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordSuggestions {


    // Func to calc min of three numbers
    private static int minimumOf3(int a, int b, int c) {
        return Math.min(c, Math.min(a, b));
    }

    // checking the word to spell check with the list of canadian cities using the edit distance algorithm and providing suggestions
    public static List<String> providecorrections(String wordtospellcheck, int spellcheckThreshold,
            Set<String> allcitiesin_canada) {
        List<String> suggestedcities = new ArrayList<>();
        for (String citylistword : allcitiesin_canada) {
            int[][] edit_cityTable = new int[wordtospellcheck.length() + 1][citylistword.length() + 1];

            for (int i = 0; i <= wordtospellcheck.length(); i++) {
                for (int j = 0; j <= citylistword.length(); j++) {
                    if (i == 0)
                    edit_cityTable[i][j] = j;
                    else if (j == 0)
                    edit_cityTable[i][j] = i;
                    else if (wordtospellcheck.charAt(i - 1) == citylistword.charAt(j - 1))
                    edit_cityTable[i][j] = edit_cityTable[i - 1][j - 1];
                    else
                    edit_cityTable[i][j] = 1 + minimumOf3(edit_cityTable[i - 1][j - 1], edit_cityTable[i][j - 1], edit_cityTable[i - 1][j]);
                }
            }
            int cityEditDis = edit_cityTable[wordtospellcheck.length()][citylistword.length()];

            if (cityEditDis <= spellcheckThreshold) {
                suggestedcities.add(citylistword);
            }
        }
        return suggestedcities;
    }

    public static List<String> checkcitiesSpelling(String word) {
        Set<String> allcitiesin_canada = new HashSet<String>();
        String wordtospellcheck = word.toLowerCase(); // getting the word from the user input
        
    	// read the list of canadian cities
        try (BufferedReader bufffilreder = new BufferedReader(new FileReader("canadiancities.txt"))) {
            String cityfromlist;
            while ((cityfromlist = bufffilreder.readLine()) != null) {
                String trimmedCity = cityfromlist.trim(); // removing unnecessary spaces before and after the city name
                String lowercaseCity = trimmedCity.toLowerCase(); // making city name into lower case to spell check 
            	allcitiesin_canada.add(lowercaseCity); // Add words to to set containing all cities in canada
            }
        } catch (IOException exception_reading_fileList) {
            System.out.println(exception_reading_fileList.getMessage());
            System.out.println("There seems to be an error. Please try again at a later time");
        }

        List<String> listofcorrections = providecorrections(wordtospellcheck, 2, allcitiesin_canada); // threshold of edit distance in this case is 2
        if (listofcorrections.isEmpty()) {
            return null;
        } else {
            return listofcorrections;
        }
    }

    
    //BST for storing the cities in canada

    private static class BSTNode 
    {
        String city;
        BSTNode left, right;

        public BSTNode(String city) 
        {
            this.city = city;
            this.left = this.right = null;
        }
    }

    private BSTNode root;

    public WordSuggestions() 
    {
        root = null;
        loadCitiesFromFile();
    }

    private void loadCitiesFromFile() 
    {
        try (BufferedReader br = new BufferedReader(new FileReader("canadiancities.txt"))) {
            String line;
            while ((line = br.readLine()) != null) 
            {
                insert(line);
            }
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    private void insert(String city) 
    {
        root = insertRec(root, city);
    }

    private BSTNode insertRec(BSTNode root, String city) 
    {
        if (root == null) 
        {
            root = new BSTNode(city);
            return root;
        }

        if (city.compareToIgnoreCase(root.city) < 0)
            root.left = insertRec(root.left, city);
        else if (city.compareToIgnoreCase(root.city) > 0)
            root.right = insertRec(root.right, city);

        return root;
    }

    List<String> getCitySuggestions(String partialName) 
    {
        List<String> suggestions = new ArrayList<>();
        getCitySuggestionsRec(root, partialName.toLowerCase(), suggestions);
        return suggestions;
    }

    private void getCitySuggestionsRec(BSTNode root, String prefix, List<String> result) 
    {
        if (root == null)
            return;

        if (root.city.toLowerCase().startsWith(prefix))
            result.add(root.city);

        if (prefix.compareToIgnoreCase(root.city) < 0)
            getCitySuggestionsRec(root.left, prefix, result);
        else
            getCitySuggestionsRec(root.right, prefix, result);
    }
    
}
