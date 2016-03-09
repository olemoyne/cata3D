package view.view3D;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ModeSelectionPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4496835198026241931L;
	

	public ModeSelectionPanel (PrintableObjectViewer list) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.black);


		//Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();

	    JLabel lab = new JLabel("Mode de visualisation :");
	    lab.setBackground(Color.black);
	    lab.setForeground(Color.white);
	    add(lab);
	    
	    // Ajoute les boutons
	    addButton("Perspective", "Mode perspective", list, group, true);
	    addButton("Ortho", "Mode orthonormé", list, group, false);
	    addButton("Découpe", "Mode découpe", list, group, false);
	}

	private void addButton(String nom, String action, ActionListener list, ButtonGroup grp, boolean val) {
		JRadioButton button = new JRadioButton(nom);
	    button.setActionCommand(action);
	    button.setBackground(Color.black);
	    button.setForeground(Color.white);
	    button.addActionListener(list);
	    button.setSelected(val);
	    grp.add(button);
	    this.add(button);
	
	}

}
