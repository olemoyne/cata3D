package model.composants;

import java.util.ArrayList;

import view.scene.PrintableObject;
import model.Cata;
import model.Poids;
import model.Position;
import model.math.MapDeVecteurs;

/**
 * Composant généré à partir d'un patch 
 * 
 * @author lemoyne
 *
 */
public class Recopie extends Composant {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -487937364652537453L;

	public Composant autre;
	
	public Recopie (Cata bato) {
		super(bato); // Creation des données liées au patch	
		this.autre = null;
	}


    /**
     * Recalcule les éléments essentiels de la pièce :
     *    Map affichage et calcul
     *    centre de gravité et poids
     *     
     */
    public void recalcule () {
    	if (autre == null) {
    		this.gravite = new Poids();
    		mapAffichage = new MapDeVecteurs();
    		return;
    	}
    	if (autre.mapAffichage == null) { autre.recalcule();}
    	
    	if (situation == null) situation = new Position();
    	
    	mapAffichage = autre.mapAffichage;
		this.gravite = new Poids(autre.gravite);
		super.recalcule();
    }


    public int getType() {
		return Composant.RECOPIE;
	}

	/** 
	 * G�n�re la liste des objets correspondant au composant recopi� 
	 * **/ 
	public ArrayList<PrintableObject> getSceneObjects(Position trans) {
		if (autre == null) return new ArrayList<PrintableObject>();
		return autre.getSceneObjects(trans);
	}

	
	public boolean isPatch() {
		if (autre == null) 
			return false;
		return autre.isPatch();
	}
	
	public MapDeVecteurs getMapNonReduite() {
		return autre.getMapNonReduite();
	}


	
}
