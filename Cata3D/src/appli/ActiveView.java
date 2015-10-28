package appli;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import view.gabarits.GabaritDetailsViewer;
import view.scene.PrintableScene;
import view.view3D.PrintableObjectViewer;
import view.view3D.PrintableViewUpdate;

/**
 * Vue permettant de switcher le type de vue en fonction de la sc�ne � afficher.
 * 
 * @author olemoyne
 *
 */
public class ActiveView {

	private JPanel fondDeVue;
	// Vue 3D
	private PrintableObjectViewer viewer3D;
	private GabaritDetailsViewer gabView;

	private JPanel active;
	
	
	public ActiveView (JPanel fond) {
		fondDeVue = fond;
		
		/** Creation de la vue 3D **/
		PrintableViewUpdate upd = new PrintableViewUpdate("0.25");
		viewer3D = new PrintableObjectViewer(upd);
		fond.add(viewer3D, BorderLayout.CENTER);
		active = viewer3D;

		/** Creation de la vue du gabarit **/
		
	}
	
	public void setScene (PrintableScene scene) {
		if (scene.is3DScene()) {
			if (active != viewer3D) {
				viewer3D.setScene(scene);
				fondDeVue.remove(active);
				fondDeVue.add(viewer3D, BorderLayout.CENTER);
				active = viewer3D;
			}
		} else {
			fondDeVue.remove(active);
			fondDeVue.add(viewer3D, BorderLayout.CENTER);
			active = viewer3D;
		}
	}
	
	
}