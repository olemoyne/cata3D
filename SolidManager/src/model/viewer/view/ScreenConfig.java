package model.viewer.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Gestion de la configuration des donn�es
 * 
 * @author olemoyne
 *
 */
public class ScreenConfig {
	
	private Properties prop;
	private String fileName;
	
	/**
	 * CReation de la configuration
	 * @param name
	 */
	public ScreenConfig (String name) {
		fileName = name;
		File fle = new File(name);
		prop = new Properties();

		// Initialisatoin du fichier
		if (!fle.exists()) {
			try {
				FileOutputStream output = new FileOutputStream(fileName);
				// save properties to project root folder
				prop.storeToXML(output, null);
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}

	public void loadConfig () {
		InputStream input = null;
	 
		try {
			input = new FileInputStream(fileName);
	 
			// load a properties file
			prop.loadFromXML(input);	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void saveConfig () {
		System.out.println("************ Sauvetage de la configuration ******************");
		OutputStream output = null;		 
		try {
	 
			output = new FileOutputStream(fileName);
			// save properties to project root folder
			prop.storeToXML(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	 
		}
	}
	
	/**
	 * Liste des repertoires de formes ouverts
	 * 
	 * @return
	 */
	public ArrayList<String> getRepertoires() {
		// parcours la liste des �l�ments
		ArrayList<String> strs = new ArrayList<String> ();
		String slng = prop.getProperty("nbFormeRepertoires");
		if (slng == null) return null;
		int lng = Integer.parseInt(slng);
		for (int pos = 0; pos< lng; pos ++) {
			StringBuilder sb = new StringBuilder("repertoires.");
			sb.append(pos);
			strs.add(prop.getProperty(sb.toString()));
		}
		return strs;
	}

	public void setDirectories(ArrayList<String> reps) {
		prop.setProperty("nbFormeRepertoires", Integer.toString(reps.size()));
		int pos = 0;
		for (String str : reps) {
			StringBuilder sb = new StringBuilder("repertoires.");
			sb.append(pos);
			prop.setProperty(sb.toString(), str);
			pos ++;
		}
	}

	public String getProperty(String name, String p) {
		StringBuilder sb = new StringBuilder(name);
		sb.append(".");
		sb.append(p);
		return prop.getProperty(sb.toString());
	}

	public void setProperty(String name, String p, String val) {
		StringBuilder sb = new StringBuilder(name);
		sb.append(".");
		sb.append(p);
		prop.setProperty(sb.toString(), val);
	}
}
