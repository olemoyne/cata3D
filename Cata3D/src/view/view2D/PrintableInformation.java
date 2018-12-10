package view.view2D;

import javax.swing.JTextArea;


public class PrintableInformation extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3103898284174275227L;
	
	
	public String objectData;

	public String mouseData;



	
	
	public PrintableInformation () {
		super();
	}
	
	
	public void show() {
		// Affiche la position en Z
		StringBuffer sb = new StringBuffer("");
		sb.append(objectData);
		sb.append(mouseData);
		
		setText(sb.toString());
	}

}
