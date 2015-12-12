package appli.values;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import appli.Message;
import appli.arbre.DesignTreeNode;
import appli.arbre.TreeNodeProperty;


/**
 * Le value table model est automatiquement g�r� sous la forme de :
 *    Nom --> Statique (String)
 *    Valeur --> Editable (avec validation)
 *     
 * @author olemoyne
 *
 */
public class ValuesTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1421834489650905810L;

	private final String[] entetes = { "Nom", "Valeur" };	
	
	private DesignTreeNode node;
	private ArrayList<TreeNodeProperty> nodeData;
	
	private Message log;
	
	public ValuesTableModel (Message l) {
		log = l;
	}
	
	@Override
	public int getColumnCount() {
		return entetes.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return entetes[columnIndex];
	}
		
	@Override
	public Object getValueAt(int row, int col) {

		if (nodeData == null) return ("No boat found");
		if (row >= nodeData.size()) return ("Out of value");
		TreeNodeProperty prop = nodeData.get(row);
		if (prop == null) return ("No property found");
		
		if (col ==0 )return prop.nom;
		if (col ==1 ) return prop.value;	
		return "Wrong structure";
	}

	@Override
	public int getRowCount() {
		if (nodeData == null) return 0;
		return nodeData.size();
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return String.class;
	}

	
    /*
     * D�termine si la cellule peut �tre �dit�e.
     */
    public boolean isCellEditable(int row, int col) {
    	return false;
//        if (col == 1) return true;
//        return false;
    }

    
    /**
     * En cas de modification de la cellule
     */
    public void setValueAt(Object value, int row, int col){    	
		if (nodeData == null) return;
		if (row >= nodeData.size()) return;
		TreeNodeProperty prop = nodeData.get(row);
		if (prop == null) return;
		
		node.updateValue(prop.nom, value);
		fireTableCellUpdated(row, col);
    }

    /**
     * Premet de positionner les donn�es d'un noeud
     * En mettant � jour les donne�s du tableau
     * 
     * @param name
     */
	public void setNode(DesignTreeNode nd) {
		node = nd;
		this.nodeData = node.getProperties();
		fireTableChanged(new TableModelEvent (this));
	}

	public void refreshData() {
		this.nodeData = node.getProperties();
		fireTableChanged(new TableModelEvent (this));		
	}

	
	public void addRow(int position) {
		// Ajoute une nouvelle propriete
		try {
			node.addProperty(nodeData.get(position));
			fireTableChanged(new TableModelEvent (this));
		} catch (CataValuesException e) {
			log.logError(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	
	public void deleteRow(int position) {
		// Ajoute une nouvelle propriete
		try {
			node.removeProperty(nodeData.get(position));
			fireTableChanged(new TableModelEvent (this));
		} catch (CataValuesException e) {
			log.logError(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public boolean isDataEditable(int selectedRow, int i) {
		if (nodeData == null) return false;
		if (selectedRow >= nodeData.size()) return false;
		TreeNodeProperty prop = nodeData.get(selectedRow);
		if (prop == null) return false;

		return prop.editable;
	}
	
	public DesignTreeNode getNode() {
		return node;
	}

	/**
	 * retrourne la propriété associée à la ligne donnée 
	 * */
	public TreeNodeProperty getProperty(int selectedRow) {
		if (nodeData == null) return null;
		if (selectedRow >= nodeData.size()) return null;
		TreeNodeProperty prop = nodeData.get(selectedRow);
		return prop;
	}

}
