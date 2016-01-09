package view.view3D;

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

import model.composants.Composant;
import model.math.Decimal;
import appli.values.updater.DecimalField;

public class DialogEchelle extends JDialog implements ActionListener {

	public Composant composant;
	public boolean isOk;
	
	private DecimalField val;
	public Decimal echelle;
	

	/**
	 * Creation et affichage de la Frame avec le panel associé
	 * 
	 * @param frm
	 * @param value
	 * @param pnl
	 */
	public DialogEchelle(JFrame frm, Decimal echl) {
		super(frm, true);
		echelle = echl;
		this.setTitle("Modification de l'échelle");
		isOk = false;
		
		//** Construit le panel central avec le choix du type de composant **/
		JPanel centre = new JPanel();
		centre.setLayout(new BoxLayout(centre, BoxLayout.PAGE_AXIS));
		// Choix du nom
		val = new DecimalField("Echelle :", "");
		val.setField(echl);
		centre.add(val);
		
		JPanel panel = new JPanel ();
		panel.setLayout(new BorderLayout());
		panel.add(centre, BorderLayout.CENTER);
		
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

		panel.add(sub, BorderLayout.SOUTH);
		
		setContentPane(panel);
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
			Decimal dec = (Decimal)this.val.getValue();
			if (dec != null) {
				echelle = dec;
				this.isOk = true;
				this.setVisible(false);
			}
		}
		if (arg0.getActionCommand().equals("Annule")) {
			this.isOk = false;
			this.setVisible(false);
		}
		
	}
}
