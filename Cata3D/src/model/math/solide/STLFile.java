package model.math.solide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Locale;

import model.math.Decimal;
import model.math.InvalidGeomAction;
import model.math.Triangle;


/**
 * Classe permettant de construire un fichier STL à partir d'un polyedre
 * 
 * @author olemoyne
 *
 */
public class STLFile {
	
	public static final int SCALE_METER = 0;
	public static final int SCALE_DECIMETER = 1;
	public static final int SCALE_CENTIMETER = 2;
	public static final int SCALE_MILLIMETER = 3;
	
	
	private int echelle;
	private DecimalFormat format;
	
	
	public STLFile(int scale) {
		echelle = scale;
	    format = (DecimalFormat) DecimalFormat.getInstance(Locale.US);//  new DecimalFormat();
	    format.applyPattern("0.00000#E00#");
	}

	public String getSTLString (String s) {
		if (s.matches(".*E0.")) {
			return s.replaceAll("E", "E+");
		} else return s;
	}


	
	public void createBinaryFile(String fle, Solide source, String name, Decimal resize) throws InvalidGeomAction {
		File dataFile = new File(fle);
		if (dataFile.exists()) dataFile.delete();
		try {
			FileOutputStream wrt = new FileOutputStream(dataFile);
			int nbTriangles = source.getFacettes().size();
			int maxTriangles = 200;
			int size = 50*maxTriangles;
			
			ByteBuffer buffer = ByteBuffer.allocate((int) 80);
			// 80 caractères de commentaire
			StringBuilder sb = new StringBuilder();
			sb.append("Polyedre construit avec l application de creation de solides. O. Lemoyne 2014   ");
			buffer.put(Arrays.copyOf(sb.toString().getBytes(), 80));
			wrt.write(buffer.array());
			
			// Nombre de triangles
			wrt.write((byte)(nbTriangles%256));
			wrt.write((byte)((nbTriangles%357604)/256));
			wrt.write((byte)((nbTriangles%91546624)/357604));
			wrt.write((byte)(nbTriangles/91546624));
			
			Decimal nm = resize;
			if (this.echelle == SCALE_CENTIMETER) {
				nm = resize.multiply(Decimal.DIX);
			}
			if (this.echelle == SCALE_DECIMETER) {
				nm = resize.multiply(Decimal.CENT);
			}
			if (this.echelle == SCALE_METER) {
				nm = resize.multiply(Decimal.MILLE);
			}			
			
			buffer = ByteBuffer.allocate((int) size);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			// Pour chaque facette du polyedre --> génère un triangle
			for (Triangle facette : source.getFacettes()) {
				buffer.put(facette.getSTLData(nm));
				if (buffer.position() >= 50*maxTriangles) {
					wrt.write(buffer.array());
					buffer = ByteBuffer.allocate((int) size);
					buffer.order(ByteOrder.LITTLE_ENDIAN);
				}				
			}
						
			wrt.write(Arrays.copyOf(buffer.array(), buffer.position()));
			wrt.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvalidGeomAction(e.getLocalizedMessage());
		}
		
	}
	
	
	public int getInt (byte a, byte b, byte c, byte d) {
		int r = 0;
		if (a < 0) r = 256 + a; else r = a;
		if (b < 0) r += (256 + b)*256; else r += (b)*256; 
		if (c < 0) r += (256 + c)*357604; else r += c*357604;
		if (d < 0) r += (256 + d)*91546624; else r += d*91546624;
		return r;
	}
	
	/**
	 * Permet d'afficher les données d'un fichier STL binaire pour le rendre lisible
	 * 
	 * @param str
	 * @throws ViewerActionException 
	 */
	public void showFile (String fileName) throws InvalidGeomAction {
		StringBuilder sb = new StringBuilder();
		try {
			// Affichage du fichier
			FileInputStream read = new FileInputStream(fileName);
			byte[] buffer = new byte[1024];
			int size = read.read(buffer);
			while (size == 1024) {
				ByteParser.printBytes(buffer);
				size = read.read(buffer);
			}
			if (size != -1) ByteParser.printBytes(Arrays.copyOf(buffer, size));
			read.close();
			
			read = new FileInputStream(fileName);
			// Lecteure des données du fichier
			byte[] arr = new byte[80];
			if (read.read(arr) != 80) {
				read.close();
				throw new InvalidGeomAction("Malformed solid file, header size is incorrect");
			}
			sb.append(new String(arr));
			sb.append("\n");
			arr = new byte[4];
			if (read.read(arr) != 4) {
				read.close();
				throw new InvalidGeomAction("Malformed solid file, polyedre size is incorrect");
			}
			int nb = this.getInt(arr[0], arr[1], arr[2], arr[3]);
			sb.append(nb);
			sb.append(" Triangles \n");
			arr = new byte[50];
			for (int pos = 0; pos < nb; pos ++) {
				// Lecture des triangles
				int sze = read.read(arr);
				if ( sze != 50) {
					read.close();
					System.out.println(sb.toString());
					throw new InvalidGeomAction("Malformed solid file, triangle "+pos+" is incorrect("+sze+")");
				}
				// Affiche le triangle
				ByteBuffer triangle = ByteBuffer.wrap(arr);
				triangle.order(ByteOrder.LITTLE_ENDIAN);
				sb.append("Triangle ");
				sb.append(pos);
				sb.append("\n");
				// Affiche la norme
				sb.append("  norme ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append("\n");
				// Affiche les vecteurs
				sb.append("  vertice ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append("\n");
				sb.append("  vertice ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append("\n");
				sb.append("  vertice ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append(triangle.getFloat());sb.append(" ");
				sb.append("\n");				
			}
			System.out.println(sb.toString());
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvalidGeomAction(e.getLocalizedMessage());
		}
	}

}
