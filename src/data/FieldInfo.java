package data;

import org.apache.log4j.*;

public class FieldInfo {
	static Logger logger = Logger.getLogger(FieldInfo.class);
	
	private String name = "";
	private boolean skip = true;
	private String type = null;
	private int partitions = 0;	
	
	public FieldInfo(String name, boolean skip, String type,
			String rOrV, int partitions) {
		this.name = name;
		this.skip = skip;
		this.type = type;
		this.partitions = partitions;
	}
	
	public FieldInfo() {		
	}
	
	public void setField(SchemaField field, String value) {
		switch (field) {
		case Name:
			name = value;
			break;
		case Type:
			type = value;
			break;
		case Skip:			
			if (value.equals("0")) {
				skip = false;
			}
			else
				skip = true;
			break;
		case Partitions:
			partitions = Integer.parseInt(value);
			break;
		default:
			logger.error("Unexpected shema type");
			break;
		}
	}
	
	public String getName() {
		return name;
	}

	public boolean isSkip() {
		return skip;
	}

	public String getType() {
		return type;
	}
	
	public int getPartitions() {
		return partitions;
	}
	
	
	
}
