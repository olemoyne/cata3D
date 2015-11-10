package appli;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Cata;
import model.patch.Patch;

/** 
 * Permet d'afficher le nom du fichier en cours d'adition
 * Permet d'ouvrir / créer / sauver l'étude en cours
 * 
 * @author olemoyne
 *
 */
public class CataFileManager extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8089353080616451909L;
	
	private JButton ajoute, supprime;
	private JTextField editeur;

	public CataFileManager (ActionListener mngr) {		
		super();
		
		Color buttonColor = this.getBackground();

		this.setLayout(new FlowLayout(FlowLayout.LEADING));

		editeur = new JTextField();
		editeur.setColumns(80);
		editeur.setToolTipText("Saisir le chemin vers le fichier à éditer");
		this.add(editeur);
		
// Ajoute les boutons de gestion 
		ajoute = new JButton("Ouvre");
		ajoute.setForeground(Color.black);
		ajoute.setBackground(buttonColor);
		ajoute.setToolTipText("Ouvre un fichier de catamaran");
		ajoute.setActionCommand("Ouvre");
		if (mngr != null) ajoute.addActionListener(mngr);		
		this.add(ajoute);

		// Ajoute les boutons de gestion 
		supprime = new JButton("Sauve");
		supprime.setForeground(Color.black);
		supprime.setBackground(buttonColor);
		supprime.setToolTipText("Sauvegarde le fichier en cours");
		supprime.setActionCommand("Sauve");
		if (mngr != null) supprime.addActionListener(mngr);		
		this.add(supprime);

	}

	
	public void setFile(String str) {
		this.editeur.setText(str);
	}
	
	public String getFile() {
		return this.editeur.getText();
	}

	
	/**
	 * Extrait le Catamaran du fichier 
	 * 
	 * @return
	 */
	public Cata getCataFromFile () throws CataAppliException{
		String str = this.editeur.getText();
		if (str == null) throw new CataAppliException("Empty file name");
		
		File fle = new File (str);
		if (!fle.exists()) throw new CataAppliException("File not found "+str);
			
		try {
			FileInputStream fis = new FileInputStream(fle);
		    ObjectInputStream ois = new ObjectInputStream(fis);
		    
		    Cata bato = (Cata)ois.readObject();
		    bato.setPatch(bato.patch.x, bato.patch.y);
		    ois.close();
		    
		    return bato;
		} catch (FileNotFoundException e) {
			throw new CataAppliException(e);
		} catch (IOException e) {
			throw new CataAppliException(e);
		} catch (ClassNotFoundException e) {
			throw new CataAppliException(e);
		}
	}
	
	public void saveCataToFile (Cata bato) throws CataAppliException {
		String str = this.editeur.getText();
		if (str == null) throw new CataAppliException("Empty file name");
		
		File fle = new File (str);
		try {
			if (!fle.exists()) fle.createNewFile();

			FileOutputStream fis = new FileOutputStream(fle);
		    ObjectOutputStream ois = new ObjectOutputStream(fis);
			
		    ois.writeObject(bato);
		    
		    ois.close();
		} catch (IOException e) {
			throw new CataAppliException(e);
		}	
	}
	
	
	
	/*** 
	 * Test purpose  
	 * **/
	public static void main(String [ ] args) {
		// sauvegarde d'un bateau
		Cata boat = new Cata();
		// Construit un patch de test
		boat.patch = Patch.getPatch();
		//Sauvegarde du bateau
		CataFileManager mngr = new CataFileManager (null); 
		mngr.editeur.setText("D:/dev/GIT/cata3D/Cata3D/data/testFile.boat");
		try {
			mngr.saveCataToFile(boat);
			// lecture du bateau
			Cata nBoat = mngr.getCataFromFile();
			if (nBoat.patch == null) {
				System.err.println("Sauvegarde KO : patch null");
				System.exit(12);
			}
			if (nBoat.patch.x != 4) {
				System.err.println("Sauvegarde KO : patch de mauvaise taille");
				System.exit(12);
			}
			if (nBoat.patch.y != 4) {
				System.err.println("Sauvegarde KO : patch de mauvaise taille");
				System.exit(12);
			}
			if (nBoat.patch.points.length != 4) {
				System.err.println("Sauvegarde KO : patch de mauvaise taille");
				System.exit(12);
			}
			System.err.println("Test OK - Sauvegarde réussie");
			System.out.println(nBoat.toString());
		} catch (CataAppliException e) {
			e.printStackTrace();
			System.exit(12);
		}
	}

}
