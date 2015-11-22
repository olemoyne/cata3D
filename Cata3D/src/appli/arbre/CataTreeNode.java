package appli.arbre;

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
	public static DesignTreeNode getNodes(Cata boat) {
		DesignTreeNode top = new DesignTreeNode(null, "Définition d'un bateau");
		
		DesignTreeNode  sub = new DesignTreeNode(top, "Design");
	    
		/**
		 *  Ajoute les compostants de construction
		 */
	    for (Composant cmp : boat.composants) {
	    	// Selon le type de composant
	    	if (cmp.getType() == Composant.PATCH_PLEIN) {
		    	DesignTreeNode node= new PatchPleinTreeNode(sub, cmp.nom, (PatchPlein)cmp);
			    //Patches de design de la coque
			    new DesignTreeNode(node, "Patch");
			    new DesignTreeNode(node, "Poids");
			    new DesignTreeNode(node, "Gabarits");
			    new DesignTreeNode(node, "Poutres");
			    new DesignTreeNode(node, "plans");
	    	}
	    	if (cmp.getType() == Composant.PATCH_VIDE) {
		    	DesignTreeNode node= new PatchVideTreeNode(sub, cmp.nom, (PatchVide)cmp);
			    //Patches de design de la coque
			    new DesignTreeNode(node, "Patch");
			    new DesignTreeNode(node, "Poids");
			    new DesignTreeNode(node, "Gabarits");
			    new DesignTreeNode(node, "Poutres");
			    new DesignTreeNode(node, "plans");
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
