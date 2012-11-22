package data;

import java.util.Vector;

public class Record {
	protected Vector<Field> fields;
	
	public Record(Vector<Field> fields) {
		this.fields = fields;
	}

	public Vector<Field> getFields() {
		return fields;
	}
	
	public String getFieldString(int fieldIdx) {
		return fields.get(fieldIdx).getRaw();
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {
			if (i != 0)
				str.append(",");
			str.append(fields.get(i).getRaw());
		}
		return str.toString();
	}
}
