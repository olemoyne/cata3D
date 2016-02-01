package appli.values.updater;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BooleanField extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8259908935961026817L;

	private JComboBox<String> val;
	
	public BooleanField (String nom) {
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(new JLabel(nom));

		val = new JComboBox<String>();
		val.addItem("OUI");
		val.addItem("NON");
		
		add(val);

	}

	public Boolean getValue() {
		String init = (String)val.getSelectedItem();
		if (init.equals("OUI")) return Boolean.TRUE;
		return Boolean.FALSE;
	}

	public void setField (Boolean value) {
		val.setSelectedItem("OUI");
		if ((value != null)&&(!value.equals(Boolean.TRUE)))
			val.setSelectedItem("OUI");
	}
	
}
