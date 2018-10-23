package model.script.actions.operation;

import java.util.ArrayList;

import model.concepts.math.Axis;
import model.concepts.math.Decimal;
import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Plan3D;
import model.concepts.math.Triangle;
import model.concepts.math.Vecteur;
import model.concepts.solid.Solide;
import model.script.SolideReader;
import model.script.TriangleSplitter;
import model.script.actions.ActionFusion;
import model.script.actions.ActionOperation;
import model.solid.manage.InvalidSolidException;
import model.solid.manage.STLFile;
import model.solid.manage.SolidPrintStream;

/**
 * Ex�cute une op�ration en utilisant 8 zones pour sous-d�couper les solides. 
 * @author olemoyne
 *
 */
public class ActionSplittedOperation extends ActionOperation {

	
	/**
	 * Ex�cute l'operation demand�e **
	 */
	@Override
	public Solide execOperation(Solide from, Solide to, SolidPrintStream log)
			throws InvalidGeomAction {
		
		long start = System.currentTimeMillis();
		
		long sze = (from.getFacettes().size() + to.getFacettes().size()) *2;
		
		Partition root = new Partition (from.getFacettes(), to.getFacettes(), sze);
		partitionning (root, log);
		
//		root.printTree(log, 0);
//		int nb = root.count();
//		log.println("Nombre d'op�rations = "+nb, SolidPrintStream.LEVEL_ALARM);

		// Recup�re la liste des partitions
		ArrayList<SolidPartition> liste = new ArrayList<SolidPartition>();
		root.getPartitions(liste);
		
		root = null;
		System.gc();

		int nb = liste.size();
		log.println("Nombre d'op�rations = "+nb, SolidPrintStream.LEVEL_ALARM);

		ActionFusion fus = new ActionFusion();
		ArrayList<Triangle> ret = new ArrayList<Triangle>();
		for (SolidPartition part : liste) {
			ret.addAll(part.getOperation(fus, log));
			log.printChrono(SolidPrintStream.LEVEL_ALARM);
			log.println("Reste � faire = "+(nb--)+" "+ret.size(), SolidPrintStream.LEVEL_ALARM);
		}

		long end = System.currentTimeMillis();
		log.println("Temps total de traitement = "+(start-end), SolidPrintStream.LEVEL_ALARM);

		return new Solide(ret);
	}
	
	
	/** Execute le partitionning r�cursif d'une partition 
	 * @throws InvalidGeomAction **/
	private void partitionning (Partition root, SolidPrintStream log) throws InvalidGeomAction {
		log.startChrono();

		log.printChrono(SolidPrintStream.LEVEL_ALARM);
		log.println("Taille - "+root.from.size() + " - " + root.to.size(), SolidPrintStream.LEVEL_ALARM);
		
		// Check si l'�clatement est n�cessaire
		if (root.checkPartitionNeeded()) {
			log.printChrono(SolidPrintStream.LEVEL_ALARM);
			log.println("Partitionning", SolidPrintStream.LEVEL_ALARM);

			/** Calcul des diff�rentes zones du solide **/
			root.centre = getCentre(root.from, root.to);

			long sze = (root.from.size() + root.to.size());

			log.printChrono(SolidPrintStream.LEVEL_ALARM);
			log.println("Centre - "+root.centre.toString(), SolidPrintStream.LEVEL_ALARM);

			// Eclate la partition en plusieurs morceaux
			ArrayList<ArrayList<Triangle>> froms = splitPartition(root.from, root.centre, log);
			ArrayList<ArrayList<Triangle>> tos = splitPartition(root.to, root.centre, log);
			
			// Verifie si les partitions resultantes doivent �tre �clat�es
			for (int i = 0; i < 8 ; i++) {
				Partition prt = new Partition(froms.get(i), tos.get(i), sze);
				root.subs[i] = prt;
				partitionning(prt, log);
			}
		}
			
			// Toutes les partitions sont constitu�es
	}


	/**
	 * Eclate un solide sur les paroies centrales de la zone :
	 *      Translation des formes
	 *      calcul des paroies
	 * 		split des triangles du solide
	 *      Tri des triangles selon les sous-formes
	 * 
	 * @param from
	 * @param log
	 * @return
	 * @throws InvalidGeomAction 
	 */
	private ArrayList<ArrayList<Triangle>>  splitPartition(ArrayList<Triangle> part, Vecteur centre, SolidPrintStream log) throws InvalidGeomAction {
		
		ArrayList<ArrayList<Triangle>> alls = new ArrayList<ArrayList<Triangle>>();
		alls.add(part);
		
		//d�coupe l'espace en deux � l'aide d'un plan : X =0
		ArrayList<ArrayList<Triangle>> iteration = new ArrayList<ArrayList<Triangle>>();
		for (ArrayList<Triangle> tris : alls) {
			decoupeEspaces(tris, Axis.XAxis, centre, iteration, log);
		}
		alls = iteration;

		//d�coupe l'espace en deux � l'aide d'un plan : Y =0
		iteration = new ArrayList<ArrayList<Triangle>>();
		for (ArrayList<Triangle> tris : alls) {
			decoupeEspaces(tris, Axis.YAxis, centre, iteration, log);
		}
		alls = iteration;

		//d�coupe l'espace en deux � l'aide d'un plan : Z =0
		iteration = new ArrayList<ArrayList<Triangle>>();
		for (ArrayList<Triangle> tris : alls) {
			decoupeEspaces(tris, Axis.ZAxis, centre, iteration, log);
		}
		alls = iteration;

		return alls;
	}
	
	
	// 1 --> Positif, -1 --> negatif, 0 --> ind�termin�
	private int getCoteTriangle (Triangle tr, Plan3D pl, int axis) {
		boolean[] statuts = new boolean[3];
		Vecteur[] pts = tr.getSommets();
		statuts[0] = (pl.donneCote(pts[0]) > 0);
		statuts[1] = (pl.donneCote(pts[1]) > 0);
		statuts[2] = (pl.donneCote(pts[2]) > 0);
		// Regarde si tous les points sont dans le m�me cot�
		if ((statuts[0] == statuts[1]) && (statuts[1] == statuts[2])) {
			if (statuts[0]) return 1;
			else return -1;
		} else {
			return 0;
		}
		
	}
	
