package appli.values;

import java.util.ArrayList;

import appli.values.properties.HorizontalPatchSize;
import appli.values.properties.PatchPoint;
import appli.values.properties.VerticalPatchSize;
import model.Cata;
import model.math.Vecteur;

/**
 * Permet de gérer la mise à jour des données de patch
 * 
 * @author olemoyne
 *
 */
public class PatchValuesProvider implements CataValuesProvider {

	private Cata bateau;
	
	@Override
	public CataValuesProperties getProperties(Cata boat) {
		
		bateau = boat;
		
		// Construit les propriétés
		return getPatchProperties();
	}

	@Override
	public void saveProperties(Cata boat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean areArrayButtonsNeeded() {
		return false;
	}

	
	
	@Override
	public CataValuesProperties ajouter(CataValuesProperties values, int pos) {
		return null;
	}

	@Override
	public CataValuesProperties supprimer(CataValuesProperties values, int pos) {
		return null;
	}

	/** 
	 * Mise à jour d'une des valeurs du patch
	 * 
	 */
	@Override
	public void updateProperty(CataValuesProperty prop) {
		Class<?> classe= prop.getClass();
		if (classe.isAssignableFrom(HorizontalPatchSize.class)) {
			int newX = (Integer)((HorizontalPatchSize)prop).value;
			// Renumarotation des éléments du patch
			ArrayList<Vecteur> pts = new ArrayList<Vecteur>();
			for (int x = 0; x < bateau.x; x++) {
				for (int y = 0; y < bateau.y; y++) {
					pts.set(x+y*newX, bateau.points.get(bateau.x*y + x));
				}
			}
			for (int x = bateau.x; x < newX; x++) {
				for (int y = 0; y < bateau.y; y++) {
					pts.set(x+y*newX, new Vecteur());
				}
			}
			bateau.x = newX;
			bateau.points = pts;
		}
		if (classe.isAssignableFrom(VerticalPatchSize.class)) {
			int newY = (Integer)((VerticalPatchSize)prop).value;
			// Renumarotation des éléments du patch
			ArrayList<Vecteur> pts = new ArrayList<Vecteur>();
			for (int x = 0; x < bateau.x; x++) {
				for (int y = 0; y < bateau.y; y++) {
					pts.set(x+y*bateau.x, bateau.points.get(bateau.x*y + x));
				}
				for (int y = bateau.y; y < newY; y++) {
					pts.set(x+y*bateau.x, new Vecteur());
				}
			}
			bateau.x = newY;
			bateau.points = pts;
		}
	}

	
	private CataValuesProperties getPatchProperties() {
		CataValuesProperties ret = new CataValuesProperties ();
		
		// Ajoute le nombre de points
		ret.properties.add(new HorizontalPatchSize(bateau.x));
		ret.properties.add(new VerticalPatchSize(bateau.y));
		
		// Ajoute les points à voir
		for (int x = 0; x < bateau.x; x++) {
			for (int y = 0; y < bateau.y; y++) {
				ret.properties.add(new PatchPoint(x, y, bateau.points.get(bateau.x*y + x)));
			}
		}
		return ret;
	}

	

}
