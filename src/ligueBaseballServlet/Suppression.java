package ligueBaseballServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import ligueBaseball.GestionLigue;
import ligueBaseball.LigueException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by vonziper on 2015-04-05.
 */
public class Suppression extends HttpServlet {

    private JSONObject jsonResponse = new JSONObject();
    private GestionLigue gestionLigue = new GestionLigue();
    private Integer id;
    private String token = null;
    private String action = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Set les headers pour la reponse json
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "nocache");
        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();

        if (request.getParameter("id") == null) {
            //throw new LigueException("Element invalide");
        } else {
            id = Integer.parseInt(request.getParameter("id"));
        }
        if (request.getParameter("token") == null) {
            //throw new LigueException("Token invalide");
        } else {
            token = request.getParameter("token");
        }
        if (request.getParameter("action") == null) {
            //throw new LigueException("Action invalide");
        } else {
            action = request.getParameter("action");
        }

        System.out.println(action);

        switch (action) {
            case "supprimerJoueur" :
                traiterSuppressionJoueur(request, response);
                break;
            case "supprimerEquipe" :
                traiterSuppressionEquipe(request, response);
                break;
            case "supprimerArbitre" :
                traiterSuppressionArbitre(request, response);
                break;
            default:
                actionInvalide(request, response);
        }

        // Envoie de la reponse json
        out.print(jsonResponse.toString());
    }

    public void traiterSuppressionJoueur(
            HttpServletRequest request, HttpServletResponse response) throws IOException{

        Boolean supprimerJoueurParticipe = false;
        Boolean supprimerJoueurFaitPartie = false;

        try {

            if ( Boolean.parseBoolean(request.getParameter("supprimerJoueurParticipe"))) {
                supprimerJoueurParticipe = true;
            }
            if (Boolean.parseBoolean(request.getParameter("supprimerJoueurFaitPartie"))) {
                supprimerJoueurFaitPartie = true;
            }

            if (gestionLigue.supprimerJoueur(id, supprimerJoueurParticipe, supprimerJoueurFaitPartie)) {
                jsonResponse.put("status", "success");
                jsonResponse.put("msg", "Le joueur a été supprimé avec succès!");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("msg", "Une erreur inatendue est survenue lors de la suppression du joueur");
            }

        } catch (LigueException e) {
            jsonResponse.put("status", "error");
            jsonResponse.put("msg", e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }
    }

    public void traiterSuppressionArbitre(
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {

            if (gestionLigue.supprimerArbitre(id)) {
                jsonResponse.put("status", "success");
                jsonResponse.put("msg", "L'arbitre a été supprimé avec succès!");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("msg", "Une erreur inatendue est survenue lors de la suppression de l'arbitre");
            }

        } catch (LigueException e) {
            jsonResponse.put("status", "error");
            jsonResponse.put("msg", e.toString());
        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }

    }

    public void traiterSuppressionEquipe(
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {

            if (gestionLigue.supprimerEquipe(id)) {
                jsonResponse.put("status", "success");
                jsonResponse.put("msg", "L'équipe a été supprimé avec succès!");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("msg", "Une erreur inatendue est survenue lors de la suppression de l'équipe");
            }

        } catch (LigueException e) {
            jsonResponse.put("status", "error");
            jsonResponse.put("msg", e.toString());
        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }

    }

    public void actionInvalide(
            HttpServletRequest request, HttpServletResponse response) {

        jsonResponse.put("status", "error");
        jsonResponse.put("msg", "Impossible de supprimer cet élément");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
