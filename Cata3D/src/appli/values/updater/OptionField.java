package appli.values.updater;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptionField extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8259908935961026817L;

	private JComboBox<String> val;
	
	public OptionField (String nom, String[] list) {
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(new JLabel(nom));

		val = new JComboBox<String>();
		for (String lst : list)
			val.addItem(lst);
		
		add(val);

	}

	public Object getValue() {
		String init = (String)val.getSelectedItem();
		return init;
	}

	public void setField (Object value) {
		if (value != null)
			val.setSelectedItem(value);
	}

	public void setValues(String[] list) {
		val.removeAllItems();
		for (String lst : list)
			val.addItem(lst);
	}
	
}
