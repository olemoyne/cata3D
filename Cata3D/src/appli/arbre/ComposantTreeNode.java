package appli.arbre;

import java.util.ArrayList;

import appli.values.updater.ObjectUpdater;
import model.composants.Composant;
import model.math.Vecteur;

public class ComposantTreeNode extends DesignTreeNode {

	protected Composant composant;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5297335109812442541L;

	public ComposantTreeNode(DesignTreeNode up, String name, Composant cmp) {
		super(up, name);
	}

	
	public ArrayList<TreeNodeProperty> getProperties() {
		ArrayList<TreeNodeProperty> ret = new ArrayList<TreeNodeProperty>();
		ret.add(new TreeNodeProperty ("Nom", composant.nom, true, ObjectUpdater.STRING) );
		ret.add(new TreeNodeProperty ("Description", composant.description, true, ObjectUpdater.STRING) );
		ret.add(new TreeNodeProperty ("Position", composant.position, true, ObjectUpdater.VECTEUR) );
		ret.add(new TreeNodeProperty ("poids total", composant.poids, false, ObjectUpdater.POIDS) );
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		if (fld.equals("Nom")) composant.nom = (String)value;
		if (fld.equals("Description")) composant.description = (String)value;
		if (fld.equals("Position")) composant.position= (Vecteur)value;
	}
	
}
