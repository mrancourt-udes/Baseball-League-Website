package ligueBaseballServlet;

import ligueBaseball.GestionLigue;
import ligueBaseball.LigueException;
import ligueBaseball.LigueIO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vonziper on 2015-04-06.
 */

public class Export extends HttpServlet {

    // dossier contenant les fichiers xml des equipes
    private static final String UPLOAD_DIRECTORY = "XML";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            String equipe;

            if (request.getParameter("equipe") == null) {
                throw new LigueException("Veuillez sélectionner une équipe.");
            } else {
                equipe = request.getParameter("equipe");
            }

            LigueIO ligueIO = new LigueIO();

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


            //response.sendRedirect("Routes?page=exporter");

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
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + nomFichier + ".xml");

        File file = new File(pathFichier+ "/" +nomFichier + ".xml");
        FileInputStream fileIn = new FileInputStream(file);
        ServletOutputStream out = response.getOutputStream();

        byte[] outputByte = new byte[4096];
        //copy binary contect to output stream
        while(fileIn.read(outputByte, 0, 4096) != -1)
        {
            out.write(outputByte, 0, 4096);
        }
        fileIn.close();
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
