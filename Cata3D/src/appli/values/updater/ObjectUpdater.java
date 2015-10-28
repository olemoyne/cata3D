package appli.values.updater;

import javax.swing.BoxLayout;
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
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
	public void setProperty (Object val) {
	}
	
	public Object getData() {
		return null;
	}

}
