package view.view3D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.math.Decimal;
import view.scene.PrintableObject;
import view.scene.PrintableScene;

public class PrintableObjectViewer extends View3D implements ListSelectionListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JList<PrintableObject> objectListTable;
	private PrintableViewUpdate view;
	//** Gestion de l'échelle **/
	private JButton echelle;
	
	public PrintableObjectViewer(PrintableViewUpdate viewMgr) {
		super(viewMgr);
		view = viewMgr;
		
		JPanel pnl = new JPanel ();
		pnl.setLayout(new BorderLayout());
		
		objectListTable = new JList<PrintableObject>();
		objectListTable.setBackground(Color.black);
		objectListTable.setCellRenderer(viewMgr);

		objectListTable.setAutoscrolls(true);
		objectListTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		objectListTable.getSelectionModel().addListSelectionListener(this);

		if (objectListTable != null) pnl.add(new JScrollPane(objectListTable), BorderLayout.CENTER);
		// Ajoute un bouton de gestion de l'�chelle
		echelle = new JButton(view.echelle.toString());
		echelle.setForeground(Color.black);
		echelle.setToolTipText("Ajuste l'échelle");
		echelle.setActionCommand("echelle");
		echelle.addActionListener(this);		
		pnl.add(echelle, BorderLayout.SOUTH);

		
		this.add(pnl, BorderLayout.EAST);
	}

	public void setScene (PrintableScene scene) {
		view.setScene(scene);
		// Ajoute les elements de la liste
		ArrayList<PrintableObject> listData = new ArrayList<PrintableObject>();
		for (PrintableObject o : scene.allObjects) if (o.toBePrinted) listData.add(o);
		objectListTable.setListData(listData.toArray(new PrintableObject[listData.size()]));
		this.display();
	}

	/*************************************************************************************************************************** 
	 * Efface ou affiche les �lements selon les selections 
	 **/
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			PrintableObject obj = objectListTable.getSelectedValue(); 
			if (obj.toBePrinted) obj.toBePrinted = false;
			else obj.toBePrinted = true;
			this.display();
		}
	}

	/** Gestion de l'échelle **/
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("echelle".equals(e.getActionCommand())) {
			// Ouvre le dialogue de mise à jour de l'échelle
			// Si OK : ajoute un composant sur le bateau
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			DialogEchelle dialg = new DialogEchelle(topFrame, view.echelle); 
			if (dialg.isOk) {
				this.view.echelle = dialg.echelle;
				this.echelle.setText(this.view.echelle.toString());
				super.display();
			}

		}

	}

	public Decimal getEchelle() {
		return view.echelle;
	}
	
}
