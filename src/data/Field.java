package data;

public class Field {
	protected String raw;
	
	public Field(String raw) {
		this.raw = raw;
	}	
	
	public String getRaw() {
		return raw;
	}
	
	public void setRaw(String value) {
		raw = value;
	}
	
	public boolean isNull() {
		return raw == null;
	}
}
