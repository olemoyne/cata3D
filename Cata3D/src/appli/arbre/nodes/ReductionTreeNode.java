package appli.arbre.nodes;


import java.awt.Color;
import java.util.ArrayList;

import appli.arbre.TreeNodeProperty;
import view.scene.PrintableScene;
import view.scene.PrintedMap;
import model.Position;
import model.composants.PatchComposant;
import model.math.Decimal;
import model.math.InvalidGeomAction;
import model.math.solide.MapConversion;
import model.math.solide.STLFile;
import model.math.solide.Solide;

public class ReductionTreeNode extends ComposantTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7587228627542488036L;


	public ReductionTreeNode(DesignTreeNode up, PatchComposant cmp) {
		super(up, "Réduction", cmp);
	}		
	
	
	public ArrayList<TreeNodeProperty> getProperties() {
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		return ret;
	}
	
	/** 
	 * Permet d'afficher la scene correspondant Ã  la vue dÃ©finie
	 * 
	 */
	public PrintableScene getScene () {
		PrintableScene ret = super.getScene(); 

		PatchComposant pc = (PatchComposant) composant;
		// Affiche la coque en gris
		ret.add(new PrintedMap (composant.mapAffichage, "Coque réduite", false, Color.red, new Position()));					
		
		ret.add(new PrintedMap (pc.mapNonReduite, "Coque non réduite", true, Color.green, new Position()));					
		

		return ret;
	}
	

	public String[] getActionList() {
		String[] strs =  {"Fichier STL"};
		return strs;
	}


	public void generateSTLFile(String filename) throws InvalidGeomAction {
		PatchComposant pv = (PatchComposant)composant;
		// Gï¿½nï¿½ration d'un solide
		Solide sol = MapConversion.getSolide(pv.mapAffichage, pv.reflexive);
		STLFile f = new STLFile(1);
		f.createBinaryFile(filename, sol, pv.nom, Decimal.CENT);
	}


}
