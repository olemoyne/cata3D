package appli.values.updater;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import appli.arbre.TreeNodeProperty;

public class DialogValue extends JDialog implements ActionListener {

	TreeNodeProperty property;
	ObjectUpdater update;
	boolean isOk;
	/**
	 * Creation et affichage de la Frame avec le panel associé
	 * @param frm
	 * @param value
	 * @param pnl
	 */
	public DialogValue(JFrame frm, TreeNodeProperty prop, ObjectUpdater pnl) {
		super(frm, true);
		this.setLocation(frm.getMousePosition());
		this.setTitle("Modification d'un paramètre");
		property = prop;
		update = pnl;
		isOk = false;
		
		JPanel panel = new JPanel ();
		panel.setLayout(new BorderLayout());
		panel.add(pnl, BorderLayout.CENTER);
		
		JLabel lab= new JLabel(prop.nom);
		panel.add(lab, BorderLayout.NORTH);
		
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
			Object data = update.getData();
			this.property.value = data;
			this.isOk = true;
			this.setVisible(false);
		}
		if (arg0.getActionCommand().equals("Annule")) {
			Object data = update.getData();
			this.property.value = data;
			this.setVisible(false);
		}
		
	}

}
