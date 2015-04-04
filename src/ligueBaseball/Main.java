package ligueBaseball;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Classe invenaire.
 * @author 	Martin Rancourt
 * @author 	Vincent Ribou
 * @version 1.0
 * @since   2015-02-01
 */
public class Main {

	/**
	 * Nouveaux membres
	 */
	public static GestionLigue inventaireManager = new GestionLigue();

	// TODO : supprimmer les membres inutiles
	private String nomFichier;
	public static Map<Integer, String> operations;
	public static final Scanner scanner = new Scanner(System.in);

	/**
	 * Constructeur de l'inventaire.
	 * @param nomFichier Nom du fichier contenant l'inventaire.
	 */
	Main () {
		operations = new HashMap<Integer, String>();
	}

	/**
	 * Initialise l'inventaire.
	 * @throws IOException Si le fichier ne peut être ouvert.
	 */
	public void init () throws IOException {

		// Initialisation des operations
		operations.put(1, "Créer une nouvelle équipe"); 
		operations.put(2, "Afficher la liste des équipes"); 
		operations.put(3, "Supprimer l’équipe < EquipeNom >"); 
		operations.put(4, "Créer un nouveau joueur");
		operations.put(5, "Afficher la liste de joueurs");
		operations.put(6, "Supprimer le joueur"); 
		operations.put(7, "Ajouter un match"); 
		operations.put(8, "Créer un nouvel arbitre"); 
		operations.put(9, "Afficher la liste des arbitres"); 
		operations.put(10, "Affecter des arbitres à un match"); 
		operations.put(11, "Entrer le résultat d’un match"); 
		operations.put(12, "Afficher les résultats de tous les matchs."); 
		operations.put(13, "Afficher les résultats des matchs de l’équipe <EquipeNom>"); 
		operations.put(0, "Sortir");

	}

