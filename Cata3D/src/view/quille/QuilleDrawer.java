package view.quille;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import model.math.Axis;
import model.math.Vecteur;
import view.scene.PrintableObject;
import view.view2D.ObjectDrawer;

public class QuilleDrawer extends ObjectDrawer{

	private PrintedQuille object;
	
	private Vecteur fullPosition;
	private Vecteur insidePosition;


	public QuilleDrawer (Rectangle d){
		super(d);
		this.axis= Axis.XAxis;
	}

	public QuilleDrawer (Rectangle d, PrintedQuille g){
		super(d, g);
		object = g;
	}

	public QuilleDrawer (Rectangle d, PrintedQuille g, int dpi){
		super(d, g, dpi);
		object = g;
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
		
		if (object == null) return ;

		// Affiche la limite de flottaison en bleu
		gr.setColor(Color.BLUE);
		gr.drawLine(getX(object.mer.getA()), getY(object.mer.getA()), getX(object.mer.getB()), getY(object.mer.getB()));

		// Affiche le gabarit non resiz� en rouge
		gr.setColor(Color.RED);
		drawArea(gr, object.contours);
		
		// Affiche le gabarit non resiz� en rouge
		gr.setColor(Color.BLACK);
		drawArea(gr, object.interieur);		

		if (object.limiteBordure.size()== 0) return;
		Vecteur v = null;
		for (int p = 1; p < object.limiteBordure.size(); p ++) {
			v = object.limiteBordure.get(p);
			gr.drawLine(getX(object.limiteBordure.get(p-1)), getY(object.limiteBordure.get(p-1)),	getX(v), getY(v));
		}

		
}
	
	public void setObject(PrintableObject g, Rectangle d) {
		super.setObject(g, d);
		this.object = (PrintedQuille)g;
	}

	public String getData(PrintableObject obj) {
		PrintedQuille gab = (PrintedQuille)obj;
		
		// Affiche la position en Z
		StringBuffer sb = new StringBuffer("Quille ");
		sb.append(" d'épaisseur ");
		sb.append(gab.epaisseur.toString());
		sb.append("\n");
		

    	return sb.toString();
	}
	
	
	public String getMouseData(PrintableObject obj, Point2D pt) {
		if (this.object == null) return "";
		Vecteur pos= getPosition(pt);
		Vecteur mousePosition = pos;

		StringBuilder sb = new StringBuilder();
		sb.append("Mouse position : ");
		sb.append(mousePosition.toString());

		if (object.contours != null) {
			// D�termine le point du FULL le plus proche
			int id = object.contours.getNearestPoint(pos);
			fullPosition = object.contours.points.get(id);
			
			if (fullPosition != null) {
				sb.append("\n");
				sb.append("Full nearest point position : ");
				sb.append(fullPosition.toString());
			}
		}
				
		return sb.toString();
	}
	
}
