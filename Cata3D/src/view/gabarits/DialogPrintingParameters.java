package view.gabarits;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import model.math.Decimal;
import appli.values.updater.BooleanField;
import appli.values.updater.DecimalField;

public class DialogPrintingParameters extends JDialog implements ActionListener {

	public PrintingParameters params;
	public boolean isOk;
	
	private BooleanField quadri;
	private DecimalField xSize;
	private DecimalField ySize;

	/**
	 * Creation et affichage de la Frame avec le panel associÃ©
	 * 
	 * @param frm
	 * @param value
	 * @param pnl
	 */
	public DialogPrintingParameters(JFrame frm, PrintingParameters p) {
		super(frm, true);
		params = p;
		this.setLocation(frm.getMousePosition());
		this.setTitle("Gestion des paramètres d'affichage");
		isOk = false;
		
		//** Construit le panel central avec le choix du type de composant **/
		JPanel centre = new JPanel();
		centre.setLayout(new BoxLayout(centre, BoxLayout.PAGE_AXIS));
		// Choix du quadrillage
		quadri = new BooleanField("Affichage du quadrillage");
		quadri.setField(new Boolean(params.showQuadrillage));
		centre.add(quadri);
		
		// Hauteur de l'image
		ySize = new DecimalField("Hauteur de l'image", "pix.");
		ySize.setField(new Decimal(p.ySize));
		centre.add(ySize);

		// Largeur de l'image
		xSize = new DecimalField("Largeur de l'image", "pix.");
		xSize.setField(new Decimal(p.xSize));
		centre.add(xSize);
		
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
			Decimal dec = (Decimal)(this.xSize.getValue());
			params.xSize = Math.round(dec.floatValue());
			dec = (Decimal)(this.ySize.getValue());
			params.ySize = Math.round(dec.floatValue());
			this.isOk = true;
			this.setVisible(false);
		}
		if (arg0.getActionCommand().equals("Annule")) {
			this.isOk = false;
			this.setVisible(false);
		}
		
	}

}
