package model.calcul;

import model.math.Vecteur;

public class Intersection {
		public int myPos; // Position du point avant l'intersection sur l'aire principale
		public int hisPos; // Position du point avant l'intersection sur l'aire secondaire
		public Vecteur inter; // Point d'intersection entre les deux zones

		public Intersection () {
			
		}

		/**
		 * Contruction d'une intersection entre deux formes
		 * 
		 * @param my
		 * @param his
		 * @param in
		 */
		public Intersection (int my, int his, Vecteur in) {
			myPos = my;
			hisPos = his;
			inter = in;
		}
		
		public String toString () {
			StringBuffer sb = new StringBuffer();
			sb.append(myPos);
			sb.append("-");
			sb.append(hisPos);
			sb.append("-");
			sb.append(inter.toString());
			return sb.toString();
		}
}
