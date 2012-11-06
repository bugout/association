package data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.tranformer.FieldTransformer;

public class Database {
	protected Schema schema;
	protected List<Record> records = new ArrayList<Record>();
	
	public Database(Schema schema) {
		this.schema = schema;		
	}
	
	public void insert(Record record) {
		records.add(record);
	}
	
	public Record get(int rid) {
		return records.get(rid);
	}
	
	
	public Schema getSchema() {
		return schema;
	}
	
	public RecordIterator getRecordIterator() {
		return new RecordIterator();
	}
	
	public FieldIterator getFieldIterator(FieldInfo info) {
		return new FieldIterator(info);
	}
	
	public void transform(FieldInfo fieldInfo, FieldTransformer transformer) {
		FieldIterator iter = getFieldIterator(fieldInfo);
		while (iter.hasNext()) {
			transformer.tranform(iter.next());
		}
	}
	
	public class FieldIterator implements Iterator<Field> {
		private int position = 0;
		private int fieldIdx = -1;
		
		public FieldIterator(FieldInfo info) {
			fieldIdx = info.getFieldIdx();
		}
		
		@Override
		public boolean hasNext() {
			return position < records.size();
		}

		@Override
		public Field next() {
			Record r = records.get(position);
			position++;
			return r.getFields().get(fieldIdx);
		}

		@Override
		public void remove() {	}
		
	}

	
	public class RecordIterator implements Iterator<Record> {
		private int position = 0;
		
		public RecordIterator() {}

		@Override
		public boolean hasNext() {
			return position < records.size();
		}

		@Override
		public Record next() {
			Record r = records.get(position);
			position++;
			return r;
		}

		@Override
		public void remove() {}
		
	}


	public int columnCount() {
		return schema.getFieldInfos().size();
	}
}
