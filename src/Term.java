import java.util.LinkedList;

/**
 * An object that contains information about a specific 
 * term used in one or more documents.
 * @author Bobby Signor
 */
public class Term {
	/**
	 * The string that stores what term from the document this object represents.
	 */
	private String name;
	
	/**
	 * The number of times this term has appeared in documents.
	 */
	private int docFrequency = 1;
	
	/**
	 * The list of the occurrences of this term in the documents.
	 */
	private LinkedList<Occurrence> docsList = new LinkedList<Occurrence>();

	/**
	 * <p>Constructs a new Term object from a given name.</p>
	 * <p><i>NOTE: Prefer Term(String name, String  docName) for </i></p>
	 * @param name The string the new term will represent.
	 */
	public Term(String name) {
		this.name = name;
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
	 * Registers the occurrence with docsList by either adding 
	 * a new Occurrence or incrementing an existing one.
	 * @param docName The name of the document where the string occurred.
	 */
	public void incFrequency(String docName) {
		Occurrence newDoc = new Occurrence(docName);	//Make a new Occurrence object from the name of the document
		Occurrence o;	//Temporary variable for the current one in the list
		
		for (int i = 0; i < docsList.size(); i++)  {	//Iterate through the entire list
			//Get the current Occurrence
			o = docsList.get(i);
			if (o.equals(newDoc)) {	//If the new one matches the current one...
				o.incFrequency();	//...increment the frequency...
				return;	//...& GTFO.
			} else if (docName.compareTo(o.getDocName()) > 0) {	//If we're in the right spot but there's no Occurrence...
				docsList.add(i, newDoc);	//..add it to the list in the current position...
				return;	//...& GTFO.
			}
		}
		docsList.add(newDoc); //If we've reached the end of the list, add it to the end & GTFO.
	}
}
