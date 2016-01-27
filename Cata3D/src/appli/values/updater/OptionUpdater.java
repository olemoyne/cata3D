package appli.values.updater;


/** 
 * Permet de mettre à jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class OptionUpdater extends ObjectUpdater {

	OptionField fld;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301380L;

	public OptionUpdater () {
		super();	
		String[] lst = new String[0];
		fld = new OptionField("Valeur : ", lst);
		add(fld);
	}
	
	public void setOptions(Object[] opts) {
		String[] lst = new String[opts.length];
		for (int p = 0; p < opts.length; p ++) {
			lst[p] = opts[p].toString();
		}
		fld.setValues(lst);
	}
	
	public void setProperty (Object val) {
		String v = (String)val;
		fld.setField(v);
	}
	
	public Object getData() {
		return (String) fld.getValue();
	}
	
}
