package data;

import java.io.IOException;

import data.parser.CSVRecordReader;
import data.parser.RecordReader;
import data.tranformer.FieldMapper;
import data.tranformer.FieldTransformer;

public class DataLoader {
	protected String dataFileName;
	protected String configFileName;
	
	public DataLoader(String datafile, String configfile) {
		dataFileName = datafile;
		configFileName = configfile;
	}

	public Database load() throws IOException {
		Schema schema = Schema.readSchema(configFileName);
		Database db = new Database(schema);
		
		// PASS 1: Read in strings
		RecordReader rr = new CSVRecordReader(schema, dataFileName);
		
		Record record;
		while ((record = rr.readNext()) != null) {
			db.insert(record);
		}
		// PASS 2: Transform values
		for (FieldInfo fieldInfo : schema.getFieldInfos()) {
			FieldMapper mapper = FieldMapper.createMapper(fieldInfo);
			mapper.genMap(db.getFieldIterator(fieldInfo));
			FieldTransformer transformer = new FieldTransformer(mapper);
			db.transform(fieldInfo, transformer);
		}
		
		return db;
	}
}