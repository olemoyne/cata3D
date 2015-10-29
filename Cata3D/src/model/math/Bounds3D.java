package model.math;

import model.Area;

public class Bounds3D {
	
	private Vecteur max;
	private Vecteur min;
	
	public Bounds3D () {
	}

	public void addPoint(Vecteur v) {
		if (max == null) {
			max = v; min = v; return;
		}
		
		if (max.getX() < v.getX()) max = max.set(Axis.XAxis, v.getX());
		if (max.getY() < v.getY()) max = max.set(Axis.YAxis, v.getY());
		if (max.getZ() < v.getZ()) max = max.set(Axis.ZAxis, v.getZ());

		if (min.getX() > v.getX()) min = min.set(Axis.XAxis, v.getX());
		if (min.getY() > v.getY()) min = min.set(Axis.YAxis, v.getY());
		if (min.getZ() > v.getZ()) min = min.set(Axis.ZAxis, v.getZ());
	}

	
	public Vecteur getMax() {
		return max;
	}

	public Vecteur getMin() {
		return min;
	}

	public void add(Area a) {
		for (Vecteur v : a.points) addPoint(v);
		
	}


}
