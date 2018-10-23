package appli.values.updater;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/** 
 * Permet de mettre � jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class ObjectUpdater extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301380L;
	
	public static final String STRING = "String";
	public static final String VECTEUR = "Vecteur";
	public static final String POIDS = "Poids";
	public static final String DECIMAL = "Decimal";
	public static final String INTEGER = "Integer";
	public static final String GABARIT = "Gabarit";
	public static final String POUTRE = "Poutre";
	public static final String OPTION = "Option";
	public static final String POSITION = "Position";
	public static final String BOOLEAN = "Boolean";
	public static final String APPARENCE = "Apparence";
	public static final String COMPARTIMENT = "Compartiment";

	public ObjectUpdater () {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
	public void setProperty (Object val) {
	}
	
	public Object getData() {
		return null;
	}

	public void setOptions(Object[] opts) {
		// TODO Auto-generated method stub
		
	}

}
