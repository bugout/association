package data;

import java.io.FileNotFoundException;

public class DataLoader {
	
	protected String dataFileName;
	protected String schemaFileName;
	
	public DataLoader(String datafile, String configfile) {
		dataFileName = datafile;
		schemaFileName = configfile;
	}

	public Database load() {
		Schema schema = Schema.readSchema(schemaFileName);
		Database db = new Database(schema);
		
		RecordReader rr = null;
		try {
			rr = new CSVRecordReader(schema, dataFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}				
		Record record;
		while ((record = rr.readNext()) != null) {
			db.insert(record);
		}
		rr.close();
		
		// PASS 2: Do range partition
		for (FieldInfo fieldInfo : schema.getFieldInfos()) {
			if (fieldInfo.getPartitions() != 0) {
				db.partition(fieldInfo, fieldInfo.getPartitions());
			}
		}
		
		db.appendPrefix();
		
		return db;
	}
	
	public static void main(String[] args) {
		Database db = new DataLoader("boiler-noheader.csv", "schema.txt").load();
		db.export("output-full.txt");
	}
}