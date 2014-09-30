import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/** 
 * Code for mergesort methods taken from Data Abstraction and Problem Solving with Java 3rd Edition by
 * Prichard and Carrano with modifications. Some additional inspiration and assistance taken from Dr. Adele Howe's
 * lecture slides for the CS200 course at Colorado State University.
 */ 

/**
 * Class used to store and organize terms.
 * @author Grant Stein & Bobby Signor
 */
public class webPages {
    /**
     * The sorted list of terms encountered across all documents.
     */
    private ArrayList<Term> termIndex = new ArrayList<Term>();
    private ArrayList<Term> helpingList = new ArrayList<Term>();
    
    /**
     * A counter used for grading purposes.
     */
    private int timesMergeSortRan = 0;
    
    /**
     * A helper index for things.
     */
    private int helpingIndex;

    /**
     * Adds the terms from a given page into the term index.
     * @param fileName The name of the file to be parsed
     * @author Grant Stein & Bobby Signor
     */
    public void addPage(String fileName){
        try {
            Scanner scanFile = new Scanner(new File(fileName)); 
            scanFile.useDelimiter("(<[^>]*>)|([^A-z0-9'<>]+)");         //Tell the scanner to filter out all HTML tags & extraneous punctuation
            while(scanFile.hasNext()){
                String temp = scanFile.next().toLowerCase(); 			//read a string in (in lower case)
              // System.out.println("Word read in is: " + temp);
                int index = binarySearchAndInsert(temp);;              //TODO: Fix binarySearchAndInsert so it actually inserts the term rather than return the index 

                if(index != -1){                                        //if the word is not already in the arraylist
                    Term tempTerm = new Term(temp, fileName);
                   // System.out.println(tempTerm.toString());
                    termIndex.add(index, tempTerm);						//add the term to the arraylist
                }else{
                    termIndex.get(helpingIndex).incFrequency(fileName);		//otherwise increase frequency of word found in document
                }
            }
            scanFile.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: No file found.");    //Print a message to the error console
            e.printStackTrace();                                        //Print the stacktrace of the error to the error console
        }
    }

    /**
     * Registers a term in the term index.
     * @param words 
     * @param word
     * @return
     * @author Grant Stein
     */
    public int binarySearchAndInsert(String word){ //search array list for a word to see if it is already present using binary search
        int min = 0;														//start of arraylist
        int max = this.termIndex.size();												//end of arraylist

        while(min < max){ 													//search while we haven't check everything and haven't found a matching entry
            int mid = min + (max - min) / 2;								//get the index

            if(word.compareTo(this.termIndex.get(mid).getName()) < 0){				//if the word is lexicographically less then search lower half
                max = mid;

            }else if (word.compareTo(this.termIndex.get(mid).getName()) > 0){ 		//if the word is lexicographically greater search the upper half
                min = mid + 1;
            }else{															//the word is already in the array
                helpingIndex = mid;
                return -1;
            }
        }// end of while loop
        return max;		//where the word should go if not found
    }

    //TODO Remove the following method & replace WebPages' toString w/something more useful
    public String toString(Term word){ //turn the term into a string for printing purposes
        return word.getName();
    }

    public void printTerms(){ 	//print all of the terms in the arraylist
        System.out.println("\nWORDS"); System.out.println();
        for (int i = 0; i < termIndex.size(); i++) {
            System.out.println(termIndex.get(i).toString());
        }
    }

    public void pruneStopWords(int n){						//remove a certain number of determined stopwords from the arraylist
        mergeSortByCount(termIndex, 0, termIndex.size() - 1);	//sort terms by total # of occurrences
        
        printTerms();
        
       System.out.println("\nCopies: " + timesMergeSortRan); timesMergeSortRan = 0;
       
        for(int i = 0; i < n; i++){							//remove the number of stop words
            termIndex.remove(termIndex.size() - 1);
        }
        
        mergeSortByWord(termIndex, 0, termIndex.size() - 1);	//re-sort the terms alphabetically
        
        System.out.println("Copies: " + timesMergeSortRan); timesMergeSortRan = 0;
    }

    public String[] whichPages(String word){ 									//find all documents a word is named in
        int index; 
        if(binarySearchAndInsert(word) == -1){						//get index of word of interest
            index = helpingIndex;	//
        }else{
            return null;														//return null if word is not found
        }
        int arraySize = termIndex.get(index).getDocFrequency(); 				//set return array size to the # of docs the word is found in
        String[] documents = new String[arraySize];
        LinkedList<Occurrence> tempArray = termIndex.get(index).getDocsList(); 	//get linked list of occurrences

        for(int i = 0; i < arraySize; i++){										//copy document names into array of strings
            documents[i] = tempArray.get(i).toString();
        }
        return documents;														//return array of document names where word was found
    }

