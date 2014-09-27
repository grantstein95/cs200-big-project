import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/* Code for mergesort methods taken from Data Abstraction and Problem Solving with Java 3rd Edition by
 * Prichard and Carrano with modifications. Some additional inspiration and assistance taken from Dr. Adele Howe's
 * lecture slides for the CS200 course at Colorado State University.
 */ 

public class webPages {
	ArrayList<Term> termIndex = new ArrayList<Term>();		//main arraylist used for storing all term objects
	int timesMergeSortRan;		//counter for how often mergesort is run
	int helpingIndex;			//used for helping with certain methods

	public void addPage(String fileName){
		try {
			Scanner scanFile = new Scanner(new File(fileName)); scanFile.useDelimiter("[ \n\t\r%*\\-,_=~/(){}:!?.]+"); 	//scanner for files with delimiters
			while(scanFile.hasNext()){
				String temp = scanFile.next().toLowerCase(); 			//read a string in (in lower case)

				if(isHtml(temp)){  //ATTENTION BOBBY: this part needs some revising to properly get rid of html tags. It only works if the carrots are touching the word.
					//do nothing
				}else{
					int index = binarySearchAndInsert(termIndex, temp); //get index to put word at

					if(index != -1){									//if the word is not already in the arraylist
						Term tempTerm = new Term(temp, fileName);
						termIndex.add(index, tempTerm);						//add the term to the arraylist
					}else{													//else
						termIndex.get(index).incFrequency(fileName);		//increase frequency of word found in document
					}
				}
			}
			scanFile.close();
		} catch (FileNotFoundException e) { System.out.println("Error: No file found.");
		e.printStackTrace();
		}
	}

	public int binarySearchAndInsert(ArrayList<Term> words, String word){ //search array list for a word to see if it is already present using binary search
		//if it is present, return -1. if not present, return the index it should go in
		int min = 0;														//start of arraylist
		int max = words.size();												//end of arraylist

		while(min < max){ 													//search while we haven't check everything and haven't found a matching entry
			int mid = min + (max - min) / 2;								//get the index
	
			if(word.compareTo(words.get(mid).getName()) < 0){				//if the word is lexicographically less then search lower half
				max = mid;
				
			}else if (word.compareTo(words.get(mid).getName()) > 0){ 		//if the word is lexicographically greater search the upper half
				min = mid + 1;
			}else{															//the word is already in the array
				helpingIndex = mid;
				return -1;
			}
		}// end of while loop
		return max;		//where the word should go if not found
	}

	public String toString(Term word){ //turn the term into a string for printing purposes
		return word.getName();
	}

	public void printTerms(){ 	//print all of the terms in the arraylist
		System.out.println("WORDS");
		for(int i = 0; i < termIndex.size(); i++){
			System.out.println(termIndex.get(i).toString());
		}
	}

	public void pruneStopWords(int n){						//remove a certain number of determined stopwords from the arraylist
		mergeSortByCount(termIndex, 0, termIndex.size());	//sort terms by total # of occurrences
		
		for(int i = 0; i < n; i++){							//remove the number of stop words
			termIndex.remove(termIndex.size());
		}
		mergeSortByWord(termIndex, 0, termIndex.size());	//re-sort the terms alphabetically
	}

	public String[] whichPages(String word){ 									//find all documents a word is named in
		int index; 
		if(binarySearchAndInsert(termIndex, word) == -1){						//get index of word of interest
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
			int mid = (first + last) /2;							//set mid
			mergeSortByWord(terms, first, mid);						//sort bottom half
			mergeSortByWord(terms, mid + 1, last);					//sort top half
			mergeByWord(terms, first, mid, last);					//combine two halves in order
		}
	}

	public void mergeSortByCount(ArrayList<Term> terms, int first, int last){ 	//merge sort by total frequency from lowest to highest

		if(first < last){
			int mid = (first + last) /2;
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
				tempArray.add(index, terms.get(first1));
				first1++;
			}else{
				tempArray.add(index, terms.get(first2));
				first2++;
			}
			index++;
		}//end while
		
		while(first1 <= last1){																//finish sorting the first half of the array if necessary
			tempArray.add(index, terms.get(first1));
			first1++;
			index++;
		}//end while
		
		while(first2 <= last2){																//finish sorting the last half of the array if necessary
			tempArray.add(index, terms.get(first2));
			first2++;
			index++;
		}//end while
		
		for(int i = 0; i < terms.size(); i++){												//copy the temporary array to the legit one
			terms.set(i, tempArray.get(i));
		}
	}

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
		}//end while
		
		while(first2 <= last2){
			tempArray.add(index, terms.get(first2));
			first2++;
			index++;
		}//end while
		
		for(int i = 0; i < terms.size(); i++){
			terms.set(i, tempArray.get(i));
		}
	}

	public boolean isHtml(String readString){ 						//check if a string is html
		if(readString.contains("<") || readString.contains(">")){
			return true;
		}return false;
	}

}
