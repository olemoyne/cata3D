package appli.values.updater;

import java.awt.FlowLayout;

import javax.swing.JPanel;

/** 
 * Permet de mettre à jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class ObjectUpdater extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301380L;

	public ObjectUpdater () {
		super();
		setLayout(new FlowLayout(FlowLayout.LEADING));
	}
	
	public void setProperty (Object val) {
	}
	
	public Object getData() {
		return null;
	}

}
