package appli.values.updater;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StringField extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8259908935961026817L;

	private JTextField val;
	
	public StringField (String nom) {
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(new JLabel(nom));
		
		val = new JTextField(20);
		add(val);
	}

	public Object getValue() {
		return val.getText();
	}

	public void setField (Object value) {
		val.setText(value.toString());
	}
	
	public boolean validateField () {
		return true;
	}
	
}
