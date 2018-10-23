package math;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



/**
 * Liste de composants comportant des triangles de couleurs différentes
 * 
 * @author olemoyne
 *
 */
public class Dessin {
	
	private ArrayList<Composant> triangles;
	

	public Dessin () {
		triangles = new ArrayList<Composant> ();
	}


	public ArrayList<Composant>  getFormes() {
		return triangles;
	}


	public Composant getForme(String nom) {
		for (Composant comp : triangles ) { 
			if (comp.nom.equals(nom)) return comp;
		}
		return null;
	}


	public void writeToFile(File datafile) throws InvalidGeomAction {
		try {
			FileWriter writer = new FileWriter(datafile);
			for (Composant comp : triangles ) {
				writer.write(comp.toString());
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			throw new InvalidGeomAction(e.getLocalizedMessage());
		}
	}


	public static Dessin readFromFile(File datafile) throws InvalidGeomAction {
		try {
			Dessin dessin = new Dessin();
			BufferedReader read = new BufferedReader(new FileReader(datafile));
			String lne = read.readLine();
			while (lne != null) {
				Composant comp = new Composant (lne);
				dessin.addForme(comp);
				lne = read.readLine();
			}
			read.close();
			return dessin;
		} catch (IOException e) {
			throw new InvalidGeomAction(e.getLocalizedMessage());
		}
	}


	public void addForme(Composant comp) {
		this.triangles.add(comp);
		
	}


	public void removeForme(int pos) {
		this.triangles.remove(pos);
	}

}
