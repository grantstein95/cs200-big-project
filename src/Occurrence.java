/**
 * An object that contains information about a Term's
 * occurrence(s) in a particular document.
 * @author Bobby Signor
 */
public class Occurrence implements Comparable<Occurrence> {
	/**
	 * The name of the document this occurrence references.
	 */
	private String docName;
	
	/**
	 * The number of times the term appears in the document.
	 */
	private int termFrequency = 1;

	/**
	 * Constructs a new Occurrence object from a given document name.
	 */
	public Occurrence(String docName) {
		this.docName = docName;
	}

	/**
	 * @return The name of the document this occurrence references.
	 */
	public String getDocName() {
		return this.docName;
	}

	/**
	 * @return The number of times the term appears in the document.
	 */
	public int getTermFrequency() {
		return this.termFrequency;
	}
	
	/**
	 * Increments the frequency within this occurrence.
	 */
	public void incFrequency() {
		++termFrequency;
	}
	
	public boolean equals(Object o) {
		return o instanceof Occurrence  && ((Occurrence) o).getDocName().equals(this.docName);
	}
	
	public String toString() {
		return this.docName;
	}

    @Override
    public int compareTo(Occurrence o) {
        return this.docName.compareTo(o.getDocName());
    }
}
