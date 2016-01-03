package appli.arbre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Cata;
import model.composants.Composant;
import model.composants.PatchPlein;
import model.composants.PatchVide;
import model.composants.Recopie;
import model.composants.Tube;
import appli.values.updater.StringField;

public class DialogComponant extends JDialog implements ActionListener {

	public Composant composant;
	public boolean isOk;
	
	private Cata bateau;
	private StringField nom;
	private JComboBox<String> select;

	/**
	 * Creation et affichage de la Frame avec le panel associé
	 * 
	 * @param frm
	 * @param value
	 * @param pnl
	 */
	public DialogComponant(JFrame frm, Cata boat) {
		super(frm, true);
		bateau = boat;
		this.setLocation(frm.getMousePosition());
		this.setTitle("Ajout d'un composant");
		isOk = false;
		
		//** Construit le panel central avec le choix du type de composant **/
		JPanel centre = new JPanel();
		centre.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		// Choix du nom
		nom = new StringField("Nom du composant :");
		centre.add(nom);
		// Choix du type
		String[] lst = {"Patch vide", "Patch plein", "Recopie", "Tube"};
		JPanel sub = new JPanel ();
		sub.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		sub.add(new JLabel ("Type de composant :"));
		select = new JComboBox<String>();
		for (String str : lst) select.addItem(str);
		sub.add(select);
		centre.add(sub);
		
		JPanel panel = new JPanel ();
		panel.setLayout(new BorderLayout());
		panel.add(centre, BorderLayout.CENTER);
		
		sub = new JPanel();
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
			String type = (String)select.getSelectedItem();
			if (type.equals("Patch vide")) {
				this.composant = new PatchVide(bateau);
			}
			if (type.equals("Patch plein")) {
				this.composant = new PatchPlein(bateau);
			}
			if (type.equals("Recopie")) {
				this.composant = new Recopie(bateau);
			}
			if (type.equals("Tube")) {
				this.composant = new Tube(bateau);
			}
			if (composant != null) {
				composant.nom = (String)this.nom.getValue();
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
