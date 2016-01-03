package appli.arbre.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import model.Cata;
import model.composants.Composant;
import model.composants.PatchPlein;
import model.composants.PatchVide;
import model.composants.Recopie;

/**
 * Dans cette classe se concentre tout le paramétrage 
 * 
 * @author lemoyne
 *
 */
public class CataTreeNode {
	
	/** Retourne une liste de noeuds correspondant au bateau **/
	public static DefaultMutableTreeNode getNodes(Cata boat, DefaultMutableTreeNode top) {
		BoatTreeNode  sub = new BoatTreeNode( top, boat, "Design");
		    
		/**
		 *  Ajoute les compostants de construction
		 */
	    for (Composant cmp : boat.composants) {
	    	getNodeForComponent(cmp, sub);
	    }
	    
	    return top;
	}

	
	public static void getNodeForComponent(Composant cmp, DesignTreeNode sub) {
	    	// Selon le type de composant
	    	if (cmp.getType() == Composant.PATCH_PLEIN) {
		    	new PatchPleinTreeNode(sub, cmp.nom, (PatchPlein)cmp);
	    	}
	    	if (cmp.getType() == Composant.PATCH_VIDE) {
		    	new PatchVideTreeNode(sub, cmp.nom, (PatchVide)cmp);
	    	}
	    	if (cmp.getType() == Composant.RECOPIE) {
		    	new RecopieTreeNode(sub, cmp.nom, (Recopie)cmp);
	    	}
	    	if (cmp.getType() == Composant.TUBE) {
		    	new TubeTreeNode(sub, cmp.nom, (Recopie)cmp);
	    	}
	}
}
