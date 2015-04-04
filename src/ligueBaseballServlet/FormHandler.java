package ligueBaseballServlet;

import ligueBaseball.InventaireManager;
import ligueBaseball.LigueBaseballException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Martin Rancourt on 2015-04-04.
 */
// TODO : Ajouter toutes les actions possibles
public class FormHandler extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer etat = (Integer) request.getSession().getAttribute("etat");
        if (etat == null) {
            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
        else if (request.getParameter("ajouterJoueur") != null) {
            traiterAjoutJoueur(request, response);
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
                    .getRequestDispatcher(request.getRequestURI());
            dispatcher.forward(request, response);
        }
    }

    public void traiterAjoutJoueur(HttpServletRequest request,
                                   HttpServletResponse response) throws ServletException, IOException {
        try {

            String equipe;
            String terrain;

            if (request.getParameter("inputEquipe") == null) {
                throw new LigueBaseballException("Veuillez entrer un nom d'équipe");
            } else {
                equipe = request.getParameter("inputEquipe");
            }
            if (request.getParameter("inputTerrain") == null) {
                throw new LigueBaseballException("Veuillez choisir un terrain");
            } else {
                terrain = request.getParameter("inputTerrain");
            }

            InventaireManager inv = new InventaireManager();

            // TODO : Initialise un inventaireManager
            /*GestionBibliotheque biblio = (GestionBibliotheque) request
                    .getSession().getAttribute("biblio");
            // exécuter la maj en utilisant synchronized pour s'assurer
            // que le thread du servlet est le seul à exécuter une transaction
            // sur biblio
            synchronized (biblio) {
                biblio.gestionPret.retourner(idLivre, dateRetour);
            }*/

            inv.creerEquipe(equipe, terrain);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/equipes.jsp");
            dispatcher.forward(request, response);
        } catch (LigueBaseballException e) {
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


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
