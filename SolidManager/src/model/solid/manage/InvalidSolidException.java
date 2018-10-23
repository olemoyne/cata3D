package model.solid.manage;

public class InvalidSolidException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creation de l'exception avec le justificatif
	 * @param str
	 */
	public InvalidSolidException (String str) {
		super(str);
	}

}
