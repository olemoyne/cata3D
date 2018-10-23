package model.concepts.btree;

import java.util.ArrayList;

import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Triangle;
import model.concepts.math.Vecteur;
import model.concepts.solid.Bounds;
import model.concepts.solid.Solide;
import model.concepts.solid.SolideMaker;

/** Application du princide de Partitionning en binary tree
 * 
 * @author olemoyne
 *
 */
public class SolidBinaryTree {
	
	/**
	 *  Noeud root
	 */
	SolidBinaryNode root;
	
	public SolidBinaryTree(ArrayList<Triangle> tris) {
		// Définition des bounds
		Bounds bnds = new Bounds ();
		bnds.setBounds(Solide.getSommets(tris));
		
		root = new SolidBinaryNode (tris, bnds);
	}
	
	public void split() throws SolidBinaryTreeException {
		root.SplitNode();
	}
	
	public String toString () {
		StringBuilder sb = new StringBuilder ();
		
		root.toString(sb, 0);
		
		return sb.toString();
	}
	
	
	/**
	 * Test de fusion de patch
	 * 
	 * @param args
	 * @throws InvalidGeomAction
	 * @throws SolidBinaryTreeException 
	 */
	public static void main(String[] args) throws InvalidGeomAction, SolidBinaryTreeException {
		
		// Test de est dedans 
		ArrayList <Triangle> tris = SolideMaker.getPave(new Vecteur ("1;1;1"), new Vecteur ("0;0;0")).getFacettes();
		SolidBinaryTree tree = new SolidBinaryTree(tris);
		tree.root.SplitNode();
		System.out.println(tree.toString());

		tris = SolideMaker.getPatch(20).getFacettes();
		tree = new SolidBinaryTree(tris);
		tree.root.SplitNode();
		System.out.println(tree.toString());
}


}
