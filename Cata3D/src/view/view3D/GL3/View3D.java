package view.view3D.GL3;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;


public class View3D extends JPanel {

	private static final long serialVersionUID = 8333100250653626930L;

	private GLCanvas canvas;
	private ViewListener rsl;
	private ViewUpdate calc;

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
    	final GLProfile glp = GLProfile.get(GLProfile.GL3);
    	final GLCapabilities glCapabilities = new GLCapabilities(glp);
        return new GLCanvas(glCapabilities);
    }

 	public void display() {
		String pos = calc.camera.toString();
		this.position.setText(pos);		
		
 		canvas.display();
 	}

	public GL3 getGL() {
		return canvas.getGL().getGL3();
	}

}
