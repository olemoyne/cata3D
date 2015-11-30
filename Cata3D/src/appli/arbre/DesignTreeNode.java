package appli.arbre;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import appli.values.CataValuesException;
import view.scene.PrintableScene;

public class DesignTreeNode extends DefaultMutableTreeNode {

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

}
