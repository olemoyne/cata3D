package appli.values.updater;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Poids;
import model.math.Decimal;
import model.math.Vecteur;


public class PropertyValueUpdater extends JPanel {

	private ObjectUpdater active;
	
	private Hashtable<String, ObjectUpdater> panels;

	private JButton modifie;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7196472580862805817L;
	
	public PropertyValueUpdater (ActionListener ctr) {
		super();
		
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(380, 200));
		this.setBackground(Color.white);
		panels = new Hashtable<String, ObjectUpdater>();
		
		JLabel lab = new JLabel ("Propriété sélectionnée :");
		this.add(lab, BorderLayout.NORTH);

		ObjectUpdater o = new VecteurUpdater();
		panels.put(Vecteur.class.toString(), o);
		
		o = new PoidsUpdater();
		panels.put(Poids.class.toString(), o);

		o = new IntegerUpdater();
		panels.put(Integer.class.toString(), o);

		o = new DecimalUpdater();
		panels.put(Decimal.class.toString(), o);

		JPanel sub = new JPanel();
		sub.setLayout(new FlowLayout(FlowLayout.CENTER));
		// Ajoute les boutons de gestion 
		modifie = new JButton("Modifie");
		modifie.setForeground(Color.black);
		modifie.setToolTipText("Modifie l'élément dans le tableau");
		modifie.setActionCommand("modifie");
		modifie.addActionListener(ctr);		
		sub.add(modifie);

		this.add(sub, BorderLayout.SOUTH);
	}
	
	
	public void setValue (Object val, boolean modif) {
		if (val == null) return;
		String cl = val.getClass().toString();
		if (active != null) this.remove(active);
		active = panels.get(cl);
		if (active != null) {
			active.setProperty(val);
			this.add(active, BorderLayout.CENTER);
			modifie.setEnabled(modif);
			this.repaint();
		}
	}
	
	public Object getValue () {
		if (active != null) {
			return active.getData();
		}
		return null;
	}

}
