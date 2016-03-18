package appli.arbre.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import view.scene.PrintableScene;
import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.Cata;
import model.CataScene;
import model.math.Decimal;

/**
 * Permet de gérer les calculs de stabilité et de dérive sur la coque
 * 
 * @author olemoyne
 *
 */
public class StudyTreeNode extends DesignTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6451719310760204674L;

	private Cata bateau;
	
	public StudyTreeNode(DefaultMutableTreeNode up, Cata boat, String name) {
		super(up, name);
		bateau = boat;
		
		new StabilityTreeNode( this, boat, "Stabilité");
		new DeriveTreeNode( this, boat, "Dérive");		
		new DynamismeTreeNode( this, boat, "Dynamisme");
	}
	

	/**
	 * Gestion du patch de donnÃ©es
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		
		if (bateau == null) return ret;
		
		if (bateau.mer != null) {
			ret.add(new TreeNodeProperty ("Pilonnement", bateau.mer.pilonnement, false, ObjectUpdater.DECIMAL) );
			ret.add(new TreeNodeProperty ("Gîte", bateau.mer.gite, false, ObjectUpdater.DECIMAL) );
			ret.add(new TreeNodeProperty ("Tangage", bateau.mer.tangage, false, ObjectUpdater.DECIMAL) );
		}
		
		return ret;
	}

	public void updateValue (String fld, Object value) {
		if (bateau == null)
			return;
		
		if (fld.equals("Pilonnement")) {
			bateau.mer.pilonnement = (Decimal)value;
		}
		if (fld.equals("Gîte")) {
			bateau.mer.gite= (Decimal)value;
		}
		if (fld.equals("Tangage")) {
			bateau.mer.tangage= (Decimal)value;
		}
		bateau.recalcule();
	}

	/** 
	 * Permet d'afficher la scene correspondant Ã  la vue dÃ©finie
	 * 
	 */
	public PrintableScene getScene () {
		return CataScene.getNavigationScene(bateau);
	}


}
