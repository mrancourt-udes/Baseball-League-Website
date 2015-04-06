package ligueBaseballServlet;


import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import ligueBaseball.GestionLigue;
import ligueBaseball.LigueException;

/**
 * Classe pour login système de gestion de bibliothèque
 * <P>
 * Système de gestion de bibliothèque &copy; 2004 Marc Frappier, Université de
 * Sherbrooke
 */

public class Login extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        // fermer la session si elle a déjà été ouverte lors d'un appel
        // précédent
        // survient lorsque l'usager recharge la page login.jsp
        if (session.getAttribute("etat") != null) {
            // pour déboggage seulement : afficher no session et information
            System.out
                    .println("GestionLigue: session déja crée; id="
                            + session.getId());
            // la méthode invalidate appelle le listener
            // BiblioSessionListener; cette classe est chargée lors du
            // démarrage de
            // l'application par le serveur (voir le fichier web.xml)
            session.invalidate();
            session = request.getSession();
            System.out.println("GestionLigue: session invalidée");
        }

        // lecture des paramètres du formulaire login.jsp
        String userIdOracle = request.getParameter("userIdBD");
        String motDePasseOracle = request.getParameter("motDePasseBD");
        String serveur = request.getParameter("serveur");
        String adresseIP = request.getParameter("adresseIP");
        String bd = request.getParameter("bd");

        // ouvrir une connexion avec la BD et créer les gestionnaires
        System.out.println("Login: session id="
                + session.getId());

        // TODO : Gerer la connexion
        try {
            GestionLigue gestionLigue = new GestionLigue();

            // afficher le menu membre en appelant la page selectionMembre.jsp
            // tous les JSP sont dans /WEB-INF/
            // ils ne peuvent pas être appelés directement par l'utilisateur
            // seulement par un autre JSP ou un servlet
            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/accueil.jsp");
            dispatcher.forward(request, response);
            session.setAttribute("etat", new Integer(LigueConstantes.CONNECTE));

        } catch (LigueException e) {
            List listeMessageErreur = new LinkedList();
            listeMessageErreur.add(e.toString());

            request.setAttribute("listeMessageErreur", listeMessageErreur);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("Login");
            dispatcher.forward(request, response);
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

} // class