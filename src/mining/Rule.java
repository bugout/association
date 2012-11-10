package mining;

import java.util.Map;

public class Rule {
	private Map<Integer, Integer> sources;
	private int targetPos;
	private int targetValue;
	
	public Rule(Map<Integer, Integer> src, int tgtpos, int tgtval) {
		sources = src;
		targetPos = tgtpos;
		targetValue = tgtval;
	}
	
}
