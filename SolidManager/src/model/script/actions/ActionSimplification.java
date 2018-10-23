package model.script.actions;

import model.concepts.math.Decimal;
import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Triangle;
import model.concepts.solid.Bounds;
import model.concepts.solid.Solide;
import model.solid.manage.SolidPrintStream;

public class ActionSimplification extends ActionOperation {

	@Override
	public Solide execOperation(Solide sFrom, Solide sTo, SolidPrintStream log)
			throws InvalidGeomAction {

		// SI une des formes est vide --> retourne tous les triangles
		if (sFrom.size() == 0) {return sTo;}
		if (sTo.size() == 0) {return sFrom;}
		
		// Affichage du nombre de triangles et donc d'op�ration

		Bounds b = sFrom.getBounds();
		Bounds c = sTo.getBounds();
		log.println("Bounding : "+b.toString()+" - "+c.toString(), SolidPrintStream.LEVEL_DEBUG);
		
		
		int nb = sFrom.size();
		log.print("Split des formes : "+nb+" - ", SolidPrintStream.LEVEL_DEBUG);
		
		Decimal limit = new Decimal(30000);
		int nbLimit = 0; 
		int inBound = 0;
		for (Triangle t : sFrom.getFacettes()) {
			Decimal s = t.getSurface();
			if (s.compareTo(limit) < 0) nbLimit += 1;
			Bounds tb = t.getBounds();
			if (tb.intersecs(c)) inBound ++;
		}
		log.println("A simplifier : "+nbLimit, SolidPrintStream.LEVEL_DEBUG);
		log.println("Inbounds : "+inBound, SolidPrintStream.LEVEL_DEBUG);
		

		nb = sTo.size();
		log.print("Split des formes : "+nb+" - ", SolidPrintStream.LEVEL_DEBUG);

		nbLimit = 0; 
		inBound = 0;
		for (Triangle t : sTo.getFacettes()) {
			Decimal s = t.getSurface();
			if (s.compareTo(limit) < 0) nbLimit += 1;
			Bounds tb = t.getBounds();
			if (tb.intersecs(b)) inBound ++;
		}
		log.println("A simplifier : "+nbLimit, SolidPrintStream.LEVEL_DEBUG);
		log.println("Inbounds : "+inBound, SolidPrintStream.LEVEL_DEBUG);


		return sFrom;
	}


}
