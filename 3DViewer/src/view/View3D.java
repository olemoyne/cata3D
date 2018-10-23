package view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import math.Dessin;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;


public class View3D extends JPanel implements Displayable{

	private static final long serialVersionUID = 8333100250653626930L;

	private GLCanvas canvas;
	ViewListener rsl;
	ViewUpdate calc;
	
	public Dessin formeAAfficher;
	
	private JLabel position;
	
	public View3D (ViewUpdate viewMgr) {
		super();
		setLayout(new BorderLayout());

		calc = viewMgr;
		canvas = getGLCanvas();
		rsl = new ViewListener(calc, this);
		canvas.addGLEventListener(calc);
		canvas.addKeyListener(rsl);
		canvas.addMouseListener(rsl);
		canvas.addMouseMotionListener(rsl);
		canvas.addMouseWheelListener(rsl);
	    add(canvas, BorderLayout.CENTER);

		position = new JLabel();
		position.setForeground(Color.blue);
		this.add(position, BorderLayout.SOUTH);
		
	}
	
	public JPanel getPanel() {
		return this;
	}

    private GLCanvas getGLCanvas() {
        final GLProfile glprofile = GLProfile.getDefault ();
        final GLCapabilities capabilities = new GLCapabilities(glprofile);
        return new GLCanvas(capabilities);
    }

 	public void display() {
		String pos = calc.camera.toString();
		this.position.setText(pos);		

 		canvas.display();
 	}

}
