package ligueBaseballServlet;
import javax.servlet.http.*;

import ligueBaseball.GestionLigue;
import ligueBaseball.GestionLigue;

import java.sql.*;

/** Classe pour gestion des sessions
 *  <P>
 * Système de gestion de bibliothèque
 *  &copy; 2004 Marc Frappier, Université de Sherbrooke
 */

public class LigueSessionListener implements HttpSessionListener
{
	public void sessionCreated(HttpSessionEvent se)
	{
	}

	public void sessionDestroyed(HttpSessionEvent se)
	{
		System.out.println("LigueSessionListener " + se.getSession().getId());
		GestionLigue ligue = (GestionLigue) se.getSession().getAttribute("ligue");
		if (ligue != null)
		{
			System.out.println("connexion =" + ligue.db);
			try {ligue.fermer();}
			catch (SQLException e)
			{
				System.out.println("Impossible de fermer la connexion");
				e.printStackTrace();
			}
		}
		else {
			System.out.println("ligue inaccessible.");
		}
	}
}
