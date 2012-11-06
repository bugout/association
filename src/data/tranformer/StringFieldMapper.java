package data.tranformer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import data.Field;

public class StringFieldMapper extends FieldMapper {

	@Override
	public void genMap(Iterator<Field> fieldIter) {
		int sid = 0;
		while (fieldIter.hasNext()) {
			Field f = fieldIter.next();
			if (!map.containsKey(f)) {
				map.put(f.getRaw(), sid);
			}
		}
	}

}
