package view.param;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import java.text.SimpleDateFormat;

import view.gabarits.GabaritDrawer;
import view.scene.PrintableObject;
import view.scene.PrintedGabarit;
import view.view2D.ObjectDrawer;

public class ImageFileCreator {

	public static void createFile(PrintableObject pg, int pos, PrintingParameters params, String catafilename) throws IOException {
		/** Définition de la taille du dessin --> 20.32x27.94 cm => 8x11 ***/
		int xSize = params.pixByInch*8;
		int ySize = params.pixByInch*11;
		BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB); 
		Graphics2D gr = image.createGraphics();
		// Affiche le fond
		gr.setColor(Color.WHITE);
		gr.fillRect(0, 0, xSize, ySize);
		/** Affiche les données d'information dans un encart 
		 *   --> Fichier de catamaran
		 *   --> Numéro de tranche, position et épaisseur 
		 *   --> Date d'édition **/
		gr.setColor(Color.BLACK);
		gr.drawRect(30, 30, 600, 160);
	    gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    gr.setFont(new Font (Font.SANS_SERIF, Font.PLAIN, 38));
		gr.drawString("File = "+catafilename, 40, 80);
		gr.drawString(pg.getStringDescr(), 40, 120);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		gr.drawString(sdf.format(new Date()), 40, 160);
		
		// Affichage de la forme
		Rectangle rec = new Rectangle (10, 170, xSize-20, ySize-180);
		
		ObjectDrawer gd = getDrawer(rec, pg, params.pixByInch);
		gd.drawObject(gr, true, false);

		sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
		String dte = sdf.format(new Date());

		// --> Supprime l'extention
		String cataFile = catafilename;
		int p = catafilename.lastIndexOf('.');
		if (p != -1) {
			cataFile = catafilename.substring(0, p);
		}
		
		// Stockage d'image dans le fichier
		File nomfichier = new File(params.fileName + File.separator+cataFile+"_"+pos+"."+dte+".bmp");// ou jpg 
		ImageIO.write(image, "BMP", nomfichier);//ou JPG 
	}
	
	
	public static ObjectDrawer getDrawer (Rectangle rec, PrintableObject pg, int pix) {
		GabaritDrawer gd =  new GabaritDrawer(rec, (PrintedGabarit)pg, pix);
		gd.setObject((PrintedGabarit)pg, rec, pix);
		return gd;

	}

}
