package model.script;

import java.util.ArrayList;

import model.concepts.math.Axis;
import model.concepts.math.Decimal;
import model.concepts.math.Droite3D;
import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Matrix;
import model.concepts.math.Plan3D;
import model.concepts.math.Vecteur;
import model.concepts.solid.Bounds;
import model.concepts.transfo.MattrixTransformation;
import model.concepts.transfo.Transformation;
import model.concepts.transfo.Translation;
import model.script.actions.ActionOperation;
import model.concepts.math.Segment;
import model.concepts.math.Triangle;
import model.solid.manage.SolidPrintStream;


/**
 * Classe permettant de d�couper un triangle en plusieurs triangles selon son intersection avec un segement 
 * 
 * @author olemoyne
 *
 */
public class TriangleSplitter {
	
	
	private static void addTriangle (ArrayList<Triangle> lst, Transformation trans, Vecteur A, Vecteur B, Vecteur C) {
		try {
			Triangle tr = new Triangle (A, B, C);
//			Triangle ntr = tr.transforme(trans); 
			lst.add(tr);
		} catch (InvalidGeomAction e) {
//			System.err.println(e.getLocalizedMessage());
		}
	}
	
	/**
	 * Get Transformation from plan to bing it to origin
	 * 
	 * 
Re : Changement de repère dans l'espace

    Merci pour vos réponses.
    Finalement, après quelques recherches, je suis arrivé à la solution suivante:
    Si je prends Mg les coordonnées d'un point dans le repère (Ox, Oy, Oz), alors les coordonnées Ml de ce point dans le repère défini par (U, V, W) et un point d'origine C se trouvant sur le plan sont données par

    Ml = PInv.Mg

    Où PInv est l'inverse de la matrice
    Xu Xv Xw
    Yu Yv Yw
    Zu Zv Zw
    (la matrice de passage de (Ox, Oy, Oz) à (U, V, W))

    Pour obtenir les coordonnées 2D, il suffit de prendre les coordonnées pour les deux vecteurs appartenant au plan.
**/


	public static Transformation getTransformation (Plan3D pl) {
/*		Vecteur n = pl.getNorme(); // Taille = 1
		Decimal aX = n.getDecX().aSin();
		Decimal aZ = n.getDecZ().aSin(); */
		
		Matrix m = pl.getMatrix(); 
	
//		Translation transPil = new Translation (pl.getOrigine().negat(), null);
		MattrixTransformation trans = new MattrixTransformation (m, null);
		
//		Translation transPil = new Translation (pl.getOrigine().negat(), null);
//		Rotation rotx = new Rotation(Axis.XAxis, aX, transPil);
//		Rotation rotz = new Rotation(Axis.ZAxis, aZ, rotx);
		
		return trans;
	}

	/**
	 * Split le triangle slon les points du triangle
	 * 
	 * 
	 * @param tri
	 * @param seg
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public static ArrayList<Triangle> splitTriangle (Triangle tri, Segment seg, Transformation back, SolidPrintStream log) throws InvalidGeomAction {
		ArrayList<Triangle> ret = new ArrayList<Triangle>();

		if (tri == null) return ret;
		if (seg == null) {
			ret.add(tri); 
			return ret;
		}
		
/*		Scene sc = new Scene();
		sc.addForme(new Composant(tri, "Découpeur", Color.WHITE));
		sc.addForme(new Composant(seg, "A découper", Color.YELLOW));
		log.addScene(sc);
*/		
		// Liste des sommets
		Vecteur[] pts = tri.getSommets();

