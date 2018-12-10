package view.param;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.math.Decimal;
import appli.arbre.DialogFileName;
import appli.values.updater.BooleanField;
import appli.values.updater.DecimalField;

public class DialogPrintingParameters extends JDialog implements ActionListener {

	public PrintingParameters params;
	public boolean isOk;
	
	private BooleanField quadri;
	private DecimalField ppi;
	private JTextField editeur;

	/**
	 * Creation et affichage de la Frame avec le panel associé
	 * 
	 * @param frm
	 * @param value
	 * @param pnl
	 */
	public DialogPrintingParameters(JFrame frm, PrintingParameters p) {
		super(frm, true);
		params = p;
		this.setLocation(frm.getMousePosition());
		this.setTitle("Gestion des param�tres d'affichage");
		isOk = false;
		
		//** Construit le panel central avec le choix du type de composant **/
		JPanel centre = new JPanel();
		centre.setLayout(new BoxLayout(centre, BoxLayout.PAGE_AXIS));
		// Choix du quadrillage
		quadri = new BooleanField("Affichage du quadrillage");
		quadri.setField(new Boolean(params.showQuadrillage));
		centre.add(quadri);
		
		// Hauteur de l'image
		ppi = new DecimalField("Précision de l'image", "ppi");
		ppi.setField(new Decimal(p.pixByInch));
		centre.add(ppi);
		
		JLabel lab = new JLabel("Réperoire des fichiers de plans :");
		centre.add(lab);
		
		editeur = new JTextField();
		editeur.setColumns(20);
		editeur.setToolTipText("Saisir le chemin vers le fichier � �diter");
		if (p.fileName != null) editeur.setText(p.fileName);
		centre.add(editeur);

		// Ajoute les boutons de gestion 
		JButton fichier = new JButton("Selection");
		fichier.setForeground(Color.black);
		fichier.setActionCommand("Selection");
		fichier.addActionListener(this);		
		centre.add(fichier);

		
		JPanel sub = new JPanel();
		sub.setLayout(new FlowLayout(FlowLayout.LEADING));

		// Ajoute les boutons de gestion 
		JButton ajoute = new JButton("OK");
		ajoute.setForeground(Color.black);
		ajoute.setActionCommand("Valide");
		ajoute.addActionListener(this);		
		sub.add(ajoute);

		// Ajoute les boutons de gestion 
		JButton supprime = new JButton("Annule");
		supprime.setForeground(Color.black);
		supprime.setActionCommand("Annule");
		supprime.addActionListener(this);		
		sub.add(supprime);

		centre.add(sub, BorderLayout.SOUTH);
		
		setContentPane(centre);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4306376096092261094L;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("Valide")) {
			params.showQuadrillage = this.quadri.getValue();
			Decimal dec = (Decimal)(this.ppi.getValue());
			params.pixByInch = Math.round(dec.floatValue());
			params.fileName = editeur.getText();

			this.isOk = true;
			this.setVisible(false);
		}
		if (arg0.getActionCommand().equals("Annule")) {
			this.isOk = false;
			this.setVisible(false);
		}
		if (arg0.getActionCommand().equals("Selection")) {
			/** d�finition du nom de fichier � produire **/
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			DialogFileName dial = new DialogFileName (topFrame, editeur.getText(), "Sélectionne un répertoire de "); 
			// Si OK : g�n�ration du fichier
			if (dial.isOk) {
				editeur.setText(dial.path);
			}
		}
		
	}

}
