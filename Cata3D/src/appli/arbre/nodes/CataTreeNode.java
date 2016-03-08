package appli.arbre.nodes;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import model.Cata;
import model.composants.Composant;
import model.composants.PatchPlein;
import model.composants.PatchVide;
import model.composants.Recopie;
import model.composants.Tube;

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
		new StudyTreeNode( top, boat, "Etude hydrostatique");
		    
		/**
		 *  Ajoute les compostants de construction
		 */
	    for (Composant cmp : boat.composants) {
	    	getNodeForComponent(cmp, sub);
	    }
	    
	    return top;
	}

	
	public static DesignTreeNode getNodeForComponent(Composant cmp, DesignTreeNode sub) {
	    	// Selon le type de composant
	    	if (cmp.getType() == Composant.PATCH_PLEIN) {
		    	return new PatchPleinTreeNode(sub, cmp.nom, (PatchPlein)cmp);
	    	}
	    	if (cmp.getType() == Composant.PATCH_VIDE) {
	    		return new PatchVideTreeNode(sub, cmp.nom, (PatchVide)cmp);
	    	}
	    	if (cmp.getType() == Composant.RECOPIE) {
	    		return new RecopieTreeNode(sub, cmp.nom, (Recopie)cmp);
	    	}
	    	if (cmp.getType() == Composant.TUBE) {
	    		return new TubeTreeNode(sub, cmp.nom, (Tube)cmp);
	    	}
	    	return null;
	}


	public static DesignTreeNode getDesignNode(DefaultMutableTreeNode top) {
		for (Enumeration<?> e = top.children(); e.hasMoreElements();) {
			DesignTreeNode dtn = (DesignTreeNode)e.nextElement();
			if (dtn.toString().equals("Design")) return (DesignTreeNode)dtn;
		}
		return null;
	}
}
