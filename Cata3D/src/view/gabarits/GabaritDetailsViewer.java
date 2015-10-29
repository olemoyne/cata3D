package view.gabarits;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Gabarit;
import model.Poutre;
import model.math.Plan3D;
import view.scene.GabaritScene;

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
	
	private JComboBox<Gabarit> select;
	
	private Plan3D mer;
	
	private ArrayList<Poutre> poutres;
	
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
		select = new JComboBox<Gabarit>();
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
/**		for (Gabarit g : gab.getGabarits()) select.addItem(g);
		if (gab.getGabarits().size() > 0) {
			Gabarit g = gab.getGabarits().get(0);
			if (g != null) select.getModel().setSelectedItem(g);
		}
**/		
	}

	@Override
	/** 
	 * Changement de gabarit selectionn� 
	 ***/
	public void actionPerformed(ActionEvent e) {
		Gabarit gab = (Gabarit)this.select.getSelectedItem();
		// TODO Auto-generated method stub
		this.vue.setGabarit(gab, mer, poutres);
	}
	
}
