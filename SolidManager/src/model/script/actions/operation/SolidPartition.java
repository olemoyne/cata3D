package model.script.actions.operation;

import java.util.ArrayList;

import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Triangle;
import model.concepts.solid.Solide;
import model.script.actions.ActionOperation;
import model.solid.manage.SolidPrintStream;

public class SolidPartition {
	
	/** Liste des triangles composant les deux formes ces triangles sont inclus dans cet espace**/
	public ArrayList<Triangle> from, to;

	/** Parcours toutes les partitions pour déterminer le solide 
	 * @throws InvalidGeomAction **/
	public ArrayList<Triangle> getOperation(ActionOperation ope, SolidPrintStream log) throws InvalidGeomAction {
		Solide frm = new Solide(from);
		Solide tom= new Solide(to);
		
		log.println("Operation -> "+from.size()+" "+to.size(), SolidPrintStream.LEVEL_ALARM);
		
		Solide ret = ope.execOperation(frm, tom, log);
		if (ret == null) return null;
		return ret.getFacettes();
	}

}
