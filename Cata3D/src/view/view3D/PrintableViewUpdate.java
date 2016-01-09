package view.view3D;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.jogamp.opengl.GL2;

import view.scene.PrintableObject;
import view.scene.PrintableScene;

public class PrintableViewUpdate extends ViewUpdate implements ListCellRenderer<PrintableObject> {

	public PrintableScene scene;
	
	public PrintableViewUpdate(String inc) {
		super(inc);
		
	}
	
	protected void setScene (PrintableScene scn) {
		scene = scn;
	}
	
	/**
	 *  Affichage des donn�es dans le CANVAS
	 *   
	 * @param gl
	 */
    public void drawInside(GL2 gl) {
    	super.drawInside(gl);
		
    	if (scene == null) return ;
    	
    	scene.printScene(gl, echelle);
	}

	/** 
	 * D�finit le composant � afficher :
	 *    --> label noir avec en fond blanc le nom de la forme
	 *      --> gras si selectionn�
	 */
	@Override
	public Component getListCellRendererComponent(JList<? extends PrintableObject> list,
			PrintableObject value, int index, boolean isSelected, boolean cellHasFocus) {

		JLabel lab = new JLabel (value.name);
		lab.setBackground(Color.black);
		lab.setForeground(Color.white);
		
		if (value.toBePrinted) lab.setFont(new Font("Arial", Font.BOLD, 12));
		else lab.setFont(new Font("Arial", Font.PLAIN, 12));
		return lab;
	}

}
