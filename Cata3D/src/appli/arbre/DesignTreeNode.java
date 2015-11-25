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

	public DesignTreeNode (DesignTreeNode up, String name) {
		super();
		nomAffiche = name;
		if (up != null) up.add(this);
	}
	
	public String toString(){
		return nomAffiche;
	}
	
	public ArrayList<TreeNodeProperty> getProperties() {
		return null;
	}

	public PrintableScene getView() throws CataValuesException{
		// TODO Auto-generated method stub
		return null;
	}
}
