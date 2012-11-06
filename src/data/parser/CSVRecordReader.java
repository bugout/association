package data.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import data.Field;
import data.Record;
import data.Schema;

public class CSVRecordReader extends RecordReader {
	private static final String splitter = ",";
	protected int curid = 0;
	protected List<FieldReader> fieldReaders;
	
	public CSVRecordReader(Schema schema, String filename) throws FileNotFoundException {
		super(schema, filename);
	}

	@Override
	public Record readNext() {
		String line;
		try {
			line = fileReader.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		String[] fieldStrs = line.split(splitter);
		Vector<Field> fields = new Vector<Field>();
		
		for (int i = 0; i < fieldStrs.length; i++) {
			Field field = fieldReaders.get(i).parse(fieldStrs[i]);
			fields.add(field);
		}
		
		Record r = new Record(curid++, fields);
		return r;
	}

}
