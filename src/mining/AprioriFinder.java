package mining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Database;
import data.Field;
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
					Map<Integer, Integer> items = new HashMap<Integer, Integer>();
					items.put(i, count.getKey());
					LargeItemSet set = new LargeItemSet(items, count.getValue());
					initSet.add(set);
				}
			}
		}
		
		return initSet;
	}
	
	@Override
	public List<List<LargeItemSet>> findLargeItemSets() {
		List<List<LargeItemSet>> largeItemSets = new ArrayList<List<LargeItemSet>>(); 
		largeItemSets.add(initializeItemSets());
		
		int k = 0;
		while (largeItemSets.get(k).size() > 0) {
			List<LargeItemSet> candidates = generateCandidates(largeItemSets.get(k), k+1);
			
			List<LargeItemSet> large = verify(candidates);
			
			largeItemSets.add(large);
			
			k++;
		}
		
		return largeItemSets;
	}

	private List<LargeItemSet> verify(List<LargeItemSet> candidates) {
		Map<LargeItemSet, Integer> supports = new HashMap<LargeItemSet, Integer>();
		for (LargeItemSet s : candidates) {
			supports.put(s, 0);		
		}
		
		RecordIterator iter = db.getRecordIterator();
		
		while (iter.hasNext()) {
			Record r = iter.next();
			List<LargeItemSet> sets = setsInRecord(candidates, r);
			for (LargeItemSet set : sets) {
				supports.put(set, supports.get(set) + 1);
			}
		}

		List<LargeItemSet> results = new ArrayList<LargeItemSet>();
		for (Map.Entry<LargeItemSet, Integer> setsup : supports.entrySet()) {
			if (setsup.getValue() > minsupp) {
				setsup.getKey().setSupport(setsup.getValue());
				results.add(setsup.getKey());			
			}
		}
		
		return results;
	}
	
	private List<LargeItemSet> setsInRecord(List<LargeItemSet> candidates, Record r) {
		List<LargeItemSet> sets = new ArrayList<LargeItemSet>();
		for (LargeItemSet set : candidates) {
			if (set.isInRecord(r)) {
				sets.add(set);
			}
		}
		return sets;
	}
	

	/*
	 * Generate candidate large item sets for round k+1, using large item sets in round k
	 */
	private List<LargeItemSet> generateCandidates(List<LargeItemSet> list, int size) {
				
		List<LargeItemSet> candidates = new ArrayList<LargeItemSet>();
		
		for (LargeItemSet setA : list) {
			for (LargeItemSet setB : list) {
				LargeItemSet joinSet = LargeItemSet.join(setA, setB);
				if (joinSet != null && joinSet.size() == size) {
					candidates.add(joinSet);
				}
			}
		}
		
		return candidates;
	}

}
