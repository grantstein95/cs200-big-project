/**
 * Code for mergesort methods taken from Data Abstraction and Problem Solving with Java 3rd Edition by
 * Prichard and Carrano with modifications. Some additional inspiration and assistance taken from Dr. Adele Howe's
 * lecture slides for the CS200 course at Colorado State University.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Class used to store and organize terms.
 * @author Grant Stein & Bobby Signor
 */
public class WebPages {
    /**
     * The sorted list of terms encountered across all documents.
     */
    private ArrayList<Term> termIndex = new ArrayList<Term>();
    
    /**
     * A counter used for grading purposes.
     */
    private int timesMergeSortRan;
    
    /**
     * A helper index for things.
     */
    private int helpingIndex;

    /**
     * Default constructor.
     */
    public WebPages() {
        this.timesMergeSortRan = 0;
        this.helpingIndex = 0;
    }

    /**
     * Adds the terms from a given page into the term index.
     * @param fileName The name of the file to be parsed
     * @author Grant Stein & Bobby Signor
     */
    public void addPage(String fileName){
        try {
            Scanner scanFile = new Scanner(new File(fileName));
            scanFile.useDelimiter("(<[^>]*>\\s*)|([^A-z0-9'<>]+)");     //Tell the scanner to filter out all HTML tags & extraneous punctuation as delimiters
            while(scanFile.hasNext())                               //While there's still stuff left in the document...
                addWord(new Term(scanFile.next(), fileName));       //...take the word, make it into a term,
            scanFile.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: No file found.");            //Print a message to the error console
            e.printStackTrace();                                    //Print the stacktrace of the error to the error console
        }
    }

    /**
     * Registers a term in the term index.
     * @param newTerm The term to be added to the array.
     * @author Bobby Signor
     */
    private void addWord(Term newTerm) {
        int start = 0, end = this.termIndex.size() - 1, middle = start + ((end - start) / 2);

        //The binary insertion logic.
        while (end >= start) {
            if (this.termIndex.get(middle).compareTo(newTerm) < 0) {
                //If we're not far enough into the alphabet, set the next zone to start at middle + 1.
                start = middle + 1;
            } else if (this.termIndex.get(middle).compareTo(newTerm) > 0) {
                //If we're too far into the alphabet, set the next zone to end at middle - 1.
                end = middle - 1;
            } else {                                //If the word is already in the list...
                this.termIndex.get(middle).incFrequency(newTerm.getDocsList().get(0).getDocName());
                //...then we can leave. So leave.
                return;
                //What are you still doing here?
                //No, seriously, you should leave.
                //Word's already been dealt with. There's nothing left to do in this section.
                //Wait, you're just reading straight through my code, aren't you?
                //God, that really sucks for you, doesn't it?
                //Well, go on. There's nothing important in these comments. You can ignore them.
                //I'm serious. I'm just doing typing these to get some practice on a new keyboard.
                //Okay, see, I wasn't kidding in the last line. I'm 100% just doing this to get some more practice.
                //Alright, fine. I'm done. See? Next line isn't a comment. You're free. Congrats.
            }
            middle = start + ((end - start) / 2);
        }
        this.termIndex.add(middle, newTerm);
    }

    /**
     * Finds a term in the list or returns null if it's not found.
     * @param name The name of the term to look for
     * @return The term if found, null otherwise
     */
    public Term findWord(String name) {
        int start = 0, end = this.termIndex.size() - 1, middle = start + ((end - start) / 2);
        Term newTerm = new Term(term);

        while (end >= start) {
            if (this.termIndex.get(middle).compareTo(newTerm) < 0) {
                start = middle + 1;
            } else if (this.termIndex.get(middle).compareTo(newTerm) > 0) {
                end = middle - 1;
            } else {
                return this.termIndex.get(middle);
            }
            middle = start + ((end - start) / 2);
        }

        return null;
    }

    /**
     * Returns an array with the names of all the files where the given word occurrs.
     * @param word The word to look for
     * @return An array containing the names of all the files where the word occurrs
     */
    public String[] whichPages(String word) {
        Term temp = findWord(word);
        if (temp == null)
            return null;
        int arraySize = temp.getDocFrequency();                 //set return array size to the # of docs the word is found in
        String[] documents = new String[arraySize];
        LinkedList<Occurrence> tempArray = temp.getDocsList();  //get linked list of occurrences

        for(int i = 0; i < arraySize; i++)                      //copy document names into array of strings
            documents[i] = tempArray.get(i).toString();

        return documents;                                       //return array of document names where word was found
    }

