package data;

import java.io.FileNotFoundException;

/*
 * Load a database from the datafile and the given schema
 */
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
		
		// PASS 1: Read all lines and insert into the database
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
		
		// PASS 3: Append a unique prefix to each column values
		db.appendPrefix();
		
		return db;
	}
	
	public static void main(String[] args) {
		Database db = new DataLoader("data/boiler-noheader.csv", "data/schema.txt").load();
		db.export("data/integrated-dataset.csv");
	}
}