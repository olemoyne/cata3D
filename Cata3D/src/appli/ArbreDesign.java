package appli;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Permet de g�rer le menu des param�tres de design de la coque
 * 
 * @author olemoyne
 *
 */
public class ArbreDesign extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7084266294399341057L;
	
	/**
	 * Arbre de visualisation des donn�es 
	 */
	private JTree arbre;
	
	/**
	 * Constructeur avec la r�f�rence vers le viewer pour changer les donn�es ou rafraichir la vision
	 * 
	 * @param v
	 * @param msg 
	 */
	// Createur
	public ArbreDesign (Controleur crtl) {
		super();
		setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(400, 1000));
		this.setBackground(Color.black);
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("D�finition de la coque");
		createNodes(top);

		arbre = new JTree(top);
		arbre.setBackground(Color.white);

		arbre.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
		arbre.setRootVisible(false);

		//Listen for when the selection changes.
		arbre.addTreeSelectionListener(crtl);
		JScrollPane treeView = new JScrollPane(arbre);
		this.add(treeView, BorderLayout.CENTER);

		ImageIcon leafIcon = new ImageIcon("imgs/anchor_.png");
		if (leafIcon != null) {
		    DefaultTreeCellRenderer renderer = 
		        new DefaultTreeCellRenderer();
		    renderer.setLeafIcon(leafIcon);
		    arbre.setCellRenderer(renderer);
		}
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
	    top.add(optimisation);
	    optimisation.add(new DefaultMutableTreeNode("Flottaison"));
	    optimisation.add(new DefaultMutableTreeNode("Fluidité"));
	    optimisation.add(new DefaultMutableTreeNode("Habitabilité"));

	    //construction de la coque
	    construction= new DefaultMutableTreeNode("Construction");
	    top.add(construction);
	    construction.add(new DefaultMutableTreeNode("Gabarits"));
	    construction.add(new DefaultMutableTreeNode("Poutres"));
	    construction.add(new DefaultMutableTreeNode("Plans"));
	}

	public DefaultMutableTreeNode getTheNode() {
		return (DefaultMutableTreeNode) this.arbre.getLastSelectedPathComponent();	
	}
	
	public TreePath getPath () {
		TreePath pth = arbre.getAnchorSelectionPath();
		return pth;
	}
	
	public void gotToPath(TreePath path) { 
		arbre.expandPath(path);
		arbre.addSelectionPath(path);
	}
	
}
