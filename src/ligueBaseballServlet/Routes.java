package ligueBaseballServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vonziper on 2015-04-03.
 */
public class Routes extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = request.getParameter("page");

        RequestDispatcher dispatcher = null;

        dispatcher = request.getRequestDispatcher("/WEB-INF/"+page+".jsp");

        dispatcher.forward(request, response);
    }
}
