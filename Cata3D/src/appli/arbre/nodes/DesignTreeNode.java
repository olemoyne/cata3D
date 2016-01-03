package appli.arbre.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import appli.arbre.TreeNodeProperty;
import appli.values.CataValuesException;
import view.scene.PrintableScene;

public class DesignTreeNode extends DefaultMutableTreeNode {

	
	public static int LEVEL_CATA = 0;
	public static int LEVEL_COMPOSANT = 1;
	public static int LEVEL_COMPOSANT_DESIGN = 2;
	public static int LEVEL_CATA_DESIGN = 3;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 815607850685164482L;
	
	private String nomAffiche;

	public DesignTreeNode (DefaultMutableTreeNode up, String name) {
		super();
		nomAffiche = name;
		if (up != null) up.add(this);
	}
	
	public String toString(){
		return nomAffiche;
	}
	
	public boolean requireButtons() {
		return false;
	}
	
	public ArrayList<TreeNodeProperty> getProperties() {
		return new ArrayList<TreeNodeProperty> ();
	}

	public void updateValue (String fld, Object value) {
		
	}

	public void addProperty(TreeNodeProperty treeNodeProperty) throws CataValuesException{
		// TODO Auto-generated method stub
		
	}

	public void removeProperty(TreeNodeProperty treeNodeProperty) throws CataValuesException{
		// TODO Auto-generated method stub
		
	}

	public PrintableScene getScene() {
		return new PrintableScene();
	}

	public TreeNodeProperty getNewProperty() {
		// TODO Auto-generated method stub
		return null;
	}

}
