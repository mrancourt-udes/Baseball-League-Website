package ligueBaseballServlet;

import ligueBaseball.GestionLigue;
import ligueBaseball.LigueException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by Martin Rancourt on 2015-04-04.
 */
// TODO : Ajouter toutes les actions possibles
public class FormHandler extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        checkSession(request, response);

        GestionLigue ligue = (GestionLigue) request.getSession().getAttribute("ligue");

        System.out.println(ligue);

        /*if (etat == null) {
            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
        else */if (request.getParameter("ajouterJoueur") != null) {
            traiterAjoutJoueur(request, response);
        }
        else if (request.getParameter("ajouterArbitre") != null) {
            traiterAjoutArbitre(request, response);
        }
        else if (request.getParameter("ajouterEquipe") != null) {
            traiterAjoutEquipe(request, response);
        }
        else if (request.getParameter("ajouterMatch") != null) {
            traiterAjoutMatch(request, response);
        }
        else if (request.getParameter("ajouterResultatMatch") != null) {
            traiterAjoutResultatMatch(request, response);
        }
        else if (request.getParameter("affecterArbitres") != null) {
            traiterAffecterArbitres(request, response);
        }
        else if (request.getParameter("filtrerMatchDate") != null) {
            traiterFiltrerMatchDate(request, response);
        }

        else {
            List listeMessageErreur = new LinkedList();
            listeMessageErreur.add("Choix non reconnu");
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/accueil.jsp");
            dispatcher.forward(request, response);
        }
    }

    // TODO : Toute la logique
    public void traiterAjoutJoueur(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {
        try {

            String prenom;
            String nom;
            String equipe;
            int numero;
            java.sql.Date dateDebut;

            if (request.getParameter("prenom") == null) {
                throw new LigueException("Veuillez entrer le prénom du joueur");
            } else {
                prenom = request.getParameter("prenom");
            }
            if (request.getParameter("nom") == null) {
                throw new LigueException("Veuillez entrer le nom du joueur");
            } else {
                nom = request.getParameter("nom");
            }
            if (request.getParameter("equipe") == null) {
                throw new LigueException("Veuillez sélectionner l'Équipe du joueur");
            } else {
                equipe = request.getParameter("equipe");
            }
            if (request.getParameter("numero") == null) {
                throw new LigueException("Veuillez entrer le numero du joueur");
            } else {
                try {
                    numero = Integer.parseInt(request.getParameter("numero"));
                } catch (NumberFormatException e) {
                    throw new LigueException("Veuillez entrer un numéro de joueur valide");
                }
            }
            if (request.getParameter("dateDebut") == null) {
                throw new LigueException("Veuillez entrer la date de début du joueur");
            } else {
                try {
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    Date tmpDate = df.parse(request.getParameter("dateDebut"));
                    dateDebut = new java.sql.Date(tmpDate.getTime());
                } catch (ParseException e) {
                    throw new LigueException("Veuillez entrer une date de début valide");
                }
            }
            GestionLigue gestionLigue = (GestionLigue) request
                    .getSession().getAttribute("ligue");


            // exécuter la transaction
            synchronized (gestionLigue) {
                gestionLigue.creerJoueur(nom, prenom, equipe, numero, dateDebut);
            }

            List listeMessageSucces = new LinkedList();
            listeMessageSucces.add("L'arbitre «" + prenom + " " + nom + "» a été ajouté avec succès!");

            HttpSession session = request.getSession(false);
            // sauvegarde du message dans la session
            session.setAttribute("listeMessageSucces", listeMessageSucces);
            response.sendRedirect("Routes?page=joueurs");

        } catch (LigueException e) {

            List listeMessageErreur = new LinkedList();
            listeMessageErreur.add(e.toString());

            request.setAttribute("listeMessageErreur", listeMessageErreur);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/ajouterJoueur.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }
    }

    public void traiterAjoutArbitre(HttpServletRequest request,
                                    HttpServletResponse response) throws ServletException, IOException {
        try {

            String prenom;
            String nom;

            if (request.getParameter("inputPrenom") == null) {
                throw new LigueException("Veuillez entrer un prénom d'arbitre");
            } else {
                prenom = request.getParameter("inputPrenom");
            }
            if (request.getParameter("inputNom") == null) {
                throw new LigueException("Veuillez choisir un nom d'arbitre");
            } else {
                nom = request.getParameter("inputNom");
            }

            GestionLigue gestionLigue = (GestionLigue) request
                    .getSession().getAttribute("ligue");

            // exécuter la transaction
            synchronized (gestionLigue) {
                gestionLigue.creerArbitre(prenom, nom);
            }

            List listeMessageSucces = new LinkedList();
            listeMessageSucces.add("L'arbitre «" + prenom + " " + nom + "» a été ajouté avec succès!");

            request.setAttribute("listeMessageSucces", listeMessageSucces);

            HttpSession session = request.getSession(false);
            // sauvegarde du message dans la session
            session.setAttribute("listeMessageSucces", listeMessageSucces);
            response.sendRedirect("Routes?page=arbitres");

        } catch (LigueException e) {

            List listeMessageErreur = new LinkedList();
            listeMessageErreur.add(e.toString());

            request.setAttribute("listeMessageErreur", listeMessageErreur);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/ajouterArbitre.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }
    }

    // TODO : Fixer la gestion du terrain
    public void traiterAjoutEquipe(HttpServletRequest request,
                                   HttpServletResponse response) throws ServletException, IOException {

        try {

            String equipe;
            String terrain;
            String adresse = "";

            if (request.getParameter("equipe") == null) {
                throw new LigueException("Veuillez entrer un nom d'équipe");
            } else {
                equipe = request.getParameter("equipe");
            }
            if (request.getParameter("terrain") == null) {
                throw new LigueException("Veuillez entrer un nom de terrain");
            } else {
                terrain = request.getParameter("terrain");
            }
            if (request.getParameter("adresse") != null) {
                adresse = request.getParameter("adresse");
            }

            GestionLigue gestionLigue = (GestionLigue) request
                    .getSession().getAttribute("ligue");


            // exécuter la transaction
            synchronized (gestionLigue) {
                gestionLigue.creerEquipe(equipe, terrain, adresse);
            }

            List listeMessageSucces = new LinkedList();
            listeMessageSucces.add("L'équipe «" + equipe +"» associée au terrain «" + terrain + "» a été ajouté avec succès!");

            request.setAttribute("listeMessageSucces", listeMessageSucces);

            HttpSession session = request.getSession(false);
            // sauvegarde du message dans la session
            session.setAttribute("listeMessageSucces", listeMessageSucces);
            response.sendRedirect("Routes?page=equipes");

        } catch (LigueException e) {

            List listeMessageErreur = new LinkedList();
            listeMessageErreur.add(e.toString());

            request.setAttribute("listeMessageErreur", listeMessageErreur);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/ajouterEquipe.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }
    }


    public void traiterAjoutMatch(HttpServletRequest request,
                                  HttpServletResponse response) throws ServletException, IOException {

        java.sql.Date matchDate = null;
        Timestamp matchHeure = null;
        String nomEquipeLocale = null;
        String nomEquipeVisiteur = null;

        try {

            if (request.getParameter("matchDate") == null) {
                throw new LigueException("Veuillez entrer la date du match");
            } else {
                try {
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    Date tmpDate = df.parse(request.getParameter("matchDate"));
                    matchDate = new java.sql.Date(tmpDate.getTime());
                } catch (ParseException e) {
                    throw new LigueException("Veuillez entrer une heure de match valide");
                }
            }
            if (request.getParameter("matchHeure") == null) {
                throw new LigueException("Veuillez entrer l'heure du match");
            } else {
                try {
                    DateFormat tf = new SimpleDateFormat("hh:mm");
                    Date tmpHeure = tf.parse(request.getParameter("matchHeure"));
                    matchHeure = new Timestamp(tmpHeure.getTime());
                } catch (ParseException e) {
                    throw new LigueException("Veuillez entrer une heure de match valide");
                }
            }
            if (request.getParameter("equipeLocale") == null) {
                throw new LigueException("Veuillez sélectionner l'équipe locale");
            } else {
                nomEquipeLocale = request.getParameter("equipeLocale");
            }
            if (request.getParameter("equipeVisiteur") == null) {
                throw new LigueException("Veuillez sélectionner l'équipe visiteur");
            } else {
                nomEquipeVisiteur = request.getParameter("equipeVisiteur");
            }

            GestionLigue gestionLigue = (GestionLigue) request
                    .getSession().getAttribute("ligue");


            // exécuter la transaction
            synchronized (gestionLigue) {
                gestionLigue.creerMatch(matchDate, matchHeure, nomEquipeLocale, nomEquipeVisiteur);
            }

            List listeMessageSucces = new LinkedList();
            listeMessageSucces.add("Le match a été ajouté avec succès!");

            request.setAttribute("listeMessageSucces", listeMessageSucces);

            HttpSession session = request.getSession(false);
            // sauvegarde du message dans la session
            session.setAttribute("listeMessageSucces", listeMessageSucces);
            response.sendRedirect("Routes?page=matchs");

        } catch (LigueException e) {

            List listeMessageErreur = new LinkedList();
            listeMessageErreur.add(e.toString());

            request.setAttribute("listeMessageErreur", listeMessageErreur);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/ajouterMatch.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }
    }

    public void traiterAjoutResultatMatch(HttpServletRequest request,
                                          HttpServletResponse response) throws ServletException, IOException {
        try {

            java.sql.Date matchDate;
            Timestamp matchHeure;
            String nomEquipeLocale;
            String nomEquipeVisiteur;
            int pointsLocal;
            int pointsVisiteur;

            if (request.getParameter("matchDate") == null) {
                throw new LigueException("Veuillez entrer la date du match");
            } else {
                try {
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    Date tmpDate = df.parse(request.getParameter("matchDate"));
                    matchDate = new java.sql.Date(tmpDate.getTime());
                } catch (ParseException e) {
                    throw new LigueException("Veuillez entrer une heure de match valide");
                }
            }
            if (request.getParameter("matchHeure") == null) {
                throw new LigueException("Veuillez entrer l'heure du match");
            } else {
                try {
                    DateFormat tf = new SimpleDateFormat("hh:mm");
                    Date tmpHeure = tf.parse(request.getParameter("matchHeure"));
                    matchHeure = new Timestamp(tmpHeure.getTime());
                } catch (ParseException e) {
                    throw new LigueException("Veuillez entrer une heure de match valide");
                }
            }
            if (request.getParameter("equipeLocale") == null) {
                throw new LigueException("Veuillez sélectionner l'équipe locale");
            } else {
                nomEquipeLocale = request.getParameter("equipeLocale");
            }
            if (request.getParameter("equipeVisiteur") == null) {
                throw new LigueException("Veuillez sélectionner l'équipe visiteur");
            } else {
                nomEquipeVisiteur = request.getParameter("equipeVisiteur");
            }
            if (request.getParameter("pointsLocal") == null || request.getParameter("pointsLocal") == "0") {
                throw new LigueException("Veuiller entrer un pointage pour l'équipe locale");
            } else {
                try {
                    pointsLocal = Integer.parseInt(request.getParameter("pointsLocal"));
                } catch (NumberFormatException e) {
                    throw new LigueException("Veuiller entrer un pointage valide pour l'équipe locale");
                }
            }
            if (request.getParameter("pointsVisiteur") == null || request.getParameter("pointsVisiteur") == "0") {
                throw new LigueException("Veuiller entrer un pointage pour l'équipe visiteur");
            } else {
                try {
                    pointsVisiteur = Integer.parseInt(request.getParameter("pointsVisiteur"));
                } catch (NumberFormatException e) {
                    throw new LigueException("Veuiller entrer un pointage valide pour l'équipe visiteur");
                }
            }

            GestionLigue gestionLigue = (GestionLigue) request
                    .getSession().getAttribute("ligue");


            // exécuter la transaction
            synchronized (gestionLigue) {
                gestionLigue.entrerResultatMatch(matchDate, matchHeure, nomEquipeLocale,
                        nomEquipeVisiteur, pointsLocal, pointsVisiteur);
            }

            List listeMessageSucces = new LinkedList();
            listeMessageSucces.add("Le résultat du match a été ajouté avec succès!");

            request.setAttribute("listeMessageSucces", listeMessageSucces);

            HttpSession session = request.getSession(false);
            // sauvegarde du message dans la session
            session.setAttribute("listeMessageSucces", listeMessageSucces);
            response.sendRedirect("Routes?page=matchs");

        } catch (LigueException e) {

            List listeMessageErreur = new LinkedList();
            listeMessageErreur.add(e.toString());

            request.setAttribute("listeMessageErreur", listeMessageErreur);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/ajouterResultatMatch.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }
    }

    public void traiterAffecterArbitres(HttpServletRequest request,
                                        HttpServletResponse response) throws ServletException, IOException {
        try {

            java.sql.Date matchDate;
            Timestamp matchHeure;
            String nomEquipeLocale;
            String nomEquipeVisiteur;
            String[] arbitres;

            if (request.getParameter("matchDate") == null) {
                throw new LigueException("Veuillez entrer la date du match");
            } else {
                try {
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    Date tmpDate = df.parse(request.getParameter("matchDate"));
                    matchDate = new java.sql.Date(tmpDate.getTime());
                } catch (ParseException e) {
                    throw new LigueException("Veuillez entrer une heure de match valide");
                }
            }
            if (request.getParameter("matchHeure") == null) {
                throw new LigueException("Veuillez entrer l'heure du match");
            } else {
                try {
                    DateFormat tf = new SimpleDateFormat("hh:mm");
                    Date tmpHeure = tf.parse(request.getParameter("matchHeure"));
                    matchHeure = new Timestamp(tmpHeure.getTime());
                } catch (ParseException e) {
                    throw new LigueException("Veuillez entrer une heure de match valide");
                }
            }
            if (request.getParameter("equipeLocale") == null) {
                throw new LigueException("Veuillez sélectionner l'équipe locale");
            } else {
                nomEquipeLocale = request.getParameter("equipeLocale");
            }
            if (request.getParameter("equipeVisiteur") == null) {
                throw new LigueException("Veuillez sélectionner l'équipe visiteur");
            } else {
                nomEquipeVisiteur = request.getParameter("equipeVisiteur");
            }
            if (request.getParameter("arbitres") == null) {
                throw new LigueException("Veuillez sélectionne au moins un arbitre");
            } else {
                arbitres = request.getParameterValues("arbitres");

                if (arbitres.length == 0) {
                    throw new LigueException("Veuillez sélectionne au moins un arbitre");
                } else if (arbitres.length > 4) {
                    throw new LigueException("Le nombre maximale d'arbitre est de 4.");
                }
            }

            GestionLigue gestionLigue = (GestionLigue) request
                    .getSession().getAttribute("ligue");

            // exécuter la transaction
            synchronized (gestionLigue) {
                gestionLigue.arbitrerMatch(matchDate, matchHeure, nomEquipeLocale,
                        nomEquipeVisiteur, arbitres);
            }

            List listeMessageSucces = new LinkedList();
            listeMessageSucces.add("Les arbitres ont étés affectés au match avec succès!");

            request.setAttribute("listeMessageSucces", listeMessageSucces);

            HttpSession session = request.getSession(false);
            // sauvegarde du message dans la session
            session.setAttribute("listeMessageSucces", listeMessageSucces);
            response.sendRedirect("Routes?page=matchs");

        } catch (LigueException e) {

            List listeMessageErreur = new LinkedList();
            listeMessageErreur.add(e.toString());

            request.setAttribute("listeMessageErreur", listeMessageErreur);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/affecterArbitres.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }
    }

    public void traiterFiltrerMatchDate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


        try {

            Date aPartirDe;

            if (request.getParameter("aPartirDe") == null) {
                throw new LigueException("Veuillez entrer la date du match");
            } else {
                try {
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    Date tmpDate = df.parse(request.getParameter("aPartirDe"));
                    aPartirDe = new java.sql.Date(tmpDate.getTime());
                } catch (ParseException e) {
                    throw new LigueException("Veuiller indiquer à partir de quel date vous voulez afficher les matchs.");
                }
            }

            HttpSession session = request.getSession(false);
            // sauvegarde du message dans la session
            session.setAttribute("aPartirDe", aPartirDe);
            response.sendRedirect("Routes?page=matchs");

        } catch (LigueException e) {

            List listeMessageErreur = new LinkedList();
            listeMessageErreur.add(e.toString());

            request.setAttribute("listeMessageErreur", listeMessageErreur);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/matchs.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }

    }

    public void checkSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession().getAttribute("ligue") == null) {

            List listeMessageErreur = new LinkedList();
            listeMessageErreur.add("Veuillez vous connecter pour poursuivre.");

            request.setAttribute("listeMessageErreur", listeMessageErreur);

            // Retour au login
            RequestDispatcher dispatcher;
            dispatcher = request.getRequestDispatcher("Login");
            dispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}