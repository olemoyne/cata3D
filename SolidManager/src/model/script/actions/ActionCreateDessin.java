package model.script.actions;

import java.io.File;

import tarpeia.com.model.SerialObjException;
import model.concepts.math.InvalidGeomAction;
import model.script.ScriptAction;
import model.script.ScriptContext;
import model.script.SolideReader;
import model.solid.manage.Dessin;
import model.solid.manage.InvalidSolidException;
import model.solid.manage.SolidPrintStream;

public class ActionCreateDessin extends ScriptAction {

	@Override
	public Dessin executeAction(ScriptContext dsn, String str, SolidPrintStream log) throws InvalidGeomAction {
		// Type -> Fichier / Composant
		int t = str.indexOf(":");
		if (t == -1) throw new InvalidGeomAction("Description d'action d'ajout invalide, pas de type");
		String type = str.substring(0, t);

		String infos = str.substring(t+1);
		int p = infos.indexOf(":");
		if (p == -1) throw new InvalidGeomAction("Description d'action d'ajout invalide, fichier illisible");
		
		String compName= infos.substring(0, p);
		String info = infos.substring(p+1);
		
		if (type.equals("FICHIER")) { // Lecture du composant depuis le fichier

			String fileName = info;
			try {
				// Vérifie que le fichier existe
				File fle = new File(fileName);
				if (!fle.exists()) throw new InvalidGeomAction( "Le fichier de forme n'existe pas :"+fileName);

				Dessin formeAIntegrer = Dessin.readFromFile(fileName);
				if (formeAIntegrer == null) throw new InvalidGeomAction( "Le fichier identifié ne contient pas de forme");
				dsn.setDessin(compName, formeAIntegrer);
				return formeAIntegrer;
			} catch (SerialObjException e) {
				e.printStackTrace();
				throw new InvalidGeomAction(e.getLocalizedMessage());
			} catch (InvalidSolidException e) {
				e.printStackTrace();
				throw new InvalidGeomAction(e.getLocalizedMessage());
			}
		}
		if (type.equals("COMPOSANT") ) {
			Dessin formeAIntegrer = SolideReader.readDessin(info);

			if (formeAIntegrer == null) throw new InvalidGeomAction( "Le fichier identifié ne contient pas de forme");
			dsn.setDessin(compName, formeAIntegrer);
			return formeAIntegrer;
		}
		throw new InvalidGeomAction("Action d'ajout incomprise : "+type);

	}
}