	// D�coupe une liste de triangle pour les repartir en deux groupes.
	// Les triangles � la limite sont d�coup�s.

	private void decoupeEspaces(ArrayList<Triangle> tris, int axis, Vecteur pt,
			ArrayList<ArrayList<Triangle>> iteration, SolidPrintStream log) throws InvalidGeomAction {
		ArrayList<Triangle> positifs = new ArrayList<Triangle>();
		ArrayList<Triangle> negatifs = new ArrayList<Triangle>();
		
		Plan3D pl = Plan3D.getPlan(axis, pt.get(axis));
		
		for (Triangle tr : tris) {
			int pos = this.getCoteTriangle(tr, pl, axis);
			if (pos > 0) positifs.add(tr);
			if (pos < 0) negatifs.add(tr);
			if (pos == 0) { // Sur les deux cot�s 
				ArrayList<Triangle> arr = TriangleSplitter.split(tr, pl, log);
				for (Triangle tri : arr ){
					pos = this.getCoteTriangle(tri, pl, axis);
					if (pos > 0) positifs.add(tri);
					if (pos < 0) negatifs.add(tri);
					if (pos == 0) negatifs.add(tri);
//						throw new InvalidGeomAction("Triangle mal d�coup� sur le plan"); 
				}
			}
		}
		
		// Ajoute les negatifs & positifs dans le resultat
		iteration.add(positifs);
		iteration.add(negatifs);
	}

	private Vecteur getCentre(ArrayList<Triangle> from, ArrayList<Triangle> to) {
		long x = 0;
		long y = 0;
		long z = 0;
		
		int nb = 0;
		for (Triangle tri : from) {
			for (Vecteur v : tri.getSommets()) {
				x = x+v.getX(); y = y+v.getY(); z = z+v.getZ();
				nb ++;
			}
		}
		for (Triangle tri : to) {
			for (Vecteur v : tri.getSommets()) {
				x = x+v.getX(); y = y+v.getY(); z = z+v.getZ();
				nb ++;
			}
		}
		
		Decimal div = new Decimal(nb);
		return new Vecteur (new Decimal(x).divide(div).divide(Vecteur.METER), new Decimal(y).divide(div).divide(Vecteur.METER), new Decimal(z).divide(div).divide(Vecteur.METER));
	}

	
	/**
	 * Test de l'op�ration
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Solide droite= SolideReader.readSolide("PATCH:100:0.4;0.2;0:0.2;0.1;0:0.1;-0.08;0:0.05;-0.1;0:0.8;0.2;0.75:0.28;0.12;0.75:0.2;-0.25;0.75:0.08;-0.32;0.75:0.05;0.15;3:0.2;0.08;3:0.1;-0.12;3:0.02;-0.4;3:0.004;0.1;5:0.004;0;5:0.004;-0.001;5:0.004;-0.22;5");
			Solide gauche = SolideReader.readSolide("PATCH:100:-0.4;0.2;0:-0.2;0.1;0:-0.1;-0.08;0:-0.05;-0.1;0:-0.8;0.2;0.75:-0.28;0.12;0.75:-0.2;-0.25;0.75:-0.08;-0.32;0.75:-0.05;0.15;3:-0.2;0.08;3:-0.1;-0.12;3:-0.02;-0.4;3:-0.004;0.1;5:-0.004;0;5:-0.004;-0.001;5:-0.004;-0.22;5");
//			Solide droite= SolideReader.readSolide("PAVE:2:1;1;1:0;0;0");
//			Solide gauche = SolideReader.readSolide("PAVE:2:1;1;1:0.5;0.5;0.5");
			ActionSplittedOperation ope = new ActionSplittedOperation();
			Solide sol = ope.execOperation(droite, gauche, new SolidPrintStream(System.out, SolidPrintStream.LEVEL_WARNING) );
			STLFile fle = new STLFile(STLFile.SCALE_METER);
			fle.createBinaryFile("D:/dev/code/model3D/data/resultat.stl", sol, "Test", null);
		} catch (InvalidGeomAction e) {
			System.err.println(e.getLocalizedMessage());
		} catch (InvalidSolidException e) {
			e.printStackTrace();
		}
	}

	
}
