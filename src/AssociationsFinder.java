import java.io.IOException;

import generator.ItemsetGenerator;

public class AssociationsFinder {

	public static void main(String[] args) {
		
		if (args.length != 3)
		{
			System.err.println("Usage: AssociationsFinder <fileName> <min_support> <min_confidence>");
			System.exit(1);
		}
		
		String fileName = args[0];
		Double min_support = 0.0;
		Double min_confidence = 0.0;
		
		try {
			min_support = Double.parseDouble(args[1]);
			min_confidence = Double.parseDouble(args[2]);
		} catch (Exception e) {
			System.err.println("Error parsing values of min_support or min_confidence.  Please check values.");
			System.exit(1);
		}
		
		if (min_support <= 0.0 || min_support > 1) {
			System.err.println("Min Support value should be in (0,1]");
			System.exit(1);
		}
		
		if (min_confidence <= 0.0 || min_confidence > 1) {
			System.err.println("Min confidence value should be in (0,1]");
			System.exit(1);
		}
			
		ItemsetGenerator generator;
		try {
			generator = new ItemsetGenerator();
			generator.generateAssociations(fileName, min_support, min_confidence);
			System.out.println("Example-run.txt contains detailed results");
		} catch (IOException e) {
			System.err.println("Can't create output file. Error Message: " + e.getMessage());
			System.exit(1);
		}
	}	
}
