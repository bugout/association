package generator;

import java.io.*;
import java.util.*;

public class ItemsetGenerator {
	
	//contains all the transactions
	private List<Itemset> transactions;
	
	//contains all large itemsets
	private List<Itemset> largeItemSets;
	
	//seed for kth iteration
	private List<Itemset> seed;
	
	private List<Rule> rules;
	
	FileWriter writer;
	
	public ItemsetGenerator() throws IOException {
		transactions = new ArrayList<Itemset>();
		largeItemSets = new ArrayList<Itemset>();
		rules = new ArrayList<Rule>();
		writer = new FileWriter(new File("Example-run.txt"));
	}
	
	public void generateAssociations(String fileName, double min_support, double min_conf) {
		
		//this populates all the transactions
		readInputFile(fileName);
		
		//return all items that meet required min_support.  
		//We don't need this list to generated association rules as it only contains one element 
		//but we add it because we need to print it as required by the project.
		List<Itemset> initialList = initilizeLargeItemSet(min_support);	
		largeItemSets.addAll(initialList);
		seed = initialList;
		int k = 2;
		
		while (!seed.isEmpty())
		{
			List<Itemset> candidateList = aprioriGen(seed, k);
			
			//calculate the support for each element in candidateList
			//and remove any element that doesn't contain the required support
			List<Itemset> removeList = new ArrayList<Itemset>();
			for (Itemset is : candidateList) {
				double itemset_support = calculateSupport(is);
				if (itemset_support < min_support)
					removeList.add(is);
				else 
					is.setSupport(itemset_support);
			}
			
			candidateList.removeAll(removeList);
		
			//set the seed to the candidateList for the next iteration
			seed = candidateList;
			
			//add all the cadidates to the largeItemSets since they meeting the minimum support
			largeItemSets.addAll(candidateList);
			
			k++;
		}
		
		//evaluate all the association rules
		for (Itemset is : largeItemSets) {
			if ((is.getItems().size()) > 1)
				generateRules(is, min_conf);
		}
		
		Collections.sort(largeItemSets);
		Collections.sort(rules);
		
		System.out.println("All Large Itemsets:");
		for (Itemset is : largeItemSets) 
			System.out.println(is.toString() + " Support: " + is.getSupport());
		
		System.out.println("All Association Rules:");
		for (Rule rule : rules) {
			System.out.println(rule.toString() + " (Support: " + rule.getSupport() + 
					" , Confidence: " + rule.getConfidence() + ")");
		}
		
		generateOutput();
	}
		
