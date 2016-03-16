package view.view3D.GL3;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame; 
import javax.swing.JPanel;

import view.scene.PrintableScene;
import view.scene.PrintedBlock;
import model.math.Vecteur;
 
public class ViewTest3GL extends JFrame { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1121422051200727396L;


	public ViewTest3GL() {
		super();
	}

	
    public void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(false);

        //Create and set up the window.
		setTitle("Testing the view 3D");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = getToolkit().getScreenSize();
		setBounds(0, 0, dim.width, dim.height-30);
            
        JPanel fond = new JPanel();
        fond.setLayout(new BorderLayout());

		SceneViewUpdate upd = new SceneViewUpdate("0.025");
        View3D viewer3D = new View3D(upd);
		fond.add(viewer3D, BorderLayout.CENTER);
        
        /**Ajoute la scene qui va bien **/
        PrintableScene scene = new PrintableScene();
        scene.add(new PrintedBlock(new Vecteur("0;0;0"), new Vecteur("2;2;2"), "bloc1", false, Color.red));
        scene.add(new PrintedBlock(new Vecteur("1;1;1"), new Vecteur("3;3;3"), "bloc2", false, Color.blue));
        
        upd.setScene(scene);
        
		setContentPane(fond);
		
		//Display the window.
		setVisible(true);
		
    }
	
   public static void main( String[] args ) {
	   
		ViewTest3GL ddWindow = new ViewTest3GL();
		ddWindow.createAndShowGUI();
   } //end of main 
	
} //end of class 