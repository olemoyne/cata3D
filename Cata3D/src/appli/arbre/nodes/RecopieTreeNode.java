package appli.arbre.nodes;

import java.util.ArrayList;

import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.composants.Composant;
import model.composants.Recopie;

public class RecopieTreeNode extends ComposantTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7587228627542488036L;


	public RecopieTreeNode(DesignTreeNode up, String name, Recopie cmp) {
		super(up, "Recopie : "+name, cmp);
	}

	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		Recopie pc = (Recopie)composant;
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		String[] str = new String[pc.boat.composants.size()];
		for (int p= 0; p < pc.boat.composants.size(); p++) {
			str[p] = pc.boat.composants.get(p).nom;
		}
		
		if (pc.autre == null) {
			TreeNodeProperty prp = new TreeNodeProperty ("Source de recopie", "Non d�fini", true, ObjectUpdater.OPTION);
			prp.setOptions(str);
			ret.add(prp);
		} else {
			TreeNodeProperty prp = new TreeNodeProperty ("Source de recopie", pc.autre.nom, true, ObjectUpdater.OPTION);
			prp.setOptions(str);
			ret.add(prp);
		}
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		super.updateValue(fld, value);

		Recopie pv = (Recopie)composant;

		if (fld.equals("Source de recopie")) {
			String a = (String)value;
			// Recherche le composant
			for (Composant c : pv.boat.composants) if (c.nom.equals(a)) pv.autre = c;
		}
	}
	
}