		Segment ab = new Segment(pts[0], pts[1]);
		Segment bc = new Segment(pts[1], pts[2]);
		Segment ca = new Segment(pts[2], pts[0]);
		// un seul point
		if (seg.getA().equals(seg.getB())) {
			Vecteur v = seg.getA();
			// v = sommet
			if ( (v.equals(pts[0]))||(v.equals(pts[1]))||(v.equals(pts[2]))) {
				ret.add(tri);
				return ret;
			}
			
			// Retourne 2 triangles
			if (ab.contient(v))  {
				addTriangle (ret, back, pts[0], v, pts[2]);
				addTriangle (ret, back, v, pts[1], pts[2]);
				log.print("a", SolidPrintStream.LEVEL_ALARM);
				return ret;
			}
			if (bc.contient(v))  {
				addTriangle (ret, back, pts[1], v, pts[0]);
				addTriangle (ret, back, v, pts[2], pts[0]);
				log.print("b", SolidPrintStream.LEVEL_ALARM);
				return ret;
			}
			if (ca.contient(v))  {
				addTriangle (ret, back, pts[2], v, pts[1]);
				addTriangle (ret, back, v, pts[0], pts[1]);
				log.print("c", SolidPrintStream.LEVEL_ALARM);
				return ret;
			}
			// Retourne 3 triangles
			addTriangle (ret, back, pts[0], pts[1], v);
			addTriangle (ret, back, pts[1], pts[2], v);
			addTriangle (ret, back, pts[2], pts[0], v);
			log.println("d "+tri.toString()+" - "+seg.toString(), SolidPrintStream.LEVEL_ALARM);
			return ret;			
		} else {
			if (seg.equals(ab)) { ret.add(tri.transforme(back)); return ret;	}
			if (seg.equals(bc)) { ret.add(tri.transforme(back)); return ret;	}
			if (seg.equals(ca)) { ret.add(tri.transforme(back)); return ret;	}
			Vecteur a = seg.getA();
			Vecteur b = seg.getB();
			ArrayList<Triangle> tris = splitTriangleSurSommet(back, pts[0], pts[1], pts[2], a, b);
			log.print("e", SolidPrintStream.LEVEL_ALARM);
			if (tris != null) { return tris; }
			tris = splitTriangleSurSommet(back, pts[1], pts[2], pts[0], a, b);
			if (tris != null) { return tris; }
			tris = splitTriangleSurSommet(back, pts[2], pts[0], pts[1], a, b);
			if (tris != null) { return tris; }
			
			
			// Arrete au milieu du triangle
			// D�coupe le triangle en trois, selon le prmier point (a)
			Triangle t1 = new Triangle(pts[0], pts[1], a);
			Triangle t2 = new Triangle(a, pts[1], pts[2]);
			Triangle t3 = new Triangle(pts[0], a, pts[2]);

			// Si b appartient � une des arretes de ces triangles
			Segment s = new Segment(a, pts[0]);
			if (s.contient(b)) {
				ret.add(t2.transforme(back));
				addTriangle (ret, back, pts[0], pts[1], b);
				addTriangle (ret, back, b, pts[1], a);
				addTriangle (ret, back, pts[0], b , pts[2]);
				addTriangle (ret, back, b, a, pts[2]);
				log.print("f", SolidPrintStream.LEVEL_ALARM);
				return ret;
			}
			s = new Segment(a, pts[1]);
			if (s.contient(b)) {
				ret.add(t3.transforme(back));
				addTriangle (ret, back, pts[0], pts[1], b);
				addTriangle (ret, back, b, a, pts[0]);
				addTriangle (ret, back, pts[1], pts[2], b);
				addTriangle (ret, back, b, pts[2], a);
				log.print("g", SolidPrintStream.LEVEL_ALARM);
				return ret;
			}
			s = new Segment(a, pts[2]);
			if (s.contient(b)) {
				ret.add(t1.transforme(back));
				addTriangle (ret, back, pts[0], b, pts[2]);
				addTriangle (ret, back, b, pts[0], a);
				addTriangle (ret, back, pts[1], pts[2], b);
				addTriangle (ret, back, a, pts[1], b);
				log.print("h", SolidPrintStream.LEVEL_ALARM);
				return ret;
			}
			// Le point b n'est pas sur une arrete
			if (t1.contient(b)) {
				ret.add(t2.transforme(back));
				ret.add(t3.transforme(back));
				addTriangle (ret, back, pts[0], b, a);
				addTriangle (ret, back, pts[0], pts[1], b);
				addTriangle (ret, back, pts[1], a, b);
				log.print("i", SolidPrintStream.LEVEL_ALARM);
				return ret;
			}
			if (t2.contient(b)) {
				ret.add(t1.transforme(back));
				ret.add(t3.transforme(back));
				addTriangle (ret, back, pts[1], b, a);
				addTriangle (ret, back, pts[1], pts[2], b);
				addTriangle (ret, back, pts[2], a, b);
				log.print("j", SolidPrintStream.LEVEL_ALARM);
				return ret;
			}
			if (t3.contient(b)) {
				ret.add(t1.transforme(back));
				ret.add(t3.transforme(back));
				addTriangle (ret, back, pts[2], b, a);
				addTriangle (ret, back, pts[2], pts[0], b);
				addTriangle (ret, back, pts[0], a, b);
				log.print("k", SolidPrintStream.LEVEL_ALARM);
				return ret;
			}
			throw new InvalidGeomAction ("Situation de triangle inconnue "+tri.toString()+" "+seg.toString());
		}
	}
	
	/**
	 * Effectue le split du triangle vu du sommet A et du cot� AB
	 * @param A --> triangle.A
	 * @param B --> triangle.B
	 * @param C --> triangle.C
	 * @param a --> segment.A
	 * @param b --> segment.B
	 * 
	 * @return
	 * @throws InvalidGeomAction 
	 */
	private static ArrayList<Triangle> splitTriangleSurSommet (Transformation tr, Vecteur A, Vecteur B, Vecteur C, Vecteur a, Vecteur b) throws InvalidGeomAction {
		ArrayList<Triangle> ret = null;
		// Teste le sommet A
		if ((a.equals(B))||(a.equals(C))||(b.equals(B))||(b.equals(C))) return ret;
		
		ret = splitSommet(tr, A, B, C, a, b);
		if (ret != null) return ret;
		// Teste l'arrete AB
		ret = splitArrete(tr, A, B, C, a, b);
		if (ret != null) return ret;
		// Teste l'arrete AC
		ret = splitArrete(tr, A, B, C, b, a);
		if (ret != null) return ret;
		return null;
	}
	
	
	/**
	 * Calcule les triangles de split sur le sommet M
	 * 
	 * @param A --> triangle.A
	 * @param B --> triangle.B
	 * @param C --> triangle.C
	 * @param a --> segment.A
	 * @param b --> segment.B
	 * @return
	 * @throws InvalidGeomAction
	 */
	private static ArrayList<Triangle> splitSommet (Transformation tr, Vecteur m, Vecteur n, Vecteur p, Vecteur a, Vecteur b) throws InvalidGeomAction {
		ArrayList<Triangle> ret = new ArrayList<Triangle> ();

		Segment ab = new Segment(m, n);
		Segment bc = new Segment(n, p);
		Segment ca = new Segment(p, m);
		// segment sur un sommet --
		if (m.equals(a)) {
			if (ab.contient(b)) {
				addTriangle (ret, tr, m, b, p);
				addTriangle (ret, tr, b, n, p);
				return ret;
			}
			if (ca.contient(b)) {
				addTriangle (ret, tr, m, n, b);
				addTriangle (ret, tr, n, b, p);
				return ret;
			}
			if (bc.contient(b)) {
				addTriangle (ret, tr, m, n, b);
				addTriangle (ret, tr, b, p, m);
				return ret;
			}
			// b n'est pas un sommet 
			if ((!n.equals(b)) && (!p.equals(b))) {
				addTriangle (ret, tr, m, n, b);
				addTriangle (ret, tr, n, p, b);
				addTriangle (ret, tr, p, m, b);
				return ret;
			}
			return ret;
		}
		return null;
	}


	/**
	 * Calcule les triangles de split sur le sommet M
	 * 
	 * @param A --> triangle.A
	 * @param B --> triangle.B
	 * @param C --> triangle.C
	 * @param a --> segment.A
	 * @param b --> segment.B
	 * @return
	 * @throws InvalidGeomAction
	 */
	private static ArrayList<Triangle> splitArrete (Transformation tr, Vecteur A, Vecteur B, Vecteur C, Vecteur a, Vecteur b) throws InvalidGeomAction {
		ArrayList<Triangle> ret = new ArrayList<Triangle> ();
		
		Segment ab = new Segment(A, B);
		Segment bc = new Segment(B, C);
		Segment ca = new Segment(C, A);
		// Segment sur une arrete
		if (ab.contient(a)) {
			//    les deux points sur la m�me arrete
			if (ab.contient(b)) {
				Segment s = new Segment (A, a);
				if (s.contient(b)) {
					addTriangle (ret, tr, A, b, C);
					addTriangle (ret, tr, b, a, C);
					addTriangle (ret, tr, a, B, C);
				} else {
					addTriangle (ret, tr, A, a, C);
					addTriangle (ret, tr, a, b, C);
					addTriangle (ret, tr, b, B, C);
				}
				return ret;
			} else {
				//    un point sur deux arretes diff�rentes
				if (bc.contient(b)) {
					addTriangle (ret, tr, A, a, C);
					addTriangle (ret, tr, a, b, C);
					addTriangle (ret, tr, a, B, b);
					return ret;
				}					
				if (ca.contient(b)) {
					addTriangle (ret, tr, A, a, b);
					addTriangle (ret, tr, a, C, b);
					addTriangle (ret, tr, a, B, C);
					return ret;
				}
				//    un seul point sur l'arrete
				addTriangle (ret, tr, A, a, b);
				addTriangle (ret, tr, a, B, b);
				addTriangle (ret, tr, b, B, C);
				addTriangle (ret, tr, b, C, A);
				return ret;		
			}
		}
		return null;
	}

	
	// Si tagl est inclu dans tr --> F 6 facettes � construire
	// Construction it�rative -> pour chaque cot� du triangle 

	/**
	 * split de deux triangles complanaires
	 * 
	 * @param tagl
	 * @param tr
	 * @return
	 * @throws InvalidGeomAction
	 */
	public static ArrayList<Triangle> splitTriangleCoplanaire(Triangle tagl, Triangle tr, Transformation back, SolidPrintStream log) throws InvalidGeomAction {
		ArrayList<Triangle> ret = new ArrayList<Triangle> ();
		if (!tagl.estColplanaire(tr)) {
			ret.add(tagl);
			return ret;
		}
		
		Vecteur[] pts = tr.getSommets();
		Droite3D drt = new Droite3D(pts[1].minus(pts[0]), pts[0]);
		Segment s = tagl.intersection(drt);
		ArrayList<Triangle> tmp = splitTriangle(tagl, s, null, log);
		// Seconde arrete
		ArrayList<Triangle> tmp2 = new ArrayList<Triangle> ();
		for (Triangle tri : tmp) {
			s = tagl.intersection(new Droite3D(pts[2].minus(pts[1]), pts[1]));
			if (s != null) {
				tmp2.addAll(splitTriangle(tri, s, null, log));
			} else tmp2.add(tri);
		}
		// troisieme arrete
		for (Triangle tri : tmp2) {
			s = tagl.intersection(new Droite3D(pts[2].minus(pts[1]), pts[1]));
			if (s != null) {
				ret.addAll(splitTriangle(tri, s, back, log));			
			} else ret.add(tri);
		}
		return ret;
	}
	
	
	
	public static ArrayList<Triangle> splitTriangle (Triangle tri, Triangle tagl, SolidPrintStream ps) throws InvalidGeomAction {
		try {
			ArrayList<Triangle> ret = null;
			ps.print(".", SolidPrintStream.LEVEL_ALARM);
			if (tagl.estColplanaire(tri)) { // Si les deux triangles sont dans le m�me plan
				ps.print("+", SolidPrintStream.LEVEL_ALARM);
				ret = TriangleSplitter.splitTriangleCoplanaire(tagl, tri, null, ps);
			} else {
					Segment seg = tagl.intersection(tri);
					// Split du triangle 
					ret = TriangleSplitter.splitTriangle(tagl, seg, null, ps);	
					if (ret.size() > 1) {
//						done = true;
						ps.print("°", SolidPrintStream.LEVEL_ALARM);
					}
//						ps.println("From triangle "+tagl.toString()+" "+add.size()+" "+add.toString(), SolidPrintStream.LEVEL_DEBUG);
					if ((ret == null) || (ret.size() == 0)) 
						throw new InvalidGeomAction ("Split tris - Error in splitting triangle - "+tri.toString()+" "+tagl.toString());
			}						
			/** Identifie les éléments du triangle au dessus des autres  **/
   			Plan3D pl = tri.getPlan();
			for (Triangle t : ret) {
				if (t.getInside() != -1) {
					Vecteur[] pts = t.getSommets();
					int d0 = pl.estDessus(pts[0]);
					int d1 = pl.estDessus(pts[1]);
					int d2 = pl.estDessus(pts[2]);
					
					if (d0+d1+d2 == 0) {
						t.setInside(0);
					} else {
						if (d0 < 0) {
							t.setInside(-1);
						} else {
							if (d1 < 0) {
								t.setInside(-1);
							} else {
								if (d2 < 0) {
									t.setInside(-1);
								} else {
									t.setInside(1);
								}
							}
						}
					}
				}
			}				
			return ret;
		} catch (InvalidGeomAction iga) {
			ps.println("Err : "+iga.getLocalizedMessage(), SolidPrintStream.LEVEL_ALARM);
			System.exit(12);
		}
		return null;
	}
	
	/**
	 * Split une liste de triangles par un autre triangle
	 * @param tris
	 * @param tr
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public static boolean split(ArrayList<Triangle> tris, ArrayList<Triangle> ret, Triangle tri, SolidPrintStream ps) throws InvalidGeomAction {
		ArrayList<Triangle> splitted = new ArrayList<Triangle>(); 
		// Exécute la transformation
		if (tri == null) return false;
		
		Transformation back = new Translation (tri.getSommets()[0], null);
		Transformation trans = back.getReverse(null);
		
		Triangle tr = tri.transforme(trans);
		Plan3D pl = tr.getPlan();
		
		boolean done = false;

		// Parcours la liste des triangles du solide pour d�finir ceux impact�s et split les triangles si n�cessaire.
		Bounds zone = tr.getBounds();
//		ps.println("Cutting triangle "+tr.toString(), SolidPrintStream.LEVEL_DEBUG);
		for (Triangle tgl : tris) {
			Triangle tagl = tgl.transforme(trans);
			// Verifie que les zones des deux triangles sont en intersection
			if (tagl.getBounds().intersecs(zone)) {
				// Ajoute la scene à voir
				done = true;
				try {
					ps.print(".", SolidPrintStream.LEVEL_ALARM);
					ArrayList<Triangle> add = null;
					if (tagl.estColplanaire(tr)) { // Si les deux triangles sont dans le m�me plan
						ps.print("+", SolidPrintStream.LEVEL_ALARM);
						add = TriangleSplitter.splitTriangleCoplanaire(tagl, tr, null, ps);
					} else {
						Segment seg = tagl.intersection(tr);

						// Split du triangle 
						add = TriangleSplitter.splitTriangle(tagl, seg, null, ps);	
						if (add.size() > 1) {
//							done = true;
							ps.print("°", SolidPrintStream.LEVEL_ALARM);
						}
//							ps.println("From triangle "+tagl.toString()+" "+add.size()+" "+add.toString(), SolidPrintStream.LEVEL_DEBUG);
						if ((add == null) || (add.size() == 0)) 
							throw new InvalidGeomAction ("Split tris - Error in splitting triangle - "+tr.toString()+" "+tagl.toString());
					}						
					if (add != null) splitted.addAll(add);
				} catch (InvalidGeomAction iga) {
					ps.println("Err : "+iga.getLocalizedMessage(), SolidPrintStream.LEVEL_ALARM);
					System.exit(12);
				}
			} else {
				splitted.add(tagl);
			}
			if (done) {
				/** Identifie les éléments du triangle au dessus des autres  **/
				for (Triangle t : splitted) {
					if (t.getInside() != -1) {
						Vecteur[] pts = t.getSommets();
						int d0 = pl.estDessus(pts[0]);
						int d1 = pl.estDessus(pts[1]);
						int d2 = pl.estDessus(pts[2]);
						
						if (d0+d1+d2 == 0) {
							t.setInside(0);
						} else {
							if (d0 < 0) {
								t.setInside(-1);
							} else {
								if (d1 < 0) {
									t.setInside(-1);
								} else {
									if (d2 < 0) {
										t.setInside(-1);
									} else {
										t.setInside(1);
									}
								}
							}
						}
					}
				}				
			}
		}

		if (done) {
			Bounds b = ActionOperation.getBounds(splitted);
			ps.println("The Bounds = "+b.toString(), SolidPrintStream.LEVEL_ALARM);
			for (Triangle t : splitted) {
				ret.add(t.transforme(back));
			}
			b = ActionOperation.getBounds(ret);
			ps.println("After Bounds = "+b.toString(), SolidPrintStream.LEVEL_ALARM);
		}
