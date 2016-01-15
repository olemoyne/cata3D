package appli;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import model.math.Decimal;
import view.gabarits.GabaritDetailsViewer;
import view.scene.GabaritScene;
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
	
	
	public ActiveView (JPanel fond, Decimal echelle) {
		fondDeVue = fond;
		
		/** Creation de la vue 3D **/
		PrintableViewUpdate upd = new PrintableViewUpdate("0.025", echelle);
		viewer3D = new PrintableObjectViewer(upd);
		fond.add(viewer3D, BorderLayout.CENTER);
		active = viewer3D;

		/** Creation de la vue du gabarit **/
		gabView = new GabaritDetailsViewer();
		
	}
	
	public void setScene (PrintableScene scene) {
		if (scene == null) return;
		if (scene.is3DScene()) {
			viewer3D.setScene(scene);
			if (active != viewer3D) {
				fondDeVue.remove(active);
				fondDeVue.add(viewer3D, BorderLayout.CENTER);
				active = viewer3D;
			}
		} else {
			gabView.setScene((GabaritScene)scene);
			if (active != gabView) {
				fondDeVue.remove(active);
				fondDeVue.add(gabView, BorderLayout.CENTER);
				active = gabView;
			}
		}
	}

	public Decimal getEchelle() {
		return this.viewer3D.getEchelle();
	}
	
	
}
