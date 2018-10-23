package model.script.actions;

import java.awt.Color;
import java.io.File;

import tarpeia.com.model.SerialObjException;
import model.concepts.math.Decimal;
import model.concepts.math.InvalidGeomAction;
import model.concepts.solid.Solide;
import model.script.ScriptAction;
import model.script.ScriptContext;
import model.solid.manage.Dessin;
import model.solid.manage.InvalidSolidException;
import model.solid.manage.STLFile;
import model.solid.manage.SolidPrintStream;

public class ActionReadFromFile extends ScriptAction {

	@Override
	public Dessin executeAction(ScriptContext dsn, String str, SolidPrintStream log) throws InvalidGeomAction {
		// Type, source, target
		String [] data = str.split(":");
		if (data.length < 4) throw new InvalidGeomAction("Description d'action invalide");

		String type = data[0];
		String filename = data[1];
		String nomDessin = data[2];
		String scale = data[3];
		int echelle = Integer.parseInt(scale);
		try {
			// V�rifie que le fichier existe
			File fle = new File(filename);
			if (!fle.exists()) throw new InvalidGeomAction( "Le fichier de forme n'existe pas");

			if (type.equals("OBJECT")) {
				Dessin formeAIntegrer= Dessin.readFromFile(filename);
				if (formeAIntegrer != null) dsn.setDessin(nomDessin, formeAIntegrer);
				return formeAIntegrer;
			}

			if (type.equals("STL")) {
				STLFile stl = new STLFile(3);
				Solide s = stl.readBinaryFile(echelle, filename);

				Dessin formeAIntegrer= new Dessin();
				formeAIntegrer.forme = s;
				formeAIntegrer.dessinType = Dessin.DESSIN_TYPE_SOLIDE;
				formeAIntegrer.densite = Decimal.UN;
				formeAIntegrer.color = Color.black.getRGB();
				if (formeAIntegrer != null) dsn.setDessin(nomDessin, formeAIntegrer);
				return formeAIntegrer;
			}
			return null;
			
		} catch (SerialObjException e) {
			e.printStackTrace();
			throw new InvalidGeomAction(e.getLocalizedMessage());
		} catch (InvalidSolidException e) {
			e.printStackTrace();
			throw new InvalidGeomAction(e.getLocalizedMessage());
		}
	}
}
