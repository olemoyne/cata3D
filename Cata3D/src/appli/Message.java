package appli;

import java.awt.Color;

import javax.swing.JLabel;

public class Message extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8746469084715404073L;

	public Message () {
		super();
	}
	
	
	public void logError (String msg) {
		this.setForeground(Color.red);
		this.setText(msg);
	}

	public void logTrace (String msg) {
		this.setForeground(Color.blue);
		this.setText(msg);
	}

}
