package model.script;

import java.util.Hashtable;

import model.solid.manage.Dessin;

/**
 * Contexte d'exécution du script
 * Contient la liste des objects en cours de manipulation 
 * 
 * @author olemoyne
 *
 */
public class ScriptContext {

	private Hashtable<String, Dessin> formes;
	
	/**
	 * Initialisation de la liste des dessins en mémoire
	 */
	public ScriptContext () {
		formes = new Hashtable<String, Dessin> ();
	}
	
	/**
	 * récupération d'un dessin
	 */
	public Dessin getDessin(String str) {
		return formes.get(str);
	}

	/**
	 * Enregistrement d'un dessin
	 */
	public Dessin setDessin(String str, Dessin dsn) {
		return formes.put(str, dsn);
	}

	/** Supprime le dessin de la liste **/
	public void removeDessin(Dessin dsn) {
		formes.remove(dsn);
	}

}
