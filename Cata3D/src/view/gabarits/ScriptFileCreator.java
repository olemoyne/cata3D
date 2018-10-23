package view.gabarits;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import model.Area;
import model.math.Decimal;
import model.math.Vecteur;

import java.text.SimpleDateFormat;

import view.scene.PrintableObject;
import view.scene.PrintedGabarit;

public class ScriptFileCreator {

	public static void createFile(PrintedGabarit pg, int pos, PrintingParameters params, String catafilename) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
		String dte = sdf.format(new Date());

		// --> Supprime l'extention
		String cataFile = catafilename;
		int p = catafilename.lastIndexOf('.');
		if (p != -1) {
			cataFile = catafilename.substring(0, p);
		}
		
		File nomFichier = new File(params.fileName + File.separator+cataFile+"_"+pos+"."+dte+".scr");// ou jpg 
		FileWriter write = new FileWriter(nomFichier); 
		
		write.write("#File = "+catafilename+"\n");
		write.write("#Tranche = "+pos+" - "+pg.zPosition+" - "+pg.epaisseur+"\n");
		sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"+"\n"); 
		write.write("#Date ="+sdf.format(new Date()));

		//Affiche les instructions
		write.write("import FreeCAD\nimport Sketcher\n\n\n");
		
		write.write("Gui.activateWorkbench(\"SketcherWorkbench\")"+"\n");
		
		String forme = createAreaScript(pg.fullInside, "Forme", Decimal.ZERO);
		write.write(forme);

		int i = 0;
		for (Area tr : pg.trous) {
			String trou = createAreaScript(tr, "Trou"+i, Decimal.ZERO);
			write.write(trou);
			i++;
		}

