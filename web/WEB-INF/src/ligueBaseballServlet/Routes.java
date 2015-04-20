package ligueBaseballServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vonziper on 2015-04-03.
 */
public class Routes extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher;
        String route;

        if (request.getSession().getAttribute("ligue") == null) {
            // Retour au login
            route = "login.jsp";
        } else if (request.getParameter("home") != null) {
            route = "/WEB-INF/accueil.jsp";
        } else {
            String page = request.getParameter("page");
            route = "/WEB-INF/" +page+".jsp";
        }

        dispatcher = request.getRequestDispatcher(route);
        dispatcher.forward(request, response);
    }

}
