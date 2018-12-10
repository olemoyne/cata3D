package view.gabarits;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import model.Area;
import model.calcul.CalculSurface;
import model.math.Axis;
import model.math.Vecteur;
import view.scene.PrintableObject;
import view.scene.PrintedGabarit;
import view.view2D.ObjectDrawer;

public class GabaritDrawer extends ObjectDrawer{

	private PrintedGabarit gabarit;
	
	private Vecteur fullPosition;
	private Vecteur insidePosition;


	public GabaritDrawer (Rectangle d){
		super(d);
		axis = Axis.ZAxis;
	}

	public GabaritDrawer (Rectangle d, PrintedGabarit g){
		super(d, g);
		axis = Axis.ZAxis;
	}

	public GabaritDrawer (Rectangle d, PrintedGabarit g, int dpi){
		super(d, g, dpi);
		axis = Axis.ZAxis;
		gabarit = g;
	}
	

	public void addComment (Graphics gr) {
		if (gr != null) {
			if (fullPosition != null) {
				gr.setColor(Color.RED);
				drawPoint(gr, fullPosition);			
			}
	
			if (insidePosition != null) {
				gr.setColor(Color.CYAN);
				drawPoint(gr, insidePosition);			
			}
		}
	}


	public void drawObject(Graphics gr, boolean quad, boolean fond) {
		
		// Affiche le fond en blanc
		if (fond) {
			gr.setColor(Color.WHITE);
			gr.fillRect(dim.x, dim.y, dim.width, dim.height);
		}
		
		if (quad) showQuad(gr);
		
		if (gabarit == null) return ;

		// Affiche la limite de flottaison en bleu
		gr.setColor(Color.BLUE);
		gr.drawLine(getX(gabarit.mer.getA()), getY(gabarit.mer.getA()), getX(gabarit.mer.getB()), getY(gabarit.mer.getB()));

		// Affiche le gabarit non resiz� en rouge
		gr.setColor(Color.RED);
		drawArea(gr, gabarit.full);

		// Affiche le gabarit en noir
		gr.setColor(Color.BLACK);
		drawArea(gr, gabarit.fullInside);

		// Affiche les trous en vert
		gr.setColor(Color.GREEN);
		for (Area a : gabarit.trous) {
			drawArea(gr, a);
		}

		// Affiche les �l�ments adjacents en orange
		gr.setColor(Color.ORANGE);
		for (Area a : gabarit.effacements) {
			drawArea(gr, a);
		}
		
		Vecteur pt = CalculSurface.getCentre(gabarit.full.points, Axis.ZAxis);
		drawPoint(gr, pt);		
		
	}
	
	public void setObject(PrintableObject g, Rectangle d) {
		super.setObject(g, d);
		this.gabarit = (PrintedGabarit)g;
	}

	public String getData(PrintableObject obj) {
		PrintedGabarit gab = (PrintedGabarit)obj;
		
		// Affiche la position en Z
		StringBuffer sb = new StringBuffer("Gabarit ");
		sb.append(gab.position);
		sb.append(" de position ");
		sb.append(gab.zPosition.toString());
		sb.append(" d'épaisseur ");
		sb.append(gab.epaisseur.toString());
		sb.append("\n");
		

    	return sb.toString();
	}
	
	
	public String getMouseData(PrintableObject obj, Point2D pt) {
		if (this.gabarit == null) return "";
		Vecteur pos= getPosition(pt);
		Vecteur mousePosition = pos;
		
		// D�termine le point du FULL le plus proche
		int id = gabarit.full.getNearestPoint(pos);
		fullPosition = gabarit.full.points.get(id);
		insidePosition = null;
		if (gabarit.fullInside.points.size() > id) {
			id = gabarit.fullInside.getNearestPoint(pos);
			insidePosition = gabarit.fullInside.points.get(id);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Mouse position : ");
		sb.append(mousePosition.toString());
		if (fullPosition != null) {
			sb.append("\n");
			sb.append("Full nearest point position : ");
			sb.append(fullPosition.toString());
		}
		if (insidePosition != null) {
			sb.append("\n");
			sb.append("Inside nearest point position : ");
			sb.append(insidePosition.toString());
		}
				
		return sb.toString();
	}
	
}
