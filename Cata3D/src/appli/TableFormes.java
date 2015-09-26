package appli;

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

import math.Composant;
import math.Dessin;
import math.InvalidGeomAction;

public class TableFormes extends JPanel implements ActionListener, ListSelectionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2179527244572915851L;
	
	private FormesTableModel model;
	private JTable table;

	private JButton supprime, recopie;
	
	public TableFormes () {
		super();
		
		Color buttonColor = this.getBackground();

		model = new FormesTableModel ();
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
		JButton label = new JButton("Ajoute");
		label.setForeground(Color.black);
		label.setBackground(buttonColor);
		label.setToolTipText("Ajoute une forme dans le dessin");
		label.setActionCommand("ajoute");
		label.addActionListener(this);		
		sub.add(label);

		// Ajoute les boutons de gestion 
		label = new JButton("Supprime");
		label.setForeground(Color.black);
		label.setBackground(buttonColor);
		label.setToolTipText("Supprime une forme dans le dessin");
		label.setActionCommand("supprime");
		label.setEnabled(false);
		label.addActionListener(this);		
		sub.add(label);
		supprime = label;


		// Ajoute les boutons de gestion 
		label = new JButton("Recopie");
		label.setToolTipText("Recopie la forme");
		label.setActionCommand("copie");
		label.setEnabled(false);
		label.addActionListener(this);		
		sub.add(label);
		recopie = label;

/**
 
 		// Ajoute les boutons de gestion 
		label = new JButton("Affiche");
		label.setToolTipText("Affiche ou masque la forme");
		label.setActionCommand("affiche");
		label.addActionListener(this);		
		sub.add(label);

 		// Ajoute les boutons de gestion 
		label = new JButton("Refresh");
		label.setForeground(Color.black);
		label.setBackground(buttonColor);
		label.setToolTipText("Mise à jour du dessin");
		label.setActionCommand("refresh");
		label.addActionListener(this);		
		sub.add(label);

		// Ajoute les boutons de gestion 
		label = new JButton("Exporte");
		label.setToolTipText("Exporte la forme");
		label.setActionCommand("export");
		label.addActionListener(this);		
		sub.add(label);
**/
		this.add(sub, BorderLayout.SOUTH);
	}

	public void setDessin(Dessin dessin) {
		model.dessin = dessin;
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent action) {

		/** Ajoute une ligne dans le tableau **/
		if (action.getActionCommand().equals("ajoute")) {
			try {
				Composant cmp = new Composant ("TRIANGLE:Test:987987:[(0;0;0),(1;1;0),(1;1;1)]");
				model.dessin.addForme(cmp);
				model.fireTableDataChanged();
			} catch (InvalidGeomAction e) {
				e.printStackTrace();
			}
		}

		/** recopie une ligne du tableau **/
		if (action.getActionCommand().equals("copie")) {
			try {
				int pos = table.getSelectedRow();
				Composant cmp = model.getElementAt(pos);
				Composant duplic = new Composant (cmp);
				model.dessin.addForme(duplic);
				model.fireTableDataChanged();
			} catch (InvalidGeomAction e) {
				e.printStackTrace();
			}
		}

		/** supprime une ligne du tableau **/
		if (action.getActionCommand().equals("supprime")) {
			int pos = table.getSelectedRow();
			model.dessin.removeForme(pos);
			model.fireTableDataChanged();
		}

	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		ListSelectionModel lsm = (ListSelectionModel)arg0.getSource();
        if (lsm.isSelectionEmpty()) {
            /** disable des buttons **/
        	supprime.setEnabled(false);
        	recopie.setEnabled(false);
        }
        else{
        	table.setEditingRow(lsm.getMaxSelectionIndex());
            /** enable des buttons **/
        	supprime.setEnabled(true);
        	recopie.setEnabled(true);
        }
	}

}
