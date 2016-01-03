package model.composants;

import java.io.Serializable;
import java.util.ArrayList;

import view.scene.PrintableObject;
import model.Cata;
import model.Poids;
import model.math.MapDeVecteurs;
import model.math.Vecteur;


/**
 * Modelisation simple d'un composant de catamaran 
 *   * Type de composant : 
 *   	* patch --> définition de X points pour générer une forme
 *      * recopie --> recopie un autre compsant avec une éventuelle symétrie.
 *      * tube --> poutre ou tube métallique 
 *   
 * @author olemoyne
 *
 */
public class Composant implements Serializable{
   /**
	 * 
	 */
	private static final long serialVersionUID = -5490545959071227392L;

	public final static int PATCH_PLEIN = 1;
	public final static int PATCH_VIDE = 2;
	public final static int RECOPIE = 3;
	public final static int TUBE = 4;
	
	/** Nom du composant **/
	public String nom;
	
	/** Description du composant **/
	public String description;
	
	/** Modèles matematiques d'affichage et de calcul **/
	public MapDeVecteurs mapAffichage;

	/** Poids **/
	public ArrayList<Poids> poids;

	/** Position dans le bateau **/
	public Vecteur position;

	/** Centre de gravité du composant **/
	public Poids gravite;
	
	/** Lien vers le bateau du composant **/
	public Cata boat;
	
	/**
	 * Constructeur --> intialisation des listes
	 */
	public Composant (Cata bato) {
		nom = "Nom";
		boat = bato;
		description = "Description";
		poids = new ArrayList<Poids> ();
		position = new Vecteur();
	}
	
	/**
	 * Génère une description
	 */
    public String toString() {
        StringBuilder sb = new StringBuilder("Composant = ");
        sb.append(nom);
        sb.append(" ");
        sb.append(description);
        return sb.toString();
    }	
    
    /**
     * Recalcule les éléments essentiels de la pièce :
     *    Map affichage et calcul
     *    centre de gravité et poids
     *     
     */
    public void recalcule () {
    	if (boat != null) boat.recalcule();
    }

	public int getType() {
		return -1;
	}

	public ArrayList<PrintableObject> getSceneObjects() {
		return new ArrayList<PrintableObject>();
	}
    
}
