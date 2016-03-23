package model.patch;

import java.io.Serializable;
import java.util.ArrayList;

import model.Area;
import model.math.Axis;
import model.math.Bounds;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Plan3D;
import model.math.Vecteur;

/**
 * Patch de donne�s
 * 
 * @author olemoyne
 *
 */
public class Patch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3819829819391630423L;

	// D�finition des xx points 
	public int x, y;

	// Liste des points
	public Vecteur[][] points;

	/**
	 * Construction du Patch par d�faut
	 */
	public Patch () {
		x = 0;
		y = 0;
		
		points = new Vecteur[0][0];
	}

	/**
	 * Construit la MAP de vecteurs correspondant au patch d�finit
	 * Puis d�finit une liste de tranches verticales (plans) et calcule la MAP correspondant � la section entre ces plans 
	 *   et ma MAP
	 * 
	 * @param precisionDAffichage
	 * @return
	 */
	public MapDeVecteurs getMap(int nbPoints, boolean sym) {
		if (x+y == 0) return new MapDeVecteurs(0, 0);
		if (x*y == 0)  return new MapDeVecteurs(0, 0);
		// Détermine combien de sous éléments sont nécessaires
		int nbX = (y-1)/3;
		int nbY = (x-1)/3;
		
		// Agrégation totale
		MapDeVecteurs map = new MapDeVecteurs(nbY*(nbPoints), nbX*(nbPoints));
		
		for (int posX = 0; posX < nbX; posX++) {
			// Parcours toutes les zones par les X
			for (int posY = 0; posY < nbY; posY++) {
				// Récupère la MAP asssociée
				Vecteur[][] subMap = this.getSubMap(posY*3, posX*3, nbPoints);
				// Ajout la MAP dans la MAP générale
				int maxy = nbPoints;
//				if (posY == nbY-1) maxy ++;
				int maxx = nbPoints;
//				if (posX == nbX-1) maxx ++;
				for (int ysm = 0 ; ysm < maxy; ysm++) {					
					for (int xsm = 0 ; xsm < maxx; xsm ++) {
						map.setPoint(posY*(nbPoints)+ysm, posX*(nbPoints)+xsm, subMap[xsm][ysm]);
					}
				}
			}
		}
		MapDeVecteurs temp = map.reverse();
		MapDeVecteurs ret = getMapDecoupe(temp, nbPoints, Axis.ZAxis);		
		return ret;
	}

	public static MapDeVecteurs getMapDecoupe(MapDeVecteurs temp, int nbPoints, int axis) {
		Bounds bnds =Bounds.getBounds(temp);
		// Par d�faut 10 positions
		Decimal ecart = bnds.getMax().getDec(axis).minus(bnds.getMin().getDec(axis)).divide(new Decimal(nbPoints-1));
		 MapDeVecteurs ret = new MapDeVecteurs(temp.xSize(), nbPoints);
		for (int nb = 0; nb < nbPoints; nb ++) {
			Decimal pos = bnds.getMin().getDec(axis).add(new Decimal(nb).multiply(ecart));
			if (pos.compareTo(bnds.getMax().getDec(axis))>= 0) {
				pos = bnds.getMax().getDec(axis).minus(new Decimal(0.001d));
			}
			Plan3D pl = Plan3D.getPlan(axis, pos);
			Area aire = temp.intersectionHorizontale(pl);
			if (aire.points.size() != 0) {
				for (int x = 0; x < temp.xSize(); x++) {
					if (x < aire.points.size())
						ret.setPoint(x, nb, aire.points.get(x));
					else
						ret.setPoint(x, nb, aire.points.get(aire.points.size()-1));
				}
			} else {
				for (int x = 0; x < temp.xSize(); x++) {
					ret.setPoint(x, nb, new Vecteur());
				}				
			}
		}
		return ret;
	}

	/**
	 * Initialise le patch
	 * 
	 * @param xVal
	 * @param yVal
	 */
	public void recalcule(int xVal, int yVal) {
		// pas de modification
		if ((xVal == x) && (yVal == y)) return;
		
		// Initialise la liste des points 
		Vecteur[][] refresh = new Vecteur[xVal][yVal];
		
		for (int ypos = 0; ypos < yVal; ypos ++) {
			for (int xpos = 0; xpos < xVal; xpos ++) {
				if ((ypos < y) && (xpos < x)) 
						refresh[xpos][ypos] = points[xpos][ypos];
				else refresh[xpos][ypos] = new Vecteur();
			}
		}
		
		this.x= xVal;
		this.y = yVal;
		points=  refresh;
	}
	
	private Vecteur[] getPoints(int x, int y, int pos, int nbPoints) {
		int taille = 4;
		Vecteur[] pts = new Vecteur[taille];
		for (int l= 0; l<taille; l++) {
			pts[l] = points [pos+x][l+y];
		}
		// Calcule la spline d�finie par le point
		ArrayList<Vecteur> al = CourbeParametree3D.getCurvePoints(pts[0], pts[1], pts[2], pts[3], nbPoints-1);
		// recopie les points du arrayList vers le tableau
		Vecteur[] ret = new Vecteur[nbPoints];
		for (int i = 0; i < Math.min(al.size(), nbPoints); i ++) {
			ret[i] = (Vecteur)al.get(i);
		}
		return ret;
	}

	/**
	 * Retourne une map de 4x4 points
	 * 
	 * @param nbPoints
	 * @return
	 */
	private Vecteur[][] getSubMap(int x, int y,  int nbPoints) {
	
		// Calcul de la position des points
		Vecteur [][] map = new Vecteur [nbPoints][nbPoints];
		
		// Calcul des points du haut
		Vecteur[] haut = getPoints(x, y, 0, nbPoints);
		Vecteur[] flott = getPoints(x, y, 1, nbPoints);
		Vecteur[] flanc = getPoints(x, y, 2, nbPoints);
		Vecteur[] quille = getPoints(x, y, 3, nbPoints);
	
		for (int pos = 0; pos < nbPoints; pos ++) {
			ArrayList<Vecteur> al = CourbeParametree3D.getCurvePoints(haut[pos], flott[pos], flanc[pos], quille[pos], nbPoints-2);
			for (int h = 0; h < al.size(); h ++) {
//				map [pos][h+1] = (Vecteur)al.get(h);
				map [pos][h] = (Vecteur)al.get(h);
						}
//			Vecteur v = map[pos][1];
			
			// Gestion de la SKIN
//			map[pos][0] = new Vecteur(0, v.getY(), v.getZ());
//			Vecteur v = map[pos][al.size()];
			// Rajoute des points vides �quivalents au dernier point
//			for (int h = al.size()+1; h < nbPoints; h++ ) {
				// Gestion de la SKIN
//				map[pos][h] = new Vecteur(0, v.getY(), v.getZ());
//			}
		}
		
		return map;
	}
	
	
	public static Patch getPatch() {
		Patch patch = new Patch();
		patch.x = 4;
		patch.y = 4;
		patch.points = new Vecteur[4][4];
		patch.points[0][0] = new Vecteur("0.12;0.2;0");
		patch.points[0][1] = new Vecteur("0.2;0.1;0");
		patch.points[0][2] = new Vecteur("0.16;-0.08;0");
		patch.points[0][3] = new Vecteur("0.05;-0.15;0");
		patch.points[1][0] = new Vecteur("0.16;0.2;0.75");
		patch.points[1][1] = new Vecteur("0.28;0.12;0.75");
		patch.points[1][2] = new Vecteur("0.2;-0.25;0.75");
		patch.points[1][3] = new Vecteur("0.08;-0.32;0.75");
		patch.points[2][0] = new Vecteur("0.05;0.15;3");
		patch.points[2][1] = new Vecteur("0.2;0.08;3");
		patch.points[2][2] = new Vecteur("0.16;-0.12;3");
		patch.points[2][3] = new Vecteur("0.02;-0.4;3");
		patch.points[3][0] = new Vecteur("0.004;0.1;5");
		patch.points[3][1] = new Vecteur("0.01;0;5");
		patch.points[3][2] = new Vecteur("0.01;-0.1;5");
		patch.points[3][3] = new Vecteur("0.004;-0.22;5");
		
		return patch;
	}

	public static final void main(String[] a) {
		Patch patch = new Patch();
		patch.x = 4;
		patch.y = 4;
		patch.points = new Vecteur[4][4];
		patch.points[0][0] = new Vecteur("0.001;0;0.0");
		patch.points[0][1] = new Vecteur("0.001;-0.01;0");
		patch.points[0][2] = new Vecteur("0.001;-0.02;0");
		patch.points[0][3] = new Vecteur("0.001;-0.04;0");
		patch.points[1][0] = new Vecteur("0.001;0;0.001");
		patch.points[1][1] = new Vecteur("0.001;-0.01;0.001");
		patch.points[1][2] = new Vecteur("0.001;-0.02;0.001");
		patch.points[1][3] = new Vecteur("0.001;-0.04;0.001");
		patch.points[2][0] = new Vecteur("0.001;0;0.002");
		patch.points[2][1] = new Vecteur("0.001;-0.01;0.002");
		patch.points[2][2] = new Vecteur("0.001;-0.02;0.002");
		patch.points[2][3] = new Vecteur("0.001;-0.04;0.002");
		patch.points[3][0] = new Vecteur("0.001;0;0.004");
		patch.points[3][1] = new Vecteur("0.001;-0.01;0.004");
		patch.points[3][2] = new Vecteur("0.001;-0.02;0.004");
		patch.points[3][3] = new Vecteur("0.001;-0.04;0.004");

		System.out.println(patch.getMap(10, true).toString());
	}

    public String toString() {
        StringBuilder sb = new StringBuilder("Patch = ");
        sb.append(x);
        sb.append(" ");
        sb.append(y);
        sb.append(" : \n");

		for (int ysm = 0 ; ysm < y; ysm++) {					
			for (int xsm = 0 ; xsm < x; xsm ++) {
				sb.append (points[xsm][ysm].toString());
			}
			sb.append("\n"); 
		}

        return sb.toString();
    }

    
    
	
}
