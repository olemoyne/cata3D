package model.script.actions;

import model.concepts.math.InvalidGeomAction;
import model.script.ScriptAction;
import model.script.ScriptContext;
import model.solid.manage.Dessin;
import model.solid.manage.SolidPrintStream;

public class ActionDelete extends ScriptAction {

	@Override
	public Dessin executeAction(ScriptContext dsn, String info, SolidPrintStream log) throws InvalidGeomAction {

		Dessin cible = dsn.getDessin(info);
		if (cible == null) throw new InvalidGeomAction("Forme "+info+" non définie dans le dessin");
		
		dsn.removeDessin(cible);
		return cible;
	}
}
