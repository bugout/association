package mining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import data.Database;
import data.Field;
import data.FieldInfo;
import data.Database.RecordIterator;
import data.Record;

public class AprioriFinder extends LargeItemSetFinder {

	public AprioriFinder(Database db, int minsupp) {
		super(db, minsupp);
	}

	private List<LargeItemSet> initializeItemSets() {
		List<LargeItemSet> initSet = new ArrayList<LargeItemSet>();

		List<Map<Integer, Integer>> counts = new ArrayList<Map<Integer, Integer>>(db.columnCount());
		for (int i = 0; i < db.columnCount(); i++) {
			counts.add(new HashMap<Integer, Integer>());
		}
		
		RecordIterator iter = db.getRecordIterator();
		while (iter.hasNext()) {
			Record r = iter.next();
			for (int idx = 0; idx < db.columnCount(); idx++) {
				Field f = r.getFields().get(idx);
				if (!counts.get(idx).containsKey(f.getValue())) {
					counts.get(idx).put(f.getValue(), 1);
				}
				else {
					counts.get(idx).put(f.getValue(), counts.get(idx).get(f.getValue()) + 1);
				}
			}
		}
		
		for (int i = 0; i < db.columnCount(); i++) {
			for (Map.Entry<Integer, Integer> count : counts.get(i).entrySet()) {
				if (count.getValue() >= minsupp) {
					LargeItemSet set = new LargeItemSet();
					set.addItem(i, count.getKey());
					initSet.add(set);
				}
			}
		}
		
		return initSet;
	}
	
	@Override
	public List<LargeItemSet> findLargeItemSets() {
		List<List<LargeItemSet>> largeItemSets = new ArrayList<List<LargeItemSet>>(); 
		largeItemSets.add(initializeItemSets());
		
		int k = 0;
		while (largeItemSets.get(k).size() > 0) {
			List<LargeItemSet> candidates = generateCandidates(largeItemSets.get(k));
			
			List<LargeItemSet> large = verify(candidates);
			
			largeItemSets.add(large);
			
			k++;
		}
		
		List<LargeItemSet> allItemSets = new ArrayList<LargeItemSet>();
		for (List<LargeItemSet> sets : largeItemSets) {
			allItemSets.addAll(sets);
		}
		return allItemSets;
	}

	private List<LargeItemSet> verify(List<LargeItemSet> candidates) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<LargeItemSet> generateCandidates(List<LargeItemSet> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
