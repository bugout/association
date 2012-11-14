package generator;

public class Rule {
	
	private Itemset lhs;
	private Itemset rhs;
	
	public Rule(Itemset lis, Itemset ris) {
		this.lhs = lis;
		this.rhs = ris;
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

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(lhs.toString());
		sb.append(" => ");
		sb.append(rhs.toString());
		
		return sb.toString();
	}
}
