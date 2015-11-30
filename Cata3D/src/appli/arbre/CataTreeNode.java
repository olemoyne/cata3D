package appli.arbre;

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
		DesignTreeNode  sub = new DesignTreeNode( top, "Design");
		    
		/**
		 *  Ajoute les compostants de construction
		 */
	    for (Composant cmp : boat.composants) {
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
	    
	    return top;

/**	    //optimisation du design
	    optimisation= new DefaultMutableTreeNode("Optimisation");
	    top.add(optimisation);
	    optimisation.add(new DefaultMutableTreeNode("Flottaison"));
	    optimisation.add(new DefaultMutableTreeNode("Fluidité"));
	    DefaultMutableTreeNode derive = new DefaultMutableTreeNode("Dérive");
	    optimisation.add(derive);
	    derive.add(new DefaultMutableTreeNode("Forme de dérive"));
	    optimisation.add(new DefaultMutableTreeNode("Habitabilité"));

	    //construction de la coque
	    construction= new DefaultMutableTreeNode("Construction");
	    top.add(construction);
	    construction.add(new DefaultMutableTreeNode("Gabarits"));
	    construction.add(new DefaultMutableTreeNode("Poutres"));
	    construction.add(new DefaultMutableTreeNode("Plans"));
**/
	}

}
