package appli.arbre.nodes;


import java.awt.Color;
import java.util.ArrayList;

import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import view.scene.PrintableScene;
import view.scene.PrintedForce;
import view.scene.PrintedMap;
import model.composants.PatchComposant;
import model.math.Decimal;
import model.math.InvalidGeomAction;
import model.math.solide.MapConversion;
import model.math.solide.STLFile;
import model.math.solide.Solide;

public class PatchTreeNode extends ComposantTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7587228627542488036L;


	public PatchTreeNode(DesignTreeNode up, String name, PatchComposant cmp) {
		super(up, name, cmp);
		// Ajoute les TreeNode correspondant au composant
	    new PatchPropertyTreeNode(this, cmp.patch);
	    //Patches de design de la coque
	    new PoidsTreeNode(this, cmp);
	    // gestion des plans de construction
	    new PlanTreeNode(this);
	}		
	
	
	public ArrayList<TreeNodeProperty> getProperties() {
		PatchComposant p = (PatchComposant)composant;
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		ret.add(new TreeNodeProperty ("PrÃ©cision", p.precision, true, ObjectUpdater.INTEGER) );
		ret.add(new TreeNodeProperty ("Symétrie", p.reflexive, true, ObjectUpdater.BOOLEAN) );
		return ret;
	}
	
	
	
	public void updateValue (String fld, Object value) {
		super.updateValue(fld, value);

		PatchComposant pv = (PatchComposant)composant;

		
		if (fld.equals("PrÃ©cision")) {
			Decimal dec = (Decimal)value;
			pv.precision = (int)Math.round(dec.doubleValue());
		}
		// Gestion de la symétrie
		if (fld.equals("Symétrie")) {
			Boolean dec = (Boolean)value;
			pv.reflexive = dec; 
		}
	}


	
	/** 
	 * Permet d'afficher la scene correspondant Ã  la vue dÃ©finie
	 * 
	 */
	public PrintableScene getScene () {
		PrintableScene ret = super.getScene(); 

		// Affiche la coque en gris
		ret.add(new PrintedMap (composant.mapAffichage, "Coque", false, Color.darkGray));					
		
		ret.add(new PrintedForce(this.composant.gravite, Color.RED));

		return ret;
	}
	

	public String[] getActionList() {
		String[] strs =  {"Fichier STL"};
		return strs;
	}


	public void generateSTLFile(String filename) throws InvalidGeomAction {
		PatchComposant pv = (PatchComposant)composant;
		// Génération d'un solide
		Solide sol = MapConversion.getSolide(pv.mapAffichage, pv.reflexive);
		STLFile f = new STLFile(1);
		f.createBinaryFile(filename, sol, pv.nom, Decimal.CENT);
	}


}
