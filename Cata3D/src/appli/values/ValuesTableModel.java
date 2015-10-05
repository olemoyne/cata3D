package appli.values;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import appli.Message;


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
	
	private CataDataManager dataProvider;

	private String nodeName;
	
	private Message log;
	
	public ValuesTableModel (CataDataManager cdm, Message l) {
		dataProvider = cdm;
		nodeName = "Patch";
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
		
		try {
			if (col ==0 )return this.dataProvider.getPropertyName(nodeName, row);
			if (col ==1 ) return this.dataProvider.getPropertyValue(nodeName, row);	
			return "Wrong structure";
		} catch (CataValuesException e) {
			e.printStackTrace();
			log.logError(e.getLocalizedMessage());
			return e.getLocalizedMessage();
		}
	}

	@Override
	public int getRowCount() {
		try {
			return dataProvider.getPropertiesCount(this.nodeName);
		} catch (CataValuesException e) {
			log.logError(e.getLocalizedMessage());
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0: return String.class;
		case 1: return String.class;
		default:
			throw new IllegalArgumentException();
		}
	}

	
    /*
     * D�termine si la cellule peut �tre �dit�e.
     */
    public boolean isCellEditable(int row, int col) {
        if (col == 1) return true;
        return false;
    }

    
    /**
     * En cas de modification de la cellule
     */
    public void setValueAt(Object value, int row, int col) {    	
    	try {
			dataProvider.setPropertyValue(this.nodeName, row, value);

			fireTableCellUpdated(row, col);
		} catch (CataValuesException e) {
			log.logError(e.getLocalizedMessage());
			e.printStackTrace();
		}
    }

    /**
     * Premet de positionner les donn�es d'un noeud
     * En mettant � jour les donne�s du tableau
     * 
     * @param name
     */
	public void setNodeName(String name) {
		nodeName = name;
		fireTableChanged(new TableModelEvent (this));
	}
}