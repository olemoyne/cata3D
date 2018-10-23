package math;

import java.awt.Color;


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
	public Triangle forme;

	/**
	 * Couleur d'affichage du composant
	 */
	public Color color;
	
	/**
	 * Nom du composant 
	 */
	public String nom;

	
	/**
	 * Constructeur par défaut
	 */
	public Composant() {
		
	}


	public Composant(Triangle pol, String n) {
		this.forme = pol;
		this.nom = n;
	}


	public Composant(Composant o) throws InvalidGeomAction {
		this.forme = new Triangle(o.forme);
		this.nom = o.nom;
		this.color = o.color;
	}


	/**
	 * Chaine de caractère de type type:nom:couleur:data
	 * @param lne
	 * @throws InvalidGeomAction 
	 */
	public Composant(String lne) throws InvalidGeomAction {
		String []data = lne.split(":");
		
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
