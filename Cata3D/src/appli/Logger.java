package appli;

import javax.swing.JLabel;

public class Logger {
	
	public JLabel toScreen;
	
	public Logger () {
		
	}
	
	public void writeLog (String msg) {
		long tme = System.currentTimeMillis();
		
		System.out.print(tme);
		System.out.print(" - ");
		System.out.println(msg);
		
		if (toScreen != null) toScreen.setText(msg);
	}

}
