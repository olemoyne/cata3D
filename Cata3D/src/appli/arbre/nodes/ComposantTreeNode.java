package appli.arbre.nodes;

import java.util.ArrayList;

import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.Position;
import model.composants.Composant;

public class ComposantTreeNode extends DesignTreeNode {

	protected Composant composant;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5297335109812442541L;

	public ComposantTreeNode(DesignTreeNode up, String name, Composant cmp) {
		super(up, name);
		composant= cmp;
	}
	
	public Composant getComposant () {
		return composant;
	}

	
	public ArrayList<TreeNodeProperty> getProperties() {
		ArrayList<TreeNodeProperty> ret = new ArrayList<TreeNodeProperty>();
		ret.add(new TreeNodeProperty ("Nom", composant.nom, true, ObjectUpdater.STRING) );
		ret.add(new TreeNodeProperty ("Description", composant.description, true, ObjectUpdater.STRING) );
		ret.add(new TreeNodeProperty ("Position", composant.position, true, ObjectUpdater.POSITION) );
		ret.add(new TreeNodeProperty ("Poids total", composant.gravite, false, ObjectUpdater.POIDS) );
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		if (fld.equals("Nom")) composant.nom = (String)value;
		if (fld.equals("Description")) composant.description = (String)value;
		if (fld.equals("Position")) composant.position= (Position)value;
	}
	
	public String[] getActionList() {
		String[] strs =  {"Ajoute composant", "Supprime composant"};
		return strs;
	}

	public int getNodeLevel() {
		return LEVEL_COMPOSANT;
	}

}
