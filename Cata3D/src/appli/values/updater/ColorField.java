package appli.values.updater;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ColorField extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8259908935961026817L;

	private JColorChooser val;
	
	public ColorField (String nom) {
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(new JLabel(nom));

		val = new JColorChooser();		
		add(val);
	}

	public Color getValue() {
		return val.getColor();
	}

	public void setField (Color value) {
		val.setColor(value);
	}
	
}
