package model.concepts.solid;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import model.concepts.math.Axis;
import model.concepts.math.Decimal;
import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Plan3D;
import model.concepts.math.Vecteur;
import model.concepts.math.Segment;
import model.concepts.solid.Solide;
import model.concepts.math.Triangle;
import model.concepts.transfo.Rotation;
import model.concepts.transfo.Transformation;
import model.concepts.transfo.Translation;

/**
 * Repr�sente une courbe de d�coupe d'une forme selon un plan donn�
 * la courbe est identifi�e comme une liste de points cons�cutifs d�finissant la courbe de d�coupe
 * 
 * @author olemoyne
 *
 */
public class Slice {
	
	private Plan3D planDeDecoupe;
	
	private ArrayList<Vecteur> listeDesPoints;
	
	/** 
	 * Construction d'une d�coupe selon un plan donn�
	 *  
	 * @param plan de d�coupe
	 */
	public Slice (Plan3D plan) {
		planDeDecoupe = plan;
		listeDesPoints = new ArrayList<Vecteur>();
	}

	/** 
	 * Duplication d'une tranche
	 *  
	 * @param plan de d�coupe
	 */
	public Slice (Slice slc) {
		planDeDecoupe = slc.planDeDecoupe;
		listeDesPoints = new ArrayList<Vecteur>();
		listeDesPoints.addAll(slc.listeDesPoints);
	}

	/**
	 * D�coupe un solide selon le plan de d�coupe
	 * 
	 * @param sol
	 * @throws InvalidGeomAction 
	 */
	public void decoupe (Solide sol) throws InvalidGeomAction {
		ArrayList<Segment> segments = new ArrayList<Segment>();
		listeDesPoints.clear();
		// Parcours la liste des triangles du solide et determine la liste des segments d'intersrection
		for (Triangle tri : sol.getFacettes()) {
			Segment seg = tri.intersection(planDeDecoupe);
			// TODO : gestion du cas ou le triangle est dans le plan --> � voir
			if (seg != null) segments.add(seg);
		}
		// Recherche � replacer tous les segments � la suite les uns des autres.
		if (segments.size() == 0) return;
		Segment seg = segments.remove(0);
		listeDesPoints.add(seg.getA());
		listeDesPoints.add(seg.getB());
		while (seg != null) {
			seg = getNextSegment(segments, seg.getB());
			if (seg != null) {
				segments.remove(seg);
				listeDesPoints.add(seg.getB());
			}
		}
	}

	/** 
	 * Recherche le segment commencant par ce point parmis la liste
	 * @param segments
	 * @param b
	 * @return
	 */
	private Segment getNextSegment(ArrayList<Segment> segs, Vecteur b) {
		for (Segment s : segs) {
			if (s.getA().equals(b)) return s;
		}
		return null;
	}
	
	/**
	 * Permet de g�n�rer un script de parcours de la tranche selon une progression de la taille
	 * d�finie en parametre
	 * 
	 * @param step
	 * @param filename
	 */
	public void writeToFile (Decimal step, String filename) {
	}

	private Bounds getBounds() {
		Bounds bnds = new Bounds ();
		for (Vecteur v : this.getPoints()) {
			bnds.setBounds(v);
		}
		return bnds;
	}

	/**
	 * G�n�re un fichier avec l'image de la tranche
	 * 
	 * @param filename
	 * @throws InvalidGeomAction 
	 */
	public void writeImageFile (String filename, int ratio) throws InvalidGeomAction {
		// Get the boounds 
		Bounds bnds = this.getBounds();
		
		int x = (int)(bnds.getMax().getX() - bnds.getMin().getX())/ratio;
		int y = (int)(bnds.getMax().getY() - bnds.getMin().getY())/ratio;
		
		BufferedImage img = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		
		// Tracage de la forme sur l'image (polygone)
		int nb = this.listeDesPoints.size();
		int[] xTab = new int[nb];
		int[] yTab = new int[nb];
		
		
		for (int pos = 0; pos < nb; pos ++) {
			xTab[pos] = (int)this.listeDesPoints.get(pos).getX()/ratio;
			yTab[pos] = (int)this.listeDesPoints.get(pos).getY()/ratio;
		}
		g2.drawPolygon(xTab, yTab, nb);
		
		// Enregistrement de l'image sur le fichier
		try {
		    // retrieve image
		    File outputfile = new File(filename);
		    ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			throw new InvalidGeomAction (e.getLocalizedMessage());
		}
	}
		
