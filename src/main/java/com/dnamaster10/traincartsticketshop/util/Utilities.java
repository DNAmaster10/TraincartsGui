package com.dnamaster10.traincartsticketshop.util;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Utilities {
    /**
     * Returns true if the passed String can be parsed as an integer.
     *
     * @param value Input String
     * @return Boolean indicating whether the String can be parsed as an integer
     * */
    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns the minimum number of pages required to contain the specified number of elements.
     *
     * @param elementCount The number of elements
     * @param pageSize The maximum number of elements a page can hold
     * @return Minimum number of pages required to hold the specified number of elements
     * */
    public static int getPageCount(int elementCount, int pageSize) {
        //Returns the minimum amount of pages that would be required to hold given elements
        return (int) Math.ceil((double) elementCount / pageSize);
    }

    /**
     * Joins arguments within a String array surrounded by double quotes into a single array element.
     *
     * @param args Input array
     * @return An array of Strings with elements surrounded by double quotes concatenated together
     * */
    public static String[] concatenateQuotedStrings(String[] args) {
        ArrayList<String> result = new ArrayList<>();
        StringBuilder inQuote = new StringBuilder();
        StringBuilder outQuote = new StringBuilder();
        boolean inQuotes = false;

        for (String word : args) {
            if (inQuotes) {
                inQuote.append(' ');
            } else if (!outQuote.isEmpty()){
                result.add(outQuote.toString());
                outQuote.setLength(0);
            }
            for (char character : word.toCharArray()) {
                if (character == '"') {
                    if (inQuotes) {
                        result.add(inQuote.toString());
                        inQuote.setLength(0);
                        inQuotes = false;
                    } else {
                        if (!outQuote.isEmpty()) {
                            result.add(outQuote.toString());
                            outQuote.setLength(0);
                        }
                        inQuotes = true;
                    }
                } else {
                    if (inQuotes) {
                        inQuote.append(character);
                    } else {
                        outQuote.append(character);
                    }
                }
            }
        }
        if (inQuotes && !inQuote.isEmpty()) result.add(inQuote.toString());
        else if (!outQuote.isEmpty()) result.add(outQuote.toString());

        return result.toArray(String[]::new);
    }

    private static final Pattern STRING_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    /**
     * Checks if the input String contains any characters other than Aa - Zz, numbers, underscores and dashes.
     *
     * @param input Input String
     * @return True if special characters are present
     */
    public static boolean checkSpecialCharacters(String input) {
        //Returns true if string contains characters other than Aa-Zz, dashes, numbers, and underscores.
        return !STRING_PATTERN.matcher(input).matches();
    }
}
