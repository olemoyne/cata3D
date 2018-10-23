package model.viewer.appli;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.viewer.view.Scene;
import model.viewer.view.ScreenConfig;
import model.viewer.view.View3D;

/**
 * Permet de visualiser et de gerer les formes en 3 dimensions
 * 
 * Contient un menu permettant d'effectuer des op�rations sur des formes
 * 
 * @author olemoyne
 *
 */
public class Viewer extends JFrame implements WindowListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8973743370089212343L;
	/** 
	 * Affichage 3D 
	 */
	protected View3D viewPanel;
	/**
	 * Titre de la fenetre
	 */
	private String title;
	
	/**
	 * Table des formes 
	 */
	private TableFormes formes;
	
	/**
	 *  Affichage d'un message d'erreur
	 */
	private JLabel message;
	
	/**
	 * Configuration de l'application
	 */
	private ScreenConfig config;
	
	/** Updater de scene **/
	private DessinViewUpdate upd;
	
	/**
	 * Dessin affich� 
	 */
	private Scene dessin;
	private ScriptActionProvider sap;
	
	/**
	 * @param name
	 * @param title
	 * @param icon
	 */
	public Viewer(String name, String t, String c) {
		super();
		title = t;
		
		config = new ScreenConfig(c);
		config.loadConfig();
	}	
	

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() {
        //Suggest that the L&F (rather than the system)
        //decorate all windows.  This must be invoked before
        //creating the JFrame.  Native look and feels will
        //ignore this hint.
        JFrame.setDefaultLookAndFeelDecorated(false);

        //Create and set up the window.
		setTitle(title);
		this.setIconImage(new ImageIcon("./data/conf/appli.png").getImage());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = getToolkit().getScreenSize();
		setBounds(0, 0, dim.width, dim.height-30);
     
        addWindowListener(this);
        
        JPanel fond = new JPanel();
        fond.setLayout(new BorderLayout());
        
        
        JPanel bas = new JPanel(new FlowLayout(FlowLayout.LEFT));
		  // Message d'erreur
		message = new JLabel();
		message.setForeground(Color.red);
		bas.add(message);
		fond.add(bas, BorderLayout.SOUTH);

		
		upd = new DessinViewUpdate("0.25");
		viewPanel = new View3D(upd, this);
		sap = new ScriptActionProvider(upd, viewPanel);
		formes = new TableFormes();

//		fond.add(menuFormes, BorderLayout.NORTH);
		fond.add(formes, BorderLayout.EAST);
		
		fond.add(viewPanel, BorderLayout.CENTER);
		setContentPane(fond);
		
		dessin = new Scene ();
		formes.setDessin (dessin);
		upd.setDessin(dessin);
		
		//Display the window.
		setVisible(true);
		
    }

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent arg0) {
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 * 
	 * Closing the the Connexion to the ADT System Server
	 */
	public void windowClosed(WindowEvent arg0) {
	}
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent arg0) {
		config.saveConfig();
	}
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent arg0) {
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent arg0) {
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent arg0) {
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent arg0) {
		
	}
	
	
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Viewer ddWindow = new Viewer("modelisation", "Mod�lisation 3D", "/home/lemoyne/dev/GIT/cata3D/SolidManager/data/model3D.config");
				ddWindow.createAndShowGUI();
				//** Lecture du script et mise à jour
				ddWindow.sap.runScript(args[0]);
			}
		});
	}

	

	/**
	 * Affiche un message d'erreur sur la console
	 * 
	 * @param msg
	 */
	public void printMessage(String msg) {
		this.message.setText(msg);
	}


	public ScreenConfig getConfig() {
		return this.config;
	}


	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\n') {
			sap.nextScene();
		}
		
	}
}
