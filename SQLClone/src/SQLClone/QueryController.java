package SQLClone;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * This class handles the functionalities of the queries entered by the user.
 */
public class QueryController {
	
	String query;
	/**
	 * Relative path of the transaction file to handle the transactions.
	 */
	String transactionFile="databases/"+User.getCurrUser().getUserName()+"/"+currDB+"/transaction.txt";
	/**
	 * Relative path of the current database folder.
	 */
	String dbFile="databases/"+User.getCurrUser().getUserName()+"/"+QueryController.getDB()+"/";
	/**
	 * Relative path to the current user folder which stores the user's databases.
	 */
	String userFiles="databases/"+User.getCurrUser().getUserName()+"/";
	private static String currDB=null;
	private static boolean transactionOn=false;
	/**
	 * Initialize the QueryController with the entered query.
	 * @param qry query entered by user.
	 */
	public QueryController(String qry){
		this.query=qry;
	}
	/**
	 * Setter method to set the current database.
	 * @param dbname database name
	 */
	public static void setDB(String dbname) {
		currDB=dbname;
	}
	/**
	 * Getter method to get the current running database.
	 * @return database name
	 */
	public static String getDB() {
		return currDB;
	}
	/**
	 * Setter method to set the transaction status.
	 * @param flag true/false denoting wheteher the transaction is running or not.
	 */
	public static void setTransactionOn(boolean flag) {
		transactionOn=flag;
	}
	/**
	 * Getter method to get the transaction status.
	 * @return true/false denoting whether the transaction is running or not.
	 */
	public static boolean getTransactionOn() {
		return transactionOn;
	}
	/**
	 * This method handles writing the queries entered in the running transaction.
	 */
	public void writeQueryInTransaction() {
		try {
			String path=transactionFile;
			PrintWriter pw=new PrintWriter(new FileOutputStream(path,true));
			pw.println(this.query);
			pw.close();
		}catch(Exception e) {
			
		}
	}
	/**
	 * 
	 * @param qry
	 */
	public void processQuery(String qry) {
		QueryController qc=new QueryController(qry);
		String command=qry.split(" ")[0];
		if(command.equals("EXIT")) {
			return;
		}
		String identifier=qry.split(" ")[1];
		switch(command) {
		case "SELECT":
			qc.selectQuery();
			break;
		case "INSERT":
			qc.insertQuery();
			break;
		case "CREATE":
			if(identifier.equals("DATABASE")) {
				qc.createDatabaseQuery();
			}else if(identifier.equals("TABLE")){
				qc.createTableQuery();
			}
			break;
		
		case "SHOW":
			qc.showDatabaseQuery();
			break;
			
		
	}
	}
	/**
	 * Lists out the databases names created by the current user.
	 */
	public void showDatabaseQuery() {
		String dbpath=dbFile;
		Set<String> set=Stream.of(new File(dbpath).listFiles()).filter(file -> !file.isDirectory()).map(File::getName).collect(Collectors.toSet());
		for(String i:set) {
			System.out.println(i);
		}
	}
	//SELECT * FROM TABLENAME;
	/**
	 * This method handles the functionality of SELECT query.
	 */
	public void selectQuery() {
		String tablename=this.query.split(" ")[3].substring(0,this.query.split(" ")[3].length()-1)+".csv";
		String tablepath=dbFile+tablename;
		try {
			BufferedReader br=new BufferedReader(new FileReader(tablepath));
			String s=br.readLine();
//			s=br.readLine();
			System.out.format("%s%n", "-".repeat(125));
			String[] temp=s.split(",");
			for(String i:temp) {
				System.out.printf("%-25s",i);
			}
			System.out.println();
			System.out.format("%s%n", "-".repeat(125));
			
			s=br.readLine();
			while(s!=null) {
				temp=s.split(",");
				for(String i:temp) {
					System.out.printf("%-25s",i);
				}
				System.out.println();
				s=br.readLine();
			}
			System.out.println();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Display the contents of the table.
	 * @param str path for the table.
	 */
	public void display(String str) {
		
		String tablepath=str;
		try {
			BufferedReader br=new BufferedReader(new FileReader(tablepath));
			String s=br.readLine();
//			s=br.readLine();
			System.out.format("%s%n", "-".repeat(125));
			String[] temp=s.split(",");
			for(String i:temp) {
				System.out.printf("%-25s",i);
			}
			System.out.println();
			System.out.format("%s%n", "-".repeat(125));
			
			s=br.readLine();
			while(s!=null) {
				temp=s.split(",");
				for(String i:temp) {
					System.out.printf("%-25s",i);
				}
				System.out.println();
				s=br.readLine();
			}
			System.out.println();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
//	INSERT INTO People VALUES (101, 'Peter', 'Engineer', 32); 
	//insert into user values (1,'harsh','harsh@gmail.com',1234567890,'dartmouth'),(2,'kunj','kunj@gmail.com',1234567891,'halifax'),(3,'manan','manan@gmail.com',1234567892,'toronto');
	//INSERT INTO STUDENTINFO VALUES (1,'HARSH','HARSH@GMAIL.COM',1234567,'DARTMOUTH'),(2,'KUNJ','KUNJ@GMAIL.COM',74563287,'HALIFAX');
	/**
	 * This method handles the functionality of INSERT query.
	 */
	public void insertQuery() {
		String tablename=this.query.split(" ")[2]+".csv";
		System.out.println(tablename);
		String tableFilePath=dbFile+tablename;
		System.out.println(tableFilePath);
		String[] data=this.query.split("[(]");
		data=Arrays.copyOfRange(data, 1, data.length);
		
		PrintWriter pw=null;
		try {
			pw=new PrintWriter(new FileOutputStream(tableFilePath,true));
			System.out.println("Writing file..");
			
			for(String i:data) {
				i=i.substring(0,i.length()-2);
				pw.println(i);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		pw.close();
		display(tableFilePath);
	}
	// USE databasename
	/**
	 * This method handles the functionality of USE DATABASE query.
	 */
	public void useDatabaseQuery() {
		
		String userDatabasePath=userFiles;
		String dbname=this.query.split(" ")[1];
		String dbpath=userDatabasePath+dbname+"/";
		Path path=Paths.get(dbpath);
		if(Files.exists(path)) {
			QueryController.setDB(dbname);
			System.out.println("User: "+User.getCurrUser().getUserName()+" Database: "+QueryController.getDB());
		}else {
			System.out.println("No such database exists.");
		}
		
	}
//	CREATE TABLE TABLE1 (COLUMN1 DTYPE,COLUMN1 DTYPE,COLUMN1 DTYPE,COLUMN1 DTYPE);
	/**
	 * This method handles the functionality of CREATE TABLE query.
	 */
	public void createTableQuery() {

		//CREATE TABLE STUDENTINFO (ID INT,NAME VARCHAR,EMAIL VARCHAR,PHONE LONG,ADDRESS VARCHAR);
		if(QueryController.getDB()==null) {
			System.out.println("Select the database first.");
			return;
		}
		PrintWriter pw=null;
		try {
			String tableName=this.query.split(" ")[2];
			String fileName=dbFile+tableName+".csv";
			pw=new PrintWriter(new FileOutputStream(fileName));
			
//			HashMap<String,String> dtype=new HashMap<>();
			String qryArgs=this.query.split("[(]")[1].substring(0,this.query.split("[(]")[1].length()-2);
			System.out.println(qryArgs);
//			qryArgs=qryArgs.substring(0, qryArgs.length()-2);
			String[] colData=qryArgs.split(",");
			
			for(int i=0;i<colData.length;i++) {
				
//				dtype.put(colData[i].split(" ")[0], colData[i].split(" ")[1]);
				
				pw.print(colData[i].replace(' ', '.')+",");
				
			}
			pw.println();
			
			
			

			
		}catch(Exception e) {
			e.printStackTrace();
		}
		pw.close();
		
	}
	
	
	
//	CREATE DATABASE EMPLOYEE
	/**
	 * This method handles the functionality of CREATE DATABASE query.
	 */
	public void createDatabaseQuery() {
		String dbname=this.query.split(" ")[2];
		
		System.out.println(dbname);
		Database db=new Database(dbname,User.getCurrUser());
		String dir=userFiles+dbname+"/";
		
		Path path=Paths.get(dir);
		System.out.println(path.toAbsolutePath().toString());
		
		try {
			Files.createDirectories(path);
			User.getCurrUser().getDb().add(dbname);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	/**
	 * This method handles the BEGIN TRANSACTION query.
	 */
	public void beginTransactionQuery(){
//		String filepath="/Users/devan/eclipse-workspace/CSCI5408/databases/"+User.getCurrUser().getUserName()+"/transaction.txt";
		setTransactionOn(true);
		System.out.println("=================TRANSACTION STARTED.=================");
		
	}
	/**
	 * This method handles the COMMIT query.
	 */
	public void commitTransactionQuery() {
		if(!getTransactionOn()) {
			System.out.println("No transaction is running currently. Kindly start the transaction first.");
			return;
		}else {
			String filepath=transactionFile;
			try {
				BufferedReader br=new BufferedReader(new FileReader(filepath));
				String s=br.readLine();
				while(s!=null) {
					processQuery(s);
					s=br.readLine();
				}
			}catch(Exception e) {
				
			}
			
		}
		setTransactionOn(false);
	}
	/**
	 * This method handles the ROLLBACK query.
	 */
	public void rollbackTransactionQuery() {
		String filepath=transactionFile;
		try {
			PrintWriter pw=new PrintWriter(new FileOutputStream(filepath));
			pw.println("");
			pw.close();
		}catch(Exception e) {
			
		}
		setTransactionOn(false);
		
	}
	/**
	 * This method handles the END TRANSACTION query.
	 */
	public void endTransactionQuery() {
		setTransactionOn(false);
	}
}
