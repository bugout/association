package generator;
import java.util.*;

public class Itemset {
	
	public Itemset() {
		itemSet = new TreeSet<String>();
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
	
	private Set<String> itemSet; 
}
