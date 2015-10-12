package model.math;

import java.io.Serializable;

import model.math.transfo.Transformation;

public class MapDeVecteurs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2991526300922823875L;

	int xSize;
	
	int ySize;
	
	Vecteur[][] data;
	
	public MapDeVecteurs () {
		
	}
		
	public MapDeVecteurs (int x, int y) {
		data = new Vecteur[x][y];
		xSize = x;
		ySize = y;
	}
	
	public MapDeVecteurs(MapDeVecteurs map) {
		this.xSize = map.xSize;
		this.ySize = map.ySize;
		
		data = new Vecteur[map.xSize][map.ySize];

		for (int x = 0; x < xSize; x ++) {
			for (int y = 0; y < ySize; y ++) {
				data[x][y] = new Vecteur(map.getPoint(x, y));
			}
		}
	}


	public Vecteur getPoint(int i, int y) {
		return data[i][y];
	}

	public int xSize() {
		return xSize;
	}

	public int ySize() {
		return ySize;
	}

	public void setPoint(int i, int y, Vecteur v) {
		if (v == null) return;
		data[i][y] = new Vecteur(v);
	}

	// dans ce cas, un nouveau tableau de données est créé
	//  les points sont ajoutés à chaque colonne,à la suite
	public MapDeVecteurs addMap(MapDeVecteurs map) {
		
		if (map.xSize() != this.xSize()) return null;

		MapDeVecteurs ret = new MapDeVecteurs(xSize+ map.xSize(), ySize );

		for (int pos = 0; pos < this.ySize; pos ++) {
			for (int xpos = 0; xpos < this.xSize; xpos ++) {
				ret.setPoint(xpos, pos, getPoint(xpos, pos));
			}
			for (int xpos = 0; xpos < map.xSize(); xpos ++) {
				ret.setPoint(xSize + xpos, pos, map.getPoint(map.xSize -1 - xpos, pos));
			}
		}
		return ret;
	}


	public Vecteur[] getTranche(int x) {
		return this.data[x];
	}

	public MapDeVecteurs transforme(Transformation ref) {
		MapDeVecteurs ret = new MapDeVecteurs (this.xSize, this.ySize);
		for (int pos = 0; pos < this.xSize; pos ++) {
			for (int ypos = 0; ypos < this.ySize; ypos ++) {
				ret.setPoint(pos, ypos, ref.transforme(getPoint(pos, ypos)));
			}
		}	
		return ret;
	}

	/** Calcule le résultat entre une map et un plan
	 * 
	 * @param surface
	 * @return
	 */
	public MapDeVecteurs truncate(Plan3D surface) {
		// Recopie les éléments de la map
		MapDeVecteurs ret = new MapDeVecteurs(this);
		
		//Parcours tous les points verticaux
		for (int ypos = 0; ypos < this.ySize; ypos ++) {
			// calcul l'intersection avec le plan
			Vecteur inter = null;
			for (int pos = 1; pos < this.xSize; pos ++) {
				Vecteur v = surface.intersection(this.getPoint(pos-1, ypos), this.getPoint(pos, ypos));
				if (v != null) inter = v;
			}
			if (inter == null) inter = new Vecteur (0, 0, 0);
			for (int pos = 0; pos < this.xSize; pos ++) {
				if (surface.donneCote(getPoint(pos, ypos)) < 0) 
					ret.setPoint(pos, ypos, inter);
				else 
					ret.setPoint(pos, ypos, this.getPoint(pos, ypos));
			}									
		}	
		
		return ret;
	}
}
