package generator;

import java.sql.*;
import java.util.*;

public class DBAccess {
	
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public DBAccess() {
		
	}
	
	public List<Itemset> populateLargeItemSet(List<Itemset> seed, int k) {
		
		List<Itemset> retList = new ArrayList<Itemset>();
		try {
			String sql;
			
			Class.forName("org.sqlite.JDBC");
			
			
			connection = DriverManager.getConnection("jdbc:sqlite:temp.db");	
			statement = connection.createStatement();
			
			try {
				System.out.println("Dropping Table");
				statement.execute("Drop table LargeItemSet");
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			sql = generateCreateQuery(k);
			System.out.println("Creating Table");
			System.out.println(sql);
			statement.execute(sql);

			for (Itemset is : seed) 
			{
				System.out.println("inserting data for query: ");
				sql = generateInsertStatement(is);
				System.out.println(sql);
				statement.execute(sql);
			}
			
			sql = generateSelectQuery(k);
			System.out.println(sql);
			rs = statement.executeQuery(sql);
			ResultSetMetaData theMetaData = rs.getMetaData();
			
			int colCount = theMetaData.getColumnCount();
			
			while ( rs.next() )
			{
				Itemset itemSet = new Itemset();
				
				for (int i = 1; i <=colCount; i++)
				{
					String value = rs.getString(i);
					itemSet.addElement(value);
				}
				
				retList.add(itemSet);
			}
			
			statement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//values being returned
		for (Itemset is : retList) {
			System.out.println(is.toString());
		}
		return retList;
				
	}
	
	private String generateCreateQuery(int k) {
		StringBuffer sql = new StringBuffer("");
		sql.append("CREATE TABLE LargeItemSet ( ");
		
		for (int i = 1; i < k - 1; i++) {
			sql.append("column" + i);
			sql.append(" VARCHAR(255), ");
		}
		
		sql.append("column");
		sql.append(k-1);
		sql.append(" VARCHAR(255) )");
		
		return sql.toString();
	}
	
	private String generateSelectQuery(int k) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT * from LargeItemSet p, LargeItemSet q WHERE ");
		
		for (int i = 1; i < k - 1; i++)
		{
			sb.append("p.column");
			sb.append(i);
			sb.append("=");
			sb.append("q.column");
			sb.append(i);
			sb.append(" AND ");
		}
		
		sb.append("p.column");
		sb.append(k - 1);
		sb.append("<");
		sb.append("q.column");
		sb.append(k - 1);
		
		return sb.toString();
	}
	
	private String generateInsertStatement(Itemset theSet) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("Insert INTO LargeItemSet values (");
		
		Set<String> items = theSet.getItems();
		Iterator<String> itr = items.iterator();
		
		int i = 0;
		
		while (i < (items.size() - 1) )
		{
			String value = itr.next();
			sb.append("'");
			sb.append(value);
			sb.append("',");
			i++;
		}
		
		sb.append("'");
		String value = itr.next();
		sb.append(value);
		sb.append("'");
		sb.append(")");
		
		return sb.toString();
	}

	
	public static void main(String[] args) {
		DBAccess dba = new DBAccess();
		
		List<Itemset> seed = new ArrayList<Itemset>();
		
		Itemset is1 = new Itemset();
		is1.addElement("pen");
		is1.addElement("ink");
		
		seed.add(is1);

		Itemset is2 = new Itemset();
		is2.addElement("ink");
		is2.addElement("dairy");
		
		seed.add(is2);
		
		Itemset is3 = new Itemset();
		is3.addElement("dairy");
		is3.addElement("pen");

		seed.add(is3);
		
		dba.populateLargeItemSet(seed, 3);
	}
	

}
