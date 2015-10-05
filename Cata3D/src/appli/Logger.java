package appli;

public class Logger {
	
	public Logger () {
		
	}
	
	public void writeLog (String msg) {
		long tme = System.currentTimeMillis();
		
		System.out.print(tme);
		System.out.print(" - ");
		System.out.println(msg);
	}

}
