package appli;

public class CataAppliException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5902627210810347718L;
	
	public CataAppliException (String str) {
		super(str);
	}

	
	public CataAppliException (Exception str) {
		super(str);
	}

}
