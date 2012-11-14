package generator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class ItemsetGenerator {
	
	//contains all the transactions
	private List<Itemset> transactions;
	
	//contains all large itemsets
	private List<Itemset> largeItemSets;
	
	//seed for kth iteration
	private List<Itemset> seed;
	
	private List<Rule> rules;
	
	public ItemsetGenerator() {
		transactions = new ArrayList<Itemset>();
		largeItemSets = new ArrayList<Itemset>();
		rules = new ArrayList<Rule>();
	}
	
	public void generateAssociations(String fileName, double min_support, double min_conf) {
		
		readInputFile(fileName);
		
		//return all items that meet required min_support.  We don't have to
		//add this list to largeItemSets as we are interested in sets that are 
		//more than 1 element
		List<Itemset> initialList = initilizeLargeItemSet(min_support);
		
		seed = initialList;
		
		int k = 2;
		while (!seed.isEmpty())
		{
			List<Itemset> candidateList = aprioriGen(seed, k);
			
			//calculate the support for each element in candidateList
			//and remove any element that doesn't contain the required support
			List<Itemset> removeList = new ArrayList<Itemset>();
			for (Itemset is : candidateList) {
				if (calculateSupport(is) < min_support)
					removeList.add(is);
			}
			candidateList.removeAll(removeList);
		
			//set the seed to the candidateList for the next iteration
			seed = candidateList;
			
			//add all the cadidates to the largeItemSets since they 
			//meeting the minimum support
			largeItemSets.addAll(candidateList);
			
			k++;
		}
		
		//largeItemSets contain all the required itemsets
		System.out.println("All Large Itemsets:");
		for (Itemset is : largeItemSets) {
			System.out.println(is.toString());
		}
		
		//evaluate all the association rules
		for (Itemset is : largeItemSets)
			generateRules(is, min_conf);
		
		for (Rule rule : rules) {
			System.out.println(rule.toString());
		}
	}
		
	private List<Itemset> aprioriGen(List<Itemset> seed, int k) {
		
		boolean usesql = true;
		List<Itemset> candidateList;
		
		if (usesql) {
			DBAccess db = new DBAccess();
			//this returns a new candidate list
			candidateList = db.populateLargeItemSet(seed, k);
		}
		else {
			// do the join in the memory 
			candidateList = populateLargeItemSet(seed, k);
		}
		
		/* Optimize by evaluating subsets of each candidateList of size k-1 and deleting the ones 
		 * that don't exist in the seed 
		 */
		/*
		List<Itemset> removeList = new ArrayList<Itemset>();
		for (Itemset is : candidateList)
		{
			List<Itemset> subsets = populateSubsets(is, k);
			if (!seed.containsAll(subsets))
				removeList.add(is);
		}
		
		candidateList.removeAll(removeList);
		
		for (Itemset is : candidateList) 
			System.out.println(is.toString());
		*/
		
		return candidateList;
	}
	
	private List<Itemset> populateLargeItemSet(List<Itemset> seed, int k) {
		
		List<Itemset> retlist = new ArrayList<Itemset>();
		
		for (Itemset p : seed)
		{
			for (Itemset q : seed) {
				//do a self test
				if (p != q)
				{
					boolean join = true;
					
					//get p and q sets and iterators
					Set<String> p_set = p.getItems();
					Set<String> q_set = q.getItems();
					
					Iterator<String> p_itr = p_set.iterator();
					Iterator<String> q_itr = q_set.iterator();
					
					//compare sets from 0 to k-2 elements while join is true
					//each itemset in seed should contain k-1 items
					for (int i = 0; i < k - 2 && join; ++i) {
						if (p_itr.next() != q_itr.next())
							join = false;
					}
			
					//compare k-1th element of p, is less than k-1th element of q
					if (join) {
						
						if ((p_itr.next()).compareTo((q_itr.next())) >= 0)
							join = false;
					}
					
					if (join) {
						//do the join by creating a new Itemset and adding strings to it
						Itemset is = new Itemset();
						
						p_itr = p_set.iterator();
						q_itr = q_set.iterator();
						//the first k-2 elements are the same is both p_set and q_set
						for (int i = 0; i < k-2; ++i) {
							is.addElement(p_itr.next());
							q_itr.next();
						}
						is.addElement(p_itr.next());
						
						is.addElement(q_itr.next());
						
						retlist.add(is);
					}
				}
				
			}
		}
		
		return retlist;
	}
	
	private List<Itemset> populateSubsets(Itemset itemset, int k) {
		
		List<Itemset> myList = new ArrayList<Itemset>();
		//evaluate all k-1 subsets and too the ArrayList
		
		
		return myList;
		
	}
	
	private void readInputFile(String fileName) {
	//populate the transaction dataset
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String theLine = "";
			
			while ((theLine = br.readLine()) != null) {
				String[] items = theLine.split(",");
				
				Itemset theItemSet = new Itemset();
				
				for (int i = 0; i < items.length; i++)
					theItemSet.addElement(items[i]);
				
				transactions.add(theItemSet);
			}
			
			System.out.println("Transactions: ");
			for (Itemset theSet : transactions) {
				System.out.println(theSet.toString());
			}
			
		} catch (Exception e) {
			System.out.println("Input file not found");
			System.exit(1);
		}
	}
	
	private List<Itemset> initilizeLargeItemSet(double min_support) { 
		
		List<Itemset> theSet = new ArrayList<Itemset>();
		
		//use a map, to keep track of item and count
		
		Map<String, Double> itemCount = new TreeMap<String, Double>();
		
		double totalTransactions = transactions.size();
		System.out.println(totalTransactions);
		for (Itemset is : transactions)
		{
			Set<String> items = is.getItems();
			for (String s : items) {
				if (itemCount.containsKey(s))
				{
					itemCount.put(s, (itemCount.get(s)+1.0));
				}
				else
					itemCount.put(s, 1.0);
			}
		}
		
		for (String s : itemCount.keySet())
		{
			Double support = (itemCount.get(s) / totalTransactions);
			
			System.out.println(s + ": " + support);
			
			if (support > min_support)
			{
				Itemset itemset = new Itemset();
				itemset.addElement(s);
				theSet.add(itemset);
			}
		}
		
		for (Itemset is : theSet) {
			System.out.println(is.toString());
		}
		
		return theSet;		
	}
	
	private double calculateSupport(Itemset is) {
		
		double support = 0.0;
		double totalTransactions = transactions.size();
		
		for (Itemset transaction : transactions)
			if (transaction.contains(is))
				support = support + 1.0;
		
		return support/totalTransactions;
	}

	private void generateRules(Itemset is, double min_conf) {
		
		//for each largeItemSet, generate rules
		//according to project description, rhs should only contain 1 string
		
		Set<String> items = is.getItems();
		
		for (String s : items) {
			Itemset rhs = new Itemset();
			rhs.addElement(s);
			
			//evaluate all the rhsCandidates -> needs enhancement
			Itemset lhs = new Itemset();
			
			for (String str : items)
				if (s != str)
					lhs.addElement(str);
			
			if ((calculateConfidence(lhs, rhs)) >= min_conf)
			{
				System.out.println("Adding new rule");
				System.out.println(lhs.toString());
				System.out.println(rhs.toString());
				rules.add(new Rule(lhs, rhs));
			}
		}

	}
	
	private double calculateConfidence(Itemset lhs, Itemset rhs) {
	
		double confidence = 0.0;
	
		//calculate support for lhs U rhs
		Itemset union = new Itemset();
		
		for (String s : lhs.getItems())
			union.addElement(s);
		
		for (String s : rhs.getItems())
			union.addElement(s);
		
		double unionSupport = calculateSupport(union);
	
		//calculate support for rhs
		double rhsSupport = calculateSupport(rhs);
		
		confidence = unionSupport/rhsSupport;
		
		System.out.println(confidence);
		return confidence;
	}
	
	public static void main(String[] args) {
		ItemsetGenerator generator = new ItemsetGenerator();
		generator.generateAssociations("integrated-dataset.csv", 0.4, 0.5);
	}
}
