import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Object used to store and organize terms.
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
    private int timesMerged;

    /**
     * Default constructor.
     */
    public WebPages() {
        this.timesMerged = 0;
    }

    /**
     * Adds the terms from a given page into the term index.
     * @param fileName The name of the file to be parsed
     * @author Grant Stein & Bobby Signor
     */
    public void addPage(String fileName){
        try {
            Scanner scanFile = new Scanner(new File(fileName));
            scanFile.useDelimiter("(<[^>]*>[^A-z0-9<>]*)|([^A-z0-9'<>]+)");     //Tell the scanner to filter out all HTML tags & extraneous punctuation as delimiters
            while(scanFile.hasNext()) {                               //While there's still stuff left in the document...
                String s = scanFile.next();
                if (!s.equals(""))
                    addWord(new Term(s, fileName));       //...take the word, make it into a term,
            }
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
     * @author Bobby Signor
     */
    public Term findWord(String name) {
        int start = 0, end = this.termIndex.size() - 1, middle = start + ((end - start) / 2);
        Term newTerm = new Term(name);

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
     * Returns an array with the names of all the files where the given word occurs.
     * @param word The word to look for
     * @return An array containing the names of all the files where the word occurs
     * @author Bobby Signor & Grant Stein
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
     * @author Grant Stein
     */
    public void pruneStopWords(int n){                      //remove a certain number of determined stopwords from the arraylist
        this.timesMerged = 0;
        mergeSortByCount(0, termIndex.size() - 1);          //sort terms by total # of occurrences
        System.out.println("Copies: " + this.timesMerged);
        for(int i = 0; i < n; i++){                         //remove the number of stop words
            termIndex.remove(termIndex.size() - 1);
        }
        this.timesMerged = 0;
        mergeSortByName(0, termIndex.size() - 1);           //re-sort the terms alphabetically
        System.out.println("Copies: " + this.timesMerged + "\n");
    }

    /**
     * Performs a Merge Sort on termIndex by total frequency.
     * @param first The first element in the section.
     * @param last The last element in the section.
     * @author Bobby Signor
     */
    private void mergeSortByCount(int first, int last) {
        if (last - first > 0) {
            int middle = first + ((last - first) / 2);
            mergeSortByCount(first, middle);
            mergeSortByCount(middle + 1, last);
            mergeByCount(first, middle, middle + 1, last);
        }
    }

    /**
     * Merges 2 sections of the array back together in ascending order by total frequency.
     * @param s1Front The front of the first section of the array.
     * @param s1Back The back of the first section of the array.
     * @param s2Front The front of the second section of the array.
     * @param s2Back The back of the second section of the array.
     * @author Bobby Signor
     */
    private void mergeByCount(int s1Front, int s1Back, int s2Front, int s2Back) {
        int s1Current = s1Front, s2Current = s2Front;
        ArrayList<Term> temp = new ArrayList<Term>(s2Back - s1Front);

        while (s1Current <= s1Back && s2Current <= s2Back) {
            if (this.termIndex.get(s1Current).getTotalFrequency() <= this.termIndex.get(s2Current).getTotalFrequency()) {
                this.timesMerged++;
                temp.add(this.termIndex.get(s1Current));
                s1Current++;
            } else {
                temp.add(this.termIndex.get(s2Current));
                s2Current++;
            }
        }

        while (s1Current <= s1Back)
            temp.add(this.termIndex.get(s1Current++));

        while (s2Current <= s2Back)
            temp.add(this.termIndex.get(s2Current++));

        for (int i = 0; i < temp.size(); i++)
            this.termIndex.set(i + s1Front, temp.get(i));
    }

    /**
     * Performs a Merge Sort on termIndex in alphabetical order.
     * @param first The first element in the section.
     * @param last The last element in the section.
     * @author Bobby Signor
     */
    private void mergeSortByName(int first, int last) {
        if (last - first > 0) {
            mergeSortByName(first, first + ((last - first) / 2));
            mergeSortByName((first + ((last - first) / 2)) + 1, last);
            mergeByName(first, first + ((last - first) / 2), (first + ((last - first) / 2)) + 1, last);
        }
    }

    /**
     * Merges 2 sections of the array back together in ascending alphabetical order.
     * @param s1Front The front of the first section of the array.
     * @param s1Back The back of the first section of the array.
     * @param s2Front The front of the second section of the array.
     * @param s2Back The back of the second section of the array.
     * @author Bobby Signor
     */
    private void mergeByName(int s1Front, int s1Back, int s2Front, int s2Back) {
                int s1Current = s1Front, s2Current = s2Front;
        ArrayList<Term> temp = new ArrayList<Term>(s2Back - s1Front);

        while (s1Current <= s1Back && s2Current <= s2Back) {
            if (this.termIndex.get(s1Current).compareTo(this.termIndex.get(s2Current)) <= 0) {
                this.timesMerged++;
                temp.add(this.termIndex.get(s1Current));
                s1Current++;
            } else {
                temp.add(this.termIndex.get(s2Current));
                s2Current++;
            }
        }

        while (s1Current <= s1Back)
            temp.add(this.termIndex.get(s1Current++));

        while (s2Current <= s2Back)
            temp.add(this.termIndex.get(s2Current++));

        for (int i = 0; i < temp.size(); i++)
            this.termIndex.set(i + s1Front, temp.get(i));
    }

    /**
     * Prints the Terms in termIndex in PA2's format
     * @author Grant Stein
     */
    public void printTerms(){
        System.out.println("WORDS");
        for (int i = 0; i < termIndex.size(); i++) {
            System.out.println(termIndex.get(i));
        }
        System.out.println();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Term t : this.termIndex)
            s.append((t.getName() + "\t" + t.getTotalFrequency() + "\n"));
        return s.toString();
    }

    /**
     * An internal structure representing a Binary Search Tree.
     * @author Bobby Signor
     * @param <T> The data type to be stored by the data structure.
     */
    private class BST<T extends Term> implements Iterable<T> {
        private BSTNode<T> root = null;
        private int size = 0;

        public BST() { /* Empty Constructor */ }

        public BST(T root) {
            add(root);
        }

        public void add(T item) {
            if (item != null) {
                if (size == 0) {
                    root = new BSTNode<T>(item);
                    size++;
                } else {
                    add(item, this.root);
                }
            }
        }

        private void add(T item, BSTNode<T> current) {
            BSTNode<T> temp;
            int compResult = current.getItem().compareTo(item);

            if (compResult == 0) {
                current.getItem().incFrequency(item.getDocsList().get(0).getDocName());
            } else if (compResult > 0) {
                temp = current.getRightNode();
                if (temp != null) {
                    add(item, temp);
                } else {
                    current.setRightNode(new BSTNode<T>(item));
                }
            } else {
                temp = current.getLeftNode();
                if (temp != null) {
                    add(item, temp);
                } else {
                    current.setLeftNode(new BSTNode<T>(item));
                }
            }
        }

        public T get(T item, boolean printDepth) {
            return get(item, printDepth, 0, this.root);
        }

        private T get(T item, boolean printDepth, int depth, BSTNode<T> current) {
            if (current != null) {
                int compResult = current.getItem().compareTo(item);
                if (compResult == 0) {
                    if (printDepth)
                        System.out.printf("  At depth %d\n", depth);
                    return current.getItem();
                } else if (compResult > 0) {
                    return get(item, printDepth, depth + 1, current.getRightNode());
                } else {
                    return get(item, printDepth, depth + 1, current.getLeftNode());
                }
            } else {
                if (printDepth)
                    System.out.printf("  At depth %d\n", depth);
                return null;
            }
        }

        public int size() {
            return this.size;
        }

        public boolean isEmpty() {
            return this.size == 0;
        }

        /**
         * The internal object model for the nodes in the tree.
         * @author Bobby Signor
         * @param <T> The data type to be stored by the data structure.
         */
        private class BSTNode<T extends Term> {
            private T item;
            private BSTNode<T> leftNode = null;
            private BSTNode<T> rightNode = null;

            /**
             * Constructs a BSTNode from an item.
             * @param item The item to create the BSTNode from.
             */
            public BSTNode(T item) {
                this.item = item;
            }

            /**
             * Returns the item this node represents.
             * @return the item the term represents.
             */
            public T getItem() {
                return item;
            }

            /**
             * Sets this node's left reference to the given node.
             * @param leftNode New node to be assigned to the left position.
             */
            public void setLeftNode(BSTNode<T> leftNode) {
                this.leftNode = leftNode;
            }

            public BSTNode<T> getLeftNode() {
                return this.leftNode;
            }

            public void setRightNode(BSTNode<T> rightNode) {
                this.rightNode = rightNode;
            }

            public BSTNode<T> getRightNode() {
                return this.rightNode;
            }
        }

        /**
         * An in-order iterator for the BST structure.
         * @author Bobby Signor
         * @param <T> The data type to be stored by the data structure.
         */
        private class BSTIterator<T extends Term> implements Iterator<T> {
            private LinkedBlockingQueue<T> inorder = new LinkedBlockingQueue<T>();
            private BST<T> origin;
            
            public BSTIterator(BST<T> origin)  {
                this.origin = origin;
            }

            private void startAssembleInorder() {
                assembleInorder(this.origin.root);
            }

            private void assembleInorder(BST<T>.BSTNode<T> current) {
                if (current != null) {
                    assembleInorder(current.getLeftNode());
                    this.inorder.add(current.getItem());
                    assembleInorder(current.getRightNode());
                }
            }
            
            @Override
            public boolean hasNext() {
                return this.inorder.isEmpty();
            }

            @Override
            public T next() {
                return this.inorder.poll();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        @Override
        public Iterator<T> iterator() {
            return new BSTIterator<T>(this);
        }
    }
}
