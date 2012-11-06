package data;


public class FieldInfo {
	private final String fieldName;
	private final int fieldIdx;
	
	public FieldInfo(String fieldName, int fieldIdx) {
		this.fieldName = fieldName;
		this.fieldIdx = fieldIdx;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public int getFieldIdx() {
		return fieldIdx;
	}
	
	
	
}
