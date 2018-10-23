package model.solid.manage;

import java.awt.Color;

import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Segment;
import model.concepts.math.Triangle;


/**
 * 
 * Element d'un dessin avec :
 *    * une forme (Triangle)
 *    * une couleur
 * 
 * @author olemoyne
 *
 */
public class Composant {

	/**
	 * Forme du composant
	 */
	public Object forme;
	
	/**
	 *  Type de composant 
	 */
	public int type;

	public final static int TYPE_TRIANGLE =0;
	public final static int TYPE_SEGMENT =1;
	
	

	/**
	 * Couleur d'affichage du composant
	 */
	public Color color;
	
	/**
	 * Nom du composant 
	 */
	public String nom;

	
	/**
	 * Constructeur par d�faut
	 */
	public Composant() {
		
	}


	public Composant(Triangle pol, String n, Color c) {
		this.type = TYPE_TRIANGLE;
		this.forme = pol;
		this.nom = n;
		color = c;
	}

	public Composant(Segment pol, String n, Color c) {
		this.type = TYPE_SEGMENT;
		this.forme = pol;
		this.nom = n;
		color = c;
	}


	public Composant(Composant o) throws InvalidGeomAction {
		this.forme = o.forme;
		this.nom = o.nom;
		this.color = o.color;
	}


	/**
	 * Chaine de caract�re de type type:nom:couleur:data
	 * @param lne
	 * @throws InvalidGeomAction 
	 */
	public Composant(String lne) throws InvalidGeomAction {
		String []data = lne.split(":");
		
		this.type = Integer.parseInt(data[0]);
		this.forme = Triangle.getTriangle(data[3]);
		this.nom = data[1];
		this.color = new Color(Integer.parseInt(data[2]));
	}
	
	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append("TRIANGLE:");
		sb.append(nom); sb.append(":");
		sb.append(color.getRGB()); sb.append(":");
		sb.append(forme.toString()); sb.append(":");
		return sb.toString();
	}
	
}
