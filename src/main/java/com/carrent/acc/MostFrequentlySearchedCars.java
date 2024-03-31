package com.carrent.acc;

import java.util.*;

public class MostFrequentlySearchedCars {
    private Map<String, Integer> wordSearchCounts;
    private PriorityQueue<Map.Entry<String, Integer>> maxHeap;

    public MostFrequentlySearchedCars() {
        wordSearchCounts = new HashMap<>();
        maxHeap = new PriorityQueue<>(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue().compareTo(a.getValue()); // Compare frequencies in descending order
            }
        });
    }

    public void searchWords(String sentence) {
        // Update frequency of the entire sentence
        int count = wordSearchCounts.getOrDefault(sentence, 0);
        wordSearchCounts.put(sentence, count + 1);

        // Add the entries to the max heap
        maxHeap.clear(); // Clear the max heap before re-adding entries
        maxHeap.addAll(wordSearchCounts.entrySet());

        // Display the top 3 most frequently searched words
        // displayTopFrequentWords();
    }

    public void displayTopFrequentWords() {
        // Display the top 3 most frequent words
        int count = 0;
        System.out.println("Top 3 most frequently searched cars:");
        while (!maxHeap.isEmpty() && count < 3) {
            Map.Entry<String, Integer> entry = maxHeap.poll();
            System.out.println(entry.getKey() + ": " + entry.getValue() + " searches");
            count++;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MostFrequentlySearchedCars wordTracker = new MostFrequentlySearchedCars();

        // Main loop for searching words
        while (true) {
            System.out.print("Enter a word/sentence (or 'exit' to quit): ");
            String sentence = scanner.nextLine().trim();

            if (sentence.equalsIgnoreCase("exit")) {
                break; // Exit the program
            }

            wordTracker.searchWords(sentence);
        }

        scanner.close();
    }
}
