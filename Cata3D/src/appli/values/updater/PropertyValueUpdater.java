package appli.values.updater;

import java.util.Hashtable;

import javax.swing.JFrame;

import appli.arbre.TreeNodeProperty;

public class PropertyValueUpdater {
	
	private Hashtable<String, ObjectUpdater> panels;
	/**
	 * 
	 */
	
	public PropertyValueUpdater () {
		
		panels = new Hashtable<String, ObjectUpdater>();
		
		ObjectUpdater o = new VecteurUpdater();
		panels.put(ObjectUpdater.VECTEUR, o);
		
		o = new StringUpdater();
		panels.put(ObjectUpdater.STRING, o);

		o = new IntegerUpdater();
		panels.put(ObjectUpdater.INTEGER, o);
		
		o = new GabaritUpdater();
		panels.put(ObjectUpdater.GABARIT, o);

  		o = new PoutreUpdater();
		panels.put(ObjectUpdater.POUTRE, o);

		o = new PoidsUpdater();
		panels.put(ObjectUpdater.POIDS, o);

		o = new DecimalUpdater();
		panels.put(ObjectUpdater.DECIMAL, o);
	}
	
	
	
	/**  
	 * Affiche une fenetre de confirmation de modification de la valeur **/
	public boolean showEditionScreen(TreeNodeProperty prop, JFrame frm) {
		if (prop == null) return false;
		
		ObjectUpdater pnl = this.panels.get(prop.editeurType);
		if (pnl != null) pnl.setProperty(prop.value);
		else return true;

		DialogValue dial = new DialogValue(frm, prop, pnl); 
		return dial.isOk;
	}

}
