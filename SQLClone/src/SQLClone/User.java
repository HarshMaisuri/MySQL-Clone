package SQLClone;

import java.util.ArrayList;

import java.io.Serializable;
/**
 * This class handles the user data.
 */
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * User name.
	 */
	private String userName;
	/**
	 * Password.
	 */
	private String password;
	/**
	 * Lists of databases created by the user.
	 */
	private ArrayList<String> db;
	/**
	 * Stores the current user object.
	 */
	private static User currUser=null;
//	private String currDB=null;
	/**
	 * Contructor to initialize the user object.
	 * @param userName Username
	 * @param password Password
	 */
	public User(String userName,String password) {
		this.userName=userName;
		this.password=password;
		this.db=new ArrayList<>();
	}
	/**
	 * Setter method to set the current user.
	 * @param u Current user object.
	 */
	public static void setCurrUser(User u) {
		currUser=u;
	}
	/**
	 * Getter method to get the current active user.
	 * @return Current user object.
	 */
	public static User getCurrUser() {
		return currUser;
	}
	/**
	 * Getter method to get the current active user's username.
	 * @return Username
	 */
	public String getUserName() {
		return this.userName;
	}
	/**
	 * Setter method to set the username.
	 * @param name Username
	 */
	public void setUserName(String name) {
		this.userName=name;
	}
	/**
	 * Getter method to get the password.
	 * @return Password.
	 */
	public String getPassword() {
		return this.password;
	}
	/**
	 * Setter method to set the password.
	 * @param pw password.
	 */
	public void setPassword(String pw) {
		this.password=pw;
	}
	/**
	 * Getter method to get the database list.
	 * @return Databases created by the user.
	 */
	public ArrayList<String> getDb() {
		return this.db;
	}
	/**
	 * Setter method to set the database list.
	 * @param db List of databases.
	 */
	public void setDb(ArrayList<String> db) {
		this.db=db;
	}
//	public void setCurrDB(String dbname) {
//		this.currDB=dbname;
//	}
//	public String getCurrDB() {
//		return this.currDB;
//	}
	/**
	 * Handles the logging out functionality.
	 */
	public void logout() {
		currUser=null;
		AuthController.main(null); // ?
	}
}
