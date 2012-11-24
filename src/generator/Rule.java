package generator;

public class Rule implements Comparable<Rule> {
	
	private Itemset lhs;
	private Itemset rhs;
	
	private double confidence;
	private double support;
	
	public Rule(Itemset lis, Itemset ris) {
		this.lhs = lis;
		this.rhs = ris;
		confidence = 0.0;
		support = 0.0;
	}
	
	public Itemset getLhs() {
		return lhs;
	}
	
	public Itemset getRhs() {
		return rhs;
	}
	
	public void setLhs(Itemset is) {
		lhs = is;
	}
	
	public void setRhs (Itemset is) {
		rhs = is;
	}	
	
	public void setSupport(double support) {
		this.support = support;
	}
	
	public void setConfidecne(double confidence) {
		this.confidence = confidence;
	}

	public double getSupport() {
		return support;
	}
	
	public double getConfidence() {
		return confidence;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(lhs.toString());
		sb.append(" => ");
		sb.append(rhs.toString());
		
		return sb.toString();
	}

	@Override
	public int compareTo(Rule rule) {
		int retval = 0;
		
		if (confidence > rule.confidence)
			retval =  -1;
		else if (confidence < rule.confidence)
			retval = 1;
		
		return retval;
	}
}
