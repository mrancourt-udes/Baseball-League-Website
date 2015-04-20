package ligueBaseballServlet;

import ligueBaseball.GestionLigue;
import ligueBaseball.LigueException;
import ligueBaseball.LigueIO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vonziper on 2015-04-06.
 */

public class Export extends HttpServlet {

    // dossier contenant les fichiers xml des equipes
    private static final String UPLOAD_DIRECTORY = "XML";
    private static final String EXT = ".xml";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // On vérifie qu'il y a bien une session de créé.
        checkSession(request, response);

        try {

            String equipe;

            if (request.getParameter("equipe") == null) {
                throw new LigueException("Veuillez sélectionner une équipe.");
            } else {
                equipe = request.getParameter("equipe");
            }

            GestionLigue ligue = (GestionLigue) request.getSession().getAttribute("ligue");
            LigueIO ligueIO = new LigueIO(ligue);

            // recuperation du path de destination
            String filePath = request.getSession().getServletContext().getRealPath("/") + UPLOAD_DIRECTORY;
            filePath = filePath.replace("out/artifacts/tp4/", "");

            // exécuter la transaction
            synchronized (ligueIO) {
                ligueIO.exporter(equipe, filePath);
            }

            // L'export a réussi sans erreurs
            List listeMessageSucces = new LinkedList();
            listeMessageSucces.add("L'exportation de l'équipe a été effectuée avec succès!");

            request.setAttribute("listeMessageSucces", listeMessageSucces);

            HttpSession session = request.getSession(false);

            // sauvegarde du message dans la session
            session.setAttribute("listeMessageSucces", listeMessageSucces);

            // On tente de forcer le téléchargement du fichier
            forceDownload(request, response, filePath, equipe);

        } catch (LigueException e) {

            // Une erreur s'est produite lors de l'exportation
            List listeMessageErreur = new LinkedList();
            listeMessageErreur.add(e.toString());

            request.setAttribute("listeMessageErreur", listeMessageErreur);

            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/exporter.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }
    }

    public void forceDownload(HttpServletRequest request, HttpServletResponse response, String pathFichier, String nomFichier) throws IOException {

        response.setContentType("application/xml");
        response.setHeader("Content-Disposition","attachment;filename="+ nomFichier + EXT);

        File file = new File(pathFichier+ "/" +nomFichier + EXT);

        InputStream in = new FileInputStream(file);
        BufferedInputStream bin = new BufferedInputStream(in);
        DataInputStream din = new DataInputStream(bin);
        PrintWriter out = response.getWriter();

        while(din.available() > 0){
            out.print(din.readLine());
            out.print("\n");
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
