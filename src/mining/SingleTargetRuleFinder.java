package mining;

import java.util.ArrayList;
import java.util.List;

public class SingleTargetRuleFinder extends RuleFinder {

	public SingleTargetRuleFinder(List<List<LargeItemSet>> largeSets, double minconf) {
		super(largeSets, minconf);
	}

	@Override
	public List<Rule> findRules() {
		List<Rule> rules = new ArrayList<Rule>();
		for (int size = 2; size < largeSets.size() + 1; size++) {
			List<LargeItemSet> itemSets = largeSets.get(size - 1);
			
			for (LargeItemSet itemset : itemSets) {
				for (LargeItemSet left : itemset.getLeftSets()) {
					int index = largeSets.get(size-1).indexOf(left);
					int leftsupp = largeSets.get(size-1).get(index).getSupport();
					double conf = 1.0 * leftfupp / itemset.getSupport();
					
					if (conf > minconf) {
						rules.add(new Rule());
					}
				}				
			}
		}
		return rules;
	}

}
