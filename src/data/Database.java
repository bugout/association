package data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Represent the entire dataset, contains a list of records
 */
public class Database {
	
	protected Schema schema;
	protected List<Record> records = new ArrayList<Record>();
	
	public Database(Schema schema) {
		this.schema = schema;		
	}
	
	// output the database into a file	
	public void export(String outfile) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(outfile));
			for (Record r : records) {
				writer.write(r.toString());
				writer.newLine();
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	// Get an iterator to iterate through each record
	public RecordIterator getRecordIterator() {
		return new RecordIterator();
	}
	
	// Get an iterator to iterate through one of the column
	public FieldIterator getFieldIterator(FieldInfo info) {
		return new FieldIterator(schema.indexOf(info));
	}
	
	public void appendPrefix() {
		RecordIterator riter = new RecordIterator();
		while (riter.hasNext()) {
			Record r = riter.next();
			Vector<Field> fields = r.getFields();
			int k = 0;
			for (FieldInfo finfo : schema.getActiveFields()) {
				fields.get(k).setRaw(finfo.getName() + "-" + fields.get(k).getRaw());
				k++;
			}
		}
	}
	
	// range partition double values
	public void partitionDouble(FieldInfo fieldInfo, int partitions) {
		FieldIterator iter = getFieldIterator(fieldInfo);
		
		ArrayList<Double> values = new ArrayList<Double>();
		ArrayList<Double> boundaries = new ArrayList<Double>();
		
		// first pass, generate partition boundary
		while (iter.hasNext()) {
			Field f = iter.next();
			values.add(Double.parseDouble(f.getRaw()));
	
		}
		Collections.sort(values);
		
		int interval = records.size() / partitions;
		
		assert(interval > 0);
		
		double endpoint = 0;
		for (int i = 0; i < partitions; i++) {
			
			if (i == partitions - 1)
				endpoint = values.get(values.size() - 1);
			else
				endpoint = values.get((i+1) * interval);
			boundaries.add(endpoint);
		}
		
		// second pass, generate mapping		
		iter = getFieldIterator(fieldInfo);
		while (iter.hasNext()) {
			Field f = iter.next();		
			double v = Double.parseDouble(f.getRaw());
			int idx = 0;
			while (boundaries.get(idx) < v) {
				idx++;
			}
			f.setRaw(Integer.toString(idx));
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + fieldInfo.getName() + ".range"));
			writer.write(values.get(0).toString());
			writer.newLine();
			for (Double i : boundaries) {
				writer.write(i.toString());
				writer.newLine();
			}
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// range partition integer values
	public void partition(FieldInfo fieldInfo, int partitions) {
		if (fieldInfo.getType().equals("DOUBLE")) {
			partitionDouble(fieldInfo, partitions);
			return;
		}			
		
		FieldIterator iter = getFieldIterator(fieldInfo);
		
		ArrayList<Integer> values = new ArrayList<Integer>();
		ArrayList<Integer> boundaries = new ArrayList<Integer>();
		
		// first pass, generate partition boundary
		while (iter.hasNext()) {
			Field f = iter.next();
			values.add(Integer.parseInt(f.getRaw()));
	
		}
		Collections.sort(values);
		
		int interval = records.size() / partitions;
		assert(interval > 0);
		
		int endpoint = 0;
		for (int i = 0; i < partitions; i++) {
			if (i == partitions - 1)
				endpoint = values.get(values.size() - 1);
			else
				endpoint = values.get((i+1) * interval);
			boundaries.add(endpoint);
		}
			
		// second pass, generate mapping		
		iter = getFieldIterator(fieldInfo);
		while (iter.hasNext()) {
			Field f = iter.next();		
			int v = Integer.parseInt(f.getRaw());
			int idx = 0;
			while (boundaries.get(idx).compareTo(v) < 0) {
				idx++;
			}
			f.setRaw(Integer.toString(idx));
		}
	
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/" +fieldInfo.getName() + ".range"));
			writer.write(values.get(0).toString());
			writer.newLine();
			for (Integer i : boundaries) {
				writer.write(i.toString());
				writer.newLine();
			}
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public class FieldIterator implements Iterator<Field> {
		private int position = 0;
		private int fieldIdx = -1;
		
		public FieldIterator(int fieldIdx) {
			this.fieldIdx = fieldIdx;
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

}