	private List<Itemset> aprioriGen(List<Itemset> seed, int k) {
		
		boolean usesql = false;
		List<Itemset> candidateList;
		
		if (usesql) {
			DBAccess db = new DBAccess();
			//this returns a new candidate list
			candidateList = db.populateLargeItemSet(seed, k);
		}
		else {
			// do the join in the memory, it returns a new candidate list 
			candidateList = populateLargeItemSet(seed, k);
		}
		
		/* 
		 * Optimize by evaluating subsets of each candidate of size k-1. If any candidate's subsets 
		 * are not contained in the seed, delete that candidate
		 */
		List<Itemset> removeList = new ArrayList<Itemset>();
		for (Itemset is : candidateList)
		{
			boolean remove = false;
			List<Itemset> subsets = populateSubsets(is, k);
			
			for (Itemset subsetIs : subsets) {
				boolean isFound = false;
				for (Itemset seedIs : seed) {
					if (seedIs.containsSameItems(subsetIs)) {
						isFound = true;
						break;
					}		
				}
				if (!isFound) {
					remove = true;
					break;
				}
			}
			
			if (remove)
				removeList.add(is);
		}
		candidateList.removeAll(removeList);
		
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
					
					//get p and q sets and iterators, p and q are treesets, thus they are already ordered
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
		
		for (Itemset is : transactions)
		{
			Set<String> items = is.getItems();
			for (String s : items) {
				if (itemCount.containsKey(s))
					itemCount.put(s, (itemCount.get(s)+1.0));
				else
					itemCount.put(s, 1.0);
			}
		}
		
		for (String s : itemCount.keySet())
		{
			Double support = (itemCount.get(s) / totalTransactions);
			if (support > min_support)
			{
				Itemset itemset = new Itemset();
				itemset.addElement(s);
				itemset.setSupport(support);
				theSet.add(itemset);
			}
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
		//according to project description, rhs should only contain 1 item
		Set<String> items = is.getItems();
		
		for (String s : items) {
			Itemset rhs = new Itemset();
			rhs.addElement(s);
			
			//evaluate all the rhsCandidates -> needs enhancement
			Itemset lhs = new Itemset();
			
			for (String str : items)
				if (s != str)
					lhs.addElement(str);	
	
			double confidence = calculateConfidence(lhs, rhs);
			if (confidence >= min_conf) {
				Rule rule = new Rule(lhs, rhs);
				rule.setConfidecne(confidence);
				rule.setSupport(is.getSupport());
				rules.add(rule);
			}
		}
	}
	
	//support(lhs U rhs)/support(rhs)
	private double calculateConfidence(Itemset lhs, Itemset rhs) {
	
		double confidence = 0.0;
		Itemset union = new Itemset();
		
		for (String s : lhs.getItems())
			union.addElement(s);
		
		for (String s : rhs.getItems())
			union.addElement(s);
		
		confidence = (calculateSupport(union))/(calculateSupport(rhs));
		
		return confidence;
	}
	
	private void generateOutput() {
		
		try {
			writer.write("All Large Itemsets:\n");
			for (Itemset is : largeItemSets) 
				writer.write(is.toString() + ", Support: " + is.getSupport() + "\n");
			
			writer.write("\nAll Association Rules:\n");
			for (Rule rule : rules) {
				writer.write(rule.toString() + " (Support: " + rule.getSupport() + 
						" , Confidence: " + rule.getConfidence() + ")\n");
			}
			
			
			//print the term range
			
			//create a set of all the terms in the rules
			Set<String> items = new TreeSet<String>();
			
			for (Rule rule : rules) 
			{
				items.addAll(rule.getLhs().getItems());
				items.addAll(rule.getRhs().getItems());
			}
			
			//read the schema file
			
			BufferedReader br = new BufferedReader(new FileReader("schema.txt"));
			
			//hard-coding the value here, it can be read from schema-format.txt to make it generic
			
			Map<String, String[]> rangeMap = new HashMap<String, String[]>();
			
			String line;
			while ( (line = br.readLine()) != null ) {
				String schemaEntry[] = line.split(",");
				String columnName = schemaEntry[0];
				
				int partitionNumber = 0;
				
				try {
					partitionNumber = Integer.parseInt(schemaEntry[3]);
				} catch (NumberFormatException e) {
					System.err.println("Error in schema file, one or more range info in Example-run.txt file might be missing");
				}
				
				if (0 < partitionNumber) {
					String partitions[] = new String[partitionNumber+1];
					
					int k = 0;
					
					BufferedReader reader = new BufferedReader(new FileReader(columnName + ".range"));
					String value;
					while (k <= partitionNumber && (value = reader.readLine()) != null ) {
						partitions[k] = value;
						++k;
					}
					rangeMap.put(columnName, partitions);
				}
			}
			
			writer.write("\nFollowing are the range values for items listed above, where applies: \n");
			
			for (String s : items) {
				String key = s.substring(0, s.indexOf("-"));
				if (rangeMap.containsKey(key)) {
					try {
						int partitionNumber = Integer.parseInt(s.substring(s.indexOf("-")+1));
						String myPartitions[] = rangeMap.get(key);
						StringBuffer sb = new StringBuffer("");
						sb.append(s);
						sb.append(": (");
						sb.append(myPartitions[partitionNumber]);
						sb.append(",");
						sb.append(myPartitions[partitionNumber+1]);
						sb.append(")");
						writer.write(sb.toString() + "\n");
					}
					catch (NumberFormatException e) {
						System.err.println("Error in schema file, one or more range info in Example-run.txt file might be missing");
					}
				}
			}
			
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) throws IOException {
		ItemsetGenerator generator = new ItemsetGenerator();
		generator.generateAssociations("output-full.txt", 0.1, 0.1);
	}
}
