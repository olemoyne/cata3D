package appli.values.properties;

import appli.values.CataValuesException;
import appli.values.CataValuesProperty;
import appli.values.CataValuesProvider;
import model.Cata;
/**
 * Permet de gérer la mise à jour des données de patch
 * 
 * @author olemoyne
 *
 */
public class PatchValuesProvider extends CataValuesProvider {

	/**
	 * Intitialisation avec un beateau vide
	 */
	public PatchValuesProvider () {
		super();
		liste.add(new HorizontalPatchSize());
		liste.add(new VerticalPatchSize());

		for (int pos = 0; pos < 16; pos ++) {
			liste.add(new PatchPoint(pos / 4, pos % 4));
		}
	}

	/** 
	 * Mise à jour d'une des valeurs du patch
	 * @throws CataValuesException 
	 * 
	 */
	@Override
	public void setProperty(Cata data, int row, Object value) throws CataValuesException {
		// Identifie le type de données en fonction de la position
		CataValuesProperty prop = liste.get(row);
		prop.setProperty(data, value.toString());
/**			int newX = (Integer)((HorizontalPatchSize)prop).value;
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
	**/
	}

	
	public void setPatchProperties(Cata bateau) {
		// 	Fait un RAZsur les donnes
		liste.clear();
		liste.add(new HorizontalPatchSize());
		liste.add(new VerticalPatchSize());

		// ajuste le nombre de points associés
		for (int x = 0; x < bateau.patch.x; x++) {
			for (int y = 0; y < bateau.patch.y; y++) {
				liste.add(new PatchPoint(x, y));
			}
		}
	}

	@Override
	public String getProperty(Cata data, int row) {
		CataValuesProperty prop = liste.get(row);
		return prop.getPropertyValue(data);
	}

	@Override
	public int getPropertyCount(Cata data) {
		return data.patch.x*data.patch.y+2;
	}

	public String getPropertyName(int row) {
		CataValuesProperty prop = liste.get(row);
		return prop.getPropertyName();
	}

	

}
