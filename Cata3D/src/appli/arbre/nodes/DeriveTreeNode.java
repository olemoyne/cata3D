package appli.arbre.nodes;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import view.scene.PrintableScene;
import view.scene.PrintedArea;
import view.scene.PrintedForce;
import view.scene.PrintedMap;
import view.scene.PrintedPoint;
import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.Area;
import model.Cata;
import model.Position;
import model.math.MapDeVecteurs;

public class DeriveTreeNode extends DesignTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1673478056209937230L;
	private Cata bateau;

	public DeriveTreeNode(DefaultMutableTreeNode up, Cata boat, String name) {
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
			ret.add(new TreeNodeProperty ("Surface de dérive", bateau.mer.surfaceTotale, false, ObjectUpdater.DECIMAL) );
			ret.add(new TreeNodeProperty ("Centre de dérive", bateau.mer.centreAntiDerive, false, ObjectUpdater.VECTEUR) );
			ret.add(new TreeNodeProperty ("Centre de gravité", bateau.mer.poidsTotal.position, false, ObjectUpdater.VECTEUR) );
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
				ret.add(new PrintedMap(a, "Carène", true, Color.blue, new Position()));
		}
		
		// Affiche les zones anti-dérive
		if (bateau.mer.surfaceAntiDerive != null) {
			for (Area a : bateau.mer.surfaceAntiDerive) 
				ret.add(new PrintedArea(a, "Dérive", true, false, Color.RED, new Position()));
		}
		
		// affiche le centre de gravité
		if (bateau.mer.poidsTotal != null) {
			ret.add(new PrintedForce(bateau.mer.poidsTotal, Color.GREEN));
		}

		// Affiche le centre anti-dérive
		if (bateau.mer.centreAntiDerive != null) {
			ret.add(new PrintedPoint(bateau.mer.centreAntiDerive, "Centre antidérive", Color.yellow));
		}
		
		return ret;
	}



	
}
