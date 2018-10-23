package model.viewer.view;

import java.awt.Color;

/**
 * Objet � afficher
 * 
 * @author olemoyne
 *
 */
public class PrintableForm {

	public static final int TYPE_POLY = 0;
	public static final int TYPE_ARRETE = 1;
	public static final int TYPE_FACETTE = 2;
	public static final int TYPE_POINT = 3;
	public static final int TYPE_SLICE = 4;
	
	
	/** 
	 * Type d'objet � afficher
	 */
	public int printedType; 
	
	/**
	 * Objet � afficher
	 */
	public Object data;
	
	/**
	 * Courleur d'affichage
	 */
	public Color color;


	public PrintableForm(int type, Object poly, Color col) {
		this.printedType = type;
		this.data = poly;
		this.color = col;
	}


}
