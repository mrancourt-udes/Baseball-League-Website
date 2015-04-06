package ligueBaseball;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class GestionLigue {

	private Database db;
	private Connection connexion;

	public GestionLigue() throws LigueException {
		db = Database.getInstance();
	}

	/**
	 * Crée une nouvelle équipe. L’équipe est identifiée de manière unique par 
	 * son EquipeId.
	 * @param nomEquipe Nom de l'équipe à créer
	 * @throws SQLException Exception SQL
	 */
	public void creerEquipe(String nomEquipe, String nomTerrain, String adresse) throws SQLException, LigueException {
		int terrainId;

		connexion = db.getConnection();

		PreparedStatement preparedStatementCheck = null;
		String queryCheck = "SELECT "
				+ "EXISTS (SELECT FROM equipe WHERE equipeNom = ?) "
				+ "AS equipeExists";

		preparedStatementCheck = connexion.prepareStatement(queryCheck);

		preparedStatementCheck.setString(1, nomEquipe);

		ResultSet rs = preparedStatementCheck.executeQuery();
		rs.next();

		if (rs.getBoolean("equipeExists")) {
			throw new LigueException("L'équipe "+nomEquipe+" existe déjà");
		} else {

			terrainId = traitementTerrain(nomTerrain, adresse);

			connexion = db.getConnection();

			String queryId = "SELECT MAX(EquipeId)+1 AS nextEquipeId FROM equipe";
			PreparedStatement preparedStatementId= connexion.prepareStatement(queryId);
			ResultSet rsId = preparedStatementId.executeQuery();
			rsId.next();
			int equipeId = rsId.getInt("nextEquipeId");

			PreparedStatement preparedStatement = null;

			String query = "insert into equipe (equipeid, equipenom,terrainid)"
					+ "values (?, ?, ?); ";

			try {
				preparedStatement = connexion.prepareStatement(query);
				preparedStatement.setInt(1, equipeId);
				preparedStatement.setString(2, nomEquipe);
				preparedStatement.setInt(3, terrainId);
				preparedStatement.executeUpdate();
				connexion.commit();
			} catch (SQLException e) {
				System.out.println(e);
				System.out.println("USERWARNING - Une erreur est survenue durant la ceation de l'equipe.");
			} finally {
				// fermeture de la connexion 
				connexion.close();
			}
		}
	}

	/**
	 * Afficher la liste des équipes.
	 * @throws SQLException Exception SQL
	 */
	public void afficherEquipes() throws SQLException {

		connexion = db.getConnection();

		PreparedStatement preparedStatement = null;
		String query =
				"SELECT EquipeId, EquipeNom "
						+ "FROM equipe "
						+ "ORDER BY EquipeNom";

		String printFmt1 = "%3s | %-12s\n";

		try {
			preparedStatement = connexion.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();

			if (!rs.next()) {
				System.out.println("Aucune équipe pour l'instant.");
			} else {

				System.out.printf(printFmt1, "Id", "Équipe");
				System.out.printf("----+-----------------------\n");

				do {
					System.out.printf(printFmt1, rs.getString("EquipeId"),
							rs.getString("EquipeNom"));

				} while (rs.next());
			}

		} catch (SQLException e) {
			System.out.println("USERWARNING - Une erreur est survenue durant la sélection des équipes.");
		} finally {
			// fermeture de la connexion 
			connexion.close();
		}
	}

	public List<TupleEquipe> getEquipes() throws SQLException {

		connexion = db.getConnection();

		List<TupleEquipe> equipes = new LinkedList<>();

		PreparedStatement preparedStatement = null;
		String query =
				"SELECT EquipeId, EquipeNom "
						+ "FROM equipe "
						+ "ORDER BY EquipeNom";

		try {
			preparedStatement = connexion.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				do {
					TupleEquipe tupleEquipe;

					tupleEquipe = new TupleEquipe(
							rs.getInt("EquipeId"),
							rs.getString("EquipeNom")
					);

					equipes.add(tupleEquipe);
				} while (rs.next());
			}

		} catch (SQLException e) {
			System.out.println("USERWARNING - Une erreur est survenue durant la sélection des équipes.");
		} finally {
			// fermeture de la connexion
			connexion.close();
		}

		return equipes;
	}


	public List<TupleTerrain> getTerrains() throws SQLException {

		connexion = db.getConnection();

		List<TupleTerrain> terrains = new LinkedList<>();

		PreparedStatement preparedStatement = null;
		String query =
				"SELECT *"
						+ "FROM terrain "
						+ "ORDER BY terrainNom";

		try {
			preparedStatement = connexion.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				do {
					TupleTerrain tupleTerrain;

					tupleTerrain = new TupleTerrain(
							rs.getInt("terrainid"),
							rs.getString("terrainNom")
					);

					terrains.add(tupleTerrain);
				} while (rs.next());
			}

		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("USERWARNING - Une erreur est survenue durant la recuperation des terrains.");
		} finally {
			// fermeture de la connexion
			connexion.close();
		}

		return terrains;
	}


	/**
	 * Supprimer l’équipe < EquipeNom >. L’équipe ne doit pas avoir de joueurs,
	 * sinon la transaction est refusée.
	 * @throws SQLException Exception SQL
	 */
	public boolean supprimerEquipe(Integer equipeId) throws SQLException, LigueException {

		connexion = db.getConnection();
		boolean success = false;

		PreparedStatement preparedStatementCheck = null;

		String queryCheck = "SELECT "
				+ "NOT EXISTS (SELECT equipeId FROM equipe e "
				+ "WHERE equipeid = ?) AS \"EquipeNotExists\", "
				+ "EXISTS (SELECT joueurId FROM equipe e1 "
				+ "RIGHT JOIN faitpartie fp ON e1.EquipeId = fp.EquipeId "
				+ "WHERE e1.equipeid = ?) AS \"JoueursExists\"";

		preparedStatementCheck = connexion.prepareStatement(queryCheck);

		preparedStatementCheck.setInt(1, equipeId);
		preparedStatementCheck.setInt(2, equipeId);

		ResultSet rs = preparedStatementCheck.executeQuery();
		rs.next();

		if (rs.getBoolean("EquipeNotExists")) {
			throw new LigueException("Cette équipe ne semble pas faire partie de la ligue.");
		} else if (rs.getBoolean("JoueursExists")) {
			// Des joueurs font partie de l'equipe
			throw new LigueException("Impossible de supprimer l'équipe. <br> " +
					" Veuillez vous assurer qu'aucun joueur n'en fait partie.");
		} else {
			// Aucune contraintes, on procede a la suppression
			PreparedStatement preparedStatementSuppression = null;
			String querySupression = "DELETE FROM equipe WHERE equipeid = ?";

			try {

				preparedStatementSuppression = connexion.prepareStatement(querySupression);
				preparedStatementSuppression.setInt(1, equipeId);
				preparedStatementSuppression.executeUpdate();

				connexion.commit();

				success = true;

			} catch (SQLException e) {
				System.out.println("USERWARNING - Une erreur est survenue durant la supression de l'équipes.");
				connexion.rollback();
			} finally {
				connexion.close();
			}
		}

		return success;

	}



	/**
	 * <JoueurNom> <JoueurPrenom> [<EquipeNom> <Numero> [<DateDebut>]]
	 * Créer un nouveau joueur, le programme doit calculer le JoueurId, et 
	 * l’utilisateur doit fournir le <JoueurNom> et le <JoueurPrenom> De manière 
	 * optionnelle on peut donner les informations pour le joindre à une équipe. 
	 * Si l’<EquipeNom > est donné il faut fournir le numéro du joueur dans 
	 * cette équipe <Numero>. De manière optionnelle, on peut donner la 
	 * <DateDebut>.
	 * @param nomJoueur Nom du joueur à créer
	 * @param prenomJoueur Prénom du joueur à créer
	 * @param equipeNom Nom de l'équipe à créer
	 * @param numero Numéro du joueur à créer
	 * @param dateDebut Date de début
	 * @throws SQLException Exception SQL
	 */
	public void creerJoueur(String nomJoueur, String prenomJoueur, String equipeNom,
							int numero, Date dateDebut) throws SQLException {

		connexion = db.getConnection();

		String queryId = "SELECT MAX(joueurId)+1 AS nextJoueurId FROM joueur";
		PreparedStatement preparedStatementId= connexion.prepareStatement(queryId);
		ResultSet rsId = preparedStatementId.executeQuery();
		rsId.next();
		int joueurId = rsId.getInt("nextJoueurId");

		PreparedStatement preparedStatement = null;

		String query = "INSERT INTO joueur (joueurid, joueurnom, joueurprenom)"
				+ "VALUES (?, ?, ?); ";

		try {
			preparedStatement = connexion.prepareStatement(query);
			preparedStatement.setInt(1, joueurId);
			preparedStatement.setString(2, nomJoueur);
			preparedStatement.setString(3, prenomJoueur);
			preparedStatement.executeUpdate();
			connexion.commit();
		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("USERWARNING - Une erreur est survenue durant la ceation d'un joueur.");
		} finally {
			// fermeture de la connexion 
			connexion.close();
		}

		if(equipeNom != null){
			connexion = db.getConnection();

			String queryIdEquipe = "SELECT equipeid AS nextEquipeId FROM equipe WHERE equipenom = ?";
			PreparedStatement preparedStatementIdEquipe= connexion.prepareStatement(queryIdEquipe);
			preparedStatementIdEquipe.setString(1, equipeNom);
			ResultSet rsIdEquipe = preparedStatementIdEquipe.executeQuery();
			rsIdEquipe.next();
			int equipeId = rsIdEquipe.getInt("nextEquipeId");

			PreparedStatement preparedStatementFaitPartie = null;

			String query2 = "insert into faitpartie (joueurid, equipeid, numero, datedebut, datefin)"
					+ "values (?, ?, ?, ?, ?); ";

			try {
				preparedStatementFaitPartie = connexion.prepareStatement(query2);
				preparedStatementFaitPartie.setInt(1, joueurId);
				preparedStatementFaitPartie.setInt(2, equipeId);
				preparedStatementFaitPartie.setInt(3, numero);
				preparedStatementFaitPartie.setDate(4, dateDebut);
				preparedStatementFaitPartie.setDate(5, null);
				preparedStatementFaitPartie.executeUpdate();
				connexion.commit();
			} catch (SQLException e) {
				System.out.println(e);
				System.out.println("USERWARNING - Une erreur est survenue durant l'assignation du joueur a une equipe.");
			} finally {
				// fermeture de la connexion 
				connexion.close();
			}
		}
	}

	/**
	 * Afficher la liste de joueurs. Si le paramètre < EquipeNom > est fourni, 
	 * le programme affiche seulement les joueurs de l’équipe correspondante. 
	 * Si non, afficher tous les joueurs de toutes les équipes indiquant le nom 
	 * de l’équipe.
	 * @param EquipeNom Nom de l'équipe (Optionnel)
	 * @throws SQLException
	 */
	public void afficherJoueursEquipe(String ... EquipeNom) throws SQLException {

		connexion = db.getConnection();

		String nomEquipe = null;
		if (EquipeNom.length > 0) {
			nomEquipe = EquipeNom[0];
		}

		PreparedStatement preparedStatement = null;

		String query = "SELECT "
				+ "e.equipeid, e.equipeNom, j.joueurId, "
				+ "j.joueurPrenom || ' ' || j.joueurNom AS joueur, "
				+ "fp.numero, e.equipeNom, "
				+ "to_char(fp.dateDebut,'YYYY-MM-DD') AS \"DateDebut\", "
				+ "to_char(fp.dateFin,'YYYY-MM-DD') AS \"DateFin\" "
				+ "FROM equipe e, joueur j, faitpartie fp "
				+ "WHERE fp.joueurid = j.joueurid "
				+ "AND fp.equipeid = e.equipeid ";

		if (nomEquipe != null) {
			query += "AND equipeNom = ? ";
		}

		query += "ORDER BY fp.equipeid, fp.numero ";

		try {
			preparedStatement = connexion.prepareStatement(query);

			if (nomEquipe != null) {
				preparedStatement.setString(1, nomEquipe);
			}

			ResultSet rs = preparedStatement.executeQuery();

			String derniereEquipe = null;
			if (rs.next()) {
				do {
					String equipeCourante = rs.getString("EquipeNom");
					if (!equipeCourante.equals(derniereEquipe)) {
						System.out.printf("%s - %s\n",
								rs.getString("EquipeId"), equipeCourante);
					}

					System.out.printf("%6s - %s\n",
							rs.getString("JoueurId"), rs.getString("Joueur"));

					derniereEquipe = equipeCourante;
				} while (rs.next());
			} else {
				System.out.println("Aucun joueur pour cette équipe.");
			}

		} catch (SQLException e) {
			System.out.println("USERWARNING - Une erreur est survenue durant la sélection des équipes.");
		} finally {
			// fermeture de la connexion 
			connexion.close();
		}
	}

	public List<TupleJoueur> getJoueurs(String ... EquipeNom) throws SQLException {

		connexion = db.getConnection();

		List<TupleJoueur> joueurs = new LinkedList<>();


		String nomEquipe = null;
		if (EquipeNom.length > 0) {
			nomEquipe = EquipeNom[0];
		}

		PreparedStatement preparedStatement = null;

		String query = "SELECT "
				+ "e.equipeid, e.equipeNom, j.joueurId, "
				+ "j.joueurPrenom, j.joueurNom, "
				+ "fp.numero, e.equipeNom, "
				+ "to_char(fp.dateDebut,'YYYY-MM-DD') AS \"DateDebut\", "
				+ "to_char(fp.dateFin,'YYYY-MM-DD') AS \"DateFin\" "
				+ "FROM equipe e, joueur j, faitpartie fp "
				+ "WHERE fp.joueurid = j.joueurid "
				+ "AND fp.equipeid = e.equipeid ";

		if (nomEquipe != null) {
			query += "AND equipeNom = ? ";
		}

		query += "ORDER BY fp.equipeid, fp.numero ";

		try {
			preparedStatement = connexion.prepareStatement(query);

			if (nomEquipe != null) {
				preparedStatement.setString(1, nomEquipe);
			}

			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				do {

					TupleJoueur tupleJoueur;

					tupleJoueur = new TupleJoueur(
							rs.getInt("JoueurId"),
							rs.getInt("EquipeId"),
							rs.getInt("numero"),
							rs.getString("joueurNom"),
							rs.getString("joueurPrenom"),
							rs.getString("EquipeNom"),
							rs.getDate("DateDebut"),
							rs.getDate("DateFin")
					);

					joueurs.add(tupleJoueur);

				} while (rs.next());
			}

		} catch (SQLException e) {
			System.out.println("USERWARNING - Une erreur est survenue durant la sélection des équipes.");
		} finally {
			// fermeture de la connexion
			connexion.close();
		}

		return joueurs;

	}

	/**
	 * Supprime le joueur et toute l’information stockée sur lui
	 * @param joueurId Nom du joueur
	 * @param supprimerJoueurParticipe Supprimer les commentaires du joueur
	 * @param supprimerJoueurFaitPartie Supprimer les liens du joueur
	 * @throws SQLException Exception SQL
	 */
	public boolean supprimerJoueur(Integer joueurId,
								   Boolean supprimerJoueurParticipe,
								   Boolean supprimerJoueurFaitPartie)
			throws SQLException, LigueException {

		connexion = db.getConnection();
		boolean success = false;

		String queryCheck = "SELECT JoueurId FROM joueur "
				+ "WHERE JoueurId = ?";

		PreparedStatement preparedStatementCheck = connexion.prepareStatement(queryCheck);
		preparedStatementCheck.setInt(1, joueurId);

		ResultSet rs = preparedStatementCheck.executeQuery();

		// Le joueur existe
		if (rs.next()) {

			// Supression des équipes dans lesquel le joueur fait partie
			if ( ! supprimerJoueurParticipe(joueurId, supprimerJoueurParticipe)) {
				throw new LigueException("Des commentaires de matchs sont associés au joueur. " +
						"<br>Vous devez supprimer ces commentaires afin du supprimer le joueur.");
			}
			if ( ! supprimerJoueurFaitPartie(joueurId, supprimerJoueurFaitPartie)) {
				throw new LigueException("Le joueur fait partie d'une ou de plusieurs équipes. " +
						"<br>Vous devez supprimer ces liens afin du supprimer le joueur.");
			}

			PreparedStatement preparedStatement = null;
			String query = "DELETE FROM joueur WHERE joueurid = ? ";

			try {

				connexion = db.getConnection();

				preparedStatement = connexion.prepareStatement(query);
				preparedStatement.setInt(1, joueurId);
				preparedStatement.execute();

				connexion.commit();
				success = true;

			} catch (SQLException e) {
				System.out.println(e);
				System.out.println("USERWARNING - Une erreur est survenue durant la suppression du joueur.\n");
				connexion.rollback();
			} finally {
				// fermeture de la connexion
				connexion.close();
			}

		} else {
			System.out.println("USERWARNING - Suppression avorté, veuillez vous assurer de supprimer toutes les informations reliés au joueur.\n");
		}

		return success;

	}


	/**
	 *
	 * @param JoueurId
	 * @return
	 * @throws SQLException
	 */
	public boolean supprimerJoueurParticipe(int JoueurId, Boolean accepteSuppression) throws SQLException {
		connexion = db.getConnection();

		boolean succes = true;

		String queryCheck = "SELECT COUNT(JoueurId) AS nbParticipe FROM participe "
				+ "WHERE joueurId = ?";

		PreparedStatement preparedStatementCheck = connexion.prepareStatement(queryCheck);
		preparedStatementCheck.setInt(1, JoueurId);

		ResultSet rs = preparedStatementCheck.executeQuery();

		int nbParticipe = 0;
		if (rs.next()) {
			nbParticipe = rs.getInt("nbParticipe");
		}

		if (nbParticipe > 0) {
			if (accepteSuppression) {

				PreparedStatement preparedStatement = null;

				try {

					String query = "DELETE FROM participe WHERE joueurId = ?";

					preparedStatement = connexion.prepareStatement(query);
					preparedStatement.setInt(1, JoueurId);
					preparedStatement.execute();

					connexion.commit();

				} catch (SQLException e) {
					System.out.println("USERWARNING - Une erreur est survenue durant la suppression du joueur.");
					connexion.rollback();
				} finally {
					// fermeture de la connexion 
					connexion.close();
				}
			} else {
				succes = false;
			}
		}

		return succes;
	}

	/**
	 *
	 * @param JoueurId
	 * @return
	 * @throws SQLException
	 */
	public boolean supprimerJoueurFaitPartie(int JoueurId, Boolean accepteSuppression) throws SQLException {
		connexion = db.getConnection();

		boolean succes = true;

		String queryCheck = "SELECT COUNT(JoueurId) AS nbFaitPartie FROM faitpartie "
				+ "WHERE joueurId = ?";

		PreparedStatement preparedStatementCheck = connexion.prepareStatement(queryCheck);
		preparedStatementCheck.setInt(1, JoueurId);

		ResultSet rs = preparedStatementCheck.executeQuery();

		int nbFaitPartie = 0;
		if (rs.next()) {
			nbFaitPartie = rs.getInt("nbFaitPartie");
		}

		if (nbFaitPartie > 0) {

			if (accepteSuppression) {
				PreparedStatement preparedStatement = null;

				try {

					String query = "DELETE FROM faitpartie WHERE joueurId = ?";

					preparedStatement = connexion.prepareStatement(query);
					preparedStatement.setInt(1, JoueurId);
					preparedStatement.execute();

					connexion.commit();

				} catch (SQLException e) {
					System.out.println("USERWARNING - Une erreur est survenue durant la suppression du joueur.");
					connexion.rollback();
				} finally {
					// fermeture de la connexion 
					connexion.close();
				}
			} else {
				succes = false;
			}
		}

		return succes;
	}

	/**
	 * 7. creerMatch <MatchDate> <MatchHeure> <EquipeNomLocal> <EquipeNomVisiteur>
	 * Ajouter un match, en calculant le MatchId de manière automatique. Il faut 
	 * vérifier que les équipes existent et qu’ils sont différents, une équipe 
	 * ne peut pas jouer contre lui-même! Il faut vérifier aussi la date et 
	 * l’heure. Le terrain doit être assigné à celui de l’équipe locale.
	 * @throws SQLException Exception SQL
	 */
	public void creerMatch(Date MatchDate, java.util.Date MatchHeure,
						   String EquipeNomLocal, String EquipeNomVisiteur) throws SQLException, LigueException {

		connexion = db.getConnection();

		PreparedStatement preparedStatement = null;

		// Vé́rifier que les équipes existent et qu’ils sont différents
		if (EquipeNomLocal.equals(EquipeNomVisiteur)) {
			throw new LigueException("Les équipes doivent être différentes.");
		} else {
			PreparedStatement preparedStatementCheck = null;

			String queryCheck = "SELECT "
					+ "EXISTS (SELECT EquipeId FROM equipe WHERE equipeNom = ?) AS equipeLocalExists, "
					+ "EXISTS (SELECT EquipeId FROM equipe WHERE equipeNom = ?) AS equipeVisiteurExists";

			preparedStatementCheck = connexion.prepareStatement(queryCheck);
			preparedStatementCheck.setString(1, EquipeNomLocal);
			preparedStatementCheck.setString(2, EquipeNomVisiteur);
			preparedStatementCheck.executeQuery();

			ResultSet rs = preparedStatementCheck.getResultSet();
			rs.next();

			if (!rs.getBoolean("equipeLocalExists") || !rs.getBoolean("equipeVisiteurExists")) {
				System.out.println("USERWARNING - Veuillez vous assurer que les équipes existent.");
			}
		}

		PreparedStatement preparedStatementTerrain = null;
		preparedStatementTerrain = connexion.prepareStatement("SELECT terrainId FROM equipe WHERE equipenom = ?");
		preparedStatementTerrain.setString(1, EquipeNomLocal);
		preparedStatementTerrain.executeQuery();
		ResultSet rs = preparedStatementTerrain.getResultSet();

		int terrainId = 0;

		if (rs.next()) {
			terrainId = rs.getInt("terrainId");
		}

		String query = "INSERT INTO match (MatchId, TerrainId, MatchDate, MatchHeure, EquipeLocal, EquipeVisiteur) VALUES "
				+ "((SELECT MAX(matchId)+1 FROM match), "
				+ "(SELECT terrainId FROM equipe WHERE equipenom = ?), ?, ?, "
				+ "(SELECT equipeId FROM equipe WHERE equipenom = ?), "
				+ "(SELECT equipeId FROM equipe WHERE equipenom = ?))";
		try {
			preparedStatement = connexion.prepareStatement(query);

			preparedStatement.setString(1, EquipeNomLocal);
			preparedStatement.setDate(2, MatchDate);
			preparedStatement.setTimestamp(3, (Timestamp) MatchHeure);
			preparedStatement.setString(4, EquipeNomLocal);
			preparedStatement.setString(5, EquipeNomVisiteur);

			preparedStatement.executeUpdate();

			connexion.commit();

			System.out.println("Le match a été créé avec succès.");

		} catch (SQLException e) {
			System.out.println(preparedStatement);
			System.out.println(e);
			System.out.println("USERWARNING - Une erreur est survenue durant la création du match.");
			connexion.rollback();
		} finally {
			// fermeture de la connexion 
			connexion.close();
		}
	}

	/**
	 * 8. creerArbitre <ArbitreNom> <ArbitrePrenom>
	 * Créer un nouvel arbitre en calculant de manière automatique l’ArbitreId. 
	 * Assurez-vous de ne pas répéter les noms des arbitres, on suppose que dans 
	 * la ligue il n’y a pas d’homonymes.
	 * @throws SQLException Exception SQL
	 */
	public void creerArbitre(String ArbitrePrenom, String ArbitreNom) throws SQLException, LigueException {

		connexion = db.getConnection();

		PreparedStatement preparedStatementCheck = null;
		String queryCheck = "SELECT "
				+ "EXISTS (SELECT FROM arbitre WHERE arbitrePrenom = ? AND arbitreNom = ?) "
				+ "AS arbitreExists";

		preparedStatementCheck = connexion.prepareStatement(queryCheck);

		preparedStatementCheck.setString(1, ArbitrePrenom);
		preparedStatementCheck.setString(2, ArbitreNom);

		ResultSet rs = preparedStatementCheck.executeQuery();
		rs.next();

		if (rs.getBoolean("arbitreExists")) {
			throw new LigueException("L'arbitre «"+ ArbitrePrenom + " " + ArbitreNom + "» existe déjà.");
		} else {

			String queryId = "SELECT MAX(arbitreid)+1 AS nextArbitreId FROM arbitre";
			PreparedStatement preparedStatementId = connexion.prepareStatement(queryId);
			ResultSet rsId = preparedStatementId.executeQuery();
			rsId.next();
			int arbitreId = rsId.getInt("nextArbitreId");

			PreparedStatement preparedStatement = null;

			String query = "INSERT into arbitre (arbitreid, arbitreprenom, arbitrenom) "
					+ "values (?, ?, ?); ";

			try {
				preparedStatement = connexion.prepareStatement(query);
				preparedStatement.setInt(1, arbitreId);
				preparedStatement.setString(2, ArbitrePrenom);
				preparedStatement.setString(3, ArbitreNom);
				preparedStatement.executeUpdate();

				connexion.commit();

			} catch (SQLException e) {
				System.out.println("USERWARNING - Une erreur est survenue durant la création arbitres.");
				connexion.rollback();
			} finally {
				// fermeture de la connexion 
				connexion.close();
			}
		}
	}

	public boolean supprimerArbitre(Integer arbitreId) throws SQLException, LigueException {
		boolean success = false;

		connexion = db.getConnection();

		PreparedStatement preparedStatementCheck = null;
		String queryCheck = "SELECT "
				+ "EXISTS (SELECT FROM arbitre WHERE arbitreid = ?) "
				+ "AS arbitreExists, "
				+ "EXISTS (SELECT FROM arbitre "
				+ " INNER JOIN arbitrer ON arbitre.arbitreid = arbitrer.arbitreid "
				+ " WHERE arbitre.arbitreid = ?) "
				+ "AS arbitreArbitrer ";

		preparedStatementCheck = connexion.prepareStatement(queryCheck);

		preparedStatementCheck.setInt(1, arbitreId);
		preparedStatementCheck.setInt(2, arbitreId);

		ResultSet rs = preparedStatementCheck.executeQuery();
		rs.next();

		if (!rs.getBoolean("arbitreExists")) {
			throw new LigueException("L'arbitre n'existe pas.");
		} else if (rs.getBoolean("arbitreArbitrer")) {
			// L'arbitre arbitre des matchs
			throw new LigueException("Impossible de supprimer l'arbitre. <br> " +
					" Veuillez vous assurer qu'il n'est affecté à aucun match.");
		} else {

			PreparedStatement preparedStatement = null;

			String query = "DELETE FROM arbitre WHERE arbitreId = ?";


			try {
				preparedStatement = connexion.prepareStatement(query);
				preparedStatement.setInt(1, arbitreId);

				System.out.println(preparedStatement);


				preparedStatement.executeUpdate();

				connexion.commit();

				success = true;

			} catch (SQLException e) {



				System.out.println(e.toString());
				System.out.println("USERWARNING - Une erreur est survenue durant la suppresion de arbitre.");
				connexion.rollback();
			} finally {
				// fermeture de la connexion
				connexion.close();
			}
		}

		return success;
	}

	/**
	 * 9. afficherArbitres
	 * Afficher la liste des arbitres en ordre alphabétique d’ArbitreNom
	 * @throws SQLException Exception SQL
	 */
	public void afficherArbitres() throws SQLException {

		connexion = db.getConnection();

		String query = "SELECT (ArbitrePrenom || ' ' || ArbitreNom) AS \"arbitre\" FROM arbitre ";

		String printFmt = "%s\n";

		try {
			PreparedStatement preparedStatement = connexion.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();

			if (!rs.next()) {
				System.out.println("Aucun arbitre l'instant.");
			} else {

				System.out.printf(printFmt, "Arbitre");
				System.out.printf("----------------------------\n");

				do {
					System.out.printf(printFmt, rs.getString("arbitre"));
				} while (rs.next());
			}

		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("USERWARNING - Une erreur est survenue durant la sélection des arbitres.");
		} finally {
			// fermeture de la connexion 
			connexion.close();
		}

	}

	public List<TupleArbitre> getArbitres() throws SQLException{
		connexion = db.getConnection();

		List<TupleArbitre> arbitres = new LinkedList<>();

		String query = "SELECT ArbitreId, ArbitrePrenom, ArbitreNom FROM arbitre ";

		try {
			PreparedStatement preparedStatement = connexion.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				do {

					TupleArbitre tupleArbitre;

					tupleArbitre = new TupleArbitre(
							rs.getInt("ArbitreId"),
							rs.getString("ArbitrePrenom"),
							rs.getString("ArbitreNom")
					);

					arbitres.add(tupleArbitre);

				} while (rs.next());
			}

		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("USERWARNING - Une erreur est survenue durant la sélection des arbitres.");
		} finally {
			// fermeture de la connexion
			connexion.close();
		}

		return arbitres;
	}

	/**
	 * 10.arbitrerMatch <MatchDate> <MatchHeure> <EquipeNomLocal>
	 * <EquipeNomVisiteur> <ArbitreNom> <ArbitrePrenom>
	 * Affecter des arbitres à un match. Valider que le match existe, ainsi que
	 * les <ArbitreNom> <ArbitrePrenom>. Un match peut avoir un maximum de
	 * quatre arbitres, il faut les compter.
	 * @throws SQLException Exception SQL
	 */
	public void arbitrerMatch(Date  MatchDate, Timestamp MatchHeure,
							  String EquipeNomLocal, String EquipeNomVisiteur,
							  String[] arbitres) throws SQLException, LigueException {

		connexion = db.getConnection();

		String queryCheck = "SELECT "
				+ "EXISTS (SELECT * FROM match "
				+ "JOIN equipe AS equipeL ON equipeL.equipeid = equipeLocal "
				+ "JOIN equipe AS equipeV ON equipeV.equipeid = equipevisiteur "
				+ "WHERE matchDate = ? "
				+ "AND matchHeure = ? "
				+ "AND equipeL.equipeNom = ? "
				+ "AND equipeV.equipenom = ?) "
				+ "AS matchExists";

		PreparedStatement preparedStatementCheck = connexion.prepareStatement(queryCheck);
		preparedStatementCheck.setDate(1, MatchDate);
		preparedStatementCheck.setTimestamp(2, MatchHeure);
		preparedStatementCheck.setString(3, EquipeNomLocal);
		preparedStatementCheck.setString(4, EquipeNomVisiteur);

		ResultSet rs = preparedStatementCheck.executeQuery();
		rs.next();


		if(rs.getBoolean("matchExists")) {

			for (int i = 0; i < arbitres.length; i++) {

				String queryCheck1 = "SELECT EXISTS (SELECT * FROM arbitre WHERE arbitreid = ? ) AS arbitreExists";

				PreparedStatement preparedStatementCheck1 = connexion.prepareStatement(queryCheck1) ;
				preparedStatementCheck1.setInt(1, Integer.parseInt(arbitres[i]));

				rs = preparedStatementCheck1.executeQuery();
				rs.next();

				if(rs.getBoolean("arbitreExists")){
					String queryNbArbitres = "SELECT count(matchid) AS nbArbitres FROM arbitrer GROUP BY matchid";
					PreparedStatement preparedStatementNbArbitres = connexion.prepareStatement(queryNbArbitres);
					rs = preparedStatementNbArbitres.executeQuery();
					rs.next();
					int nbArbitres = rs.getInt("nbArbitres");

					if(nbArbitres < 4){

						String queryArbitre = "SELECT arbitreid FROM arbitre WHERE arbitreid = ? ";
						String queryMatch = "SELECT matchid FROM match JOIN equipe ON equipeLocal = equipeid "
								+ "WHERE matchDate = ? AND matchHeure = ? AND equipeNom = ?";

						PreparedStatement preparedStatementArbitre = connexion.prepareStatement(queryArbitre);
						preparedStatementArbitre.setInt(1, Integer.parseInt(arbitres[i]));

						PreparedStatement preparedStatementMatch = connexion.prepareStatement(queryMatch);
						preparedStatementMatch.setDate(1, MatchDate);
						preparedStatementMatch.setTimestamp(2, MatchHeure);
						preparedStatementMatch.setString(3, EquipeNomLocal);

						rs = preparedStatementArbitre.executeQuery();
						rs.next();

						int arbitreid = rs.getInt("arbitreid");

						rs = preparedStatementMatch.executeQuery();
						rs.next();

						int matchid = rs.getInt("matchid");

						String query = "INSERT INTO arbitrer "
								+ "VALUES (?, ?)";

						try {
							PreparedStatement preparedStatement = connexion.prepareStatement(query);

							preparedStatement.setInt(1, arbitreid);
							preparedStatement.setInt(2, matchid);

							preparedStatement.executeUpdate();
							connexion.commit();

						} catch (SQLException e) {
							System.out.println("USERWARNING - Une erreur est survenue dans l'association d'un arbitre avec un match");
							connexion.rollback();
						}
					}
				}
				else {
					throw new LigueException("Cet arbitre n'existe pas");
				}
			}
		}
		else {
			throw new LigueException("Aucun match ne correspond à votre requête.");
		}


		// fermeture de la connexion
		connexion.close();


	}


	/**
	 * 11.entrerResultatMatch <MatchDate> <MatchHeure> <EquipeNomLocal> 
	 * <EquipeNomVisiteur> <PointsLocal> <PointsVisiteur>
	 * Entrer le résultat d’un match. Valider que la valeur utilisée pour les 
	 * points soit toujours plus grande ou égale à zéro.
	 * @throws SQLException Exception SQL
	 */
	public void entrerResultatMatch(Date MatchDate, Timestamp MatchHeure,
									String EquipeNomLocal, String EquipeNomVisiteur, int pointsLocal,
									int PointsVisiteur) throws SQLException, LigueException {

		if (pointsLocal < 0 || PointsVisiteur < 0) {
			System.out.println("USERWARNING -  Les scores doivent être > 0");
		} else {
			connexion = db.getConnection();

			PreparedStatement preparedStatementCheck = null;
			String queryCheck = "SELECT "
					+ "EXISTS (SELECT matchId FROM match WHERE EquipeLocal = (SELECT EquipeId FROM equipe WHERE EquipeNom = ?) "
					+ "AND EquipeVisiteur = (SELECT EquipeId FROM equipe WHERE EquipeNom = ?)  AND matchdate = ? AND MatchHeure = ?) "
					+ "AS matchIdExists";

			preparedStatementCheck = connexion.prepareStatement(queryCheck);

			preparedStatementCheck.setString(1, EquipeNomLocal);
			preparedStatementCheck.setString(2, EquipeNomVisiteur);
			preparedStatementCheck.setDate(3, MatchDate);
			preparedStatementCheck.setTimestamp(4, MatchHeure);

			ResultSet rs = preparedStatementCheck.executeQuery();
			rs.next();

			if (rs.getBoolean("matchIdExists")) {
				PreparedStatement preparedStatement = null;

				try {
					String query = "UPDATE match "
							+ "SET pointslocal = ?, pointsvisiteur = ? "
							+ "WHERE matchid = (SELECT matchId FROM match WHERE EquipeLocal = (SELECT EquipeId FROM equipe WHERE EquipeNom = ?) "
							+ "AND EquipeVisiteur = (SELECT EquipeId FROM equipe WHERE EquipeNom = ?)  AND matchdate = ? AND MatchHeure = ?) ";

					preparedStatement = connexion.prepareStatement(query);
					preparedStatement.setInt(1, pointsLocal);
					preparedStatement.setInt(2, PointsVisiteur);
					preparedStatement.setString(3, EquipeNomLocal);
					preparedStatement.setString(4, EquipeNomVisiteur);
					preparedStatement.setDate(5, MatchDate);
					preparedStatement.setTimestamp(6, MatchHeure);


					System.out.println(preparedStatement);

					preparedStatement.executeUpdate();

					connexion.commit();


				} catch (SQLException e) {
					System.out.println("USERWARNING - Une erreur est survenue durant l'ajout des points. \n ");
					connexion.rollback();
				} finally {
					// fermeture de la connexion
					connexion.close();
				}
			} else {
				throw new LigueException("Aucun match ne correspond à votre requête.");
			}
		}
	}


	/**
	 * 12. afficherResultatsDate [<APartirDate>]
	 * Afficher les résultats de tous les matchs. Si le paramètre <APartirDate>
	 * est donné, il faut afficher seulement les résultats à partir de cette
	 * date. Afficher aussi les arbitres, s’ils existent pour le match. Les
	 * résultats doivent être triés par date.
	 * @throws SQLException Exception SQL
	 */
	public void afficherResultatsDate(Date aPartirDate) throws SQLException {

		connexion = db.getConnection();

		PreparedStatement preparedStatement = null;

		String query = "SELECT m.matchid, b.arbitrePrenom || ' ' || b.arbitreNom AS arbitre, "
				+ "pointslocal, pointsvisiteur, e1.equipeNom AS equipelocal, e2.equipeNom AS equipeVisiteur "
				+ "FROM match m "
				+ "RIGHT JOIN arbitrer a ON a.matchid = m.matchid "
				+ "LEFT JOIN arbitre b ON a.arbitreid = b.arbitreid "
				+ "INNER JOIN equipe e1 ON e1.equipeid = m.equipelocal "
				+ "INNER JOIN equipe e2 ON e2.equipeid = m.equipeVisiteur ";
		if(aPartirDate != null){
			query += "WHERE matchdate > ? ";
		}
		query += "GROUP BY m.matchid, b.arbitrePrenom, b.arbitreNom, pointslocal, pointsvisiteur, "
				+ "e1.equipeNom, e2.equipeNom "
				+ "ORDER BY m.matchid ";

		try {

			preparedStatement = connexion.prepareStatement(query);
			if(aPartirDate != null){
				preparedStatement.setDate(1, aPartirDate);
			}
			ResultSet rs = preparedStatement.executeQuery();

			System.out.println("Liste des matchs");
			System.out.println("_______________________________________________________________________________________");

			String printFmt = "%6s | %-12s | %-12s | %-15s | %-17s | %s";

			int dernierMatch = 0;

			if (!rs.next()) {
				System.out.println("Aucun match à l'horaire.");
			} else {

				System.out.printf(printFmt, "Match", "Local", "Visiteurs", "Points (local)", "Points (visiteur)", "Arbitres\n");
				System.out.print("\n-------+--------------+--------------+-----------------+-------------------+-----------");

				do {

					if (dernierMatch != rs.getInt("matchid")) {
						System.out.printf("\n" + printFmt,
								rs.getInt("matchid"),
								rs.getString("equipelocal"), rs.getString("equipevisiteur"),
								(rs.getString("pointslocal") != null ? rs.getString("pointslocal") : "À venir"),
								(rs.getString("pointsvisiteur") != null ? rs.getString("pointsvisiteur") : "À venir"),
								"");
					}

					System.out.print(rs.getString("arbitre") + ", ");
					dernierMatch = rs.getInt("matchid");

				} while (rs.next());
			}

		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("USERWARNING - Une erreur est survenue durant la sélection des arbitres.");
		} finally {
			// fermeture de la connexion 
			connexion.close();
		}
	}

	// TODO : Trouver une façon d'aller chercher les arbitres
	public List<TupleMatch> getResultatsDate(Date aPartirDate) throws SQLException{
		connexion = db.getConnection();

		List<TupleMatch> matchs = new LinkedList<TupleMatch>();

		PreparedStatement preparedStatement = null;

		String query = "SELECT m.matchid, b.arbitrePrenom || ' ' || b.arbitreNom AS arbitre, "
				+ "pointslocal, pointsvisiteur, e1.equipeNom AS equipelocal, e2.equipeNom AS equipeVisiteur, "
				+ "matchDate, to_char(matchHeure, 'HH24:MI:SS') AS matchheure, terrainnom "
				+ "FROM match m "
				+ "RIGHT JOIN arbitrer a ON a.matchid = m.matchid "
				+ "LEFT JOIN arbitre b ON a.arbitreid = b.arbitreid "
				+ "INNER JOIN equipe e1 ON e1.equipeid = m.equipelocal "
				+ "INNER JOIN equipe e2 ON e2.equipeid = m.equipeVisiteur "
				+ "INNER JOIN terrain t ON t.terrainid = m.terrainid ";
		if(aPartirDate != null){
			query += "WHERE matchdate > ? ";
		}
		query += "GROUP BY m.matchid, b.arbitrePrenom, b.arbitreNom, pointslocal, pointsvisiteur, "
				+ "e1.equipeNom, e2.equipeNom, terrainnom "
				+ "ORDER BY m.matchid ";

		try {

			preparedStatement = connexion.prepareStatement(query);
			if(aPartirDate != null){
				preparedStatement.setDate(1, aPartirDate);
			}
			ResultSet rs = preparedStatement.executeQuery();


			if (rs.next()) {

				do {

					TupleMatch tupleMatch = new TupleMatch(
							rs.getInt("matchid"),
							rs.getString("equipelocal"),
							rs.getString("equipevisiteur"),
							(rs.getString("pointslocal") != null ? rs.getString("pointslocal") : "À venir"),
							(rs.getString("pointsvisiteur") != null ? rs.getString("pointsvisiteur") : "À venir"),
							rs.getString("arbitre"),
							rs.getDate("matchDate"),
							rs.getString("matchHeure"),
							rs.getString("terrainnom")
					);

					matchs.add(tupleMatch);

				} while (rs.next());
			}
		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("USERWARNING - Une erreur est survenue durant la sélection des matchs.");
		} finally {
			// fermeture de la connexion
			connexion.close();
		}

		return matchs;
	}

	/**
	 * 13.afficherResultats [<EquipeNom>]
	 * Afficher les résultats des matchs où l’équipe <EquipeNom> a participé,
	 * peu importe si c’était comme local ou comme visiteur. Afficher aussi les
	 * arbitres, s’ils existent pour le match. Les résultats doivent être triés
	 * par date.
	 * @throws SQLException Exception SQL
	 */
	public void afficherResultats(String EquipeNom) throws SQLException {
		connexion = db.getConnection();

		PreparedStatement preparedStatement = null;

		String query = "SELECT * FROM "
				+ "(SELECT * FROM match m "
				+ "		INNER JOIN equipe e1 ON m.equipelocal = e1.EquipeId "
				+ "UNION "
				+ "SELECT * FROM match m "
				+ "		INNER JOIN equipe e2 ON m.equipevisiteur = e2.EquipeId) AS resultats "
				+ "WHERE equipeNom LIKE ? "
				+ "ORDER BY matchdate, matchheure";

		try {

			preparedStatement = connexion.prepareStatement(query);
			preparedStatement.setString(1, EquipeNom);

			ResultSet rs = preparedStatement.executeQuery();

			System.out.println("____________________________");

			if (!rs.next()) {
				System.out.println("Aucun match pour cette équipe.");
			} else {

				System.out.printf("%s%12s%9s \n", "Date", "Heure", "Score");
				System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");

				do {
					System.out.printf("%s %s %s%s\n",
							rs.getString("matchdate"), rs.getString("matchheure"),
							(rs.getString("pointsLocal") != null ? rs.getString("pointsLocal") : "À venir"),
							(rs.getString("pointsVisiteur") != null ? " - " + rs.getString("pointsVisiteur") : ""));
				} while (rs.next());
			}

		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("USERWARNING - Une erreur est survenue durant la sélection des arbitres.");
		} finally {
			// fermeture de la connexion 
			connexion.close();
		}
	}

	public void insererEquipe(List<TupleJoueur> joueurs) throws SQLException, LigueException{
		connexion = db.getConnection();

		PreparedStatement preparedStatementCheck = null;
		String queryCheck = "SELECT "
				+ "EXISTS (SELECT FROM equipe WHERE equipeNom = ?) "
				+ "AS equipeExists";

		preparedStatementCheck = connexion.prepareStatement(queryCheck);

		preparedStatementCheck.setString(1, joueurs.get(0).nomEquipe);

		ResultSet rs = preparedStatementCheck.executeQuery();
		rs.next();

		System.out.println(preparedStatementCheck);

		int equipeId;

		if (rs.getBoolean("equipeExists")) {
			throw new LigueException("Cette équipe est déjà enregistrée.");
		} else {
			String queryIdEquipe = "SELECT MAX(equipeId)+1 AS nextEquipeId FROM equipe";
			PreparedStatement preparedStatementIdEquipe = connexion.prepareStatement(queryIdEquipe);
			ResultSet rsIdEquipe = preparedStatementIdEquipe.executeQuery();
			rsIdEquipe.next();
			equipeId = rsIdEquipe.getInt("nextEquipeId");
		}

		String queryIdJoueur = "SELECT MAX(joueurId)+1 AS nextJoueurId FROM joueur";
		PreparedStatement preparedStatementIdJoueur = connexion.prepareStatement(queryIdJoueur);
		ResultSet rsIdJoueur = preparedStatementIdJoueur.executeQuery();
		rsIdJoueur.next();
		int joueurId = rsIdJoueur.getInt("nextJoueurId");

		String queryEquipe = "INSERT INTO equipe (equipeid, equipenom) "
				+ "VALUES (?, ?); ";

		String queryJoueur = "INSERT INTO joueur (joueurid, joueurNom, joueurPrenom) "
				+ "VALUES (?, ?, ?); ";

		String queryJoueurFP = "INSERT INTO faitpartie (joueurid, equipeid, numero, datedebut) "
				+ "VALUES (?, ?, ?, ?); ";

		PreparedStatement preparedStatementEquipe = connexion.prepareStatement(queryEquipe) ;
		PreparedStatement preparedStatementJoueur = connexion.prepareStatement(queryJoueur) ;
		PreparedStatement preparedStatementJoueurFP = connexion.prepareStatement(queryJoueurFP) ;

		try {

			// Ajout de l'équipe
			preparedStatementEquipe.setInt(1, equipeId);
			preparedStatementEquipe.setString(2, joueurs.get(0).nomEquipe);
			preparedStatementEquipe.executeUpdate();

			// insertion en batch
			for (TupleJoueur joueur : joueurs) {
				preparedStatementJoueur.setInt(1, joueurId);
				preparedStatementJoueur.setString(2, joueur.nom);
				preparedStatementJoueur.setString(3, joueur.prenom);
				preparedStatementJoueur.addBatch();

				preparedStatementJoueurFP.setInt(1, joueurId);
				preparedStatementJoueurFP.setInt(2, equipeId);
				preparedStatementJoueurFP.setInt(3, joueur.numero);
				preparedStatementJoueurFP.setDate(4, joueur.dateDebut);
				preparedStatementJoueurFP.addBatch();

				joueurId ++;

			}

			preparedStatementJoueur.executeBatch();
			preparedStatementJoueurFP.executeBatch();

			connexion.commit();

		} catch (SQLException e) {
			System.out.println("USERWARNING - Une erreur est survenue durant l'insertion des joueurs.");
			connexion.rollback();
		} finally {
			// fermeture de la connexion
			connexion.close();
		}
	}

	/**
	 * traitementTerrain
	 *Verifie si le nom du terrain est valide, si non, il en creer un nouveau
	 * @throws SQLException Exception SQL
	 */
	public int traitementTerrain(String nomTerrain, String adresse) throws SQLException {
		int terrainId = 0;

		connexion = db.getConnection();

		PreparedStatement preparedStatementCheck = null;
		String queryCheck = "SELECT terrainid, "
				+ "EXISTS (SELECT FROM terrain WHERE terrainNom = ?) "
				+ "AS terrainExists "
				+ " FROM terrain ";

		preparedStatementCheck = connexion.prepareStatement(queryCheck);

		preparedStatementCheck.setString(1, nomTerrain);

		ResultSet rs = preparedStatementCheck.executeQuery();
		rs.next();

		if (rs.getBoolean("terrainExists")) {
			terrainId = rs.getInt("terrainid");
		} else {

			String queryId = "SELECT MAX(terrainId)+1 AS nextterrainId FROM terrain";
			PreparedStatement preparedStatementId = connexion.prepareStatement(queryId);

			ResultSet rsId = preparedStatementId.executeQuery();
			rsId.next();
			terrainId = rsId.getInt("nextterrainId");

			PreparedStatement preparedStatement = null;

			String query = "insert into terrain (terrainid, terrainnom, terrainadresse)"
					+ "values (?, ?, ?); ";

			try {
				preparedStatement = connexion.prepareStatement(query);
				preparedStatement.setInt(1, terrainId);
				preparedStatement.setString(2, nomTerrain);
				preparedStatement.setString(3, adresse);
				preparedStatement.executeUpdate();
				connexion.commit();
			} catch (SQLException e) {
				System.out.println(e);
				System.out.println("USERWARNING - Une erreur est survenue durant la creation du terrain");
			} finally {
				// fermeture de la connexion 
				connexion.close();
			}
		}

		return terrainId;
	}

}
