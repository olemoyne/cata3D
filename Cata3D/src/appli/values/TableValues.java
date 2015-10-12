package appli.values;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;

import appli.Controleur;
import appli.Message;
import appli.values.updater.PropertyValueUpdater;

/***
 * Permet d'éditer les éléments de conception de la coque
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
	
	private JButton ajoute, supprime;
	
	private CataDataManager dataManager;
	
	private PropertyValueUpdater fields;
	
	private int selectedRow;
	private boolean buttonsShown;
	
	private Controleur control;
	
	private Message msg;
	
	public TableValues (CataDataManager cdm, Controleur ctrl, Message log) {
		super();
		
		
		Color buttonColor = this.getBackground();
		dataManager = cdm;
		msg = log;
		control = ctrl;

		model = new ValuesTableModel (cdm, log, ctrl);
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
		ajoute.setToolTipText("Ajoute un élément dans le tableau");
		ajoute.setActionCommand("ajoute");
		ajoute.addActionListener(this);		
		sub.add(ajoute);

		// Ajoute les boutons de gestion 
		supprime = new JButton("Supprime");
		supprime.setForeground(Color.black);
		supprime.setBackground(buttonColor);
		supprime.setToolTipText("Supprime un élément dans le tableau");
		supprime.setActionCommand("supprime");
		supprime.addActionListener(this);		
		sub.add(supprime);
		
		this.add(sub, BorderLayout.NORTH);
		
		fields = new PropertyValueUpdater(this);
		this.add(fields, BorderLayout.SOUTH);

	}
	
	/** 
	 * Affiche les données d'un noeuds
	 * 
	 */
	public void showNode (String nodeName) throws CataValuesException{
		// Modifie l'affichage des boutons
		if (dataManager.areButtonsNeeded(nodeName)) {
			supprime.setEnabled(true);
			ajoute.setEnabled(true);
			buttonsShown = true;
		} else {
			supprime.setEnabled(false);
			ajoute.setEnabled(false);
			buttonsShown = false;
		}
		// Modifie les données de la table
		model.setNodeName (nodeName);
		selectedRow = 0;
    	/** Mise à jour des données dans l'éditeur **/
		Object val = model.getValueAt(0, 1);
		
    	fields.setValue(val, model.isDataEditable(0, 1));
    	repaint();
	}

	/***
	 * Gestion des boutons d'ajout / suppression 
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent action) {

		/** Ajoute une ligne dans le tableau **/
		if (action.getActionCommand().equals("ajoute")) {
			model.addRow();
		}
		/** supprime une ligne du tableau **/
		if (action.getActionCommand().equals("supprime")) {
			model.deleteRow(selectedRow);
		}
		/** Modification de la valeur dans le tableau  **/
		if (action.getActionCommand().equals("modifie")) {
			try {
				dataManager.setPropertyValue(model.getNodeName(), selectedRow, fields.getValue());
				model.fireTableChanged(new TableModelEvent(model));
		    	control.showDessin(model.getNodeName());
			} catch (CataValuesException e) {
				e.printStackTrace();
				msg.logError(e.getLocalizedMessage());
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
        	
            /** enable des buttons si necessaire**/
        	supprime.setEnabled(this.buttonsShown);
        	
        	/** Mise à jour des données dans l'éditeur **/
    		Object val = model.getValueAt(selectedRow, 1);
    		if (val == null) this.msg.logError("Null value returned");
        	fields.setValue(val, model.isDataEditable(selectedRow, 1));
        	this.repaint();
        }
	}

}
