package SQLClone;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class handles the Authentication of the user.
 */
public class AuthController {
	
	static Scanner sc=new Scanner(System.in);

	/**
	 * This method handles the encryption of the entered password string.
	 * @param pw entered password
	 * @return Encrypted password
	 */
	public static String encrpypt(String pw) {
		byte[] data = pw.getBytes();
		byte[] hash = null;
		try {
			hash = MessageDigest.getInstance("MD5").digest(data);
		}catch(NoSuchAlgorithmException e) {
			System.out.println("algo error");
		}
		
		String checksum = new BigInteger(1, hash).toString(16);
		return checksum;
	}
	/**
	 * This handles the authentication of the entered username and password
	 * @param enteredUserName Username.
	 * @param enteredPassword Password.
	 * @return User object if the authentication is successful.
	 */
	public static User auth(String enteredUserName, String enteredPassword) {
		String cap=createCaptcha();
		System.out.println("Captcha: "+cap);
		System.out.print("Enter the above Captcha: ");
		String enteredCaptcha=sc.nextLine();
		if(!cap.equals(enteredCaptcha)) {
			System.out.println("Entered wrong captcha. ");
			return null;
		}
		try {
			BufferedReader br=new BufferedReader(new FileReader("userdata1.txt"));
			String s=br.readLine();
			while(s!=null) {
				String tempuser=s.split(",")[0];
				if(tempuser.equals(enteredUserName)) {
					String temppw=s.split(",")[1];
					if(temppw.equals(enteredPassword)) {
//						System.out.println("Authenticated..");
						User temp=new User(enteredUserName,enteredPassword);
						return temp;
					}else {
						System.out.println("Wrong Password..");
					}
				}
				s=br.readLine();
			}
		}catch(FileNotFoundException e) {
			System.out.println("File not found..");
		}catch(IOException e) {
			
		}
		return null;
	}
	/**
	 * This method handles the craetion of Captcha code.
	 * @return Captcha code.
	 */
	public static String createCaptcha() {
		String chars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String cap="";
		Random rd=new Random();
		for(int i=0;i<6;i++) {
			cap=cap+chars.charAt(rd.nextInt(0, chars.length()));
			
			// cap=cap+chars.charAt(rd.nextInt(0,l));
		}
		return cap;
	}
	/**
	 * This method handles the login of user.
	 */
	public static void login() {
		System.out.print("Enter the username: ");
		String enteredUserName=sc.nextLine();
		
		System.out.print("Enter the Password: ");
		String enteredPassword=sc.nextLine();
		User u=auth(enteredUserName,encrpypt(enteredPassword));
		if(u!=null) {
			System.out.println("===================================\nWelcome back, "+u.getUserName()+".\n===================================");
			//login
			User.setCurrUser(u);
//			System.out.println(User.getCurrUser().getUserName());
			queryInputHandler();
			
		}else {
			System.out.println("Login unsuccessful..");
		}
	}
	/**
	 * This method handles the signup of user.
	 */
	public static void signup() {
		System.out.print("Enter the new username: ");
		String enteredUserName=sc.nextLine();
		
		System.out.print("Enter the Password: ");
		String enteredPassword=sc.nextLine();
		
		System.out.print("Confirm the Password: ");
		String confirmedPassword=sc.nextLine();
		if(!enteredPassword.equals(confirmedPassword)) {
			System.out.println("Wrong password.");
			return;
		}
		String cap=createCaptcha();
		System.out.println("Captcha: "+cap);
		System.out.print("Enter the above Captcha: ");
		String enteredCaptcha=sc.nextLine();
		if(!cap.equals(enteredCaptcha)) {
			System.out.println("Entered wrong captcha. ");
			return;
		}

		try {
			PrintWriter pw=new PrintWriter(new FileOutputStream("userdata1.txt",true));
			//check if the user already exists
//			System.out.println("File created..");
//			User user=new User(enteredUserName,encrpypt(enteredPassword));
			pw.println(enteredUserName+","+encrpypt(enteredPassword));
			pw.close();
		}catch(Exception e) {
			
		}
	}
	/**
	 * This method displays the users registered in the system.
	 */
	public static void showUsers() {
		
		System.out.println("=================\nUsers in the system.\n=================");
		
		try {
			BufferedReader br=new BufferedReader(new FileReader("userdata1.txt"));
			String s=br.readLine();
			while(s!=null) {
				System.out.println(s.split(",")[0]);
				s=br.readLine();
			}
			br.close();
		}catch(Exception e) {
			System.out.println("Error in displaying users.");
		}
		
	}
	/**
	 * This method handles taking the query input from the user.
	 */
	public static void queryInputHandler() {
		Scanner sc=new Scanner(System.in);
		while(true) {
			System.out.println("Run any of the following queries: \n1. CREATE DATABASE \n2. CREATE TABLE\n3. USE DATABASE\n4. INSERT\n5. SELECT\n6. BEGIN TRANSACTION\n7. COMMIT\n8. ROLLBACK\n9. EXIT ");
			System.out.println();
			System.out.print("Enter the query: ");
			String qry=sc.nextLine();
			QueryController qc=new QueryController(qry);
			String command=qry.split(" ")[0];
			if(command.equals("EXIT")) {
				break;
			}
			String identifier="";
			if(qry.split(" ").length>1) {
				identifier=qry.split(" ")[1];
			}
			
			switch(command) {
			case "SELECT":
				qc.selectQuery();
				break;
			case "INSERT":
				if(QueryController.getTransactionOn()) {
					qc.writeQueryInTransaction();
					break;
				}
				qc.insertQuery();
				break;
			case "CREATE":
				if(QueryController.getTransactionOn()) {
					qc.writeQueryInTransaction();
					break;
				}
				if(identifier.equals("DATABASE")) {
					qc.createDatabaseQuery();
				}else if(identifier.equals("TABLE")){
					qc.createTableQuery();
				}
				break;
			case "USE":
				if(QueryController.getTransactionOn()) {
					System.out.println("Cannot switch between databases in between the transaction.");
					break;
				}
				qc.useDatabaseQuery();
				break;
			case "SHOW":
				qc.showDatabaseQuery();
				break;
				
			case "BEGIN":
				if(QueryController.getTransactionOn()) {
					System.out.println("Transaction is already running.");
					break;
				}
				qc.beginTransactionQuery();
				break;
			case "COMMIT":
				qc.commitTransactionQuery();
				break;
			case "ROLLBACK":
				qc.rollbackTransactionQuery();
				break;
			case "END":
				qc.endTransactionQuery();
				break;
			case "EXIT":
				
				break;
			
			}
		
			
		
		}
		
	}
	/**
	 * Main method to initiate the program.
	 * @param args String array of arguments.
	 */
	public static void main(String[] args) {
		
			while(true) {
				System.out.println("Welcome to MySQL. To proceed further, enter the user login details or register the user. ");
				System.out.println("1. Login 2. Signup 3. Exit 4. Show all users.");
				int opt=sc.nextInt();
				sc.nextLine();
				switch(opt) {
				case 1:
					login();
					break;
				case 2:
					signup();
					break;
				
				case 3:
					break;
				case 4:
					showUsers();
					break;
				default:
					System.out.println("Invalid");
			}
			
		
		
		
		
		
		
		
		
		
		
		
		
		

		
		
		
	}

}
}
	




