package view.gabarits;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import view.scene.GabaritScene;
import view.scene.PrintableObject;
import view.scene.PrintedGabarit;

public class GabaritDetailsViewer extends JPanel implements ActionListener {

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
	
	private JComboBox<PrintedGabarit> select;
	
	/**
	 * Creation de la vue 2D
	 */
	public GabaritDetailsViewer () {
		super();
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		
		// Ajoute les �l�ments 
		//Liste des gabarits
		JPanel sub = new JPanel();
		sub.setLayout(new BoxLayout(sub, BoxLayout.LINE_AXIS));
		sub.add(new JLabel("Choisissez le gabarit � �tudier"));
		select = new JComboBox<PrintedGabarit>();
		select.addActionListener(this);
		sub.add(select);
		
		this.add(sub, BorderLayout.NORTH);
		
		// Vue du plan du Gabarit
		vue = new GabaritPlanViewer ();
		add(vue, BorderLayout.CENTER);
		
	}
	
	/**
	 * Affiche la scene demand�e
	 * 
	 * @param gab
	 */
	public void setScene (GabaritScene gab) {
		// Affiche la liste des gabarits possible
		select.removeAllItems();
		for (PrintableObject g : gab.allObjects) select.addItem((PrintedGabarit)g);
		if (gab.allObjects.size() > 0) {
			PrintableObject g = gab.allObjects.get(0);
			if (g != null) select.getModel().setSelectedItem((PrintedGabarit)g);
		}
		repaint();
	}

	@Override
	/** 
	 * Changement de gabarit selectionn� 
	 ***/
	public void actionPerformed(ActionEvent e) {
		PrintedGabarit gab = (PrintedGabarit)this.select.getSelectedItem();
		
		this.vue.setGabarit(gab);
		repaint();
	}
	
}
