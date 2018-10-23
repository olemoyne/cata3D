package view.gabarits;

/**
 * Liste des parametres de dessin des �l�ments
 * 
 * @author olemoyne
 *
 */
public class PrintingParameters {
	
	// Affichage du quadriallage
	public boolean showQuadrillage;
	// taille de l'image  --> en pixels
	int pixByInch;
	
	public String fileName;
	
	public PrintingParameters() {
		showQuadrillage = true;
		pixByInch = 300;
	}

}
