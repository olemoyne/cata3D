package appli.arbre.nodes;

import java.awt.Color;

import javax.swing.tree.DefaultMutableTreeNode;

import model.Position;
import model.composants.Collision;
import model.composants.Composant;
import model.composants.PatchComposant;
import view.scene.PrintableScene;
import view.scene.PrintedMap;

/**
 * Liste des poids soumis 
 * 
 * @author lemoyne
 *
 */
public class CollisionsTreeNode extends DesignTreeNode {

//	 ArrayList<Poids> poids;
	 Composant comp;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -871482094598531184L;

	public CollisionsTreeNode(DefaultMutableTreeNode up, Composant cmp) {
		super(up, "Collisions");
		
		comp = cmp;
	}
	
	/** 
	 * Permet d'afficher la scene correspondant à la vue définie
	 * 
	 */
	public PrintableScene getScene () {
		PatchTreeNode father = (PatchTreeNode)this.parent;
		PatchComposant cmp = (PatchComposant)father.composant;
		PrintableScene ret = super.getScene(); 

		// Affiche la coque en gris
		ret.add(new PrintedMap (cmp.mapAffichage, "Coque", true, Color.darkGray, new Position()));					

		// Affiche les collisions en rouge
		for (Collision col : cmp.collisions) {
			ret.add(new PrintedMap (col.collision, col.autre.nom, false, Color.red, new Position()));					
		}
		return ret;
	}

}
