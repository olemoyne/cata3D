package appli;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.MutableTreeNode;

import model.Cata;
import model.composants.Composant;
import view.scene.PrintableScene;
import appli.arbre.ArbreDesign;
import appli.arbre.DialogComponant;
import appli.arbre.nodes.ComposantTreeNode;
import appli.arbre.nodes.DesignTreeNode;
import appli.values.CataValuesException;
import appli.values.TableValues;

public class Controleur implements ActionListener, TreeSelectionListener{
	
	/**
	 * Catamaran affich� 
	 */
	private Cata dessin;
	
	/** Gestion des outils affich�s**/
	private Message message;
	private CataFileManager mngr;
	private TableValues values;
	private ArbreDesign arbre;
	private ActiveView vue; 
	
	private Logger log;

	public Controleur (JPanel fond, Logger lg) {
		log = lg;

		log.writeLog("Reading the application context ");
		Context ctx = Context.readFromfile();

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.LEFT));
		  // Message d'erreur
		message = new Message();
		bas.add(message);
		fond.add(bas, BorderLayout.SOUTH);
		
		/**
		 * Creation du manager de formes
		 */
		log.writeLog("Starting the view");
		vue = new ActiveView(fond);

		log.writeLog("Drawing the tree");
		arbre = new ArbreDesign(this);
		fond.add(arbre, BorderLayout.WEST);

		
		log.writeLog("File manager starting");
		mngr = new CataFileManager(this);
		
		fond.add(mngr, BorderLayout.NORTH);
		

		log.writeLog("Drawing the value table");
		values = new TableValues (this, message);
		
		if ((ctx != null)&&(ctx.lastCataFile != null)) {
			mngr.setFile (ctx.lastCataFile);
			try {
				dessin = mngr.getCataFromFile();
				arbre.setBoatTree(dessin);
				
				if (ctx.lastTreePath != null)
					arbre.gotToPath(ctx.lastTreePath);
			} catch (CataAppliException e) {
				log.writeLog("Last boat file not found "+ctx.lastCataFile);
			} 
		}

		if (dessin == null) {
			dessin = new Cata();
			arbre.setBoatTree(dessin);
		}

		arbre.add(values, BorderLayout.SOUTH);
	}
	
	/**
	 * Permet de r�agir si l'arbre change de s�lection
	 * @param e
	 */
	public void valueChanged(TreeSelectionEvent e) {
		//Returns the last path element of the selection.
		//This method is useful only when the selection model allows a single selection.
		showNodeDetails();
	}
	
	
	private void showNodeDetails () {
		MutableTreeNode node = arbre.getTheNode();
		//Nothing is selected.     
		if (node == null) return;
		
		try {
			DesignTreeNode des = (DesignTreeNode) node;
			// Positionne la bonne view
			this.showDessin(des);
			// Positionne les bonnes valeurs
			values.showNode(des);
		} catch (CataValuesException e) {
			message.logError (e.getLocalizedMessage());
		} catch (ClassCastException e) {
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
				this.arbre.setBoatTree(dessin);
				// Affiche le catamaran
				showNodeDetails();
			} catch (CataAppliException e1) {
				e1.printStackTrace();
				this.message.logError(e1.getLocalizedMessage());
			}
		}

		if ("Sauve".equals(e.getActionCommand())) {
			try {
				// Ouvre un nouveau fichier
				mngr.saveCataToFile(this.dessin);
				message.logTrace("Catamaran sauv�");
			} catch (CataAppliException e1) {
				e1.printStackTrace();
				this.message.logError(e1.getLocalizedMessage());
			}
		}
		
		// Ajoute un composant à l'arbre de visualisation
		if ("ajouteComposant".equals(e.getActionCommand())) {
			// affiche une fenetre avec le choix du type de composant et le nom du composant
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this.arbre);
			DialogComponant dial = new DialogComponant(topFrame, this.dessin); 
			// Si OK : ajoute un composant sur le bateau
			if (dial.isOk) {
				Composant cmp = dial.composant;
				//et ajoute un nouveau node dans l'arbre 
				this.dessin.composants.add(cmp);
				arbre.setBoatTree(dessin);
				this.recalcule();
			}
		}

		// Ajoute un composant à l'arbre de visualisation
		if ("SupprimeComposant".equals(e.getActionCommand())) {
			// R�cup�re le composant actif et le supprime
			MutableTreeNode node = arbre.getTheNode();
			if (node != null) {
				DesignTreeNode nde = (DesignTreeNode) node;
				if (nde.getLevel() == DesignTreeNode.LEVEL_COMPOSANT) {
					ComposantTreeNode cnd = (ComposantTreeNode) node;
					this.dessin.composants.remove(cnd.getComposant());
					arbre.setBoatTree(dessin);
					this.recalcule();
				}
			}
		}
	}

	public void saveContext() {
		Context ctx = new Context();
		ctx.lastCataFile = mngr.getFile();
		ctx.lastTreePath = arbre.getPath();
		System.out.println("Actual path = "+ctx.lastTreePath);
		Context.saveTofile(ctx);
	}

	public void showDessin(DesignTreeNode node) {
		// Positionne la bonne view
		PrintableScene scene = node.getScene();
		vue.setScene(scene);
	}
	
	public void recalcule () {
		this.dessin.recalculeAll();
	}
}
