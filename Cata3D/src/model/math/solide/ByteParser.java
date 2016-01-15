package model.math.solide;


/** Static Class used to print Serial objects into ByteArray representation. This is used for managing messages
 * and compact représentation of data.
 * 
 * @author olemoyne
 *
 */
public class ByteParser {
	
	/**
	 * Printing a ByteArray into String 
	 * @param data : byteArray
	 */
	public static void printBytes (byte[] data) {
		StringBuilder sb = new StringBuilder("\t");
		StringBuilder hex = new StringBuilder();
		StringBuilder car = new StringBuilder();
		int lne = 0;
		sb.append(lne);
		sb.append(" ");
		int p = 10000;
		for (int nb = 0; (nb < p)&&(nb < data.length); nb ++) {
			byte b = data[nb];
			// Conversion en hexadécimal
			hex.append(toHexString(b));
			hex.append(" ");
			if (Character.isLetterOrDigit((char)b)) {
				car.append((char)b);
			} else car.append(".");
			
			if ((nb+1) % 40 == 0) {
				lne ++;
				sb.append(hex);
				sb.append(car);
				System.out.println(sb.toString());
				hex = new StringBuilder();
				car = new StringBuilder();
				sb = new StringBuilder(" \t");
				sb.append(lne);
				sb.append(" ");
			}
		}
		sb.append(hex);
		sb.append(car);
		System.out.println(sb.toString());
	}
	
	
	/**
	 * Printing an array of bytes into a String with an Hexa représentation
	 * 
	 * @param b Byte array to print
	 * @return
	 */
	// Conversion en hexa
	protected static String toHexString(byte b) {
		StringBuilder out = new StringBuilder();
		int f = b;
		if (f < 0) f += 127;
		int a = f/16;
		if (a < 10)	out.append(a);
		if (a == 10) out.append('A');
		if (a == 11) out.append('B');
		if (a == 12) out.append('C');
		if (a == 13) out.append('D');
		if (a == 14) out.append('E');
		if (a == 15) out.append('F');
		a = f%16;
		if (a < 10)	out.append(a);
		if (a == 10) out.append('A');
		if (a == 11) out.append('B');
		if (a == 12) out.append('C');
		if (a == 13) out.append('D');
		if (a == 14) out.append('E');
		if (a == 15) out.append('F');
		return out.toString();
	}


}
