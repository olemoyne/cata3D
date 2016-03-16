package appli.arbre.nodes;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import view.scene.PrintableObject;
import view.scene.PrintableScene;
import view.scene.PrintedArea;
import view.scene.PrintedMap;
import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.Area;
import model.Cata;
import model.Position;
import model.composants.Composant;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Vecteur;
import model.math.transfo.Transformation;

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
		PrintableScene ret = new PrintableScene();

		if (bateau == null) return ret;
				
		// Affiche chaque composant
		for (Composant cmp : this.bateau.composants) {
			for (PrintableObject obj : cmp.getSceneObjects(cmp.situation)) {
				ret.add(obj);
			}
		}
		Transformation trs = bateau.mer.getTransformation();
		// TODO : Affiche la mer ... Y = 0 
		ArrayList<Vecteur> mer = new ArrayList<Vecteur>();
		mer.add(trs.getPoint(new Vecteur ("-8;0;-8")));
		mer.add(trs.getPoint(new Vecteur ("8;0;-8")));
		mer.add(trs.getPoint(new Vecteur ("8;0;8")));
		mer.add(trs.getPoint(new Vecteur ("-8;0;8")));
		
		Area a = new Area();
		a.points.addAll(mer);
		
		ret.add(new PrintedArea(a, "Mer", true, Color.blue, new Position()));
// Seconde face
		mer = new ArrayList<Vecteur>();
		mer.add(trs.getPoint(new Vecteur ("-8;0;-8")));
		mer.add(trs.getPoint(new Vecteur ("-8;0;8")));
		mer.add(trs.getPoint(new Vecteur ("8;0;8")));
		mer.add(trs.getPoint(new Vecteur ("8;0;-8")));
		
		a = new Area();
		a.points.addAll(mer);
		
		ret.add(new PrintedArea(a, "Mer dessous", true, Color.blue, new Position()));

		
		
/**		
		// Affiche la carène des objects ...
		for (MapDeVecteurs m : bateau.mer.carenes) {
			ret.add(new PrintedMap(m, "Carène", true, Color.blue, new Position()));
			
		}
**/		return ret;
	}


}
