package ligueBaseball;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class Upload extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // dossier contenant les fichiers xml des equipes
    private static final String UPLOAD_DIRECTORY = "XML";

    // parametres d'upload
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // verification des fichiers uploades dans le formulaire
        if (!ServletFileUpload.isMultipartContent(request)) {
            // aucun fichier uploadé
            PrintWriter writer = response.getWriter();
            writer.println("Error: Form must has enctype=multipart/form-data.");
            writer.flush();
            return;
        }

        // configuration des parametres d'upload
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // recuperation du path de destination
        String uploadPath = request.getSession().getServletContext().getRealPath("/") + UPLOAD_DIRECTORY;
        uploadPath = uploadPath.replace("out/artifacts/tp4/", "");

        // creation du dossier si inexistant
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // extration des informations sur les fichiers uploades
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // parcours des champs
                for (FileItem item : formItems) {

                    // Sauvegarde du fichier s'il s'agit d'un type file
                    if (!item.isFormField()) {

                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);

                        if (fileName.isEmpty()) {
                            throw new LigueException("Aucun fichier sélectionner. <br>Veuillez vous assurer de sélectionner le fichier XML de votre équipe.");
                        }

                        // sauvegarde du fichier sur le disque
                        item.write(storeFile);

                        GestionLigue ligue = (GestionLigue) request.getSession().getAttribute("ligue");
                        LigueIO ligueIO = new LigueIO(ligue);
                        ligueIO.importer(filePath);

                        List listeMessageSucces = new LinkedList();
                        listeMessageSucces.add("Le fichier XML à été importé avec succès!");

                        request.setAttribute("listeMessageSucces", listeMessageSucces);

                        HttpSession session = request.getSession(false);

                        // sauvegarde du message dans la session
                        session.setAttribute("listeMessageSucces", listeMessageSucces);
                        response.sendRedirect("Routes?page=importer");

                    }
                }
            }

        } catch (LigueException e) {

            List listeMessageSucces = new LinkedList();
            listeMessageSucces.add(e.toString());

            request.setAttribute("listeMessageErreur", listeMessageSucces);

            HttpSession session = request.getSession(false);
            //save message in session
            session.setAttribute("listeMessageErreur", listeMessageSucces);
            response.sendRedirect("Routes?page=importer");

        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
                    .toString());
        }
    }
}