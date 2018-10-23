package model.script.actions;

import model.concepts.math.InvalidGeomAction;
import model.concepts.solid.Slice;
import model.concepts.solid.Solide;
import model.script.ScriptAction;
import model.script.ScriptContext;
import model.solid.manage.Dessin;
import model.solid.manage.SolidPrintStream;

public class ActionDuplicate extends ScriptAction {

	@Override
	public Dessin executeAction(ScriptContext dsn, String info, SolidPrintStream log) throws InvalidGeomAction {
		// Type, source, target
		String [] data = info.split(":");
		if (data.length < 2) throw new InvalidGeomAction("Description d'action de recopie invalide");
		String nomFormeSource = data[0];
		String nomFormeCible = data[1];
		
		Dessin source = dsn.getDessin(nomFormeSource);
		if (source == null) throw new InvalidGeomAction("Forme "+nomFormeCible+" non définie dans le dessin");
		
		Dessin cible = new Dessin();
		cible.color = source.color;
		cible.densite = source.densite;
		cible.matiere = source.matiere;
		if (cible.dessinType == Dessin.DESSIN_TYPE_SOLIDE)  {
			Solide sld = (Solide)source.forme;
			cible.forme = new Solide(sld.getFacettes());
		}
		if (cible.dessinType == Dessin.DESSIN_TYPE_SLICE)  {
			Slice sld = (Slice)source.forme;
			cible.forme = new Slice(sld);
		}

		dsn.setDessin(nomFormeCible, cible);
		return cible;
	}
}
