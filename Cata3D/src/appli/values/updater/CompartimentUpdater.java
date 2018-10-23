package appli.values.updater;

import model.Compartiment;
import model.math.Decimal;
import model.math.Vecteur;

/** 
 * Permet de mettre � jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class CompartimentUpdater extends ObjectUpdater {

	ColorField couleur;
	StringField nom;

	DecimalField pX, pY, pZ;
	DecimalField tX, tY, tZ;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301382L;

	public CompartimentUpdater () {
		super();
		
		addFields();
	}
	
	// Ajoute les champs de saisie
	private void addFields() {
		couleur = new ColorField("Couleur");
		add(couleur);
		
		nom = new StringField("Nom");
		add(nom);

		pX = new DecimalField("position X : ", "m");
		add(pX);
		pY = new DecimalField("position Y : ", "m");
		add(pY);
		pZ = new DecimalField("position Z : ", "m");
		add(pZ);

		tX = new DecimalField("taille X : ", "m");
		add(tX);
		tY = new DecimalField("taille Y : ", "m");
		add(tY);
		tZ = new DecimalField("taille Z : ", "m");
		add(tZ);

	}

	public void setProperty (Object val) {
		Compartiment v = (Compartiment)val;
		
		nom.setField(v.getNom());
		Vecteur p = v.getPosition();
		pX.setField(p.getDecX());
		pY.setField(p.getDecY());
		pZ.setField(p.getDecZ());

		Vecteur t = v.getTaille();
		tX.setField(t.getDecX());
		tY.setField(t.getDecY());
		tZ.setField(t.getDecZ());

		couleur.setField(v.getCouleur());

	}
	
	public Object getData() {

		Compartiment comp = new Compartiment();
		        comp.setData((String)nom.getValue(), 
				new Vecteur ((Decimal)pX.getValue(), (Decimal)pY.getValue(), (Decimal)pZ.getValue()),
				new Vecteur ((Decimal)tX.getValue(), (Decimal)tY.getValue(), (Decimal)tZ.getValue()),
				couleur.getValue()
				);
		
		return comp;
	}
	
}
