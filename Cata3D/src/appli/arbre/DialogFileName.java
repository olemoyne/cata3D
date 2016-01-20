package appli.arbre;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class DialogFileName extends JFileChooser {

	public String filename;
	public String path;
	public boolean isOk;
	
	/**
	 * Creation et affichage de la Frame avec le panel associé
	 * 
	 * @param frm
	 * @param value
	 * @param pnl
	 */
	public DialogFileName(JFrame frm, String path, String desc) {
		super(path);
		this.setLocation(frm.getMousePosition());
		this.setDialogTitle(desc);
		isOk = false;
		
	    int returnVal = this.showOpenDialog(frm);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	this.filename = this.getSelectedFile().getAbsolutePath();
	    	this.path = this.getSelectedFile().getParent();
	    	this.isOk = true;
	    } else {
		    this.isOk = false;
	    }
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4306376096092261094L;

}
