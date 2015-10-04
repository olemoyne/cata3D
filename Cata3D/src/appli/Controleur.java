package appli;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.Cata;
import view.View3D;
import appli.values.CataDataManager;
import appli.values.TableValues;

public class Controleur implements ActionListener, TreeSelectionListener{
	
	/**
	 * Catamaran affiché 
	 */
	private Cata dessin;
	
	/** Gestion des outils affichés**/
	private JLabel message;
	private CataFileManager mngr;
	private View3D viewer;
	private TableValues values;
	private ArbreDesign arbre;
	
	private CataDataManager dataManager;
	

	public Controleur (JPanel fond) {
		/**
		 * Creation du manager de formes
		 */
		CataViewUpdate upd = new CataViewUpdate("0.25");
		viewer = new View3D(upd);

		fond.add(viewer, BorderLayout.CENTER);

		arbre = new ArbreDesign(this);
		fond.add(arbre, BorderLayout.WEST);

		dataManager = new CataDataManager();
		
		values = new TableValues (dataManager);
		arbre.add(values, BorderLayout.SOUTH);
	
        JPanel bas = new JPanel(new FlowLayout(FlowLayout.LEFT));
		  // Message d'erreur
		message = new JLabel();
		message.setForeground(Color.red);
		bas.add(message);
		fond.add(bas, BorderLayout.SOUTH);
		
		mngr = new CataFileManager(this);
		fond.add(mngr, BorderLayout.NORTH);
		
		dessin = null;
	}
	
	/**
	 * Permet de réagir si l'arbre change de sélection
	 * @param e
	 */
	public void valueChanged(TreeSelectionEvent e) {
		//Returns the last path element of the selection.
		//This method is useful only when the selection model allows a single selection.
		showNodeDetails();
	}
	
	
	private void showNodeDetails () {
		DefaultMutableTreeNode node = arbre.getTheNode();

		//Nothing is selected.     
		if (node == null) return;
		String nodeName = (String) node.getUserObject();
		if (node.isLeaf()) {
			// Positionne la bonne view
			// Positionne les bonnes valeurs
		} else {
			// Positionne la bonne view
			// Pas de valeurs
		}
	}

	/**
	 * Gestion des sauvegardes du catamaran
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("Ouvre".equals(e.getActionCommand())) {
			try {
				// Ouvre un nouveau fichier
				this.dessin = mngr.getCataFromFile();
				// Affiche le catamaran
				showNodeDetails();
			} catch (CataAppliException e1) {
				e1.printStackTrace();
				this.printMessage(e1.getLocalizedMessage());
			}
		}

		if ("Sauve".equals(e.getActionCommand())) {
			try {
				// Ouvre un nouveau fichier
				mngr.saveCataToFile(this.dessin);
				printMessage("Catamaran sauvé");
			} catch (CataAppliException e1) {
				e1.printStackTrace();
				this.printMessage(e1.getLocalizedMessage());
			}
		}

	}


	/**
	 * Affiche un message d'erreur sur la console
	 * 
	 * @param msg
	 */
	public void printMessage(String msg) {
		this.message.setText(msg);
	}



}
