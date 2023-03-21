import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();

    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);
    }

    public void makeWords(String word, String letters){
        if (letters.length() == 0) {
            words.add(word);
        }

        for (int i = 0; i < letters.length(); i++) {
            String newPrefix = word + letters.charAt(i);
            String newRemainingLetters = letters.substring(0, i) + letters.substring(i + 1);
            makeWords(newPrefix, newRemainingLetters);
        }
        words.add(word);
    }
    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        words = mergesort(words);
    }

    public ArrayList<String> mergesort(ArrayList<String> arr) {
        if(arr.size() == 1){
            return arr;
        }
        int half = arr.size()/2;
        ArrayList<String> firstHalf = new ArrayList<>();
        ArrayList<String> secondHalf = new ArrayList<>();
        for(int i = 0; i<half; i++){
            firstHalf.add(arr.get(i));
        }
        for(int i = half; i<arr.size(); i++){
            secondHalf.add(arr.get(i));
        }
        ArrayList<String> arr1 = mergesort(firstHalf);
        ArrayList<String> arr2 = mergesort(secondHalf);

        return merge(arr1, arr2);

    }
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2){
        ArrayList<String> newArr = new ArrayList<String>();
        while(arr1.size() != 0 && arr2.size() != 0){
            if(arr1.get(0).compareTo(arr2.get(0)) < 0){
                String str = arr1.remove(0);
                newArr.add(str);
            }
            else {
                String str = arr2.remove(0);
                newArr.add(str);
            }
        }
        if(arr1.isEmpty() && !(arr2.isEmpty())) {
            newArr.addAll(newArr.size(), arr2);
        }
        else if(!(arr1.isEmpty()) && arr2.isEmpty()) {
            newArr.addAll(newArr.size(), arr1);
        }
        else{}
        return newArr;

    }
    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        boolean inDictionary;
        for(int i = 0; i<words.size(); i++){
            inDictionary = binarySearch(DICTIONARY, words.get(i), 0, DICTIONARY.length - 1);
            if(!inDictionary) {
                words.remove(i);
                i--;
            }

        }

    }
    //This method doesn't work because it only accesses the dictionary for words that start with an a.
    //The other methods should work though.
    public boolean binarySearch(String[] dic, String target, int low, int high) {
        if (high < low) {
            return false;
        }
        int mid = low + (high-low)/2;
        if(dic[mid].equals(target)) {
            return true;
        }
        else if (dic[mid].compareTo(target) > 0) {
            return binarySearch(dic, target, low, mid-1);
        }
        else {
            return binarySearch(dic, target, mid+1, high);
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
