package view.gabarits;

/**
 * Liste des parametres de dessin des éléments
 * 
 * @author olemoyne
 *
 */
public class PrintingParameters {
	
	// Affichage du quadriallage
	public boolean showQuadrillage;
	// taille de l'image  --> en pixels
	public int xSize;
	public int ySize;
	
	public String fileName;
	
	public PrintingParameters() {
		showQuadrillage = true;
		xSize = 1024;
		ySize = 768;
	}

}
