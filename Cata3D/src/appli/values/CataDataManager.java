package appli.values;

import java.util.Hashtable;

import view.scene.PrintableScene;
import appli.values.properties.build.GabaritsValuesProvider;
import appli.values.properties.build.PlansValuesProvider;
import appli.values.properties.build.PoutresValuesProvider;
import appli.values.properties.design.PatchValuesProvider;
import appli.values.properties.design.PoidsValuesProvider;
import appli.values.properties.optimize.FlottaisonValuesProvider;
import appli.values.properties.optimize.FluiditeValuesProvider;
import model.Cata;

/**
 * Gestion des donn�es � afficher � partir du catamaran
 * 
 * @author olemoyne
 *
 */
public class CataDataManager {
	
	private Cata data;

	/** Liste des noeuds  **/
	private Hashtable<String, CataValuesProvider> providers;

	public CataDataManager() {
		providers = new Hashtable<String, CataValuesProvider>();
		providers.put("Patch", new PatchValuesProvider());
		providers.put("Poids", new PoidsValuesProvider());
		providers.put("Flottaison", new FlottaisonValuesProvider());
		providers.put("Fluidité", new FluiditeValuesProvider());
		providers.put("Gabarits", new GabaritsValuesProvider());
		providers.put("Poutres", new PoutresValuesProvider());
		providers.put("Plans", new PlansValuesProvider());
		
		// par d�faut, le cata est initialis�
//		data = new Cata();
	}
	
	/**
	 * D�termine la n�cessit� d'afficher des boutons 
	 * 
	 * @param nodeName
	 * @return
	 * @throws CataValuesException
	 */
	public boolean areButtonsNeeded(String nodeName) throws CataValuesException{
		CataValuesProvider prv = providers.get(nodeName);
		if (prv == null) throw new CataValuesException("Node name not defined : "+nodeName);
		
		return prv.areArrayButtonsNeeded();
	}

	/**
	 * Mise � jour d'une donn�es dans le model
	 * 
	 * @param nodeName
	 * @param row
	 * @param value
	 */
	public void setPropertyValue(String nodeName, int row, Object value) throws CataValuesException{
		CataValuesProvider prv = providers.get(nodeName);
		if (prv == null) throw new CataValuesException("Node name not defined : "+nodeName);
		
		prv.setProperty(data, row, value);
	}


	/**
	 * Mise � jour d'une donn�es dans le model
	 * 
	 * @param nodeName
	 * @param row
	 * @param value
	 */
	public Object getPropertyValue(String nodeName, int row) throws CataValuesException{
		CataValuesProvider prv = providers.get(nodeName);
		if (prv == null) throw new CataValuesException("Node name not defined : "+nodeName);
		
		return prv.getProperty(data, row);
	}

	/**
	 * Retourne le nombre de donn�es dans le tableau
	 * 
	 * @param nodeName
	 * @return
	 * @throws CataValuesException
	 */
	public int getPropertiesCount(String nodeName) throws CataValuesException{
		CataValuesProvider prv = providers.get(nodeName);
		if (prv == null) throw new CataValuesException("Node name not defined : "+nodeName);
		
		return prv.getPropertyCount(data);
	}

	/**
	 * Si une valeur peut �tre mise � jour
	 * 
	 * @param nodeName
	 * @return
	 * @throws CataValuesException
	 */
	public boolean isUpdatable(String nodeName, int position) throws CataValuesException{
		CataValuesProvider prv = providers.get(nodeName);
		if (prv == null) throw new CataValuesException("Node name not defined : "+nodeName);
		
		return prv.isUpdatable(data, position);
	}

	
	/**
	 * Retourne le nom de la propri�t� 
	 * 
	 * @param nodeName
	 * @param row
	 * @return
	 * @throws CataValuesException 
	 */
	public String getPropertyName(String nodeName, int row) throws CataValuesException {
		CataValuesProvider prv = providers.get(nodeName);
		if (prv == null) throw new CataValuesException("Node name not defined : "+nodeName);
		
		return prv.getPropertyName(row);
	}

	public PrintableScene getView(String nodeName) throws CataValuesException {
		CataValuesProvider prv = providers.get(nodeName);
		if (prv == null) return null;//throw new CataValuesException("Node name not defined : "+nodeName);
		
		return prv.getScene(data);
	}

	public void setData(Cata dessin) {
		this.data = dessin;
		for (CataValuesProvider prv : providers.values()) {
			prv.setProperties(dessin);
		}
	}

	public void addProperty(String nodeName) throws CataValuesException {
		CataValuesProvider prv = providers.get(nodeName);
		if (prv == null) throw new CataValuesException("Node name not defined : "+nodeName);
		
		prv.ajouter(data, 0);
		
	}

	public void deleteProperty(String nodeName, int position) throws CataValuesException {
		CataValuesProvider prv = providers.get(nodeName);
		if (prv == null) throw new CataValuesException("Node name not defined : "+nodeName);
		
		prv.supprimer(data, position);
	}

	
}
