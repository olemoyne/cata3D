package appli.values;

public class CataValuesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7142935613120016192L;

	
	public CataValuesException(String str) {
		super(str);
	}
	
	public CataValuesException(Exception e) {
		super(e);
	}

}
