package ligueBaseballServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import ligueBaseball.GestionLigue;

/**
 * Created by vonziper on 2015-04-04.
 */
public class Equipes extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request
                .getRequestDispatcher("/WEB-INF/equipes.jsp");

        GestionLigue gestionLigue = (GestionLigue) request
                .getSession().getAttribute("gestionLigue");

        dispatcher.forward(request, response);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
