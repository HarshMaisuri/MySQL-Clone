package SQLClone;

import java.util.ArrayList;
/**
 * This class stores the data about the database.
 */
public class Database {
	/**
	 * Name of the database.
	 */
	private String name;
	/**
	 * Owner of the database.
	 */
	private User owner;
	/**
	 * List of tables in the database.
	 */
	private ArrayList<String> tables;
	
	
	/**
	 * Contructor to initialize the database object.
	 * @param name Database name.
	 * @param owner User who created the database.
	 */
	Database(String name,User owner){
		this.name=name;
		this.owner=owner;
		this.tables=new ArrayList<String>();

	}
	/**
	 * Adds the table to the database.
	 * @param tableName Table name.
	 */
	public void addTable(String tableName) {
		this.tables.add(tableName);
	}
}
