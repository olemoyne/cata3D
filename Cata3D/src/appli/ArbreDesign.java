package appli;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import view.View3D;
import model.Cata;

/**
 * Permet de g�rer le menu des param�tres de design de la coque
 * 
 * @author olemoyne
 *
 */
public class ArbreDesign extends JPanel implements TreeSelectionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7084266294399341057L;
	
	/**
	 * Arbre de visualisation des donn�es 
	 */
	private JTree arbre;
	
	/**
	 * Catamaran affich� 
	 */
	private Cata dessin;
	
	/** 
	 * Viewer
	 * 
	 */
	private View3D viewer;

	
	// Createur
	public ArbreDesign (View3D v) {
		super();
		setLayout(new BorderLayout());
		
		viewer = v;
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("D�finition de la coque");
		createNodes(top);
		
		arbre.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
		arbre.setRootVisible(false);

		//Listen for when the selection changes.
		arbre.addTreeSelectionListener(this);
		
		arbre = new JTree(top);
		JScrollPane treeView = new JScrollPane(arbre);
		this.add(treeView, BorderLayout.CENTER);
		
	}

	// Les neuds sont cr��s en dur
	private void createNodes(DefaultMutableTreeNode top) {
	    DefaultMutableTreeNode design = null;
	    DefaultMutableTreeNode optimisation = null;
	    DefaultMutableTreeNode construction = null;

	    design= new DefaultMutableTreeNode("Design");
	    top.add(design);
	    
	    //Patches de design de la coque
	    design.add(new DefaultMutableTreeNode("Patch"));
	    design.add(new DefaultMutableTreeNode("Poids"));

	    //optimisation du design
	    optimisation= new DefaultMutableTreeNode("Optimisation");
	    optimisation.add(new DefaultMutableTreeNode("Flottaison"));
	    optimisation.add(new DefaultMutableTreeNode("Fluidit�"));
	    optimisation.add(new DefaultMutableTreeNode("Habitablit�"));

	    //construction de la coque
	    construction= new DefaultMutableTreeNode("Construction");
	    construction.add(new DefaultMutableTreeNode("Gabarits"));
	    construction.add(new DefaultMutableTreeNode("Poutres"));
	}
	
	/**
	 * Permet de r�agir si l'arbre change de s�lection
	 * @param e
	 */
	public void valueChanged(TreeSelectionEvent e) {
		//Returns the last path element of the selection.
		//This method is useful only when the selection model allows a single selection.
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.arbre.getLastSelectedPathComponent();

		//Nothing is selected.     
		if (node == null)
		return;

		String nodename= (String) node.getUserObject();
		if (node.isLeaf()) {
			// Positionne la bonne view
			// Positionne les bonnes valeurs
		} else {
			// Positionne la bonne view
			// Pas de valeurs
		}
	}

}
