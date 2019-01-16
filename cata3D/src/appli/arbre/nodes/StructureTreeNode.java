package appli.arbre.nodes;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import appli.arbre.DesignTreeRenderer;
import appli.arbre.TreeNodeProperty;
import model.Area;
import model.Gabarit;
import model.Position;
import model.composants.Composant;
import model.composants.PatchVide;
import model.math.Axis;
import model.math.Decimal;
import model.math.Vecteur;
import model.math.transfo.Translation;
import view.scene.PrintableScene;
import view.scene.PrintedArea;

/**
 * Liste des poids soumis 
 * 
 * @author lemoyne
 *
 */
public class StructureTreeNode extends DesignTreeNode {
	 Composant comp;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -871482094598531184L;

	public StructureTreeNode(DefaultMutableTreeNode up, Composant cmp) {
		super(up, "Structure");
	    new GabaritsTreeNode(this, (PatchVide)cmp);
	    new PoutresTreeNode(this, (PatchVide)cmp);
	    new QuilleTreeNode(this, (PatchVide)cmp);
	    // gestion des plans de construction
	    new PlanTreeNode(this);


		comp = cmp;
	}
	
	
	// gestion des icones
	public Icon getImage() {
		return DesignTreeRenderer.structureImg;
	}


	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
	}

	/** 
	 * Affiche la structure interne du bateau 
	 * 
	 */
	public PrintableScene getScene () {
		PatchTreeNode father = (PatchTreeNode)this.parent;
		PatchVide cmp = (PatchVide)father.composant;
		PrintableScene ret = super.getScene(); 

		// Affiche la quille 
		if (cmp.structure.quille != null) {		
			Translation trans = new Translation(new Vecteur().setDec(Axis.XAxis, cmp.structure.quille.epaisseur.divide(Decimal.DEUX)), null);
			Area a = cmp.structure.quille.getAffichage().transform(trans);
			ret.add(new PrintedArea (a, "Quille", false, false, Color.darkGray, new Position()));
			trans = new Translation(new Vecteur().setDec(Axis.XAxis, cmp.structure.quille.epaisseur.divide(Decimal.DEUX).negate()), null);
			a = cmp.structure.quille.getAffichage().transform(trans);
			ret.add(new PrintedArea (a, "Quille", false, false, Color.darkGray, new Position()));
		}
		
		// Affiche les gabarits
		if (cmp.structure.gabarits != null) {		
			for (Gabarit g : cmp.structure.gabarits) {
	//			Translation trans = new Translation(new Vecteur().setDec(Axis.ZAxis, g.position), null);
				Area a = g.getContours(); //.transform(trans);
				ret.add(new PrintedArea (a, "Quille", false, false, Color.green, new Position()));
				Translation trans = new Translation(new Vecteur().setDec(Axis.ZAxis, g.epaisseur), null);
				a = g.getContours().transform(trans);
				ret.add(new PrintedArea (a, "Quille", false, false, Color.green, new Position()));
			}
		}
		

		return ret;
	}
	
	public boolean requireButtons() {
		return false;
	}

}
