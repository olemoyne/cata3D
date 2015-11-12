package model.composants;

import model.math.transfo.Rotation;
import model.math.transfo.Translation;

/**
 * Composant généré à partir d'un patch 
 * 
 * @author lemoyne
 *
 */
public class recopie extends Composant {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -487937364652537453L;

	public Composant autre;
	
	/** décalage de la forme **/
	public Rotation pivot;

	/** décalage de la forme **/
	public Translation decalage;


	public recopie () {
		super(); // Creation des données liées au patch	
	}
	
    /**
     * Recalcule les éléments essentiels de la pièce :
     *    Map affichage et calcul
     *    centre de gravité et poids
     *     
     */
    public void recalcule () {
    	mapAffichage = autre.mapAffichage.transforme(pivot).transforme(decalage);
		this.gravite = autre.gravite;
		gravite.position = decalage.getPoint(pivot.transforme(gravite.position));
    }

}