package model.viewer.appli;

import java.awt.Color;

import javax.swing.table.AbstractTableModel;

import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Segment;
import model.concepts.math.Triangle;
import model.concepts.math.Vecteur;
import model.solid.manage.*;
import model.viewer.view.Scene;

public class FormesTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1421834489650905810L;

	public Scene dessin;
	
	private final String[] entetes = { "Nom", "Type", "Couleur", "Point 1", "Point 2", "Point 3" };	
	
	@Override
	public int getColumnCount() {
		return entetes.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return entetes[columnIndex];
	}
	
	public String getVecteur(Vecteur v) {
		StringBuilder sb = new StringBuilder();
		sb.append(v.getDecX().toString());
		sb.append(',' );
		sb.append(v.getDecY().toString());
		sb.append(',' );
		sb.append(v.getDecZ().toString());
		return sb.toString();
	}
	
		
	@Override
	public Object getValueAt(int row, int col) {
		Composant comp = dessin.getForme(row);

		if (comp == null) return null;

		if (comp.type == Composant.TYPE_TRIANGLE) {
			Vecteur[] pts = ((Triangle)comp.forme).getSommets();
			
			switch (col) {
				case 0: return comp.nom;
				case 1: return "Triangle";
				case 2: return comp.color;
				case 3: return getVecteur(pts[0]);
				case 4: return getVecteur(pts[1]);
				case 5: return getVecteur(pts[2]);
				default:
					throw new IllegalArgumentException();
			}
		} else {
			Segment seg = (Segment) comp.forme;
			switch (col) {
				case 0: return comp.nom;
				case 1: return "Segment";
				case 2: return comp.color;
				case 3: return getVecteur(seg.getA());
				case 4: return getVecteur(seg.getB());
				case 5: return "";
				default:
				throw new IllegalArgumentException();
			}			
		}
	}

	@Override
	public int getRowCount() {
		if (dessin == null) return 1;
		return dessin.getSize();
	}

	public Composant getElementAt(int pos) {
		if (dessin == null) return null;
		return dessin.getForme(pos);
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0: return String.class;
		case 1: return String.class;
		case 2: return Color.class;
		case 3: return String.class;
		case 4: return String.class;
		case 5: return String.class;
		default:
			throw new IllegalArgumentException();
		}
	}

	
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    
    public void setValueAt(Object value, int row, int col) {
    	Composant comp = this.dessin.getForme(row);
    	
		if (comp.type == Composant.TYPE_TRIANGLE) {
			Vecteur[] pts = ((Triangle)comp.forme).getSommets();
	    	try {
		    	switch (col) {
		    	case 0 : // Nom 
		    		comp.nom = (String)value; break;
		    	case 1 : // Type
		    		break;
		    	case 2 : // Couleur
		    		comp.color = (Color) value; break;
		    	case 3 : // Point 1
		    		String str = (String)value;
		    		Vecteur v = new Vecteur(str.replace(',', ';')); comp.forme = new Triangle (v, pts[1], pts[2]); break;
		    	case 4 : // Point 2
		    		str = (String)value;
		    		v = new Vecteur(str.replace(',', ';')); comp.forme = new Triangle (pts[0], v, pts[2]); break;
		    	case 5 : // Point 3
		    		str = (String)value;
		    		v = new Vecteur(str.replace(',', ';')); comp.forme = new Triangle (pts[0], pts[1], v); break;
		    	}
	    	} catch (InvalidGeomAction e) {
	    		e.printStackTrace();
	    	}
	    	fireTableCellUpdated(row, col);
		}
    }
}
