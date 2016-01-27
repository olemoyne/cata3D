package appli.arbre.nodes;

import java.util.ArrayList;

import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.composants.Composant;
import model.composants.Recopie;
import model.math.Axis;
import model.math.Decimal;
import model.math.transfo.Rotation;

public class RecopieTreeNode extends ComposantTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7587228627542488036L;


	public RecopieTreeNode(DesignTreeNode up, String name, Recopie cmp) {
		super(up, "Recopie : "+name, cmp);
	}

	/**
	 * Gestion du patch de donn√©es
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		Recopie pc = (Recopie)composant;
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		String[] str = new String[pc.boat.composants.size()];
		for (int p= 0; p < pc.boat.composants.size(); p++) {
			str[p] = pc.boat.composants.get(p).nom;
		}
		
		if (pc.autre == null) {
			TreeNodeProperty prp = new TreeNodeProperty ("Source de recopie", "Non dÈfini", true, ObjectUpdater.OPTION);
			prp.setOptions(str);
			ret.add(prp);
		} else {
			TreeNodeProperty prp = new TreeNodeProperty ("Source de recopie", pc.autre.nom, true, ObjectUpdater.OPTION);
			prp.setOptions(str);
			ret.add(prp);
		}
		String[] axes = {"Axes X", "Axes Y", "Axes Z"};
		if (pc.pivot != null) {
			String axe = "Axe X";
			if (pc.pivot.axis == Axis.YAxis) axe = "Axe Y";
			if (pc.pivot.axis == Axis.ZAxis) axe = "Axe Z";
			TreeNodeProperty prp = new TreeNodeProperty ("Axe de rotation", axe, true, ObjectUpdater.OPTION) ;
			prp.setOptions(axes);
			ret.add(prp);
					
			ret.add(new TreeNodeProperty ("Angle de rotation", pc.pivot.angle, true, ObjectUpdater.DECIMAL) );
		} else {
			TreeNodeProperty prp = new TreeNodeProperty ("Axe de rotation", "Axe Z", true, ObjectUpdater.OPTION) ;
			prp.setOptions(axes);
			ret.add(prp);
			
			ret.add(new TreeNodeProperty ("Angle de rotation", new Decimal (), true, ObjectUpdater.DECIMAL) );
			
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

		if (fld.equals("Axe de rotation")) {
			String v = (String)value;
			int axe = -1;
			if (v.equals("Axe X")) axe = Axis.XAxis;
			if (v.equals("Axe Y")) axe = Axis.YAxis;
			if (v.equals("Axe Z")) axe = Axis.ZAxis;
			
			if (pv.pivot != null)
				pv.pivot  = new Rotation(axe, pv.pivot.angle, null);
			else 
				pv.pivot  = new Rotation(axe, Decimal.ZERO, null);
		}

		if (fld.equals("Angle de rotation")) {
			pv.pivot  = new Rotation(pv.pivot.axis, (Decimal)value, null); 
		}

	}
	
}
