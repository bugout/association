package generator;

import java.util.*;
import java.io.*;

public class Parser {
	
	List<String> integrated_input;
	FileWriter writer;
	
	public Parser() throws IOException {
		integrated_input = new ArrayList<String>();
		writer = new FileWriter(new File("integrated-dataset.csv"));
	}
	
	//this function generates a comma seperated file that is fed to the main function
	public void parse() {
		
		
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("raw_input.csv"));
			
			String line;
			
			while ( (line = br.readLine()) != null)
				processLine(line);
			
			generateOutputFile();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//TO-DO - need to process the data more
	private void processLine(String line) {
		
		String[] elements = line.split(",");
		
		StringBuffer sb = new StringBuffer("");
		int size = 0;
		int totalCons = 0;
		int area = 0;
		
		String sizeStr;
		
		size = Integer.parseInt(elements[0]);
		totalCons = Integer.parseInt(elements[1]);
		area = Integer.parseInt(elements[2]);
		
		if (size < 10)
			sizeStr = "small";
		else if (size < 20)
			sizeStr = "medium";
		else 
			sizeStr = "large";
		
		sb.append(sizeStr);
		sb.append(",");
		
		
		sb.append(totalCons/100000);
		sb.append(",");
		
		sb.append(area/10000);
		
		sb.append('\n');
		
		//process each element and add to the integrated input
		integrated_input.add(sb.toString());
	}
	
	//generate the integrated input file
	private void generateOutputFile() {
		
		try {
			for (String s : integrated_input) 
				writer.write(s);
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
			System.out.println("Error writing file");
		}
	}
	
	public static void main(String args[]) {
		
		try {
			Parser myParser = new Parser();
			myParser.parse();
		} catch (Exception e) {
			
		}
		
	}

}
