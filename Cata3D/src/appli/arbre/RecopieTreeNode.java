package appli.arbre;

import java.util.ArrayList;

import appli.values.updater.ObjectUpdater;
import model.composants.Composant;
import model.composants.Recopie;
import model.math.Axis;
import model.math.Decimal;
import model.math.Vecteur;
import model.math.transfo.Rotation;
import model.math.transfo.Translation;

public class RecopieTreeNode extends ComposantTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7587228627542488036L;


	public RecopieTreeNode(DesignTreeNode up, String name, Recopie cmp) {
		super(up, name, cmp);
	}

	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		Recopie pc = (Recopie)composant;
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		ret.add(new TreeNodeProperty ("Source de recopie", pc.autre, true, ObjectUpdater.STRING) );
		ret.add(new TreeNodeProperty ("Position", pc.decalage.trans, true, ObjectUpdater.VECTEUR) );
		
		String axe = "Axe X";
		if (pc.pivot.axis == Axis.YAxis) axe = "Axe Y";
		if (pc.pivot.axis == Axis.ZAxis) axe = "Axe Z";
		ret.add(new TreeNodeProperty ("Axe de rotation", axe, true, ObjectUpdater.VECTEUR) );
				
		ret.add(new TreeNodeProperty ("Angle de rotation", pc.pivot.angle, true, ObjectUpdater.DECIMAL) );
					
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		Recopie pv = (Recopie)composant;

		if (fld.equals("Source de recopie")) {
			String a = (String)value;
			// Recherche le composant
			for (Composant c : pv.source.composants) if (c.nom.equals(a)) pv.autre = c;
		}

		// set la position de de la recopie
		if (fld.equals("Position")) {			
			pv.decalage = new Translation((Vecteur)value, null);
		}

		if (fld.equals("Axe de rotation")) {
			String v = (String)value;
			int axe = -1;
			if (v.equals("Axe X")) axe = Axis.XAxis;
			if (v.equals("Axe Y")) axe = Axis.YAxis;
			if (v.equals("Axe Z")) axe = Axis.ZAxis;
				
			pv.pivot  = new Rotation(axe, pv.pivot.angle, null); 
		}

		if (fld.equals("Angle de rotation")) {
			pv.pivot  = new Rotation(pv.pivot.axis, (Decimal)value, null); 
		}

	}
	
}
