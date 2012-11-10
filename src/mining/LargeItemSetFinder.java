package mining;

import java.util.List;

import data.Database;


public abstract class LargeItemSetFinder {
	protected Database db;
	protected int minsupp;
	
	public LargeItemSetFinder(Database db, int minsupp) {
		this.db = db;
		this.minsupp = minsupp;
	}
	
	public abstract List<List<LargeItemSet>> findLargeItemSets();
}
