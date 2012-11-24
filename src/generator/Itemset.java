package generator;
import java.util.*;

public class Itemset implements Comparable<Itemset> {
	
	public Itemset() {
		itemSet = new TreeSet<String>();
		support = 0.0;
	}
	
	public void addElement(String element) {
		itemSet.add(element);
	}
	
	public Set<String> getItems() {
		return itemSet;
	}
	
	public String toString() {
		return itemSet.toString();
	}
	
	public boolean contains(Itemset is) {
		return (itemSet.containsAll(is.getItems()));
		
	}
	
	public void setSupport(double support) {
		this.support = support;
	}
	
	public double getSupport() {
		return support;
	}
	
	@Override
	public int compareTo(Itemset is) {
		
		int retval = 0;
		
		if (support > is.support)
			retval =  -1;
		else if (support < is.support)
			retval = 1;
		
		return retval;
	}
	
	private Set<String> itemSet;
	private double support;
}
