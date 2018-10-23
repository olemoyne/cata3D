package model.solid.manage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import model.concepts.math.Decimal;
import tarpeia.com.model.BufferParser;
import tarpeia.com.model.SerialMaker;
import tarpeia.com.model.SerialObj;
import tarpeia.com.model.SerialObjException;
import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

/**
 *   Description d'un élément 3D ou 2D à afficher ou permettant de faire des calculs 
 *  
 * @author olemoyne
 *
 */
@TarpeiaObject
public class Dessin {
	
	public static final int DESSIN_TYPE_SOLIDE = 0;
	public static final int DESSIN_TYPE_SLICE = 1;
	
	
	/**
	 *  Type de dessin
	 */
	@TarpeiaField
	public int dessinType;

	/**
	 * Forme du dessin
	 */
	@TarpeiaField
	public Object forme;  

	/**
	 * matière du composant
	 */
	@TarpeiaField
	public String matiere;

	/**
	 * Couleur d'affichage du composant
	 */
	@TarpeiaField
	public int color;
	
	/**
	 * Densité du composant
	 */
	@TarpeiaField
	public Decimal densite;

	
	/**
	 * Creation de la forme 
	 */
	public Dessin () {
	}
		
	

	/**
	 * Lecture d'un dessin à partir d'un fichier donné
	 * 
	 * @param path
	 * @return
	 * @throws SerialObjException
	 * @throws InvalidSolidException
	 */
	public static Dessin readFromFile (String path) throws SerialObjException, InvalidSolidException {
		Dessin dessin = null;
		File formeDataFile = new File(path);
		if (!formeDataFile.exists()) return null;
		try {
			FileInputStream w = new FileInputStream(formeDataFile);
			int sze = (int)formeDataFile.length();
			if (sze > 0) {
				byte[] array = new byte[sze];
				w.read(array);
				w.close();

				SerialObj obj = BufferParser.getSerial(ByteBuffer.wrap(array));
				dessin = (Dessin) SerialMaker.getDataObject(obj, null);
			} else {
				dessin = null;
			}
		} catch (SerialObjException e) {
			e.printStackTrace();
			throw new InvalidSolidException(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvalidSolidException(e.getLocalizedMessage());
		}
		return dessin;
	}


	/**
	 * Ecriture du dessin dans un fichier
	 * 
	 * @param formeDataFile
	 * @throws InvalidSolidException
	 */
	public void writeToFile(File formeDataFile) throws InvalidSolidException {
		if (!formeDataFile.exists())
			try {
				formeDataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new InvalidSolidException(e.getLocalizedMessage());
			}

		try {
			SerialObj obj = SerialMaker.getSerialObject(this);
			byte[] array = BufferParser.getBuffer(obj);
			FileOutputStream w = new FileOutputStream(formeDataFile);
			w.write(array);
			w.close();
		} catch (SerialObjException e) {
			e.printStackTrace();
			throw new InvalidSolidException(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvalidSolidException(e.getLocalizedMessage());
		}
	}

}
