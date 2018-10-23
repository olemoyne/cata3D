package view.gabarits;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import appli.Logger;
import view.scene.GabaritScene;
import view.scene.PrintedGabarit;

public class GabaritDetailsViewer extends JPanel implements MouseWheelListener, ActionListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3990462677794567308L;

	/**
	 * Scene de visualisation des gabarits  
	 *    -> Liste des diff�rents gabarits (Choice / Select)
	 *    -> D�tail de visualisation
	 */
	
	private GabaritPlanViewer vue;
	private GabaritInformation position;
	
	private PrintingParameters params;
	
	private GabaritScene scene;
	private int element; 
	
	private Logger trace;
	
	/**
	 * Creation de la vue 2D
	 */
	public GabaritDetailsViewer (Logger log) {
		super();
		trace = log;
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		// Ajoute les informations et boutons en haut
		params = new PrintingParameters();

		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JButton button = new JButton("Imprime");
		button.setToolTipText("Impression du gabarit");
		button.setActionCommand("Imprime");
		button.addActionListener(this);
		jp.add(button);

		button = new JButton("Exporte");
		button.setToolTipText("Exporte le gabarit en segments 3D");
		button.setActionCommand("Exporte");
		button.addActionListener(this);
		jp.add(button);

		button = new JButton("Exporte tous");
		button.setToolTipText("Exporte tous les gabarits en segments 3D");
		button.setActionCommand("ExporteAll");
		button.addActionListener(this);
		jp.add(button);


		position = new GabaritInformation();
		jp.add(position);

		this.add(jp, BorderLayout.NORTH);
		
		// Vue du plan du Gabarit
		vue = new GabaritPlanViewer (position, this);
		add(vue, BorderLayout.CENTER);
		
	}
	
	/**
	 * Affiche la scene demand�e
	 * 
	 * @param gab
	 */
	public void setScene (GabaritScene gab) {
		this.scene = gab;
		element = 0;
		showElement(null);
		repaint();
	}

	public void showElement(Point2D pt) {
		if (this.scene.allObjects.size() == 0 ) element = 0; 
		else {
			if (element >= this.scene.allObjects.size()) element = this.scene.allObjects.size()-1;
			if (element < 0) element = 0;
		}

		PrintedGabarit gab = (PrintedGabarit) this.scene.allObjects.get(element);
		this.vue.setGabarit(gab);

		this.position.epaisseur = gab.epaisseur;
		this.position.id = element;
		this.position.position = gab.zPosition;
		
		if (pt != null) vue.setMousePosition(pt);
		position.show();

		vue.repaint();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int r = e.getWheelRotation();
		if (r < 0) element -= 1;
		if (r > 0) element += 1;

		showElement(e.getPoint());
		
	}

	/***
	 * Action d'imprimer les plans de la structure sélectionnée
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if ("Imprime".equals(arg0.getActionCommand())) {
			/* Affiche une fenêtre pour demander :
			 *   --> le nom du fichier de destination
			 *   --> le nombre de pixels / inc --> Calculé pour générer le plan 
			 */
			// Demande d'impression de l'element sélectionné
			PrintedGabarit pg = (PrintedGabarit) scene.allObjects.get(element);
			/** d�finition du nom de fichier � produire **/
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			DialogPrintingParameters dial = new DialogPrintingParameters (topFrame, params); 
			// Si OK : g�n�ration du fichier
			if (dial.isOk) {
				// Production des dessins
				try {
					ImageFileCreator.createFile(pg, element, params, this.scene.cataFileName);
					trace.writeLog("Plans créés avec succès");
				} catch (IOException e) {
					e.printStackTrace();
					trace.writeLog(e.getMessage());
				}
			}
		}

		if ("Exporte".equals(arg0.getActionCommand())) {
			if (scene == null) return;
			PrintedGabarit pg = (PrintedGabarit) scene.allObjects.get(element);
			/** d�finition du nom de fichier � produire **/
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			DialogExportParameters dial = new DialogExportParameters (topFrame, params); 
			// Si OK : g�n�ration du fichier
			if (dial.isOk) {
				// Production des dessins
				try {
					ScriptFileCreator.createFile(pg, element, params, this.scene.cataFileName);
					trace.writeLog("Exports créés avec succès");
				} catch (IOException e) {
					e.printStackTrace();
					trace.writeLog(e.getMessage());
				}
			}
		}
		
		if ("ExporteAll".equals(arg0.getActionCommand())) {
			if (scene == null) return;
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			DialogExportParameters dial = new DialogExportParameters (topFrame, params); 
			/** d�finition du nom de fichier � produire **/
			
			if (dial.isOk) {
				// Production des dessins
				try {
					ScriptFileCreator.createPositionFile(scene.allObjects, params, this.scene.cataFileName);
					trace.writeLog("Exports créés avec succès");
				} catch (IOException e) {
					e.printStackTrace();
					trace.writeLog(e.getMessage());
				}
			}
		}

	
	}

}
