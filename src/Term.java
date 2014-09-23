import java.util.LinkedList;

/**
 * An object that contains information about a specific 
 * term used in one or more documents.
 * @author Bobby Signor
 */
public class Term {
	/**
	 * The string that stores what term from the document this object represents in lower case.
	 */
	private String name;
	
	/**
	 * The number of times this term has appeared in documents.
	 */
	private int docFrequency = 0;
	
	/**
	 * The list of the occurrences of this term in the documents.
	 */
	private LinkedList<Occurrence> docsList = new LinkedList<Occurrence>();

	/**
	 * <p>Constructs a new Term object from a given name.</p>
	 * <p><i>NOTE: Prefer</i> {@code Term(String name, String  docName)} <i>constructor for
	 * actual implementation. </i></p>
	 * @param name The string the new term will represent.
	 */
	public Term(String name) {
		this.name = name.toLowerCase();
	}
	
	/**
	 * Constructs a new Term object from a given name and adds a new occurrence to the list.
	 * @param name The string the new term will represent.
	 * @param docName The name of the document where the string occurred.
	 */
	public Term(String name, String  docName) {
		this(name);
		incFrequency(docName);
	}

	/**
	 * @return The string from the documents this term represents.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return The number of times this term has appeared in the documents.
	 */
	public int getDocFrequency() {
		return this.docFrequency;
	}
	
	/**
	 * @return The list of occurrences for this term.
	 */
	public LinkedList<Occurrence> getDocsList() {
		return this.docsList;
	}
	
	/**
	 * Registers the occurrence with docsList by either adding 
	 * a new Occurrence or incrementing an existing one.
	 * @param docName The name of the document where the string occurred.
	 */
	public void incFrequency(String docName) {
		Occurrence newDoc = new Occurrence(docName);	//Make a new Occurrence object from the name of the document
		
		for (int i = 0; i < docsList.size(); i++)  {	//Iterate through the entire list
			Occurrence o = docsList.get(i);				//Get the current Occurrence
			if (o.equals(newDoc)) {						//If the new one matches the current one...
				o.incFrequency();						//...increment the frequency...
				return;									//...& GTFO.
			} else if (docName.compareTo(o.getDocName()) < 0) {	//If we're in the right spot but there's no Occurrence...
				docsList.add(i, newDoc);				//..add it to the list in the current position...
				this.docFrequency++;					//...increment the document counter...
				return;									//...& GTFO.
			}
		}
		docsList.add(newDoc);	//If we've reached the end of the list, add it to the end...
		this.docFrequency++;	//...increment the document counter...
		//...& GTFO.
	}
	
	public String toString() {
		return this.name;
	}

	public boolean equals(Object o) {
		return o instanceof Term && ((Term) o).getName().equals(this.name);
	}
	
	/**
	 * Test code for Term & Occurrence models.
	 */
	public static void main(String[] args) {
		Term test = new Term("term1", "doc1");
		test.incFrequency("doc3");
		test.incFrequency("doc1");
		test.incFrequency("doc3");
		test.incFrequency("adoc4");
		
		LinkedList<Occurrence> l = test.getDocsList();
		System.out.printf("%d document(s) contain \"%s\":\n", test.getDocFrequency(), test);
		for (int i = 0; i < l.size(); i++) {
			System.out.printf("%s \t%d%n", l.get(i), l.get(i).getTermFrequency());
		}
	}
}