    public void mergeSortByWord(ArrayList<Term> terms, int first, int last){	//merge sort alphabetically from a-z
        if(first < last){
        	timesMergeSortRan++;
            int mid = (first + last) /2;							//set mid
            mergeSortByWord(terms, first, mid);						//sort bottom half
            mergeSortByWord(terms, mid + 1, last);					//sort top half
            mergeByWord(terms, first, mid, last);					//combine two halves in order
        }
    }

    public void mergeSortByCount(ArrayList<Term> terms, int first, int last){ 	//merge sort by total frequency from lowest to highest
    	
        if(first < last){
        	timesMergeSortRan++;
            int mid = (first + last) /2;
           // System.out.println("First: " + first + "\tLast: " + last + "\tMid: " + mid);
            mergeSortByCount(terms, first, mid);
            mergeSortByCount(terms, mid + 1, last);
            mergeByCount(terms, first, mid, last);
        }
    }

    public void mergeByCount(ArrayList<Term> terms, int first, int mid, int last){ //helper method pointing to real sorter
       ArrayList<Term> tempArray = new ArrayList<Term>();
        mergeByCount(terms, tempArray, first, mid, last);
    }

    public void mergeByWord(ArrayList<Term> terms, int first, int mid, int last){ //helper method pointing to real sorter
       ArrayList<Term> tempArray = new ArrayList<Term>();
        mergeByWord(terms, tempArray, first, mid, last);
    }

    public void mergeByWord(ArrayList<Term> terms, ArrayList<Term> tempArray, int first, int mid, int last){ //contains mergesort algorithm for words
        int first1 = first; int last1 = mid; int first2 = mid+1; int last2 = last; int index = first1; 		//set helpful indexes

        while((first1 <= last1) && (first2 <= last2)){										//sort at least one half of the array into the temp array
            if( terms.get(first1).getName().compareTo(terms.get(last1).getName()) < 0){		//if a word is "less than" another lexicographically, add it to the temp array
                //tempArray.add(index, terms.get(first1));
                tempArray.add(terms.get(first1));
                first1++;
            }else{
               // tempArray.add(index, terms.get(first2));
                tempArray.add(terms.get(first2));
                first2++;
            }
            index++;
        }//end while

        while(first1 <= last1){																//finish sorting the first half of the array if necessary
           // tempArray.add(index, terms.get(first1));
            tempArray.add(terms.get(first1));
            first1++;
            index++;
        }//end while

        while(first2 <= last2){																//finish sorting the last half of the array if necessary
            //tempArray.add(index, terms.get(first2));
            tempArray.add(terms.get(first2));
            first2++;
            index++;
        }//end while

        for(int i = 0; i < tempArray.size(); i++){												//copy the temporary array to the legit one
            terms.set(i, tempArray.get(i));
        }
    }

    public void mergeByCount(ArrayList<Term> terms, ArrayList<Term> tempArray, int first, int mid, int last){ //contains mergesort algorithm for counts
        int first1 = first; int last1 = mid; int first2 = mid+1; int last2 = last; int index = first1;

     // System.out.println(terms.toString());
        
        while((first1 <= last1) && (first2 <= last2)){
            if( terms.get(first1).getTotalFrequency() < terms.get(first2).getTotalFrequency()){
            	tempArray.add(terms.get(first1));
            	//System.out.println("First 1 = " + first1 + "\tArray Size is: " + tempArray.size() + "\tIndex is: " + index);
               // tempArray.add(index, terms.get(first1));
                first1++;
            }else{
                tempArray.add(terms.get(first2));
                //tempArray.add(index, terms.get(first2));
                first2++;
            }
            index++;
        }

        while(first1 <= last1){
            tempArray.add(terms.get(first1));
           //tempArray.add(index, terms.get(first1));
            first1++;
            index++;
        }//end while

        while(first2 <= last2){
        	 tempArray.add(terms.get(first2));
         // tempArray.add(index, terms.get(first2));
            first2++;
            index++;
        }//end while

        for(int i = 0; i < tempArray.size(); i++){												//copy the temporary array to the legit one
            terms.set(i, tempArray.get(i));
        }
    }

//    public boolean isHtml(String readString){ 						//check if a string is html
//        if(readString.contains("<") || readString.contains(">")){
//            return true;
//        }return false;
//    }
}
