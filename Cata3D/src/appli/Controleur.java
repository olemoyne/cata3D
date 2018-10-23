package appli;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.MutableTreeNode;

import model.Cata;
import model.composants.Composant;
import model.math.Decimal;
import model.math.InvalidGeomAction;
import view.scene.PrintableScene;
import appli.arbre.ArbreDesign;
import appli.arbre.DialogComponant;
import appli.arbre.DialogFileName;
import appli.arbre.nodes.CataTreeNode;
import appli.arbre.nodes.ComposantTreeNode;
import appli.arbre.nodes.DesignTreeNode;
import appli.arbre.nodes.PatchTreeNode;
import appli.values.CataValuesException;
import appli.values.TableValues;

public class Controleur implements ActionListener, TreeSelectionListener, MouseListener{
	
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
	
	private String filePath;

	public Controleur (JPanel fond, Logger lg) {
		log = lg;

		log.writeLog("Reading the application context ");
		Context ctx = Context.readFromfile();
		if (ctx.filePath != null) this.filePath = ctx.filePath;
		else this.filePath = "C:/";

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.LEFT));
		  // Message d'erreur
		message = new Message();
		bas.add(message);
		fond.add(bas, BorderLayout.SOUTH);
		log.toScreen  = message;
		
		/**
		 * Creation du manager de formes
		 */
		log.writeLog("Starting the view");
		if (ctx.echelle == null) ctx.echelle = Decimal.UN;
		vue = new ActiveView(fond, ctx.echelle, lg);

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
				dessin = mngr.getCataFromFile(ctx.lastCataFile);
				arbre.setBoatTree(dessin);
				
			} catch (CataAppliException e) {
				log.writeLog("Last boat file not found "+ctx.lastCataFile);
			} 
		}

		if (dessin == null) {
			dessin = new Cata("A créer");
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
	
	
	/**
	 * Parcours des noeuds de l'arbre
	 *   --affiche les valeurs 
	 *   --affiche la vue 3D
	 */
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
				/** d�finition du nom de fichier � produire **/
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this.arbre);
				DialogFileName dial = new DialogFileName (topFrame, this.mngr.getFile(), "Ouvre un fichier de mod�lisation"); 
				// Si OK : g�n�ration du fichier
				if (dial.isOk) {
					this.filePath = dial.path;
					
					// Ouvre un nouveau fichier
					this.dessin = mngr.getCataFromFile(dial.filename);
					this.arbre.setBoatTree(dessin);
					// Affiche le catamaran
					showNodeDetails();
				}
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
		if ("Ajoute composant".equals(e.getActionCommand())) {
			DesignTreeNode prt = this.arbre.getDesignNode();
			// affiche une fenetre avec le choix du type de composant et le nom du composant
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this.arbre);
			DialogComponant dial = new DialogComponant(topFrame, this.dessin); 
			// Si OK : ajoute un composant sur le bateau
			if (dial.isOk) {
				Composant cmp = dial.composant;
				//et ajoute un nouveau node dans l'arbre 
				this.dessin.composants.add(cmp);
				DesignTreeNode sub = CataTreeNode.getNodeForComponent(cmp, prt);
				arbre.addNode(sub, prt);
				this.recalcule();
			}
		}

		// Ajoute un composant à l'arbre de visualisation
		if ("Supprime composant".equals(e.getActionCommand())) {
			// R�cup�re le composant actif et le supprime
			MutableTreeNode node = arbre.getTheNode();
			if (node != null) {
				DesignTreeNode nde = (DesignTreeNode) node;
				if (nde.getNodeLevel() == DesignTreeNode.LEVEL_COMPOSANT) {
					ComposantTreeNode cnd = (ComposantTreeNode) node;
					this.dessin.composants.remove(cnd.getComposant());
					arbre.removeNode(nde);
					this.recalcule();
				}
			}
		}
		/** G�n�ration du composant sous la forme d'un fichier **/
		if ("Fichier STL".equals(e.getActionCommand())) {
			MutableTreeNode node = arbre.getTheNode();
			if (node != null) {
				PatchTreeNode nde = (PatchTreeNode) node;
				/** d�finition du nom de fichier � produire **/
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this.arbre);
				DialogFileName dial = new DialogFileName (topFrame, this.filePath, "Fichier d'export STL du composant"); 
				// Si OK : g�n�ration du fichier
				if (dial.isOk) {
					this.filePath = dial.path;
					try {
						nde.generateSTLFile (dial.filename);
					} catch (InvalidGeomAction e1) {
						e1.printStackTrace();
						this.message.logError("Bp de s�rialisation : "+e1.getLocalizedMessage());
					}
				}
			}
		}
	}

	public void saveContext() {
		System.out.println("Saving the context");
		Context ctx = new Context();
		ctx.lastCataFile = mngr.getFile();
		ctx.echelle = this.vue.getEchelle();
		ctx.filePath = this.filePath;
		System.out.println("Start saving");
		Context.saveTofile(ctx);
		System.out.println("Saved");
	}

	public void showDessin(DesignTreeNode node) {
		// Positionne la bonne view
		PrintableScene scene = node.getScene();
		vue.setScene(scene);
	}
	
	public void recalcule () {
		this.dessin.recalculeAll();
	}

	/**
	 * Affiche le PopupMenu
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// check is click by right button
		if (e.getButton() == MouseEvent.BUTTON3) {
			MutableTreeNode nde = this.arbre.getTheNode();
			if (nde == null) return;
			if (nde == arbre.getRootNode()) return;
			DesignTreeNode dtn = (DesignTreeNode) nde;
			String[] lst = dtn.getActionList();
			if (lst != null) {
				// Pour chaque action on ajoute un menu
				JPopupMenu popup = new JPopupMenu();
				for (String str : lst) {
					JMenuItem menuItem = new JMenuItem(str);
				    menuItem.addActionListener(this);
				    popup.add(menuItem);
				}
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
