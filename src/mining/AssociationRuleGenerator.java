package mining;

import java.io.IOException;
import java.util.List;

import data.DataLoader;
import data.Database;

public class AssociationRuleGenerator {
	private final static String configfile = "config.xml";
	
	public static void main(String[] args) throws IOException {
		String datafile = args[0];
		int minsupp = Integer.parseInt(args[1]);
		
		DataLoader loader = new DataLoader(datafile, configfile);
		Database db = loader.load();
		
		LargeItemSetFinder finder = new AprioriFinder(db, minsupp);
		List<List<LargeItemSet>> largeSets = finder.findLargeItemSets();
		
		
		
	}
}
