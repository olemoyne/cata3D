package model.calcul;


import model.Area;
import model.math.Decimal;
import model.math.Vecteur;

/**
 * La tranche est une combinaison de points représentant une aire { AREA } addictionnée de 
 *   plusieurs métriques permettant de voir combien de points sont ajoutés 
 * 
 * @author olemoyne
 *
 */
public class Tranche {
	
	public Area original;
	public Area target;
	
	public int startIncrustation;
	public int endIncrustation;
	
	public int countIn;
	public int countOut;
	
	public int countAdded;
	
	public Tranche (Area a) {
		original = a;
		target = null;
	}
	
	/** 
	 * Positionne la target et calcule les éléments 
	 * *******************************************************/
	public void setTarget(Area a) {
		target = a;
		startIncrustation = -1;
		endIncrustation = -1;
		
		for (int p = 0; p < original.points.size(); p++) {
			Vecteur v = original.points.get(p);
			if (!target.points.contains(v)) { // dans de l'incrustation
				if ((startIncrustation == -1)) startIncrustation = p-1;
			} else {                         // hors de l'incrustation
				if ((endIncrustation == -1)&&(startIncrustation != -1)) endIncrustation = p;
			}
		}
		
		// check le nombre de points de l'incrustation
		if (startIncrustation == -1) {
			this.countIn = original.points.size();
			this.countOut = 0;
		} else {
			this.countOut = endIncrustation - startIncrustation - 1;
			this.countIn = original.points.size() - countIn;
		}
	}
	
	
	public static void main (String[] arg) {
		Area a1 = new Area();
		a1.points.add(new Vecteur("4;6;1"));
		a1.points.add(new Vecteur("4;4;1"));
		a1.points.add(new Vecteur("4;2;1"));
		a1.points.add(new Vecteur("4;0;1"));
		a1.points.add(new Vecteur("0;0;1"));
		a1.points.add(new Vecteur("0;6;1"));

		Tranche tr = new Tranche (a1);
		Area a2 = new Area();
		a2.points.add(new Vecteur("7;5;1"));
		a2.points.add(new Vecteur("2;5;1"));
		a2.points.add(new Vecteur("2;1;1"));
		a2.points.add(new Vecteur("7;1;1"));
		
		Area a3 = CalculFormes.getExtrusion(a1, a2, Decimal.UN);
		tr.setTarget(a3);
		System.out.println("Cas 1.A : "+tr.startIncrustation+" - "+tr.endIncrustation );
		
		Area a1b = new Area(); // Cas B
		a1b.points.add(new Vecteur("4;4;1"));
		a1b.points.add(new Vecteur("4;2;1"));
		a1b.points.add(new Vecteur("4;0;1"));
		a1b.points.add(new Vecteur("0;0;1"));
		a1b.points.add(new Vecteur("0;6;1"));
		a1b.points.add(new Vecteur("4;6;1"));

		a3 = CalculFormes.getExtrusion(a1b, a2, Decimal.UN);
		tr.setTarget(a3);
		System.out.println("Cas 1.B : "+tr.startIncrustation+" - "+tr.endIncrustation );

		Area a1c = new Area(); // Cas C
		a1c.points.add(new Vecteur("4;4;1"));
		a1c.points.add(new Vecteur("4;2;1"));
		a1c.points.add(new Vecteur("4;0;1"));
		a1c.points.add(new Vecteur("0;0;1"));
		a1c.points.add(new Vecteur("0;6;1"));
		a1c.points.add(new Vecteur("4;6;1"));
		a1c.points.add(new Vecteur("4;4;1"));
		a1c.points.add(new Vecteur("4;2;1"));

		a3 = CalculFormes.getExtrusion(a1c, a2, Decimal.UN);
		tr.setTarget(a3);
		System.out.println("Cas 1.C : "+tr.startIncrustation+" - "+tr.endIncrustation );

		a2 = new Area();
		a2.points.add(new Vecteur("7;4;1"));
		a2.points.add(new Vecteur("2;4;1"));
		a2.points.add(new Vecteur("2;2;1"));
		a2.points.add(new Vecteur("7;2;1"));

		a3 = CalculFormes.getExtrusion(a1, a2, Decimal.UN);
		tr.setTarget(a3);
		System.out.println("Cas 2.A : "+tr.startIncrustation+" - "+tr.endIncrustation );

		a3 = CalculFormes.getExtrusion(a1b, a2, Decimal.UN);
		tr.setTarget(a3);
		System.out.println("Cas 2.B : "+tr.startIncrustation+" - "+tr.endIncrustation );

		a3 = CalculFormes.getExtrusion(a1c, a2, Decimal.UN);
		tr.setTarget(a3);
		System.out.println("Cas 2.C : "+tr.startIncrustation+" - "+tr.endIncrustation );

		a2 = new Area();
		a2.points.add(new Vecteur("7;4.5;1"));
		a2.points.add(new Vecteur("2;4.5;1"));
		a2.points.add(new Vecteur("2;4.2;1"));
		a2.points.add(new Vecteur("7;4.2;1"));

		a3 = CalculFormes.getExtrusion(a1, a2, Decimal.UN);
		tr.setTarget(a3);
		System.out.println("Cas 3.A : "+tr.startIncrustation+" - "+tr.endIncrustation );

		a3 = CalculFormes.getExtrusion(a1b, a2, Decimal.UN);
		tr.setTarget(a3);
		System.out.println("Cas 3.B : "+tr.startIncrustation+" - "+tr.endIncrustation );

		a3 = CalculFormes.getExtrusion(a1c, a2, Decimal.UN);
		tr.setTarget(a3);
		System.out.println("Cas 3.C : "+tr.startIncrustation+" - "+tr.endIncrustation );

	}
}


