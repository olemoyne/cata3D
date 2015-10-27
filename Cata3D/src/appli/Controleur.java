package appli;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.Cata;
import view.PrintableObjectViewer;
import view.PrintableViewUpdate;
import view.scene.PrintableScene;
import appli.values.CataDataManager;
import appli.values.CataValuesException;
import appli.values.TableValues;

public class Controleur implements ActionListener, TreeSelectionListener{
	
	/**
	 * Catamaran affiché 
	 */
	private Cata dessin;
	
	/** Gestion des outils affichés**/
	private Message message;
	private CataFileManager mngr;
	private PrintableObjectViewer viewer;
	private PrintableViewUpdate upd;
	private TableValues values;
	private ArbreDesign arbre;
	
	private CataDataManager dataManager;
	
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
		upd = new PrintableViewUpdate("0.25");
		viewer = new PrintableObjectViewer(upd);

		fond.add(viewer, BorderLayout.CENTER);

		log.writeLog("Drawing the tree");
		arbre = new ArbreDesign(this);
		fond.add(arbre, BorderLayout.WEST);

		
		log.writeLog("File manager starting");
		mngr = new CataFileManager(this);
		fond.add(mngr, BorderLayout.NORTH);
		

		/** Lecture du catamaran du dernier fichier **/
		dataManager = new CataDataManager();
		
		log.writeLog("Drawing the value table");
		values = new TableValues (dataManager, this, message);
		
		if ((ctx != null)&&(ctx.lastCataFile != null)) {
			mngr.setFile (ctx.lastCataFile);
			try {
				dessin = mngr.getCataFromFile();
				dataManager.setData(dessin);
				
				if (ctx.lastTreePath != null)
					arbre.gotToPath(ctx.lastTreePath);
			} catch (CataAppliException e) {
				log.writeLog("Last boat file not found "+ctx.lastCataFile);
			} 
		}

		if (dessin == null) {
			dessin = new Cata();
			dataManager.setData(dessin);
		}

		arbre.add(values, BorderLayout.SOUTH);

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
		log.writeLog("Loading screen "+nodeName);
		this.message.logTrace("Loading screen "+nodeName);
		
		try {
			if (node.isLeaf()) {
				// Positionne la bonne view
				viewer.setScene(dataManager.getView(nodeName));
				// Positionne les bonnes valeurs
				values.showNode(nodeName);
			} else {
				// Positionne la bonne view
				// Pas de valeurs
			}
		} catch (CataValuesException e) {
			message.logError (e.getLocalizedMessage());
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
				this.dataManager.setData(dessin);
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
				message.logTrace("Catamaran sauvé");
			} catch (CataAppliException e1) {
				e1.printStackTrace();
				this.message.logError(e1.getLocalizedMessage());
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

	public void updateView(PrintableScene view) {
		this.viewer.setScene(view);
	}

	public void showDessin(String nodeName) {
		// Positionne la bonne view
		try {
			viewer.setScene(dataManager.getView(nodeName));
		} catch (CataValuesException e) {
			e.printStackTrace();
			this.message.logError(e.getLocalizedMessage());
		}
	}
}
