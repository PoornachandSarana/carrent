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

    // Function to calculate the edit distance between two strings
    private static int getedist(String fir_word, String sec_word) {
        int[][] ed_table = new int[fir_word.length() + 1][sec_word.length() + 1];

        for (int i = 0; i <= fir_word.length(); i++) {
            for (int j = 0; j <= sec_word.length(); j++) {
                if (i == 0)
                	ed_table[i][j] = j;
                else if (j == 0)
                	ed_table[i][j] = i;
                else if (fir_word.charAt(i - 1) == sec_word.charAt(j - 1))
                	ed_table[i][j] = ed_table[i - 1][j - 1];
                else
                	ed_table[i][j] = 1 + minimumOf3(ed_table[i - 1][j - 1], ed_table[i][j - 1], ed_table[i - 1][j]);
            }
        }
        return ed_table[fir_word.length()][sec_word.length()];
    }

    // Function to check edit dist between input and dictionary words and suggest corrections
    public static List<String> providecorrections(String wordtospellcheck, int threshold, Set<String> alldictwords) {
        List<String> listofsuggestions = new ArrayList<>();
        for (String wordfromdict : alldictwords) {
            int e_dist_calculated = getedist(wordtospellcheck, wordfromdict);
            if (e_dist_calculated <= threshold) {
            	listofsuggestions.add(wordfromdict);
            }
        }
        return listofsuggestions;
    }


    public static List<String> spellCheck(String word) {
        Set<String> allwords_dict = new HashSet<String>();
        String wordtospellcheck = word.toLowerCase(); // getting the word from the user input
        
    	// reading the list of files from the dictionary
        try (BufferedReader bufffilreder = new BufferedReader(new FileReader("canadiancities.txt"))) {
            String linfromdict;
            while ((linfromdict = bufffilreder.readLine()) != null) {
            	allwords_dict.add(linfromdict.trim().toLowerCase()); // Add words to dictionary
            }
        } catch (IOException fil_read_exptn) {
            System.out.println(fil_read_exptn.getMessage());
        }

        int threshold = 2; // Setting a threshold for minimum edit distance
        List<String> listofcorrections = providecorrections(wordtospellcheck, threshold, allwords_dict);
        if (listofcorrections.isEmpty()) {
            return null;
        } else {
            return listofcorrections;
        }
    }
}
