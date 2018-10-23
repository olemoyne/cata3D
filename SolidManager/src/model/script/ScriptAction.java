package model.script;

import model.concepts.math.InvalidGeomAction;
import model.solid.manage.Dessin;
import model.solid.manage.SolidPrintStream;


public abstract class ScriptAction {
	
	public abstract Dessin executeAction (ScriptContext dsn, String str, SolidPrintStream log) throws InvalidGeomAction;

}
