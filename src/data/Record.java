package data;

import java.util.Vector;

public class Record {
	protected final int recordId;
	protected Vector<Field> fields;
	
	public Record(int rid, Vector<Field> fields) {
		this.recordId = rid;
		this.fields = fields;
	}
	
	public int getId() {
		return recordId;		
	}
	
	public Vector<Field> getFields() {
		return fields;
	}
	
	public int getFieldValue(int fieldIdx) {
		return fields.get(fieldIdx).getValue();
	}
}
