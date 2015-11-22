package appli.arbre;

import java.util.ArrayList;

import appli.values.updater.ObjectUpdater;
import model.composants.PatchComposant;
import model.composants.PatchPlein;
import model.math.Decimal;
import model.math.Vecteur;

public class PatchTreeNode extends ComposantTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7587228627542488036L;


	public PatchTreeNode(DesignTreeNode up, String name, PatchComposant cmp) {
		super(up, name, cmp);
	}

	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		PatchComposant pc = (PatchComposant)composant;
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		ret.add(new TreeNodeProperty ("Taille X", pc.patch.x, true, ObjectUpdater.DECIMAL) );
		ret.add(new TreeNodeProperty ("Taille Y", pc.patch.y, true, ObjectUpdater.DECIMAL) );
		
		for (int x = 0; x < pc.patch.x ; x++) {
			for (int y = 0; y < pc.patch.y ; y++) {
				StringBuilder sb = new StringBuilder("Point (");
				sb.append(x);
				sb.append(", ");
				sb.append(y);
				sb.append(")");
			
				ret.add(new TreeNodeProperty (sb.toString(), pc.patch.points[x][y], true, ObjectUpdater.VECTEUR) );
			}
		}
			
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		PatchPlein pv = (PatchPlein)composant;

		if (fld.equals("Taille X")) {
			int x  = (int) Math.round(((Decimal)value).doubleValue());
			pv.setPatch(x, pv.patch.y);
		}

		if (fld.equals("Taille Y")) {
			int y  = (int) Math.round(((Decimal)value).doubleValue());
			pv.setPatch(pv.patch.x, y);
		}
		
		// Retrouve la position du point 
		if (fld.startsWith("Point (")) {
			int pos = fld.indexOf(", ");
			String xstr = fld.substring(7, pos);
			int x = Integer.parseInt(xstr);
			int pos2 = fld.indexOf(")");
			String ystr = fld.substring(pos+2, pos2);
			int y = Integer.parseInt(ystr);
			
			pv.patch.points[x][y] = (Vecteur)value;
		}
	}
	
}
