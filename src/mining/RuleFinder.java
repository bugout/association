package mining;

import java.util.List;

public abstract class RuleFinder {
	protected List<List<LargeItemSet>> largeSets;
	protected double minconf;
	
	public RuleFinder(List<List<LargeItemSet>> largeSets, double minconf) {
		this.largeSets = largeSets;
		this.minconf = minconf;
	}
	
	public abstract List<Rule> findRules();
}
