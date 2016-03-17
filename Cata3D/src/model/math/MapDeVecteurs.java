package model.math;

import java.io.Serializable;
import java.util.ArrayList;

import model.Area;
import model.calcul.CalculSurface;
import model.math.transfo.Transformation;


/**
 *  Maillage de vecteurs qui correspondent � une coque
 *   --> X �talonnage vertical 
 *   --> Y �talonnage horizontal
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

	// dans ce cas, un nouveau tableau de donn�es est cr��
	//  les points sont ajout�s � chaque colonne,� la suite
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

	/** Calcule le r�sultat entre une map et un plan
	 * 
	 * @param surface
	 * @return
	 */
	public MapDeVecteurs truncate(Plan3D surface) {
		// Recopie les �l�ments de la map
		MapDeVecteurs ret = new MapDeVecteurs(this);
		
		ArrayList<Integer> toRemove = new ArrayList<Integer>(); 
		
		//Parcours tous les points verticaux. Attention les points entre et sortent de l'eau
		for (int ypos = 0; ypos < this.ySize; ypos ++) {
			
			// calcul l'intersection avec le plan
			Vecteur inter = null;
			boolean nullValues = false;
			boolean notNullValues = false;
			// Recherche la premi�re intersection
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
			// Attention, il existe des cas � null
			if (nullValues) {
				Vecteur last = null;
				for (int xpos = this.xSize-1; xpos >= 0 ; xpos --) {
					if (ret.getPoint(xpos, ypos) == null)
						ret.setPoint(xpos, ypos, last);
					else last = ret.getPoint(xpos, ypos);
				}
			}
		}
		
		// Supprime les tranches sans intersections
		// Si aucune intersection --> retourne aucune MAP
		if (toRemove.size() == ySize) return null;

		MapDeVecteurs mps = new MapDeVecteurs(this.xSize, this.ySize-toRemove.size());
		int indx = 0;
		for (int ypos = 0; ypos < this.ySize; ypos ++) {
			boolean trouve = false;
			for (int i : toRemove) if (i == ypos) trouve = true;
			if (!trouve) {
				for (int xpos = 0; xpos < this.xSize; xpos ++) {
					mps.setPoint(xpos, indx, ret.getPoint(xpos, ypos));
				}	
				indx ++;
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
	 * G�n�re un plan de coupe de la forme selon le plan donn�
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
				if (v!= null) ret.points.add(v);
			}
		}
		return ret;
	}

	public Area intersectionHorizontale(Plan3D pl) {
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
	
	
	/**
	 * TODO : Retourne la projection de la forme sur un plan donné par l'axe
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
     * d�termine le centre g�om�trique de la map 
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
     * Construit une MAP redimentionn�e de X 
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
}