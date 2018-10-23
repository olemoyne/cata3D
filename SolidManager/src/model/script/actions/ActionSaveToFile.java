package model.script.actions;

import java.io.File;

import model.concepts.math.Decimal;
import model.concepts.math.InvalidGeomAction;
import model.concepts.solid.Solide;
import model.script.ScriptAction;
import model.script.ScriptContext;
import model.solid.manage.Dessin;
import model.solid.manage.InvalidSolidException;
import model.solid.manage.STLFile;
import model.solid.manage.SolidPrintStream;

public class ActionSaveToFile extends ScriptAction {

	@Override
	public Dessin executeAction(ScriptContext dsn, String info, SolidPrintStream log) throws InvalidGeomAction {
		String [] data = info.split(":");
		if (data.length < 3) throw new InvalidGeomAction("Description d'action de sauvegarde invalide");
		String nomForme = data[0];
		String type = data[1];
		String nomFichier = data[2].replace("!", ":");

		Dessin dessin = dsn.getDessin(nomForme);
		if (dessin == null) throw new InvalidGeomAction("Dessin recherche introuvable");
	
		File fle = new File(nomFichier);
		try {
			if (type.equals("OBJECT")) {
				dessin.writeToFile(fle);
				return dessin;
			}
			if (type.equals("STL")) {
				if (dessin.dessinType != Dessin.DESSIN_TYPE_SOLIDE) throw new InvalidGeomAction("Dessin de type incompatible");
				STLFile stl = new STLFile(3);
				stl.createBinaryFile(nomFichier, (Solide)dessin.forme, nomForme, Decimal.UN.divide(Decimal.MILLE));
				
//				stl.showFile(nomFichier);
				return dessin;
			}
		} catch (InvalidSolidException e) {
			e.printStackTrace();
			throw new InvalidGeomAction (e.getLocalizedMessage());
		}
		return dessin;
	}
}
