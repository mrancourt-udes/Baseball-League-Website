package ligueBaseball;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jdk.internal.org.xml.sax.Attributes;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Upload extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // location to store file uploaded
    String uploadPath;
    private static final String UPLOAD_DIRECTORY = "XML";

    // upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // checks if the request actually contains upload file
        if (!ServletFileUpload.isMultipartContent(request)) {
            // if not, we stop here
            PrintWriter writer = response.getWriter();
            writer.println("Error: Form must has enctype=multipart/form-data.");
            writer.flush();
            return;
        }

        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // sets maximum size of upload file
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // sets maximum size of request (include file + form data)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // constructs the directory path to store upload file
        // this path is relative to application's directory
        String uploadPath = request.getSession().getServletContext().getRealPath("/") + UPLOAD_DIRECTORY;
        uploadPath = uploadPath.replace("out/artifacts/tp4/", "");

        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // parses the request's content to extract file data
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields

                    if (!item.isFormField()) {

                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);

                        // saves the file on disk
                        item.write(storeFile);

                        LigueIO ligueIO = new LigueIO();
                        ligueIO.importer(filePath);

                        List listeMessageSucces = new LinkedList();
                        listeMessageSucces.add("Le fichier XML à été importé avec succès!");

                        request.setAttribute("listeMessageSucces", listeMessageSucces);

                        HttpSession session = request.getSession(false);
                        //save message in session
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