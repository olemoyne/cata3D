package model.composants;

import model.Poids;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Vecteur;

/**
 * Composant généré à partir d'un patch 
 * 
 * @author lemoyne
 *
 */
public class Tube extends Composant {
	
	public static final int PRECISION = 10;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3254205662119556033L;

	/** Dimensions **/
	public Decimal diametre;
	
	/** Epaisseur du tube  */ 
	public Decimal epaisseur;
	
	/** Logueur du tube **/
	public Decimal longueur;
	
	/** Direction du tube **/
	public Vecteur direction;
	
	/** Densité du tube **/
	public Decimal densite;
	
	public Tube () {
		super(); // Creation des données liées au patch
	}
	
    /**
     *  Recalcule les éléments essentiels de la pièce :
     *    Map affichage et calcul
     *    centre de gravité et poids
     *     
     */
    public void recalcule () {
    	mapAffichage = createMap();
		Decimal pds = new Decimal(Math.PI).multiply(diametre.square().minus(diametre.minus(Decimal.DEUX.multiply(epaisseur)).square())).multiply(densite);
		Vecteur ctr = this.position.add(direction.multiply(longueur.divide(Decimal.DEUX).divide(direction.getNorme())));
		this.gravite = new Poids ("Centre de gravité", ctr, pds);
    }

	private MapDeVecteurs createMap() {
		// TODO Creation d"un saucisson de la longueur du tube
		return null;
	}

}