/**
		if (ps.getLevel() == SolidPrintStream.LEVEL_ALARM) {
			Scene sc = new Scene();
			sc.addForme(new Composant(tri, "A découper", Color.YELLOW));
			for (Triangle t : ret) {
				if (t.getInside() == -1) {
					sc.addForme(new Composant(t, "Découpeur", Color.GRAY));
				} else {
					sc.addForme(new Composant(t, "Découpeur", Color.RED));
				}
			}
			ps.addScene(sc);
		}
**/
		return done;
	}


	/**
	 * Split une liste de triangles par un plan
	 * @param tris
	 * @param tr
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public static ArrayList<Triangle> split(Triangle tagl, Plan3D plan, SolidPrintStream ps) throws InvalidGeomAction {
		try {
			ArrayList<Triangle> splitted = TriangleSplitter.splitTriangle(tagl, plan);	
//			if (splitted.size() > 1)
//				ps.println("From triangle "+tagl.toString()+" "+splitted.size()+" "+splitted.toString(), SolidPrintStream.LEVEL_ALARM);
			if (splitted  == null)  
				throw new InvalidGeomAction ("Split plan - Error in splitting triangle - "+plan.toString()+" "+tagl.toString());
			return splitted;			
		} catch (InvalidGeomAction iga) {
			ps.println("Err : "+iga.getLocalizedMessage(), SolidPrintStream.LEVEL_ALARM);
			System.exit(12);
		}
		return null;
	}

	/** un plan non colliuneaire peut couper un triangle en : 
	  - deux triangles si un des sommets appartient au plan
	  - trois triangles sinon
	**/
	private static ArrayList<Triangle> splitTriangle(Triangle tri, Plan3D plan) {
		ArrayList<Triangle> ret = new ArrayList<Triangle>();

		if (tri == null) return ret;
		if (plan == null) { ret.add(tri); return ret; }
		
		Transformation tr = new Translation(new Vecteur(Decimal.ZERO, Decimal.ZERO, Decimal.ZERO), null); 

		// Liste des sommets
		Vecteur[] pts = tri.getSommets();

		// Verifie les sommets
		if (plan.contains(pts[0])) {
			if (plan.contains(pts[1])) {ret.add(tri); return ret;}
			if (plan.contains(pts[2])) {ret.add(tri); return ret;}
			Vecteur pt = plan.intersection(pts[1], pts[2]);
			if (pt == null) {ret.add(tri); return ret;}
			addTriangle(ret, tr, pts[0], pts[1], pt);
			addTriangle(ret, tr, pts[0], pt, pts[2]);
			return ret;
		}
		if (plan.contains(pts[1])) {
			if (plan.contains(pts[2])) {ret.add(tri); return ret;}
			Vecteur pt = plan.intersection(pts[2], pts[0]);
			if (pt == null) {ret.add(tri); return ret;}
			addTriangle(ret, tr, pts[1], pts[2], pt);
			addTriangle(ret, tr, pts[1], pt, pts[0]);
			return ret;
		}
		if (plan.contains(pts[2])) {
			Vecteur pt = plan.intersection(pts[0], pts[1]);
			if (pt == null) {ret.add(tri); return ret;}
			addTriangle(ret, tr, pts[2], pts[0], pt);
			addTriangle(ret, tr, pts[2], pt, pts[1]);
			return ret;
		}
		
		// V�rifie les segements --> Trois points
		Vecteur pt = plan.intersection (pts[0], pts[1]);
		if (pt != null) {
			Vecteur sec = plan.intersection (pts[1], pts[2]);
			if (sec != null) {
				addTriangle(ret, tr, pts[1], sec, pt);
				addTriangle(ret, tr, pts[0], pt, sec);
				addTriangle(ret, tr, pts[0], sec, pts[2]);
				return ret;
			} 
			sec = plan.intersection (pts[2], pts[0]);
			if (sec != null) {
				addTriangle(ret, tr, pts[0], pt, sec);
				addTriangle(ret, tr, pt, pts[1], sec);
				addTriangle(ret, tr, pts[1], pts[2], sec);
				return ret;
			} 
		}

		pt = plan.intersection (pts[1], pts[2]);
		if (pt != null) {
			Vecteur sec = plan.intersection (pts[2], pts[0]);
			if (sec != null) {
				addTriangle(ret, tr, pts[1], pt, sec);
				addTriangle(ret, tr, pts[0], pts[1], sec);
				addTriangle(ret, tr, pt, pts[2], sec);
				return ret;
			} 
		}
		
		ret.add(tri);
		return ret;
	}
	/**
	 * Test de split de triangle
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SolidPrintStream log = new SolidPrintStream(System.out, SolidPrintStream.LEVEL_ALARM);

			Triangle t1 = Triangle.getTriangle("(68,63,-922);(46,42,341);(8,-117,341)");
			Triangle t2 = Triangle.getTriangle("(0,0,0);(-6,1,658);(146,1,658)");
			
			Segment s = t1.intersection(t2);
			
			
			/** ex�cution de test unitaires sur des triangles **/
			Triangle tri = Triangle.getTriangle("(-0.043,0.0291,4.4218);(-0.0409,0.029,4.4497);(-0.0409,0.029,4.4499)");
