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

		MapDeVecteurs ret = new MapDeVecteurs(xSize, ySize+ map.ySize() );

		for (int pos = 0; pos < this.xSize; pos ++) {
			for (int xpos = 0; xpos < this.ySize; xpos ++) {
				ret.setPoint(pos, xpos, getPoint(pos, xpos));
			}
			for (int xpos = 0; xpos < map.ySize(); xpos ++) {
				ret.setPoint(pos, ySize + xpos,  map.getPoint(pos, map.ySize -1 - xpos));
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
		
		//Parcours tous les points verticaux. Attention les points entre et sortent de l'eau
		for (int xpos = 0; xpos < this.xSize; xpos ++) {
			// calcul l'intersection avec le plan
			Vecteur inter = null;
			// Recherche la première intersection
			for (int ypos = 1; ypos < this.ySize; ypos ++) {
				Vecteur v = surface.intersection(this.getPoint(xpos, ypos-1), this.getPoint(xpos, ypos));
				if ((v != null)&&(inter == null)) inter = v;
			}

			boolean auDessus = true;
			if (surface.donneCote(getPoint(xpos, 0)) > 0) auDessus = false; 
			
			if (auDessus)
				ret.setPoint(xpos, 0, inter);
			else 
				ret.setPoint(xpos, 0, this.getPoint(xpos, 0));
				
			for (int ypos = 1; ypos < this.ySize; ypos ++) {
				Vecteur v = surface.intersection(this.getPoint(xpos, ypos-1), this.getPoint(xpos, ypos));
				if (v != null) {
					auDessus = !auDessus;
					inter = v;
				}
				if (auDessus)
					ret.setPoint(xpos, ypos, inter);
				else 
					ret.setPoint(xpos, ypos, this.getPoint(xpos, ypos));
			}									
		}	
		
		return ret;
	}

	/** 
	 * Retourne la map 
	 * **/
	public MapDeVecteurs reverse() {
		MapDeVecteurs ret = new MapDeVecteurs (this.ySize, this.xSize);
		for (int xpos = 0; xpos < this.xSize; xpos ++) {
			for (int ypos = 0; ypos < this.ySize; ypos ++) {
				ret.setPoint(ypos, xpos, this.getPoint(xpos, ypos));
			}
		}
		return ret;
	}
}
