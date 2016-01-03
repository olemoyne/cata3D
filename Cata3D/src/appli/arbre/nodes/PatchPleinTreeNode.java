package appli.arbre.nodes;

import java.util.ArrayList;

import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.composants.PatchPlein;
import model.math.Decimal;

public class PatchPleinTreeNode extends PatchTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7587228627542488036L;


	public PatchPleinTreeNode(DesignTreeNode up, String name, PatchPlein cmp) {
		super(up, "Patch plein : "+name, cmp);
	}

	
	public ArrayList<TreeNodeProperty> getProperties() {
		PatchPlein pv = (PatchPlein)composant;
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		ret.add(new TreeNodeProperty ("Densité", pv.densite, true, ObjectUpdater.DECIMAL) );
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		super.updateValue(fld, value);

		PatchPlein pv = (PatchPlein)composant;

		if (fld.equals("Densité")) pv.densite = (Decimal)value;
	}
	
}
