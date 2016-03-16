package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.math.Axis;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Plan3D;
import model.math.Vecteur;
import model.math.transfo.Rotation;
import model.math.transfo.Transformation;
import model.math.transfo.Translation;

/***************************
 *  Permet de d�finir le niveau de la mer par rapport � la coque
 *  
 * @author olemoyne
 *
 ***********************************************************/


public class Flottaison implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8873722559801931229L;

	public Decimal pilonnement;
	
	public Decimal tangage;
	
	public Decimal gite;
	
	/**
	 * Elements calcul�s pour affichage
	 */
	// Autant de carènes que de volumes immergés
	public ArrayList<MapDeVecteurs> carenes;
	
	public Poids poidsTotal;
	public Poids pousseeArchimede;
	
	public String flottabilite;

	// Elements calculés à partir de la surface de carène
	public Vecteur centreAntiDerive;
	public ArrayList<Area> surfaceAntiDerive;
	public Decimal surfaceTotale;

	public Flottaison () {
		carenes = new ArrayList<MapDeVecteurs>();
		pilonnement = new Decimal(0);
		tangage = new Decimal(0); // Angle de tangage (rotation selon Y )
		gite = new Decimal(0); // Angle de gite (rotation selon Z)
	}

	/**
	 * Get Transformation 
	 */
	public Transformation getTransformation () {
		Rotation rotgite = new Rotation(Axis.ZAxis, gite, null);
		Rotation rottang = new Rotation(Axis.XAxis, tangage, rotgite);
		Translation transPil = new Translation (new Vecteur (Decimal.ZERO, pilonnement.negate(), Decimal.ZERO), rottang);
		
		return transPil;
	}
	
	/**
	 * D�termine le plan correspondant � la surface de la mer
	 * 
	 * @return
	 */
	public Plan3D getPlan() {
		// plan Y= 0 --> transform�
		Transformation trs = this.getTransformation();
		Plan3D pl = new Plan3D(
				trs.getPoint(new Vecteur(1, 0, 0)), 
				trs.getPoint(new Vecteur(0, 0, 0)),
				trs.getPoint(new Vecteur(0, 0, 1))  );
		
		return pl;
	}
}
