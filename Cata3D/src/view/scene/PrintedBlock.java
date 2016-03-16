package view.scene;

import java.awt.Color;

import view.view3D.Printer;
import model.Position;
import model.math.Vecteur;

import com.jogamp.opengl.GL2;

public class PrintedBlock extends PrintableObject {

	private Vecteur deb;
	private Vecteur fin;
	private boolean fillup;
	
	public PrintedBlock (Vecteur d, Vecteur f, String nom, boolean isFilled, Color col, Position pos) {
		super(nom, col, pos);
		deb = d;
		fin = f;
		fillup = isFilled;
	}
	
	
	@Override
	public void drawObject(GL2 gl, int mode) {
		if ((!fillup)&&(mode == 0)) Printer.drawLinePave(gl, deb, fin, color);
		else Printer.drawFilledPave(gl, deb, fin, color);
	}
	
}
