package model.script.actions.operation;

import java.util.ArrayList;

import model.concepts.math.Triangle;
import model.concepts.math.Vecteur;
import model.solid.manage.SolidPrintStream;

/**
 * Partition de l'espace 
 * 
 * @author olemoyne
 *
 */
public class Partition extends SolidPartition{
	
	private static final int MAX_SIZE = 1000;

	private long lastSize; // de la forme dans l'espace supérieur
	
	/** Liste des sous espaces inclus dans cet espace**/
	Partition[] subs;
	
	Vecteur centre;
	
	public Partition (ArrayList<Triangle> f, ArrayList<Triangle>  t, long l) {
		from = f;
		to = t;
		
		lastSize = l;
		
		subs = new Partition[8];
	}

	/**
	 * un partition est nécessaire si un des deux membres contient plus de MAX triangles
	 * @return
	 */
	public boolean checkPartitionNeeded() {
		if (this.from.size() > MAX_SIZE) return true; 
		if (this.to.size() > MAX_SIZE) return true; 
		
		if (this.from.size() + this.to.size() >= this.lastSize) return false;
		return false;
	}
	
	public boolean hasSubs() {
		for (int p = 0; p < 8; p++) if (subs[p] != null) return true;
		return false;
	}

	/** 
	 * Recupère une liste de partitions pour effectuer les opérations 
	 * @param parties
	 */
	public void getPartitions (ArrayList<SolidPartition> parties) {
		// Pas de subs --> opération sur les deux listes de Triangles
		if (!this.hasSubs()) {
			SolidPartition part = new SolidPartition();
			part.from = this.from;
			part.to = this.to;
			parties.add(part);
		} else {
			// Parcours la liste des sous partitions pour déterminer les parties
			for (int pos = 0; pos < 8; pos ++) {
				if (subs[pos] != null) {
					subs[pos].getPartitions(parties);
				}
			}
		}		
	}
	
	/** Affiche la hiérarchie des espaces **/
	public void printTree(SolidPrintStream log, int level) {
		log.print(level, SolidPrintStream.LEVEL_ALARM);
		log.print(" ", SolidPrintStream.LEVEL_ALARM);
		for (int i = 0; i < level; i++) log.print("\t", SolidPrintStream.LEVEL_ALARM);
		log.println(this.from.size()+" + "+this.to.size(), SolidPrintStream.LEVEL_ALARM);

		if (this.hasSubs()) {
			if (centre != null)
				log.println(" - Centre = "+centre.toString(), SolidPrintStream.LEVEL_ALARM);

			for (int i = 0; i < 8; i++) {
				if (this.subs[i] != null) subs[i].printTree(log, level+1);
			}
		}
	}
	
	public int count() {
		int pos = 0;
		
		if (this.hasSubs()) {
			for (int i = 0; i < 8; i++) {
				if (this.subs[i] != null) pos += subs[i].count();
			}
		} else return 1;
		
		return pos;
	}
}
