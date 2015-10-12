package appli.values.updater;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.math.Decimal;

public class DecimalField extends JPanel implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8259908935961026817L;

	private JTextField val;
	
	public DecimalField (String nom, String metric) {
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(new JLabel(nom));
		
		val = new JTextField(10);
		val.addKeyListener(this);
		add(val);

		add(new JLabel(metric));

	}

	public Object getValue() {
		if (validateField())
			return new Decimal(val.getText());
		else return null;
	}

	public void setField (Object value) {
		val.setText(value.toString());
	}
	
	public boolean validateField () {
		String init = val.getText();
		
		if (init.length() == 0 ) return false;
		
		String text = init.replace(',', '.');
		
		
		try {
			Decimal.parse(text);
			val.setForeground(Color.black);
			val.setText(text);
			return true;
		} catch (NumberFormatException e) {
			val.setForeground(Color.red);
		}
		return false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		validateField();		
	}


	@Override
	public void keyPressed(KeyEvent e) {
	}


	@Override
	public void keyReleased(KeyEvent e) {
	}

}
