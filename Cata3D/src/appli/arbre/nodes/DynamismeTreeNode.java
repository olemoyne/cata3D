package appli.arbre.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import view.scene.PrintableScene;
import view.scene.PrintedFluidityMap;
import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.Cata;
import model.Position;
import model.calcul.CalculCoque;
import model.math.Decimal;
import model.math.MapDeVecteurs;

public class DynamismeTreeNode extends DesignTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1673478056209937230L;
	private Cata bateau;

	public DynamismeTreeNode(DefaultMutableTreeNode up, Cata boat, String name) {
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
			Decimal surf = Decimal.ZERO;
			for (MapDeVecteurs mps : bateau.mer.carenes) {
				Decimal s = CalculCoque.calculeSurfaceCoque(mps);
				surf = surf.add(s);
			}
			ret.add(new TreeNodeProperty ("Surface immergée", surf, false, ObjectUpdater.DECIMAL) );
		}	

		return ret;
	}

	/** 
	 * Permet d'afficher la scene correspondant Ã  la vue dÃ©finie
	 * 
	 */
	public PrintableScene getScene () {
		PrintableScene  ret =  new PrintableScene(); // CataScene.getNavigationScene(bateau);
		// Affiche les carenes
		if (bateau.mer.carenes != null) {
			for (MapDeVecteurs a : bateau.mer.carenes) 
				ret.add(new PrintedFluidityMap("Carène", a, new Position()));
		}
		return ret;
	}



	
}
