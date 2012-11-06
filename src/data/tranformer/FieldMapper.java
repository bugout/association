package data.tranformer;

import java.util.Iterator;
import java.util.Map;

import data.Field;
import data.FieldInfo;

public abstract class FieldMapper {
	protected Map<String ,Integer> map;
	
	public abstract void genMap(Iterator<Field> fieldIter);
	
	public int map(String fieldstr) {
		return map.get(fieldstr);
	}

	public static FieldMapper createMapper(FieldInfo fieldInfo) {
		return null;
	}
}
