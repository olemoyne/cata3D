package appli.arbre;


public class TreeNodeProperty {
	
	/** Nom de la propriété **/
	public String nom;
	
	/** Valeur de la propriété **/
	public Object value;  
	
	public boolean editable;
	
	public String editeurType;

	public Object [] optionList;

	public TreeNodeProperty(String n, Object v, boolean e, String u) {
		nom = n;
		value = v;
		editable = e;
		editeurType = u;
	}

	public Object[] getOptions() {
		return optionList;
	}

	public void setOptions(Object[] o) {
		this.optionList = o;
	}


}
