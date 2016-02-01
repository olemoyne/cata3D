package appli.values.updater;


/** 
 * Permet de mettre à jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class BooleanUpdater extends ObjectUpdater {

	BooleanField fld;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301380L;

	public BooleanUpdater () {
		super();	
		fld = new BooleanField("Valeur : ");
		add(fld);
	}
	
	public void setOptions(Object[] opts) {
	}
	
	public void setProperty (Object val) {
		Boolean v = (Boolean)val;
		fld.setField(v);
	}
	
	public Object getData() {
		return (Boolean) fld.getValue();
	}
	
}
