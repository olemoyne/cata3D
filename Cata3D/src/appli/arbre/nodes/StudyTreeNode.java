package appli.arbre.nodes;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import view.scene.PrintableObject;
import view.scene.PrintableScene;
import view.scene.PrintedMap;
import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.Cata;
import model.composants.Composant;
import model.math.Decimal;
import model.math.MapDeVecteurs;

/**
 * Permet de g�rer les calculs de stabilit� et de d�rive sur la coque
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
		
		new StabilityTreeNode( this, boat, "Stabilit�");
		new DeriveTreeNode( this, boat, "D�rive");		
	}
	

	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		
		if (bateau == null) return ret;
		
		if (bateau.mer != null) {
			ret.add(new TreeNodeProperty ("Pilonnement", bateau.mer.pilonnement, false, ObjectUpdater.DECIMAL) );
			ret.add(new TreeNodeProperty ("G�te", bateau.mer.gite, false, ObjectUpdater.DECIMAL) );
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
		if (fld.equals("G�te")) {
			bateau.mer.gite= (Decimal)value;
		}
		if (fld.equals("Tangage")) {
			bateau.mer.tangage= (Decimal)value;
		}
		bateau.recalcule();
	}

	/** 
	 * Permet d'afficher la scene correspondant à la vue définie
	 * 
	 */
	public PrintableScene getScene () {
		PrintableScene ret = new PrintableScene();

		if (bateau == null) return ret;
		
		// Affiche chaque composant
		for (Composant cmp : this.bateau.composants) {
			for (PrintableObject obj : cmp.getSceneObjects(cmp.situation.getTransformation(null))) {
				ret.add(obj);
			}
		}
		// TODO : Affiche la mer ...
		
		
		// Affiche la car�ne des objects ...
		for (MapDeVecteurs m : bateau.mer.carenes) {
			ret.add(new PrintedMap(m, "Car�ne", true, Color.blue));
			
		}
		return ret;
	}


}
