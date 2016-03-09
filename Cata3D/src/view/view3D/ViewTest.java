package view.view3D;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame; 
import javax.swing.JPanel;

import view.scene.PrintableScene;
import view.scene.PrintedBlock;
import model.math.Decimal;
import model.math.Vecteur;
import appli.ActiveView;
 
public class ViewTest extends JFrame { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1121422051200727396L;


	public ViewTest() {
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

        ActiveView vue = new ActiveView(fond, new Decimal(1));
        
        /**Ajoute la scene qui va bien **/
        PrintableScene scene = new PrintableScene();
        scene.add(new PrintedBlock(new Vecteur("0;0;0"), new Vecteur("2;2;2"), "bloc1", false, Color.red));
        scene.add(new PrintedBlock(new Vecteur("1;1;1"), new Vecteur("3;3;3"), "bloc2", false, Color.blue));
        
        vue.setScene(scene);
        
		setContentPane(fond);
		
		//Display the window.
		setVisible(true);
		
    }
	
   public static void main( String[] args ) {
	   
		ViewTest ddWindow = new ViewTest();
		ddWindow.createAndShowGUI();
   } //end of main 
	
} //end of class 