	/**
	 * Point d'entree du systeme.
	 * @param args	Arguments.
	 * @throws IOException Si le fichier args[0] ne peut être ouvert.
	 * @throws EnregistrementInexistentException Si l'enregistrement à modifier est inexistent
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, EnregistrementInexistentException, SQLException {

		Main inventaire = new Main();
		inventaire.init();
		inventaire.afficheMenu();

		System.out.print("Votre sélection : ");	

		while(scanner.hasNext()) {
			try {
				inventaire.effectuerOperation(scanner.nextInt());
			} catch (InputMismatchException e) {
				System.out.println("Option invalide.");
				inventaire.afficheMenu();
				scanner.next();
			} finally {
				System.out.print("\nVotre sélection : ");
			}
		}

	}

	/**
	 * Affiche les options de menus ainsi que leur descriptions.
	 */
	public void afficheMenu () {
		for(Entry<Integer, String> entry : operations.entrySet()){
			System.out.printf("%s - %s %n", entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Effectue une operation de gestion de l'inventaire.
	 * @param operation	Opération à effectuer.
	 * @throws IOException S'il y a une erreur d'ouverture du fichier.
	 * @throws EnregistrementInexistentException Si l'enregistrement à modifier est inexistent
	 * @throws SQLException 
	 */
	public void effectuerOperation(int operation) throws IOException, EnregistrementInexistentException, SQLException {

		boolean operationValide = false;

		for(Entry<Integer, String> entry : operations.entrySet()){
			if (entry.getKey() == operation) {
				System.out.printf("Option sélectionnée : %s. %s %n\n", operation, 
						operations.get(operation));

				switch (operation) {
				case 1 :
					creerEquipe();
					break;
				case 2 :
					afficherEquipes();
					break;
				case 3 :
					supprimerEquipe();
					break;
				case 4 :
					creerJoueur();
					break;
				case 5 :
					afficherJoueursEquipe();
					break;
				case 6 :
					supprimerJoueur();
					break;
				case 7 :
					creerMatch();
					break;
				case 8 :
					creerArbitre();
					break;
				case 9 :
					afficherArbitres();
					break;
				case 10 :
					arbitrerMatch();
					break;
				case 11 :
					entrerResultatMatch();
					break;
				case 12 :
					afficherResultatsDate();
					break;
				case 13 :
					afficherResultats();
					break;
				case 0 :
					sortir();
					break;
				default :
					break;
				}

				operationValide = true;
			}
		}

		if ( ! operationValide ) {
			System.out.printf("Operation invalide %n", operation);
			afficheMenu();
		}
	}

	public void creerEquipe() throws SQLException {
		String nomEquipe = null;

		System.out.print("Entrez le nom de l'equipe : ");
		nomEquipe = scanner.next();		
		inventaireManager.creerEquipe(nomEquipe, "");
	}

	public void afficherEquipes() throws SQLException {
		inventaireManager.afficherEquipes();
	}

	public void supprimerEquipe() throws SQLException {
		String nomEquipe = null;

		System.out.print("Equipe : ");
		nomEquipe = scanner.next();

		inventaireManager.supprimerEquipe(nomEquipe);
	}

	public void creerJoueur() throws SQLException {

		String nomJoueur;
		String prenomJoueur;
		String nomEquipe = null;

		int numeroJoueur = 0;
		Date dateDebut = null;

		char associationEquipe;
		char ajoutDate;

		System.out.print("Entrez le prenom du joueur : ");
		nomJoueur = scanner.next();
		System.out.print("Entrez le nom du joueur : ");
		prenomJoueur = scanner.next();

		System.out.print("Voulez-vous associer un joueur a une equipe ? (o/n) : ");
		associationEquipe = scanner.next().charAt(0);

		if (associationEquipe == 'o') {
			System.out.print("Entrez l'equipe du joueur : ");
			nomEquipe = scanner.next();
			System.out.print("Entrez le numero du joueur : ");
			numeroJoueur = scanner.nextInt();

			System.out.print("Voulez-vous ajouter une date d'entree dans l'equipe ? (o/n) : ");
			ajoutDate = scanner.next().charAt(0);
			if (ajoutDate == 'o'){

				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				java.util.Date date1 = null;
				String start;
				System.out.print("Entrez la date sous le format dd-MM-yyyy : ");
				while (date1 == null) {
					try{
						start = scanner.next();
						date1 = (java.util.Date) df.parse(start);

						Date sqlDate = new Date(date1.getTime());
						dateDebut = sqlDate;
					} catch(ParseException e) {
						System.out.println("Veuillez entrer une date valide!");
					}
				}
			} else {
				java.util.Date date = new java.util.Date();
				Date sqlDate = new Date(date.getTime());
				dateDebut = sqlDate;
			}
		}

		inventaireManager.creerJoueur(nomJoueur, prenomJoueur, nomEquipe, numeroJoueur, dateDebut);
	}


	public void afficherJoueursEquipe() throws SQLException {
		String EquipeNom = null;

		System.out.printf("Selectioner une équipe en particulier ? (o/n) : ");
		char choisirEquipe = scanner.next().charAt(0);

		if (choisirEquipe == 'o') {
			System.out.print ("Equipe : ");
			EquipeNom = scanner.next();
		}

		inventaireManager.afficherJoueursEquipe(EquipeNom);
	}

	public void supprimerJoueur() throws SQLException {
		String joueurNom = null;
		String joueurPrenom = null;

		System.out.print("Nom du joueur : ");
		joueurNom = scanner.next();
		System.out.print("Prénom du joueur : ");
		joueurPrenom = scanner.next();

		inventaireManager.supprimerJoueur(joueurNom, joueurPrenom);
	}

	public void creerMatch() throws SQLException {
		Date MatchDate = null;
		java.util.Date MatchHeure = null; 
		String EquipeNomLocal = null;
		String EquipeNomVisiteur = null;


		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date1 = null;
		String start;
		System.out.print("Entrez la date sous le format dd-mm-yyyy : ");
		while(date1 == null){
			try{
				start = scanner.next();
				date1 = (java.util.Date) df.parse(start);
				Date sqlDate = new Date(date1.getTime());
				MatchDate = sqlDate;
			}catch(ParseException e){
				System.out.println("Veuillez entrer une date valide!");
			}
		}

		System.out.print("Entrez l'heure sous le format HH:MM:SS : ");
		df = new SimpleDateFormat("hh:mm:ss");
		while (MatchHeure == null) {
			try{
				start = scanner.next();
				MatchHeure = (java.util.Date) df.parse(start);
				Timestamp sqlTimeStamp = new Timestamp(MatchHeure.getTime());
				MatchHeure = sqlTimeStamp;
			}
			catch(ParseException e) {
				System.out.println("Please enter a valid time!");
			}
		}      

		System.out.print("Équipe local : ");
		EquipeNomLocal = scanner.next();
		System.out.print("Équipe visiteur : ");
		EquipeNomVisiteur = scanner.next();

		inventaireManager.creerMatch(MatchDate, MatchHeure, EquipeNomLocal, EquipeNomVisiteur);
	}

	public void creerArbitre() throws SQLException {
		String ArbitrePrenom = null;
		String ArbitreNom = null;

		System.out.print("Prénom de l'arbitre : ");
		ArbitrePrenom = scanner.next();
		System.out.print("Nom de l'arbitre : ");
		ArbitreNom = scanner.next();

		inventaireManager.creerArbitre(ArbitrePrenom, ArbitreNom);
	}

	public void afficherArbitres() throws SQLException {
		inventaireManager.afficherArbitres();
	}

	public void arbitrerMatch() throws SQLException {
		Date MatchDate = null;
		Timestamp MatchHeure = null;
		String EquipeNomLocal = null;
		String EquipeNomVisiteur = null;
		String ArbitreNom = null;
		String ArbitrePrenom = null;

		System.out.print("Entrez le prenom de l'arbitre : ");
		ArbitrePrenom = scanner.next();
		System.out.print("Entrez le nom de l'arbitre : ");
		ArbitreNom = scanner.next();
		System.out.print("Entrez le nom de l'équipe locale : ");
		EquipeNomLocal = scanner.next();
		System.out.print("Entrez le nom de l'équipe visiteur : ");
		EquipeNomVisiteur = scanner.next();

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date1 = null;
		String start;
		System.out.print("Entrez la date sous le format dd-mm-yyyy : ");
		while (date1 == null) {
			try{
				start = scanner.next();
				date1 = (java.util.Date) df.parse(start);
				Date sqlDate = new Date(date1.getTime());
				MatchDate = sqlDate;
			}
			catch(ParseException e) {
				System.out.println("Please enter a valid date!");
			}
		}
		java.util.Date heure1 = null;
		System.out.print("Entrez l'heure sous le format HH:MM:SS : ");
		df = new SimpleDateFormat("hh:mm:ss");
		while (heure1 == null) {
			try{
				start = scanner.next();
				heure1 = (java.util.Date) df.parse(start);
				Timestamp sqlTimeStamp = new Timestamp(heure1.getTime());
				MatchHeure = sqlTimeStamp;
			}
			catch(ParseException e) {
				System.out.println("Please enter a valid time!");
			}
		}
		inventaireManager.arbitrerMatch(MatchDate, MatchHeure, EquipeNomLocal, 
				EquipeNomVisiteur, ArbitreNom, ArbitrePrenom);
	}


	public void entrerResultatMatch() throws SQLException {
		Date MatchDate = null;
		Timestamp MatchHeure = null;
		String EquipeNomLocal = null;
		String EquipeNomVisiteur = null; 
		int pointsLocal = 0;
		int PointsVisiteur = 0;

		System.out.println("Entrez la date sous le format dd-mm-yyyy:");
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date1 = null;
		String start;
		while(date1 == null){
			try{
				start = scanner.next();
				date1 = (java.util.Date) df.parse(start);
				Date sqlDate = new Date(date1.getTime());
				MatchDate = sqlDate;
			}catch(ParseException e){
				System.out.println("Veuillez une date valide!");
			}
		}

		System.out.println("Entrez l'heure sous le format HH:MM:SS :");

		java.util.Date heure1 = null;
		df = new SimpleDateFormat("hh:mm:ss");
		while (heure1 == null) {
			try{
				start = scanner.next();
				heure1 = (java.util.Date) df.parse(start);
				Timestamp sqlTimeStamp = new Timestamp(heure1.getTime());
				MatchHeure = sqlTimeStamp;
			}
			catch(ParseException e) {
				System.out.println("Veuillez entrer une heure valide");
			}
		}

		System.out.println("Entrez le nom de l'equipe local : ");
		EquipeNomLocal = scanner.next();
		System.out.println("Entrez le nom de l'equipe visiteur : ");
		EquipeNomVisiteur = scanner.next();
		System.out.println("Entrez les points des locals : ");
		pointsLocal = scanner.nextInt();
		System.out.println("Entrez les points des visiteurs : ");
		PointsVisiteur = scanner.nextInt();

		inventaireManager.entrerResultatMatch(MatchDate, MatchHeure, 
				EquipeNomLocal, EquipeNomVisiteur, pointsLocal, PointsVisiteur);
	}

	public void afficherResultatsDate() throws SQLException {
		char ajoutDate;
		Date dateDebut = null;

		System.out.print("Voulez-vous ajouter une date pour la recherche ? (o/n) : ");
		ajoutDate = scanner.next().charAt(0);
		if(ajoutDate == 'o'){

			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date date1 = null;
			String start;
			System.out.print("Entrez la date sous le format dd-mm-yyyy : ");
			while(date1 == null) {
				try{
					start = scanner.next();
					date1 = (java.util.Date) df.parse(start);
					Date sqlDate = new Date(date1.getTime());
					dateDebut = sqlDate;
				}catch(ParseException e){
					System.out.println("Veuillez entrer une date valide!");
				}
			}
		}
		inventaireManager.afficherResultatsDate(dateDebut);
	}

	public void afficherResultats() throws SQLException {
		String EquipeNom = null;

		System.out.print("Equipe : ");
		EquipeNom = scanner.next();

		inventaireManager.afficherResultats(EquipeNom);
	}

	/**
	 * Quitte le programme de gestion d'inventaire.
	 */
	public void sortir() {
		System.out.println("Sortir");
		System.out.printf("Le fichier %s a été créé avec succès.", nomFichier);
		System.out.println("Merci d'avoir utilisé le système de gestion "
				+ " d'inventaire de cartes.");
		System.exit(0);
	}

}