	/**
	 * R�cup�re la liste des points qui d�terminent la tranche
	 * 
	 * @param step
	 * @return
	 */
	public ArrayList<Vecteur> getVecteurs (Decimal step) {
		ArrayList<Vecteur> ret = new ArrayList<Vecteur> ();
		// Parcours la liste des points
		Vecteur last = null;
		for (Vecteur next : listeDesPoints) {
			if (last != null) {
				// Parcours la distance entre les points par �tape
				// Calcule le nombre de points
				Decimal nb = roundUp(last.distance(next).divide(step));
				double iNb = nb.doubleValue();
				Vecteur var = new Vecteur(
						next.getDecX().minus(last.getDecX()).divide(nb),
						next.getDecY().minus(last.getDecY()).divide(nb),
						next.getDecZ().minus(last.getDecZ()).divide(nb));
				for (int index = 0; index <= iNb; index ++) {
					ret.add(last.add(var.multiply(new Decimal(index))));
				}
			}
			last = next;
		}
		// Dernier segment 
		Vecteur next = listeDesPoints.get(0);
		if (last != null) {
			// Parcours la distance entre les points par �tape
			// Calcule le nombre de points
			Decimal nb = roundUp(last.distance(next).divide(step));
			double iNb = nb.doubleValue();
			Vecteur var = new Vecteur(
					next.getDecX().minus(last.getDecX()).divide(nb),
					next.getDecY().minus(last.getDecY()).divide(nb),
					next.getDecZ().minus(last.getDecZ()).divide(nb));
			for (int index = 0; index <= iNb; index ++) {
				ret.add(last.add(var.multiply(new Decimal(index))));
			}
		}
		
		return ret;
	}

	/**
	 * Arrondi le Decimal � la position sup�rieure
	 * 
	 * @param divide
	 * @return
	 */
	private Decimal roundUp(Decimal val) {
		double d = Math.floor(val.doubleValue());
		if ( d == val.doubleValue()) return val;
		else return (new Decimal(d+1.0));
	}

	
	/** Op�re une trasformation sur la tranche pour la transposer sur le rep�re X/Y
	 * 
	 */
	public void repositionne () {
		// Calcule la transformation � effectuer � partir du Plan de d�coupe
		// Repositionne le rep�re du plan � l'origine
		Vecteur ctr=planDeDecoupe.getOrigine();
		Translation trans = new Translation(ctr.multiply(Decimal.UN.negate()), null);

		// Rotation dans l'axe des X
		Vecteur norme = planDeDecoupe.getNorme();
		Decimal angle = new Decimal((Math.asin(norme.getDecX().doubleValue())/Math.PI)*180.0);
		Rotation rotX = new Rotation (Axis.XAxis, angle, null);

		// Rotation dans l'axe des X
		angle = new Decimal((Math.asin(norme.getDecY().doubleValue())/Math.PI)*180.0);
		Rotation rotY = new Rotation (Axis.YAxis, angle, null);

		//Applique les transformations
		ArrayList<Vecteur>lst = new ArrayList<Vecteur>();
		for (Vecteur v : listeDesPoints) {
			lst.add(rotX.getPoint(rotY.getPoint(trans.getPoint(v))));
		}
		
		listeDesPoints = lst;
	}

	public ArrayList<Vecteur> getPoints() {
		return listeDesPoints;
	}

	public Slice transfo(Transformation trans) {
		ArrayList<Vecteur> arr = new ArrayList<Vecteur>();
		for (Vecteur v : listeDesPoints) {
			arr.add(trans.transforme(v));
		}
		Slice ret = new Slice (this.planDeDecoupe);
		ret.listeDesPoints = arr;
		return ret;
	}
	
	
	
}
