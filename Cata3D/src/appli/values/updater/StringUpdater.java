package appli.values.updater;

/** 
 * Permet de mettre � jour une chaine de caractère
 * 
 * @author olemoyne
 *
 */
public class StringUpdater extends ObjectUpdater {

	StringField fld;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301380L;

	public StringUpdater () {
		super();	
		fld = new StringField("Valeur : ");
		add(fld);
	}
	
	public void setProperty (Object val) {
		String v = (String)val;
		fld.setField(v);
	}
	
	public Object getData() {
		return (String) fld.getValue();
	}
	
}
