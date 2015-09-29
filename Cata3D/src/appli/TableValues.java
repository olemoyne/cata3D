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
	
	private FormesTableModel model;
	private JTable table;
	
	private JButton ajoute, supprime;
	
	public TableValues () {
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

	}

	@Override
	public void actionPerformed(ActionEvent action) {

		/** Ajoute une ligne dans le tableau **
		if (action.getActionCommand().equals("ajoute")) {
			try {
				Composant cmp = new Composant ("TRIANGLE:Test:987987:[(0;0;0),(1;1;0),(1;1;1)]");
				model.dessin.addForme(cmp);
				model.fireTableDataChanged();
			} catch (InvalidGeomAction e) {
				e.printStackTrace();
			}
		}

		** recopie une ligne du tableau **
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

		** supprime une ligne du tableau **
		if (action.getActionCommand().equals("supprime")) {
			int pos = table.getSelectedRow();
			model.dessin.removeForme(pos);
			model.fireTableDataChanged();
		}

		**/

	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		ListSelectionModel lsm = (ListSelectionModel)arg0.getSource();
        if (lsm.isSelectionEmpty()) {
            /** disable des buttons **/
        	supprime.setEnabled(false);
        }
        else{
        	table.setEditingRow(lsm.getMaxSelectionIndex());
            /** enable des buttons **/
        	supprime.setEnabled(true);
        }
	}

}
