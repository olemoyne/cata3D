package model;

import java.io.Serializable;

import model.math.Decimal;
import model.math.Vecteur;
import model.math.transfo.Translation;

/** 
 * Poids appliqué à la coque
 * 
 * @author olemoyne
 *
 */
public class Poids implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2766347239318331795L;

	public String nom; 
	
	public Vecteur position;
	
	public Decimal force; // Exprimée en KG
	
	public Poids () { 
		nom = "Poid à définir";
		position = new Vecteur ();
		force = Decimal.ZERO;
	}

	public Poids (String n, Vecteur p, Decimal f) { 
		nom = n;
		position = p;
		force = f;
	}

	
	/** 
	 * Version String du poids
	 **/
	public String toString () {
		StringBuilder sb = new StringBuilder(nom);
		sb.append(" : ");
		sb.append(force.toString());
		sb.append(" - ");
		sb.append(position.toString());
		
		return sb.toString();
	}

	/**
	 * Met à jour les valeurs du poids en fonction des données
	 * 
	 * @param val
	 */
	public void setValue(String val) {
		// récupère le nom
		int pos = val.indexOf(" : ");
		if (pos == -1) throw new NumberFormatException("Poids illisible name not found");
		nom = val.substring(0, pos);
		String reste = val.substring(pos+3);
		
		// Récupère le vecteur
		pos = reste.indexOf(" - ");
		if (pos == -1) throw new NumberFormatException("Poids illisible position not found");
		position = new Vecteur(reste.substring(0, pos));
		reste = reste.substring(pos+3);
		
		// Récupère la force
		force = Decimal.parse(reste);
	}

	public Poids transforme(Translation trsl) {
		Poids ret = new Poids (this.nom, trsl.getPoint(position), force);
		return ret;
	}
	
	
	
}
