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
    private BST<Term> termIndex = new BST<Term>();
    
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
                    this.termIndex.add(new Term(s, fileName));       //...take the word, make it into a term,
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
        this.termIndex.add(newTerm);
    }

    /**
     * Finds a term in the list or returns null if it's not found.
     * @param name The name of the term to look for
     * @return The term if found, null otherwise
     * @author Bobby Signor
     */
    public Term findWord(String name) {
        return this.termIndex.get(new Term(name), false);
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
     * Prints the Terms in termIndex in PA2's format
     * @author Grant Stein
     */
    public void printTerms(){
        System.out.println("WORDS");
        for (Term t : this.termIndex) {
            System.out.println(t);
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
