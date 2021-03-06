package appli.arbre.nodes;

import java.util.ArrayList;

import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.composants.PatchVide;
import model.math.Decimal;

public class PatchVideTreeNode extends PatchTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7587228627542488036L;


	public PatchVideTreeNode(DesignTreeNode up, String name, PatchVide cmp) {
		super(up, "Patch vide : "+name, cmp);
	    new GabaritsTreeNode(this, cmp);
	    new PoutresTreeNode(this, cmp);
	    new QuilleTreeNode(this, cmp);
	}

	
	public ArrayList<TreeNodeProperty> getProperties() {
		PatchVide pv = (PatchVide)composant;
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		ret.add(new TreeNodeProperty ("Epaisseur de bardage", pv.epaisseurDeBardage, true, ObjectUpdater.DECIMAL) );
		ret.add(new TreeNodeProperty ("Densité de bardage", pv.densiteBardage, true, ObjectUpdater.DECIMAL) );
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		super.updateValue(fld, value);
		PatchVide pv = (PatchVide)composant;

		if (fld.equals("Epaisseur de bardage")) pv.epaisseurDeBardage = (Decimal)value;
		if (fld.equals("Densité de bardage")) pv.densiteBardage = (Decimal)value;
	}
	
}