    /**
     * Prunes a given number of words.
     * @param n The number of words to prune.
     */
    public void pruneStopWords(int n){                      //remove a certain number of determined stopwords from the arraylist
        mergeSortByCount(termIndex, 0, termIndex.size());   //sort terms by total # of occurrences
        for(int i = 0; i < n; i++){                         //remove the number of stop words
            termIndex.remove(termIndex.size());
        }
        mergeSortByWord(termIndex, 0, termIndex.size());    //re-sort the terms alphabetically
    }

    /**
     * Sort the
     * @param terms
     * @param first
     * @param last
     */
    public void mergeSortByWord(ArrayList<Term> terms, int first, int last){    //merge sort alphabetically from a-z
        if(first < last){
            int mid = (first + last) /2;                            //set mid
            mergeSortByWord(terms, first, mid);                     //sort bottom half
            mergeSortByWord(terms, mid + 1, last);                  //sort top half
            mergeByWord(terms, first, mid, last);                   //combine two halves in order
        }
    }

    /**
     *
     * @param terms
     * @param first
     * @param mid
     * @param last
     */
    public void mergeByWord(ArrayList<Term> terms, int first, int mid, int last){ //helper method pointing to real sorter
        ArrayList<Term> tempArray = new ArrayList<Term>();
        mergeByWord(terms, tempArray, first, mid, last);
    }

    /**
     *
     * @param terms
     * @param tempArray
     * @param first
     * @param mid
     * @param last
     */
    public void mergeByWord(ArrayList<Term> terms, ArrayList<Term> tempArray, int first, int mid, int last){ //contains mergesort algorithm for words
        int first1 = first; int last1 = mid; int first2 = mid+1; int last2 = last; int index = first1;      //set helpful indexes

        while((first1 <= last1) && (first2 <= last2)){                                      //sort at least one half of the array into the temp array
            if( terms.get(first1).getName().compareTo(terms.get(last1).getName()) < 0){     //if a word is "less than" another lexicographically, add it to the temp array
                tempArray.add(index, terms.get(first1));
                first1++;
            }else{
                tempArray.add(index, terms.get(first2));
                first2++;
            }
            index++;
        }

        while(first1 <= last1){                                                             //finish sorting the first half of the array if necessary
            tempArray.add(index, terms.get(first1));
            first1++;
            index++;
        }

        while(first2 <= last2){                                                             //finish sorting the last half of the array if necessary
            tempArray.add(index, terms.get(first2));
            first2++;
            index++;
        }

        for(int i = 0; i < terms.size(); i++){                                              //copy the temporary array to the legit one
            terms.set(i, tempArray.get(i));
        }
    }

    /**
     *
     * @param terms
     * @param first
     * @param last
     */
    public void mergeSortByCount(ArrayList<Term> terms, int first, int last){   //merge sort by total frequency from lowest to highest
        if(first < last){
            int mid = (first + last) /2;
            mergeSortByCount(terms, first, mid);
            mergeSortByCount(terms, mid + 1, last);
            mergeByCount(terms, first, mid, last);
        }
    }

    /**
     *
     * @param terms
     * @param first
     * @param mid
     * @param last
     */
    public void mergeByCount(ArrayList<Term> terms, int first, int mid, int last){ //helper method pointing to real sorter
        ArrayList<Term> tempArray = new ArrayList<Term>();
        mergeByCount(terms, tempArray, first, mid, last);
    }

    /**
     *
     * @param terms
     * @param tempArray
     * @param first
     * @param mid
     * @param last
     */
    public void mergeByCount(ArrayList<Term> terms, ArrayList<Term> tempArray, int first, int mid, int last){ //contains mergesort algorithm for counts
        int first1 = first; int last1 = mid; int first2 = mid+1; int last2 = last; int index = first1;

        while((first1 <= last1) && (first2 <= last2)){
            if( terms.get(first1).getTotalFrequency() < terms.get(first2).getTotalFrequency()){
                tempArray.add(index, terms.get(first1));
                first1++;
            }else{
                tempArray.add(index, terms.get(first2));
                first2++;
            }
            index++;
        }

        while(first1 <= last1){
            tempArray.add(index, terms.get(first1));
            first1++;
            index++;
        }

        while(first2 <= last2){
            tempArray.add(index, terms.get(first2));
            first2++;
            index++;
        }

        for(int i = 0; i < terms.size(); i++){
            terms.set(i, tempArray.get(i));
        }
    }

    /**
     *
     */
    public void printTerms(){   //print all of the terms in the arraylist
        System.out.println("WORDS");
        for (int i = 0; i < termIndex.size(); i++) {
            System.out.println(termIndex.get(i));
        }
    }
}
