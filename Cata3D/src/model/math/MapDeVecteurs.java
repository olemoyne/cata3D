package model.math;

import java.io.Serializable;
import java.util.ArrayList;

import model.Area;
import model.calcul.CalculSurface;
import model.math.transfo.Transformation;


/**
 *  Maillage de vecteurs qui correspondent ï¿½ une coque
 *   --> X ï¿½talonnage vertical 
 *   --> Y ï¿½talonnage horizontal
 * 
 *   point X Y --> point sur la position verticale X (plus ou moins haut dans le bateau) 
 *      et horizontale en Y (plus ou moins vers l'avant)
 * 
 * @author olemoyne
 *
 */

public class MapDeVecteurs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2991526300922823875L;

	int xSize;
	
	int ySize;
	
	Vecteur[][] data;
	
	Vecteur centre;
	
	public MapDeVecteurs () {
		data = new Vecteur[0][0];
		centre = new Vecteur();
	}
		
	public MapDeVecteurs (int x, int y) {
		data = new Vecteur[x][y];
		xSize = x;
		ySize = y;
		centre = new Vecteur();
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
		centre = new Vecteur(map.centre);

	}

	public Vecteur getCenter() {
		return centre;
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

	// dans ce cas, un nouveau tableau de donnï¿½es est crï¿½ï¿½
	//  les points sont ajoutï¿½s ï¿½ chaque colonne,ï¿½ la suite
	// L'ajout de la MAP se fait via les Y
	public MapDeVecteurs addMap(MapDeVecteurs map) {
		
		if (map.xSize() != this.xSize()) return null;

		MapDeVecteurs ret = new MapDeVecteurs(xSize+ map.xSize, ySize);

		for (int ypos = 0; ypos < this.ySize; ypos ++) {
			for (int xpos = 0; xpos < this.xSize; xpos ++) {
				ret.setPoint(xpos, ypos, getPoint(xpos, ypos));
			}
			for (int xpos = 0; xpos < map.xSize(); xpos ++) {
				ret.setPoint(xSize + xpos, ypos,  map.getPoint(map.xSize -1 - xpos, ypos));
			}
		}
		return ret;
	}


	public Vecteur[] getTranche(int x) {
		return this.data[x];
	}

	public MapDeVecteurs transforme(Transformation ref) {
		if (ref == null) return this;
		MapDeVecteurs ret = new MapDeVecteurs (this.xSize, this.ySize);
		for (int pos = 0; pos < this.xSize; pos ++) {
			for (int ypos = 0; ypos < this.ySize; ypos ++) {
				ret.setPoint(pos, ypos, ref.transforme(getPoint(pos, ypos)));
			}
		}
		ret.centre = ref.transforme(centre);
		return ret;
	}

	/** Calcule le rï¿½sultat entre une map et un plan
	 * 
	 * @param surface
	 * @return
	 */
	public MapDeVecteurs truncate(Plan3D surface) {
		// Recopie les ï¿½lï¿½ments de la map
		// Structure de stockage du résultat
		int nextPos = 0;
		Vecteur[][] map = new Vecteur[xSize][ySize];
		
		
		//Parcours tous les points verticaux. Attention les points entrent et sortent de l'eau
		for (int ypos = 0; ypos < this.ySize; ypos ++) {
			// Regarde si toute la form est dans ou hors de l'eau
			boolean isFull = true;
			for (int xpos = 1; xpos < this.xSize; xpos ++) {
				Vecteur inter = surface.intersection(this.getPoint(xpos-1, ypos), this.getPoint(xpos, ypos));
				if (inter != null) isFull = false;
			}
			if (isFull) {
				if (surface.donneCote(getPoint(0, ypos)) > 0) {// en dessous
					// on intègre tous les pts dans le résultat
					for (int xpos = 0; xpos < this.xSize; xpos ++) {
						map[xpos][nextPos] = this.getPoint(xpos, ypos);
					}			
					nextPos ++;
				}  // Sinon on ne fait rien
			} else {
				// Il y a des points d'intersection
				// calcul l'intersection avec le plan
				Vecteur inter = null;
				// Recherche la premiï¿½re intersection
				for (int xpos = 0; xpos < this.xSize; xpos ++) {
					Vecteur v = getPoint(xpos, ypos);
					if (surface.donneCote(v) > 0) {// en dessous
						map[xpos][nextPos] = v;
						if (inter == null) {
							if (xpos > 0) {
								inter = surface.intersection(this.getPoint(xpos-1, ypos), v);
								// Parcours tous les points déjà parcourus pour setter l'inter
								for (int p = xpos-1; p >= 0; p--) {
									if (map[p][nextPos] == null) 
										map[p][nextPos] = inter;
								}
							}
						}
					} else {
						map[xpos][nextPos] = inter;
					}
				} 
				nextPos ++;
			}
/**			// calcul l'intersection avec le plan
			Vecteur inter = null;
			boolean nullValues = false;
			boolean notNullValues = false;
			// Recherche la premiï¿½re intersection
			for (int xpos = 0; xpos < this.xSize; xpos ++) {
				if (surface.donneCote(getPoint(xpos, ypos)) <= 0) {// en dessous
					ret.setPoint(xpos, ypos, getPoint(xpos, ypos));
					notNullValues = true;
					if (xpos > 0) {
						inter = surface.intersection(this.getPoint(xpos-1, ypos), this.getPoint(xpos, ypos));
					}
				} else {
					if (inter == null) nullValues = true;
					else notNullValues = true;
					ret.setPoint(xpos, ypos, inter);
				}
			} 
			// Toutes les valeurs sont nulles
			if (notNullValues == false) {
				toRemove.add(ypos);
			}
			// Attention, il existe des cas à null
			if (nullValues) {
				Vecteur last = null;
				for (int xpos = this.xSize-1; xpos >= 0 ; xpos --) {
					if (ret.getPoint(xpos, ypos) == null)
						ret.setPoint(xpos, ypos, last);
					else last = ret.getPoint(xpos, ypos);
				}
			}
		}
**/		
		}
		// Aucune position au dessous ou à la surface
		if (nextPos == 0) return null; 
		
		// Conversion du résultat
		MapDeVecteurs mps = new MapDeVecteurs(this.xSize, nextPos);
		for (int ypos = 0; ypos < nextPos; ypos ++) {
			for (int xpos = 0; xpos < this.xSize; xpos ++) {
				mps.setPoint(xpos, ypos, map[xpos][ypos]);
			}
		}
		
		mps.centre = this.centre;
		
		return mps;
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

	/**
	 * Gï¿½nï¿½re un plan de coupe de la forme selon le plan donnï¿½
	 * 
	 * @param pl
	 * @return
	 */
	public Area intersectionVerticale(Plan3D pl) {
		Area ret = new Area();
		//Parcours tous les points verticaux. Attention les points entre et sortent de l'eau
		for (int ypos = 0; ypos < this.ySize; ypos ++) {
			// calcule la position du plan pour une tranche
			for (int xpos = 1; xpos < this.xSize; xpos ++) {
				Vecteur v = pl.intersection(this.getPoint(xpos-1, ypos), this.getPoint(xpos, ypos));
				if (v!= null) {
					if (!ret.points.contains(v)) {
						ret.points.add(v);
					}
				}
			}
		}
		return ret;
	}

	public Area intersectionHorizontaleZ(Plan3D pl) {
		Area ret = new Area();
		//Parcours tous les points verticaux. Attention les points entre et sortent de l'eau
		for (int xpos = 0; xpos < this.xSize; xpos ++) {
			// calcule la position du plan pour une tranche
			for (int ypos = 1; ypos < this.ySize; ypos ++) {
				Vecteur A = this.getPoint(xpos, ypos-1);
				Vecteur B = this.getPoint(xpos, ypos);
				Vecteur v = pl.intersection(A, B);
				if (v!= null) {
					if (!ret.points.contains(v)) {
						ret.points.add(v);
					}
					
				}
			}
		}
		return ret;
	}

	// Découpe en tranche sur le plan X = pos
	public Area intersectionHorizontaleX(Plan3D pl) {
		Area ret = new Area();
		ArrayList<Vecteur> haut = new ArrayList<Vecteur> ();
		ArrayList<Vecteur> bas = new ArrayList<Vecteur> ();
		//Parcours tous les points verticaux. Attention les points entre et sortent de l'eau
		for (int ypos = 0; ypos < this.ySize; ypos ++) {
			Vecteur last = null;
			boolean ok = false;
			// calcule la position du plan pour une tranche
			for (int xpos = 1; xpos < this.xSize; xpos ++) {
				Vecteur A = this.getPoint(xpos-1, ypos);
				Vecteur B = this.getPoint(xpos, ypos);
				Vecteur v = pl.intersection(A, B);
				if (v!= null) {
					if (last == null) { 
						last = v;
					} else {
						ok = true;
						if (last.getDecY().compareTo(v.getDecY()) >= 0) {
							haut.add(last); bas.add(v); 
						} else {
							haut.add(v); bas.add(last); 
						}
					}
				}
			}
			if (!ok) { // haut et bas non affectés
				if( last != null) { // un seul point
					haut.add(last);
					bas.add(last);
				}
			}
		}
		// parcous les liste et ajout les éléments
		for (Vecteur v : haut) ret.points.add(v);
		for (int p = bas.size()-1; p >= 0; p--) ret.points.add(bas.get(p));
		return ret;
	}

	
	/**
	 * Retourne la projection de la forme sur un plan donnÃ© par l'axe
	 *  
	 * @return
	 */
	public Area getProjection () {
		Area surface = new Area();
		ArrayList<Vecteur> maxes = new ArrayList<Vecteur>();
		ArrayList<Vecteur> mines = new ArrayList<Vecteur>();
		
		for (int y = 0; y < ySize(); y++) {
			Vecteur max = null;
			Vecteur min = null;
			for (int x = 0; x < xSize(); x++) {
				Vecteur v = getPoint(x, y);
				if ((max == null)||(max.getDecY().compareTo(v.getDecY()) < 0)) max = v;
				if ((min == null)||(min.getDecY().compareTo(v.getDecY()) > 0)) min = v;
			}		
			maxes.add(max); mines.add(min);
		}

		for (Vecteur v : maxes) {
			surface.points.add(new Vecteur (Decimal.ZERO, v.getDecY(), v.getDecZ()));
		}
		for (int p = mines.size()-1; p >= 0; p--) {
			Vecteur v = mines.get(p);
			surface.points.add(new Vecteur (Decimal.ZERO, v.getDecY(), v.getDecZ()));
		}

		return surface;
	}

    public String toString() {
        StringBuilder sb = new StringBuilder("Patch = ");

		for (int ysm = 0 ; ysm < this.ySize; ysm++) {					
			for (int xsm = 0 ; xsm < xSize; xsm ++) {
				sb.append (this.getPoint(xsm, ysm));
				sb.append (", ");
			}
			sb.append("\n"); 
		}

        return sb.toString();
    }
    
    /**
     * détermine le centre géométrique de la map 
     * 
     * @return
     */
    public Vecteur getCentre () {
    	ArrayList<Vecteur> pts = new ArrayList<Vecteur>();
		for (int y = 0 ; y < this.ySize; y++) {					
			for (int x = 0 ; x < xSize; x ++) {
				pts.add(getPoint(x, y));
			}
		}

		return CalculSurface.getCentreGeometrique(pts);
    }
    
    /**
     * Construit une MAP redimentionnée de X 
     * 
     * @param ep
     * @return
     */
    public MapDeVecteurs rezise (Decimal ep) {
    	Vecteur ctr = this.getCentre();
    	MapDeVecteurs ret = new MapDeVecteurs(this.xSize, this.ySize);

		for (int y = 0 ; y < this.ySize; y++) {					
			for (int x = 0 ; x < xSize; x ++) {
				Vecteur v = getPoint(x, y);
				Vecteur p = v.minus(ctr);
				Decimal d = p.getNorme();
				Decimal r = d.divide(d.add(ep));
				Decimal dx = p.getDecX().multiply(r).add(ctr.getDecX());
				Decimal dy = p.getDecY().multiply(r).add(ctr.getDecY());
				Decimal dz = p.getDecZ().multiply(r).add(ctr.getDecZ());
				ret.setPoint(x, y, new Vecteur (dx, dy, dz));
			}
		}
		return ret;
    }

	public boolean isValid() {
		for (int y = 0 ; y < this.ySize; y++) {					
			for (int x = 0 ; x < xSize; x ++) {
				if (getPoint(x, y) == null) return false;
			}
		}
		return true;
	}
}