package appli.arbre.nodes;

import java.util.ArrayList;

import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.composants.Tube;
import model.math.Decimal;

public class TubeTreeNode extends ComposantTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7587228627542488036L;


	public TubeTreeNode(DesignTreeNode up, String name, Tube cmp) {
		super(up, "Tube : "+name, cmp);
	}

	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		Tube pc = (Tube)composant;
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		ret.add(new TreeNodeProperty ("Densité", pc.densite, true, ObjectUpdater.DECIMAL) );
		ret.add(new TreeNodeProperty ("Diamètre", pc.diametre, true, ObjectUpdater.DECIMAL) );
		ret.add(new TreeNodeProperty ("Epaisseur", pc.epaisseur, true, ObjectUpdater.DECIMAL) );
		ret.add(new TreeNodeProperty ("Longueur", pc.longueur, true, ObjectUpdater.DECIMAL) );
		
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		super.updateValue(fld, value);

		Tube pv = (Tube)composant;

		if (fld.equals("Densité")) {
			pv.densite = (Decimal)value;
		}

		if (fld.equals("Diamètre")) {
			pv.diametre = (Decimal)value;
		}

		if (fld.equals("Epaisseur")) {
			pv.epaisseur = (Decimal)value;
		}

		if (fld.equals("Longueur")) {
			pv.longueur = (Decimal)value;
		}
	}
	
}
