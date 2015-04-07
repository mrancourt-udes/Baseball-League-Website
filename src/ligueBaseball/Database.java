package ligueBaseball;

import java.sql.*;

/**
 * Gestionnaire d'une connexion avec une BD relationnelle via JDBC.
 *
 * <pre>
 * Marc Frappier - 83 427 378
 * Université de Sherbrooke
 * version 2.0 - 13 novembre 2004
 * ift287 - exploitation de bases de données
 *
 * Ce programme ouvrir une connexion avec une BD via JDBC.
 * La méthode serveursSupportes() indique les serveurs supportés.
 *
 * Pré-condition
 *   le driver JDBC approprié doit être accessible.
 *
 * Post-condition
 *   la connexion est ouverte en mode autocommit false et sérialisable, 
 *   (s'il est supporté par le serveur).
 * </pre>
 */
public class Database {

	private String serveur;
	private String adresseIP;
	private String bd;
	private String user;
	private String pass;
	private Driver d;

	private Connection conn;

	private boolean serializable;

	/**
	 * Ouverture d'une connexion en mode autocommit false et sérialisable (si
	 * supporté)
	 *
	 * @param serveur
	 *            serveur SQL de la BD
	 * @bd nom de la base de données
	 * @user userid sur le serveur SQL
	 * @pass mot de passe sur le serveur SQL
	 */
	public Database(String serveur, String adresseIP, String bd, String user, String pass)
			throws SQLException {

		this.serveur = serveur;
		this.adresseIP = adresseIP;
		this.bd = bd;
		this.user = user;
		this.pass = pass;

	}

	public void setSerializable() throws SQLException {
		if (serializable)
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	}

	public void setReadCommitted() throws SQLException {
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	}

	public void displayCurrentIsolationLevel() throws SQLException {
		int level = conn.getTransactionIsolation();
		if (level == Connection.TRANSACTION_SERIALIZABLE)
			System.out.println("Transaction en mode sérialisable.");
		else if (level == Connection.TRANSACTION_READ_COMMITTED)
			System.out.println("Transaction en mode read commited.");
		else
			System.out.println("Transaction en mode inconnu.");
	}

	/**
	 * fermeture d'une connexion
	 */
	public void fermer() throws SQLException {
		conn.rollback();
		conn.close();
		System.out.println("Connexion fermée" + " " + conn);
	}

	/**
	 * commit
	 */
	public void commit() throws SQLException {
		conn.commit();
	}

	/**
	 * rollback
	 */
	public void rollback() throws SQLException {
		conn.rollback();
	}

	/**
	 * retourne la Connection jdbc
	 */
	public Connection getConnection() throws SQLException {

		try {
			if (serveur.equals("local")) {
				d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver")
						.newInstance();
				DriverManager.registerDriver(d);
				conn = DriverManager.getConnection(
						"jdbc:oracle:thin:@127.0.0.1:1521:" + bd, user, pass);
			}
			if (serveur.equals("sti")) {
				d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver")
						.newInstance();
				DriverManager.registerDriver(d);
				conn = DriverManager.getConnection(
						"jdbc:oracle:thin:@io.usherbrooke.ca:1521:" + bd, user,
						pass);
			} else if (serveur.equals("postgres")) {
				d = (Driver) Class.forName("org.postgresql.Driver")
						.newInstance();
				DriverManager.registerDriver(d);
				conn = DriverManager.getConnection("jdbc:postgresql://" + adresseIP + "/" + bd,
						user, pass);
			}

			// mettre en mode de commit manuel
			conn.setAutoCommit(false);

			// mettre en mode sérialisable si possible
			// (plus haut niveau d'integrité l'accès concurrent aux données)
			DatabaseMetaData dbmd = conn.getMetaData();
			if (dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)) {
				serializable = true;
			}
			else {
				serializable = false;
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new SQLException("JDBC Driver non instancié");
		}

		return conn;
	}

	/**
	 * Retourne la liste des serveurs supportés par ce gestionnaire de
	 * connexions
	 */
	public static String serveursSupportes() {
		return "local : Oracle installé localement 127.0.0.1\n"
				+ "sti   : Oracle installé au Service des technologies de l'information\n"
				+ "postgres : Postgres installé localement\n"
				+ "access : Microsoft Access, installé localement et inscrit dans ODBC";
	}
}// Classe Connexion
