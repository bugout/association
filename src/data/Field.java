package data;

public class Field {
	protected int value;
	protected final String raw;
	
	public Field(String raw, int value) {
		this.raw = raw;
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public String getRaw() {
		return raw;
	}
	
	public boolean isNull() {
		return raw == null;
	}
}
