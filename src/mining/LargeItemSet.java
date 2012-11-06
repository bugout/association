package mining;

import java.util.HashMap;
import java.util.Map;


public class LargeItemSet {
	private Map<Integer, Integer> items;
	
	public LargeItemSet(Map<Integer, Integer> items) {
		this.items = items;
	}
	
	public LargeItemSet() {
		items = new HashMap<Integer, Integer>();
	}
	
	public void addItem(int pos, int value) {
		items.put(pos, value);
	}
}
