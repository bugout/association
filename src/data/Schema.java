package data;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.*;


public class Schema {
	
	static Logger logger = Logger.getLogger(Schema.class);
	
	private ArrayList<FieldInfo> fieldInfos;
	private ArrayList<FieldInfo> activeFields = new ArrayList<FieldInfo>();
	
	public Schema() {
		fieldInfos = new ArrayList<FieldInfo>();
	}
	
	public Schema(ArrayList<FieldInfo> fieldInfos) {
		this.fieldInfos = fieldInfos;
	}	
	
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
			logger.error("Reading config file error");
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
