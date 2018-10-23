package model.viewer.view;

import java.util.ArrayList;

import model.solid.manage.Composant;

/**
 *  Objefct affichable
 *  
 * @author lemoyne
 *
 */
public class Scene {
	
	public ArrayList<Composant> composants;
	public String comment; 
	
	public Scene () {
		composants = new ArrayList<>();
		comment = "";
	}

	public Composant getForme(int pos) {
		return composants.get(pos);
	}
	
	public int getSize() {
		return composants.size();
	}

	public void addForme(Composant cmp) {
		composants.add(cmp);
	}

	public void removeForme(int pos) {
		composants.remove(pos);
		
	}
}
