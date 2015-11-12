package appli.arbre;

import java.awt.event.ComponentEvent;

import javax.swing.tree.DefaultMutableTreeNode;

import model.Cata;
import model.composants.Composant;

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
	    
	    for (Composant cmp : boat.composants) {
	    	DesignTreeNode node= new DesignTreeNode(sub, cmp.nom);
	    	// Selon le type de composant
	    	if (cmp.getType() == Composant.PATCH_PLEIN) {
			    //Patches de design de la coque
			    new DesignTreeNode(node, "Patch");
			    new DesignTreeNode(node, "Poids");
			    new DesignTreeNode(node, "Gabarits");
			    new DesignTreeNode(node, "Poutres");
			    new DesignTreeNode(node, "plans");
	    	}
	    	if (cmp.getType() == Composant.PATCH_PLEIN) {
			    //Patches de design de la coque
			    new DesignTreeNode(node, "Patch");
			    new DesignTreeNode(node, "Poids");
			    new DesignTreeNode(node, "Gabarits");
			    new DesignTreeNode(node, "Poutres");
			    new DesignTreeNode(node, "plans");
	    	}
	    }
	    

	    //optimisation du design
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

	}

}
