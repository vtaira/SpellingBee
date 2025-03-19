import java.io.*;
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
 * @author Zach Blick, [Veronica Taira]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {
    // Instance variables
    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    // Constructor
    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // Generates all possible substrings of a word in words.
    public void generate() {

        // Calls recursive makeWords() function to generate substrings
        makeWords("", letters);

    }

    // Recursively goes through letters in the word
    public void makeWords(String str, String letters){
        // If the current word in str isn't empty, and not already in words,
        // Add str to words.
        if(!str.isEmpty() && !words.contains(str)){
            words.add(str);
        }
       // Cycles through the different letter combinations in each word.
        for(int i = 0; i < letters.length(); i++){
            makeWords(str + letters.charAt(i), letters.substring(0, i) +  letters.substring(i + 1));
        }
    }

    // Applies Mergesort to all of the words in words.
    public void sort() {

       words = mergesort(words, 0, words.size() - 1);

    }
    // Recursive mergesort function
    public ArrayList<String> mergesort(ArrayList<String> arr, int left, int right) {
        int mid = (left + right) / 2;
        // Sets each array after a split as a left or right array.
        ArrayList<String> rightArr = new ArrayList<>();
        ArrayList<String> leftArr = new ArrayList<>();
        ArrayList<String> base = new ArrayList<>();

        // Base Case.
        if (left == right) {
            base.add(arr.get(left));
            return base;
        }
        else {
            // Sort the right side.
            rightArr = mergesort(arr, left, mid);
            // Sort the left side.
            leftArr = mergesort(arr, mid + 1, right);
        }
        // Calls merge on the sorted right array and left array.
        return merge(rightArr, leftArr);
    }
    // Merges two sorted arrays together alphabetically.
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2){
        // Creates a new array that can fit both arr1 and arr2.
        ArrayList<String> sorted = new ArrayList<String>(arr1.size() + arr2.size());
        int index1 = 0, index2 = 0, count = 0;

       // While index1(where we are in arr1) and index2(where we are in arr2),
       //  Are less than arr1 & arr2.
        while(index1 < arr1.size() && index2 < arr2.size()){
            // If the element in arr1 is greater than the current element in arr2,
            // Add that element to total array, and increment index1.
            if(arr1.get(index1).compareTo(arr2.get(index2)) < 0){
                sorted.add(count, arr1.get(index1++));
            }
           // Else, add the arr2 element, and increment index2.
            else{
                sorted.add(count, arr2.get(index2++));
            }
            count++;
        }
        // If arr1 is larger than arr2,
        // While the index is smaller, add each of the arr1 elements to the total array.
        while(index1 < arr1.size()){
            sorted.add(count, arr1.get(index1++));
        }
       // If arr2 is larger than arr1,
       // Add each remaining element from arr2 to the total array.
        while(index2 < arr2.size()){
            sorted.add(count, arr2.get(index2++));
        }
        return sorted;
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

    // Checks all words in words to make sure that they are in the given dictionary
    public void checkWords() {
        // For each word in words,
        // If the word is not in the dictionary (checked using binary search),
        // Remove that word from words.
       for(int i = 0; i < words.size(); i++){
           if(!binarySearch(DICTIONARY, words.get(i), 0, DICTIONARY_SIZE - 1)){
               words.remove(i);
               i--;
           }
       }

    }
    // Searches for a word in the dictionary using binary search
    public boolean binarySearch(String[] dictionary, String word, int left, int right){
        int mid = left + (right - left)/2;

        // Base case: if the left index becomes greater than the right index,
        // Meaning that the element hasn't been found in the dictionary,
        // Return false.
        if(left > right){
            return false;
        }
        int compare = dictionary[mid].compareTo(word);

        // Recursively go through the left of the array.
        if(compare > 0){
           return binarySearch(dictionary, word, left, mid -1);
        }
        // Recursively go through the right of the array.
        else if(compare < 0){
            return binarySearch(dictionary, word, mid + 1, right);
        }
        else{
            return true;
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