//			Plan3D pli = tri.getPlan();
						
			Plan3D plg = Plan3D.getPlan(Axis.ZAxis, 44497l);

			splitTriangle(tri, plg);
			
			Segment seg = tri.intersection(plg);
			if (seg == null) log.println("pas d'intersection", SolidPrintStream.LEVEL_ALARM);
			else log.println("Intersection "+seg.toString(), SolidPrintStream.LEVEL_ALARM);
			
//	Erreur à identifier : 		[(2;-65;-1263);(0;-2;0);(0;-1;0)] [(0.3333;-12.5;-208.83);(0.1818;-6.8182;-113.91)]
//  Situation de triangle inconnue [(2044.78;-932.132;6351);(4756.2;4000.8;811606);(1183.7;-1605.7;0)] [(-1025600;48871;0);(-1025615;0;0)]
			
			Triangle tr = Triangle.getTriangle("(2044.78,-932.132,6351);(4756.2,4000.8,811606);(1183.7,-1605.7,0)");
			Segment v = new Segment(new Vecteur("-1025600;48871;0"), new Vecteur("-1025615;0;0"));
			
			@SuppressWarnings("unused")
			ArrayList<Triangle> arr  = TriangleSplitter.splitTriangle(tr, v, null, log);
/**			
			
			Triangle tri = new Triangle(new Vecteur ("1;0;0"), new Vecteur ("1;1;1"), new Vecteur ("1;0;1"));
			Triangle tagl = new Triangle(new Vecteur ("1;0.5;0.5"), new Vecteur ("0.5;0.5;0.5"), new Vecteur ("1;1;0.5"));
			
			Segment seg = tagl.intersection(tri);
			System.out.println("Intersection triangle : "+seg.toString()+ " Must be [(1,0.5,0.5);(1,0.5,0.5)]");
			
			tri = new Triangle(new Vecteur ("0;1;0"), new Vecteur ("1;1;1"), new Vecteur ("1;1;0"));
			tagl = new Triangle(new Vecteur ("0.6;0.6;0.6"), new Vecteur ("0.6;1;0.6"), new Vecteur ("1.6;1.6;0.6"));
			
			seg = tagl.intersection(tri);
			System.out.println("Intersection triangle : "+seg.toString()+ " Must be [(0.6,1,0.6);(1,1,0.6)]");
			
			tri = new Triangle(new Vecteur ("0;0;1"), new Vecteur ("1;0;1"), new Vecteur ("1;1;1"));
			tagl = new Triangle(new Vecteur ("1.5;0.5;1.5"), new Vecteur ("0.5;0.5;1.5"), new Vecteur ("0.5;0.5;0.5"));
			
			seg = tagl.intersection(tri);
			if (seg != null)
				System.out.println("Intersection triangle  2: "+seg.toString());
			else System.out.println("Intersection triangle  2: null");

			tri = new Triangle(new Vecteur ("0;0;1"), new Vecteur ("1;0;1"), new Vecteur ("1;1;1"));
			tagl = new Triangle(new Vecteur ("1.8;0.6;1.6"), new Vecteur ("0.8;0.6;1.6"), new Vecteur ("0.8;0.6;0.6"));
			
			seg = tagl.intersection(tri);
			if (seg != null)
				System.out.println("Intersection triangle  3: "+seg.toString());
			else System.out.println("Intersection triangle  3: null");

			ArrayList<Triangle> trs = TriangleSplitter.splitTriangle(tagl, seg);//
			System.out.println("Intersection simple : "+trs.size());
			
			Solide sol = SolideMaker.getPave(new Vecteur ("1;1;1"), new Vecteur ("0.8;0.8;0.6"));
			trs = TriangleSplitter.split(sol.getFacettes(), tri, log);
			System.out.println("Intersection solide : "+trs.size());
			
			Solide sol2 = SolideMaker.getPave(new Vecteur ("1;1;1"), new Vecteur ("0;0;0"));
			ActionFusion fus = new ActionFusion();
			sol = fus.execOperation(sol, sol2, log);

			tri = new Triangle (new Vecteur("0;0.2;0"), new Vecteur("0.119998;0.2;0"), new Vecteur("0.125099;0.19959;0.13063"));
			tagl = new Triangle(new Vecteur ("0;-1;0.01"), new Vecteur ("1;1;0.01"), new Vecteur ("0;1;0.01"));
			
			seg = tagl.intersection(tri);
			if (seg != null)
				System.out.println("Intersection triangle  3: "+seg.toString());
			else System.out.println("Intersection triangle  3: null");
*/			
			

		
		} catch (InvalidGeomAction e) {
			e.printStackTrace();
		}
	}
}
