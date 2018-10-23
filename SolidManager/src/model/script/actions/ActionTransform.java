package model.script.actions;

import model.concepts.math.Decimal;
import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Vecteur;
import model.concepts.solid.Slice;
import model.concepts.solid.Solide;
import model.concepts.transfo.Reflexion;
import model.concepts.transfo.Rotation;
import model.concepts.transfo.Transformation;
import model.concepts.transfo.Translation;
import model.script.ScriptAction;
import model.script.ScriptContext;
import model.solid.manage.Dessin;
import model.solid.manage.SolidPrintStream;

public class ActionTransform extends ScriptAction {

	@Override
	public Dessin executeAction(ScriptContext dsn, String info, SolidPrintStream log) throws InvalidGeomAction {
		// Target : type : Data
		String [] data = info.split(":");
		if (data.length < 3) throw new InvalidGeomAction("Description d'action de transfo invalide");

		Dessin forme = dsn.getDessin(data[1]);
		if (forme == null) throw new InvalidGeomAction("Description d'action de transfo invalide, forme inconnue");
		
		if (data[0].equals("ROTATION")) {
			int axe = Integer.parseInt(data[2]);
			Decimal angle = Decimal.parse(data[3]);
			Rotation trans = new Rotation(axe, angle, null);

			appliqueTransfo(forme, data[1], trans);
			return forme;
		}
		if (data[0].equals("TRANSLATION")) {
			Vecteur trs = new Vecteur(data[2]);
			Translation trans = new Translation(trs, null);

			appliqueTransfo(forme, data[1], trans);
			return forme;
		}
		if (data[0].equals("REFLEXION")) {
			int axe = Integer.parseInt(data[2]);
			Reflexion trans = new Reflexion(axe, null);

			appliqueTransfo(forme, data[1], trans);
			return forme;
		}
		throw new InvalidGeomAction("Action de transformation incomprise : "+data[0]);
	}
	
	private static String appliqueTransfo (Dessin frm, String nomForme, Transformation trans) throws InvalidGeomAction {

		// application de la transformation
		if (frm.dessinType == Dessin.DESSIN_TYPE_SOLIDE) {
			Solide sld = (Solide)frm.forme;
			Solide pol = sld.transfo(trans);

			frm.forme = pol;
			return "OK";
		}
		// application de la transformation
		if (frm.dessinType == Dessin.DESSIN_TYPE_SLICE) {
			Slice sld = (Slice)frm.forme;
			Slice pol = sld.transfo(trans);

			frm.forme = pol;
			return "OK";
		}
		return "KO";
	}


}