		write.close();
	}


	
	private static String getGeometryLine(Vecteur v1, Vecteur v2, Decimal Zpos, String name) {
		StringBuilder sb = new StringBuilder("App.ActiveDocument.");
		sb.append(name);
		sb.append(".addGeometry(Part.Line(App.Vector(") ;
		sb.append(v1.getDecX().multiply(Decimal.MILLE)) ;
		sb.append(", ") ;
		sb.append(v1.getDecY().multiply(Decimal.MILLE)) ;
		sb.append(", ") ;
		sb.append(Zpos.multiply(Decimal.MILLE)) ;
		sb.append("),App.Vector(") ;
		sb.append(v2.getDecX().multiply(Decimal.MILLE)) ;
		sb.append(", ") ;
		sb.append(v2.getDecY().multiply(Decimal.MILLE)) ;
		sb.append(", ") ;
		sb.append(Zpos.multiply(Decimal.MILLE)) ;
		sb.append(")),False)\n");
		
		return sb.toString();
	}
	

	private static String createAreaScript (Area area, String name, Decimal Zpos) {
		StringBuilder write = new StringBuilder();

		if ((area != null ) & (area.points.size()!= 0)) {
			
			write.append("App.activeDocument().addObject('Sketcher::SketchObject','"+name+"')"+"\n");
			write.append("App.activeDocument()."+name+".Placement = App.Placement(App.Vector(0.000000,0.000000,0.000000),App.Rotation(0.000000,0.000000,0.000000,1.000000))"+"\n");
			write.append("Gui.activeDocument().setEdit('"+name+"')\n");

			
			for (int i = 1; i < area.points.size(); i ++) {
					write.append(getGeometryLine(area.points.get(i-1), area.points.get(i), Zpos, name));
					if (i > 1) {
						write.append("App.ActiveDocument."+name+".addConstraint(Sketcher.Constraint('Coincident',"+(i-2)+",2,"+(i-1)+",1))"+"\n"); 
					}
			}
			int i = area.points.size();
			write.append(getGeometryLine(area.points.get(i-1) ,area.points.get(0), Zpos, name));
			
			write.append("App.ActiveDocument."+name+".addConstraint(Sketcher.Constraint('Coincident',"+(i-1)+",2,0,1))"+"\n");
	
			write.append("Gui.activeDocument().resetEdit()\n");
			write.append("App.activeDocument().recompute()\n");
		}
		return write.toString();

	}
	
	
	
	public static String extrudArea (String forme, String object, Decimal size, Decimal offset) {
		StringBuilder write = new StringBuilder();

		/** Activation du WorkBench qui permet de gérer l'extrusion **/
		write.append("Gui.activateWorkbench(\"PartWorkbench\")\n");
		write.append("FreeCAD.activeDocument().addObject(\"Part::Extrusion\",\""+object+"\")\n");
		write.append("FreeCAD.activeDocument()."+object+".Base = FreeCAD.getDocument(\"Unnamed\")."+forme+"\n");
		write.append("FreeCAD.activeDocument()."+object+".Dir = (0,0,"+size.multiply(Decimal.MILLE)+")\n");
		write.append("FreeCAD.activeDocument()."+object+".Solid = (True)\n");
		write.append("FreeCAD.activeDocument()."+object+".TaperAngle = (0)\n");
		write.append("FreeCAD.activeDocument()."+object+".Label = '"+object+"'\n");
		write.append("Gui.activeDocument()."+forme+".Visibility=False\n");
		
		if (offset != null) {
			write.append("FreeCAD.activeDocument()."+object+".Placement = App.Placement(App.Vector(0,0,"+offset.multiply(Decimal.MILLE)+"),App.Rotation(App.Vector(0,0,1),0))\n");
		}
		write.append("App.activeDocument().recompute()\n");

		return write.toString();
	}

	
	/**
	 *     
	 * @param toCut
	 * @param cutter
	 * @return
	 */
	public static String cutAreas(String toCut, String cutter, String result) {
		StringBuilder write = new StringBuilder();

		write.append("App.activeDocument().addObject(\"Part::Cut\",\""+result+"\")\n");
		write.append("App.activeDocument()."+result+".Base = App.activeDocument()."+toCut+"\n");
		write.append("App.activeDocument()."+result+".Tool = App.activeDocument()."+cutter+"\n");
		write.append("App.activeDocument()."+toCut+".Visibility=False\n");
		write.append("App.activeDocument()."+cutter+".Visibility=False\n");
		write.append("Gui.ActiveDocument."+result+".ShapeColor=Gui.ActiveDocument."+toCut+".ShapeColor\n");
		write.append("Gui.ActiveDocument."+result+".DisplayMode=Gui.ActiveDocument."+toCut+".DisplayMode\n");
		write.append("App.ActiveDocument.recompute()\n");

		return write.toString();
	}


	public static void createPositionFile(ArrayList<PrintableObject> allObjects, PrintingParameters params,	String catafilename) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
		String dte = sdf.format(new Date());

		// --> Supprime l'extention
		String cataFile = catafilename;
		int p = catafilename.lastIndexOf('.');
		if (p != -1) {
			cataFile = catafilename.substring(0, p);
		}
		
		File nomFichier = new File(params.fileName + File.separator+cataFile+"."+dte+".scr");// ou jpg 
		FileWriter write = new FileWriter(nomFichier); 
		
		write.write("#File = "+catafilename+"\n");
		sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"+"\n"); 
		write.write("#Date ="+sdf.format(new Date()));

		//Affiche les instructions
		write.write("import FreeCAD\nimport Sketcher\n\n\n");
		
		write.write("Gui.activateWorkbench(\"SketcherWorkbench\")"+"\n");

		int pos = 0;
		for (PrintableObject po : allObjects) {
			PrintedGabarit pg = (PrintedGabarit) po;
			write.write("#Tranche = "+pos+" - "+pg.zPosition+" - "+pg.epaisseur+"\n");

			StringBuilder formeName = new StringBuilder("Forme_");
			formeName.append(pos);

			String forme = createAreaScript(pg.fullInside, formeName.toString(), pg.zPosition);
			write.write(forme);

			StringBuilder objectName = new StringBuilder("Tranche_");
			objectName.append(pos);

			write.write(extrudArea(formeName.toString(), objectName.toString(), pg.epaisseur, null));
			
			int i = 0;
			Decimal ep2 = pg.epaisseur.add(Decimal.DEUX.divide(Decimal.MILLE));
			StringBuilder cutName= objectName;
			StringBuilder lastName = objectName;

			for (Area tr : pg.trous) {
				StringBuilder trouName = new StringBuilder("Trou_");
				trouName.append(pos);
				trouName.append("_");
				trouName.append(i);
				String trou = createAreaScript(tr, trouName.toString(), pg.zPosition);
				write.write(trou);

				StringBuilder trou2Name = new StringBuilder(trouName);
				trou2Name.append("_extr");

				// Extrude le trou 
				write.write(extrudArea(trouName.toString(), trou2Name.toString(), ep2, Decimal.UN.divide(Decimal.MILLE).negate()));

				cutName= new StringBuilder(objectName);
				cutName.append("_");
				cutName.append(i);

				// Construit la tranche en effectuant les trous
				write.write(cutAreas(lastName.toString(), trou2Name.toString(), cutName.toString()) );
				lastName = new StringBuilder(cutName.toString());
				
				i++;
			}
			write.write("FreeCAD.activeDocument().getObject(\""+lastName.toString()+
					 "\").Placement = App.Placement(App.Vector(0,0,"+pg.zPosition.multiply(Decimal.MILLE)+"),App.Rotation(App.Vector(0,0,1),0))\n");
			pos ++;
			
		}

		write.close();
	}
		
}
