package model.concepts.solid;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;


public class Courbe2D {
	
	Point2D.Float debut;
	Point2D.Float fin;
	Point2D.Float controle1;
	Point2D.Float controle2;
	
	public Courbe2D () {
		debut = new Point2D.Float(0f, 0f);
		fin = new Point2D.Float(5f,  2f);
		
		controle1 = new Point2D.Float(1f, 0f);
		controle2 = new Point2D.Float(0f, 2f);
	}
	
	public static ArrayList<Point2D.Double> getCurvePoints (Point2D.Double d, Point2D.Double c1, Point2D.Double c2, Point2D.Double f, int max) {
		double k = 1/(double)max;

		ArrayList<Double> al = new ArrayList<Double>((int) max);
		double ax = d.getX();
		double ay = d.getY();
		double bx = c1.getX();
		double by = c1.getY();
		double cx = c2.getX();
		double cy = c2.getY();
		double dx = f.getX();
		double dy = f.getY();

		al.add(new Point2D.Double(ax, ay));

		double x, y;
		double t = 0;
		for(t=k;t<=1;t+=k){ 
			 x= (ax+t*(-ax*3+t*(3*ax-ax*t)))+
			    t*(3*bx+t*(-6*bx+bx*3*t))+
			    t*t*(cx*3-cx*3*t)+ 
				dx*t*t*t; 
			y=(ay+t*(-ay*3+t*(3*ay-ay*t)))+
			   t*(3*by+t*(-6*by+by*3*t))+
			   t*t*(cy*3-cy*3*t)+ 
			   dy*t*t*t;
			al.add(new Point2D.Double(x, y));
		}
		al.add(new Point2D.Double(dx, dy));
		return al;
	}

	/*
	//la map est définie sur une seule dimension
	public point getMap(int precision) {
		MapDeVecteurs map = new MapDeVecteurs(precision, 1);

		ArrayList al = Courbe2D.getCurvePoints(debut, controle1, controle2, fin, precision);
		int pos = 0;
        for (Iterator it = al.iterator(); it.hasNext(); pos ++){
        	Point2D.Float pt = (Point2D.Float)it.next();
        	map.setPoint(i, y, v)
        }
		return map;
	}
	*/
}