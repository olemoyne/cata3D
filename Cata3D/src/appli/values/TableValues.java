package appli.values;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import appli.Controleur;
import appli.Message;
import appli.arbre.DesignTreeNode;
import appli.arbre.TreeNodeProperty;
import appli.values.updater.PropertyValueUpdater;

/***
 * Permet d'�diter les �l�ments de conception de la coque
 * 
 * @author olemoyne
 *
 */
public class TableValues extends JPanel implements ActionListener, ListSelectionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2179527244572915851L;
	
	private ValuesTableModel model;
	private JTable table;
	
	private JButton ajoute, supprime, modifie;
		
	private PropertyValueUpdater fields;
	
	private int selectedRow;
	private boolean buttonsShown;
	
	private Controleur control;
		
	public TableValues (Controleur ctrl, Message log) {
		super();
		
		
		Color buttonColor = this.getBackground();
		control = ctrl;

		model = new ValuesTableModel (log);
		table = new JTable(model);
		table.setAutoscrolls(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(this);
		
		table.setDefaultRenderer(Color.class, new ColorRenderer(true));
        table.setDefaultEditor(Color.class, new ColorEditor());
        table.setDefaultEditor(String.class, new DefaultCellEditor(new JTextField()));


		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		
		JPanel sub = new JPanel();
		sub.setLayout(new FlowLayout(FlowLayout.LEADING));

// Ajoute les boutons de gestion 
		ajoute = new JButton("Ajoute");
		ajoute.setForeground(Color.black);
		ajoute.setBackground(buttonColor);
		ajoute.setToolTipText("Ajoute un �l�ment dans le tableau");
		ajoute.setActionCommand("ajoute");
		ajoute.addActionListener(this);		
		sub.add(ajoute);

		// Ajoute les boutons de gestion 
		supprime = new JButton("Supprime");
		supprime.setForeground(Color.black);
		supprime.setBackground(buttonColor);
		supprime.setToolTipText("Supprime un �l�ment dans le tableau");
		supprime.setActionCommand("supprime");
		supprime.addActionListener(this);		
		sub.add(supprime);

		modifie = new JButton("Modifie");
		modifie.setForeground(Color.black);
		modifie.setBackground(buttonColor);
		modifie.setToolTipText("Modifie un élément dans le tableau");
		modifie.setActionCommand("modifie");
		modifie.addActionListener(this);		
		sub.add(modifie);


		this.add(sub, BorderLayout.NORTH);
		

 		fields = new PropertyValueUpdater();
	}
	
	/** 
	 * Affiche les donn�es d'un noeuds
	 * 
	 */
	public void showNode (DesignTreeNode node) throws CataValuesException{
		// Modifie l'affichage des boutons
		if (node.requireButtons()) {
			supprime.setEnabled(true);
			ajoute.setEnabled(true);
			buttonsShown = true;
		} else {
			supprime.setEnabled(false);
			ajoute.setEnabled(false);
			buttonsShown = false;
		}
		// Modifie les données de la table
		model.setNode (node);
		selectedRow = 0;
	}

	/***
	 * Gestion des boutons d'ajout / suppression 
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent action) {

		/** Ajoute une ligne dans le tableau **/
		if (action.getActionCommand().equals("ajoute")) {
			model.addRow(selectedRow);
		}
		/** supprime une ligne du tableau **/
		if (action.getActionCommand().equals("supprime")) {
			model.deleteRow(selectedRow);
		}
		/** Modification de la valeur dans le tableau  **/
		if (action.getActionCommand().equals("modifie")) {
			// Affiche la fenentre de modification des données 
			TreeNodeProperty prop = model.getProperty(selectedRow);
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			boolean ok = fields.showEditionScreen(prop, topFrame);
			if (ok) {
				// modifie la valeur
				model.getNode().updateValue(prop.nom, prop.value);
				model.refreshData();
//				model.setValueAt(fields.getValue(), 1, selectedRow);
		    	control.showDessin(model.getNode());
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		ListSelectionModel lsm = (ListSelectionModel)arg0.getSource();
        if (lsm.isSelectionEmpty()) {
            /** disable des buttons **/
        	supprime.setEnabled(false);
        }
        else{
        	this.selectedRow = lsm.getMaxSelectionIndex();
        	table.setEditingRow(selectedRow);
        	TreeNodeProperty prp = this.model.getProperty(selectedRow);
        	if (prp != null) modifie.setEnabled(prp.editable); 
        	
        	
            /** enable des buttons si necessaire**/
        	supprime.setEnabled(this.buttonsShown);
        	
        	this.repaint();
        }
	}

}
