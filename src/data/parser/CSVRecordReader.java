package data.parser;

import java.io.FileNotFoundException;import java.io.IOException;
import java.util.List;
import java.util.Vector;

import data.Field;
import data.FieldInfo;
import data.Record;
import data.Schema;

public class CSVRecordReader extends RecordReader {
	private static final String splitter = ",";
	
	public CSVRecordReader(Schema schema, String filename) throws FileNotFoundException {
		super(schema, filename);
	}

	@Override
	public Record readNext() {
		String line;
		try {
			line = fileReader.readLine();
			if (line == null)
				return null;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		String[] fieldStrs = line.split(splitter);
		Vector<Field> fields = new Vector<Field>();

		System.err.println(line);
		
		List<FieldInfo> infos = schema.getFieldInfos();
		for (int i = 0; i < fieldStrs.length; i++) {
			if (infos.get(i).isSkip())
				continue;
			Field field = new Field(fieldStrs[i]);
			fields.add(field);
		}
		
		Record r = new Record(fields);
		return r;
	}

}
