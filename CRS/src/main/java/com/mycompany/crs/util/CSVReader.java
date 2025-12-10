package com.mycompany.crs.util;

import java.io.*;
import java.util.*;

/**
 * CSVReader - Utility to read CSV files
 * FIXED: Now handles both comma and tab separated files
 * 
 * @author konda
 * @version 2.0
 */
public class CSVReader {
    
    /**
     * Read CSV file and return list of rows
     * Each row is a Map of column_name -> value
     * 
     * @param filePath Path to CSV file
     * @return List of rows as Maps
     */
    public static List<Map<String, String>> readCSV(String filePath) {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = null;
            boolean firstLine = true;
            String delimiter = null; // Auto-detect delimiter
            
            while ((line = br.readLine()) != null) {
                // Remove BOM if present
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                
                line = line.trim();
                if (line.isEmpty()) continue;
                
                // Auto-detect delimiter on first line
                if (delimiter == null) {
                    if (line.contains("\t")) {
                        delimiter = "\t";
                        System.out.println("✓ Detected TAB-separated file");
                    } else if (line.contains(",")) {
                        delimiter = ",";
                        System.out.println("✓ Detected COMMA-separated file");
                    } else {
                        delimiter = ","; // Default
                    }
                }
                
                String[] values = line.split(delimiter);
                
                if (firstLine) {
                    headers = values;
                    // Clean headers
                    for (int i = 0; i < headers.length; i++) {
                        headers[i] = headers[i].trim();
                    }
                    firstLine = false;
                    System.out.println("✓ CSV Headers: " + Arrays.toString(headers));
                } else {
                    if (headers != null && values.length > 0) {
                        Map<String, String> row = new HashMap<>();
                        for (int i = 0; i < headers.length && i < values.length; i++) {
                            String value = values[i].trim();
                            // Remove quotes if present
                            if (value.startsWith("\"") && value.endsWith("\"")) {
                                value = value.substring(1, value.length() - 1);
                            }
                            row.put(headers[i], value);
                        }
                        data.add(row);
                    }
                }
            }
            
            System.out.println("✓ Loaded " + data.size() + " rows from " + filePath);
            
        } catch (IOException e) {
            System.err.println("✗ Error reading CSV file: " + filePath);
            System.err.println("  Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return data;
    }
    
    /**
     * Parse integer safely
     */
    public static int parseInt(String value, int defaultValue) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return defaultValue;
            }
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}