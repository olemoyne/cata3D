package view.view3D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import view.scene.PrintableObject;
import view.scene.PrintableScene;

public class PrintableObjectViewer extends View3D implements ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JList<PrintableObject> objectListTable;
	private PrintableViewUpdate view;
	
	public PrintableObjectViewer(PrintableViewUpdate viewMgr) {
		super(viewMgr);
		view = viewMgr;
		
		objectListTable = new JList<PrintableObject>();
		objectListTable.setBackground(Color.black);
		objectListTable.setCellRenderer(viewMgr);

		objectListTable.setAutoscrolls(true);
		objectListTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		objectListTable.getSelectionModel().addListSelectionListener(this);

		if (objectListTable != null) this.add(new JScrollPane(objectListTable), BorderLayout.EAST);

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
	 * Efface ou affiche les élements selon les selections 
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

}
