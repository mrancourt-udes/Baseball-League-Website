package ligueBaseball;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	
	private static Database dbInstance;
	private static Connection connexion;
	private final static String url = "jdbc:postgresql://127.0.0.1:5432/";
	private final static String dbName = "tp3/";
	private final static String user = "vonziper";
	private final static String pwd = "tp3";
	
	public static Database getInstance() {
		if (dbInstance == null) {
			dbInstance = new Database();
		}
		return dbInstance;
	}
	
	Database() {
		try{
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException cnfe){
			System.out.println("SYSTEMERROR – Impossible de trouver le Driver JDBC!");
			System.exit(1);
		}
	}	
	
	public Connection getConnection() {
		connexion = null;
		try {
			connexion = DriverManager.getConnection (url+dbName, user, pwd);
			connexion.setAutoCommit(false); 
		} catch (SQLException e) {
			System.out.println("SYSTEMERROR – Problème de connexion à la base de données");
			System.exit(1);
		}
		
		return connexion;		
	}
}
