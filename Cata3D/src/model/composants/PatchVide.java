package model.composants;

import java.util.ArrayList;

import model.Cata;
import model.Poids;
import model.Structure;
import model.calcul.CalculCoque;
import model.calcul.CalculVolume;
import model.math.Decimal;
import model.patch.Patch;

/**
 * Composant généré à partir d'un patch 
 * 
 * @author lemoyne
 *
 */
public class PatchVide extends PatchComposant {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3254205662119556033L;

	/** Epaisseur du bardage nécessaire à la génération de la forme **/
	public Decimal epaisseurDeBardage;
	
	/** Densité du matériau utilisé pour le bardage **/
	public Decimal densiteBardage;

	/** Structure du bateau **/
	public Structure structure;


	public PatchVide (Cata bato) {
		super(bato); // Creation des données liées au patch
		structure = new Structure(); 
	}
	
	/**
	 * Si les donne�s de patch sont modifi�es 
	 * 
	 * @param y 
	 * @param x 
	 */
	public void setPatch(int x, int y) {
		patch.recalcule(x, y);
		recalcule();
	}

	/**
	 * Reclacul des éléments hors MAP d'affichage
	 */
	public void calculeElements (){
     	if (epaisseurDeBardage == null) epaisseurDeBardage = new Decimal(0.001d);
     	if (densiteBardage == null) densiteBardage = new Decimal(0.6d);
		Poids pdsCoque = CalculCoque.calculePoidsCoque(mapAffichage, epaisseurDeBardage.multiply(densiteBardage));
		ArrayList<Poids> lst = new ArrayList<Poids>();
		lst.add(pdsCoque); // Ajoute le poids du bardage
		lst.addAll(this.poids); // Ajoute les poids d�finis
		lst.addAll(this.structure.getAllPoids(this, PatchComposant.DENSITE_BOIS));
		this.gravite= CalculVolume.getCentreGravite("Poids total", lst);
	}

	
	
	public int getType() {
		return Composant.PATCH_VIDE;
	}

	/**
	 * Creation d'un patch par defaut
	 * 
	 * @return
	 */
	public static Composant getDefaultPatch(Cata bato) {
		PatchVide ret = new PatchVide(bato);
		ret.nom = "Flotteur";
		ret.patch= Patch.getPatch();
		ret.epaisseurDeBardage = new Decimal(0.01d);
		ret.densiteBardage = new Decimal(0.6d);
		ret.recalcule();
		return ret;
	}


}
