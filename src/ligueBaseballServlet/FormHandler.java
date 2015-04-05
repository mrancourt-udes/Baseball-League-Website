package ligueBaseballServlet;

import ligueBaseball.GestionLigue;
import ligueBaseball.LigueException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer etat = (Integer) request.getSession().getAttribute("etat");

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
        else if (request.getParameter("renouveler") != null) {
            //traiterAjoutEquipe(request, response);
        }
        else if (request.getParameter("emprunter") != null) {
            //traiterAjoutMatch(request, response);
        }
        else if (request.getParameter("selectionMembre") != null) {
            //traiterAjoutArbitre(request, response);
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
    public void traiterAjoutJoueur(HttpServletRequest request,
                                   HttpServletResponse response) throws ServletException, IOException {
        try {

            String equipe;
            String terrain;

            if (request.getParameter("inputEquipe") == null) {
                throw new LigueException("Veuillez entrer un nom d'équipe");
            } else {
                equipe = request.getParameter("inputEquipe");
            }
            if (request.getParameter("inputTerrain") == null) {
                throw new LigueException("Veuillez choisir un terrain");
            } else {
                terrain = request.getParameter("inputTerrain");
            }

            GestionLigue gestionLigue = new GestionLigue();

            // TODO : Initialise un inventaireManager
            /*GestionBibliotheque biblio = (GestionBibliotheque) request
                    .getSession().getAttribute("biblio");
            // exécuter la maj en utilisant synchronized pour s'assurer
            // que le thread du servlet est le seul à exécuter une transaction
            // sur biblio
            synchronized (biblio) {
                biblio.gestionPret.retourner(idLivre, dateRetour);
            }*/

            gestionLigue.creerEquipe(equipe, terrain);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/equipes.jsp");
            dispatcher.forward(request, response);
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

            GestionLigue gestionLigue = new GestionLigue();

            // exécuter la transaction
            synchronized (gestionLigue) {
                gestionLigue.creerArbitre(prenom, nom);
            }

            List listeMessageSucces = new LinkedList();
            listeMessageSucces.add("L'arbitre «" + prenom + " " + nom + "» a été ajouté avec succès!");

            request.setAttribute("listeMessageSucces", listeMessageSucces);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/arbitres.jsp");

            dispatcher.forward(request, response);

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

            if (request.getParameter("inputEquipe") == null) {
                throw new LigueException("Veuillez entrer un nom d'équipe");
            } else {
                equipe = request.getParameter("inputEquipe");
            }
            if (request.getParameter("inputTerrain") == null) {
                throw new LigueException("Veuillez entrer un nom de terrain");
            } else {
                terrain = request.getParameter("inputTerrain");
            }

            GestionLigue gestionLigue = new GestionLigue();

            // exécuter la transaction
            synchronized (gestionLigue) {
                gestionLigue.creerEquipe(equipe, terrain);
            }

            List listeMessageSucces = new LinkedList();
            listeMessageSucces.add("L'équipe «" + equipe +"» associée au terrain «" + terrain + "» a été ajouté avec succès!");

            request.setAttribute("listeMessageSucces", listeMessageSucces);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/equipes.jsp");

            dispatcher.forward(request, response);

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
        try {

            java.sql.Date matchDate;
            Timestamp matchHeure;
            String nomEquipeLocale;
            String nomEquipeVisiteur;

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

            GestionLigue gestionLigue = new GestionLigue();

            // exécuter la transaction
            synchronized (gestionLigue) {
                gestionLigue.creerMatch(matchDate, matchHeure, nomEquipeLocale, nomEquipeVisiteur);
            }

            List listeMessageSucces = new LinkedList();
            listeMessageSucces.add("Le match a été ajouté avec succès!");

            request.setAttribute("listeMessageSucces", listeMessageSucces);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/matchs.jsp");

            dispatcher.forward(request, response);

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
