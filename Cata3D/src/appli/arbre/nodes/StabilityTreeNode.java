package appli.arbre.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.Cata;

public class StabilityTreeNode extends DesignTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 156467930396700730L;

	private Cata bateau;

	public StabilityTreeNode(DefaultMutableTreeNode up, Cata boat, String name) {
		super(up, name);
		bateau = boat;
	}

	/**
	 * Gestion du patch de donnÃ©es
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		
		if (bateau == null) return ret;
		
		if (bateau.mer != null) {
			ret.add(new TreeNodeProperty ("Poids total", bateau.mer.poidsTotal, false, ObjectUpdater.POIDS) );
			ret.add(new TreeNodeProperty ("Poussée", bateau.mer.pousseeArchimede, false, ObjectUpdater.POIDS) );
		}	
		return ret;
	}

	

}
