package model.viewer.appli;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import model.concepts.math.InvalidGeomAction;
import model.script.ActionReader;
import model.script.ScriptContext;
import model.solid.manage.SolidPrintStream;
import model.viewer.view.Scene;
import model.viewer.view.View3D;

public class ScriptActionProvider extends Thread { 
	/**
	 * Tablea ,de toutes les scenes
	 */
	private SolidPrintStream log;
	private int logPos;
	private boolean finished;
	
	private DessinViewUpdate upd;
	private View3D vue;
	
	private String scriptToRun;
	
	
	public ScriptActionProvider (DessinViewUpdate u, View3D vp) {
		/**
		 * Creation du manager de formes
		 */
		upd = u;
		vue = vp;
		log = new SolidPrintStream(System.out, SolidPrintStream.LEVEL_ALARM);
		scriptToRun = null;
		start();

	}

	
	private synchronized void execScript (String fname) throws InvalidGeomAction {
		System.out.println("Set the script file "+fname);
		finished = true;
		File fle = new File(fname);
		if (fle.canRead()) {
			ScriptContext ctx = new ScriptContext();
			// Lecture du fichier
			try {
				BufferedReader read = new BufferedReader(new FileReader(fle));
				StringBuilder data = new StringBuilder();
				String lne = read.readLine();
				while (lne != null & finished) {
					String message = lne.replaceAll("#.*$", "");
					message = message.replaceAll("\\s+", "");
					data.append(message);
					if (message.endsWith(";")) { // fin de ligne
						/** Cleaning logs **/
						log.scenes.clear();
						this.logPos = 0;
						
						String[] inputs = ActionReader.getInput(data.toString());
						ActionReader.execScript(ctx, inputs[0], inputs[1], log);

						if (log.size() > 0) {
							finished = false;
							this.logPos = 0;
							upd.setDessin(log.getScene(logPos));
							
							while (!finished) {
								try {
									wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
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

	public synchronized void run ( ) {
		while (this.scriptToRun == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			if (scriptToRun != null) {
				try {
					this.execScript(scriptToRun);
				} catch (InvalidGeomAction e) {
					e.printStackTrace();
				}
				scriptToRun = null;
			}
		}
		
	}
	
	public synchronized void runScript(String str) {
		scriptToRun = str;
		notify();
	}
	
	
	public synchronized void nextScene () {
		if( log.size() == 0) return;
		this.logPos ++;
		if (logPos > log.size()-1) {
			logPos = log.size()-1;
			finished = true;
			notify();
		}
		Scene dessin = log.getScene(logPos);
		this.upd.setDessin(dessin);	
		this.vue.display();
	}
	

}
