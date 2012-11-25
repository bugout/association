package data;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * The data schema
 * Specify how to pre-process each column in the original data file
 * Can skip a column, or range partition columns of numerical value
 */
public class Schema {
	// all columns
	private ArrayList<FieldInfo> fieldInfos;
	// columns that are not skipped
	private ArrayList<FieldInfo> activeFields = new ArrayList<FieldInfo>();
	
	public Schema() {
		fieldInfos = new ArrayList<FieldInfo>();
	}
	
	public Schema(ArrayList<FieldInfo> fieldInfos) {
		this.fieldInfos = fieldInfos;
	}	
	
	// Read a schema from the schema configuration file
	public static Schema readSchema(String configfile) {
		Schema schema = new Schema();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(configfile));
			String line;
			while ((line = reader.readLine()) != null && line.trim() != "") {
				String[] fields = line.split(",");
				
				FieldInfo info = new FieldInfo();
				int k = 0;
				for (SchemaField f : SchemaField.values()) {
					info.setField(f, fields[k]);
					k++;
				}
				
				schema.insertField(info);
			}
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return schema;
	}
	
	public void insertField(FieldInfo info) {
		fieldInfos.add(info);
		if (!info.isSkip())
			activeFields.add(info);
	}
	
	public int indexOf(FieldInfo info) {
		return activeFields.indexOf(info);
	}
	
	public List<FieldInfo> getFieldInfos() {
		return fieldInfos;
	}

	public List<FieldInfo> getActiveFields() {
		return activeFields;
	}
}
