package model.script.actions;

import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Plan3D;
import model.concepts.math.Vecteur;
import model.concepts.solid.Slice;
import model.concepts.solid.Solide;
import model.script.ScriptAction;
import model.script.ScriptContext;
import model.solid.manage.Dessin;
import model.solid.manage.SolidPrintStream;

public class ActionCreateSlice extends ScriptAction {

	@Override
	public Dessin executeAction(ScriptContext dsn, String info, SolidPrintStream log) throws InvalidGeomAction {
		// source, target, plan 
		String [] data = info.split(":");
		if (data.length < 3) throw new InvalidGeomAction("Description d'action de découpe en tranche invalide");
		String nomFormeAjout = data[0];
		String nomFormeCible = data[1];
		String descPlan = data[2];
		
		// Récupère la source 
		Dessin from = dsn.getDessin(nomFormeAjout);
		if (from == null) throw new InvalidGeomAction("Forme "+nomFormeAjout+" non définie dans le dessin");
		if (from.dessinType != Dessin.DESSIN_TYPE_SOLIDE) 
			throw new InvalidGeomAction("La forme à trancher n'est pas de type solide");
		// Récupération du plan de découpe
		String [] coords = descPlan.split(" ");
		Vecteur a = new Vecteur(coords[0]);
		Vecteur o = new Vecteur(coords[1]);
		Vecteur b = new Vecteur(coords[2]);
		Plan3D pl = new Plan3D(a, o, b);
		
		Dessin ret = new Dessin();
		ret.color= from.color;
		ret.densite = from.densite;
		ret.dessinType = Dessin.DESSIN_TYPE_SLICE;
		Slice slc = new Slice (pl);
		slc.decoupe((Solide)from.forme);
		ret.forme = slc;
		
		dsn.setDessin(nomFormeCible, ret);
		
		return ret;
	}
}
