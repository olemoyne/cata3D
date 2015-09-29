package appli.values;

public abstract class CataValuesProperty {
	
	public Object value;
	
	public abstract String getPropertyName();
	public abstract String getPropertyValue();
	
	public abstract Object validateProperty(String value) throws CataValuesException;
}
