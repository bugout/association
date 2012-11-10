package mining;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import data.Record;


public class LargeItemSet {
	private final Map<Integer, Integer> items;
	private int support;
	
	public LargeItemSet(Map<Integer, Integer> items, int support) {
		this.items = items;
		this.support = support;
	}
	
	public LargeItemSet(Map<Integer, Integer> items) {
		this.items = items;
		this.support = -1;
	}
	
	public void setSupport(int sup) {
		support = sup;
	}
	

	public int getSupport() {
		return support;
	}
	
	public int size() {
		return items.size();
	}
	
	public boolean isInRecord(Record r) {
		for (Map.Entry<Integer, Integer> item : items.entrySet()) {
			if (r.getFieldValue(item.getKey()) != item.getValue())
				return false;
		}
		return true;
	}
	
	public static LargeItemSet join(LargeItemSet a, LargeItemSet b) {
		Map<Integer, Integer> itemsA = a.items;
		Map<Integer, Integer> itemsB = b.items;
		
		Set<Integer> overlap = new HashSet<Integer>(itemsA.keySet());
		overlap.retainAll(itemsB.keySet());
		

		Map<Integer, Integer> newitems = new HashMap<Integer, Integer>();
		for (int pos : overlap) {
			if (itemsA.get(pos) != itemsB.get(pos))
				return null;
			newitems.put(pos, itemsA.get(pos));
		}
		
		Set<Integer> remainA = new HashSet<Integer>(itemsA.keySet());
		remainA.removeAll(overlap);
		Set<Integer> remainB = new HashSet<Integer>(itemsB.keySet());
		remainB.removeAll(overlap);
		
		for (int pos : remainA) {
			newitems.put(pos, itemsA.get(pos));
		}
		for (int pos : remainB) {
			newitems.put(pos, itemsB.get(pos));
		}
		
		return new LargeItemSet(newitems);
	}
}
