package generator;
import java.util.*;

/*
 * Itemset
 * Save support for each itemset
 * Use a TreeSet to store all items to ensure ordering
 */
public class Itemset implements Comparable<Itemset> {
	
	public Itemset() {
		itemSet = new TreeSet<String>();
		support = 0.0;
	}
	
	public Itemset(TreeSet<String> items) {
		itemSet = items;
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
	
	/*
	 * Check if two itemsets contain the same items
	 */
	public boolean containsSameItems(Itemset is) {
		
		boolean retval = true;
		
		if (itemSet.size() != is.getItems().size())
			return false;
		
		Iterator<String> set1itr = itemSet.iterator();
		Iterator<String> set2itr = is.getItems().iterator();
		
		for (int i = 0; i < itemSet.size() && retval; ++i)
		{
			String item1 = set1itr.next();
			String item2 = set2itr.next();
			
			if (item1 != item2)
				retval = false;
		}
			
		return retval;
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
