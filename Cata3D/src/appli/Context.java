package appli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import model.math.Decimal;


public class Context implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6098160960455857365L;
	
	public String lastCataFile;
	public Decimal echelle;

	public String filePath;
	
	public Context() {
		
	}
	
	public static int saveTofile (Context ctx) {
		File fle=new File("./context.data");

		try {
			if (!fle.exists()) fle.createNewFile();

			FileOutputStream fis = new FileOutputStream(fle);
		    ObjectOutputStream ois = new ObjectOutputStream(fis);
			
		    ois.writeObject(ctx);
		    
		    ois.close();
		} catch (IOException e) {
			return 12;
		}	
		return 0;

	}


	public static Context readFromfile () {
		File fle = new File ("./context.data");
		if (!fle.exists()) return new Context();
			
		try {
			FileInputStream fis = new FileInputStream(fle);
		    ObjectInputStream ois = new ObjectInputStream(fis);
		    
		    Context ctx = (Context)ois.readObject();
		    ois.close();
		    
		    return ctx;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}		
	}

}
