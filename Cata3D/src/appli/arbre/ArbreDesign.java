package appli.arbre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import appli.Controleur;
import model.Cata;

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
	private DefaultMutableTreeNode top;
	
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
		
		top = new DefaultMutableTreeNode("D�finition d'un bateau");

		arbre = new JTree(top);
		arbre.setBackground(Color.white);

		arbre.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
		arbre.setRootVisible(true);

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
		
// Ajoute les boutons de gestion
		JPanel sub = new JPanel();
		sub.setLayout(new FlowLayout());
		sub.add(new JLabel("Gestion des composants"));
		JButton ajoute = new JButton("Ajoute");
		ajoute.setForeground(Color.black);
		ajoute.setToolTipText("Ajoute un composant");
		ajoute.setActionCommand("ajouteComposant");
		if (crtl != null) ajoute.addActionListener(crtl);		
		sub.add(ajoute);

		// Ajoute les boutons de gestion 
		JButton supprime = new JButton("Supprime");
		supprime.setForeground(Color.black);
		supprime.setToolTipText("Supprime le composant en cours");
		supprime.setActionCommand("SupprimeComposant");
		if (crtl != null) supprime.addActionListener(crtl);		
		sub.add(supprime);
		this.add(sub, BorderLayout.NORTH);
	}

	public DesignTreeNode getTheNode() {
		MutableTreeNode mtn = (MutableTreeNode) this.arbre.getLastSelectedPathComponent();
		if (mtn == top) return null;
		return (DesignTreeNode)mtn; 	
	}
	
	public void setBoatTree (Cata data) {
		DefaultMutableTreeNode tn = CataTreeNode.getNodes(data, top);
		tn.setParent((MutableTreeNode) this.arbre.getModel().getRoot());
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
