package model.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import model.concepts.math.InvalidGeomAction;
import model.script.actions.ActionCreateDessin;
import model.script.actions.ActionCreateSlice;
import model.script.actions.ActionDelete;
import model.script.actions.ActionDuplicate;
import model.script.actions.ActionExtrusion;
import model.script.actions.ActionFusion;
import model.script.actions.ActionIntersection;
import model.script.actions.ActionReadFromFile;
import model.script.actions.ActionSaveToFile;
import model.script.actions.ActionSimplification;
import model.script.actions.ActionTransform;
import model.solid.manage.Dessin;
import model.solid.manage.SolidPrintStream;

public class ActionReader {


	
	public static String[] getInput (String data) throws InvalidGeomAction{
		String message = data.replaceAll("\\s+", "");
		message = message.replaceAll(";$", "");
		
		if (message.length() == 0) return null;
		
		int p = message.indexOf(":");
		if (p == -1) throw new InvalidGeomAction("Description d'action invalide, pas de type");
		
		String action = message.substring(0, p);
		String info = message.substring(p+1);	
		
		return new String []  {action, info};
	}
	/**
	 * Ex�cute une action sur un dessin donn� 
	 * 
	 * @param dsn
	 * @param data
	 * @param errors 
	 * @throws InvalidGeomAction 
	 */
	public static Dessin execScript (ScriptContext dsn, String action, String info, SolidPrintStream errors) throws InvalidGeomAction{
		// Type de forme : LIRE | AJOUTE | FUSION | TRANSFO | SAUVE
		// Donn�es de la forme
		// Separateur :		
		// Affiche l'op�ration
		StringBuilder sb = new StringBuilder("Exécution de l'opération ");
		sb.append(action);
		sb.append(" -> ");
		sb.append(info);
		
		errors.println(sb.toString());
		
		ScriptAction scriptAction = getScriptAction(action);
		
		return scriptAction.executeAction(dsn, info, errors);
	}

	private static ScriptAction getScriptAction (String action) throws InvalidGeomAction {
		if (action.equals("LIRE")) return new ActionReadFromFile();
		
		if (action.equals("AJOUTE")) return new ActionCreateDessin();
		
		if (action.equals("RECOPIE")) return new ActionDuplicate();
		
		if (action.equals("TRANSFO")) return new ActionTransform();
		
		if (action.equals("FUSION")) return new ActionFusion();

		if (action.equals("EXTRUSION")) return new ActionExtrusion();

		if (action.equals("INTERSECTION")) return new ActionIntersection();

		if (action.equals("EFFACE")) return new ActionDelete();

		if (action.equals("SAUVE")) return new ActionSaveToFile();
		
		if (action.equals("SLICE")) return new ActionCreateSlice();

		if (action.equals("SIMPLIFY")) return new ActionSimplification();

		throw new InvalidGeomAction("Action incomprise : "+action);
	}
	
	/**
	 * Ex�cute un script dans un fichier
	 * 
	 * @param fname
	 */
	public static void execScript (String fname, SolidPrintStream log) throws InvalidGeomAction {
		System.out.println("Set the script file "+fname);
		File fle = new File(fname);
		if (fle.canRead()) {
			ScriptContext ctx = new ScriptContext();
			// Lecture du fichier
			try {
				BufferedReader read = new BufferedReader(new FileReader(fle));
				StringBuilder data = new StringBuilder();
				String lne = read.readLine();
				while (lne != null) {
					String message = lne.replaceAll("#.*$", "");
					message = message.replaceAll("\\s+", "");
					data.append(message);
					if (message.endsWith(";")) { // fin de ligne
						String[] inputs = ActionReader.getInput(data.toString());
						execScript(ctx, inputs[0], inputs[1], log);
						data = new StringBuilder();
					}
					lne = read.readLine();
				}
				read.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new InvalidGeomAction ("Script error : "+e.getLocalizedMessage());
			} catch (IOException e) {
				e.printStackTrace();
				throw new InvalidGeomAction ("Script error : "+e.getLocalizedMessage());
			}
		} else throw new InvalidGeomAction("Fichier introuvable "+fname);
	}	
	
	/**
	 * Test de split de triangle
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Pas de script en entrée.");
			return;
		}
		try {
			ActionReader.execScript(args[0], new SolidPrintStream(System.out, SolidPrintStream.LEVEL_DEBUG));
		} catch (InvalidGeomAction e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
}
