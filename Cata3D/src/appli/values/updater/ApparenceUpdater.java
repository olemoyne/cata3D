package appli.values.updater;

import model.Apparence;

/** 
 * Permet de mettre à jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class ApparenceUpdater extends ObjectUpdater {

	ColorField couleur;
	BooleanField transparence;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301382L;

	public ApparenceUpdater () {
		super();
		
		addFields();
	}
	
	// Ajoute les champs de saisie
	private void addFields() {
		couleur = new ColorField("Couleur");
		add(couleur);

		transparence = new BooleanField("Transparence : ");
		add(transparence);
	}

	public void setProperty (Object val) {
		Apparence v = (Apparence)val;
		couleur.setField(v.couleur);
		transparence.setField(v.transparence);
	}
	
	public Object getData() {
		
		Apparence ret = new Apparence();
		ret.couleur = couleur.getValue();
		ret.transparence = transparence.getValue();
		
		return ret;
	}
	
}
