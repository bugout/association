package data;

import java.util.List;

public class Schema {
	
	public List<FieldInfo> fieldInfos;
	
	public Schema(List<FieldInfo> fieldInfos) {
		this.fieldInfos = fieldInfos;
	}
	
	
	public static Schema readSchema(String configfile) {
		return null;
	}
	
	
	public List<FieldInfo> getFieldInfos() {
		return fieldInfos;
	}
}
